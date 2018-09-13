package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class Unidad_Medida {
    
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
     public static mx.smartteam.entities.Unidad_MedidaCollection DownloadUnidad(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        METHOD_NAME = "/GetUnidadesDeMedida";
        ServiceURI service = new ServiceURI();
        mx.smartteam.entities.Unidad_MedidaCollection collection = new mx.smartteam.entities.Unidad_MedidaCollection();
        
        jsonString = new JSONStringer()
               .object()
                   .key("Proyecto")
                   .object()
                       .key("Id").value(proyecto.getId())
                   .endObject()
               .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult.getJSONArray("GetUnidadesDeMedidaResult");
            
            
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Unidad_Medida medida = new mx.smartteam.entities.Unidad_Medida();
                
                    medida.setId(jsonObject.getInt("Id"));
                    medida.setNombre(jsonObject.getString("Nombre"));
                    medida.setAbreviatura(jsonObject.getString("Abreviatura"));
                
                    Insert_Unidad_Insumos(context, medida);
                collection.add(medida);
                
            }
            
        return collection;
    }
     
     public static void Insert_Unidad_Insumos(Context context, mx.smartteam.entities.Unidad_Medida medida) {
         
         db = (new BaseDatos(context)).getWritableDatabase();
         String select = "SELECT COUNT(1) FROM " + BaseDatos.TABLE_UNIDAD_INSUMOS
                            + " WHERE " + BaseDatos.COLUMN_ID + " = " + medida.getId() + ";";
         Cursor cursor = db.rawQuery(select, null);
         
         if(cursor.moveToFirst()) {
             
             int existe = cursor.getInt(0);
             if(existe == 0) { // Inserta
                 
                 String inserta = "INSERT INTO " + BaseDatos.TABLE_UNIDAD_INSUMOS
                                        + "("
                                            + BaseDatos.COLUMN_ID + ", "
                                            + BaseDatos.COLUMN_NOMBRE + ", "
                                            + BaseDatos.COLUMN_ABREVIATURA
                                        + ") VALUES ("
                                            + medida.getId() + ", "
                                            + "'" + medida.getNombre() + "', "
                                            + "'" + medida.getAbreviatura() + "'"
                                        + ");";
                 
                 db.execSQL(inserta);
                 
             } else { // Actualiza
                 
                 String actualiza = "UPDATE " + BaseDatos.TABLE_UNIDAD_INSUMOS
                                        + " SET "
                                            + BaseDatos.COLUMN_NOMBRE + " = " + "'" + medida.getNombre() + "', " 
                                            + BaseDatos.COLUMN_ABREVIATURA + " = " + "'" + medida.getAbreviatura() + "'"
                                        + " WHERE " + BaseDatos.COLUMN_ID + " = " + medida.getId() + ";";
                 
                 db.execSQL(actualiza);
                 
             }
             
         }
         
     }
    
}
