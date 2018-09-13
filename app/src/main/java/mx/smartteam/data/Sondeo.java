package mx.smartteam.data;

import android.content.ContentValues;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import mx.smartteam.entities.SondeoCollection;
import mx.smartteam.entities.SondeoModulosCollection;

public class Sondeo {

    private static String METHOD_NAME = "/GetSondeos";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

        public static mx.smartteam.entities.SondeoCollection GetByProyectoUsuario(mx.smartteam.entities.Proyecto proyecto,mx.smartteam.entities.Usuario usuario, String time) throws Exception {

            SondeoCollection sondeoCollection = new SondeoCollection();

            ServiceURI service = new ServiceURI();

            try {

                jsonString = new JSONStringer().object()
                        .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                        .key("Proyecto").object()
                        .key("Id").value(proyecto.Id.toString())
                        .key("Ufechadescarga").value(time.toString())
                        .endObject()
                        .endObject();

                strEntity = new StringEntity(jsonString.toString());

                jsonResult = service.HttpGet(METHOD_NAME, strEntity);

                jsonArray = jsonResult.getJSONArray("GetSondeosResult");

                for (int i = 0; i < jsonArray.length(); ++i) {
                    
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    
                    mx.smartteam.entities.Sondeo sondeo = new mx.smartteam.entities.Sondeo();
                    
                    sondeo.setActivo(jsonObject.getInt("Activo"));
                    sondeo.setId(jsonObject.getInt("IdSondeo"));
                    sondeo.setNombre(jsonObject.getString("Nombre"));
                    sondeo.setIdProyecto(jsonObject.getInt("IdProyecto"));
                    sondeo.setFechaSync(jsonObject.getLong("FechaSync"));
                    sondeo.setStatusSync(jsonObject.getInt("Statussync"));
                    sondeo.setIdentificadorVentas(jsonObject.getInt("IdentificadorVentas"));
                    sondeo.setIdGrupoSondeo(jsonObject.getInt("IdGrupoSondeo"));
                    sondeo.setNombreSondeo(jsonObject.getString("NombreSondeo"));
                    sondeo.setOrden(jsonObject.getInt("Orden"));
                    sondeo.setTipo(jsonObject.getString("Tipo"));
                    sondeo.setRequerido(jsonObject.getInt("requerido"));
                
                    sondeoCollection.add(sondeo);
                
                }

            } catch (Exception ex) {
                throw new Exception("GetByProyectoUsuario", ex);
            }

            return sondeoCollection;

        }
    }

    public static int get_Exist_Sondeo(Context context, mx.smartteam.entities.SondeoModulos modulos, mx.smartteam.entities.Pop pop) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        int exist = 0;
        
        String query = "SELECT COUNT(1) FROM Contestacion WHERE IdVisita = " + pop.getIdVisita() 
                        + " AND IdSondeo = " + modulos.getId() + ";";
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
           exist = cursor.getInt(0);
        }
        
        return exist;
    }
    
    
    public static void Insert(Context context, mx.smartteam.entities.Sondeo sondeo) throws Exception {
        db = (new BaseDatos(context)).getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery("Select COUNT(Id) FROM Sondeo WHERE id = "+ sondeo.Id , null);
            if(cursor.moveToFirst()){
                int contador = cursor.getInt(0);
                if(contador == 0){ /*Insertamos el registro si no existe */
                    db.execSQL("INSERT INTO Sondeo"
                        + " (Activo,Id,Nombre,idProyecto,FechaSync,StatusSync, IdentificadorVentas,IdGrupoSondeo,NombreSondeo,Orden, Tipo ,Requerido)" 
                            + " SELECT "
                                + sondeo.getActivo() + ","
                                + sondeo.getId() + ", "
                                + "'" + sondeo.getNombre() + "', "
                                + sondeo.getIdProyecto() + ", "
                                + sondeo.getFechaSync() + ", "
                                + sondeo.getStatusSync() + ", "
                                + sondeo.getIdentificadorVentas() + ", "
                                + sondeo.getIdGrupoSondeo() + ", "
                                + "'" + sondeo.getNombreSondeo() + "', "
                                + sondeo.getOrden() + ", "
                                + "'" + sondeo.getTipo() + "', "
                                + sondeo.getRequerido()
                                    + " WHERE NOT EXISTS(SELECT 1 FROM Sondeo WHERE ID = " + sondeo.getId() + " );");
                
                } else { /*  Actualizamos el registro existente*/
                    ContentValues values = new ContentValues();
                        values.put("Activo", sondeo.getActivo().toString());
                        values.put("Nombre", sondeo.getNombre().toString());
                        values.put("IdProyecto", sondeo.getIdProyecto().toString());
                        values.put("FechaSync", sondeo.getFechaSync().toString());
                        values.put("StatusSync", sondeo.getStatusSync().toString());
                        values.put("IdentificadorVentas", sondeo.getIdentificadorVentas().toString());
                        values.put("IdGrupoSondeo", sondeo.getIdGrupoSondeo().toString());
                        values.put("NombreSondeo",sondeo.getNombreSondeo().toString());
                        values.put("Orden",sondeo.getOrden().toString());
                        values.put("Tipo", sondeo.getTipo());
                        values.put("Requerido", sondeo.getRequerido().toString());
                        db.update("Sondeo", values, "Id = " + sondeo.getId().toString(), null);
                }
            }
        }catch(Exception ex){
            ex.getMessage().toString();
        }
        db.close();
    }

    public static mx.smartteam.entities.SondeoCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id,idproyecto,nombre,orden, Tipo, IdentificadorVentas, Requerido FROM sondeo WHERE idproyecto=? and IdGrupoSondeo = 0", new String[]{proyecto.Id.toString()});

        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Sondeo sondeo = new mx.smartteam.entities.Sondeo();
                sondeo.setId(cursor.getInt(0));
                sondeo.setIdProyecto(cursor.getInt(1));
                sondeo.setNombre(cursor.getString(2));
                sondeo.setOrden(cursor.getInt(3));
                sondeo.setTipo(cursor.getString(4));
                sondeo.setIdentificadorVentas(cursor.getInt(5));
                sondeo.setRequerido(cursor.getInt(6));
                sondeoCollection.add(sondeo);
            } while (cursor.moveToNext());
        }
        db.close();
        return sondeoCollection;

    }
        public static mx.smartteam.entities.SondeoModulosCollection GetByProyectosm(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
           // Random rn = new Random();
        mx.smartteam.entities.SondeoModulosCollection sondeoCollection = new SondeoModulosCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id,idproyecto,nombre,orden, Tipo, IdentificadorVentas, Requerido FROM sondeo WHERE idproyecto=? and activo =1 and IdGrupoSondeo = 0", new String[]{proyecto.Id.toString()});

        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //int answer = rn.nextInt(20)+1 ;
                mx.smartteam.entities.SondeoModulos sondeo = new mx.smartteam.entities.SondeoModulos();
                sondeo.setId(cursor.getInt(0));
                sondeo.setIdProyecto(cursor.getInt(1));
                sondeo.setNombre(cursor.getString(2));
                sondeo.setOrden(cursor.getInt(3));
                sondeo.setTipo(cursor.getString(4));
                sondeo.setIdentificadorVentas(cursor.getInt(5));
                sondeo.setRequerido(cursor.getInt(6));
                sondeo.setTposm("sondeos");
                sondeoCollection.add(sondeo);
            } while (cursor.moveToNext());
        }
        db.close();
        return sondeoCollection;

    }
    
        public static mx.smartteam.entities.SondeoCollection GetByProyecto2(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();

        db = (new BaseDatos(context)).getWritableDatabase();
        
        String query = "SELECT id,idproyecto,nombre,orden, Tipo, IdentificadorVentas,Requerido FROM sondeo WHERE idproyecto = " + proyecto.getId() + ";";
        
        Cursor cursor = db.rawQuery(query, null);

        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Sondeo sondeo = new mx.smartteam.entities.Sondeo();
            
                sondeo.setId(cursor.getInt(0));
                sondeo.setIdProyecto(cursor.getInt(1));
                sondeo.setNombre(cursor.getString(2));
                sondeo.setOrden(cursor.getInt(3));
                sondeo.setTipo(cursor.getString(4));
                sondeo.setIdentificadorVentas(cursor.getInt(5));
                sondeo.setRequerido(cursor.getInt(6));
                sondeoCollection.add(sondeo);
            
            } while (cursor.moveToNext());
        }
        db.close();
        return sondeoCollection;

    }
            public static mx.smartteam.entities.SondeoModulosCollection GetGruposSondeossm(Context context, mx.smartteam.entities.Proyecto proyecto)throws Exception {
    mx.smartteam.entities.SondeoModulosCollection sondeoModulosCollection = new SondeoModulosCollection();
    db = (new BaseDatos(context)).getWritableDatabase();
        
    Cursor cursor = db.rawQuery("Select id,idproyecto,nombre,orden, Tipo, IdentificadorVentas,IdGrupoSondeo,NombreSondeo,Requerido FROM sondeo WHERE idproyecto = ? AND activo = 1 and IdGrupoSondeo > 0 group by IdGrupoSondeo",
            new String[]{proyecto.Id.toString()});
    try{
        if(cursor.moveToFirst()){
            do{
                mx.smartteam.entities.SondeoModulos sondeo = new mx.smartteam.entities.SondeoModulos();
                
                    sondeo.setId(cursor.getInt(0));
                    sondeo.setIdProyecto(cursor.getInt(1));
                    sondeo.setNombre(cursor.getString(2));
                    sondeo.setOrden(cursor.getInt(3));
                    sondeo.setTipo(cursor.getString(4));
                    sondeo.setIdentificadorVentas(cursor.getInt(5));
                    sondeo.setIdGrupoSondeo(cursor.getInt(6));
                    sondeo.setNombreSondeo(cursor.getString(7));
                    sondeo.setRequerido(cursor.getInt(8));
                    
                    sondeoModulosCollection.add(sondeo);

            }while(cursor.moveToNext());
        }
    }catch(Exception ex){
        ex.getMessage().toString();
    }
    
    db.close();
    return sondeoModulosCollection;
    }
    
    public static mx.smartteam.entities.SondeoCollection GetGruposSondeos(Context context, mx.smartteam.entities.Proyecto proyecto)throws Exception {
    mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();
    db = (new BaseDatos(context)).getWritableDatabase();
        
    Cursor cursor = db.rawQuery("Select id,idproyecto,nombre,orden, IdentificadorVentas,IdGrupoSondeo,NombreSondeo, Requerido FROM sondeo WHERE idproyecto = ? and IdGrupoSondeo > 0 group by IdGrupoSondeo",
            new String[]{proyecto.Id.toString()});
    
    if(cursor.moveToFirst()){
        do{
            mx.smartteam.entities.Sondeo sondeo = new mx.smartteam.entities.Sondeo();
                sondeo.Id = cursor.getInt(0);
                sondeo.IdProyecto = cursor.getInt(1);
                sondeo.Nombre = cursor.getString(2);
                sondeo.Orden = cursor.getInt(3);
                sondeo.IdentificadorVentas = cursor.getInt(4);
                sondeo.IdGrupoSondeo = cursor.getInt(5);
                sondeo.NombreSondeo = cursor.getString(6);
                sondeo.Requerido = cursor.getInt(7);
                sondeoCollection.add(sondeo);

        }while(cursor.moveToNext());
    }db.close();
    return sondeoCollection;
    }
    
    
    public static mx.smartteam.entities.SondeoCollection GetAllGruposSondeos(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Sondeo currentSondeo)throws Exception {
    mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();
    db = (new BaseDatos(context)).getWritableDatabase();
        
    Cursor cursor = db.rawQuery("Select id,idproyecto,nombre,orden, IdentificadorVentas,IdGrupoSondeo,NombreSondeo, Requerido FROM sondeo WHERE idproyecto = ? and IdGrupoSondeo = ? ",
            new String[]{proyecto.Id.toString(), currentSondeo.IdGrupoSondeo.toString()});
    
    if(cursor.moveToFirst()){
        do{
            mx.smartteam.entities.Sondeo sondeo = new mx.smartteam.entities.Sondeo();
                sondeo.Id = cursor.getInt(0);
                sondeo.IdProyecto = cursor.getInt(1);
                sondeo.Nombre = cursor.getString(2);
                sondeo.Orden = cursor.getInt(3);
                sondeo.IdentificadorVentas = cursor.getInt(4);
                sondeo.IdGrupoSondeo = cursor.getInt(5);
                sondeo.NombreSondeo = cursor.getString(6);
                sondeo.Requerido = cursor.getInt(7);
                sondeoCollection.add(sondeo);

        }while(cursor.moveToNext());
    }db.close();
    return sondeoCollection;
    }
      public static mx.smartteam.entities.SondeoModulosCollection GetAllGruposSondeossm(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.SondeoModulos currentSondeo)throws Exception {
    mx.smartteam.entities.SondeoModulosCollection sondeoCollection = new SondeoModulosCollection();
    db = (new BaseDatos(context)).getWritableDatabase();
        
    Cursor cursor = db.rawQuery("Select id,idproyecto,nombre,orden, IdentificadorVentas,IdGrupoSondeo,NombreSondeo,Requerido FROM sondeo WHERE idproyecto = ? and IdGrupoSondeo = ? ",
            new String[]{proyecto.Id.toString(), currentSondeo.IdGrupoSondeo.toString()});
    
    if(cursor.moveToFirst()){
        do{
            mx.smartteam.entities.SondeoModulos sondeo = new mx.smartteam.entities.SondeoModulos();
                sondeo.Id = cursor.getInt(0);
                sondeo.IdProyecto = cursor.getInt(1);
                sondeo.Nombre = cursor.getString(2);
                sondeo.Orden = cursor.getInt(3);
                sondeo.IdentificadorVentas = cursor.getInt(4);
                sondeo.IdGrupoSondeo = cursor.getInt(5);
                sondeo.NombreSondeo = cursor.getString(6);
                sondeo.Requerido = cursor.getInt(7);
                sondeoCollection.add(sondeo);

        }while(cursor.moveToNext());
    }db.close();
    return sondeoCollection;
    }
    

    /**
     * ****************************************************************************************************
     * public static int GetInfoByVisita(Context context, String
     * nombreProducto){ // mx.smartteam.entities.BodegaCollection
     * bodegaCollection = new mx.smartteam.entities.BodegaCollection();
     *
     * int res = 0; db = (new BaseDatos(context)).getWritableDatabase(); Cursor
     * cursor = db .rawQuery( " SELECT respuesta as Respuesta, " + "
     * max(idContestacion) as UltimaRespuesta " + " From ContestacionPregunta C
     * " + "Inner Join Marca M On c.idMarca = M.id " + "Inner Join Producto P On
     * m.id = P.idMarca " + " Where p.nombre = ? " , new
     * String[]{nombreProducto});
     *
     * if (cursor.moveToFirst()) { // Recorremos el cursor hasta que no haya más
     * registros do { mx.smartteam.entities.Bodega bodega = new
     * mx.smartteam.entities.Bodega();
     *
     * bodega.Cantidad= cursor.getInt(0); // bodegaCollection.add(bodega);
     * res=cursor.getInt(0); } while (cursor.moveToNext()); }
     *
     * return res; }
     * ************************************************************************************
     */
    public static mx.smartteam.entities.Marca
            GetMarcaByVisita(Context context,
                    mx.smartteam.entities.Proyecto proyecto,
                    mx.smartteam.entities.Usuario usuario,
                    mx.smartteam.entities.Pop pop
            )//,mx.smartteam.entities.Producto producto)
    {
        mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();

        db = (new BaseDatos(context)).getWritableDatabase();

        Cursor cursor = db
                .rawQuery(
                        "SELECT m.id, m.Nombre "
                        + " FROM ContestacionPregunta c, Marca m, categoria ca "
                        + " where c.IdMarca=m.id"
                        + "and m.idcategoria=ca.id",
                        new String[]{ /*  proyecto.Id.toString(),
                             usuario.Id.toString(),
                          // producto.SKU.toString(),
                             pop.IdVisita.toString(),
                             pop.DeterminanteGSP.toString() */}
                );

        if (cursor.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                // mx.smartteam.entities.Marca marca= new mx.smartteam.entities.Marca();
                marca.Nombre = cursor.getString(0);
                // marca.add(marca);
            } while (cursor.moveToNext());
        }
        return marca;
    }

    
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.RespuestaSondeoCollection respuestaCollection) throws Exception {

            METHOD_NAME = "/SondeoInsertSKU";
            ServiceURI service = new ServiceURI();

            jsonString = new JSONStringer()
                    .object()
                    .key("Proyecto").object().key("Id").value(visita.IdProyecto.toString()).endObject()
                    .key("Usuario").object().key("Id").value(visita.IdUsuario.toString()).endObject()
                    .key("Sondeo").object().key("Id").value(sondeo.Id.toString()).endObject()
                    .key("Tienda").object().key("DeterminanteGSP").value(visita.DeterminanteGSP.toString()).endObject();

            //Respuestas
            jsonString.key("Respuestas").array();
            for (mx.smartteam.entities.RespuestaSondeo respuestaSondeo : respuestaCollection) {
                
                if(respuestaSondeo.IdMarca== null){respuestaSondeo.IdMarca= 0;}
                if(respuestaSondeo.IdCategoria== null){respuestaSondeo.IdCategoria= 0;}
                
                if(respuestaSondeo.Tipo.equalsIgnoreCase("combo")){
                    if(respuestaSondeo.Opciones.size() > 0){
                        jsonString.object()
                                .key("IdPregunta").value(respuestaSondeo.IdPregunta.toString())
                                .key("Valor").value(respuestaSondeo.Respuesta)
                                .key("Tipo").value(respuestaSondeo.Tipo)
                                .key("CadenaFecha").value(respuestaSondeo.FechaCrea)
                                .key("SKU").value(respuestaSondeo.Sku.toString())
                                .key("IdMarca").value(respuestaSondeo.IdMarca)
                                .key("IdCategoria").value(respuestaSondeo.IdCategoria)
                                .key("IdOpcion").value(respuestaSondeo.IdOpcion);
                        jsonString.key("Opciones").array();
                        for (mx.smartteam.entities.Opcion opcion : respuestaSondeo.Opciones) {
                        jsonString.object()
                                .key("Id").value(opcion.Id.toString())
                                .key("Nombre").value(opcion.Nombre.toString())
                                .key("Nopcion").value(opcion.nOpcion.toString())
                                .endObject();
                        }
                        jsonString.endArray();
                        jsonString.endObject();
                    }
                }else{
                    
                        jsonString.object()
                                .key("IdPregunta").value(respuestaSondeo.IdPregunta.toString())
                                .key("Valor").value(respuestaSondeo.Respuesta)
                                .key("Tipo").value(respuestaSondeo.Tipo)
                                .key("CadenaFecha").value(respuestaSondeo.FechaCrea)
                                .key("SKU").value(respuestaSondeo.Sku.toString())
                                .key("IdMarca").value(respuestaSondeo.IdMarca)
                                .key("IdCategoria").value(respuestaSondeo.IdCategoria)
                                .key("IdOpcion").value(respuestaSondeo.IdOpcion);
                        jsonString.key("Opciones").array();
                        for (mx.smartteam.entities.Opcion opcion : respuestaSondeo.Opciones) {
                        jsonString.object()
                                .key("Id").value(opcion.Id.toString())
                                .key("Nombre").value(opcion.Nombre.toString())
                                .key("Nopcion").value(opcion.nOpcion.toString())
                                .endObject();
                        }
                        jsonString.endArray();
                        jsonString.endObject();
                }
            }// END FOR

            jsonString.endArray();

            jsonString.endObject();

            Log.i("JSON", jsonString.toString());
            strEntity = new StringEntity(jsonString.toString(), "UTF-8");
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            if (!jsonResult.getString("SondeoInsertSKUResult").equals("OK")) {
                throw new Exception("Error en el servicio [SondeoInsert]");
            }

        }
    }

    public static class Upload2 {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.RespuestaSondeo respuestaCollection) throws Exception {

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

            jsonString.object()
                    .key("IdPregunta").value(respuestaCollection.IdPregunta.toString())
                    .key("Valor").value(respuestaCollection.Respuesta)
                    .key("Tipo").value(respuestaCollection.Tipo)
                    .key("CadenaFecha").value(respuestaCollection.FechaCrea);

            jsonString.key("Opciones").array();
            for (mx.smartteam.entities.Opcion opcion : respuestaCollection.Opciones) {
                jsonString.object()
                        .key("Id").value(opcion.Id.toString())
                        .key("Nombre").value(opcion.Nombre.toString())
                        .endObject();
            }
            jsonString.endArray();
            jsonString.endObject();

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
}
