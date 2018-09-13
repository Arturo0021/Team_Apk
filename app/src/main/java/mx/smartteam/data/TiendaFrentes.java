package mx.smartteam.data;

import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.TiendaFrentesCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class TiendaFrentes {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.TiendaFrentesCollection DownloadFrentes(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop){
        
        METHOD_NAME = "/GetEstatusTiendasFrentes";
        ServiceURI service = new ServiceURI();
        
        mx.smartteam.entities.TiendaFrentesCollection collection = new TiendaFrentesCollection();
        
        try {
            
            jsonString = new JSONStringer()
               .object()
                   .key("Proyecto")
                   .object()
                       .key("Id").value(proyecto.getId())
                   .endObject()
                   .key("Tiendas")
                   .object()
                       .key("DeterminanteGSP").value(pop.DeterminanteGSP)
                   .endObject()
               .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetEstatusTiendasFrentesResult");
            
             
            for (int i = 0; i < jsonArray.length(); ++i) {   
             
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.TiendaFrentes frentes = new mx.smartteam.entities.TiendaFrentes();
                frentes.setCantAnaquel(jsonObject.getInt("cantAnaquel"));
                frentes.setFolio(jsonObject.getInt("folio"));
                frentes.setManos(jsonObject.getInt("manos"));
                frentes.setOjo(jsonObject.getInt("ojo"));
                frentes.setSuelo(jsonObject.getInt("suelo"));
                frentes.setTecho(jsonObject.getInt("techo"));
                frentes.setCadena(jsonObject.getString("cadena"));
                frentes.setCategoria(jsonObject.getString("categoria"));
                frentes.setClasificacion(jsonObject.getString("clasificacion"));
                frentes.setComentarioAnaquel(jsonObject.getString("comentarioAnaquel"));
                frentes.setFecha(jsonObject.getString("fecha"));
                frentes.setMarca(jsonObject.getString("marca"));
                frentes.setProducto(jsonObject.getString("producto"));
                frentes.setSucursal(jsonObject.getString("sucursal"));
                
                collection.add(frentes);
                
            }
            
        } catch(Exception e) {
            e.getMessage();
        }
        
        return collection;
    }
    
}
