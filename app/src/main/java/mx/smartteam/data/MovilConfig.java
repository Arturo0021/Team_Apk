package mx.smartteam.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.MovilConfigCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


/*
 * DESCARGAMOS LA CONFIGURACION DEL GPS
 * 
 */
public class MovilConfig {
    private static String METHOD_NAME = "/GetGPSUsuario";//CAMBIAR
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
 
    public static class Download{
        public static mx.smartteam.entities.MovilConfigCollection GetByMovilConfig(
                mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Proyecto proyecto
                )throws Exception{
            
            MovilConfigCollection movilConfigCollection = new MovilConfigCollection();
   
           ServiceURI service = new ServiceURI();
           
            jsonString = new JSONStringer().object()
                    .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(proyecto.Id.toString()).endObject()
                    .endObject();
            
            
            strEntity =new StringEntity(jsonString.toString());
            
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetGPSUsuarioResult");//CAMBIAR
            
            for (int i=0 ; i < jsonArray.length(); i++){
            
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                
                mx.smartteam.entities.MovilConfig movilConfig = new mx.smartteam.entities.MovilConfig();
                movilConfig.gpsconfig = jsonObject.getInt("gps");
                movilConfigCollection.add(movilConfig);
            }
            
            return movilConfigCollection;
       }
    }//END DOWNLOAD
    
    
    
    
//     public static void configGPS( Context context,
//             mx.smartteam.entities.Usuario usuario,
//             mx.smartteam.entities.MovilConfig configgps
//             )throws Exception {
//        db = (new BaseDatos(context)).getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("ConfigGPS", configgps.gpsconfig);
//        db.update("ConfigGPS", values, "IdUsuario =" + usuario.Id.toString(), null);
//    }
    
    
      public static void ActualizaGps(Context context,
             mx.smartteam.entities.Usuario usuario,
             mx.smartteam.entities.MovilConfig configgps
             ) {
         ContentValues values = new ContentValues();
            db = (new BaseDatos(context)).getWritableDatabase();
                
                    values.put("ConfigGps", "0");
                    values.put("Tipo", "0");
                    values.put("Conexion", "offline");
                    
            db.update("ConfigGps", values, "IdUsuario=" +usuario.Id.toString(), null);
            db.close();
        
    }
     
     
     public static void configGPS(Context context,
             mx.smartteam.entities.Usuario usuario,
             mx.smartteam.entities.MovilConfig configgps
             ) {
         ContentValues values = new ContentValues();
            db = (new BaseDatos(context)).getWritableDatabase();
                if(configgps.gpsconfig!=null){
                            values.put("ConfigGps", configgps.gpsconfig.toString());
                    }  else{
                    values.put("ConfigGps", "0");
                    }
            db.update("ConfigGps", values, "IdUsuario=" +usuario.Id.toString(), null);
            db.close();
        
    }
    
    public static mx.smartteam.entities.MovilConfigCollection GetByGpsConfig(final Context context, mx.smartteam.entities.Usuario usuario) throws Exception{
        
        mx.smartteam.entities.MovilConfigCollection movilConfigCollection = new MovilConfigCollection();
        
        if (usuario != null){
            db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT configGps,Tipo "
                + "FROM configGPS WHERE IdUsuario = ? ",
                 new String[]{
                    usuario.Id.toString()
                }
                );
        
        if(cursor.moveToFirst()){
            do{
            mx.smartteam.entities.MovilConfig movilConfig = new mx.smartteam.entities.MovilConfig();
            
            movilConfig.gpsconfig = cursor.getInt(0);
            movilConfig.Tipo = cursor.getString(1);
            movilConfigCollection.add(movilConfig);
            }while (cursor.moveToNext());
        
        }//END CURSOR
        }//END IF
        return movilConfigCollection;
    }
    
    
     public static mx.smartteam.entities.MovilConfig GetConfig(final Context context, mx.smartteam.entities.Usuario usuario) throws Exception{
        
         mx.smartteam.entities.MovilConfig movilConfig = new mx.smartteam.entities.MovilConfig();
        
        if (usuario != null){
            db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT configGps,Tipo,Conexion "
                + "FROM configGPS WHERE IdUsuario = ? ",
                 new String[]{
                    usuario.Id.toString()
                }
                );
        
        if(cursor.moveToFirst()){
            do{
           
            
            movilConfig.gpsconfig = cursor.getInt(0);
            movilConfig.Tipo = cursor.getString(1);
            movilConfig.Conexion = cursor.getString(2);
            
            }while (cursor.moveToNext());
        
        }//END CURSOR
        }//END IF
        return movilConfig;
    }
    
    
    
    
   public static void ConfigTipo(Context context,
             mx.smartteam.entities.Usuario usuario,
             mx.smartteam.entities.MovilConfig config
             ) {
         ContentValues values = new ContentValues();
            db = (new BaseDatos(context)).getWritableDatabase();
                if(config.Tipo!=null){
                            values.put("Tipo", config.Tipo.toString());
                    } 
                if(config.Conexion!=null){
                            values.put("Conexion", config.Conexion.toString());
                    } 
                
            db.update("ConfigGps", values, "IdUsuario=" +usuario.Id.toString(), null);
            db.close();
        
    }
    
    
    
}//END CLASS