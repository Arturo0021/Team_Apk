/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.AnaquelCollection;
import mx.smartteam.entities.MaterialPromocionalCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class MaterialPromocional {

    private static String METHOD_NAME = "/";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
/************************************************************************************************/
    public static void Insert(Context context, mx.smartteam.entities.MaterialPromocional materialPromocional) throws Exception {


        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdProyecto", materialPromocional.IdProyecto);
        values.put("IdUsuario", materialPromocional.IdUsuario);
        values.put("DeterminanteGSP", materialPromocional.DeterminanteGSP);
        values.put("Sku", materialPromocional.Sku);
        values.put("IdVisita", materialPromocional.IdVisita);
        values.put("Cenafas", materialPromocional.Cenafas);
        values.put("Dangles", materialPromocional.Dangles);
        values.put("Stoppers", materialPromocional.Stoppers);
        values.put("Colgantes", materialPromocional.Colgantes);
        values.put("Cartulinas", materialPromocional.Cartulinas);
        values.put("Corbatas", materialPromocional.Corbatas);
        values.put("Flash", materialPromocional.Flash);
        values.put("Tiras", materialPromocional.Tiras);
        values.put("Preciadores", materialPromocional.Preciadores);
        values.put("Folletos", materialPromocional.Folletos);
        values.put("Tapetes", materialPromocional.Tapetes);
        values.put("Faldones", materialPromocional.Faldones);
        values.put("Otros", materialPromocional.Otros);
        values.put("IdFoto", materialPromocional.IdFoto);
        values.put("Comentario", materialPromocional.Comentario);
        values.put("StatusSync", 0);

        db.insert("MaterialPromocional", null, values);
        db.close();


    }
/************************************************************************************************/
  public static void Update(Context context, mx.smartteam.entities.MaterialPromocional materialPromocional)  throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
           
        ContentValues values = new ContentValues();
        values.put("Cenafas", materialPromocional.Cenafas);
       values.put("Dangles",materialPromocional.Dangles);
        values.put("Stoppers", materialPromocional.Stoppers);
        values.put("Colgantes", materialPromocional.Colgantes);
        values.put("Cartulinas",materialPromocional.Cartulinas);
        values.put("Flash", materialPromocional.Flash);
        values.put("Tiras", materialPromocional.Tiras);
        values.put("Preciadores",materialPromocional.Preciadores);
        values.put("Folletos", materialPromocional.Folletos);
        values.put("Tapetes", materialPromocional.Tapetes);
        values.put("Faldones",materialPromocional.Faldones);
        values.put("Otros",materialPromocional.Otros);
        values.put("IdFoto",materialPromocional.IdFoto);
        values.put("Comentario", materialPromocional.Comentario);
        
        
        db.update("MaterialPromocional", values, "Id=" + materialPromocional.Id.toString(), null);


  db.close();

    }  
 /************************************************************************************************/
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.MaterialPromocional materialPromocional) {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", materialPromocional.StatusSync);
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("MaterialPromocional", values, "Id=" + materialPromocional.Id.toString(), null);
        db.close();

    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        int existe = 0;
        
        String query = "SELECT COUNT(1) FROM MaterialPromocional WHERE IdVisita = " + pop.getIdVisita() + ";";
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
            existe = cursor.getInt(0);
        }
        
        return existe;
    }
    
/************************************************************************************************/
    public static mx.smartteam.entities.MaterialPromocionalCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) {

        mx.smartteam.entities.MaterialPromocionalCollection materialPromocionalCollection = new MaterialPromocionalCollection();
        try {

            if (popVisita != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db
                        .rawQuery(
                        "SELECT Id,SKU,Cenafas,Dangles,Stoppers,Colgantes,Cartulinas,Corbatas,Flash,Tiras,Preciadores,Folletos,Tapetes,Faldones,Otros,Comentario,datetime(fechacrea, 'unixepoch', 'localtime'),StatusSync "
                        + "FROM MaterialPromocional "
                        + "WHERE IdVisita=? ",
                        new String[]{popVisita.Id.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.MaterialPromocional materialPromocional = new mx.smartteam.entities.MaterialPromocional();
                        materialPromocional.Id = cursor.getInt(0);
                        materialPromocional.Sku = cursor.getLong(1);
                        materialPromocional.Cenafas = cursor.getInt(2);
                        materialPromocional.Dangles = cursor.getInt(3);
                        materialPromocional.Stoppers = cursor.getInt(4);
                        materialPromocional.Colgantes = cursor.getInt(5);
                        materialPromocional.Cartulinas = cursor.getInt(6);
                        materialPromocional.Corbatas = cursor.getInt(7);
                        materialPromocional.Flash = cursor.getInt(8);
                        materialPromocional.Tiras = cursor.getInt(9);
                        materialPromocional.Preciadores = cursor.getInt(10);
                        materialPromocional.Folletos = cursor.getInt(11);
                        materialPromocional.Tapetes = cursor.getInt(12);
                        materialPromocional.Faldones = cursor.getInt(13);
                        materialPromocional.Otros = cursor.getInt(14);
                        materialPromocional.Comentario = cursor.getString(15);
                        materialPromocional.FechaCrea = cursor.getString(16);
                        materialPromocional.StatusSync = cursor.getInt(17);




                        materialPromocionalCollection.add(materialPromocional);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return materialPromocionalCollection;
    }
 /**
     * @param context
     * @param proyecto
     * @param usuario
     * @param pop
     * @return **********************************************************************************************/  
     public static mx.smartteam.entities.ProductoCollection 
        GetProductosByVisita(Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop){
        mx.smartteam.entities.ProductoCollection productoCollection = new mx.smartteam.entities.ProductoCollection();
         
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "SELECT sku "
                + "FROM MaterialPromocional "+
                 " Where Idproyecto      = ? AND         " + 
                "       Idusuario       = ? AND         " + 
           
                "       IdVisita        = ? " 
                ,
                new String[]{
                             proyecto.Id.toString(),
                             usuario.Id.toString(),
                             pop.IdVisita.toString(),
                                   
                            }
                          );
        
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
            
                producto.SKU = cursor.getLong(0);
               
                productoCollection.add(producto);
            } while (cursor.moveToNext());
        }
        
        return productoCollection;
        }

 /*Alan*********************************************************************/
        
    public static mx.smartteam.entities.MaterialPromocional
            GetInfoByVisita(Context context,
                    mx.smartteam.entities.Proyecto proyecto,
                    mx.smartteam.entities.Usuario usuario,
                    mx.smartteam.entities.Pop pop,
                    mx.smartteam.entities.Producto producto) {

        mx.smartteam.entities.MaterialPromocional materialPromocional = null;

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db
                .rawQuery(
                        " Select Sku,                                            "
                        + " Cenafas            as Cenefas,               "
                        + "                       Dangles             as Dangles,                   "
                        + "                       Stoppers            as Stoppers,                  "
                        + "                       Colgantes           as Colgantes,                 "
                        + "                       Cartulinas          as Cartulinas,                "
                        + "                       Flash               as Flash,                     "
                        + "                       Tiras               as TirasDeImpulso,            "
                        + "                       Preciadores         as Preciadores,               "
                        + "                       Folletos            as Folletos,                  "
                        + "                       Tapetes             as Tapetes,                   "
                        + "                       Faldones            as Faldones,                  "
                        + "                       Otros               as OtrosMaterialesProm,       "
                        + "                       Comentario          as Comentarios,       "
                        + "                       Corbatas          as Corbatas, id, idfoto       "
                        + "  From MaterialPromocional                             "
                        + " Where Idproyecto      = ? AND                         "
                        + "       Idusuario       = ? AND                         "
                        + "       Sku             = ? AND                         "
                        + "       IdVisita        = ?  ",
                        new String[]{
                            proyecto.Id.toString(),
                            usuario.Id.toString(),
                            producto.SKU.toString(),
                            pop.IdVisita.toString(),}
                );

        if (cursor.moveToFirst()) {
            materialPromocional = new mx.smartteam.entities.MaterialPromocional();
            // Recorremos el cursor hasta que no haya más registros
            do {
                materialPromocional.Sku = cursor.getLong(0);
                materialPromocional.Cenafas = cursor.getInt(1);
                materialPromocional.Dangles = cursor.getInt(2);
                materialPromocional.Stoppers = cursor.getInt(3);
                materialPromocional.Colgantes = cursor.getInt(4);
                materialPromocional.Cartulinas = cursor.getInt(5);
                materialPromocional.Flash = cursor.getInt(6);
                materialPromocional.Tiras = cursor.getInt(7);
                materialPromocional.Preciadores = cursor.getInt(8);
                materialPromocional.Folletos = cursor.getInt(9);
                materialPromocional.Tapetes = cursor.getInt(10);
                materialPromocional.Faldones = cursor.getInt(11);
                materialPromocional.Otros = cursor.getInt(12);
                materialPromocional.Comentario = cursor.getString(13);
                materialPromocional.Corbatas = cursor.getInt(14);
                materialPromocional.Id = cursor.getInt(15);
                materialPromocional.IdFoto = cursor.getInt(16);
            } while (cursor.moveToNext());
        }
        return materialPromocional;
    }    
    /*Alan*********************************************************************/
                        public static mx.smartteam.entities.MaterialPromocional
                   GetInfoByVisita2(Context context, 
                                   mx.smartteam.entities.Proyecto proyecto, 
                                   mx.smartteam.entities.Usuario usuario,
                                   mx.smartteam.entities.Pop pop, 
                                   String producto)
    {
  
        mx.smartteam.entities.MaterialPromocional materialPromocional = new mx.smartteam.entities.MaterialPromocional();
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db
                .rawQuery(
                " Select Sku,                                            " +
                       " Cenafas            as Cenefas,               " +
"                       Dangles             as Dangles,                   " +
"                       Stoppers            as Stoppers,                  " +
"                       Colgantes           as Colgantes,                 " +
"                       Cartulinas          as Cartulinas,                " +
"                       Flash               as Flash,                     " +
"                       Tiras               as TirasDeImpulso,            " +
"                       Preciadores         as Preciadores,               " +
"                       Folletos            as Folletos,                  " +
"                       Tapetes             as Tapetes,                   " +
"                       Faldones            as Faldones,                  " +
"                       Otros               as OtrosMaterialesProm,       " +
"                       Comentario          as Comentarios,       "+
"                       Corbatas          as Corbatas, id       "+
                "  From MaterialPromocional                             " +
                " Where Idproyecto      = ? AND                         " + 
                "       Idusuario       = ? AND                         " + 
                "       Sku             = (select p.sku from producto p,  MaterialPromocional a where p.sku=a.sku and p.nombre = ?) AND                         " + 
                "       IdVisita        = ?  " 
                ,
                new String[]{
                             proyecto.Id.toString(),
                             usuario.Id.toString(),
                             producto,
                             pop.IdVisita.toString(),
                              
                            }
                          );
  
        if (cursor.moveToFirst()) 
        {
            // Recorremos el cursor hasta que no haya más registros
            do {
                materialPromocional.Sku= cursor.getLong(0);
                materialPromocional.Cenafas= cursor.getInt(1);
                 materialPromocional.Dangles= cursor.getInt(2);
                  materialPromocional.Stoppers= cursor.getInt(3);
                   materialPromocional.Colgantes= cursor.getInt(4);
                 materialPromocional.Cartulinas= cursor.getInt(5);
                  materialPromocional.Flash= cursor.getInt(6);
                   materialPromocional.Tiras= cursor.getInt(7);
                 materialPromocional.Preciadores= cursor.getInt(8);
                  materialPromocional.Folletos= cursor.getInt(9);
                   materialPromocional.Tapetes= cursor.getInt(10);
                 materialPromocional.Faldones= cursor.getInt(11);
                  materialPromocional.Otros= cursor.getInt(12);
                   materialPromocional.Comentario= cursor.getString(13);
                   materialPromocional.Corbatas= cursor.getInt(14);
 materialPromocional.Id= cursor.getInt(15);
               } 
            while(cursor.moveToNext());
        }
        return materialPromocional;
        }    
    
public static mx.smartteam.entities.MaterialPromocional ContestacionMaterialPromocional(
        Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto ){
  
        mx.smartteam.entities.MaterialPromocional materialPromocional = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db
                .rawQuery(
                " Select Sku From MaterialPromocional Where IdVisita = ? AND Idfoto = ? " 
                , new String[]{
                    visita.Id.toString(),
                    foto.Id.toString()
            }
          );
  
        if (cursor.moveToFirst()) 
        {
            materialPromocional = new mx.smartteam.entities.MaterialPromocional();
            // Recorremos el cursor hasta que no haya más registros
            do {
                materialPromocional.Sku = cursor.getLong(0);
               } 
            while(cursor.moveToNext());
        }
        return materialPromocional;
        }
                   
                   
                   
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.MaterialPromocional materialPromocional) throws Exception {

            ServiceURI service = new ServiceURI();


            METHOD_NAME = "/MaterialPromocionalInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Tienda").object()
                    .key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                    .key("SKU").value(materialPromocional.Sku.toString())
                    .key("CadenaFecha").value(materialPromocional.FechaCrea)
                    .key("IsIsla").value(0)
                    .key("IsAreaFlex").value(0)
                    .key("IsAriete").value(0)
                    .key("IsForway").value(0)
                    .key("IsMaterialPOP").value(1)
                    .key("MatPOPColgantes").value(materialPromocional.Colgantes)
                    .key("MatPOPStoppers").value(materialPromocional.Stoppers)
                    .key("MatPOPCenefas").value(materialPromocional.Cenafas)
                    .key("MatPOPDanglers").value(materialPromocional.Dangles)
                    .key("MatPOPFaldon").value(materialPromocional.Faldones)
                    .key("MatPOPCartulina").value(materialPromocional.Cartulinas)
                    .key("MatPOPCorbata").value(materialPromocional.Corbatas)
                    .key("MatPOPFlash").value(materialPromocional.Flash)
                    .key("MatPOPTira").value(materialPromocional.Tiras)
                    .key("MatPOPPreciador").value(materialPromocional.Preciadores)
                    .key("MatPOPFolleto").value(materialPromocional.Folletos)
                    .key("MatPOPTapete").value(materialPromocional.Tapetes)
                    .key("MatPOPOtros").value(materialPromocional.Otros)
                    .endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("MaterialPromocionalInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [MaterialPromocionalInsert]");
            }





        }
    }
/************************************************************************************************/
}
