package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.triplei.Utilerias;
import mx.smartteam.entities.NotificacionesCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


public class Notificaciones {
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "/GetMensajes";//Nombre del método
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;

    public static class Download {

        public static mx.smartteam.entities.NotificacionesCollection GetbyproyectoUsuario(
                mx.smartteam.entities.Proyecto proyecto,String time ) throws Exception {
     strEntity = null;     jsonResult = null;     jsonArray = null;     jsonString = null;
            
            
                NotificacionesCollection notificacionesCollection = new NotificacionesCollection();
                ServiceURI service = new ServiceURI();
                METHOD_NAME = "/GetMensajes";
                
                jsonString = new JSONStringer().object()
                            .key("Proyecto").object()
                            .key("Id").value(proyecto.Id.toString())
                            .key("Ufechadescarga").value(time.toString())
                            .endObject()
                            .endObject();
                
                    strEntity = new StringEntity(jsonString.toString());
                    
                    jsonResult = service.HttpGet(METHOD_NAME, strEntity);
                     
                    jsonArray = jsonResult.getJSONArray("GetMensajesResult");//Cambiar
                    
                    
                    for(int i = 0; i < jsonArray.length(); ++i ){
     
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mx.smartteam.entities.Notificaciones notificaciones = new mx.smartteam.entities.Notificaciones();
                        notificaciones.Id = jsonObject.getInt("Id");
                        notificaciones.Tipo = jsonObject.getString("Tipo");
                        notificaciones.Cuerpo = jsonObject.getString("Cuerpo");
                        notificaciones.CapturaFecha = jsonObject.getLong("CapturaFecha");
                        notificaciones.FechaFin = jsonObject.getLong("FechaFin");
                        notificaciones.FechaEnvio = jsonObject.getLong("FechaEnvio");
                        notificaciones.IdProyecto = jsonObject.getInt("IdProyecto");
                        notificaciones.StatusSync = jsonObject.getInt("Statussync");
                        notificaciones.FechaSync = jsonObject.getLong("FechaSync");
                                               
                        notificacionesCollection.add(notificaciones);
                        
                    }

            return notificacionesCollection;
        }
        
        public static mx.smartteam.entities.NotificacionesCollection GetMensajesByUsuario (mx.smartteam.entities.Proyecto proyecto,String time, mx.smartteam.entities.Usuario usuario ) throws Exception {
                strEntity = null;     jsonResult = null;     jsonArray = null;     jsonString = null;
                NotificacionesCollection notificacionesCollection = new NotificacionesCollection();
                ServiceURI service = new ServiceURI();
                    METHOD_NAME = "/GetMensajesByUsuario";
                
                jsonString = new JSONStringer().object()
                            .key("Proyecto").object()
                            .key("Id").value(proyecto.Id.toString())
                            .key("Ufechadescarga").value(time).endObject()
                            .key("Usuario").object()
                            .key("Id").value(usuario.Id.toString()).endObject()
                            .endObject();
                
                    strEntity = new StringEntity(jsonString.toString());
                    
                    jsonResult = service.HttpGet(METHOD_NAME, strEntity);
                     
                    jsonArray = jsonResult.getJSONArray("GetMensajesByUsuarioResult");//Cambiar
                    
                    
                    for(int i = 0; i < jsonArray.length(); ++i ){
     
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mx.smartteam.entities.Notificaciones notificaciones = new mx.smartteam.entities.Notificaciones();
                        notificaciones.Id = jsonObject.getInt("Id");
                        notificaciones.Tipo = jsonObject.getString("Tipo");
                        notificaciones.Cuerpo = jsonObject.getString("Cuerpo");
                        notificaciones.CapturaFecha = jsonObject.getLong("CapturaFecha");
                        notificaciones.FechaFin = jsonObject.getLong("FechaFin");
                        notificaciones.FechaEnvio = jsonObject.getLong("FechaEnvio");
                        notificaciones.IdProyecto = jsonObject.getInt("IdProyecto");
                        notificaciones.StatusSync = jsonObject.getInt("Statussync");
                        notificaciones.FechaSync = jsonObject.getLong("FechaSync");
                                               
                        notificacionesCollection.add(notificaciones);
                        
                    }

            return notificacionesCollection;

        }
        
        
        
    }
    // inserta notificaciones

    public static void Insert(Context context, mx.smartteam.entities.Notificaciones notificaciones) throws Exception {
       db = null;
        db = (new BaseDatos(context)).getWritableDatabase();
        
        
        
        

        switch (notificaciones.StatusSync) {
            case 1:
                db.execSQL("INSERT INTO Notificaciones "
                        + " (Id, Tipo, Cuerpo, CapturaFecha, FechaFin, FechaEnvio, IdProyecto,StatusSync,FechaSync) " + "SELECT "
                        + notificaciones.Id + " , '"
                        + notificaciones.Tipo + "' , '"
                        + notificaciones.Cuerpo + " ', "
                        + notificaciones.CapturaFecha + " , "
                        + notificaciones.FechaFin + " , "
                        + notificaciones.FechaEnvio + " , "
                        + notificaciones.IdProyecto + " , "
                        + notificaciones.StatusSync + " , "
                        + notificaciones.FechaSync
                        + " WHERE NOT EXISTS(SELECT 1 FROM NOTIFICACIONES WHERE id = "
                        + notificaciones.Id + ");");

                break;

            case 2:
                db.execSQL("INSERT INTO Notificaciones "
                        + " (Id, Tipo, Cuerpo, CapturaFecha, FechaFin, FechaEnvio, IdProyecto,StatusSync,FechaSync) " + "SELECT "
                        + notificaciones.Id + " , '"
                        + notificaciones.Tipo + "' , '"
                        + notificaciones.Cuerpo + " ', "
                        + notificaciones.CapturaFecha + " , "
                        + notificaciones.FechaFin + " , "
                        + notificaciones.FechaEnvio + " , "
                        + notificaciones.IdProyecto + " , "
                        + notificaciones.StatusSync + " , "
                        + notificaciones.FechaSync
                        + " WHERE NOT EXISTS(SELECT 1 FROM NOTIFICACIONES WHERE id = "
                        + notificaciones.Id + ");");

                ContentValues values = new ContentValues();
                values.put("Tipo", notificaciones.Tipo.toString());
                values.put("Cuerpo", notificaciones.Cuerpo.toString());
                values.put("CapturaFecha", notificaciones.CapturaFecha.toString());
                values.put("FechaFin", notificaciones.FechaFin.toString());
                values.put("FechaEnvio", notificaciones.FechaEnvio.toString());
                values.put("IdProyecto", notificaciones.IdProyecto.toString());
                values.put("StatusSync", notificaciones.StatusSync.toString());
                values.put("FechaSync", notificaciones.FechaSync.toString());
                db.update("Notificaciones", values, "Id =" + notificaciones.Id.toString(), null);

                break;

        }

        db.close();

    }
    
    // Busca notificaciones
    public static mx.smartteam.entities.NotificacionesCollection GetbyNotificaciones(final Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.NotificacionesCollection notificacionesCollection = new NotificacionesCollection();

        if (proyecto != null) {
            db = (new BaseDatos(context)).getWritableDatabase();

            try{
            Cursor cursor = db.rawQuery(
                      "SELECT Tipo, Cuerpo, IdProyecto, datetime(FechaFin, 'unixepoch', 'localtime' ) FROM Notificaciones "
                    + " WHERE  IdProyecto ="+proyecto.Id  +"  AND  datetime(FechaFin, 'unixepoch', 'localtime' ) >= '"+ Utilerias.getFechaHora() +"'"//+ " " + dateHours.getHora()
                  
                    ,null);
               
            if (cursor.moveToFirst()) {
                do {
                    mx.smartteam.entities.Notificaciones notificaciones = new mx.smartteam.entities.Notificaciones();
                    notificaciones.Tipo = cursor.getString(0);
                    notificaciones.Cuerpo = cursor.getString(1);
                    notificaciones.IdProyecto = cursor.getInt(2);
                    notificacionesCollection.add(notificaciones);
                } while (cursor.moveToNext());
            }
            }catch(Exception e){
            e.getStackTrace();
            }
        }

        db.close();

        return notificacionesCollection;
    }
    
    
}/*
  * END CLASS 
  */
