package mx.smartteam.data;

import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.TiendaSosCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class TiendaSos {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.TiendaSosCollection DownloadSos(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) {
     
        METHOD_NAME = "/GetEstatusTiendasSos";
        ServiceURI service = new ServiceURI();
        
        mx.smartteam.entities.TiendaSosCollection collection = new TiendaSosCollection();
        
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
            
            jsonArray = jsonResult.getJSONArray("GetEstatusTiendasSosResult");
            
            for (int i = 0; i < jsonArray.length(); ++i) {   
             
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.TiendaSos sos = new mx.smartteam.entities.TiendaSos();
                sos.setFolio(jsonObject.getInt("folio"));
                sos.setCadena(jsonObject.getString("cadena"));
                sos.setCategoria(jsonObject.getString("categoria"));
                sos.setClasificacion(jsonObject.getString("clasificacion"));
                sos.setFecha(jsonObject.getString("fecha"));
                sos.setMarca(jsonObject.getString("marca"));
                sos.setSucursal(jsonObject.getString("sucursal"));
                sos.setValor(jsonObject.getString("Valor"));
                sos.setUsuario(jsonObject.getString("usuario"));
                sos.setPromotor(jsonObject.getString("promotor"));
                
                collection.add(sos);
            }
            
        } catch (Exception e) {
            e.getMessage();
        }
        
        return collection;
    }
    
}
