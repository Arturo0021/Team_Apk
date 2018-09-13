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


public class Anaquel {
    
    private static String METHOD_NAME = "/";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
/*******************************************************************************/
    
    public static void Insert(Context context, mx.smartteam.entities.Anaquel anaquel)  throws Exception{
        
        db = (new BaseDatos(context)).getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        
        ContentValues values = new ContentValues();
        values.put("IdProyecto", anaquel.IdProyecto);
        values.put("IdUsuario", anaquel.IdUsuario);
        values.put("DeterminanteGSP", anaquel.DeterminanteGSP);
        values.put("Sku", anaquel.Sku);
        values.put("Cantidad", anaquel.Cantidad);
        values.put("Precio", anaquel.Precio);
        values.put("Comentario", anaquel.Comentario);
        values.put("Suelo", anaquel.Suelo);
        values.put("Manos", anaquel.Manos);
        values.put("Ojos", anaquel.Ojos);
        values.put("Techo", anaquel.Techo);
        values.put("Tipo", anaquel.Tipo);
        //values.put("FechaCrea", dateFormat.format(new Date()));
        values.put("IdVisita", anaquel.IdVisita);
        values.put("IdFoto", anaquel.IdFoto);
        
        
       if(anaquel.Fanaquel!=null){ 
           if(anaquel.Fanaquel==1){
               values.put("FAnaquel", 1);}else{
               values.put("FAnaquel", 0);
           }
       }else{
           values.put("FAnaquel", 0);
       }
        
       if(anaquel.Fprecio!=null){
           if(anaquel.Fprecio==1){
        values.put("FPrecio", 1);}
         else{
         values.put("FPrecio", 0);
         }
       }else{
         values.put("FPrecio", 0);
       }
        //values.put("FPrecio", anaquel.Fanaquel);

        values.put("StatusSync", 0);
        db.insert("Anaquel", null, values);
       db.close();
    }
    
/**************************Alan************************************************/
    public static void Update(Context context, mx.smartteam.entities.Anaquel anaquel)  throws Exception{
        
        db = (new BaseDatos(context)).getWritableDatabase();
           
        ContentValues values = new ContentValues();
        values.put("cantidad"  , anaquel.Cantidad);
        values.put("Precio"    , anaquel.Precio);
        values.put("Comentario", anaquel.Comentario);
        values.put("Suelo"     , anaquel.Suelo);
        values.put("Manos"     , anaquel.Manos);
        values.put("Ojos"      , anaquel.Ojos);
        values.put("Techo"     , anaquel.Techo);
        values.put("Tipo", anaquel.Tipo);
        values.put("StatusSync", 0);
        if(anaquel.Fanaquel!=null){ 
            if(anaquel.Fanaquel==1){
                values.put("FAnaquel", 1);}
            else{
                values.put("FAnaquel", 0);
            }
        }else{
              values.put("FAnaquel", 0);
        }
        
       if(anaquel.Fprecio!=null){
           if(anaquel.Fprecio==1){
               values.put("FPrecio", 1);
           }else{
               values.put("FPrecio", 0);
           }
       }else{
        values.put("FPrecio", 0);
       }
         //if (anaquel.IdFoto>0) {
            values.put("IdFoto", anaquel.IdFoto);
        //}
       
       
       
       db.update("Anaquel", values, "id=" + anaquel.Id, null);
       db.close();

    }
/*******************************************************************************/
  
    public static String countInsertVisita(Context context, mx.smartteam.entities.Pop pop){
        db = (new BaseDatos(context)) .getWritableDatabase();
        
        String qquery = "SELECT \n" +
            " Count(1) AS conteo, pon.ponderacion \n" +
            " FROM Anaquel a\n" +
            " INNER JOIN popvisita pv on  pv.id = a.idvisita\n" +
            " INNER JOIN pop p on p.determinantegsp = pv.determinantegsp AND pv.idproyecto = p.idproyecto\n" +
            " INNER JOIN ponderaciones pon on pon.idproyecto = p.idproyecto AND  pon.idcadena = p.idcadena\n" +
            " WHERE a.IdVisita= " + pop.IdVisita +" ;";
         
        Cursor cursor = db.rawQuery( qquery, null);
            String retorno = null;
        if (cursor.moveToFirst()){
         // Recorremos el cursor hasta que no haya más registros
            do{
                
                retorno = cursor.getString(0);
                retorno +=",";
                retorno += cursor.getString(1);
                
            }while (cursor.moveToNext());
        }
        
        
        return retorno;
    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop, String tipo){
            
        db = (new BaseDatos(context)).getWritableDatabase();
        int existe = 0;
        String query = "SELECT COUNT(1) FROM Anaquel WHERE IdVisita = " + pop.getIdVisita() + " AND TIPO = '" + tipo + "';" ;
        
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            existe = cursor.getInt(0);
        }
        
        return existe;
    }
    
    public static mx.smartteam.entities.ProductoCollection 
                  GetProductosByVisita(Context context, 
                                       mx.smartteam.entities.Proyecto proyecto , 
                                       mx.smartteam.entities.Usuario usuario,
                                       mx.smartteam.entities.Pop pop){//,mx.smartteam.entities.Producto producto)
         
        mx.smartteam.entities.ProductoCollection productoCollection = new mx.smartteam.entities.ProductoCollection();
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db.rawQuery(
                "SELECT sku                                 " + 
                "  FROM Anaquel                             " +
                " Where Idproyecto      = ? AND             " + 
                "       Idusuario       = ? AND             " + 
                "       IdVisita        = ? AND             " + 
                "       Determinantegsp = ? AND Fanaquel>0   " ,
                new String[]{
                    proyecto.Id.toString(),
                    usuario.Id.toString(),
                 // producto.SKU.toString(),
                    pop.IdVisita.toString(),
                    pop.DeterminanteGSP.toString()
                }
        );
        
        if (cursor.moveToFirst()){
         // Recorremos el cursor hasta que no haya más registros
            do{
                mx.smartteam.entities.Producto producto= new mx.smartteam.entities.Producto();
                producto.SKU = cursor.getLong(0);
                productoCollection.add(producto);
            }while (cursor.moveToNext());
        }
        
        return productoCollection;
    }
/****Alan*********************************************************************/
    public static mx.smartteam.entities.ProductoCollection 
                  GetProductosByVisita2(Context context, 
                                       mx.smartteam.entities.Proyecto proyecto , 
                                       mx.smartteam.entities.Usuario usuario,
                                       mx.smartteam.entities.Pop pop){//,mx.smartteam.entities.Producto producto)
         
        mx.smartteam.entities.ProductoCollection productoCollection = new mx.smartteam.entities.ProductoCollection();
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db.rawQuery(
                "SELECT sku                                 " + 
                "  FROM Anaquel                             " +
                " Where Idproyecto      = ? AND             " + 
                "       Idusuario       = ? AND             " + 
                "       IdVisita        = ? AND             " + 
                "       Determinantegsp = ? AND Fprecio>0   " ,
                new String[]{
                    proyecto.Id.toString(),
                    usuario.Id.toString(),
                 // producto.SKU.toString(),
                    pop.IdVisita.toString(),
                    pop.DeterminanteGSP.toString()
                }
        );
        
        if (cursor.moveToFirst()){
         // Recorremos el cursor hasta que no haya más registros
            do{
                mx.smartteam.entities.Producto producto= new mx.smartteam.entities.Producto();
                producto.SKU = cursor.getLong(0);
                productoCollection.add(producto);
            }while (cursor.moveToNext());
        }
        
        return productoCollection;
    }
/****Alan*********************************************************************/
     public static mx.smartteam.entities.Anaquel
                   GetInfoByVisita(Context context,        
                                   mx.smartteam.entities.Pop pop, 
                                   mx.smartteam.entities.Producto producto, String tipo){
  
        mx.smartteam.entities.Anaquel anaquel = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
       
        Cursor cursor = db
                .rawQuery(
                "Select Sku         as Sku,                             " +
                "       Cantidad    as Existencia,                      " +
                "       Precio      as Precio,                          " +
                "       Comentario  as Comentario,                      " +
                "       Techo       as FrenteAlturaTecho,               " +
                "       Ojos        as FrenteAlturaOjos,                " +
                "       Manos       as FrenteAlturaManos,               " +
                "       Suelo       as FrenteAlturaSuelo, Id  , IdFoto  " +
                "  From Anaquel                                         " +
                " where Sku=? AND                    " + 
                " IdVisita=? and tipo=? limit 1               " ,
                new String[]{
                             producto.SKU.toString(),
                             pop.IdVisita.toString(),
                             tipo
                            }
                          );
  
        if (cursor.moveToFirst()) 
        {
            anaquel = new mx.smartteam.entities.Anaquel();
            // Recorremos el cursor hasta que no haya más registros
            do {
                anaquel.Sku         =cursor.getLong(0);
                anaquel.Cantidad    =cursor.getInt(1);
                anaquel.Precio      =cursor.getDouble(2);
                anaquel.Comentario  =cursor.getString(3);
                anaquel.Techo       =cursor.getInt(4);
                anaquel.Ojos        =cursor.getInt(5);
                anaquel.Manos       =cursor.getInt(6);
                anaquel.Suelo       =cursor.getInt(7);
                anaquel.Id          =cursor.getInt(8);
                anaquel.IdFoto = cursor.getInt(9);
                //res=cursor.getInt(0);
               } 
            while(cursor.moveToNext());
        }
        return anaquel;
    }    
 
  /****Alan*********************************************************************/
     public static mx.smartteam.entities.Anaquel
                   GetInfoByVisita2(Context context, 
                                   mx.smartteam.entities.Proyecto proyecto, 
                                   mx.smartteam.entities.Usuario usuario,
                                   mx.smartteam.entities.Pop pop, 
                                   String producto)
    {
 
        mx.smartteam.entities.Anaquel anaquel = new mx.smartteam.entities.Anaquel();
        
        db = (new BaseDatos(context)).getWritableDatabase();
       
        Cursor cursor = db
            .rawQuery(
                "Select Sku         as Sku,                             " +
                "       Cantidad    as Existencia,                      " +
                "       Precio      as Precio,                          " +
                "       Comentario  as Comentario,                      " +
                "       Techo       as FrenteAlturaTecho,               " +
                "       Ojos        as FrenteAlturaOjos,                " +
                "       Manos       as FrenteAlturaManos,               " +
                "       Suelo       as FrenteAlturaSuelo, Id            " +
                "  From Anaquel                                         " +
                " Where Idproyecto      = ? AND                         " + 
                "       Idusuario       = ? AND                         " + 
                "       Sku             = (select p.sku from producto p,  anaquel a where p.sku=a.sku and p.nombre=?) AND                         " + 
                "       IdVisita        = ? AND                         " + 
                "       Determinantegsp = ? limit 1                     " ,
                new String[]{
                    proyecto.Id.toString(),
                    usuario.Id.toString(),
                    producto,
                    pop.IdVisita.toString(),
                    pop.DeterminanteGSP.toString()          
                }
            );
  
        if (cursor.moveToFirst()) 
        {
            // Recorremos el cursor hasta que no haya más registros
            do {
                anaquel.Sku         =cursor.getLong(0);
                anaquel.Cantidad    =cursor.getInt(1);
                anaquel.Precio      =cursor.getDouble(2);
                anaquel.Comentario  =cursor.getString(3);
                anaquel.Techo       =cursor.getInt(4);
                anaquel.Ojos        =cursor.getInt(5);
                anaquel.Manos       =cursor.getInt(6);
                anaquel.Suelo       =cursor.getInt(7);
                anaquel.Id          =cursor.getInt(8);
                
                //res=cursor.getInt(0);
               } 
            while(cursor.moveToNext());
        }
        return anaquel;
        }    
    /*Alan*********************************************************************/      
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.Anaquel anaquel) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", anaquel.StatusSync);
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("Anaquel", values, "Id=" + anaquel.Id.toString(), null);

        db.close();
    }
/**************************************************************************************************/
    public static mx.smartteam.entities.AnaquelCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        mx.smartteam.entities.AnaquelCollection anaquelCollection = new AnaquelCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "SELECT  Id,Cantidad,Precio,Comentario,Suelo,Manos,Ojos,Techo ,datetime(fechacrea, 'unixepoch', 'localtime'),statussync,sku,idfoto "
                + "FROM Anaquel "
                + "WHERE IdVisita=? ",
                new String[]{popVisita.Id.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Anaquel anaquel = new mx.smartteam.entities.Anaquel();
                anaquel.Id = cursor.getInt(0);
                anaquel.Cantidad = cursor.getInt(1);
                anaquel.Precio = cursor.getDouble(2);
                anaquel.Comentario = cursor.getString(3);
                anaquel.Suelo = cursor.getInt(4);
                anaquel.Manos = cursor.getInt(5);
                anaquel.Ojos = cursor.getInt(6);
                anaquel.Techo = cursor.getInt(7);
                anaquel.FechaCrea = cursor.getString(8);
                anaquel.StatusSync = cursor.getInt(9);
                anaquel.Sku = cursor.getLong(10);
                anaquel.IdFoto = cursor.getInt(11);
                anaquelCollection.add(anaquel);
            } while (cursor.moveToNext());

        }

        db.close();

        return anaquelCollection;
    }
    
     public static mx.smartteam.entities.Anaquel ContestacionAnaquel(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto){
  
        mx.smartteam.entities.Anaquel anaquel = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
       
        Cursor cursor = db
                .rawQuery(
                "Select Sku as Sku," +
                "Cantidad    as Existencia," +
                "Precio      as Precio," +
                "Comentario  as Comentario," +
                "Techo       as FrenteAlturaTecho," +
                "Ojos        as FrenteAlturaOjos," +
                "Manos       as FrenteAlturaManos," +
                "Suelo       as FrenteAlturaSuelo, Id," +
                "IdFoto      as IdFoto " +
                " From Anaquel " +
                " WHERE IdVisita=? and idfoto =? " ,
                new String[]{
                    visita.Id.toString(),
                    foto.Id.toString()
                    }
                );
  
        if (cursor.moveToFirst()) 
        {
            anaquel = new mx.smartteam.entities.Anaquel();
            // Recorremos el cursor hasta que no haya más registros
            do {
                anaquel.Sku         =cursor.getLong(0);
                anaquel.Cantidad    =cursor.getInt(1);
                anaquel.Precio      =cursor.getDouble(2);
                anaquel.Comentario  =cursor.getString(3);
                anaquel.Techo       =cursor.getInt(4);
                anaquel.Ojos        =cursor.getInt(5);
                anaquel.Manos       =cursor.getInt(6);
                anaquel.Suelo       =cursor.getInt(7);
                anaquel.Id          =cursor.getInt(8);
                
                //res=cursor.getInt(0);
               } 
            while(cursor.moveToNext());
        }
        return anaquel;
    }
    
    
    
    
    
    
    
    /**************************************************************************************************/
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Anaquel anaquel) throws Exception {

            METHOD_NAME = "/AnaquelInsert";

            ServiceURI service = new ServiceURI();

            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                    .key("SKU").value(anaquel.Sku.toString())
                    .key("CantAnaquel").value(anaquel.Cantidad.toString())
                    .key("Precio").value(anaquel.Precio.toString())
                    .key("ComentarioAnaquel").value(anaquel.Comentario)
                    .key("TramosAnaquel").value("0")
                    .key("CadenaFecha").value(anaquel.FechaCrea)
                    .key("Ojo").value(anaquel.Ojos.toString())
                    .key("Suelo").value(anaquel.Suelo.toString())
                    .key("Manos").value(anaquel.Manos.toString())
                    .key("Techo").value(anaquel.Techo.toString())
                    //.key("IdFoto").value(anaquel.IdFoto.toString())
                    .endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            if (!jsonResult.getString("AnaquelInsertResult").equals("OK")) {
           throw new Exception("Error el el servicio [AnaquelInsert]");
            }
        }
    }
    /**************************************************************************************************/
}
