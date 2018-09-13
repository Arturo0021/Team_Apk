package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class MetaCadena {
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";//Nombre del método
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
        
    public static class Download{
    
        public static mx.smartteam.entities.MetaCadenaCollection 
        getAllMetaCadena(mx.smartteam.entities.Proyecto proyecto , String Time) throws Exception {
            mx.smartteam.entities.MetaCadenaCollection metaCollection = new mx.smartteam.entities.MetaCadenaCollection();
            
            ServiceURI service = new ServiceURI();
            
            METHOD_NAME = "/GetMsjMetaCadenas";
                
                jsonString = new JSONStringer().object()
                            .key("Proyecto").object()
                                .key("Id").value(proyecto.Id.toString())
                                .key("Ufechadescarga").value(Time)
                                .endObject()
                            .endObject();
                
                    strEntity = new StringEntity(jsonString.toString());
                    
                    jsonResult = service.HttpGet(METHOD_NAME, strEntity);
                     
                    jsonArray = jsonResult.getJSONArray("GetMsjMetaCadenasResult");//Cambiar    
                     
                    for(int i = 0; i < jsonArray.length(); ++i ){
     
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mx.smartteam.entities.MetaCadena meta = new mx.smartteam.entities.MetaCadena();
                        meta.Id = jsonObject.getInt("id");
                        meta.IdProyecto = proyecto.Id;
                        meta.Mensaje = jsonObject.getString("mensaje");
                        meta.Tipo = jsonObject.getString("tipo");
                        meta.DiaCaptura = jsonObject.getInt("diaCaptura");
                        
                        metaCollection.add(meta);
                        
                    } 
            return metaCollection;
        }
    
    
    }
    
    
    public static void Insert(Context context , mx.smartteam.entities.MetaCadena metacadena){
        db = (new BaseDatos(context)).getWritableDatabase();
        try{
        
            Cursor cursor = db.rawQuery("SELECT COUNT(1) FROM MetaMensaje WHERE id = " + metacadena.Id, null);
            
            String qquery = " INSERT INTO MetaMensaje "
                            + "(id,idproyecto,mensaje, tipo, diaCaptura ) VALUES ("
                            + metacadena.Id.toString() + " , "
                            + metacadena.IdProyecto + " , '"
                            + metacadena.Mensaje + "' , '"
                            + metacadena.Tipo + "' , "
                            + metacadena.DiaCaptura
                            + ");";
            
            if(cursor.moveToFirst()) {
                int contador  = cursor.getInt(0);
                if(contador == 0){
                    
                    db.execSQL(qquery);
                    
                } else{
                    ContentValues values = new ContentValues();
                    values.put("mensaje", metacadena.Mensaje);
                    values.put("tipo",metacadena.Tipo);
                db.update("MetaMensaje", values, " Id= " + metacadena.Id.toString(), null);
                }
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        db.close();
    }
    
    
    public static mx.smartteam.entities.MetaCadenaCollection 
        getMensajesXMes(Context context , int bandera ,mx.smartteam.entities.Proyecto proyecto){
            db = ( new BaseDatos(context)) .getWritableDatabase();
            mx.smartteam.entities.MetaCadenaCollection metaCollection = new mx.smartteam.entities.MetaCadenaCollection();
            String subquery = null;
            if(bandera >= 30){
                subquery = "mes_completo";
            }else{
            //solo febrero o año bisiesto
                subquery = "mes_febrero";
            }
            
            String qquery = "Select id, mensaje, tipo, diacaptura FROM MetaMensaje WHERE tipo ='" + subquery +"' AND idproyecto = "+ proyecto.Id +" ;";
            
            Cursor cursor = db.rawQuery(qquery, null);
            
            if(cursor.moveToFirst()){
                do{
                    mx.smartteam.entities.MetaCadena meta = new mx.smartteam.entities.MetaCadena();
                    meta.Id = cursor.getInt(0);
                    meta.Mensaje = cursor.getString(1);
                    meta.Tipo = cursor.getString(2);
                    meta.DiaCaptura = cursor.getInt(3);
                    metaCollection.add(meta);
                
                }while(cursor.moveToNext());
            
            }
            db.close();
            return metaCollection;
    }
    
        
        
        
        
    
    
}
