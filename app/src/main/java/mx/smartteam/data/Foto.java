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
import java.util.Date;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Foto.Type;
import mx.smartteam.entities.FotoCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


public class Foto {

    private static String METHOD_NAME = "/GetOrdenFormularios";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static String strResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static Integer Insert(Context context, mx.smartteam.entities.Foto foto) {
        Long Id;
        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("IdVisita", foto.IdVisita);
        values.put("Foto", foto.Foto);
        values.put("Tipo", foto.Tipo.toString());        
        values.put("Comentario", foto.Comentario);
        values.put("StatusSync", 0);
        if(foto.IdCategoria > 0 && foto.IdCategoria != null){values.put("IdCategoria", foto.IdCategoria);}
        if(foto.IdSondeo != null && foto.IdSondeo > 0){ values.put("IdSondeo",  foto.IdSondeo);}
        if(foto.nOpcion > 0 && foto.nOpcion!= null){ values.put("nOpcion", foto.nOpcion); }
        if(foto.Categoriaid > 0 && foto.Categoriaid!= null ){values.put("Categoria", foto.Categoriaid);}
        if(foto.Marcaid > 0 && foto.Marcaid != null ){values.put("IdMarca", foto.Marcaid);}
        if(foto.SKU > 0 && foto.SKU != null){ values.put("SKU", Long.toString(foto.SKU));}
        if(foto.idExhibicionConfig > 0 && foto.idExhibicionConfig != null){values.put("IdExhibicionConfig", foto.idExhibicionConfig);  }

        Id = db.insert("Foto", null, values);
        
        db.close();
        return Id.intValue();
    }

    public static class Opcion {
        public static Integer Insert(Context context, Integer IdFoto, mx.smartteam.entities.Opcion opcion) {
            Long Id;
            db = (new BaseDatos(context)).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("IdFoto", IdFoto);
            values.put("IdOpcion", opcion.Id);
            values.put("StatusSync", 0);
            Id = db.insert("FotoOpcion", null, values);
            db.close();
            return Id.intValue();
        }
    }

    public static int GetByFotoCount(Context context, mx.smartteam.entities.PopVisita visita) {
        int count = 0;
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(Id) from Foto WHERE StatusSync = 0 AND IdVisita= ? ", new String[]{visita.Id.toString()});
        if (cursor.moveToFirst()) {
            do {
                mx.smartteam.entities.Foto foto = new mx.smartteam.entities.Foto();
                foto.contador = cursor.getInt(0);
                count = foto.contador;
            } while (cursor.moveToNext());
        }
        db.close();
        return count;
    }
    
    public static int existe_foto(Context context, mx.smartteam.entities.Pop pop){
        db = (new BaseDatos(context)).getWritableDatabase();
        int existe = 0;
        String query = "SELECT COUNT(1) from Foto WHERE Tipo != 'Entrada' || Tipo != 'Salida' AND IdVisita = " + pop.getIdVisita() + ";";
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0);
        }
        
        return existe;
    }
    
     public static int GetByFotoCount2(Context context, mx.smartteam.entities.PopVisita visita,mx.smartteam.entities.Sondeo sondeo ) {
        int count = 0;
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(Id) from Foto WHERE StatusSync = 0 AND IdVisita= ? AND IdSondeo = ?",
                new String[]{
                    visita.Id.toString(),
                        sondeo.Id.toString()
                });
        if (cursor.moveToFirst()) {
            do {
                mx.smartteam.entities.Foto foto = new mx.smartteam.entities.Foto();
                foto.contador = cursor.getInt(0);
                count = foto.contador;
            } while (cursor.moveToNext());
        }
        db.close();
        return count;
    }
     
    public static int BuscarFotoEntrada(Context context, mx.smartteam.entities.Pop pop){
        int fotos = 0;
        db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT COUNT(Id) FROM Foto WHERE IdVisita = ? AND Tipo = 'Entrada'", new String[]{pop.IdVisita.toString()});
        
        if(cursor.moveToFirst()){
            do{
                fotos = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        
    return fotos;
    }
     public static int BuscarFotoSalida(Context context, mx.smartteam.entities.Pop pop){
        int fotos = 0;
        db = (new BaseDatos(context)).getWritableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT COUNT(Id) FROM Foto WHERE IdVisita = ? AND Tipo = 'Salida'", new String[]{pop.IdVisita.toString()});
        
        if(cursor.moveToFirst()){
            do{
                fotos = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        
    return fotos;
    }
    
    public static mx.smartteam.entities.FotoCollection GetByVisita(Context context, mx.smartteam.entities.PopVisita visita) {

        mx.smartteam.entities.FotoCollection fotoCollection = new FotoCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT Id,IdVisita,Foto,Tipo,Comentario,datetime(fechacrea, 'unixepoch', 'localtime'),StatusSync,FechaSync,IdCategoria,Categoria,IdMarca,SKU "
                + " FROM Foto  "
                + " WHERE StatusSync=0 AND IdVisita=? limit 1",
                new String[]{visita.Id.toString()});

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Foto foto = new mx.smartteam.entities.Foto();
                foto.Id = cursor.getInt(0);
                foto.IdVisita = cursor.getInt(1);
                foto.Foto = cursor.getString(2);

                if (cursor.getString(3).equals("Entrada")) {
                    foto.Tipo = Type.Entrada;
                } else if (cursor.getString(3).equals("Salida")) {
                    foto.Tipo = Type.Salida;
                } else if (cursor.getString(3).equals("Foto")) {
                    foto.Tipo = Type.Foto;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                    /*Cursor cursoroOpciones = db.rawQuery(
                     "SELECT Id,IdFoto,IdOpcion,StatusSync,FechaSync, datetime(fechacrea, 'unixepoch', 'localtime') "
                     + " FROM FotoOpcion  "
                     + " WHERE StatusSync=0 AND IdFoto=?",
                     new String[]{foto.Id.toString()});*/

                    /*if (cursoroOpciones.moveToFirst()) {
                     do {
                     mx.smartteam.entities.Opcion opcion=new mx.smartteam.entities.Opcion();
                     opcion.Id=cursoroOpciones.getInt(2);
                     opcion.FechaCrea=cursoroOpciones.getString(5);
                     foto.Opcion.add(opcion);
                            
                            
                     } while (cursoroOpciones.moveToNext());
                     }*/
                } else if (cursor.getString(3).equals("Sos")) {
                    foto.Tipo = Type.Sos;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } else if (cursor.getString(3).equals("Anaquel")) {
                    foto.Tipo = Type.Anaquel;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } else if (cursor.getString(3).equals("Bodega")) {
                    foto.Tipo = Type.Bodega;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } else if (cursor.getString(3).equals("Sondeo")) {
                    foto.Tipo = Type.Sondeo;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } else if (cursor.getString(3).equals("Exhibicion")) {
                    foto.Tipo = Type.Exhibicion;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } else if (cursor.getString(3).equals("Precio")) {
                    foto.Tipo = Type.Precio;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                }
                else if (cursor.getString(3).equals("Precio")) {
                    foto.Tipo = Type.Precio;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                }
                else if (cursor.getString(3).equals("MaterialPromocional")){
                    foto.Tipo = Type.MaterialPromocional;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                }
                 else if (cursor.getString(3).equals("foto_anaquel")){
                    foto.Tipo = Type.foto_anaquel;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                    foto.Categoriaid = cursor.getInt(9);
                    foto.Marcaid = cursor.getInt(10);
                }
                 else if (cursor.getString(3).equals("foto_adicional")){
                    foto.Tipo = Type.foto_adicional;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                    foto.Categoriaid = cursor.getInt(9);
                    foto.Marcaid = cursor.getInt(10);
                }
                else if (cursor.getString(3).equals("foto_producto")){
                    foto.Tipo = Type.foto_producto;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                    foto.Categoriaid = cursor.getInt(9);
                    foto.Marcaid = cursor.getInt(10);
                    foto.SKU = cursor.getLong(11);
                }

                foto.Comentario = cursor.getString(4);
                foto.FechaCrea = cursor.getString(5);
                foto.StatusSync = cursor.getInt(6);
                foto.FechaSync = cursor.getString(7);
                foto.IdCategoria = cursor.getInt(8);
               

                fotoCollection.add(foto);

            } while (cursor.moveToNext());

        }
        db.close();
        return fotoCollection;

    }
    
      public static mx.smartteam.entities.FotoCollection FotosVisitas(Context context, mx.smartteam.entities.PopVisita visita) {

        mx.smartteam.entities.FotoCollection fotoCollection = new FotoCollection();
        db = (new BaseDatos(context)).getWritableDatabase();

        String query = "SELECT Id, IdVisita, Foto, Tipo, IdCategoria, Comentario, datetime(fechacrea, 'unixepoch', 'localtime') , "
                            + "StatusSync , FechaSync , Categoria, IdMarca, SKU, IdSondeo,nopcion , IdExhibicionConfig"
                            + " FROM Foto "
                            + " WHERE StatusSync = 0 AND IdVisita = " + visita.Id.toString() + " limit 1;";
        
        Cursor cursor = db.rawQuery(query, null);

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Foto foto = new mx.smartteam.entities.Foto();
                foto.Id = cursor.getInt(0);
                foto.IdVisita = cursor.getInt(1);
                foto.Foto = cursor.getString(2);
                
                if (cursor.getString(3).equals("Entrada")) {
                    foto.Tipo = Type.Entrada;
                } else if (cursor.getString(3).equals("Salida")) {
                    foto.Tipo = Type.Salida;
                } else if (cursor.getString(3).equals("Foto")) {
                    foto.Tipo = Type.Foto;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } else if (cursor.getString(3).equals("Sos")) {
                    foto.Tipo = Type.Sos;
                } else if (cursor.getString(3).equals("Anaquel")) {
                    foto.Tipo = Type.Anaquel;
                } else if (cursor.getString(3).equals("Bodega")) {
                    foto.Tipo = Type.Bodega;
                } else if (cursor.getString(3).equals("Sondeo")) {
                    foto.Tipo = Type.Sondeo;
                } else if (cursor.getString(3).equals("Exhibicion")) {
                    foto.Tipo = Type.Exhibicion;
                } else if (cursor.getString(3).equals("Precio")) {
                    foto.Tipo = Type.Precio;
                } else if (cursor.getString(3).equals("MaterialPromocional")){
                    foto.Tipo = Type.MaterialPromocional;
                } else if (cursor.getString(3).equals("foto_anaquel")){
                    foto.Tipo = Type.foto_anaquel;
                } else if (cursor.getString(3).equals("foto_adicional")){
                    foto.Tipo = Type.foto_adicional;
                } else if (cursor.getString(3).equals("foto_producto")){
                    foto.Tipo = Type.foto_producto;
                }else if(cursor.getString(3).equals("Sod") ){
                    foto.Tipo = Type.Sod;
                }
                
                foto.IdCategoria = cursor.getInt(4);
                foto.Comentario = cursor.getString(5);
                foto.FechaCrea = cursor.getString(6);
                foto.StatusSync = cursor.getInt(7);
                foto.FechaSync = cursor.getString(8);
                foto.Categoriaid = cursor.getInt(9);
                foto.Marcaid = cursor.getInt(10);
                foto.SKU = cursor.getLong(11);
                foto.IdSondeo = cursor.getInt(12);
                foto.nOpcion = cursor.getInt(13);
                foto.idExhibicionConfig = cursor.getInt(14);
                
                fotoCollection.add(foto);

            } while (cursor.moveToNext());
        }
        db.close();
        return fotoCollection;

    }
    
    
    public static mx.smartteam.entities.FotoCollection GetByVisita2(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo) {

        mx.smartteam.entities.FotoCollection fotoCollection = new FotoCollection();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT Id,IdVisita,Foto,Tipo,Comentario,datetime(fechacrea, 'unixepoch', 'localtime'),StatusSync,FechaSync,IdCategoria ,IdSondeo,nOpcion"
                + " FROM Foto  "
                + " WHERE StatusSync=0 AND IdVisita=? AND IdSondeo = ? limit 1",
                new String[]{
                    visita.Id.toString(),
                        sondeo.Id.toString()
                });

        // Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Foto foto = new mx.smartteam.entities.Foto();
                foto.Id = cursor.getInt(0);
                foto.IdVisita = cursor.getInt(1);
                foto.Foto = cursor.getString(2);

                if (cursor.getString(3).equals("Entrada")) {
                    foto.Tipo = Type.Entrada;
                } else if (cursor.getString(3).equals("Salida")) {
                    foto.Tipo = Type.Salida;
                } else if (cursor.getString(3).equals("Foto")) {
                    foto.Tipo = Type.Foto;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                    /*Cursor cursoroOpciones = db.rawQuery(
                     "SELECT Id,IdFoto,IdOpcion,StatusSync,FechaSync, datetime(fechacrea, 'unixepoch', 'localtime') "
                     + " FROM FotoOpcion  "
                     + " WHERE StatusSync=0 AND IdFoto=?",
                     new String[]{foto.Id.toString()});*/

                    /*if (cursoroOpciones.moveToFirst()) {
                     do {
                     mx.smartteam.entities.Opcion opcion=new mx.smartteam.entities.Opcion();
                     opcion.Id=cursoroOpciones.getInt(2);
                     opcion.FechaCrea=cursoroOpciones.getString(5);
                     foto.Opcion.add(opcion);
                            
                            
                     } while (cursoroOpciones.moveToNext());
                     }*/
                } else if (cursor.getString(3).equals("Sondeo")) {
                    foto.Tipo = Type.Sondeo;
                    foto.Opcion = OpcionesFotos(context, foto.Id.toString());
                } 

                foto.Comentario = cursor.getString(4);
                foto.FechaCrea = cursor.getString(5);
                foto.StatusSync = cursor.getInt(6);
                foto.FechaSync = cursor.getString(7);
                foto.IdCategoria = cursor.getInt(8);
                foto.IdSondeo = cursor.getInt(9);
                foto.nOpcion = cursor.getInt(10);
                fotoCollection.add(foto);

            } while (cursor.moveToNext());

        }
        db.close();
        return fotoCollection;

    }
    

    public static mx.smartteam.entities.OpcionCollection OpcionesFotos(Context context, String Id) {
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursoroOpciones = db.rawQuery(
                "SELECT Id,IdFoto,IdOpcion,StatusSync,FechaSync, datetime(fechacrea, 'unixepoch', 'localtime') "
                + " FROM FotoOpcion  "
                + " WHERE StatusSync=0 AND IdFoto=?",
                new String[]{Id});
        mx.smartteam.entities.OpcionCollection opcionCollection = new mx.smartteam.entities.OpcionCollection();
        if (cursoroOpciones.moveToFirst()) {
            do {
                mx.smartteam.entities.Opcion opcion = new mx.smartteam.entities.Opcion();
                opcion.Id = cursoroOpciones.getInt(2);
                opcion.FechaCrea = cursoroOpciones.getString(5);
                //foto.Opcion.add(opcion);
                opcionCollection.add(opcion);
            } while (cursoroOpciones.moveToNext());
        }
        return opcionCollection;
    }

    public static void UpdateStatusSync(Context context, mx.smartteam.entities.Foto foto) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StatusSync", foto.StatusSync);
        values.put("FechaSync", System.currentTimeMillis() / 1000);
        db.update("Foto", values, "Id=" + foto.Id.toString(), null);

        db.close();
    }

    public static class Upload {

        public static String Foto(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            METHOD_NAME = "/FotoInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject()
                    .key("Foto").object().key("FotoBase64").value(foto.Foto.toString())
                    .key("CadenaFecha").value(foto.FechaCrea.toString())
                    .key("IdCategoria").value(foto.IdCategoria)
                    .key("Comentarios").value(foto.Comentario)
                    .endObject();

            jsonString.key("Opciones").array();
            for (mx.smartteam.entities.Opcion opcion : foto.Opcion) {
                jsonString.object()
                        .key("Id").value(opcion.Id.toString())
                        .key("Nombre").value(opcion.Nombre)
                        .key("CadenaFecha").value(opcion.FechaCrea)
                        .endObject();
            }
            jsonString.endArray();

            jsonString.endObject();

            //Log.i("AsistenciaEntrada", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("FotoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [FotoInsert]");
            } else {
                result = "OK";
            }
            
            return result;
        }

        public static String FotoInsertSkuSondeo(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto,
                Integer IdSondeo, Long SKU, Integer idcategoria, Integer idmarca
        ) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            METHOD_NAME = "/FotoInsertSkuSondeo";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject()
                    .key("Foto").object().key("FotoBase64").value(foto.Foto.toString())
                    .key("CadenaFecha").value(foto.FechaCrea.toString())
                    .key("IdCategoria").value(foto.IdCategoria)
                    .key("Comentarios").value(foto.Comentario)
                    .key("Tipo").value(foto.Tipo).endObject();

            jsonString.key("Opciones").array();
            for (mx.smartteam.entities.Opcion opcion : foto.Opcion) {
                jsonString.object()
                        .key("Id").value(opcion.Id.toString())
                        .key("Nombre").value(opcion.Nombre)
                        .key("CadenaFecha").value(opcion.FechaCrea)
                        .endObject();
            }
            jsonString.endArray();
            jsonString.key("tipo").value(foto.Tipo);
            jsonString.key("sku").value(SKU.toString());
            jsonString.key("idsondeo").value(IdSondeo.toString());
            jsonString.key("idcategoria").value(idcategoria.toString());
            jsonString.key("idmarca").value(idmarca.toString());

            jsonString.endObject();

            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("FotoInsertSkuSondeoResult").equals("OK")) {
                throw new Exception("Error el el servicio [FotoInsert]");
            }else{
                result = "OK";
            }
            return result;
        }

        public static String FotoEntrada(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            try{
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
                            .key("FotoBase64").value(foto.Foto.toString())
                            .key("CadenaFecha").value(foto.FechaCrea.toString())
                        .endObject()
                    .endObject();

            //Log.i("AsistenciaEntrada", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("AsistenciaEntradaFotoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [AsistenciaEntradaFotoInsert]");
            } else { result = "OK"; }
            }catch(Exception ex){
                ex.getMessage();
            }
            return result;

        }
        public static String FotoAnaquel(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            METHOD_NAME = "/FotoAnaquelInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject()
                    .key("Foto").object().key("FotoBase64").value(foto.Foto.toString())
                    .key("CadenaFecha").value(foto.FechaCrea.toString())
                    .key("Categoria").value(foto.Categoriaid.toString())
                    .key("IdMarca").value(foto.Marcaid.toString())
                    .key("Comentarios").value(foto.Comentario.toString())
                    .key("Tipo").value(foto.Tipo.toString())
                    .key("IdCategoria").value(foto.IdCategoria.toString())
                    .endObject()
                    .endObject();

            //Log.i("AsistenciaEntrada", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("FotoAnaquelInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [FotoAnaquelInsert]");
            }else {result = "OK"; } 
            return result;
        }
           public static String FotoProducto(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            METHOD_NAME = "/FotoProductoInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject()
                    .key("Foto").object().key("FotoBase64").value(foto.Foto.toString())
                    .key("CadenaFecha").value(foto.FechaCrea.toString())
                    .key("Categoria").value(foto.Categoriaid.toString())
                    .key("IdMarca").value(foto.Marcaid.toString())
                    .key("Comentarios").value(foto.Comentario.toString())
                    .key("Tipo").value(foto.Tipo.toString())
                    .key("IdCategoria").value(foto.IdCategoria.toString())
                    .key("Sku").value(foto.SKU.toString())
                    .endObject()
                    .endObject();

            //Log.i("AsistenciaEntrada", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("FotoProductoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [FotoProductoInsert]");
            } else { result = "OK"; } 
            return result;
        }
          
        public static String FotoSalida(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            METHOD_NAME = "/AsistenciaSalidaFotoInsert";
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
                            .key("FotoBase64").value(foto.Foto.toString())
                            .key("CadenaFecha").value(foto.FechaCrea.toString())
                        .endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("AsistenciaSalidaFotoInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [AsistenciaSalidaFotoInsert]");
            }else{result = "OK";  }
            return result;
        }
        
        
         public static String FotoSod(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            ServiceURI service = new ServiceURI();
            String result = "";
            METHOD_NAME = "/FotoSodInsert";
            jsonString = new JSONStringer()
                    .object()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject()
                    .key("Foto").object().key("FotoBase64").value(foto.Foto.toString())
                    .key("CadenaFecha").value(foto.FechaCrea.toString())
                    .key("idExhibicionConfig").value(foto.idExhibicionConfig.toString())
                    .endObject()
                    .endObject();

            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            if (!jsonResult.getString("FotoSodInsertResult").equals("OK")) {
                throw new Exception("Error el el servicio [FotoSodInsert]");
            } else {result = "OK";}

            return result;
        }
        
    }
}
