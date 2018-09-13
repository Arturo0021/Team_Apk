package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.Insumos_ConfigCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class Insumos_Config {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.Insumos_ConfigCollection DownloadConfig(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        METHOD_NAME = "/GetInsumosConfiguracion";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.Insumos_ConfigCollection collection = new Insumos_ConfigCollection();
        
        jsonString = new JSONStringer()
               .object()
                   .key("Proyecto")
                   .object()
                       .key("Id").value(proyecto.getId())
                   .endObject()
               .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetInsumosConfiguracionResult");
        
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Insumos_Config config = new mx.smartteam.entities.Insumos_Config();
                
                config.setId(jsonObject.getInt("Id"));
                config.setIdArea_Insumo(jsonObject.getInt("IdAreaInsumo"));
                config.setIdInsumo(jsonObject.getInt("IdInsumo"));
                config.setActivo(jsonObject.getInt("Activo"));
                config.setIdProyecto(jsonObject.getInt("idProyecto"));
                config.setIdUnidad_Medida(jsonObject.getInt("IdUnidadMedida"));
                config.setValor_Min(jsonObject.getDouble("ValorMin"));
                config.setValor_Max(jsonObject.getDouble("ValorMax"));
                config.setSalto(jsonObject.getDouble("Salto"));
                
                Insert_Config_Insumos(context, config);
                collection.add(config);
                
            }
            
        return collection;
        
    }
    
    public static void Insert_Config_Insumos(Context context, mx.smartteam.entities.Insumos_Config config) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        String query = "SELECT COUNT(1) FROM " + BaseDatos.TABLE_CONFIG_INSUMOS
                            + " WHERE " + BaseDatos.COLUMN_ID + " = " + config.getId();
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
            
            int existe = cursor.getInt(0);
            
            if(existe == 0) { // Inserta
                
                String inserta = "INSERT INTO " + BaseDatos.TABLE_CONFIG_INSUMOS
                                    + "("
                                        + BaseDatos.COLUMN_ID + ", "
                                        + BaseDatos.COLUMN_IDAREAINSUMO + ", "
                                        + BaseDatos.COLUMN_IDINSUMO + ", "
                                        + BaseDatos.COLUMN_ACTIVO + ", "
                                        + BaseDatos.COLUMN_IDPROYECTO + ", "
                                        + BaseDatos.COLUMN_IDUNIDADMEDIDA + ", "
                                        + BaseDatos.COLUMN_VALORMIN + ", "
                                        + BaseDatos.COLUMN_VALORMAX + ", "
                                        + BaseDatos.COLUMN_SALTO
                                    + ") VALUES ("
                                        + config.getId() + ", "
                                        + config.getIdArea_Insumo() + ", "
                                        + config.getIdInsumo() + ", "
                                        + config.getActivo() + ", "
                                        + config.getIdProyecto() + ", "
                                        + config.getIdUnidad_Medida() + ", "
                                        + config.getValor_Min() + ", "
                                        + config.getValor_Max() + ", "
                                        + config.getSalto()
                                    + ");";
                db.execSQL(inserta);
            } else { // Actualiza
                
                String actualiza = "UPDATE " + BaseDatos.TABLE_CONFIG_INSUMOS
                                    + " SET "
                                        + BaseDatos.COLUMN_IDAREAINSUMO + " = " + config.getIdArea_Insumo() + ", "
                                        + BaseDatos.COLUMN_IDINSUMO + " = " + config.getIdInsumo() + ", "
                                        + BaseDatos.COLUMN_ACTIVO + " = " + config.getActivo() + ", "
                                        + BaseDatos.COLUMN_IDPROYECTO + " = " + config.getIdProyecto() + ", "
                                        + BaseDatos.COLUMN_IDUNIDADMEDIDA + " = " + config.getIdUnidad_Medida() + ", "
                                        + BaseDatos.COLUMN_VALORMIN + " = " + config.getValor_Min() + ", "
                                        + BaseDatos.COLUMN_VALORMAX + " =  " + config.getValor_Max() + ", "
                                        + BaseDatos.COLUMN_SALTO + " = " + config.getSalto()
                                    + " WHERE " + BaseDatos.COLUMN_ID + " = " + config.getId() + ";";
                db.execSQL(actualiza);
            }
            
        }
        
    }
    
}
