/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import mx.smartteam.entities.*;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class Sos {

    private static String METHOD_NAME = "/";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    /**
     * ****************************************************************************
     */
    public static void Insert(Context context, mx.smartteam.entities.Sos Sos) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();

        ContentValues values = new ContentValues();
        
        values.put("Valor", Sos.Valor);
        values.put("IdMarca", Sos.IdMarca);
        values.put("idVisita", Sos.IdVisita);
        values.put("idCategoria", Sos.IdCategoria);
        values.put("idfoto", Sos.IdFoto);
        db.insert("SOS", null, values);
        db.close();
    }

    /**
     * ************************Alan***********************************************
     */
    public static void Update(Context context, mx.smartteam.entities.Sos Sos) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Valor", Sos.Valor);
        values.put("IdFoto", Sos.IdFoto);
        values.put("StatusSync", 0);
        db.update("SOS", values, "id=" + Sos.Id, null);
        db.close();
    }

    /**
     * ****************************************************************************
     */
    public static mx.smartteam.entities.Sos GetByVisita(Context context, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Marca marca,
            mx.smartteam.entities.Categoria categoria) {

        mx.smartteam.entities.Sos Sos = null;

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT Id, IdMarca,IdVisita, Valor ,datetime(Fecha, 'unixepoch', 'localtime') as Fecha, StatusSync, FechaSync, idCategoria, idfoto "
                + " FROM Sos "
                + " Where idvisita = ? AND idmarca = ? AND idcategoria = ?",
                new String[]{
                    pop.IdVisita.toString(),
                    marca.Id.toString(),
                    categoria.Id.toString()
                }
        );

        if (cursor.moveToFirst()) {
            Sos = new mx.smartteam.entities.Sos();
            // Recorremos el cursor hasta que no haya más registros
            do {
                Sos.Id = cursor.getInt(0);
                Sos.IdMarca = cursor.getInt(1);
                Sos.IdVisita = cursor.getInt(2);
                Sos.Valor = cursor.getInt(3);
                Sos.Fecha = cursor.getString(4);
                Sos.StatusSync = cursor.getInt(5);
                Sos.FechaSync = cursor.getString(6);
                Sos.bandera =0;
                Sos.IdCategoria = cursor.getInt(7);
                Sos.IdFoto = cursor.getInt(8);
            } while (cursor.moveToNext());
        }

        return Sos;
    }
    
    
     public static mx.smartteam.entities.Sos ContestacionSos(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) {
        mx.smartteam.entities.Sos Sos = null;

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT Id,IdMarca,Idcategoria  FROM Sos Where idvisita = ? AND IdFoto = ?",
                new String[]{
                    visita.Id.toString(),
                    foto.Id.toString()
                }
        );

        if (cursor.moveToFirst()) 
        {
            Sos = new mx.smartteam.entities.Sos();
            // Recorremos el cursor hasta que no haya más registros
            do {
                Sos.Id = cursor.getInt(0);
                Sos.IdMarca = cursor.getInt(1);
                Sos.IdCategoria = cursor.getInt(2);
            } while (cursor.moveToNext());
        }

        return Sos;
    }

    public static mx.smartteam.entities.SosCollection getSosCollection(Context context, mx.smartteam.entities.PopVisita popVisita)
    {
        mx.smartteam.entities.SosCollection sosCollection = new mx.smartteam.entities.SosCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db
                .rawQuery(
                        "SELECT Id, IdMarca,IdVisita, Valor ,datetime(Fecha, 'unixepoch', 'localtime'), StatusSync, FechaSync,IdFoto,IdCategoria "
                        + " FROM Sos "
                        + " WHERE IdVisita= ? AND StatusSync = 0 ",
                        new String[]{
                            popVisita.Id.toString()
                        });

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Sos Sos = new mx.smartteam.entities.Sos();
                Sos.Id = cursor.getInt(0);
                Sos.IdMarca = cursor.getInt(1);
                Sos.IdVisita = cursor.getInt(2);
                Sos.Valor = cursor.getInt(3);
                Sos.Fecha = cursor.getString(4);
                Sos.StatusSync = cursor.getInt(5);
                Sos.FechaSync = cursor.getString(6);
                Sos.IdFoto = cursor.getInt(7);
                Sos.IdCategoria = cursor.getInt(8);
                sosCollection.add(Sos);
                
            } while (cursor.moveToNext());
        }
        db.close();
        return sosCollection;
    }
    /*Alan*********************************************************************/

    public static void UpdateStatusSync(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", 1 );
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("Sos", values, " IdVisita = " + popVisita.Id.toString(), null);
        db.close();
    }

     /**
     * ***********************************************************************************************
     */
    public static class Upload {

        public static String Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.SosCollection sosCollection) throws Exception {
            String strResult = "OK";
            METHOD_NAME = "/SosInsert";

            ServiceURI service = new ServiceURI();

            jsonString = new JSONStringer()
               .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject();
                    
                     jsonString.key("SosCollection").array();
                    for(mx.smartteam.entities.Sos sos : sosCollection)
                    {
                            jsonString.object()
                                    .key("IdMarca").value(sos.IdMarca.toString())
                                    .key("Valor").value(sos.Valor.toString())
                                    .key("Fecha").value(sos.Fecha)
                                    .key("IdCategoria").value(sos.IdCategoria)
                                    .endObject();
                    }

                    jsonString.endArray();
            jsonString.endObject();  

            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            if (!jsonResult.getString("SosInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [SosInsert]");
            }
            
            return strResult;
        }
    }
    /**
     * ***********************************************************************************************
     */
}
