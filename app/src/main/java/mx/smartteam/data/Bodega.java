/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.Anquel;
import mx.triplei.R;
import mx.smartteam.entities.AnaquelCollection;
import mx.smartteam.entities.BodegaCollection;
import mx.smartteam.exceptions.AnaquelException;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Bodega {

    private static String METHOD_NAME = "/GetCategorias";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
/**********************************************************************************************************/
    public static void Insert(Context context, mx.smartteam.entities.Bodega bodega) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        ContentValues values = new ContentValues();
        values.put("IdProyecto", bodega.IdProyecto);
        values.put("IdUsuario", bodega.IdUsuario);
        values.put("DeterminanteGSP", bodega.DeterminanteGSP);
        values.put("Sku", bodega.Sku);
        values.put("Cantidad", bodega.Cantidad);
        values.put("Tarima", bodega.Tarima);
        values.put("Comentario", bodega.Comentario);
        //values.put("FechaCrea", dateFormat.format(new Date()));
        values.put("StatusSync", 0);
        values.put("IdVisita", bodega.IdVisita);
        db.insert("Bodega", null, values);

        db.close();


    }

    /*Alan***************************************************************************************/
                   
        public static void Update(Context context, mx.smartteam.entities.Bodega bodega)  throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
           
        ContentValues values = new ContentValues();
        values.put("Cantidad", bodega.Cantidad);
        values.put("Tarima", bodega.Tarima);
        values.put("Comentario", bodega.Comentario);
        values.put("IdFoto", bodega.IdFoto);
        db.update("Bodega", values, "Id=" + bodega.Id.toString(), null);
  db.close();
    }
/**********************************************************************************************************/
 public static mx.smartteam.entities.ProductoCollection 
                   GetProductosByVisita(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,
                   mx.smartteam.entities.Pop pop){
        mx.smartteam.entities.ProductoCollection productoCollection = new mx.smartteam.entities.ProductoCollection();
         
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "   SELECT sku                             " +
                "     FROM Bodega                          " +
               " Where Idproyecto      = ? AND         " + 
                "       Idusuario       = ? AND         " + 
                //"       Sku             = ? AND         " + 
                "       IdVisita        = ? AND         " + 
                "       Determinantegsp = ?     " ,
                new String[]{
                             proyecto.Id.toString(),
                             usuario.Id.toString(),
                          // producto.SKU.toString(),
                             pop.IdVisita.toString(),
                             pop.DeterminanteGSP.toString()          
                            }
                          );
        
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Producto producto= new mx.smartteam.entities.Producto();
               producto.SKU = cursor.getLong(0);
                productoCollection.add(producto);
            } while (cursor.moveToNext());
        }
        
        return productoCollection;
        }
        
                
  
  /**********************************************************************************************************/
 public static mx.smartteam.entities.Bodega
        GetInfoByVisita(Context context, mx.smartteam.entities.Proyecto proyecto , 
                mx.smartteam.entities.Usuario usuario,
        mx.smartteam.entities.Pop pop, mx.smartteam.entities.Producto producto){
            
        mx.smartteam.entities.Bodega bodega = null;
         
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "Select Sku             as Sku,         " +
                " Cantidad        as Existencia,  " +
                " Tarima          as Tarima,      " +
                " Comentario      as Comentario , Id, IdFoto  " +
                "  From Bodega                          " +
                " Where Idproyecto= ? AND Idusuario = ? AND Sku = ? AND IdVisita = ? AND Determinantegsp = ? limit 1   ",
                new String[]{
                 proyecto.Id.toString(),
                    usuario.Id.toString(),
                    producto.SKU.toString(),
                    pop.IdVisita.toString(),
                    pop.DeterminanteGSP.toString()
                });
        
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                 bodega = new mx.smartteam.entities.Bodega();
                bodega.Sku= cursor.getLong(0);
                bodega.Cantidad= cursor.getInt(1);
                bodega.Tarima= cursor.getInt(2);
                bodega.Comentario= cursor.getString(3);
                bodega.Id = cursor.getInt(4);
                bodega.IdFoto = cursor.getInt(5);
                
            } while (cursor.moveToNext());
        }
        
        return bodega;
        }    

   /**********************************************************************************************************/
         public static mx.smartteam.entities.Bodega
        GetInfoByVisita2(Context context, mx.smartteam.entities.Proyecto proyecto , 
                mx.smartteam.entities.Usuario usuario,
        mx.smartteam.entities.Pop pop, String producto){
            
        mx.smartteam.entities.BodegaCollection bodegaCollection = new mx.smartteam.entities.BodegaCollection();
         mx.smartteam.entities.Bodega bodega = new mx.smartteam.entities.Bodega();
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "Select Sku             as Sku,         " +
                "       Cantidad        as Existencia,  " +
                "       Tarima          as Tarima,      " +
                "       Comentario      as Comentario ,Id  " +
                "  From Bodega                          " +
                " Where Idproyecto= ? AND Idusuario = ? AND Sku = (select p.sku from producto p,  bodega a where p.sku=a.sku and p.nombre = ? ) "
                        + " AND IdVisita = ? AND Determinantegsp = ? limit 1   ",
                new String[]{
                 proyecto.Id.toString(),
                    usuario.Id.toString(),
                    producto,
                    pop.IdVisita.toString(),
                    pop.DeterminanteGSP.toString()
                });
        
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                bodega.Sku= cursor.getLong(0);
                bodega.Cantidad= cursor.getInt(1);
                bodega.Tarima= cursor.getInt(2);
                bodega.Comentario= cursor.getString(3);
                bodega.Id = cursor.getInt(4);
                bodegaCollection.add(bodega);
                
                
            } while (cursor.moveToNext());
        }
        
        return bodega;
        }
        
        public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) {
        
            db = (new BaseDatos(context)).getWritableDatabase();
            int existe = 0;
            String query = "SELECT COUNT(1) FROM Bodega WHERE IdVisita = " + pop.getIdVisita() + ";";
            
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                existe = cursor.getInt(0);
            }
           
            return existe;
        }

   /**********************************************************************************************************/
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.Bodega bodega) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", bodega.StatusSync);
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("Bodega", values, "Id=" + bodega.Id.toString(), null);
        db.close();



    }
   /**********************************************************************************************************/
    public static mx.smartteam.entities.BodegaCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        mx.smartteam.entities.BodegaCollection bodegaCollection = new BodegaCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "SELECT  Id,Cantidad,Tarima,Comentario,datetime(fechacrea, 'unixepoch', 'localtime'),statussync,sku "
                + "FROM Bodega "
                + "WHERE IdVisita=? ",
                new String[]{popVisita.Id.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Bodega bodega = new mx.smartteam.entities.Bodega();
                bodega.Id = cursor.getInt(0);
                bodega.Cantidad = cursor.getInt(1);
                bodega.Tarima = cursor.getInt(2);
                bodega.Comentario = cursor.getString(3);
                bodega.FechaCrea = cursor.getString(4);
                bodega.StatusSync = cursor.getInt(5);
                bodega.Sku = cursor.getLong(6);


                bodegaCollection.add(bodega);
            } while (cursor.moveToNext());

        }
        db.close();
        return bodegaCollection;
    }
    
    
    public static mx.smartteam.entities.Bodega ContestacionBodega(
            Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto){
  
        mx.smartteam.entities.Bodega bodega = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
       
        Cursor cursor = db
                .rawQuery(
                "Select Sku as Sku," +
                "IdFoto as IdFoto " +
                " From Anaquel " +
                " WHERE IdVisita=? and idfoto =? " ,
                new String[]{
                    visita.Id.toString(),
                    foto.Id.toString()
                    }
                );
  
        if (cursor.moveToFirst()) 
        {
            bodega = new mx.smartteam.entities.Bodega();
            // Recorremos el cursor hasta que no haya más registros
            do {
                bodega.Sku=cursor.getLong(0);
                bodega.Id=cursor.getInt(1);
               } 
            while(cursor.moveToNext());
        }
        return bodega;
    }
    
    
/**********************************************************************************************************/
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Bodega bodega) throws Exception {

            ServiceURI service = new ServiceURI();

            METHOD_NAME = "/BodegaInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                    .key("SKU").value(bodega.Sku.toString())
                    .key("CantBodega").value(bodega.Cantidad.toString())
                    .key("ComentarioBodega").value(bodega.Comentario.toString())
                    .key("TarimasBodega").value(bodega.Tarima.toString())
                    .key("CadenaFecha").value(bodega.FechaCrea)
                    .endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("BodegaInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [BodegaInsert]");
            }




        }
    }
/**********************************************************************************************************/
  
}
