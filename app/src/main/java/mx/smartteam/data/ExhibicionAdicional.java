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
import java.text.SimpleDateFormat;
import mx.smartteam.entities.ExhibicionAdicionalCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ExhibicionAdicional {

    private static String METHOD_NAME = "/";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
  /**********************************************************************************************************/
    public static void Insert(Context context, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        ContentValues values = new ContentValues();
        values.put("IdProyecto", exhibicionAdicional.IdProyecto);
        values.put("IdUsuario", exhibicionAdicional.IdUsuario);
        values.put("DeterminanteGSP", exhibicionAdicional.DeterminanteGSP);
        values.put("Sku", exhibicionAdicional.Sku);
        values.put("IdVisita", exhibicionAdicional.IdVisita);
        values.put("Cabecera", exhibicionAdicional.Cabecera);
        values.put("CabeceraFrente", exhibicionAdicional.CabeceraFrente);
        values.put("Isla", exhibicionAdicional.Isla);
        values.put("IslaFrente", exhibicionAdicional.IslaFrente);
        values.put("Exhibidor", exhibicionAdicional.Exhibidor);
        values.put("ExhibidorFrente", exhibicionAdicional.ExhibidorFrente);
        values.put("Bunker", exhibicionAdicional.Bunker);
        values.put("BunkerFrente", exhibicionAdicional.BunkerFrente);
        values.put("Area", exhibicionAdicional.Area);
        values.put("AreaFrente", exhibicionAdicional.AreaFrente);
        values.put("Tira", exhibicionAdicional.Tira);
        values.put("TiraFrente", exhibicionAdicional.TiraFrente);
        values.put("Caja", exhibicionAdicional.Caja);
        values.put("CajaFrente", exhibicionAdicional.CajaFrente);
        values.put("Arete", exhibicionAdicional.Arete);
        values.put("AreteFrente", exhibicionAdicional.AreteFrente);
        values.put("Forway", exhibicionAdicional.Forway);
        values.put("ForwayFrente", exhibicionAdicional.ForwayFrente);
        values.put("Otros", exhibicionAdicional.Otros);
        values.put("Comentario", exhibicionAdicional.Comentario);
        values.put("IdFoto", exhibicionAdicional.IdFoto);
        //values.put("FechaCrea", dateFormat.format(new Date()));
        values.put("StatusSync", 0);

        db.insert("ExhibicionAdicional", null, values);
        db.close();

    }
  /**********************************************************************************************************/
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", exhibicionAdicional.StatusSync);
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("ExhibicionAdicional", values, "Id=" + exhibicionAdicional.Id.toString(), null);

        db.close();

    }
    /**********************************************************************************************************/  
      public static mx.smartteam.entities.ProductoCollection 
        GetProductosByVisita(Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop){
        mx.smartteam.entities.ProductoCollection productoCollection = new mx.smartteam.entities.ProductoCollection();
         
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                "SELECT sku "
                + "FROM ExhibicionAdicional "+
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
 /**
     * @param context********************************************************************************************************/  
             public static void Update(Context context, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional)  throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
           
        ContentValues values = new ContentValues();
        values.put("Cabecera", exhibicionAdicional.Cabecera);
        values.put("CabeceraFrente", exhibicionAdicional.CabeceraFrente);
        values.put("Isla", exhibicionAdicional.Isla);
        values.put("IslaFrente", exhibicionAdicional.IslaFrente);
        values.put("Exhibidor", exhibicionAdicional.Exhibidor);
        values.put("ExhibidorFrente", exhibicionAdicional.ExhibidorFrente);
        values.put("Bunker", exhibicionAdicional.Bunker);
        values.put("BunkerFrente", exhibicionAdicional.BunkerFrente);
        values.put("Area", exhibicionAdicional.Area);
        values.put("Tira", exhibicionAdicional.Tira);
        values.put("TiraFrente", exhibicionAdicional.TiraFrente);
        values.put("Caja", exhibicionAdicional.Caja);
        values.put("CajaFrente", exhibicionAdicional.CajaFrente);
        values.put("Arete", exhibicionAdicional.AreteFrente);
        values.put("AreteFrente", exhibicionAdicional.AreteFrente);
        values.put("Forway", exhibicionAdicional.Forway);
        values.put("ForwayFrente", exhibicionAdicional.ForwayFrente);
        values.put("IdFoto", exhibicionAdicional.IdFoto);
        
        db.update("ExhibicionAdicional", values, "Id=" + exhibicionAdicional.Id.toString(), null);


  db.close();

    }   
 /**********************************************************************************************************/
    public static mx.smartteam.entities.ExhibicionAdicionalCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) {

        mx.smartteam.entities.ExhibicionAdicionalCollection exhibicionAdicionalCollection = new ExhibicionAdicionalCollection();
        try {
            if (popVisita != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db
                        .rawQuery(
                                "SELECT  Id,IdProyecto,IdUsuario,DeterminanteGSP,Sku,IdVisita,Cabecera,CabeceraFrente,Isla,IslaFrente , Exhibidor, ExhibidorFrente, Bunker, BunkerFrente, Area, AreaFrente, Tira, TiraFrente , Caja , CajaFrente , Arete , AreteFrente , Forway , ForwayFrente , Otros , Comentario, datetime(fechacrea, 'unixepoch', 'localtime'), StatusSync , FechaSync "
                                + "FROM ExhibicionAdicional "
                                + "WHERE IdVisita=? ",
                                new String[]{popVisita.Id.toString()});

                // Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional = new mx.smartteam.entities.ExhibicionAdicional();
                        exhibicionAdicional.Id = cursor.getInt(0);
                        exhibicionAdicional.IdProyecto = cursor.getInt(1);
                        exhibicionAdicional.IdUsuario = cursor.getInt(2);
                        exhibicionAdicional.DeterminanteGSP = cursor.getInt(3);
                        exhibicionAdicional.Sku = cursor.getLong(4);
                        exhibicionAdicional.IdVisita = cursor.getInt(5);
                        exhibicionAdicional.Cabecera = cursor.getInt(6);
                        exhibicionAdicional.CabeceraFrente = cursor.getInt(7);
                        exhibicionAdicional.Isla = cursor.getInt(8);
                        exhibicionAdicional.IslaFrente = cursor.getInt(9);
                        exhibicionAdicional.Exhibidor = cursor.getInt(10);
                        exhibicionAdicional.ExhibidorFrente = cursor.getInt(11);
                        exhibicionAdicional.Bunker = cursor.getInt(12);
                        exhibicionAdicional.BunkerFrente = cursor.getInt(13);
                        exhibicionAdicional.Area = cursor.getInt(14);
                        exhibicionAdicional.AreaFrente = cursor.getInt(15);
                        exhibicionAdicional.Tira = cursor.getInt(16);
                        exhibicionAdicional.TiraFrente = cursor.getInt(17);
                        exhibicionAdicional.Caja = cursor.getInt(18);
                        exhibicionAdicional.CajaFrente = cursor.getInt(19);
                        exhibicionAdicional.Arete = cursor.getInt(20);
                        exhibicionAdicional.AreteFrente = cursor.getInt(21);
                        exhibicionAdicional.Forway = cursor.getInt(22);
                        exhibicionAdicional.ForwayFrente = cursor.getInt(23);
                        exhibicionAdicional.Otros = cursor.getInt(24);
                        exhibicionAdicional.Comentario = cursor.getString(25);
                        exhibicionAdicional.FechaCrea = cursor.getString(26);
                        exhibicionAdicional.StatusSync = cursor.getInt(27);
                        exhibicionAdicional.FechaSync = cursor.getString(28);

                        exhibicionAdicionalCollection.add(exhibicionAdicional);
                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        return exhibicionAdicionalCollection;
    }
  /**
     * @param context
     * @param proyecto
     * @param usuario
     * @param pop*
     * @param producto
     * @return *******************************************************************************************************/
    public static mx.smartteam.entities.ExhibicionAdicional
                  GetInfoByVisita(Context context, 
                                  mx.smartteam.entities.Proyecto proyecto , 
                                  mx.smartteam.entities.Usuario usuario,
                                  mx.smartteam.entities.Pop pop, 
                                  mx.smartteam.entities.Producto producto)
    {
        mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                " SELECT sku,                               " + 
                "        Cabecera,                          " + 
                "        CabeceraFrente ,                   " +
                "        Isla,                              " +
                "        IslaFrente,                        " +
                "        exhibidor,                         " +
                "        exhibidorFrente,                   " +
                "        bunker,                            " +
                "        bunkerFrente,                      " +
                "        Area,                              " +
                "        Tira,                              " +
                "        TiraFrente,                        " +
                "        Caja,                              " +
                "        CajaFrente,                        " +
                "        arete,                             " +
                "        AreteFrente,                       " +
                "        Forway,                            " +
                "        ForwayFrente,                      " +
                "        Otros,                             " +
                "        Comentario,             " + 
                "        id  , OtrosFrente , idfoto " + 
                "   From ExhibicionAdicional                " +
                "  Where Idproyecto      = ? AND            " + 
                "        Idusuario       = ? AND            " + 
                "        Sku             = ? AND            " + 
                "        IdVisita        = ? AND            " + 
                "        Determinantegsp = ?     " ,
                new String[]
                {
                    proyecto.Id.toString(),
                    usuario.Id.toString(),
                    producto.SKU.toString(),
                    pop.IdVisita.toString(),
                    pop.DeterminanteGSP.toString()          
                }
            );
        
        if (cursor.moveToFirst()) 
        {
            exhibicionAdicional = new mx.smartteam.entities.ExhibicionAdicional();
            // Recorremos el cursor hasta que no haya más registros
            do {
                exhibicionAdicional.Sku = cursor.getLong(0);
                exhibicionAdicional.Cabecera = cursor.getInt(1);
                exhibicionAdicional.CabeceraFrente = cursor.getInt(2);
                exhibicionAdicional.Isla = cursor.getInt(3);
                exhibicionAdicional.IslaFrente = cursor.getInt(4);
                exhibicionAdicional.Exhibidor = cursor.getInt(5);
                exhibicionAdicional.ExhibidorFrente = cursor.getInt(6);
                exhibicionAdicional.Bunker = cursor.getInt(7);
                exhibicionAdicional.BunkerFrente = cursor.getInt(8);
                exhibicionAdicional.Area = cursor.getInt(9);
                exhibicionAdicional.Tira = cursor.getInt(10);
                exhibicionAdicional.TiraFrente = cursor.getInt(11);
                exhibicionAdicional.Caja = cursor.getInt(12);
                exhibicionAdicional.CajaFrente = cursor.getInt(13);
                exhibicionAdicional.Arete = cursor.getInt(14);
                exhibicionAdicional.AreteFrente = cursor.getInt(15);
                exhibicionAdicional.Forway = cursor.getInt(16);
                exhibicionAdicional.ForwayFrente = cursor.getInt(17);
                exhibicionAdicional.Otros = cursor.getInt(18);
                exhibicionAdicional.Comentario = cursor.getString(19);
                exhibicionAdicional.Id = cursor.getInt(20);
                exhibicionAdicional.OtrosFrente = cursor.getInt(21);
                exhibicionAdicional.IdFoto = cursor.getInt(22);
                } 
          while (cursor.moveToNext());
        }
        
        return exhibicionAdicional;
    }
                  

        
      /**********************************************************************************************************/
                      public static mx.smartteam.entities.ExhibicionAdicional
                  GetInfoByVisita2(Context context, 
                                  mx.smartteam.entities.Proyecto proyecto , 
                                  mx.smartteam.entities.Usuario usuario,
                                  mx.smartteam.entities.Pop pop, 
                                  String producto)
    {
        mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional= new mx.smartteam.entities.ExhibicionAdicional();
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                " SELECT sku,                               " + 
                "        Cabecera,                          " + 
                "        CabeceraFrente ,                   " +
                "        Isla,                              " +
                "        IslaFrente,                        " +
                "        exhibidor,                         " +
                "        exhibidorFrente,                   " +
                "        bunker,                            " +
                "        bunkerFrente,                      " +
                "        Area,                              " +
                "        Tira,                              " +
                "        TiraFrente,                        " +
                "        Caja,                              " +
                "        CajaFrente,                        " +
                "        arete,                             " +
                "        AreteFrente,                       " +
                "        Forway,                            " +
                "        ForwayFrente,                      " +
                "        Otros,                             " +
                "        Comentario,             " + 
                "        id  , OtrosFrente                               " + 
                "   From ExhibicionAdicional                " +
                "  Where Idproyecto      = ? AND            " + 
                "        Idusuario       = ? AND            " + 
                "        Sku             = (select p.sku from producto p,  ExhibicionAdicional a where p.sku=a.sku and p.nombre = ? ) AND            " + 
                "        IdVisita        = ? AND            " + 
                "        Determinantegsp = ?     " ,
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
                exhibicionAdicional.Sku                 =cursor.getLong(0);
                exhibicionAdicional.Cabecera            =cursor.getInt(1);
                exhibicionAdicional.CabeceraFrente      =cursor.getInt(2);
                exhibicionAdicional.Isla                =cursor.getInt(3);
                exhibicionAdicional.IslaFrente          =cursor.getInt(4);
                exhibicionAdicional.Exhibidor           =cursor.getInt(5);
                exhibicionAdicional.ExhibidorFrente     =cursor.getInt(6);
                exhibicionAdicional.Bunker              =cursor.getInt(7);
                exhibicionAdicional.BunkerFrente        =cursor.getInt(8);
                exhibicionAdicional.Area                =cursor.getInt(9);
                exhibicionAdicional.Tira                =cursor.getInt(10);
                exhibicionAdicional.TiraFrente          =cursor.getInt(11);
                exhibicionAdicional.Caja                =cursor.getInt(12);
                exhibicionAdicional.CajaFrente          =cursor.getInt(13);
                exhibicionAdicional.Arete               =cursor.getInt(14);
                exhibicionAdicional.AreteFrente         =cursor.getInt(15);
                exhibicionAdicional.Forway              =cursor.getInt(16);
                exhibicionAdicional.ForwayFrente        =cursor.getInt(17);
                exhibicionAdicional.Otros               =cursor.getInt(18);
                exhibicionAdicional.Comentario          =cursor.getString(19);
                exhibicionAdicional.Id                  =cursor.getInt(20);
                    exhibicionAdicional.OtrosFrente                 =cursor.getInt(21);
                } 
          while (cursor.moveToNext());
        }
        
        return exhibicionAdicional;
    }
                  
   public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) {
       
       db = (new BaseDatos(context)).getWritableDatabase();
       int existe = 0;
       
       String query = "SELECT COUNT(1) FROM ExhibicionAdicional WHERE IdVisita = " + pop.getIdVisita() + ";";
       Cursor cursor = db.rawQuery(query, null);
       
       if(cursor.moveToFirst()) {
           existe = cursor.getInt(0);
       }
       
       return existe;
   }
      
   public static mx.smartteam.entities.ExhibicionAdicional ContestacionExhibicion
        (Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto)
    {
        mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional = null;
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db
                .rawQuery(
                " SELECT sku, idFoto" + 
                "   From ExhibicionAdicional                " +
                "  Where IdVisita = ? AND IdFoto = ? "  ,
                new String[]{
                             visita.Id.toString(),
                             foto.Id.toString(),
                            }
                          );
        
        if (cursor.moveToFirst()) 
        {
            exhibicionAdicional = new mx.smartteam.entities.ExhibicionAdicional();
            // Recorremos el cursor hasta que no haya más registros
            do {
                exhibicionAdicional.Sku = cursor.getLong(0);
                exhibicionAdicional.IdFoto = cursor.getInt(1);
               
                } 
          while (cursor.moveToNext());
        }
        
        return exhibicionAdicional;
    }
        
      /**********************************************************************************************************/
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) throws Exception {

            ServiceURI service = new ServiceURI();

            METHOD_NAME = "/ExhibidoresAdicionalesInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object()
                    .key("DeterminanteGSP").value(visita.DeterminanteGSP.toString())
                    .key("SKU").value(exhibicionAdicional.Sku)
                    .key("CadenaFecha").value(exhibicionAdicional.FechaCrea)
                    .key("IsCabecera").value(exhibicionAdicional.Cabecera)
                    .key("CabeceraFrentes").value(exhibicionAdicional.CabeceraFrente)
                    .key("IsIsla").value(exhibicionAdicional.Isla)
                    .key("IslasFrentes").value(exhibicionAdicional.IslaFrente)
                    .key("IsExhibidores").value(exhibicionAdicional.Exhibidor)
                    .key("IsBunkers").value(exhibicionAdicional.Bunker)
                    .key("IsAreaFlex").value(exhibicionAdicional.Area)
                    .key("AreaFlexFrentes").value(exhibicionAdicional.AreaFrente)
                    .key("IsTiras").value(exhibicionAdicional.Tira)
                    .key("TirasFrentes").value(exhibicionAdicional.TiraFrente)
                    .key("IsCajas").value(exhibicionAdicional.Caja)
                    .key("CajasFrentes").value(exhibicionAdicional.CabeceraFrente)
                    .key("IsAriete").value(exhibicionAdicional.Arete)
                    .key("ArietesFrentes").value(exhibicionAdicional.AreteFrente)
                    .key("IsForway").value(exhibicionAdicional.Forway)
                    .key("ForwayFrentes").value(exhibicionAdicional.ForwayFrente)
                    .key("IsOtro").value(exhibicionAdicional.Otros)
                    .key("OtrosFrentes").value(0)
                    .key("ComentarioExhAdic").value(exhibicionAdicional.Comentario == null ? "S/c" : exhibicionAdicional.Comentario)
                    //.key("ComentarioExhAdic").value("más comentarios")
                    .endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(),"UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("ExhibidoresAdicionalesInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [ExhibidoresAdicionalesInsert]");
            }

        }
    }
    
/**********************************************************************************************************/
}
