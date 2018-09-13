/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.SeguimientoGpsCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import mx.triplei.Utilerias;
/**
 *
 * @author jaime.bautista
 */
public class SeguimientoGps 
{
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String METHOD_NAME = null;
      private static SQLiteDatabase db = null;
      
    public static String Hola(){
    
    String hola = "Hola soy jaime";
    return hola;
    }
    
    
        public static void Insert( Context context,mx.smartteam.entities.SeguimientoGps seguimientoGps) throws Exception{

                db = (new BaseDatos(context)).getWritableDatabase();
                db.execSQL("INSERT INTO SeguimientoGps"
                + " (UsuarioId, ProyectoId,Latitud,Longitud,determinanteGsp) VALUES ("
                + seguimientoGps.UsuarioId+ ","
                + seguimientoGps.ProyectoId +","
                + seguimientoGps.Latitud+","
                + seguimientoGps.Longitud   +  ","
                +seguimientoGps.determinanteGsp+
                        
                " );");

                db.close();
            
    }   
        
         public static mx.smartteam.entities.SeguimientoGpsCollection GetSeguimientoGps(final Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception {

        mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection = new SeguimientoGpsCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "SELECT  Id,UsuarioId, ProyectoId,datetime(Fecha, 'unixepoch', 'localtime'), Latitud, Longitud, DeterminanteGsp "
                + "FROM seguimientoGps "
                + "WHERE UsuarioId = ? "
                + "AND ProyectoId = ? "
                + "AND StatusSync = 0 ",
                new String[]
                {
                    usuario.Id.toString(),
                    proyecto.Id.toString()
                }
                
                );

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.SeguimientoGps seguimiento = new mx.smartteam.entities.SeguimientoGps();
                seguimiento.LogId = cursor.getInt(0);
                seguimiento.UsuarioId = cursor.getInt(1);
                seguimiento.ProyectoId = cursor.getInt(2);
                seguimiento.Fecha = cursor.getString(3);
                seguimiento.Latitud = cursor.getDouble(4);
                seguimiento.Longitud = cursor.getDouble(5);
                seguimiento.determinanteGsp = cursor.getInt(6);
                
                
                seguimientoGpsCollection.add(seguimiento);
            } while (cursor.moveToNext());

        }

        db.close();

        return seguimientoGpsCollection;
    }
        
        public static String ActualizarStatus(Context context , mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario)
        {
            String strResult = "OK";
            db = (new BaseDatos(context)).getWritableDatabase();
            
            ContentValues value = new ContentValues();
            value.put("StatusSync", 1);
            value.put("FechaSync", Utilerias.getFechaUnix());
            db.update("SeguimientoGps", value, " usuarioId = ? AND proyectoId = ? ", 
                        new String[]{ usuario.Id.toString(), proyecto.Id.toString() }
                    );
            db.close();
            return strResult;
        }
         
         
        
        public static class Upload{
        
            public static String Insert(mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection) throws Exception{
                ServiceURI service = new ServiceURI();
                METHOD_NAME = "/SeguimientoGpsInsert";
                String strResult ="OK";
                
                jsonString = new JSONStringer().object()
                        .key("Proyecto").object().key("Id").value(proyecto.Id.toString()).endObject()
                        .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject();
                        
                jsonString.key("Seguimiento").array();
                    for(mx.smartteam.entities.SeguimientoGps seguimientoGps: seguimientoGpsCollection)
                    {
                            jsonString.object()
                                    .key("Fecha").value(seguimientoGps.Fecha)
                                    .key("Latitud").value(seguimientoGps.Latitud.toString())
                                    .key("Longitud").value(seguimientoGps.Longitud.toString())
                                    .key("DeterminanteGsp").value(seguimientoGps.determinanteGsp.toString())
                                    .endObject();
                    }

                    jsonString.endArray();
            
            jsonString.endObject();     
                        
            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            if (!jsonResult.getString("SeguimientoGpsInsertResult").equals("OK")) {
                throw new Exception("Error en el servicio Seguimiento");
            }
                       
                return strResult;
                
            }
        
        }
        
        
}
























