package mx.smartteam.data;

import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.TiendaAsistencia;
import mx.smartteam.entities.TiendaAsistenciaCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class TiendasAsistencia {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.TiendaAsistenciaCollection DownloadAsistencia(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) {
        
        METHOD_NAME = "/GetEstatusTiendasAsistencia";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.TiendaAsistenciaCollection collection = new TiendaAsistenciaCollection();
        
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
            
            jsonArray = jsonResult.getJSONArray("GetEstatusTiendasAsistenciaResult");
            
            for (int i = 0; i < jsonArray.length(); ++i) {   
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.TiendaAsistencia asistencia = new TiendaAsistencia();
                
                asistencia.setTotal_de_Asistencias(jsonObject.getInt("total_de_asistencias"));
                asistencia.setCadena(jsonObject.getString("cadena"));
                asistencia.setFecha(jsonObject.getString("fecha"));
                asistencia.setFolio(jsonObject.getString("folio"));
                asistencia.setPromotor(jsonObject.getString("promotor"));
                asistencia.setSalida(jsonObject.getString("salida"));
                asistencia.setSucursal(jsonObject.getString("sucursal"));
                asistencia.setClasificacion(jsonObject.getString("clasificacion"));
                asistencia.setTrayectoria(jsonObject.getString("trayectoria"));
                asistencia.setUsuario(jsonObject.getString("usuario"));
                
                collection.add(asistencia);
            }
            
        } catch(Exception e) {
            e.getMessage();
        }
        
        return collection;
    }
    
}
