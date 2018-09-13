package mx.smartteam.data;

import android.content.ContentValues;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import mx.smartteam.entities.CategoriaCollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Categoria {

    private static String METHOD_NAME = "/GetCategoriasProducto";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

        public static CategoriaCollection GetCategoriasProducto(
                mx.smartteam.entities.Proyecto proyecto ,  String time) throws Exception {

            METHOD_NAME = "/GetCategoriasProducto";
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.CategoriaCollection categoriaCollection = new CategoriaCollection();

            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject().endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult
                    .getJSONArray("GetCategoriasProductoResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Categoria categoria = new mx.smartteam.entities.Categoria();
                categoria.Id = jsonObject.getInt("Id");
                categoria.Nombre = jsonObject.getString("Nombre");
                categoria.IdProyecto = proyecto.Id;
                categoria.Tipo = mx.smartteam.entities.Categoria.Type.Producto;
                categoria.FechaSync = jsonObject.getLong("FechaSync");
                categoria.StatusSync = jsonObject.getInt("Statussync");
                categoria.Config =0;
                categoriaCollection.add(categoria);
            }
            return categoriaCollection;

        }

        public static CategoriaCollection GetCategoriasFoto(
                mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time) throws Exception {

            METHOD_NAME = "/GetCategoriasFoto";
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.CategoriaCollection categoriaCollection = new CategoriaCollection();

            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject()
                    .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                    .endObject();


            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult
                    .getJSONArray("GetCategoriasFotoResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Categoria categoria = new mx.smartteam.entities.Categoria();
                categoria.Id = jsonObject.getInt("Id");
                categoria.Nombre = jsonObject.getString("Nombre");
                categoria.IdProyecto = proyecto.Id;
                categoria.Tipo = mx.smartteam.entities.Categoria.Type.Foto;
                categoria.FechaSync = jsonObject.getLong("FechaSync");
                categoria.StatusSync = jsonObject.getInt("Statussync");
                categoria.Config = jsonObject.getInt("Config");
                categoria.Activo = jsonObject.getInt("activo");
                
                categoriaCollection.add(categoria);
            }
            return categoriaCollection;

        }
    }

    public static class Upload {
    }

    public static void Insert(Context context, mx.smartteam.entities.Categoria categoria) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        String query = "SELECT COUNT(1) FROM Categoria WHERE id = " + categoria.Id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            int existe = cursor.getInt(0);
            if(existe == 0) {
                String insert = "INSERT INTO Categoria(Id,Nombre,IdProyecto,Tipo,StatusSync,FechaSync,Config,Activo)"
                        + " VALUES ("
                           + categoria.Id + ",'"
                            + categoria.Nombre + "',"
                            + categoria.IdProyecto + ",'"
                            + categoria.Tipo.toString() + "',"
                            + categoria.StatusSync.toString() + ","
                            + categoria.FechaSync.toString()+ ","
                            + categoria.Config.toString() + ", "
                            + "IFNULL(" + categoria.Activo + ", 1)" 
                        + ");";
                db.execSQL(insert);
            } else {
                String update = "UPDATE Categoria SET "
                                    + " Nombre = '" + categoria.Nombre + "', "
                                    + " IdProyecto = " + categoria.IdProyecto + ", "
                                    + " Tipo = '" + categoria.Tipo.toString() + "', "
                                    + " StatusSync = " + categoria.StatusSync.toString() + ", "
                                    + " FechaSync = " + categoria.FechaSync.toString() + ", "
                                    + " Config = " + categoria.Config.toString() + ", "
                                    + " Activo = " + "IFNULL(" + categoria.Activo + ", 1)"
                                + " WHERE Id = " + categoria.Id + ";";
                db.execSQL(update);
            }
        }

    }

    public static CategoriaCollection GetCategoriaProductoByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto,String tipo) {


        CategoriaCollection categoriaCollection = new CategoriaCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
//            Cursor cursor = db.rawQuery("SELECT id,nombre FROM Categoria WHERE idproyecto=? AND tipo='Producto'", new String[]{proyecto.Id.toString()});
            
            String qquery = "SELECT c.id,c.nombre FROM Categoria c "
                + " INNER JOIN categoriasconfig cc ON cc.idcategoria=c.id and cc.activo = 1 and "+tipo
                + " WHERE c.idproyecto=? AND c.tipo='Producto' AND c.Activo = 1"
                +" ORDER BY cc.orden ";
        Cursor cursor = db.rawQuery(qquery , new String[]{proyecto.Id.toString()});
            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
//                    mx.smartteam.entities.Categoria categoria2 = new mx.smartteam.entities.Categoria();
//                    categoria2.Id = -1;
//                    categoria2.Nombre = "Todo";
//                    categoriaCollection.add(categoria2);
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
    }

    public static CategoriaCollection GetCategoriaFotoByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) {


        CategoriaCollection categoriaCollection = new CategoriaCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT id,nombre FROM Categoria WHERE idproyecto=? AND tipo='Foto' AND Activo = 1", new String[]{proyecto.Id.toString()});

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
    }
}
