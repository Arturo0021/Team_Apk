package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.VersionCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Version {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "/GetVersionAPK2";//Nombre del método 
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    
    public static class Download{
        
        public static mx.smartteam.entities.VersionCollection GetByVersion(mx.smartteam.entities.Version versionx) throws Exception {
            VersionCollection versionCollection = new VersionCollection();
            ServiceURI service = new ServiceURI();

            jsonString = new JSONStringer().object()
                    .key("version").object().key("idAplicacion").value(versionx.IdApp.toString()).endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult.getJSONArray("GetVersionAPK2Result"); //cambiar

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                mx.smartteam.entities.Version version = new mx.smartteam.entities.Version();
                version.Id = jsonObject.getInt("id");
                version.version = jsonObject.getInt("Version");
                version.nombreapk = jsonObject.getString("Nombre");
                version.url = jsonObject.getString("Url");

                versionCollection.add(version);
            }
            return versionCollection;
        }
    }
    
        public static void Insert(Context context, mx.smartteam.entities.Version version) throws Exception {
            db = (new BaseDatos(context)).getWritableDatabase();
              
            Cursor cursor = db.rawQuery("SELECT COUNT(1) FROM Versiones WHERE id = " + version.Id, null);
              
            String qquery = "INSERT INTO Versiones (Id, nombreapk,version,Url) VALUES ("
                + version.Id +" , '"
                + version.nombreapk+ "' , "
                + version.version+ " , '"
                + version.url +"' );";
              
            if(cursor.moveToFirst()){
                int contador = cursor.getInt(0);
                if(contador == 0){
                    db.execSQL(qquery);
                }else{
                    ContentValues values = new ContentValues();
                    values.put("nombreapk", version.nombreapk );
                    values.put("version", version.version );
                    values.put("url", version.url );
                    db.update("Versiones", values, " Id= " + version.Id.toString(), null);
                }
            }            
            db.close();
        } 
        
        public static mx.smartteam.entities.VersionCollection GetByVersion (final Context context ) throws Exception {
                
            mx.smartteam.entities.VersionCollection versionCollection = new VersionCollection();
           
            db = (new BaseDatos(context)).getWritableDatabase();
            try{
                Cursor cursor = db.rawQuery("SELECT nombreapk, MAX(version), url FROM Versiones LIMIT 1", null);
                      if(cursor.moveToFirst()){
                      do{
                          mx.smartteam.entities.Version version = new mx.smartteam.entities.Version();
                          version.nombreapk = cursor.getString(0);
                          version.version = cursor.getInt(1);
                          version.url = cursor.getString(2);
                          versionCollection.add(version);
                      }while(cursor.moveToNext());
                      }
            
            }catch(Exception e){
            e.printStackTrace();
            }
            db.close();
        return versionCollection;
        }
        
        
        public static mx.smartteam.entities.Version GetVersion(final Context context, Integer idversion) throws Exception {
        
            mx.smartteam.entities.Version version = null;
            
            db = (new BaseDatos(context)).getWritableDatabase();
            try{
                
                String qquery = "SELECT nombreapk, version, url FROM Versiones WHERE id = " + idversion + ";";
                Cursor cursor = db.rawQuery(qquery, null );
                if(cursor.moveToFirst()){
                    do{
                       version = new mx.smartteam.entities.Version();
                       version.nombreapk = cursor.getString(0);
                       version.version = cursor.getInt(1);
                       version.url = cursor.getString(2);
                    }while(cursor.moveToNext());
                }
            }catch(Exception ex){
                ex.getMessage();
            }
            return version;
        }
        
}

