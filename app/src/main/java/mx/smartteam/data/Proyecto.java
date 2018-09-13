/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.IOException;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import mx.smartteam.entities.ProyectoCollection;

/**
 *
 * @author edgarin.lara
 */
public class Proyecto {

    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "/getproyectosUsuario";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;

    public static mx.smartteam.entities.ProyectoCollection Sinc_GetByUsuario(mx.smartteam.entities.Usuario usuario)
            throws JSONException, IOException, Exception {

        mx.smartteam.entities.ProyectoCollection EntityProyectoCollection = new ProyectoCollection();
        ServiceURI service = new ServiceURI();

        jsonString = new JSONStringer()
                .object()
                    .key("Usuario") 
                    .object()
                        .key("Id").value(usuario.Id.toString())
                        .key("IMEI").value(usuario.IMEI.toString())
                        .key("Sim").value(usuario.Sim.toString())
                        .key("Telefono").value(usuario.Telefono.toString())
                    .endObject()
                .endObject();

        strEntity = new StringEntity(jsonString.toString());
        jsonResult = service.HttpGet(METHOD_NAME, strEntity);
        jsonArray = jsonResult.getJSONArray("getproyectosUsuarioResult");

        for (int i = 0; i < jsonArray.length(); ++i) {
            
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            mx.smartteam.entities.Proyecto EntityProyecto = new mx.smartteam.entities.Proyecto();
            EntityProyecto.Id = jsonObject.getInt("Id");
            EntityProyecto.Nombre = jsonObject.getString("Nombre");
            EntityProyecto.Ficha = jsonObject.getInt("ficha");
            EntityProyectoCollection.add(EntityProyecto);
       
        }
        return EntityProyectoCollection;
    }

    public static void Insert(final Context context, final mx.smartteam.entities.Proyecto proyecto) throws Exception{

        mx.smartteam.entities.Proyecto EntityProyecto = Proyecto.GetById(context, proyecto.Id);

        if (EntityProyecto == null) {
            
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String insert = "INSERT INTO PROYECTO (Id, Nombre, Ficha) "
                                + "VALUES( " + proyecto.Id + ", '" + proyecto.Nombre + "', " + proyecto.getFicha() + ");";
            db.execSQL(insert);
            
            db.close();
        }
    }

    public static mx.smartteam.entities.ProyectoCollection GetAll(final Context context) {


        final mx.smartteam.entities.ProyectoCollection proyectoCollection = new mx.smartteam.entities.ProyectoCollection();


        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Id,Nombre FROM PROYECTO", null);

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Proyecto proyecto = new mx.smartteam.entities.Proyecto();
                    proyecto.Id = cursor.getInt(0);
                    proyecto.Nombre = cursor.getString(1);

                    proyectoCollection.add(proyecto);
                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return proyectoCollection;

    }

    public static mx.smartteam.entities.Proyecto GetById(final Context context, Integer IdProyecto) {

        mx.smartteam.entities.Proyecto EntityProyecto = null;
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Id,Nombre FROM PROYECTO WHERE ID=?", new String[]{IdProyecto.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    EntityProyecto = new mx.smartteam.entities.Proyecto();
                    EntityProyecto.Id = cursor.getInt(0);
                    EntityProyecto.Nombre = cursor.getString(1);


                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return EntityProyecto;

    }
    
     public static mx.smartteam.entities.Proyecto GetFicha(final Context context, Integer IdProyecto) {

        mx.smartteam.entities.Proyecto EntityProyecto = null;
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Id,Nombre, Ficha FROM PROYECTO WHERE ID=?", new String[]{IdProyecto.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    EntityProyecto = new mx.smartteam.entities.Proyecto();
                    EntityProyecto.Id = cursor.getInt(0);
                    EntityProyecto.Nombre = cursor.getString(1);
                    EntityProyecto.Ficha = cursor.getInt(2);


                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return EntityProyecto;

    }
    
    
}
