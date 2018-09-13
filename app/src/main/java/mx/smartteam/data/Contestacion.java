package mx.smartteam.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Contestacion {

    private static String METHOD_NAME = "/GetOrdenSondeos";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
    private static Context context;

    public static mx.smartteam.entities.ContestacionCollection ContestacionGetByVisita(final Context context,
            mx.smartteam.entities.PopVisita popVisita,
            mx.smartteam.entities.Sondeo sondeo) {

        mx.smartteam.entities.ContestacionCollection contestacionCollection = new mx.smartteam.entities.ContestacionCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

            String query = "SELECT Id, IdSondeo, IdVisita, StatusSync, idfoto, sku From Contestacion Where "
                                + " StatusSync = 0 "
                                + " AND IdVisita = " + popVisita.Id
                                + " AND IdSondeo = " + sondeo.Id + ";";
        
        Cursor cursor = db.rawQuery(query, null);

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Contestacion contestacion = new mx.smartteam.entities.Contestacion();
                contestacion.Id = cursor.getInt(0);
                contestacion.IdSondeo = cursor.getInt(1);
                contestacion.IdVisita = cursor.getInt(2);
                contestacion.StatusSync = cursor.getInt(3);
                contestacion.IdFoto = cursor.getInt(4);
                contestacion.Sku = cursor.getLong(5);
                contestacionCollection.add(contestacion);
            } while (cursor.moveToNext());
        }
        return contestacionCollection;
    }

    public static mx.smartteam.entities.Contestacion ContestacionGetByVisitaBySondeo(final Context context,
            mx.smartteam.entities.PopVisita popVisita,
            mx.smartteam.entities.SondeoModulos sondeo) {

        mx.smartteam.entities.Contestacion contestacion = null;// new mx.smartteam.entities.ContestacionCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                //select Id, IdSondeo, IdVisita, StatusSync
                "SELECT Id, IdSondeo,IdVisita, StatusSync, idfoto,sku From Contestacion Where "
               // + " StatusSync = 0 "
                + "  IdVisita = ?"
                + " AND IdSondeo = ?", new String[]{popVisita.Id.toString(), sondeo.Id.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                contestacion = new mx.smartteam.entities.Contestacion();
                contestacion.Id = cursor.getInt(0);
                contestacion.IdSondeo = cursor.getInt(1);
                contestacion.IdVisita = cursor.getInt(2);
                contestacion.IdFoto = cursor.getInt(3);
                contestacion.Sku = cursor.getLong(4);
                //contestacionCollection.add(contestacion);
            } while (cursor.moveToNext());
        }
        return contestacion;
    }
    
    
    
    public static mx.smartteam.entities.Contestacion ContestacionGetByVisitaBySondeoByMarca(final Context context,
            mx.smartteam.entities.PopVisita popVisita,
            mx.smartteam.entities.SondeoModulos sondeo,
            mx.smartteam.entities.Marca marca
    ) {

        mx.smartteam.entities.Contestacion contestacion = null;// new mx.smartteam.entities.ContestacionCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        try{
        Cursor cursor = db.rawQuery(
                //select Id, IdSondeo, IdVisita, StatusSync
                "SELECT  c.id, c.idsondeo, c.IdVisita, c.StatusSync, idFoto, sku \n" +
                " FROM ContestacionPregunta cp  Inner Join Contestacion c on c.id = cp.idcontestacion \n" +
                " WHERE  c.idvisita = ? and c.idsondeo = ? and cp.idmarca = ? \n" +
                " group by c.id ", new String[]{popVisita.Id.toString(), sondeo.Id.toString(), marca.Id.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                contestacion = new mx.smartteam.entities.Contestacion();
                contestacion.Id = cursor.getInt(0);
                contestacion.IdSondeo = cursor.getInt(1);
                contestacion.IdVisita = cursor.getInt(2);
                contestacion.IdFoto = cursor.getInt(3);
                contestacion.Sku = cursor.getLong(4);
                //contestacionCollection.add(contestacion);
            } while (cursor.moveToNext());
        }
        }catch(Exception ex){
        ex.getMessage().toString();
        }
        return contestacion;
    }
    
    public static mx.smartteam.entities.Contestacion ContestacionSondeoPreguntaDinamica(final Context context,
            mx.smartteam.entities.PopVisita popVisita,
            mx.smartteam.entities.Sondeo sondeo) {

        mx.smartteam.entities.Contestacion contestacion = null;// new mx.smartteam.entities.ContestacionCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                //select Id, IdSondeo, IdVisita, StatusSync
                "SELECT Id, IdSondeo,IdVisita, StatusSync, idfoto,sku From Contestacion Where "
               // + " StatusSync = 0 "
                + "  IdVisita = ?"
                + " AND IdSondeo = ?", new String[]{popVisita.Id.toString(), sondeo.Id.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                contestacion = new mx.smartteam.entities.Contestacion();
                contestacion.Id = cursor.getInt(0);
                contestacion.IdSondeo = cursor.getInt(1);
                contestacion.IdVisita = cursor.getInt(2);
                contestacion.IdFoto = cursor.getInt(3);
                contestacion.Sku = cursor.getLong(4);
                //contestacionCollection.add(contestacion);
            } while (cursor.moveToNext());
        }
        return contestacion;
    }
    

    public static mx.smartteam.entities.Contestacion ContestacionGetByVisitaBySondeoBySku(final Context context,
            mx.smartteam.entities.PopVisita popVisita,
            mx.smartteam.entities.SondeoModulos sondeo, mx.smartteam.entities.Producto producto) {

        mx.smartteam.entities.Contestacion contestacion =null;// new mx.smartteam.entities.Contestacion();

        db = (new BaseDatos(context)).getWritableDatabase();

//        String query = String.format(Locale.getDefault(), "SELECT Id, IdSondeo,IdVisita, StatusSync, idfoto,sku From Contestacion Where "
//                + " StatusSync = 0 "
//                + " AND IdVisita = ?"
//                + " AND IdSondeo = ? and sku=?", new String[]{popVisita.Id.toString(), sondeo.Id.toString(), producto.SKU.toString()}
//        );

        Cursor cursor = db.rawQuery(
                //select Id, IdSondeo, IdVisita, StatusSync
                "SELECT Id, IdSondeo,IdVisita, StatusSync, idfoto,sku From Contestacion Where "
                //+ " StatusSync = 0 "
                + "  IdVisita = ?"
                + " AND IdSondeo = ? and sku=?", new String[]{popVisita.Id.toString(), sondeo.Id.toString(), producto.SKU.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                contestacion = new mx.smartteam.entities.Contestacion();
                contestacion.Id = cursor.getInt(0);
                contestacion.IdSondeo = cursor.getInt(1);
                contestacion.IdVisita = cursor.getInt(2);
                contestacion.IdFoto = cursor.getInt(4);
                contestacion.Sku = cursor.getLong(5);
                //contestacionCollection.add(contestacion);
            } while (cursor.moveToNext());
        }
        return contestacion;
    }

    public static mx.smartteam.entities.ProductoCollection getProductosContestados(final Context context,
            mx.smartteam.entities.Pop pop,
            mx.smartteam.entities.Sondeo sondeo,
            mx.smartteam.entities.Usuario usuario,
            mx.smartteam.entities.Proyecto proyecto
    ) {
        mx.smartteam.entities.ProductoCollection productosCollection = new mx.smartteam.entities.ProductoCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                //select Id, IdSondeo, IdVisita, StatusSync
                " select sku from Contestacion c "
                + " LEFT JOIN ContestacionPregunta cp  ON cp.idcontestacion = c.id  "
                + " Where "
                + "  c.IdVisita = ? " //+ pop.IdVisita 
                + " AND c.IdSondeo = ? "
                + " AND c.idUsuario = ? ", new String[]{
                    pop.IdVisita.toString(),
                    sondeo.Id.toString(),
                    usuario.Id.toString()
                }
        );

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                producto.SKU = cursor.getLong(0);

                productosCollection.add(producto);

            } while (cursor.moveToNext());
        }

        return productosCollection;
    }

    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo, /*null*/ mx.smartteam.entities.RespuestaSondeoCollection respuestaCollection) throws Exception {

            METHOD_NAME = "/SondeoInsert";
            ServiceURI service = new ServiceURI();

            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Sondeo").object().key("Id").value(sondeo.Id.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                    .endObject();

            //Respuestas
            jsonString.key("Respuestas").array();
            for (mx.smartteam.entities.RespuestaSondeo respuestaSondeo : respuestaCollection) {
                jsonString.object()
                        .key("IdPregunta").value(respuestaSondeo.IdPregunta.toString())
                        .key("Valor").value(respuestaSondeo.Respuesta)
                        .key("Tipo").value(respuestaSondeo.Tipo)
                        .key("CadenaFecha").value(respuestaSondeo.FechaCrea);

                jsonString.key("Opciones").array();
                for (mx.smartteam.entities.Opcion opcion : respuestaSondeo.Opciones) {
                    jsonString.object()
                            .key("Id").value(opcion.Id.toString())
                            .key("Nombre").value(opcion.Nombre.toString())
                            .endObject();
                }
                jsonString.endArray();
                jsonString.endObject();
            }

            jsonString.endArray();
            jsonString.endObject();

            Log.i("JSON", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            if (!jsonResult.getString("SondeoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [SondeoInsert]");
            }

        }
    }
    public static int[ ]  ContestacionId(final Context context,String respuesta,mx.smartteam.entities.PopVisita popVisita) {        
        db = (new BaseDatos(context)).getWritableDatabase();
        int [ ]  idcont= new int[2 ]  ;
        idcont[0]=0;
        idcont[1 ]=0;
       // Cursor cursor2 = db.rawQuery("SELECT idcontestacion  as cont From Contestacionpregunta Where   respuesta=' "+respuesta+"'", null);
                /*Cursor cursor2 = db.rawQuery(                
              "SELECT id  as cont,idcontestacion From Contestacionpregunta Where   respuesta=?",
                        new String[]{respuesta});*/
          Cursor cursor2 = db.rawQuery(                
              "SELECT cp.id  as cont,idcontestacion FROM  Contestacionpregunta cp "
                      + " INNER JOIN contestacion c ON c.id=cp.idcontestacion"
                      + " WHERE   cp.respuesta=? and idvisita=? ",
                        new String[]{respuesta,popVisita.Id.toString()});
// Nos aseguramos de que existe al menos un registro
        if (cursor2.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {                
                
                idcont [0 ] = cursor2.getInt(0);
                idcont [1 ] = cursor2.getInt(1);
            } while (cursor2.moveToNext());
        }
        return idcont;
    }
}
