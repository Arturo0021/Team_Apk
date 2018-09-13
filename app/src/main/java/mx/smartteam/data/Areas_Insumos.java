package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.Areas_InsumosCollection;
import mx.smartteam.entities.Insumos_ConfigCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class Areas_Insumos {

    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static mx.smartteam.entities.Areas_InsumosCollection DownloadAreas(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        METHOD_NAME = "/GetAreasInsumos";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.Areas_InsumosCollection collection = new mx.smartteam.entities.Areas_InsumosCollection();        
        
        jsonString = new JSONStringer()
               .object()
                   .key("Proyecto")
                   .object()
                       .key("Id").value(proyecto.getId())
                   .endObject()
               .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetAreasInsumosResult");
            
            
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Areas_Insumos areas = new mx.smartteam.entities.Areas_Insumos();
                
                areas.setId(jsonObject.getInt("Id"));
                areas.setNombre(jsonObject.getString("Nombre"));
                areas.setColor(jsonObject.getString("Color"));
                areas.setActivo(jsonObject.getInt("Activo"));
                areas.setDescripcion(jsonObject.getString("Descripcion"));
                
                Insert_Area_Insumos(context, areas);
                collection.add(areas);
                
            }
            
        return collection;
        
    }
    
    public static void Insert_Area_Insumos(Context context, mx.smartteam.entities.Areas_Insumos area_insumos) {
        db = (new BaseDatos(context)).getWritableDatabase();
        
        String query = "SELECT COUNT(1) FROM " + BaseDatos.TABLE_AREA_INSUMOS 
                            + " WHERE " + BaseDatos.COLUMN_ID + " = " + area_insumos.getId() + ";";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
            
            int existe = cursor.getInt(0);
            
            if(existe == 0) { // inserta
                
                String inserta = "INSERT INTO " + BaseDatos.TABLE_AREA_INSUMOS
                                    + "("
                                        + BaseDatos.COLUMN_ID + ", "
                                        + BaseDatos.COLUMN_NOMBRE + ", "
                                        + BaseDatos.COLUMN_COLOR + ", "
                                        + BaseDatos.COLUMN_ACTIVO + ", "
                                        + BaseDatos.COLUMN_DESCRIPCION
                                    + ") VALUES ("
                                        + area_insumos.getId() + ", "
                                        + "'" + area_insumos.getNombre() + "', "
                                        + "'" + area_insumos.getColor() + "', "
                                        + area_insumos.getActivo() + ", "
                                        + "'" + area_insumos.getDescripcion() + "'"
                                    + ");";
                db.execSQL(inserta);
                
            } else { // actualiza
                
                String actualiza = "UPDATE " + BaseDatos.TABLE_AREA_INSUMOS
                                        + " SET "  
                                            + BaseDatos.COLUMN_NOMBRE + " = '" + area_insumos.getNombre() + "', "
                                            + BaseDatos.COLUMN_COLOR + " = '" + area_insumos.getColor() + "', "
                                            + BaseDatos.COLUMN_ACTIVO + " = " + area_insumos.getActivo() + ", "
                                            + BaseDatos.COLUMN_DESCRIPCION + " = " + "'" + area_insumos.getDescripcion() + "'"
                                        + " WHERE " + BaseDatos.COLUMN_ID + " = " + area_insumos.getId() + ";";
                db.execSQL(actualiza);
            }
        }    
    }
    
    public static mx.smartteam.entities.Areas_InsumosCollection getArea_Insumos(Context context, mx.smartteam.entities.Proyecto proyecto) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.Areas_InsumosCollection collection = new Areas_InsumosCollection();
        
        String select = "SELECT DISTINCT " 
                            + "area." + BaseDatos.COLUMN_ID + ", "
                            + "area." + BaseDatos.COLUMN_NOMBRE + ", "
                            + "area." + BaseDatos.COLUMN_COLOR + ", "
                            + "area." + BaseDatos.COLUMN_ACTIVO + ", "
                            + "area." + BaseDatos.COLUMN_DESCRIPCION
                        + " FROM " + BaseDatos.TABLE_AREA_INSUMOS + " AS area "
                            + " INNER JOIN " + BaseDatos.TABLE_CONFIG_INSUMOS + " as config ON config." + BaseDatos.COLUMN_IDAREAINSUMO + " = area." + BaseDatos.COLUMN_ID
                        + " WHERE config." + BaseDatos.COLUMN_ACTIVO + " = 1 AND area." + BaseDatos.COLUMN_ACTIVO + " = 1 AND "
                        + "config." + BaseDatos.COLUMN_IDPROYECTO + " = " + proyecto.getId() + ";";
    Cursor cursor = db.rawQuery(select, null);
        
        if(cursor.moveToFirst()) {
            
            do {
                
                mx.smartteam.entities.Areas_Insumos area = new mx.smartteam.entities.Areas_Insumos();
                    area.setId(cursor.getInt(0));
                    area.setNombre(cursor.getString(1));
                    area.setColor(cursor.getString(2));
                    area.setActivo(cursor.getInt(3));
                    area.setDescripcion(cursor.getString(4));
                collection.add(area);
                
            } while(cursor.moveToNext());
            
        }
        
        return collection;
    }
    
    
}
