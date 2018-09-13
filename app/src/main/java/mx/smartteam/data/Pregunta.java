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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.Proyecto;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


public class Pregunta {

    private static String METHOD_NAME = "/GetPreguntasPorSondeo2";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

        public static PreguntaCollection GetPreguntas(
                mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Sondeo sondeo, String time) throws Exception {

            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.PreguntaCollection preguntaCollection = new PreguntaCollection();

            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject().key("Usuario").object().key("Id")
                    .value(usuario.Id.toString()).endObject().key("Encuesta").object().key("Id").value(sondeo.Id.toString()).endObject().endObject();

            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            jsonArray = jsonResult.getJSONArray("GetPreguntasPorSondeo2Result");

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
                pregunta.Id = jsonObject.getInt("Id");
                pregunta.Nombre = jsonObject.getString("Nombre");
                pregunta.Tipo = jsonObject.getString("Tipo");
                pregunta.Longitud = jsonObject.getInt("Maximo");
                pregunta.Requerido = jsonObject.getInt("Obligacion") == 0 ? false : true;
                pregunta.IdSondeo = sondeo.Id;
                pregunta.FechaSync = jsonObject.getLong("FechaSync");
                pregunta.StatusSync = jsonObject.getInt("Statussync");
                pregunta.clave = jsonObject.getString("clave");
                pregunta.Activo = jsonObject.getInt("Activo");

                //Opciones
                JSONArray jsonArrayOpciones = jsonObject.getJSONArray("Opciones");
                for (int j = 0; j < jsonArrayOpciones.length(); j++) {
                    JSONObject objOpc = jsonArrayOpciones.getJSONObject(j);
                    Opcion opc = new Opcion();
                    opc.Id = objOpc.getInt("Id");
                    opc.Nombre = objOpc.getString("Nombre");
                    opc.Activo = objOpc.getInt("Activo");
                    pregunta.Opciones.add(opc);
                }

                preguntaCollection.add(pregunta);
            }

            return preguntaCollection;

        }
    }

    public static void Insert(Context context, mx.smartteam.entities.Pregunta pregunta) throws Exception { // Este es el chingon para que yo lo identifique rapido XD ARTSG

        db = (new BaseDatos(context)).getWritableDatabase();

        switch (pregunta.StatusSync) {
            case 1:
                Cursor cursor = db.query("Pregunta", null, "Id=?", new String[]{pregunta.Id.toString()}, null, null, null);

                if (!cursor.moveToFirst()) {
                    ContentValues values = new ContentValues();
                    values.put("Id", pregunta.Id);
                    values.put("IdSondeo", pregunta.IdSondeo);
                    values.put("Nombre", pregunta.Nombre);
                    values.put("Tipo", pregunta.Tipo);
                    values.put("Longitud", pregunta.Longitud);
                    values.put("Requerido", pregunta.Requerido);
                    values.put("FechaSync", pregunta.FechaSync);
                    values.put("StatusSync", pregunta.StatusSync);
                    values.put("clave", pregunta.clave);
                    values.put("Activo", pregunta.Activo);
                    db.insert("Pregunta", null, values);

                    //Insertamos Opciones de la pregunta
                    for (Opcion opcion : pregunta.Opciones) {
                        ContentValues valuesOpc = new ContentValues();
                        valuesOpc.put("Id", opcion.Id);
                        valuesOpc.put("IdPregunta", pregunta.Id);
                        valuesOpc.put("Nombre", opcion.Nombre);
                        valuesOpc.put("FechaSync", pregunta.FechaSync);
                        valuesOpc.put("StatusSync", pregunta.StatusSync);
                        valuesOpc.put("Activo", pregunta.Activo);
                        db.insert("PreguntaOpcion", null, valuesOpc);
                    }
                }
                break;

            case 2:
                Cursor cursor2 = db.query("Pregunta", null, "Id=?", new String[]{pregunta.Id.toString()}, null, null, null);

                if (!cursor2.moveToFirst()) {
                    ContentValues values = new ContentValues();
                    values.put("Id", pregunta.Id);
                    values.put("IdSondeo", pregunta.IdSondeo);
                    values.put("Nombre", pregunta.Nombre);
                    values.put("Tipo", pregunta.Tipo);
                    values.put("Longitud", pregunta.Longitud);
                    values.put("Requerido", pregunta.Requerido);
                    values.put("FechaSync", pregunta.FechaSync);
                    values.put("StatusSync", pregunta.StatusSync);
                    values.put("clave", pregunta.clave);
                    values.put("Activo", pregunta.Activo);
                    db.insert("Pregunta", null, values);

                    //Insertamos Opciones de la pregunta
                    for (Opcion opcion : pregunta.Opciones) {
                        ContentValues valuesOpc = new ContentValues();
                        valuesOpc.put("Id", opcion.Id);
                        valuesOpc.put("IdPregunta", pregunta.Id);
                        valuesOpc.put("Nombre", opcion.Nombre);
                        valuesOpc.put("FechaSync", pregunta.FechaSync);
                        valuesOpc.put("StatusSync", pregunta.StatusSync);
                        valuesOpc.put("Activo", pregunta.Activo);
                        db.insert("PreguntaOpcion", null, valuesOpc);
                    }
                }

                ContentValues value = new ContentValues();
                value.put("IdSondeo", pregunta.IdSondeo);
                value.put("Nombre", pregunta.Nombre);
                value.put("Tipo", pregunta.Tipo);
                value.put("Longitud", pregunta.Longitud);
                value.put("Requerido", pregunta.Requerido);
                value.put("FechaSync", pregunta.FechaSync);
                value.put("StatusSync", pregunta.StatusSync);
                value.put("clave", pregunta.clave);
                value.put("Activo", pregunta.Activo);
                db.update("Pregunta", value, "Id=" + pregunta.Id.toString(), null);
                for (Opcion opcion1 : pregunta.Opciones) {

                    ContentValues valueOpc = new ContentValues();
                    valueOpc.put("IdPregunta", pregunta.Id);
                    valueOpc.put("Nombre", opcion1.Nombre);
                    valueOpc.put("FechaSync", pregunta.FechaSync);
                    valueOpc.put("StatusSync", pregunta.StatusSync);
                    valueOpc.put("Activo", pregunta.Activo);
                    db.update("PreguntaOpcion", valueOpc, "Id=" + opcion1.Id.toString(), null);

                }
                break;
        }

//        if(pregunta.StatusSync >= 1){
//        
//        ContentValues value = new ContentValues();
//            value.put("IdSondeo", pregunta.IdSondeo);
//            value.put("Nombre", pregunta.Nombre);
//            value.put("Tipo", pregunta.Tipo);
//            value.put("Longitud", pregunta.Longitud);
//            value.put("Requerido", pregunta.Requerido);
//            value.put("FechaSync", pregunta.FechaSync);
//            value.put("StatusSync", pregunta.StatusSync );
//
//            db.update("Pregunta", value, "Id=" + pregunta.Id.toString(), null);
//            for(Opcion opcion : pregunta.Opciones){
//   
//            ContentValues valueOpc = new ContentValues();
//                valueOpc.put("IdPregunta", pregunta.Id);
//                valueOpc.put("Nombre", opcion.Nombre);
//                valueOpc.put("FechaSync", pregunta.FechaSync);
//                valueOpc.put("StatusSync", pregunta.StatusSync );
//                db.update("PreguntaOpcion", valueOpc, "Id=" + opcion.Id.toString(), null);
//            
//            }
//            
//        }
        db.close();
    }

    public static PreguntaCollection GetPreguntaBySondeo(Context context, mx.smartteam.entities.SondeoModulos sondeo) {
        PreguntaCollection preguntaCollection = new PreguntaCollection();

        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String query = "SELECT Id,Nombre,Tipo,Longitud,Requerido FROM Pregunta WHERE clave = 0 AND IdSondeo = " + sondeo.Id + ";";
            
            Cursor cursor = db.rawQuery(query, null);

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
                    pregunta.Id = cursor.getInt(0);
                    pregunta.Nombre = cursor.getString(1);
                    pregunta.IdSondeo = sondeo.Id;
                    pregunta.Tipo = cursor.getString(2);
                    pregunta.Longitud = cursor.getInt(3);
                    pregunta.Requerido = cursor.getInt(4) == 0 ? false : true;

                    //Opciones de pregunta
                    String preg = "SELECT Id,Nombre,IdPregunta FROM PreguntaOpcion WHERE IdPregunta = " + pregunta.Id + ";";
                    
                    Cursor cursorOpc = db.rawQuery(preg, null);
                    if (cursorOpc.moveToFirst()) {
                        do {
                            Opcion opcion = new Opcion();
                            opcion.Id = cursorOpc.getInt(0);
                            opcion.Nombre = cursorOpc.getString(1);
                            pregunta.Opciones.add(opcion);
                        } while (cursorOpc.moveToNext());
                    }

                    preguntaCollection.add(pregunta);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return preguntaCollection;
    }
      
    public static mx.smartteam.entities.Pregunta GetPreguntaTipo3(Context context, mx.smartteam.entities.Sondeo sondeo){
          mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
        try {
        db = (new BaseDatos(context)).getWritableDatabase();
         Cursor cursor = db.rawQuery(" select Id,IdSondeo,Nombre,Tipo,Clave  from pregunta where idSondeo=? and clave=3 ", 
                 new String[]{ sondeo.Id.toString()});
           if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {                   
                    pregunta.Id = cursor.getInt(0);
                    pregunta.IdSondeo=cursor.getInt(1);
                    pregunta.Nombre=cursor.getString(2);
                    pregunta.Tipo=cursor.getString(3);
                    pregunta.clave=cursor.getString(4);
                  } while (cursor.moveToNext());
            }
            } catch (Exception e) {           
        } finally {
            db.close();
        }
        return pregunta;           
    }   
    
        public static mx.smartteam.entities.Pregunta GetPreguntaNContestaciones(Context context, mx.smartteam.entities.Sondeo sondeo){
          mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
        try {
        db = (new BaseDatos(context)).getWritableDatabase();
         Cursor cursor = db.rawQuery(" select Id,IdSondeo,Nombre,Tipo,Clave  from pregunta where idSondeo=? and clave = 1 ", 
                 new String[]{ sondeo.Id.toString()});
           if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {        
                    pregunta.Id = cursor.getInt(0);
                    pregunta.IdSondeo=cursor.getInt(1);
                    pregunta.Nombre=cursor.getString(2);
                    pregunta.Tipo=cursor.getString(3);
                    pregunta.clave=cursor.getString(4);
                  } while (cursor.moveToNext());
            }
            } catch (Exception e) {           
        } finally {
            db.close();
        }
        return pregunta;           
    }   
    
    
    public static PreguntaCollection GetPreguntaDinamica(Context context, mx.smartteam.entities.SondeoModulos sondeo, mx.smartteam.entities.Pop pop) {
        PreguntaCollection preguntaCollection = new PreguntaCollection();

        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Id,Nombre,Tipo,Longitud,Requerido FROM Pregunta WHERE IdSondeo=? AND clave = '1' LIMIT 1", new String[]{sondeo.Id.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
                    pregunta.Id = cursor.getInt(0);
                    pregunta.Nombre = cursor.getString(1);
                    pregunta.IdSondeo = sondeo.Id;
                    pregunta.Tipo = cursor.getString(2);
                    pregunta.Longitud = cursor.getInt(3);
                    pregunta.Requerido = cursor.getInt(4) == 0 ? false : true;

                    //Opciones de pregunta
                    Cursor cursorOpc = db.rawQuery("SELECT Id,Nombre,IdPregunta FROM PreguntaOpcion WHERE IdPregunta=?", new String[]{pregunta.Id.toString()});
                    if (cursorOpc.moveToFirst()) {
                        do {
                            Opcion opcion = new Opcion();
                            opcion.Id = cursorOpc.getInt(0);
                            opcion.Nombre = cursorOpc.getString(1);
                            
                            // obtenemos los items que ya han sido seleccionados
                            Cursor newCursor = db.rawQuery("SELECT cpo.idOpcion ,c.id as item FROM Contestacion c Inner Join ContestacionPregunta cp ON cp.idcontestacion = c.id INNER JOIN ContestacionPreguntaOpcion cpo ON cpo.IdContestacionPregunta = cp.id WHERE c.idvisita = ? AND c.idsondeo = ? AND cp.idpregunta = ? AND cpo.idopcion = ?", 
                                new String[]{pop.IdVisita.toString(),pregunta.IdSondeo.toString(), pregunta.Id.toString(), opcion.Id.toString() });
                            if(newCursor.moveToFirst())
                            {
                                do{
                                    //Si es diferente de nulo vamos a asignarle a la opcion status1
                                    Opcion opcionx = new Opcion();
                                    opcionx.Id = newCursor.getInt(0);
                                    opcionx.Constestacion = newCursor.getInt(1);
                                    if(opcionx.Id>0){
                                        opcion.Status =1;
                                    }
                                    
                                    if(opcionx.Constestacion>0){
                                        opcion.Constestacion = opcionx.Constestacion;
                                    }
                                    
                                    
                                }while(newCursor.moveToNext());
                            }
                            pregunta.Opciones.add(opcion);
                        } while (cursorOpc.moveToNext());
                    }

                    preguntaCollection.add(pregunta);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return preguntaCollection;
    }
    
     
     public static PreguntaCollection GetNPreguntaDinamica(Context context, mx.smartteam.entities.SondeoModulos sondeo, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Opcion nopcion
                                                            , mx.smartteam.entities.Pregunta pregunta1 ) {
        PreguntaCollection preguntaCollection = new PreguntaCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Id,Nombre,Tipo,Longitud,Requerido FROM Pregunta WHERE IdSondeo=? AND clave = '2' LIMIT 1", new String[]{sondeo.Id.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
                    pregunta.Id = cursor.getInt(0);
                    pregunta.Nombre = cursor.getString(1);
                    pregunta.IdSondeo = sondeo.Id;
                    pregunta.Tipo = cursor.getString(2);
                    pregunta.Longitud = cursor.getInt(3);
                    pregunta.Requerido = cursor.getInt(4) == 0 ? false : true;
                    pregunta.clave = "2";
                    // currentUsuario = getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
                    
                    //Opciones de pregunta
                    Cursor cursorOpc = db.rawQuery("SELECT Id,Nombre,IdPregunta FROM PreguntaOpcion WHERE IdPregunta = ?", new String[]{pregunta.Id.toString()});
                    if (cursorOpc.moveToFirst()) 
                    {
                        do {
                            Opcion opcion = new Opcion();
                            opcion.Id = cursorOpc.getInt(0);
                            opcion.Nombre = cursorOpc.getString(1);
                            
                            // Obtenemos los items que ya han sido seleccionados
                            Cursor newCursor = db.rawQuery("SELECT cpo.idOpcion ,c.id as item , cp.Id , cpo.nopcion FROM Contestacion c Inner Join ContestacionPregunta cp ON cp.idcontestacion = c.id INNER JOIN ContestacionPreguntaOpcion cpo ON cpo.IdContestacionPregunta = cp.id WHERE c.idvisita = ? AND c.idsondeo = ? AND cp.idpregunta = ? AND cpo.nopcion = ? AND cpo.Idopcion = ?", 
                                new String[]{ pop.IdVisita.toString(),pregunta.IdSondeo.toString(), pregunta.Id.toString(), nopcion.Id.toString(), opcion.Id.toString() });
                            if(newCursor.moveToFirst())
                            {
                                do{
                                    //Si es diferente de nulo vamos a asignarle a la opcion status1
                                    Opcion opcionx = new Opcion();
                                    opcionx.Id = newCursor.getInt(0);
                                    opcionx.Constestacion = newCursor.getInt(1);
                                    opcionx.nOpcion = newCursor.getInt(3);
                                    if(opcionx.Id>0){
                                        
                                        opcion.Status =1;
                                        opcionx.Selected = true;
                                        pregunta.ContestacionPreguntaId = newCursor.getInt(2);
                                    }
                                    
                                    if(opcionx.Constestacion>0){
                                        opcion.nOpcion = opcionx.nOpcion;
                                        opcion.Constestacion = opcionx.Constestacion;
                                    }
                                    
                                    
                                }while(newCursor.moveToNext());
                            }
                            pregunta.Opciones.add(opcion);
                        } while (cursorOpc.moveToNext());
                    }

                    preguntaCollection.add(pregunta);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return preguntaCollection;
    }
     
    public static int NContestaciones(Context context,mx.smartteam.entities.Pregunta pregunta,
                                    mx.smartteam.entities.PopVisita popvisita 
    ){
        int contestacionPRegunta = 0;
        try{
        
        db = (new BaseDatos(context)).getWritableDatabase();
           
             Cursor newCursor = db.rawQuery("SELECT cpo.idOpcion ,c.id as item , cp.Id FROM Contestacion c Inner Join ContestacionPregunta cp ON cp.idcontestacion = c.id INNER JOIN ContestacionPreguntaOpcion cpo ON cpo.IdContestacionPregunta = cp.id WHERE c.idvisita = ? AND c.idsondeo = ? AND cp.idpregunta = ? AND cpo.nopcion = ? AND cpo.Idopcion = ?", 
                                new String[]{  });
                            if(newCursor.moveToFirst())
                            {
                                do{
                                    contestacionPRegunta = newCursor.getInt(2);
                                }while(newCursor.moveToNext());
                            }
            
            
            
        }catch(Exception ex){
        ex.getMessage().toString();
        }
        
        
        return 0;
    }
     
       
     
    /*
    public static Pregunta GetPregunta1(Context context, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.Pop pop) 
    {
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT Id,Nombre,Tipo,Longitud,Requerido FROM Pregunta WHERE IdSondeo=? AND clave = '1' LIMIT 1", new String[]{sondeo.Id.toString()});

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
                    pregunta.Id = cursor.getInt(0);
                    pregunta.Nombre = cursor.getString(1);
                    pregunta.IdSondeo = sondeo.Id;
                    pregunta.Tipo = cursor.getString(2);
                    pregunta.Longitud = cursor.getInt(3);
                    pregunta.Requerido = cursor.getInt(4) == 0 ? false : true;

                    //Opciones de pregunta
                    Cursor cursorOpc = db.rawQuery("SELECT Id,Nombre,IdPregunta FROM PreguntaOpcion WHERE IdPregunta=?", new String[]{pregunta.Id.toString()});
                    if (cursorOpc.moveToFirst()) {
                        do {
                            Opcion opcion = new Opcion();
                            opcion.Id = cursorOpc.getInt(0);
                            opcion.Nombre = cursorOpc.getString(1);
                            
                            // obtenemos los items que ya han sido seleccionados
                           
                            pregunta.Opciones.add(opcion);
                        } while (cursorOpc.moveToNext());
                    }

                    preguntaCollection.add(pregunta);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return pregunta;
    }
     */
     

    public static PreguntaCollection GetPreguntaByContestacion(Context context, mx.smartteam.entities.Contestacion contestacion) {
        PreguntaCollection preguntaCollection = new PreguntaCollection();

        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String query = "SELECT p.Id, p.Nombre, p.Tipo, p.Longitud, p.Requerido, cp.respuesta, cp.id "
                            + "FROM Pregunta p "
                                + " LEFT JOIN contestacionpregunta cp ON cp.idpregunta = p.id and cp.idcontestacion = " + contestacion.Id
                    + " WHERE p.idsondeo = " + contestacion.IdSondeo + " AND p.clave = 0;";
            
            Cursor cursor = db.rawQuery(query, null);

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    mx.smartteam.entities.Pregunta pregunta = new mx.smartteam.entities.Pregunta();
                    pregunta.Id = cursor.getInt(0);
                    pregunta.Nombre = cursor.getString(1);
                    pregunta.IdSondeo = contestacion.IdSondeo;
                    pregunta.Tipo = cursor.getString(2);
                    pregunta.Longitud = cursor.getInt(3);
                    pregunta.Requerido = cursor.getInt(4) == 0 ? false : true;
                    pregunta.Respuesta = cursor.getString(5) != null ? cursor.getString(5) : "";
                    pregunta.ContestacionPreguntaId = cursor.getInt(6);// != null ? cursor.getInt(6) : 0;
                    //Opciones de pregunta
                    
                    String select = "SELECT po.Id, po.Nombre, po.IdPregunta, cpo.idopcion, cpo.id "
                            + " FROM preguntaopcion po "
                            + " INNER JOIN pregunta p on p.id = po.idpregunta "
                            + " LEFT JOIN contestacionpregunta cp ON cp.idpregunta = p.id AND cp.idcontestacion = " + contestacion.Id 
                            + " LEFT JOIN contestacionpreguntaopcion cpo ON cpo.idcontestacionpregunta = cp.id AND cpo.idopcion = po.id "
                            + " WHERE p.idsondeo = " + contestacion.IdSondeo + " AND po.idpregunta = " + pregunta.Id + ";";
                    
                    Cursor cursorOpc = db.rawQuery(select, null);
                    
                    if (cursorOpc.moveToFirst()) {
                        do {
                            Opcion opcion = new Opcion();
                            opcion.Id = cursorOpc.getInt(0);
                            opcion.Nombre = cursorOpc.getString(1);
                            //opcion.IdContestacionPreguntaOpcion = cursorOpc.getInt(4);

                            if (cursorOpc.getInt(3) > 0) {
                                opcion.Selected = true;
                            }
                            if (cursorOpc.getInt(4) > 0) {
                                pregunta.Respuesta = cursorOpc.getInt(4);
                            }
                            
                            pregunta.Opciones.add(opcion);

                            
                            
                        } while (cursorOpc.moveToNext());
                    }

                    preguntaCollection.add(pregunta);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return preguntaCollection;
    }
    
    
   public static int GetPregunta1(Context context, mx.smartteam.entities.Pregunta pregunta2, mx.smartteam.entities.PopVisita popVisita , mx.smartteam.entities.SondeoModulos sondeo){
        int go=0;
    
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT \n" +
                    "c.id , c.idvisita, cp.id as idcp\n" +
                    " FROM Contestacion c\n" +
                    " INNER JOIN ContestacionPregunta cp ON cp.idcontestacion = c.id\n" +
                    " INNER JOIN ContestacionPreguntaOpcion cpo  ON cpo.idcontestacionPregunta = cp.id\n" +
                    " LEFT JOIN PreguntaOpcion po ON po.id = cpo.IdOpcion\n" +
                    " WHERE c.IdVisita = ? AND c.idsondeo = ? AND cp.idcontestacion = ? AND  cpo.IdOpcion = ?", 
                    new String[]{
                    popVisita.Id.toString(), sondeo.Id.toString(), pregunta2.Opciones.get(0).Constestacion.toString(), pregunta2.Opciones.get(0).nOpcion.toString()
                    });
             if (cursor.moveToFirst()) 
             {
                 go = cursor.getInt(2);
             
             }
            
            
        }catch(Exception ex){
            ex.getMessage().toString();
        }
    
    
   return go;
   }

}
