package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.VersionUsuarioCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class VersionesUsuarios {

    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "/GetVersionAPKPorUsuario";//Nombre del método 
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    
    public static class Download{
        
        public static mx.smartteam.entities.VersionUsuarioCollection GetByVersionXUsuario(mx.smartteam.entities.Version versionx, mx.smartteam.entities.Usuario usuario) throws Exception {
            VersionUsuarioCollection versionUsuarioCollection = new VersionUsuarioCollection();
            ServiceURI service = new ServiceURI();

            jsonString = new JSONStringer()
                    .object()
                    .key("Version").object().key("Id").value(versionx.version.toString()).endObject()
                    .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult.getJSONArray("GetVersionAPKPorUsuarioResult"); //cambiar

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                mx.smartteam.entities.VersionUsuario versionUsuario = new mx.smartteam.entities.VersionUsuario();
                versionUsuario.Id = jsonObject.getInt("Id");
                versionUsuario.Actual = jsonObject.getInt("Actual");
                versionUsuario.Nueva = jsonObject.getInt("Nueva");
                versionUsuario.Status = jsonObject.getInt("Status");
                versionUsuario.FechaCrea = jsonObject.getString("FechaCrea");
                versionUsuario.FechaMod = jsonObject.getString("FechaMod");
                versionUsuario.UsuarioInsert = jsonObject.getInt("UsuarioInsert");
                versionUsuario.IdUsuario = jsonObject.getInt("idUsuario");

                versionUsuarioCollection.add(versionUsuario);
            }
            return versionUsuarioCollection;
        }
    }
    
    
    public static void Insert(Context context, mx.smartteam.entities.VersionUsuario versionUsuario) {
        db = (new BaseDatos(context)).getWritableDatabase();
        try{
            String qquery = "SELECT COUNT(1) FROM VersionUsuario WHERE id = " + versionUsuario.Id + ";" ;
            Cursor cursor = db.rawQuery( qquery, null);
            
            String qquery2 = "INSERT INTO VersionUsuario (Id , Actual , Nueva , Status , FechaCrea, FechaMod , UsuarioInsert, IdUsuario  ) VALUES "
                    + " ("
                    + versionUsuario.Id
                    + ", " + versionUsuario.Actual
                    + ", " + versionUsuario.Nueva
                    + ", " + versionUsuario.Status
                    + ", '" + versionUsuario.FechaCrea
                    + "' , '" + versionUsuario.FechaMod
                    + "', " + versionUsuario.UsuarioInsert
                    + ", " + versionUsuario.IdUsuario
                    + ")";
            if(cursor.moveToFirst()){
                int contador = cursor.getInt(0);
                if(contador == 0){
                    db.execSQL(qquery2);
                }else{ 
                    ContentValues values = new ContentValues();
                    values.put("Actual", versionUsuario.Actual);
                    values.put("Nueva", versionUsuario.Nueva);
                    values.put("Status", versionUsuario.Status);
                    values.put("FechaMod", versionUsuario.FechaMod);
                    values.put("UsuarioInsert", versionUsuario.UsuarioInsert);
                    values.put("IdUsuario", versionUsuario.IdUsuario);
                db.update("VersionUsuario", values, " Id= " + versionUsuario.Id.toString(), null);
                }
            }
            
            
        }catch(Exception ex){
            ex.getMessage();
        }
        db.close();
    }
    
    public static mx.smartteam.entities.VersionUsuario getVersionUsuario(final Context context) throws Exception {
        mx.smartteam.entities.VersionUsuario versionUsuario = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        try{
            
            String query = "SELECT MAX(Id) AS Id, Actual, Nueva,Status FROM VersionUsuario;";
            
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    versionUsuario = new mx.smartteam.entities.VersionUsuario();
                    versionUsuario.Id = cursor.getInt(0);
                    versionUsuario.Actual = cursor.getInt(1);
                    versionUsuario.Nueva = cursor.getInt(2);          
                    versionUsuario.Status = cursor.getInt(3);
                }while(cursor.moveToNext());
            }
        }catch(Exception ex){
         ex.getMessage();
        }
        return versionUsuario;
    }
    
    
}
