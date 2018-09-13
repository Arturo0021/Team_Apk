package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.SimpleDateFormat;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.RespuestaSondeoCollection;


public class RespuestaSondeo {

    private static SQLiteDatabase db = null;

    public static String Insert(Context context, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Sondeo sondeo, PreguntaCollection preguntaCollection, long fecha) {
        String result = "OK";

        if (sondeo != null) {

            try {

                Long IdRespuesta;
                db = (new BaseDatos(context)).getWritableDatabase();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

                for (mx.smartteam.entities.Pregunta pregunta : preguntaCollection) {
                    pop.Fecha = fecha;
                    //Regustramos la pregunta y sus respuestas    
                    ContentValues values = new ContentValues();
                    values.put("IdPregunta", pregunta.Id);
                    values.put("IdSondeo", sondeo.Id);
                    values.put("Respuesta", pregunta.Respuesta != null ? pregunta.Respuesta.toString() : null);
                    //values.put("FechaCrea", dateFormat.format(new Date()));
                    values.put("FechaCrea", pop.Fecha);

                    values.put("IdVisita", pop.IdVisita);
                    values.put("StatusSync", 0);
                    IdRespuesta = db.insert("RespuestaSondeo", null, values);

                    //Insertamos las opciones de respuesta
                    if (pregunta.Respuesta == null) {
                        for (mx.smartteam.entities.Opcion opcion : pregunta.Opciones) {
                            if (opcion.Selected.equals(true)) {
                                ContentValues valuesOpc = new ContentValues();
                                valuesOpc.put("IdRespuesta", IdRespuesta.intValue());
                                valuesOpc.put("IdOpcion", opcion.Id);
                                db.insert("RespuestaOpcion", null, valuesOpc);
                            }
                        }
                    }
                }

                db.close();

            } catch (Exception e) {
                result = e.getMessage();
            }
        }
        return result;
    }

    public static String Insertar(Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Pop pop, mx.smartteam.entities.SondeoModulos sondeo, 
                                                   PreguntaCollection preguntaCollection, long fecha, mx.smartteam.entities.Producto producto, mx.smartteam.entities.Marca marca,
                                                   mx.smartteam.entities.Categoria categoria, int idFoto, long idContestacion) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        int bandera = 0;
        pop.Fecha = fecha;
        String result = "OK";
        Integer IdContestacionPregunta = 0;
        String preguntaID = "";
        String queryOpcion = "";
        
        try {
            
        
        if(idContestacion == 0) {
            
            String insert = "INSERT INTO Contestacion (IdSondeo, IdVisita, IdUsuario";
            
                                if(idFoto > 0) {
                                    insert += ", IdFoto";
                                }

                                if(producto != null){
                                    insert += ", sku";
                                }
            
                        insert += ", Fecha, StatusSync) ";
            
                        insert += " VALUES ("
                                +  sondeo.getId() + ", "
                                +  pop.getIdVisita() + ", "
                                +  usuario.getId() + ", ";
                            
                                if(idFoto > 0) {
                                   insert += idFoto + ", ";
                                }
                                
                                if(producto != null){
                                    insert += producto.getSKU() + ", ";
                                }
                                
                        insert += "'" + fecha + "', "
                                + 0
                            + ");";
                        
            db.execSQL(insert);
            
        }
            
        if(idContestacion == 0) {
           
            String select = "SELECT Id FROM Contestacion "
                                + " WHERE IdSondeo = " + sondeo.getId()
                                        + " AND IdVisita = " + pop.getIdVisita()
                                        + " AND IdUsuario = " + + usuario.getId()
                                        + " AND Fecha = '" + fecha + "';";
            Cursor cursor = db.rawQuery(select, null);
            
            if(cursor.moveToFirst()){
                idContestacion = cursor.getInt(0);
            }
            
        }
       
        for (mx.smartteam.entities.Pregunta pregunta1 : preguntaCollection) {
            
            if (pregunta1.getContestacionPreguntaId() == null || pregunta1.getContestacionPreguntaId() == 0) {
                
                String val_preguntaID = "SELECT COUNT(1) FROM ContestacionPregunta WHERE IdContestacion = " + idContestacion
                                        + " AND IdPregunta = " + pregunta1.getId()
                                        + " AND DATE(Fecha, 'unixepoch') = DATE('" + fecha + "', 'unixepoch')"
                                        + " AND Respuesta = '" + pregunta1.getRespuesta() + "';";
                Cursor cursor = db.rawQuery(val_preguntaID, null);
                
                if(cursor.moveToFirst()){
                    
                    int ex = cursor.getInt(0);
                    
                    if(ex == 0){
                        String insertPregunta = "INSERT INTO ContestacionPregunta (IdContestacion, IdPregunta, Fecha, Respuesta, StatusSync)"
                                                        + " VALUES ("
                                                            + idContestacion + ", "
                                                            + pregunta1.getId() + ", "
                                                            + "'" + fecha + "', "
                                                            + "'" + pregunta1.getRespuesta() + "', "
                                                            + 0
                                                        + ");";
                
                        db.execSQL(insertPregunta);
                    }
                    
                }
                
                String preguntaIDa = "SELECT Id FROM ContestacionPregunta WHERE IdContestacion = " + idContestacion
                                        + " AND IdPregunta = " + pregunta1.getId()
                                        + " AND DATE(Fecha, 'unixepoch') = DATE('" + fecha + "', 'unixepoch')"
                                        + " AND Respuesta = '" + pregunta1.getRespuesta() + "';";
               
                Cursor cursora = db.rawQuery(preguntaIDa, null);
                
                if(cursora.moveToFirst()){
                    
                    IdContestacionPregunta = cursora.getInt(0);
                    
                }
                
                
            } else {
                String updatePregunta = "UPDATE ContestacionPregunta SET "
                                            + " Respuesta = '" + pregunta1.getRespuesta() + "'"
                                        + " WHERE Id = " + pregunta1.getContestacionPreguntaId() + ";";
                db.execSQL(updatePregunta);
                
                IdContestacionPregunta = pregunta1.getContestacionPreguntaId();
            }
            
            if(pregunta1.getTipo().equalsIgnoreCase("multiple")){
                
                if(pregunta1.getOpciones() != null) {
                    for (mx.smartteam.entities.Opcion opcion : pregunta1.getOpciones()) {

                            if (opcion.getSelected().equals(true)) {

                                     queryOpcion = "SELECT COUNT(1) FROM ContestacionPreguntaOpcion "
                                                            + " WHERE IdContestacionPregunta = " + IdContestacionPregunta 
                                                    + " AND IdOpcion = " + opcion.getId() + ";";

                                    Cursor cursor = db.rawQuery(queryOpcion, null);

                                    if(cursor.moveToFirst()){

                                        int exist = cursor.getInt(0);
                                        if(exist == 0){
                                            String insertOpcion = "INSERT INTO ContestacionPreguntaOpcion (IdContestacionPregunta, IdOpcion) "
                                                                    + " VALUES ("
                                                                        + IdContestacionPregunta + ", "
                                                                        + opcion.getId()
                                                                    + ");";
                                            db.execSQL(insertOpcion);
                                        } else {
                                             /*String updateOpcion = "";

                                                updateOpcion = "UPDATE ContestacionPreguntaOpcion "
                                                                        + " SET IdOpcion = " + opcion.getId()
                                                                    + " WHERE IdContestacionPregunta = " + IdContestacionPregunta
                                                            + " AND IdOpcion = " + opcion.getId() + ";";

                                            db.execSQL(updateOpcion);*/
                                        }

                                    }
                            }

                    }
                }
                
            } else {
                
                if(pregunta1.getOpciones() != null) {
                    for (mx.smartteam.entities.Opcion opcion : pregunta1.getOpciones()) {

                            if (opcion.getSelected().equals(true)) {

                                     queryOpcion = "SELECT COUNT(1) FROM ContestacionPreguntaOpcion "
                                                            + " WHERE IdContestacionPregunta = " + IdContestacionPregunta + ";";

                                    Cursor cursor = db.rawQuery(queryOpcion, null);

                                    if(cursor.moveToFirst()){

                                        int exist = cursor.getInt(0);
                                        if(exist == 0){
                                            String insertOpcion = "INSERT INTO ContestacionPreguntaOpcion (IdContestacionPregunta, IdOpcion) "
                                                                    + " VALUES ("
                                                                        + IdContestacionPregunta + ", "
                                                                        + opcion.getId()
                                                                    + ");";
                                            db.execSQL(insertOpcion);
                                        } else {
                                             String updateOpcion = "";

                                                updateOpcion = "UPDATE ContestacionPreguntaOpcion "
                                                                        + " SET IdOpcion = " + opcion.getId()
                                                                    + " WHERE IdContestacionPregunta = " + IdContestacionPregunta + ";";

                                            db.execSQL(updateOpcion);
                                        }

                                    }
                            }

                    }
                }
            }
           
            

            
            
        }
        
        } catch(Exception e){
            result = e.getMessage();
        }
            
        return result;
    }
    
    public static String InsertarMarca(Context context, mx.smartteam.entities.Usuario usuario,
                                        mx.smartteam.entities.Pop pop, 
                                        mx.smartteam.entities.SondeoModulos sondeo, 
                                        PreguntaCollection preguntaCollection,
                                        long fecha, 
                                        mx.smartteam.entities.Producto producto, 
                                        mx.smartteam.entities.Marca marca,
                                        mx.smartteam.entities.Categoria categoria, 
                                        int idFoto, long idContestacion) {
        String result = "OK";
        Integer IdContestacionPregunta;

        if (sondeo != null) {

            try {
                pop.Fecha = fecha;
                db = (new BaseDatos(context)).getWritableDatabase();

                //registramos la contestacion
                ContentValues values = new ContentValues();
                values.put("IdSondeo", sondeo.Id);
                values.put("IdVisita", pop.IdVisita);
                values.put("IdUsuario", usuario.Id);
                values.put("Fecha", pop.Fecha);
                values.put("StatusSync", 0);

                if (idFoto > 0) {
                    values.put("IdFoto", idFoto);
                }
                if (producto != null) {
                    values.put("sku", producto.SKU);
                }
                
                if (idContestacion == 0) {
                    idContestacion = db.insert("Contestacion", null, values);
                } else {
                    db.update("Contestacion", values, "id=" + idContestacion, null);
                }

                for (mx.smartteam.entities.Pregunta pregunta1 : preguntaCollection) {

                    ContentValues cvalues = new ContentValues();
                    cvalues.put("IdContestacion", idContestacion);
                    cvalues.put("IdPregunta", pregunta1.Id);
                    cvalues.put("Fecha", pop.Fecha);
                    cvalues.put("Respuesta", pregunta1.Respuesta != null ? pregunta1.Respuesta.toString() : null);
                    cvalues.put("StatusSync", 0);
                    
                    if(marca!= null){
                        cvalues.put("IdMarca", marca.Id);
                    }
                    

                    if (pregunta1.ContestacionPreguntaId == null) {
                        IdContestacionPregunta = (int) db.insert("ContestacionPregunta", null, cvalues);
                    } else {
                        db.update("ContestacionPregunta", cvalues, "id=" + pregunta1.ContestacionPreguntaId, null);
                        IdContestacionPregunta = pregunta1.ContestacionPreguntaId;
                    }

                    //if (pregunta1.Respuesta == null) {
                    if (pregunta1.Opciones != null) {
                        //db.delete("ContestacionPreguntaOpcion", "IdContestacionPregunta=?", new String[]{IdContestacionPregunta.toString()});
                        for (mx.smartteam.entities.Opcion opcion : pregunta1.Opciones) {
                            if (opcion.Selected.equals(true)) {
                                ContentValues valuesOpc = new ContentValues();
                                valuesOpc.put("IdContestacionPregunta", IdContestacionPregunta.intValue());
                                valuesOpc.put("IdOpcion", opcion.Id);
                                
                                if (pregunta1.Respuesta!=null) {
                                    
                                    db.update("ContestacionPreguntaOpcion",  valuesOpc,"id=" +pregunta1.Respuesta.toString(),null );
                                } else {
                                    db.insert("ContestacionPreguntaOpcion", null, valuesOpc);
                                }

                            }
                        }
                    }
                    //}
                }

                db.close();

            } catch (Exception e) {
                result = e.getMessage();
            }
        }
        return result;
    }   
    
    public static String Insertarx(Context context, mx.smartteam.entities.Usuario usuario,
        mx.smartteam.entities.Pop pop, mx.smartteam.entities.SondeoModulos sondeo, PreguntaCollection preguntaCollection,
        long fecha, mx.smartteam.entities.Producto producto, mx.smartteam.entities.Marca marca,
        mx.smartteam.entities.Categoria categoria, int idFoto, long idContestacion, int newopcion, int idopcion) {
        String result = "OK";
        Integer IdContestacionPregunta;

        if (sondeo != null) {

            try {
                pop.Fecha = fecha;
                //Long IdContestacion;

                db = (new BaseDatos(context)).getWritableDatabase();

                //registramos la contestacion
                ContentValues values = new ContentValues();
                values.put("IdSondeo", sondeo.Id);
                values.put("IdVisita", pop.IdVisita);
                values.put("IdUsuario", usuario.Id);
                values.put("Fecha", pop.Fecha);
                values.put("StatusSync", 0);

                if (idFoto > 0) {
                    values.put("IdFoto", idFoto);
                }
                if (producto != null) {
                    values.put("sku", producto.SKU);
                }
                values.put("IdOpcion", idopcion);
                
                if (idContestacion == 0) {
                    idContestacion = db.insert("Contestacion", null, values);
                } else {
                    db.update("Contestacion", values, "id=" + idContestacion, null);
                }
                
                for (mx.smartteam.entities.Pregunta pregunta1 : preguntaCollection) {
                    // init
                    ContentValues cvalues = new ContentValues();
                    cvalues.put("IdContestacion", idContestacion);
                    cvalues.put("IdPregunta", pregunta1.Id);
                    cvalues.put("Fecha", pop.Fecha);
                    cvalues.put("Respuesta", pregunta1.Respuesta != null ? pregunta1.Respuesta.toString() : null);
                    cvalues.put("StatusSync", 0);
                        
                    if (pregunta1.ContestacionPreguntaId == null ||  pregunta1.ContestacionPreguntaId == 0) {
                        IdContestacionPregunta = (int) db.insert("ContestacionPregunta", null, cvalues);
                        
                        if (pregunta1.Opciones != null) {
                            for (mx.smartteam.entities.Opcion opcion : pregunta1.Opciones) {
                                if (opcion.Selected.equals(true)) {
                                    ContentValues valuesOpc = new ContentValues();
                                    valuesOpc.put("IdContestacionPregunta", IdContestacionPregunta.intValue());
                                    valuesOpc.put("IdOpcion", opcion.Id);
                                    valuesOpc.put("NOpcion", newopcion);
                                    
                                    db.insert("ContestacionPreguntaOpcion", null, valuesOpc);
                                    

                                }
                            }
                        }
                    } else 
                    {
                        if (pregunta1.bandera != 2) {
                            db.update("ContestacionPregunta", cvalues, "id=" + pregunta1.ContestacionPreguntaId  , null);
                            IdContestacionPregunta = pregunta1.ContestacionPreguntaId;

                            if (pregunta1.Opciones != null) {

                                for (mx.smartteam.entities.Opcion opcion : pregunta1.Opciones) {
                                    if (opcion.Selected.equals(true)) {
                                        ContentValues valuesOpc = new ContentValues();
                                        valuesOpc.put("IdContestacionPregunta", IdContestacionPregunta.toString());
                                        valuesOpc.put("IdOpcion", opcion.Id);
                                        valuesOpc.put("NOpcion", newopcion);

                                        db.update("ContestacionPreguntaOpcion", valuesOpc, "id=" + IdContestacionPregunta.toString(), null);
                                    }
                                }
                            }
                        }
                    }
                    
                    ///END IF

                }

                db.close();

            } catch (Exception e) {
                result = e.getMessage();
            }
        }
        return result;
    }
    
    

    public static String UpdateStatusSync(Context context, mx.smartteam.entities.Contestacion contestacion) {
        String result = "OK";

        try {

            if (contestacion != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("StatusSync", contestacion.StatusSync);
                values.put("FechaSync", System.currentTimeMillis() / 1000);
                db.update("Contestacion", values, "Id=" + contestacion.Id.toString(), null);
                db.update("ContestacionPregunta", values, "IdContestacion=" + contestacion.Id.toString(), null);
                return result;
            }

        } catch (Exception e) {
            Log.i("UpdateStatusSync", e.getMessage());
            result = e.getMessage();
        } finally {
            db.close();
        }
        return result;

    }

    public static mx.smartteam.entities.RespuestaSondeoCollection GetByVisitaSondeo(final Context context, mx.smartteam.entities.PopVisita popVisita, mx.smartteam.entities.Sondeo sondeo) {
        mx.smartteam.entities.RespuestaSondeoCollection respuestaSondeoCollection = new RespuestaSondeoCollection();
        try {

            if (popVisita != null && sondeo != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT rs.Id,rs.IdPregunta,rs.IdSondeo,rs.IdVisita,rs.Respuesta,datetime(rs.fechacrea, 'unixepoch', 'localtime'),rs.StatusSync,rs.FechaSync ,p.Tipo "
                        + "FROM RespuestaSondeo rs, pregunta p "
                        + "WHERE rs.StatusSync=0 AND rs.IdSondeo=p.IdSondeo AND rs.IdPregunta=p.Id AND rs.IdVisita=? AND rs.IdSondeo=? ",
                        new String[]{popVisita.Id.toString(), sondeo.Id.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.RespuestaSondeo respuesta = new mx.smartteam.entities.RespuestaSondeo();
                        respuesta.Id = cursor.getInt(0);
                        respuesta.IdPregunta = cursor.getInt(1);
                        respuesta.IdSondeo = cursor.getInt(2);
                        respuesta.IdVisita = cursor.getInt(3);
                        respuesta.Respuesta = cursor.getString(4);
                        respuesta.FechaCrea = cursor.getString(5);
                        respuesta.StatusSync = cursor.getInt(6);
                        respuesta.Tipo = cursor.getString(8);

                        //Opciones respuesta
                        Cursor cursorOpc = db.rawQuery(
                                "SELECT RO.IdRespuesta,RO.IdOpcion ,PO.Nombre "
                                + "FROM RespuestaOpcion RO,PreguntaOpcion PO  "
                                + "WHERE RO.IdOpcion=PO.Id AND RO.IdRespuesta=? ",
                                new String[]{respuesta.Id.toString()});

                        if (cursorOpc.moveToFirst()) {
                            do {
                                Opcion opcion = new Opcion();
                                opcion.Id = cursorOpc.getInt(1);
                                opcion.Nombre = cursorOpc.getString(2);
                                respuesta.Opciones.add(opcion);

                            } while (cursorOpc.moveToNext());
                        }

                        respuestaSondeoCollection.add(respuesta);

                    } while (cursor.moveToNext());

                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return respuestaSondeoCollection;
    }

//Aparte le mandariamos como parametro el idcontestacion de ContestacionPregunta
    public static mx.smartteam.entities.RespuestaSondeoCollection GetByVisitaSondeop(final Context context, mx.smartteam.entities.PopVisita popVisita, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.Contestacion contestacion
    ) {
        mx.smartteam.entities.RespuestaSondeoCollection respuestaSondeoCollection = new RespuestaSondeoCollection();
        try {

            if (popVisita != null && sondeo != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                
                String query = "SELECT " +
                        "cp.Idcontestacion, " +
                        "cp.Id, " +
                        "cp.Idpregunta, " +
                        "c.IdSondeo, " +
                        "c.IdVisita, " +
                        "cp.Respuesta, " +
                        "datetime(c.Fecha ,'unixepoch','localtime') as tiempo, " +
                        "c.StatusSync, " +
                        "c.FechaSync, " +
                        "p.tipo, " +
                        "c.sku, c.idopcion, " +
                        "cp.idmarca " +
                        " FROM ContestacionPregunta cp " +
                        " INNER JOIN Contestacion c ON c.Id = cp.idcontestacion " +
                        " INNER JOIN Pregunta p ON p.Id = cp.IdPregunta AND p.IdSondeo = c.idsondeo "
                        + " WHERE "
                        + " c.StatusSync = 0 "
                        + " AND c.IdVisita = " + popVisita.Id.toString()
                        + " AND p.IdSondeo = " + sondeo.Id.toString()
                        + " AND cp.IdContestacion = " + contestacion.Id.toString()
                        + " order by tiempo ASC; ";
                
                Cursor cursor = db.rawQuery(query, null);

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.RespuestaSondeo respuesta = new mx.smartteam.entities.RespuestaSondeo();

                        respuesta.Id = cursor.getInt(1);
                        respuesta.IdPregunta = cursor.getInt(2);
                        respuesta.IdSondeo = cursor.getInt(3);
                        respuesta.IdVisita = cursor.getInt(4);
                        respuesta.Respuesta = cursor.getString(5);
                        respuesta.FechaCrea = cursor.getString(6);
                        respuesta.StatusSync = cursor.getInt(7);
                        respuesta.Tipo = cursor.getString(9);
                        respuesta.Sku = cursor.getLong(10);
                        respuesta.IdOpcion = cursor.getInt(11);
                        respuesta.IdMarca = cursor.getInt(12);
                        //Opciones respuesta
                        
                        String select = "SELECT "
                                + " CPO.IdContestacionPregunta, "
                                + " CPO.IdOpcion, "
                                + " PO.Nombre, "      
                                + " CPO.NOpcion "
                                + " FROM "
                                + " ContestacionPreguntaOpcion CPO, PreguntaOpcion PO "
                                + " WHERE "
                                + " CPO.IdOpcion = PO.Id "
                                + " AND CPO.IdContestacionPregunta = " + respuesta.Id.toString();
                        
                        Cursor cursorOpc = db.rawQuery(select , null);

                        if (cursorOpc.moveToFirst()) {
                            do {
                                Opcion opcion = new Opcion();
                                opcion.Id = cursorOpc.getInt(1);
                                opcion.Nombre = cursorOpc.getString(2);
                                opcion.nOpcion = cursorOpc.getInt(3);
                                
                                respuesta.Opciones.add(opcion);

                            } while (cursorOpc.moveToNext());
                        }

                        respuestaSondeoCollection.add(respuesta);

                    } while (cursor.moveToNext());

                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return respuestaSondeoCollection;
    }
    
    
    public static mx.smartteam.entities.Contestacion Contestacion(final Context context, mx.smartteam.entities.PopVisita popVisita, mx.smartteam.entities.Foto foto)
    {
        mx.smartteam.entities.Contestacion contestacion = null;
        try {

            
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db.rawQuery(
                        "SELECT IdFoto, Sku,IdSondeo FROM Contestacion "
                        + " WHERE "
                        //+ " IdVisita = ?  limit 1",
                        + " IdVisita = ?  AND IdFoto = ? ",
                        new String[]{popVisita.Id.toString(), foto.Id.toString() });
                        //new String[]{popVisita.Id.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        contestacion = new mx.smartteam.entities.Contestacion();
                        contestacion.IdFoto = cursor.getInt(0);
                        contestacion.Sku = cursor.getLong(1);
                        contestacion.IdSondeo = cursor.getInt(2);
                        
                    } while (cursor.moveToNext());
                }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return contestacion;
    }
    

}//END CLASS
