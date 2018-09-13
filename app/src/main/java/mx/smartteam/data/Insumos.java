package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.InsumosCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class Insumos {
 
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.InsumosCollection DownloadInsumos(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        METHOD_NAME = "/GetInsumos";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.InsumosCollection collection = new mx.smartteam.entities.InsumosCollection();
        
        jsonString = new JSONStringer()
               .object()
                   .key("Proyecto")
                   .object()
                       .key("Id").value(proyecto.getId())
                   .endObject()
               .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetInsumosResult");
            
            
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Insumos insumo = new mx.smartteam.entities.Insumos();
                
                insumo.setId(jsonObject.getInt("Id"));
                insumo.setNombre(jsonObject.getString("Nombre"));
                insumo.setActivo(jsonObject.getInt("Activo"));
                
                InsertInsumos(context, insumo);
                collection.add(insumo);
                
            }
            
        return collection;
    }
    
    public static void InsertInsumos(Context context, mx.smartteam.entities.Insumos insumo) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        String query = "SELECT COUNT(1) FROM " + BaseDatos.TABLE_INSUMOS
                            + " WHERE " + BaseDatos.COLUMN_ID + " = " + insumo.getId();
        
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
            int existe = cursor.getInt(0);
            
            if(existe == 0) { // Inserta
                
                String inserta = "INSERT INTO " + BaseDatos.TABLE_INSUMOS
                                    + "("
                                        + BaseDatos.COLUMN_ID + ", "
                                        + BaseDatos.COLUMN_NOMBRE + ", "
                                        + BaseDatos.COLUMN_ACTIVO
                                    + ") VALUES ("
                                        + insumo.getId() + ", "
                                        + "'" + insumo.getNombre() + "', "
                                        + insumo.getActivo()
                                    + ");";
                
                db.execSQL(inserta);
                
            } else { // Actualiza
                
                String actualiza = "UPDATE " + BaseDatos.TABLE_INSUMOS
                                        + " SET "
                                            + BaseDatos.COLUMN_NOMBRE + " = '" + insumo.getNombre() + "', "
                                            + BaseDatos.COLUMN_ACTIVO + " = " + insumo.getActivo()
                                        + " WHERE " + BaseDatos.COLUMN_ID + " = " + insumo.getId() + ";";
                
                db.execSQL(actualiza);
                
            }       
        }   
    }
    
    public static mx.smartteam.entities.InsumosCollection getInsumos(Context context, mx.smartteam.entities.Proyecto proyecto,final mx.smartteam.entities.Areas_Insumos areas_Insumos, mx.smartteam.entities.Usuario usuarios,mx.smartteam.entities.Pop pop) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.InsumosCollection collection = new InsumosCollection();
        
            String select = "SELECT "
                                + " ins." + BaseDatos.COLUMN_NOMBRE + ", "
                                + " config." + BaseDatos.COLUMN_VALORMIN + ", "
                                + " config." + BaseDatos.COLUMN_VALORMAX + ", "
                                + " config." + BaseDatos.COLUMN_SALTO + ", "
                                + " uni." + BaseDatos.COLUMN_NOMBRE + ", "
                                + " uni." + BaseDatos.COLUMN_ABREVIATURA + ", "
                                + " res." + BaseDatos.COLUMN_RESPUESTA + ", "
                                + " DATE(res." + BaseDatos.COLUMN_FECHA + ",'unixepoch'), "
                                + "'" + areas_Insumos.getColor() + "', "
                                + " config." + BaseDatos.COLUMN_ID + ", "
                                + " res." + BaseDatos.COLUMN_ESTATUS
                            + " FROM " + BaseDatos.TABLE_INSUMOS + " AS ins "
                                + " INNER JOIN " + BaseDatos.TABLE_CONFIG_INSUMOS + " AS config ON config." + BaseDatos.COLUMN_IDINSUMO + " = ins." + BaseDatos.COLUMN_ID
                                + " INNER JOIN " + BaseDatos.TABLE_UNIDAD_INSUMOS + " AS uni ON uni." + BaseDatos.COLUMN_ID + " = config." + BaseDatos.COLUMN_IDUNIDADMEDIDA
                                + " LEFT JOIN " + BaseDatos.TABLE_RESPUESTA_INSUMOS + " AS res ON res." + BaseDatos.COLUMN_IDCONFIG + " = config." + BaseDatos.COLUMN_ID 
                                                + " AND res." + BaseDatos.COLUMN_IDVISITA + " = " + pop.getIdVisita() + " AND res." + BaseDatos.COLUMN_IDUSUARIO + " = " + usuarios.getId()
                                    + " WHERE config." + BaseDatos.COLUMN_IDPROYECTO + " = " + proyecto.getId()
                                + " AND ins." + BaseDatos.COLUMN_ACTIVO + " = 1 AND config." + BaseDatos.COLUMN_ACTIVO + " = 1"
                                + " AND config." + BaseDatos.COLUMN_IDAREAINSUMO + " = " + areas_Insumos.getId() + ";";
        
            Cursor cursor = db.rawQuery(select, null);
            
            if(cursor.moveToFirst()) {
                
                do {
                
                    mx.smartteam.entities.Insumos insumos = new mx.smartteam.entities.Insumos();
                
                        insumos.setNombre(cursor.getString(0));
                        insumos.setValorMin(cursor.getDouble(1));
                        insumos.setValorMax(cursor.getDouble(2));
                        insumos.setSalto(cursor.getDouble(3));
                        insumos.setMedida(cursor.getString(4));
                        insumos.setAbreviatura(cursor.getString(5));
                        insumos.setRespuesta(cursor.getString(6));
                        insumos.setFecha(cursor.getString(7));
                        insumos.setColor(cursor.getString(8));
                        insumos.setIdConfig(cursor.getInt(9));
                        insumos.setEstatus(cursor.getInt(10));

                    collection.add(insumos);
                    
                } while(cursor.moveToNext());
                
            }
            
        return collection;
    }
    
}
