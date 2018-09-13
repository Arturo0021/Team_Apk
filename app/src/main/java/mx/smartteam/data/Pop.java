/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.Utilerias;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import mx.smartteam.entities.PopCollection;
import mx.smartteam.entities.ProyectoCollection;

public class Pop {

    private static SQLiteDatabase db = null;
    //private static String METHOD_NAME = "/gettiendasProyecto"; //Todas las tiendas
    private static String METHOD_NAME = "/getConjuntotiendasUsuario";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;

    public static mx.smartteam.entities.PopCollection Sinc_GetByProyecto(mx.smartteam.entities.Proyecto proyecto, String time, mx.smartteam.entities.Usuario usuario ) {
        mx.smartteam.entities.PopCollection EntityPopCollection = new PopCollection();
                
        ServiceURI service = new ServiceURI();
           
        try {

            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(proyecto.Id.toString()) .key("Ufechadescarga").value(time.toString()).endObject()
                    //.key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                        .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            //getConjuntotiendasUsuarioResult
            //jsonArray = jsonResult.getJSONArray("gettiendasProyectoResult"); //Todas las tiendas
            
            jsonArray = jsonResult.getJSONArray("getConjuntotiendasUsuarioResult");
            
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Pop EntityPop = new mx.smartteam.entities.Pop();
                EntityPop.Activo = jsonObject.getInt("Activo");
                EntityPop.Altitud = jsonObject.getDouble("Altitud");
                EntityPop.CP = jsonObject.getInt("CP");
                EntityPop.Calle = jsonObject.getString("Calle");
                EntityPop.Colonia = jsonObject.getString("Colonia");
                EntityPop.Id = jsonObject.getInt("Id");
                EntityPop.Latitud = jsonObject.getDouble("Latitud");
                EntityPop.Longitud = jsonObject.getDouble("Longitud");
                EntityPop.DeterminanteGSP = jsonObject.getInt("DeterminanteGSP");
                EntityPop.Direccion = jsonObject.getString("Direccion");
                EntityPop.IdCadena = jsonObject.getInt("IdCadena");
                EntityPop.IdCanal = jsonObject.getInt("IdCanal");
                EntityPop.IdGrupo = jsonObject.getInt("IdGrupo");
                EntityPop.Sucursal = jsonObject.getString("Sucursal");
                EntityPop.Cadena = jsonObject.getString("Cadena");
                EntityPop.IdProyecto = proyecto.Id;
                EntityPop.StatusSync = jsonObject.getInt("Statussync");
                EntityPop.FechaSync = jsonObject.getLong("FechaSync");
                EntityPopCollection.add(EntityPop);

            }



            /*
             if (!jsonResult.get("getUsuarioResult").equals(null)) {
             jsonObject = (JSONObject) jsonResult.get("getUsuarioResult");

             EntityProyecto = new tracker.entities.Proyecto();
             EntityProyecto.Id = jsonObject.getInt("Id");                
             EntityProyecto.Nombre = jsonObject.getString("Nombre");
             }*/

        } catch (JSONException ex) {
            Logger.getLogger(ServiceURI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceURI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServiceURI.class.getName()).log(Level.SEVERE, null, ex);
        }




        return EntityPopCollection;

    }

    public static void Insert(final Context context, final mx.smartteam.entities.Pop pop, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto ) throws Exception {
        db = (new BaseDatos(context)).getWritableDatabase();

        String qquery = "SELECT COUNT(1) FROM Pop WHERE idproyecto = "+proyecto.Id+" AND idusuario = " +usuario.Id + " AND id = "+ pop.Id +";";
        Cursor cursor =  db.rawQuery(qquery, null);
        
        if(cursor.moveToFirst()){
            int contador = cursor.getInt(0);
        
            if(contador == 0){
                db.execSQL("INSERT INTO POP"
                        + " (Id,"
                        + "Activo"
                        + ",Altitud"
                        + ",CP"
                        + ",Calle"
                        + ",Colonia"
                        + ",Latitud"
                        + ",Longitud"
                        + ",determinanteGSP"
                        + ",direccion"
                        + ",idCadena"
                        + ",idCanal,"
                        + "idGrupo,"
                        + "sucursal,"
                        + "Cadena,"
                        + "IdProyecto,"
                        + "StatusSync,"
                        + "FechaSync,"
                        + "IdUsuario"
                        + ")" + " VALUES ( "
                        + pop.Id + ","
                        + pop.Activo + ","
                        + pop.Altitud + ","
                        + pop.CP + ","
                        + "'" + pop.Calle + "',"
                        + "'" + pop.Colonia + "',"
                        + pop.Latitud + ","
                        + pop.Longitud + ","
                        + pop.DeterminanteGSP + ","
                        + "'" + pop.Direccion + "',"
                        + pop.IdCadena + ","
                        + pop.IdCanal + ","
                        + pop.IdGrupo + ","
                        + "'" + pop.Sucursal + "',"
                        + "'" + pop.Cadena + "',"
                        + pop.IdProyecto + ","
                        + pop.StatusSync + ","
                        + pop.FechaSync+ ","
                        + usuario.Id
                        + " );");
            }else{ /* Actualizar */
                ContentValues values = new ContentValues();
                values.put("DeterminanteGSP", pop.DeterminanteGSP.toString());
                values.put("IdCanal", pop.IdCanal.toString());
                values.put("IdGrupo", pop.IdGrupo.toString());
                values.put("IdCadena", pop.IdCadena.toString());
                values.put("Sucursal", pop.Sucursal.toString());
                values.put("Direccion", pop.Direccion.toString());
                values.put("CP", pop.CP.toString());
                values.put("Latitud", pop.Latitud.toString());
                values.put("Longitud", pop.Longitud.toString());
                values.put("Altitud", pop.Altitud.toString());
                values.put("Activo", pop.Activo.toString());
                values.put("Calle", pop.Calle.toString());
                values.put("Colonia", pop.Colonia.toString());
                values.put("Cadena", pop.Cadena.toString());
                values.put("IdProyecto", pop.IdProyecto.toString());
                values.put("StatusSync", pop.StatusSync.toString());
                values.put("FechaSync", pop.FechaSync.toString());
                db.update("Pop", values, "Id=" + pop.Id.toString() + " AND  IdUsuario = " + usuario.Id+ " AND IdProyecto = " + proyecto.Id , null);
            }        
        }
     db.close();
    }

    public static mx.smartteam.entities.PopCollection GetAll(final Context context) {


//        final tracker.entities.PopCollection popCollection = new tracker.entities.PopCollection();
//        }

        return null;

    }

    public static mx.smartteam.entities.Pop GetById(final Context context, Integer IdPop) {

        mx.smartteam.entities.Pop pop = null;
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Activo,Altitud,CP,Calle,Colonia,Id,Latitud,Longitud,determinanteGSP,direccion,idCadena,idCanal,idGrupo,sucursal FROM POP WHERE ID=?", new String[]{IdPop.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    pop = new mx.smartteam.entities.Pop();
                    pop.Activo = cursor.getInt(0);
                    pop.Altitud = cursor.getDouble(1);
                    pop.CP = cursor.getInt(2);
                    pop.Calle = cursor.getString(3);
                    pop.Colonia = cursor.getString(4);
                    pop.Id = cursor.getInt(5);
                    pop.Latitud = cursor.getDouble(6);
                    pop.Longitud = cursor.getDouble(7);
                    pop.DeterminanteGSP = cursor.getInt(8);
                    pop.Direccion = cursor.getString(9);
                    pop.IdCadena = cursor.getInt(10);
                    pop.IdCanal = cursor.getInt(11);
                    pop.IdGrupo = cursor.getInt(12);
                    pop.Sucursal = cursor.getString(13);


                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return pop;

    }
    
    public static int ValidarFechaVisita(final Context context, mx.smartteam.entities.Pop pop) {
        String fechaCrea = "";
        long t = 0;
        try 
        {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            Cursor cursor = db.rawQuery("SELECT DATE( FechaCrea ,'unixepoch'), FechaCrea FROM POPVISITA WHERE ID=?", new String[]{pop.IdVisita.toString()});
            if (cursor.moveToFirst()) {
                do {
                    fechaCrea = cursor.getString(0);
                    t = cursor.getLong(1);
                    
                    
                } while (cursor.moveToNext());
            }
           //fechaCrea = Utilerias.UnixToDate(t);
            
            
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return Utilerias.ValidarFechas(fechaCrea);
    }
    
    public static mx.smartteam.entities.PopCollection GetByProyecto(final Context context, mx.smartteam.entities.Proyecto entityProyecto) {

        mx.smartteam.entities.PopCollection popCollection = new PopCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Activo,Altitud,CP,Calle,Colonia,Id,Latitud,Longitud,determinanteGSP,direccion,idCadena,idCanal,idGrupo,sucursal FROM POP WHERE IdProyecto=?", new String[]{entityProyecto.Id.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Pop pop = new mx.smartteam.entities.Pop();
                    pop.Activo = cursor.getInt(0);
                    pop.Altitud = cursor.getDouble(1);
                    pop.CP = cursor.getInt(2);
                    pop.Calle = cursor.getString(3);
                    pop.Colonia = cursor.getString(4);
                    pop.Id = cursor.getInt(5);
                    pop.Latitud = cursor.getDouble(6);
                    pop.Longitud = cursor.getDouble(7);
                    pop.DeterminanteGSP = cursor.getInt(8);
                    pop.Direccion = cursor.getString(9);
                    pop.IdCadena = cursor.getInt(10);
                    pop.IdCanal = cursor.getInt(11);
                    pop.IdGrupo = cursor.getInt(12);
                    pop.Sucursal = cursor.getString(13);
                    pop.IdProyecto = cursor.getInt(14);
                    popCollection.add(pop);

                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return popCollection;

    }

    public static mx.smartteam.entities.Pop GetByDeterminante(final Context context, mx.smartteam.entities.Proyecto entityProyecto, Integer determinante, mx.smartteam.entities.Usuario usuario) {

        mx.smartteam.entities.Pop pop = null;
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT "
                    + "Activo,Altitud,CP,Calle,Colonia,Id,Latitud,Longitud,determinanteGSP,direccion,idCadena,idCanal,idGrupo,sucursal,cadena,IdProyecto "
                    + "FROM POP WHERE determinanteGSP=? and IdProyecto=? AND IdUsuario = ? ", 
                    new String[]{determinante.toString(), entityProyecto.Id.toString(), usuario.Id.toString()  });

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    pop = new mx.smartteam.entities.Pop();
                    pop.Activo = cursor.getInt(0);
                    pop.Altitud = cursor.getDouble(1);
                    pop.CP = cursor.getInt(2);
                    pop.Calle = cursor.getString(3);
                    pop.Colonia = cursor.getString(4);
                    pop.Id = cursor.getInt(5);
                    pop.Latitud = cursor.getDouble(6);
                    pop.Longitud = cursor.getDouble(7);
                    pop.DeterminanteGSP = cursor.getInt(8);
                    pop.Direccion = cursor.getString(9);
                    pop.IdCadena = cursor.getInt(10);
                    pop.IdCanal = cursor.getInt(11);
                    pop.IdGrupo = cursor.getInt(12);
                    pop.Sucursal = cursor.getString(13);
                    pop.Cadena = cursor.getString(14);
                    pop.IdProyecto = cursor.getInt(15);
                    


                } while (cursor.moveToNext());

            }


        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return pop;

    }
}
