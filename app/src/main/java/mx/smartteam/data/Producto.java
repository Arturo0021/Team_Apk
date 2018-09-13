/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Log;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.EnumFormulario;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


public class Producto {

    private static String METHOD_NAME = "/GetProductos";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
    private static mx.smartteam.entities.MovilConfig movilConfig;

    public static class Download {

        public static ProductoCollection GetProductos(
                mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {

            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.ProductoCollection productoCollection = new ProductoCollection();

            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject().endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult
                    .getJSONArray("GetProductosResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                producto.Id = jsonObject.getInt("Id");
                producto.IdCategoria = jsonObject.getInt("IdCategoria");
                producto.IdMarca = jsonObject.getInt("IdMarca");
                producto.IdProyecto = jsonObject.getInt("IdProyecto");
                producto.Nombre = jsonObject.getString("Nombre");
                producto.SKU = jsonObject.getLong("SKU");
                producto.Maximo = jsonObject.getDouble("SumaMaxima");
                producto.Minimo = jsonObject.getDouble("SumaMinima");
                producto.Precio = 0.0;
                producto.FechaSync = jsonObject.getLong("FechaSync");
                producto.StatusSync = jsonObject.getInt("Statussync");
                productoCollection.add(producto);

            }

            return productoCollection;

        }
        //GetProductosByidCadena

        public static ProductoCollection GetProductosAll(mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {
            METHOD_NAME = "/GetAllProductos";
            jsonString = null;
            strEntity = null;
            jsonResult = null;
            jsonArray = null;
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.ProductoCollection productoCollection = new ProductoCollection();
            //productoCollection = null;
            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                        .key("Id").value(proyecto.Id.toString())
                        .key("Ufechadescarga").value(time.toString()).endObject().                  
                    endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            try {

                jsonArray = jsonResult
                        .getJSONArray("GetAllProductosResult");

            } catch (Exception ex) {
                ex.getMessage().toString();
            }

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                producto.Id = jsonObject.getInt("Id");
                producto.IdCategoria = jsonObject.getInt("IdCategoria");
                producto.IdMarca = jsonObject.getInt("IdMarca");
                producto.IdProyecto = jsonObject.getInt("IdProyecto");
                producto.Nombre = jsonObject.getString("Nombre");
                producto.SKU = jsonObject.getLong("SKU");
                producto.Maximo = jsonObject.getDouble("SumaMaxima");
                producto.Minimo = jsonObject.getDouble("SumaMinima");
                producto.Precio = 0.0;
                //producto.FechaSync = jsonObject.getLong("FechaSync");
                producto.StatusSync = jsonObject.getInt("Statussync");
                producto.Barcode = jsonObject.getString("Barcode");
                producto.activo = jsonObject.getInt("Activo");
                producto.orden = jsonObject.getInt("Orden");
                productoCollection.add(producto);
            }
            return productoCollection;
        }
    }// END DOWNLOAD
    
    public static class Upload {
    }

    public static void Insert(Context context, mx.smartteam.entities.Producto producto) throws Exception {
        db = (new BaseDatos(context)).getWritableDatabase();

        switch (producto.StatusSync) {
            case 1:
                db.execSQL("INSERT INTO Producto"
                        + " (Id,IdCategoria,IdMarca,SKU,Nombre,Maximo,Minimo,IdProyecto,FechaSync,Statussync )" + " SELECT "
                        + producto.Id + ","
                        + producto.IdCategoria + ","
                        + producto.IdMarca + ","
                        + producto.SKU + ",'"
                        + producto.Nombre + "',"
                        + producto.Maximo + ","
                        + producto.Minimo + ","
                        + producto.IdProyecto + ","
                        + producto.FechaSync + ","
                        + producto.StatusSync 
                        + " WHERE NOT EXISTS(SELECT 1 FROM Producto WHERE SKU="
                        + producto.SKU + " )");

                break;

            case 2:
                db.execSQL("INSERT INTO Producto"
                        + " (Id,IdCategoria,IdMarca,SKU,Nombre,Maximo,Minimo,IdProyecto,FechaSync,Statussync )" + " SELECT "
                        + producto.Id + ","
                        + producto.IdCategoria + ","
                        + producto.IdMarca + ","
                        + producto.SKU + ",'"
                        + producto.Nombre + "',"
                        + producto.Maximo + ","
                        + producto.Minimo + ","
                        + producto.IdProyecto + ","
                        + producto.FechaSync + ","
                        + producto.StatusSync 
                        + " WHERE NOT EXISTS(SELECT 1 FROM Producto WHERE SKU="
                        + producto.SKU + " )");

                ContentValues values = new ContentValues();
                values.put("Idcategoria", producto.IdCategoria.toString());
                values.put("IdProyecto", producto.IdMarca.toString());
                values.put("SKU", producto.SKU.toString());
                values.put("Nombre", producto.Nombre.toString());
                values.put("Maximo", producto.Maximo.toString());
                values.put("Minimo", producto.Minimo.toString());
                values.put("IdProyecto", producto.IdProyecto.toString());
                values.put("FechaSync", producto.FechaSync.toString());
                values.put("StatusSync", producto.StatusSync);

                db.update("Producto", values, "SKU=" + producto.SKU.toString(), null);

                break;

        }

        db.close();
    }
    
  
     public static void Insertp(Context context, mx.smartteam.entities.Producto producto) throws Exception {
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
                        switch (producto.StatusSync) {
                            case 1:
                                db.execSQL("INSERT INTO Producto"
                                        + " (Id,IdCategoria,IdMarca,SKU,Nombre,Maximo,Minimo,IdProyecto,Statussync,barcode,activo,orden )" + " SELECT "
                                        + producto.Id + ","
                                        + producto.IdCategoria + ","
                                        + producto.IdMarca + ","
                                        + producto.SKU + ",'"
                                        + producto.Nombre + "',"
                                        + producto.Maximo + ","
                                        + producto.Minimo + ","
                                        + producto.IdProyecto + ","                            
                                        + producto.StatusSync + ", "
                                        + producto.Barcode+ ", "
                                        + producto.activo+ ", "
                                        + producto.orden+ " "
                                        + " WHERE NOT EXISTS(SELECT 1 FROM Producto WHERE ID="
                                        + producto.Id + " )");
            /*
                                db.execSQL("INSERT INTO ProductoCadena"
                                        + " (IdProyecto,IdCadena,Sku,Fechasync,Statussync,activo  )" + " SELECT "
                                        + producto.IdProyecto + ","
                                        + productoCadena.IdCadena + ","
                                        + producto.SKU + ","
                                        + productoCadena.FechaSync + ","
                                        + productoCadena.StatusSync + ","
                                        + answer
                                        + " WHERE NOT EXISTS(SELECT 1 FROM ProductoCadena WHERE IdProyecto=" + producto.IdProyecto + " and IdCadena=" + productoCadena.IdCadena + " and Sku=" + producto.SKU + " )");*/

                                break;

                            case 2:
                                db.execSQL("INSERT INTO Producto"
                                        + " (Id,IdCategoria,IdMarca,SKU,Nombre,Maximo,Minimo,IdProyecto ,barcode,activo,orden )" + " SELECT "
                                        + producto.Id + ","
                                        + producto.IdCategoria + ","
                                        + producto.IdMarca + ","
                                        + producto.SKU + ",'"
                                        + producto.Nombre + "',"
                                        + producto.Maximo + ","
                                        + producto.Minimo + ","
                                        + producto.IdProyecto + ","                            
                                       // + producto.StatusSync + ", "
                                         + producto.Barcode+ ", "
                                        + producto.activo+ ", "
                                        + producto.orden+ " "
                                        + " WHERE NOT EXISTS(SELECT 1 FROM Producto WHERE ID="
                                        + producto.Id + " )");

                                ContentValues values = new ContentValues();
                                values.put("Idcategoria", producto.IdCategoria.toString());
                                values.put("IdMarca", producto.IdMarca.toString());
                                values.put("SKU", producto.SKU.toString());
                                values.put("Nombre", producto.Nombre.toString());
                                values.put("Maximo", producto.Maximo.toString());
                                values.put("Minimo", producto.Minimo.toString());
                                values.put("IdProyecto", producto.IdProyecto.toString());
                                values.put("FechaSync", producto.FechaSync.toString());
                                //values.put("StatusSync", producto.StatusSync);
                                values.put("Barcode", producto.Barcode);                    
                                values.put("activo", producto.activo);
                                values.put("orden", producto.orden);

                                db.update("Producto", values, "Id=" + producto.Id.toString(), null);

                               /* db.execSQL("INSERT INTO ProductoCadena"
                                        + " (IdProyecto,IdCadena,Sku,Fechasync,Statussync,activo  )" + " SELECT "
                                        + producto.IdProyecto + ","
                                        + productoCadena.IdCadena + ","
                                        + producto.SKU + ","
                                        + productoCadena.FechaSync + ","
                                        + productoCadena.StatusSync + ","
                                        +answer
                                        + " WHERE NOT EXISTS(SELECT 1 FROM ProductoCadena WHERE IdProyecto=" + producto.IdProyecto + " and IdCadena=" + productoCadena.IdCadena + " and Sku=" + producto.SKU + " )");*/

                                break;
                      
                            }
                
            db.close();
        } catch (Exception ex) {
            ex.getMessage().toString();

        }
    }

    public static ProductoCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Usuario usuario) {
//

        ProductoCollection productoCollection = new ProductoCollection();
        try {
            if (proyecto != null && pop != null) {
                db = (new BaseDatos(context)).getWritableDatabase();

                Cursor cursor2 = db.rawQuery("SELECT configGps,Tipo "
                        + "FROM configGPS WHERE IdUsuario = ? limit 1 ",
                        new String[]{
                            usuario.Id.toString()
                        }
                );

                if (cursor2.moveToFirst()) {
                    do {
                        movilConfig = new mx.smartteam.entities.MovilConfig();

                        movilConfig.gpsconfig = cursor2.getInt(0);
                        movilConfig.Tipo = cursor2.getString(1);

                    } while (cursor2.moveToNext());

                }

                /*Cursor cursor = db.rawQuery("SELECT P.Id,P.IdCategoria,P.IdMarca,P.SKU,P.Nombre,P.Maximo,P.Minimo,P.IdProyecto FROM  ProductoCadena PC,Producto P "
                        + " WHERE PC.sku=P.sku "
                        + "AND PC.IdProyecto=? AND PC.IdCadena=?", new String[]{proyecto.Id.toString(), pop.IdCadena.toString()});*/
                
                 Cursor cursor = db.rawQuery(" select P.Id,P.IdCategoria,P.IdMarca,P.SKU,P.Nombre,P.Maximo,P.Minimo,P.IdProyecto from ProductoCadena pc "
                         + "inner join Producto p on p.sku=pc.sku and p.activo=1"
                         + " where pc.idproyecto=? and pc.idcadena=? and pc.activo=1", new String[]{proyecto.Id.toString(), pop.IdCadena.toString()});

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                        producto.Id = cursor.getInt(0);
                        producto.IdCategoria = cursor.getInt(1);
                        producto.IdMarca = cursor.getInt(2);
                        producto.SKU = cursor.getLong(3);
                        producto.Nombre = cursor.getString(4);
                        producto.Maximo = cursor.getDouble(5);
                        producto.Minimo = cursor.getDouble(6);
                        producto.IdProyecto = cursor.getInt(7);
                        producto.StatusTipo = movilConfig.Tipo;

                        productoCollection.add(producto);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return productoCollection;
    }

    public static String ScriptAnaquel(int idVisita, int idCadena, String tipo) {

        String query = "SELECT p.Id, p.Idcategoria,p.idmarca, p.SKU,p.Nombre,p.Descripcion,"
                + "p.Barcode,p.Maximo,p.Minimo,p.Precio,p.Idproyecto,p.StatusSync,p.FechaSync, ifnull( a.sku,0) as prod,pc.psugerido,pc.ppromocion,p.orden "
                + "from productocadena pc "
                + "inner join producto p on p.sku=pc.sku and p.activo=1 "
                + "left join anaquel a on a.sku=p.sku and a.idvisita= " + idVisita + " and a.tipo='" + tipo + "' "             
                 + " where pc.activo=1 and pc.idcadena=" + idCadena;       
        return query;
    }
    public static String ScriptFoto_Producto(int idVisita, int idCadena, String tipo) {
        String query = "SELECT p.Id, p.Idcategoria,p.idmarca, p.SKU,p.Nombre,p.Descripcion,"
                + "p.Barcode,p.Maximo,p.Minimo,p.Precio,p.Idproyecto,p.StatusSync,p.FechaSync, ifnull( f.sku,0) as prod,pc.psugerido,pc.ppromocion,p.orden "
                + "from productocadena pc "
                + "inner join producto p on p.sku=pc.sku and p.activo=1 "                
                + "left join foto f on f.SKU=p.SKU and f.Categoria=p.IdCategoria and f.IdMarca=p.IdMarca and f.tipo='" + tipo + "' and f.idvisita= "+idVisita             
                + " where pc.activo=1 and pc.idcadena=" + idCadena
                +  "  ";       
        return query;
    }

    public static String ScriptSondeo(int idVisita, int idCadena, int idsondeo) {

        String query = "SELECT p.Id, p.Idcategoria,p.idmarca, p.SKU,p.Nombre,p.Descripcion,"
                + "p.Barcode,p.Maximo,p.Minimo,p.Precio,p.Idproyecto,p.StatusSync,p.FechaSync,ifnull( c.sku,0) as prod, pc.psugerido,pc.ppromocion,p.orden "
                + " from productocadena pc "
                + " inner join producto p on p.sku=pc.sku and p.activo=1 "
                + " left join contestacion c on c.sku=p.sku and  c.idvisita= " + idVisita + " and c.idsondeo=" + idsondeo
                //+ " left join contestacionpregunta cp on cp.idcontestacion=c.id and cp.sku=p.sku"
                + " where pc.activo=1 and pc.idcadena=" + idCadena;
        return query;
    }

    public static String Scriptexhibiciones_adicionales(int idVisita, int idCadena) {
        String query = "SELECT p.Id, p.Idcategoria,p.idmarca, p.SKU,p.Nombre,p.Descripcion,"
                + "p.Barcode,p.Maximo,p.Minimo,p.Precio,p.Idproyecto,p.StatusSync,p.FechaSync, ifnull( exh.sku,0) as prod , pc.psugerido,pc.ppromocion,p.orden "
                + " FROM productocadena pc "
                + " INNER JOIN producto p ON p.sku = pc.sku and p.activo=1 "
                + " LEFT JOIN exhibicionadicional exh ON exh.sku = p.sku AND exh.idVisita = " + idVisita
                + " WHERE pc.activo=1 and  pc.Idcadena = " + idCadena;

        return query;
    }

    public static String ScriptBodegas(int idVisita, int idCadena) {
        String query = "SELECT p.Id, p.Idcategoria,p.idmarca, p.SKU,p.Nombre,p.Descripcion,"
                + "p.Barcode,p.Maximo,p.Minimo,p.Precio,p.Idproyecto,p.StatusSync,p.FechaSync, ifnull( b.sku,0) as prod , pc.psugerido,pc.ppromocion,p.orden "
                + " FROM productocadena pc "
                + " INNER JOIN producto p ON p.sku = pc.sku and p.activo=1 "
                + " LEFT JOIN Bodega b ON b.sku = p.sku AND b.idVisita = " + idVisita
                + " WHERE  pc.activo=1 and pc.Idcadena = " + idCadena;

        return query;
    }

    public static String ScriptMaterialPromocional(int idVisita, int idCadena) {
        String query = "SELECT p.Id, p.Idcategoria,p.idmarca, p.SKU,p.Nombre,p.Descripcion,"
                + "p.Barcode,p.Maximo,p.Minimo,p.Precio,p.Idproyecto,p.StatusSync,p.FechaSync, ifnull(mp.sku,0) as prod , pc.psugerido,pc.ppromocion,p.orden "
                + " FROM productocadena pc "
                + " INNER JOIN producto p ON p.sku = pc.sku and p.activo=1 "
                + " LEFT JOIN materialpromocional mp ON mp.sku = p.sku AND mp.idVisita = " + idVisita
                + " WHERE  pc.activo=1 and pc.Idcadena = " + idCadena;

        return query;
    }

    ///Obtiene los productos de la caden marcadno que productos han sido registrados en la visita
    public static ProductoCollection GetByVisita(Context context, Integer idVisita, Integer idcadena, Integer idcategoria, Integer idmarca, EnumFormulario formulario, int idSondeo, Integer iduser) {

        ProductoCollection productoCollection = null,productoCollection2 = null;

        try {
            String query = "", where = "",group="";
            db = (new BaseDatos(context)).getWritableDatabase();

            /*---------------------------*/
            Cursor cursor3 = db.rawQuery("SELECT configGps,Tipo "
                    + "FROM configGPS WHERE IdUsuario = ? limit 1 ",
                    new String[]{
                        iduser.toString()
                    }
            );

            if (cursor3.moveToFirst()) {
                do {
                    movilConfig = new mx.smartteam.entities.MovilConfig();

                    movilConfig.gpsconfig = cursor3.getInt(0);
                    movilConfig.Tipo = cursor3.getString(1);

                } while (cursor3.moveToNext());

            }
            /**/

            switch (formulario) {
                case anaquel_frentes:
                    query = ScriptAnaquel(idVisita, idcadena, "ANAQUEL");
                    break;
                case anaquel_precios:
                    query = ScriptAnaquel(idVisita, idcadena, "PRECIO");
                    break;
                case exhibiciones_adicionales:
                    query = Scriptexhibiciones_adicionales(idVisita, idcadena);
                    break;
                case existencias_anaquel:
                    break;
                case existencias_bodega:
                    query = ScriptBodegas(idVisita, idcadena);
                    break;
                case sondeo:
                    query = ScriptSondeo(idVisita, idcadena, idSondeo);
                    break;
                case material_promocional:
                    query = ScriptMaterialPromocional(idVisita, idcadena);
                    break;
                case foto_producto:
                    query = ScriptFoto_Producto(idVisita, idcadena, "foto_producto");
                    break;
                default:
                    break;
            }

            //String where = "";
//            String query = "select p.*, a.sku as prod "
//                    + "from productocadena pc "
//                    + "inner join producto p on p.sku=pc.sku "
//                    + "left join anaquel a on a.sku=p.sku and a.idvisita= " + idVisita + " and a.tipo='" + tipo + "' "
//                    + " where pc.idcadena=" + idcadena;
            if (!idcategoria.equals(0)) {
                where += " and p.idcategoria=" + idcategoria;
            }

            if (!idmarca.equals(0)) {
                where += " and p.idmarca=" + idmarca;
            }
            EnumFormulario formulario2 = null;
            if(formulario.foto_producto==formulario2.foto_producto){
                group += " group by pc.sku ";
            }
            query += where+group+" ORDER BY p.orden asc";
           // query += where;
            
            Cursor cursor = db.rawQuery(query, null);

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                productoCollection = new ProductoCollection();
                productoCollection2 = new ProductoCollection();
                do {
                    if(cursor.getInt(16)>0){
                    
                    
                        mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                        producto.Id = cursor.getInt(0);
                        producto.IdCategoria = cursor.getInt(1);
                        producto.IdMarca = cursor.getInt(2);
                        producto.SKU = cursor.getLong(3);
                        producto.Nombre = cursor.getString(4);
                        producto.Maximo = cursor.getDouble(7);
                        producto.Minimo = cursor.getDouble(8);
                        producto.IdProyecto = cursor.getInt(10);
                        producto.StatusTipo = movilConfig.Tipo;
                        producto.psugerido=cursor.getDouble(14);
                        producto.ppromocion=cursor.getDouble(15);

                        if (cursor.getLong(13) != 0) {
                            producto.bandera = 1;
                        }
                        productoCollection.add(producto);
                    }else
                    {
                        mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                        producto.Id = cursor.getInt(0);
                        producto.IdCategoria = cursor.getInt(1);
                        producto.IdMarca = cursor.getInt(2);
                        producto.SKU = cursor.getLong(3);
                        producto.Nombre = cursor.getString(4);
                        producto.Maximo = cursor.getDouble(7);
                        producto.Minimo = cursor.getDouble(8);
                        producto.IdProyecto = cursor.getInt(10);
                        producto.StatusTipo = movilConfig.Tipo;
                        producto.psugerido=cursor.getDouble(14);
                        producto.ppromocion=cursor.getDouble(15);

                        if (cursor.getLong(13) != 0) {
                            producto.bandera = 1;
                        }
                        productoCollection2.add(producto);
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        
        productoCollection.addAll(productoCollection2);
        
        return productoCollection;
    }

    public static ProductoCollection GetByProyectoCategoriaMarca(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Categoria categoria, mx.smartteam.entities.Marca marca, mx.smartteam.entities.Usuario usuario) {

        ProductoCollection productoCollection = new ProductoCollection();
        try {

            if (proyecto != null && pop != null && categoria != null && marca != null) {
                db = (new BaseDatos(context)).getWritableDatabase();

                Cursor cursor2 = db.rawQuery("SELECT configGps,Tipo "
                        + "FROM configGPS WHERE IdUsuario = ? limit 1 ",
                        new String[]{
                            usuario.Id.toString()
                        }
                );

                if (cursor2.moveToFirst()) {
                    do {
                        movilConfig = new mx.smartteam.entities.MovilConfig();

                        movilConfig.gpsconfig = cursor2.getInt(0);
                        movilConfig.Tipo = cursor2.getString(1);

                    } while (cursor2.moveToNext());

                }

                Cursor cursor = db.rawQuery("SELECT P.Id,P.IdCategoria,P.IdMarca,P.SKU,P.Nombre,P.Maximo,P.Minimo,P.IdProyecto FROM  ProductoCadena PC,Producto P "
                        + " WHERE PC.sku=P.sku "
                        + "AND PC.IdProyecto=? AND PC.IdCadena=? AND P.idCategoria=? AND P.idMarca=?", new String[]{proyecto.Id.toString(), pop.IdCadena.toString(), categoria.Id.toString(), marca.Id.toString()});

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
// mx.smartteam.entities.Producto producto2 = new mx.smartteam.entities.Producto();
//                        producto2.Id = -1; producto2.Nombre = "Todo";
//                       productoCollection.add(producto2);

                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                        producto.Id = cursor.getInt(0);
                        producto.IdCategoria = cursor.getInt(1);
                        producto.IdMarca = cursor.getInt(2);
                        producto.SKU = cursor.getLong(3);
                        producto.Nombre = cursor.getString(4);
                        // producto.Nombre = cursor.getString(3);
                        //producto.SKU = cursor.getLong(4);

                        producto.Maximo = cursor.getDouble(5);
                        producto.Minimo = cursor.getDouble(6);
                        producto.IdProyecto = cursor.getInt(7);
                        producto.StatusTipo = movilConfig.Tipo;
                        productoCollection.add(producto);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return productoCollection;
    }
    
    public static mx.smartteam.entities.Producto GetOne(Context context, String barcode){
        db = (new BaseDatos(context)).getWritableDatabase();
       mx.smartteam.entities.Producto producto = null;
            try{
                Cursor cursor = db.rawQuery("SELECT Id,Idcategoria,IdMarca,Sku,Nombre, Barcode,Maximo,Minimo FROM Producto WHere Barcode =? limit 1", new String[]{barcode});
            // Nos aseguramos de que existe al menos un registro
                if(cursor.moveToFirst())
                { /* Recorremos el cursor hasta que no haya más registros */
                    do{
                    
                    producto = new mx.smartteam.entities.Producto();
                    producto.Id = cursor.getInt(0);
                    producto.IdCategoria = cursor.getInt(1);
                    producto.IdMarca = cursor.getInt(2);
                    producto.SKU = cursor.getLong(3);
                    producto.Nombre = cursor.getString(4);
                    producto.Barcode = cursor.getString(5);
                    producto.Maximo = cursor.getDouble(6);
                    producto.Minimo = cursor.getDouble(7);
                    
                    }while(cursor.moveToNext());
                    
                }
           } catch (Exception e) {
            Log.i("GetOne", e.getMessage());
        } finally {
            db.close();
        }

        return producto;
    }
    
    

    public static ProductoCollection GetByProyectoCategoriaMarca2(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Categoria categoria, mx.smartteam.entities.Marca marca, mx.smartteam.entities.Usuario usuario, int modulo) {

        ProductoCollection productoCollection = new ProductoCollection();
        try {

            if (proyecto != null && pop != null && categoria != null && marca != null) {
                db = (new BaseDatos(context)).getWritableDatabase();

                Cursor cursor2 = db.rawQuery("SELECT configGps,Tipo "
                        + "FROM configGPS WHERE IdUsuario = ? limit 1 ",
                        new String[]{
                            usuario.Id.toString()
                        }
                );

                if (cursor2.moveToFirst()) {
                    do {
                        movilConfig = new mx.smartteam.entities.MovilConfig();

                        movilConfig.gpsconfig = cursor2.getInt(0);
                        movilConfig.Tipo = cursor2.getString(1);

                    } while (cursor2.moveToNext());

                }

                Cursor cursor = db.rawQuery("SELECT P.Id,P.IdCategoria,P.IdMarca,P.SKU,P.Nombre,P.Maximo,P.Minimo,P.IdProyecto FROM  ProductoCadena PC,Producto P "
                        + " WHERE PC.sku=P.sku "
                        + "AND PC.IdProyecto=? AND PC.IdCadena=? AND P.idCategoria=? AND P.idMarca=?", new String[]{proyecto.Id.toString(), pop.IdCadena.toString(), categoria.Id.toString(), marca.Id.toString()});

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
// mx.smartteam.entities.Producto producto2 = new mx.smartteam.entities.Producto();
//                        producto2.Id = -1; producto2.Nombre = "Todo";
//                       productoCollection.add(producto2);

                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
                        producto.Id = cursor.getInt(0);
                        producto.IdCategoria = cursor.getInt(1);
                        producto.IdMarca = cursor.getInt(2);
                        producto.SKU = cursor.getLong(3);
                        producto.Nombre = cursor.getString(4);
                        // producto.Nombre = cursor.getString(3);
                        //producto.SKU = cursor.getLong(4);

                        producto.Maximo = cursor.getDouble(5);
                        producto.Minimo = cursor.getDouble(6);
                        producto.IdProyecto = cursor.getInt(7);
                        producto.StatusTipo = movilConfig.Tipo;
                        productoCollection.add(producto);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return productoCollection;
    }

}
