package mx.smartteam.data;

import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.TiendaExAdicionalCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class TiendaExAdicional {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.TiendaExAdicionalCollection DownloadExAdicional(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) {
        
        METHOD_NAME = "/GetEstatusTiendasExAdicional";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.TiendaExAdicionalCollection collection = new TiendaExAdicionalCollection();
        
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
            
            jsonArray = jsonResult.getJSONArray("GetEstatusTiendasExAdicionalResult");
            
            for (int i = 0; i < jsonArray.length(); ++i) {   
             
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.TiendaExAdicional exadic = new mx.smartteam.entities.TiendaExAdicional();
                exadic.setCantidad(jsonObject.getInt("cantidad"));
                exadic.setFolio(jsonObject.getInt("folio"));
                exadic.setCadena(jsonObject.getString("cadena"));
                exadic.setClasificacion(jsonObject.getString("clasificacion"));
                exadic.setDepartamento(jsonObject.getString("departamento"));
                exadic.setFecha(jsonObject.getString("fecha"));
                exadic.setProducto(jsonObject.getString("producto"));
                exadic.setPromotor(jsonObject.getString("promotor"));
                exadic.setSucursal(jsonObject.getString("sucursal"));
                exadic.setTipo(jsonObject.getString("tipo"));
                exadic.setUsuario(jsonObject.getString("usuario"));
                
                collection.add(exadic);
                
            }
            
        } catch(Exception e) {
            e.getMessage();
        }
        
        return collection;
    }
    
}
