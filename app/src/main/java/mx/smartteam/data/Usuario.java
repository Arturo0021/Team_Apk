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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class Usuario {

    private static SQLiteDatabase db = null;
    private static String METHOD_GETUSUARIO = "/getUsuario";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONObject jsonObject = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;

    public static mx.smartteam.entities.Usuario Sinc_Autenticar(String usuario, String password) throws JSONException, IOException, Exception {
        mx.smartteam.entities.Usuario EntityUsuario = null;
        METHOD_GETUSUARIO = "/getUsuario";
        
        ServiceURI service = new ServiceURI();

        jsonString = new JSONStringer()
                            .object()
                                .key("Usuario")
                                .object()
                                    .key("Usuario").value(usuario)
                                    .key("Password").value(password)
                                .endObject()
                            .endObject();

        strEntity = new StringEntity(jsonString.toString());
        jsonResult = service.HttpGet(METHOD_GETUSUARIO, strEntity);


        if (!jsonResult.get("getUsuarioResult").equals(null)) {
            jsonObject = (JSONObject) jsonResult.get("getUsuarioResult");

            EntityUsuario = new mx.smartteam.entities.Usuario();
            EntityUsuario.Id = jsonObject.getInt("Id");
            EntityUsuario.Usuario = jsonObject.getString("Usuario");
            EntityUsuario.Password = jsonObject.getString("Password");
            EntityUsuario.Nombre = jsonObject.getString("Nombre");
            EntityUsuario.Tipo = jsonObject.getInt("Tipo");
            
        }

        return EntityUsuario;

    }
    
    public static void Insert( Context context, mx.smartteam.entities.Usuario usuario) throws Exception{
        
                db = (new BaseDatos(context)).getWritableDatabase();
                
                String insertusuario = "INSERT INTO USUARIO (Id, Usuario, Password, Nombre, Tipo)"
                                            + " SELECT " 
                                                + usuario.Id + ", "
                                                + "'" + usuario.Usuario.toLowerCase() + "', "
                                                + "'" + usuario.Password.toLowerCase() + "', "
                                                + "'" + usuario.Nombre + "', "
                                                + usuario.Tipo
                                            + " WHERE NOT EXISTS (SELECT 1 FROM USUARIO WHERE ID = " + usuario.Id + ")";
      
                db.execSQL(insertusuario);
                
                String config = "INSERT INTO ConfigGPS (IdUsuario, Tipo, conexion) " 
                                    + " SELECT "
                                        + usuario.Id + ", 'nombre', 'online' " 
                                    + " WHERE NOT EXISTS(SELECT 1 FROM ConfigGPS WHERE IdUsuario = " + usuario.Id +");";
                
                db.execSQL(config);
                
                db.close();
            
    }

    public static mx.smartteam.entities.UsuarioCollection GetAll(final Context context) {

        final mx.smartteam.entities.UsuarioCollection usuarioCollection = new mx.smartteam.entities.UsuarioCollection();

        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT Id,Usuario,Password,Nombre FROM USUARIO", null);

            // Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                // Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Usuario usuario = new mx.smartteam.entities.Usuario();
                    usuario.Id = cursor.getInt(0);
                    usuario.Usuario = cursor.getString(1);
                    usuario.Password = cursor.getString(2);
                    usuario.Nombre = cursor.getString(3);

                    usuarioCollection.add(usuario);
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return usuarioCollection;

    }

    public static mx.smartteam.entities.Usuario GetOne(final Context context, Integer Id) {

        mx.smartteam.entities.Usuario usuario = null;

        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT Id,Usuario,Password,Nombre FROM USUARIO WHERE Id=?", new String[]{Id.toString()});

            // Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                // Recorremos el cursor hasta que no haya más registros
                do {
                    usuario = new mx.smartteam.entities.Usuario();
                    usuario.Id = cursor.getInt(0);
                    usuario.Usuario = cursor.getString(1);
                    usuario.Password = cursor.getString(2);
                    usuario.Nombre = cursor.getString(3);


                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("GetOne", e.getMessage());
        } finally {
            db.close();
        }

        return usuario;

    }

    public static mx.smartteam.entities.Usuario GetByUsuario(
            final Context context, mx.smartteam.entities.Usuario usuario) {

        mx.smartteam.entities.Usuario EntityUsuario = null;
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db
                    .rawQuery(
                    "SELECT Id,Usuario,Password,Nombre,Tipo FROM USUARIO WHERE lower(Usuario)=? AND lower(Password)=?",
                    new String[]{usuario.Usuario.toLowerCase(), usuario.Password.toLowerCase()});

            // Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                // Recorremos el cursor hasta que no haya más registros
                do {
                    EntityUsuario = new mx.smartteam.entities.Usuario();
                    EntityUsuario.Id = cursor.getInt(0);
                    EntityUsuario.Usuario = cursor.getString(1);
                    EntityUsuario.Password = cursor.getString(2);
                    EntityUsuario.Nombre = cursor.getString(3);
                    EntityUsuario.Tipo = cursor.getInt(4);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return EntityUsuario;

    }
    
    public static mx.smartteam.entities.Usuario GetByUsuario2(Context context, String usuario) {
    
            db = (new BaseDatos(context)).getWritableDatabase();
            mx.smartteam.entities.Usuario EntityUsuario = new mx.smartteam.entities.Usuario();
            
            String query = "SELECT "
                            + " Id "
                            + ", Usuario "
                            + ", Password "
                            + ", Nombre "
                            + ", Imei "
                            + ", Sim "
                            + ", Telefono "
                                + " FROM USUARIO "
                            + " WHERE Usuario = '" + usuario + "'";
            
            Cursor cursor = db.rawQuery(query, null);

            // Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                // Recorremos el cursor hasta que no haya más registros              
                    EntityUsuario.setId(cursor.getInt(0));
                    EntityUsuario.setUsuario(cursor.getString(1));
                    EntityUsuario.setPassword(cursor.getString(2));
                    EntityUsuario.setNombre(cursor.getString(3));
                    EntityUsuario.setIMEI(cursor.getString(4));
                    EntityUsuario.setSim(cursor.getString(5));
                    EntityUsuario.setTelefono(cursor.getString(6));

            }

        return EntityUsuario;

    }
    
    public static void UpdateDevices(Context context, mx.smartteam.entities.Usuario usuario) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        String update = "UPDATE USUARIO SET "
                                + " Imei = '" + usuario.getIMEI() + "', "
                                    + " Sim = '" + usuario.getSim() + "', "
                                    + " Telefono = '" + usuario.getTelefono() + "'"
                        + " WHERE Id = " + usuario.getId();
        
        db.execSQL(update);
        
    }
    
}
