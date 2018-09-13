package mx.smartteam.data;

import android.content.ContentValues;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import mx.smartteam.entities.FormularioCollection;
import mx.smartteam.entities.SondeoModulosCollection;

public class Formulario {

    private static String METHOD_NAME = "/GetFormularios";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

        public static mx.smartteam.entities.FormularioCollection GetByProyectoUsuario(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception {

            mx.smartteam.entities.FormularioCollection formularioCollection = new FormularioCollection();
            ServiceURI service = new ServiceURI();

            try {
                jsonString = new JSONStringer().object()
                        .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                        .key("Proyecto").object()
                        .key("Id").value(proyecto.Id.toString())
                        .key("Ufechadescarga").value(time.toString())
                        .endObject().endObject();

                strEntity = new StringEntity(jsonString.toString());
                jsonResult = service.HttpGet(METHOD_NAME, strEntity);
                jsonArray = jsonResult.getJSONArray("GetFormulariosResult");

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mx.smartteam.entities.Formulario formulario = new mx.smartteam.entities.Formulario();
                    
                    formulario.setActivo(jsonObject.getInt("Activo"));
                    formulario.setId(jsonObject.getInt("Id"));
                    formulario.setNombre(jsonObject.getString("Nombre"));
                    formulario.setIdProyecto(jsonObject.getInt("IdProyecto"));
                    formulario.setFechaSync(jsonObject.getLong("FechaSync"));
                    formulario.setStatusSync(jsonObject.getInt("Statussync"));
                    formulario.setOrden(jsonObject.getInt("Orden"));
                    formulario.setTipo(jsonObject.getString("Tipo"));
                    formulario.setTitulo(jsonObject.getString("Titulo"));
                    formulario.setRequerido(jsonObject.getInt("requerido"));
                    
                    formularioCollection.add(formulario);
                }
            } catch (Exception ex) {
                throw new Exception("GetOrdenFormularios", ex);
            }
            return formularioCollection;
        }
    }

    public static class Upload {
    }

    public static void Insert(final Context context, final mx.smartteam.entities.Formulario formulario) {
        db = (new BaseDatos(context)).getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery("Select COUNT(Id) FROM Formulario WHERE id = "+ formulario.getId() +";", null);
            if(cursor.moveToFirst()){
                int contador = cursor.getInt(0);
                if(contador == 0){ /*Insertamos el registro si no existe */
                    
                    String query = "INSERT INTO Formulario"
                        + " (Activo, Id, Nombre, Orden, Tipo, idProyecto, FechaSync, StatusSync, Titulo, Requerido)" 
                                + " SELECT "
                                        + formulario.getActivo() + ", "
                                        + formulario.getId() + ", "
                                        + "'" + formulario.getNombre() + "', "
                                        + formulario.getOrden() + ", "
                                        + "'" + formulario.getTipo() + "', "
                                        + formulario.getIdProyecto() + ", "
                                        + formulario.getFechaSync() + ", "
                                        + formulario.getStatusSync() + ", "
                                        + "'" + formulario.getTitulo() + "', "
                                        + formulario.getRequerido()
                                + " WHERE NOT EXISTS( SELECT 1 FROM Formulario WHERE ID = " + formulario.getId() + " )";
                    
                        db.execSQL(query);
                    
                } else {/*  Actualizamos el registro si ya existe*/
                    ContentValues values = new ContentValues();
                    values.put("Activo", formulario.getActivo().toString());
                    values.put("Nombre", formulario.getNombre());
                    values.put("IdProyecto", formulario.getIdProyecto().toString());
                    values.put("FechaSync", formulario.getFechaSync().toString());
                    values.put("StatusSync", formulario.getStatusSync().toString());
                    values.put("Orden", formulario.getOrden().toString());
                    values.put("Tipo", formulario.getTipo());
                    values.put("Titulo", formulario.getTitulo());
                    values.put("Requerido", formulario.getRequerido());
                    db.update("Formulario", values, "Id = " + formulario.getId().toString(), null);
                }
            }
        }catch(Exception ex){
            ex.getMessage().toString();
        }
        
        db.close();
    }

    public static mx.smartteam.entities.FormularioCollection GetByProyecto(final Context context, mx.smartteam.entities.Proyecto proyecto) {

        mx.smartteam.entities.FormularioCollection formularioCollection = new FormularioCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String query = "SELECT id,idproyecto,nombre,orden, Tipo, Requerido FROM formulario WHERE idproyecto = " + proyecto.getId() + ";";
            
            Cursor cursor = db.rawQuery(query, null);

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Formulario formulario = new mx.smartteam.entities.Formulario();
                    
                    formulario.setId(cursor.getInt(0));
                    formulario.setIdProyecto(cursor.getInt(1));
                    formulario.setNombre(cursor.getString(2).replace(" ", "_"));
                    formulario.setOrden(cursor.getInt(3));
                    formulario.setTipo(cursor.getString(4));
                    formulario.setRequerido(cursor.getInt(5));
                    formularioCollection.add(formulario);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return formularioCollection;

    }
     public static mx.smartteam.entities.SondeoModulosCollection GetByProyectosm(final Context context, mx.smartteam.entities.Proyecto proyecto) {
        mx.smartteam.entities.SondeoModulosCollection sondeoModulosCollection = new SondeoModulosCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String query = "SELECT id,idproyecto,nombre,orden, Tipo, Titulo, Requerido FROM formulario WHERE idproyecto = " + proyecto.getId() + " AND activo = 1;";
            
            Cursor cursor = db.rawQuery(query, null);

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    // int answer = rn.nextInt(10)+1 ;
                    mx.smartteam.entities.SondeoModulos formulario = new mx.smartteam.entities.SondeoModulos();
                    
                        formulario.setId(cursor.getInt(0));
                        formulario.setIdProyecto(cursor.getInt(1));
                        formulario.setNombre(cursor.getString(2).replace(" ", "_"));
                        formulario.setOrden(cursor.getInt(3));
                        formulario.setTipo(cursor.getString(4));
                        formulario.setTitulo(cursor.getString(5));
                        formulario.setRequerido(cursor.getInt(6));
                    
                    sondeoModulosCollection.add(formulario);
                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return sondeoModulosCollection;

    }
     
     public static mx.smartteam.entities.SondeoModulosCollection GetFlagFoto(final Context context, mx.smartteam.entities.Proyecto proyecto) {
        // Random rn = new Random();
       
        mx.smartteam.entities.SondeoModulosCollection sondeoModulosCollection = new SondeoModulosCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String query = "SELECT id,idproyecto,nombre,orden, Tipo, Titulo, Requerido FROM formulario WHERE idproyecto = " + proyecto.getId() + " AND nombre = 'asistencia' limit 1;";
            
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {                
                do {
                    mx.smartteam.entities.SondeoModulos formulario = new mx.smartteam.entities.SondeoModulos();
                    
                    formulario.setId(cursor.getInt(0));
                    formulario.setIdProyecto(cursor.getInt(1));
                    formulario.setNombre(cursor.getString(2).replace(" ", "_"));
                    formulario.setOrden(cursor.getInt(3));
                    formulario.setTipo(cursor.getString(4));
                    formulario.setTitulo(cursor.getString(5));
                    formulario.setRequerido(cursor.getInt(6));
                    
                    sondeoModulosCollection.add(formulario);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return sondeoModulosCollection;
    }
}
