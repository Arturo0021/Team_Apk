package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import mx.triplei.Utilerias;
import mx.smartteam.entities.SodCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Sod {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "/";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    
    public static mx.smartteam.entities.Sod getOne(Context context, mx.smartteam.entities.ExhibicionConfig econfig, mx.smartteam.entities.Pop pop){
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.Sod EntitySod = new mx.smartteam.entities.Sod();
        
        try{
        String qquery = "";
        qquery = "SELECT Id, IdExhibicionConfig, Valor, IdVisita, Comentario FROM Sod WHERE idvisita = " + pop.IdVisita + " AND IdExhibicionConfig = " + econfig.Id +" ;" ;
        
        Cursor cursor = db.rawQuery(qquery, null);
        
        if(cursor.moveToFirst()){
            do{
                EntitySod.Id = cursor.getInt(0);
                EntitySod.IdExhibicionConfig = cursor.getInt(1);
                EntitySod.Valor = cursor.getDouble(2);
                EntitySod.idVisita = cursor.getInt(3);
                EntitySod.Comentario = cursor.getString(4);
            }while(cursor.moveToNext());
        }
        
        db.close();
        
        } catch(Exception ex) {
            ex.getMessage();
        }
        return EntitySod;
    }
    
    public static String Insert(Context context, mx.smartteam.entities.Sod sod, mx.smartteam.entities.Pop pop, mx.smartteam.entities.ExhibicionConfig econfig){
        db = (new BaseDatos(context)).getWritableDatabase();
        String result = "OK";
            try{
                ContentValues values = new ContentValues();
                values.put("IdExhibicionConfig", econfig.Id);
                values.put("Valor", sod.Valor);
                values.put("Comentario", sod.Comentario);
                values.put("IdVisita", pop.IdVisita);
                db.insert("Sod", null, values );
            }catch(Exception ex){
                result= "Error";
            }
        db.close();
        return result;
    }
    
       public static String Update(Context context, mx.smartteam.entities.Sod sod){
        db = (new BaseDatos(context)).getWritableDatabase();
        String result = "OK";
            try{
                ContentValues values = new ContentValues();
                values.put("Valor", sod.Valor);
                values.put("Comentario", sod.Comentario);
                values.put("fechacrea", Utilerias.getFechaUnix());
                values.put( "statussync", 0);
                db.update("Sod", values," id = " + sod.Id, null );
            }catch(Exception ex){
                result= "Error";
            }
        db.close();
        return result;
    }
    
    public static mx.smartteam.entities.SodCollection 
        getAllnotUpload(Context context, mx.smartteam.entities.PopVisita visita) {
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.SodCollection sodCollection = new mx.smartteam.entities.SodCollection();
           
        String qquery = "SELECT Id, IdExhibicionConfig, Valor, IdVisita, Comentario,datetime(fechacrea, 'unixepoch', 'localtime')"
                        + " FROM Sod Where StatusSync = 0 AND IdVisita = ? ";
        try{
            Cursor cursor = db.rawQuery(qquery, new String[]{visita.Id.toString()});
                    
            if(cursor.moveToFirst()){
                do{
                    mx.smartteam.entities.Sod entitySod = new mx.smartteam.entities.Sod();
                    entitySod.Id = cursor.getInt(0);
                    entitySod.IdExhibicionConfig = cursor.getInt(1);
                    entitySod.Valor = cursor.getDouble(2);
                    entitySod.idVisita = visita.Id;
                    entitySod.Comentario = cursor.getString(4);
                    entitySod.Fecha = cursor.getString(5);
                    sodCollection.add(entitySod);
                }while(cursor.moveToNext());
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        db.close();
        return sodCollection;
    
    }
        
    public static void UpdateSodByVisita(Context context, mx.smartteam.entities.PopVisita visita) {
        db = (new BaseDatos(context)).getWritableDatabase();
        
        try{
            ContentValues values = new ContentValues();
            values.put( "statussync", 1);
            db.update("Sod", values, " IdVisita = " + visita.Id , null);
            db.close();
        
        }catch(Exception ex){
            ex.getMessage().toString();
        }
        
    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        int existe = 0;
        String query = "SELECT COUNT(1) FROM Sod WHERE IdVisita = " + pop.getIdVisita() + ";";
        
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            existe = cursor.getInt(0);
        }
        
        return existe;
    }
    
    public static class Upload {
    
        public static String Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.SodCollection sodCollection) throws Exception{
            String strResult = "OK";
            METHOD_NAME = "/SodInsert";
            
            ServiceURI service = new ServiceURI();
            
            jsonString = new JSONStringer()
                .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject();
            
            jsonString.key("SodCollection").array();
                for(mx.smartteam.entities.Sod sod : sodCollection){
                    /*
                    ParameterCollection.Add(new Integra.Data.Parameter("var_Fecha",i.Fecha));
                    */
                    
                    jsonString.object()
                            .key("IdExhibicionConfig").value(sod.IdExhibicionConfig.toString())
                            .key("Valor").value(sod.Valor.toString())
                            .key("Comentarios").value(sod.Comentario.toString())
                            .key("Fecha").value(sod.Fecha.toString())
                            .endObject();
                            
                }
                    jsonString.endArray();
            jsonString.endObject();
                  
            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            if(!jsonResult.getString("SodInsertResult").equals("OK") ){
                throw new Exception("Error en el servicio [SodInsert]");
            }
            return strResult;
        }
    
    }
    










































































}
