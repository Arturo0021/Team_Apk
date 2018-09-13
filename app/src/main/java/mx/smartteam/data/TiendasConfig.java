package mx.smartteam.data;

import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.TiendasConfigCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class TiendasConfig {

    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.TiendasConfigCollection DownloadConfig(mx.smartteam.entities.Proyecto proyecto) {
        
        METHOD_NAME = "/GetEstatusTiendasConfig";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.TiendasConfigCollection collection = new TiendasConfigCollection();
        
        try {
        
            jsonString = new JSONStringer()
                            .object()
                                .key("Proyecto")
                                .object()
                                    .key("Id").value(proyecto.getId())
                                .endObject()
                            .endObject();

            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetEstatusTiendasConfigResult");
            
            for (int i = 0; i < jsonArray.length(); ++i) {
                
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.TiendasConfig config = new mx.smartteam.entities.TiendasConfig();
                
                config.setEstatus(jsonObject.getInt("estatus"));
                config.setTipo(jsonObject.getString("tipo"));
                
                collection.add(config);
                
            }
           
        
        } catch(Exception e) {
            e.getMessage();
        }
        
        return collection;
    }
    
}
