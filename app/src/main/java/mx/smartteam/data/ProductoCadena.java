
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.ProductoCadenaCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class ProductoCadena {

    private static String METHOD_NAME = null;
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

        public static ProductoCadenaCollection GetProductosCadena(
                mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {
                METHOD_NAME = "/GetProductosCadena";

            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.ProductoCadenaCollection productoCadenaCollection = new ProductoCadenaCollection();



            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject().endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult
                    .getJSONArray("GetProductosCadenaResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.ProductoCadena productoCadena = new mx.smartteam.entities.ProductoCadena();
                productoCadena.IdCadena = jsonObject.getInt("IdCadena");
                productoCadena.IdProyecto = jsonObject.getInt("IdProyecto");
                productoCadena.Sku = jsonObject.getLong("SKU");
                productoCadena.FechaSync = jsonObject.getLong("FechaSync");
                productoCadena.StatusSync = jsonObject.getInt("Statussync");

                productoCadenaCollection.add(productoCadena);
            }
            return productoCadenaCollection;

        }
        
        
       public static ProductoCadenaCollection GetConjuntoCadenasbyUsuario(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time)throws Exception{
           METHOD_NAME = "/GetProductosCadenaUsuario";
           ServiceURI service = new ServiceURI();
            mx.smartteam.entities.ProductoCadenaCollection productoCadenaCollection = new ProductoCadenaCollection();

               
                
                jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString()).endObject(). 
                     key("Usuario").object()
                    .key("Id") .value(usuario.Id)  
                     .endObject().
                        
                    endObject();
                
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);           
            jsonArray = jsonResult
                    .getJSONArray("GetProductosCadenaUsuarioResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.ProductoCadena productoCadena = new mx.smartteam.entities.ProductoCadena();
                productoCadena.IdCadena = jsonObject.getInt("IdCadena");
                productoCadena.IdProyecto = jsonObject.getInt("IdProyecto");
                productoCadena.Sku = jsonObject.getLong("SKU");
               // productoCadena.FechaSync = jsonObject.getLong("FechaSync");
                productoCadena.StatusSync = jsonObject.getInt("Statussync");
                productoCadena.activo = jsonObject.getInt("Activo");
                productoCadena.ppromocion = jsonObject.getDouble("PPromocion");
                productoCadena.psugerido = jsonObject.getDouble("PSugerido");

                productoCadenaCollection.add(productoCadena);
            }
            
            
            return productoCadenaCollection;
       }
       
       
         public static ProductoCadenaCollection GetConjuntoProductoCadena(mx.smartteam.entities.Proyecto proyecto, String time,mx.smartteam.entities.ProductoCadena productoCadena)throws Exception{
           METHOD_NAME = "/GetConjuntoProductoCadena";
           ServiceURI service = new ServiceURI();
            mx.smartteam.entities.ProductoCadenaCollection productoCadenaCollection = new ProductoCadenaCollection();

           jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString()).endObject()
                    .key("ProductosCadena").object()
                    .key("IdCadena").value(productoCadena.IdCadena.toString()).endObject().
                    endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
           
            jsonArray = jsonResult
                    .getJSONArray("GetConjuntoProductoCadenaResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.ProductoCadena productoCadena2 = new mx.smartteam.entities.ProductoCadena();
                productoCadena.IdCadena = jsonObject.getInt("IdCadena");
                productoCadena.IdProyecto = jsonObject.getInt("IdProyecto");
                productoCadena.Sku = jsonObject.getLong("SKU");
                productoCadena.FechaSync = jsonObject.getLong("FechaSync");
                productoCadena.StatusSync = jsonObject.getInt("Statussync");
               /* productoCadena.activo = jsonObject.getInt("activo");
                productoCadena.ppromocion = jsonObject.getDouble("ppromocion");
                productoCadena.psugerido = jsonObject.getDouble("psugerido"); */

                productoCadenaCollection.add(productoCadena);
            }
            
            
            return productoCadenaCollection;
       }
       
       
       
    }//End Download

    public static class Upload {
    }

    public static void Insert(Context context, mx.smartteam.entities.ProductoCadena productoCadena) throws Exception{
        
            db = (new BaseDatos(context)).getWritableDatabase();
            
                switch(productoCadena.StatusSync){
                    
                    case 1: 
                    db.execSQL("INSERT INTO ProductoCadena"
                    + " (IdProyecto,IdCadena,Sku,Statussync,activo,psugerido,ppromocion  )" + " SELECT "
                    + productoCadena.IdProyecto + ","
                    + productoCadena.IdCadena + ","
                    + productoCadena.Sku +","
                   // + productoCadena.FechaSync +","
                    + productoCadena.StatusSync  +","
                    + productoCadena.activo +","
                    + productoCadena.psugerido +","  
                    + productoCadena.ppromocion 
                       + " WHERE NOT EXISTS(SELECT 1 FROM ProductoCadena WHERE IdProyecto=" + productoCadena.IdProyecto + " and IdCadena=" + productoCadena.IdCadena + " and Sku=" + productoCadena.Sku + " )");


                        break;

                    case 2:
                    db.execSQL("INSERT INTO ProductoCadena"
                    + " (IdProyecto,IdCadena,Sku,Statussync,activo,psugerido,ppromocion  )" + " SELECT "
                    + productoCadena.IdProyecto + ","
                    + productoCadena.IdCadena + ","
                    + productoCadena.Sku +","
                    //+ productoCadena.FechaSync +","
                    + productoCadena.StatusSync  +","
                    + productoCadena.activo +","
                    + productoCadena.psugerido +","  
                    + productoCadena.ppromocion
                    + " WHERE NOT EXISTS(SELECT 1 FROM ProductoCadena WHERE IdProyecto=" + productoCadena.IdProyecto + " and IdCadena=" + productoCadena.IdCadena + " and Sku=" + productoCadena.Sku + " )");
                         ContentValues values = new ContentValues();
                        values.put("IdProyecto", productoCadena.IdProyecto.toString());
                        values.put("IdCadena", productoCadena.IdCadena.toString());
                        values.put("Sku", productoCadena.Sku.toString());
                        values.put("StatusSync", productoCadena.StatusSync.toString());
                        values.put("FechaSync", productoCadena.FechaSync.toString());
                        values.put("activo", productoCadena.activo.toString());
                        values.put("psugerido", productoCadena.psugerido.toString());
                        values.put("ppromocion", productoCadena.ppromocion.toString());
                        db.update("Productocadena ", values, "IdProyecto=" + productoCadena.IdProyecto + " AND Sku=" + productoCadena.Sku.toString() + " AND  idcadena = " + productoCadena.IdCadena.toString(), null);
                        break;
                }
            
            db.close();

    }
    /*
     public static CategoriaCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) {


     CategoriaCollection categoriaCollection = new CategoriaCollection();
     try {
     db = (new BaseDatos(context)).getWritableDatabase();
     Cursor cursor = db.rawQuery("SELECT id,nombre FROM Categoria WHERE idproyecto=?", new String[]{proyecto.Id.toString()});

     //Nos aseguramos de que existe al menos un registro
     if (cursor.moveToFirst()) {
     //Recorremos el cursor hasta que no haya más registros
     do {
     mx.smartteam.entities.Categoria categoria = new mx.smartteam.entities.Categoria();
     categoria.Id = cursor.getInt(0);
     categoria.Nombre = cursor.getString(1);
     categoriaCollection.add(categoria);
     } while (cursor.moveToNext());
     }
     } catch (Exception e) {
     Log.i("Insert", e.getMessage());
     } finally {
     db.close();
     }

     return categoriaCollection;
     }*/
}
