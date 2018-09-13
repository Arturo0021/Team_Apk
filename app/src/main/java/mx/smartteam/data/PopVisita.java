package mx.smartteam.data;

import android.content.ContentValues;

import mx.smartteam.entities.PopCollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import mx.triplei.Utilerias;
import mx.smartteam.entities.PopVisitaCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class PopVisita {

    private static String METHOD_NAME = "/GetOrdenFormularios";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static String strResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static mx.smartteam.entities.PopVisita Insert(final Context context, final mx.smartteam.entities.PopVisita popVisita) throws Exception {



        db = (new BaseDatos(context)).getWritableDatabase();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        ContentValues values = new ContentValues();
        values.put("IdProyecto", popVisita.IdProyecto);
        values.put("IdUsuario", popVisita.IdUsuario);
        values.put("DeterminanteGSP", popVisita.DeterminanteGSP);
        values.put("Latitud", popVisita.Latitud);
        values.put("Longitud", popVisita.Longitud);
        //values.put("FechaCrea", dateFormat.format(new Date()));

        Long id = db.insert("PopVisita", null, values);
        popVisita.Id = id.intValue();
        db.close();
        return popVisita;


    }
    
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.PopVisita popVisita) {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", popVisita.StatusSync.toString());
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("PopVisita", values, "Id=" + popVisita.Id.toString(), null);
        db.execSQL("UPDATE Regsincro SET Upload = strftime('%s','now')");
        db.close();
    }

    public static void UpdateStatus(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {
        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdStatus", popVisita.IdStatus.toString());
        db.update("PopVisita", values, "Id=" + popVisita.Id.toString(), null);       
    }

    public static void UpdateStatusAndCord(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {
        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Latitud", popVisita.Latitud);
        values.put("Longitud", popVisita.Longitud);
        values.put("IdStatus", "1");
        values.put("FechaCrea", Utilerias.getFechaUnix());
        db.update("PopVisita", values, "Id=" + popVisita.Id.toString(), null);   
        db.close();
    }
    
    
    public static String ChangeStatusVisit(Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario) throws Exception{
        String result = "Ok";
        db = (new BaseDatos(context)).getWritableDatabase();
        
        ContentValues valores =  new ContentValues();
                valores.put("IdStatus", 2);
                valores.put("FechaCierre", Utilerias.getFechaUnix());
                db.update("PopVisita", valores, "IdStatus = 1 AND IdProyecto= ? AND IdUsuario = ?",
                        new String[]{proyecto.Id.toString(), usuario.Id.toString()});
        db.close();
        return result;
    }
    
        public static mx.smartteam.entities.PopVisitaCollection FindOpenStore(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception {
            db = (new BaseDatos(context)).getWritableDatabase();
            
            mx.smartteam.entities.PopVisitaCollection visitaCollection = new PopVisitaCollection();

            Cursor cursor = db.rawQuery(" SELECT "
                    + " id,datetime(Fechacrea,'unixepoch','localtime') as oldtime"
                    + " FROM POPVISITA "
                    + " WHERE oldtime < '" + Utilerias.getFecha() + "' "
                    + " AND idproyecto = ? "
                    + " AND Idusuario = ? "
                    + " AND IdStatus >= 1 "
                    + " AND StatusSync = 0",
                    new String[]{proyecto.Id.toString(), usuario.Id.toString()});

            if (cursor.moveToFirst()) {
                do {
                    mx.smartteam.entities.PopVisita popVisita = new mx.smartteam.entities.PopVisita();
                    popVisita = new mx.smartteam.entities.PopVisita();
                    popVisita.Id = cursor.getInt(0);
                    visitaCollection.add(popVisita);
                } while (cursor.moveToNext());
            }
            db.close();
            return visitaCollection;

        }// END METHOD
    
    
    public static mx.smartteam.entities.PopVisitaCollection VerifyStatusMarket(Context context,mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario)throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.PopVisitaCollection visitaCollection = new PopVisitaCollection();
       
        Cursor cursor = db.rawQuery(
                "Select Id,IdProyecto,DeterminanteGSP,IdUsuario,IdStatus from PopVisita "
                + " WHERE IdStatus = 1 AND IdProyecto=? AND IdUsuario=? "
                + " AND datetime(FechaCrea, 'unixepoch', 'localtime' ) "
                + " Like  '%" + Utilerias.getFecha() + "%'  "
                , new String[]{proyecto.Id.toString(), usuario.Id.toString()});
         if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.PopVisita popVisita = new mx.smartteam.entities.PopVisita();
                        popVisita = new mx.smartteam.entities.PopVisita();
                        popVisita.Id = cursor.getInt(0);
                        popVisita.IdProyecto = cursor.getInt(1);
                        popVisita.DeterminanteGSP = cursor.getInt(2);
                        popVisita.IdUsuario = cursor.getInt(3);
                        popVisita.IdStatus = cursor.getInt(4);
                        visitaCollection.add(popVisita);
                    } while (cursor.moveToNext());

                }
          db.close();
        return visitaCollection;   
    } // END METHOD
    
        public static mx.smartteam.entities.PopVisitaCollection OnlyTablet(Context context,mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario)throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.PopVisitaCollection visitaCollection = new PopVisitaCollection();
       
        Cursor cursor = db.rawQuery(
                "Select Id,IdProyecto,DeterminanteGSP,IdUsuario,IdStatus from PopVisita "
                + " WHERE IdProyecto=? AND IdUsuario=? "
                + " AND datetime(FechaCrea, 'unixepoch', 'localtime' ) "
                + " <='" + Utilerias.RestarFecha() + "'"
                + " and StatusSync = 0 "
                + " and IdStatus > 0"
                , new String[]{proyecto.Id.toString(), usuario.Id.toString()});
         if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.PopVisita popVisita = new mx.smartteam.entities.PopVisita();
                        popVisita = new mx.smartteam.entities.PopVisita();
                        popVisita.Id = cursor.getInt(0);
                        popVisita.IdProyecto = cursor.getInt(1);
                        popVisita.DeterminanteGSP = cursor.getInt(2);
                        popVisita.IdUsuario = cursor.getInt(3);
                        popVisita.IdStatus = cursor.getInt(4);
                        visitaCollection.add(popVisita);
                    } while (cursor.moveToNext());

                }
          db.close();
        return visitaCollection;   
    }
    
    
    public static int existen_visitas_abiertas(Context context) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        int existe = 0;
        
        String query = "SELECT COUNT(1) FROM Popvisita WHERE IdStatus = 1 AND DATE(FechaCrea, 'unixepoch') = DATE('" + Utilerias.getFecha() + "');";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            existe = cursor.getInt(0);
        }
        
        return existe;
    }
    
    
    
    //Registro de entrada
    public static String CheckIn(Context context, mx.smartteam.entities.PopVisita popVisita) {
        String result = "OK";

        try {

            if (popVisita != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("FotoEntrada", popVisita.FotoEntrada.toString());
                values.put("FechaEntrada", System.currentTimeMillis() / 1000);

                db.update("PopVisita", values, "Id=" + popVisita.Id.toString(), null);

                return result;
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
            result = e.getMessage();
        } finally {
            db.close();
        }
        return result;
    }

    public static String CheckOut(Context context, mx.smartteam.entities.PopVisita popVisita) {
        String result = "OK";
        try {

            if (popVisita != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("FotoSalida", popVisita.FotoSalida.toString());
                values.put("FechaSalida", System.currentTimeMillis() / 1000);
                db.update("PopVisita", values, "Id=" + popVisita.Id.toString(), null);

                return result;
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
            result = e.getMessage();
        } finally {
            db.close();
        }
        return null;
    }
   
    public static mx.smartteam.entities.PopVisita GetById(Context context, Integer Id) {


        mx.smartteam.entities.PopVisita popVisita = null;

        try {

            if (Id != null) {
                db = (new BaseDatos(context)).getWritableDatabase();

                Cursor cursor = db.rawQuery(
                        "SELECT Id,IdProyecto,DeterminanteGSP,IdUsuario,Latitud,Longitud,IdStatus,FotoEntrada,FechaEntrada,FotoSalida,FechaSalida,FechaCrea,StatusSync,FechaSync"
                        + " FROM PopVisita "
                        + " WHERE Id=?",
                        new String[]{Id.toString()});


                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        popVisita = new mx.smartteam.entities.PopVisita();
                        popVisita.Id = cursor.getInt(0);
                        popVisita.IdProyecto = cursor.getInt(1);
                        popVisita.DeterminanteGSP = cursor.getInt(2);
                        popVisita.IdUsuario = cursor.getInt(3);
                        popVisita.Latitud = cursor.getDouble(4);
                        popVisita.Longitud = cursor.getDouble(5);
                        popVisita.IdStatus = cursor.getInt(6);
                        popVisita.StatusSync = cursor.getInt(12);

                    } while (cursor.moveToNext());

                }

                return popVisita;
            }

        } catch (Exception e) {
            Log.i("Select", e.getMessage());
        } finally {
            db.close();
        }
        return popVisita;
    }
    
    public static mx.smartteam.entities.PopVisita FindPopOpenNowByFolio(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, Integer folio) {

        mx.smartteam.entities.PopVisita popVisita = null;
        try {

            if (proyecto != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db
                        .rawQuery("SELECT Id,IdProyecto,DeterminanteGSP,IdUsuario,Latitud,Longitud,IdStatus "
                        + "FROM popVisita "
                        + "WHERE IdProyecto=? AND IdUsuario=? AND DeterminanteGSP=? AND date(popvisita.fechacrea, 'unixepoch', 'localtime')= date('now','localtime')",
                        new String[]{proyecto.Id.toString(), usuario.Id.toString(), folio.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        popVisita = new mx.smartteam.entities.PopVisita();
                        popVisita.Id = cursor.getInt(0);
                        popVisita.IdProyecto = cursor.getInt(1);
                        popVisita.DeterminanteGSP = cursor.getInt(2);
                        popVisita.IdUsuario = cursor.getInt(3);
                        popVisita.Latitud = cursor.getDouble(4);
                        popVisita.Longitud = cursor.getDouble(5);
                        popVisita.IdStatus = cursor.getInt(6);

                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            Log.i("FindPopOpenNowByFolio", e.getMessage());
        } finally {
            db.close();
        }
        return popVisita;

    }

    public static String CloseDay(Context context, mx.smartteam.entities.Pop popVisita) {
        String result = "OK";

        try {

            if (popVisita != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("IdStatus", 2);
                values.put("FechaCierre", Utilerias.getFechaUnix());
                db.update("PopVisita", values, "Id=" + popVisita.IdVisita.toString(), null);
                
                return result;
            }

        } catch (Exception e) {
            Log.i("CloseDay", e.getMessage());
            result = e.getMessage();
        } finally {
            db.close();
        }
        return result;

    }

    public static mx.smartteam.entities.PopCollection GetPopVisits(final Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) {
    
        mx.smartteam.entities.PopCollection popCollection = new PopCollection();
        try {

            if (usuario != null && proyecto != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                /*  Vamos por las tiendas que usaremos el dia de hoy  */
        String qquery = " SELECT  \n" +
                        " p.Activo,p.Altitud,p.CP,p.Calle,p.Colonia,p.Id,p.Latitud,p.Longitud,p.determinanteGSP,p.direccion,\n" +
                        " p.idCadena,p.idCanal,p.idGrupo,p.sucursal,p.cadena  ,pv.IdStatus,pv.Id, r.activo as bandera \n" +
                        " FROM POPVISITA pv \n" +
                            " LEFT JOIN Rutas r on r.idproyecto = pv.idproyecto AND pv.determinanteGsp  = r.determinanteGsp \n" +
                            " AND date(datetime(pv.FechaCrea, 'unixepoch', 'localtime' ))  = date(datetime(r.Fecha, 'unixepoch', 'localtime') )  \n" +
                            " INNER JOIN POP p on p.idproyecto = pv.idproyecto AND pv.determinanteGsp = p.determinantegsp \n" +
                        " WHERE   pv.idusuario=? AND \n" +
                                " pv.idproyecto=? AND \n" +
                        " date(datetime(pv.fechacrea, 'unixepoch', 'localtime')) = date('now','localtime')";
                
            Cursor cursor = db.rawQuery(qquery , new String[]{usuario.Id.toString(), proyecto.Id.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
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
                        pop.Cadena = cursor.getString(14);
                        pop.IdStatus = cursor.getInt(15);
                        pop.IdVisita = cursor.getInt(16);
                        pop.Ractiva = cursor.getInt(17);
                        
                        popCollection.add(pop);
                           
                        
                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return popCollection;
    }

    /*
     * SINCRONIZACION 
     */
    /*
     * Obtiene visitas pendientes por sincronizar
     */
    public static mx.smartteam.entities.PopVisitaCollection GetVisitsPending(Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.PopVisitaCollection visitaCollection = new PopVisitaCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        
        String query = "SELECT"
                + " Id,IdProyecto,DeterminanteGSP,IdUsuario,Latitud,Longitud,IdStatus,datetime(fechacrea, 'unixepoch', 'localtime'),StatusSync,FotoEntrada,datetime(FechaEntrada, 'unixepoch', 'localtime'),FotoSalida, datetime(FechaSalida, 'unixepoch', 'localtime'), datetime(FechaCierre,'unixepoch', 'localtime') "
                + " FROM PopVisita "
                + " WHERE IdStatus >= 1 AND StatusSync = 0 AND IdProyecto = " + proyecto.Id.toString() + " AND IdUsuario = " + usuario.Id.toString() + " ORDER BY date(popvisita.fechacrea, 'unixepoch', 'localtime') ";
        
        Cursor cursor = db.rawQuery(query , null);

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.PopVisita visita = new mx.smartteam.entities.PopVisita();
                visita.Id = cursor.getInt(0);
                visita.IdProyecto = cursor.getInt(1);
                visita.DeterminanteGSP = cursor.getInt(2);
                visita.IdUsuario = cursor.getInt(3);
                visita.Latitud = cursor.getDouble(4);
                visita.Longitud = cursor.getDouble(5);
                visita.IdStatus = cursor.getInt(6);
                visita.FechaCrea = cursor.getString(7);
                visita.StatusSync = cursor.getInt(8);
                visita.FotoEntrada = cursor.getString(9) != null ? new StringBuilder(cursor.getString(9)) : null;
                visita.FechaEntrada = cursor.getString(10);
                visita.FotoSalida = cursor.getString(11) != null ? new StringBuilder(cursor.getString(11)) : null;
                visita.FechaSalida = cursor.getString(12);
                visita.FechaCierre = cursor.getString(13);
                if (visita.FechaCierre == null){
                    visita.FechaCierre = Utilerias.getFechaHoraandSeg().toString();
                }
                visitaCollection.add(visita);
            } while (cursor.moveToNext());

        }
        
        
        db.close();
        return visitaCollection;
    }
    //Forzamos al Usuario a que suba sus cambios de dias anteriores para poder continuar
    public static mx.smartteam.entities.PopVisitaCollection ForceUpChanges(final Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) {

        mx.smartteam.entities.PopVisitaCollection visitaCollection = new PopVisitaCollection();
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                " SELECT IdProyecto, IdUsuario, StatusSync, IdStatus, datetime(FechaCrea , 'unixepoch', 'localtime' ) "
                + " FROM PopVisita WHERE "
                + " datetime(FechaCrea, 'unixepoch', 'localtime' ) < '" + Utilerias.getFecha() + "' "
                + " AND StatusSync = 0 AND IdStatus >= 1 AND IdProyecto=? AND IdUsuario=? ", new String[]{proyecto.Id.toString(), usuario.Id.toString()});

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.PopVisita visita = new mx.smartteam.entities.PopVisita();
                visita.IdProyecto = cursor.getInt(0);
                visita.IdUsuario = cursor.getInt(1);
                visita.StatusSync = cursor.getInt(2);
                visita.IdStatus = cursor.getInt(3);
                visita.FechaCrea = cursor.getString(4);

                visitaCollection.add(visita);
            } while (cursor.moveToNext());
        }

        return visitaCollection;
    }

    public static mx.smartteam.entities.PopVisitaCollection GetAll(final Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) {

        mx.smartteam.entities.PopVisitaCollection visitaCollection = new PopVisitaCollection();
        try {

            if (usuario != null && proyecto != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db
                        .rawQuery("SELECT  Id,IdProyecto,DeterminanteGSP,IdUsuario,Latitud,Longitud,StatusSync,date(fechacrea, 'unixepoch', 'localtime') "
                        + "FROM PopVisita WHERE IdStatus >= 1 AND StatusSync=0 AND IdProyecto=? AND IdUsuario=? ", new String[]{proyecto.Id.toString(), usuario.Id.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.PopVisita visita = new mx.smartteam.entities.PopVisita();
                        visita.Id = cursor.getInt(0);
                        visita.IdProyecto = cursor.getInt(1);
                        visita.DeterminanteGSP = cursor.getInt(2);
                        visita.IdUsuario = cursor.getInt(3);
                        visita.Latitud = cursor.getDouble(4);
                        visita.Longitud = cursor.getDouble(5);
                        visita.StatusSync = cursor.getInt(6);
                        visita.FechaCrea = cursor.getString(7);

                        visitaCollection.add(visita);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return visitaCollection;
    }

    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita popVisita) throws Exception {
            mx.smartteam.entities.PopVisita visita = popVisita;
            ServiceURI service = new ServiceURI();
/* Aumentarle FechaCierre segun lo pidan */
            METHOD_NAME = "/VisitasInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object()
                        .key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                        .key("CadenaFecha").value(visita.FechaCrea)
                        .key("Latitud").value(visita.Latitud.toString())
                        .key("Longitud").value(visita.Longitud.toString())
                        .key("Estatus").value(visita.IdStatus.toString())
                     //     .key("FechaCierre").value(visita.FechaCierre)
                    .endObject()
                    .key("Visitas").object()
                        .key("FechaCierre").value(visita.FechaCierre)
                    .endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);


            if (!jsonResult.getString("VisitasInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [VisitaInsert]");
            }
        }

        public static void AsistenciaEntrada(mx.smartteam.entities.PopVisita popVisita) throws Exception {
            mx.smartteam.entities.PopVisita visita = popVisita;
            ServiceURI service = new ServiceURI();


            //byte[] image= Base64.decode(popVisita.FotoEntrada.toString(),Base64.DEFAULT);

            METHOD_NAME = "/AsistenciaEntradaFotoInsert";
            jsonString = new JSONStringer()
                    .object()
                        .key("Usuario")
                        .object()
                            .key("Id").value(visita.IdUsuario.toString())
                        .endObject()
                        .key("Proyecto")
                        .object()
                            .key("Id").value(visita.IdProyecto.toString())
                        .endObject()
                        .key("Tienda")
                        .object()
                            .key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                        .endObject()
                        .key("Foto")
                        .object()
                            .key("FotoBase64").value(visita.FotoEntrada.toString())
                        .key("CadenaFecha").value(visita.FechaEntrada)
                        .endObject()
                    .endObject();

            //Log.i("AsistenciaEntrada", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);


            if (!jsonResult.getString("AsistenciaEntradaFotoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [AsistenciaEntradaFotoInsert]");
            }
        }

        public static void AsistenciaSalida(mx.smartteam.entities.PopVisita popVisita) throws Exception {
            mx.smartteam.entities.PopVisita visita = popVisita;
            ServiceURI service = new ServiceURI();

            METHOD_NAME = "/AsistenciaSalidaFotoInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject()
                    .key("FotoBase64").value(popVisita.FotoSalida.toString())
                    .key("CadenaFecha").value(visita.FechaSalida)
                    .endObject()
                    .endObject();


            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);


            if (!jsonResult.getString("AsistenciaSalidaFotoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [AsistenciaSalidaFotoInsert]");
            }
        }
    }
}
