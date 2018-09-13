package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
// import mx.smartteam.entities.Ex

public class ExhibicionConfig {
    private static String METHOD_NAME = "/GetExhibicionesConfig";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
        
    public static class Download{
    
        public static mx.smartteam.entities.ExhibicionConfigCollection GetExhibicionConfig(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time )throws Exception {
            mx.smartteam.entities.ExhibicionConfigCollection exhibicionConfigCollection = new mx.smartteam.entities.ExhibicionConfigCollection();
            ServiceURI service = new ServiceURI();

            try{
             jsonString = new JSONStringer().object()
                        .key("Proyecto").object()
                            .key("Id").value(proyecto.Id)
                            .key("Ufechadescarga").value(time)
                            .endObject()
                        .key("Usuario").object()
                            .key("Id").value(usuario.Id).endObject()
                        .endObject();

                strEntity = new StringEntity(jsonString.toString());
                jsonResult = service.HttpGet(METHOD_NAME, strEntity);
                jsonArray = jsonResult.getJSONArray("GetExhibicionesConfigResult");

                for(int i =0 ; i< jsonArray.length(); i++ ) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mx.smartteam.entities.ExhibicionConfig exhibicionConfig = new mx.smartteam.entities.ExhibicionConfig();
                    exhibicionConfig.Id = jsonObject.getInt("Id");
                    exhibicionConfig.IdCategoria = jsonObject.getInt("IdCategoria");
                    exhibicionConfig.IdMarca = jsonObject.getInt("IdMarca");
                    exhibicionConfig.IdExhibicion = jsonObject.getInt("IdExhibicion");
                    exhibicionConfig.IdSondeoOrden = jsonObject.getInt("IdSondeoOrden");
                    exhibicionConfig.Ponderacion = jsonObject.getDouble("Ponderacion");
                    exhibicionConfig.Activo = jsonObject.getInt("Activo");
                    exhibicionConfigCollection.add(exhibicionConfig);
                }

            }catch(Exception ex){
                throw new Exception("GetExhibicionesConfig", ex);
            }
                return exhibicionConfigCollection;
        }
    }
    
    public static void Insert(Context context , mx.smartteam.entities.ExhibicionConfig exhibicionConfig) throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
        //Validar si existe el registro
        
        try{
        Cursor cursor = db.rawQuery("Select COUNT(Id) FROM ExhibicionConfig WHERE id="+ exhibicionConfig.Id +";", null);
            if(cursor.moveToFirst()){
                int contador =  cursor.getInt(0);
                if(contador == 0){
                db.execSQL("INSERT INTO ExhibicionConfig "
                    + " (Id, IdSondeoOrden, IdCategoria, IdMarca, IdExhibicion, Ponderacion, Activo) VALUES ("        
                    +  exhibicionConfig.Id + ","
                    +  exhibicionConfig.IdSondeoOrden + ","
                    +  exhibicionConfig.IdCategoria + ","
                    +  exhibicionConfig.IdMarca + ","
                    +  exhibicionConfig.IdExhibicion + ","
                    +  exhibicionConfig.Ponderacion + ","
                    +  exhibicionConfig.Activo +" )"
                    );
                } else {
                    ContentValues values = new ContentValues();
                        values.put("IdSondeoOrden", exhibicionConfig.IdSondeoOrden );
                        values.put("IdCategoria", exhibicionConfig.IdCategoria );
                        values.put("IdMarca", exhibicionConfig.IdMarca );
                        values.put("IdExhibicion", exhibicionConfig.IdExhibicion );
                        values.put("Ponderacion", exhibicionConfig.Ponderacion );
                        values.put("Activo", exhibicionConfig.Activo );
                        db.update("ExhibicionConfig", values, " Id = " + exhibicionConfig.Id, null );
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
