package mx.smartteam.data;

import android.content.ContentValues;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import mx.smartteam.entities.MarcaCollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Marca {

    private static String METHOD_NAME = "/GetMarcas";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

        public static MarcaCollection GetMarcas(
                mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {

            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.MarcaCollection marcaCollection = new MarcaCollection();

            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject().endObject();

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult
                    .getJSONArray("GetMarcasResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
                marca.Id = jsonObject.getInt("Id");
                marca.Nombre = jsonObject.getString("Nombre");
                marca.IdCategoria = jsonObject.getInt("IdCategoria");
                marca.IdProyecto = proyecto.Id;
                marca.FechaSync = jsonObject.getLong("FechaSync");
                marca.StatusSync = jsonObject.getInt("Statussync");

                marcaCollection.add(marca);

            }

            return marcaCollection;

        }
    }

    public static class Upload {
    }
        
    public static void Insert(Context context, mx.smartteam.entities.Marca marca) {

        db = (new BaseDatos(context)).getWritableDatabase();
     
        switch (marca.StatusSync) {
            case 1:

                db.execSQL("INSERT INTO Marca"
                        + " (Id,Nombre,IdCategoria,IdProyecto,FechaSync,StatusSync)" + " SELECT "
                        + marca.Id + ",'"
                        + marca.Nombre + "',"
                        + marca.IdCategoria + ","
                        + marca.IdProyecto + ","
                        + marca.FechaSync + ","
                        + marca.StatusSync
                        + " WHERE NOT EXISTS(SELECT 1 FROM Marca WHERE ID="
                        + marca.Id + " AND IdCategoria=" + marca.IdCategoria + " )");
               
                break;
            case 2:
                db.execSQL("INSERT INTO Marca"
                        + " (Id,Nombre,IdCategoria,IdProyecto,FechaSync,StatusSync)" + " SELECT "
                        + marca.Id + ",'"
                        + marca.Nombre + "',"
                        + marca.IdCategoria + ","
                        + marca.IdProyecto + ","
                        + marca.FechaSync + ","
                        + marca.StatusSync
                        + " WHERE NOT EXISTS(SELECT 1 FROM Marca WHERE ID="
                        + marca.Id + " AND IdCategoria=" + marca.IdCategoria + " )");

                ContentValues values = new ContentValues();
                values.put("Nombre", marca.Nombre.toString());
                values.put("Idcategoria", marca.IdCategoria.toString());
                values.put("IdProyecto", marca.IdProyecto.toString());
                values.put("StatusSync", marca.StatusSync.toString());
                values.put("FechaSync", marca.FechaSync.toString());

                db.update("Marca", values, "Id=" + marca.Id.toString() + " and Idcategoria=" + marca.IdCategoria.toString(), null);

                break;
        }

        db.close();

    }

    public static MarcaCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto,String tipo) {

        MarcaCollection marcaCollection = new MarcaCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();

            String qquery = "SELECT m.id,m.nombre FROM Marca m "
                        + " INNER JOIN marcaconfig mc on mc.idmarca = m.id AND mc.activo=1 AND "+tipo 
                        + " WHERE m.idproyecto = "+proyecto.Id.toString()+" order by m.id ";
            
 Cursor cursor = db.rawQuery(qquery, null);
            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
//                  mx.smartteam.entities.Marca marca2 = new mx.smartteam.entities.Marca();
//                        marca2.Id = -1; marca2.Nombre = "Todo";
//                        marcaCollection.add(marca2);
                do {
                    mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
                    marca.Id = cursor.getInt(0);
                    marca.Nombre = cursor.getString(1);
                    marca.bandera = 0;
                    marcaCollection.add(marca);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }

    public static MarcaCollection GetByProyectoSos(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop)
    {

        MarcaCollection marcaCollection = new MarcaCollection();
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT m.*, ifnull(s.idmarca,0) as mar FROM Marca m "
                    + "LEFT JOIN Sos s On s.idmarca = m.id  "
                    + "WHERE m.idproyecto=? AND s.IdVisita = ? ",
                    new String[]{
                        proyecto.Id.toString(),
                        pop.IdVisita.toString()
                    });

            //Nos aseguramos de que existe al menos un registro
            if (cursor.moveToFirst()) {

                do {
                    mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
                    marca.Id = cursor.getInt(0);
                    marca.Nombre = cursor.getString(1);
                    marcaCollection.add(marca);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }
//marcainactiva
    public static MarcaCollection GetByProyectoCategoria(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Categoria categoria, String tipo) {
        MarcaCollection marcaCollection = new MarcaCollection();
        try {
            if (proyecto != null && categoria != null) {

                db = (new BaseDatos(context)).getWritableDatabase();
                String qquery = "SELECT m.id,m.nombre FROM marcaconfig mc "
                       + " INNER JOIN marca m on m.id=mc.idmarca  "
                       + " WHERE m.idproyecto = "+proyecto.Id.toString()+" AND "+tipo +" AND mc.IdCategoria="+categoria.Id.toString()+" "
                       + " AND mc.activo=1 GROUP BY m.id ORDER BY mc.orden";
                
               Cursor cursor = db.rawQuery(qquery, null);

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {            
                    do {
                        mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
                        marca.Id = cursor.getInt(0);
                        marca.Nombre = cursor.getString(1);
                        marca.bandera =0;
                        marcaCollection.add(marca);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }
     public static MarcaCollection GetMarcasFotos(Context context, mx.smartteam.entities.Proyecto proyecto) {
        MarcaCollection marcaCollection = new MarcaCollection();
        try {
            if (proyecto != null ) {

                db = (new BaseDatos(context)).getWritableDatabase();
                

               Cursor cursor = db.rawQuery("select config,nombre from categoria where config>0 and idproyecto="+proyecto.Id.toString()+" group by config", null);

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
//                  mx.smartteam.entities.Marca marca2 = new mx.smartteam.entities.Marca();
//                        marca2.Id = -1; marca2.Nombre = "Todo";
//                        marcaCollection.add(marca2);                    
                    do {
                         
    
		
                        mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
                        marca.Id = cursor.getInt(0);
                        String NombreSplit = cursor.getString(1);
                        String diaArray[] = NombreSplit.split("-");
                        marca.Nombre=diaArray[0];
                        marca.bandera = 0;
                        marcaCollection.add(marca);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }
       public static MarcaCollection GetMarcasXConfig(Context context, mx.smartteam.entities.Proyecto proyecto,Integer idmarca) {
        MarcaCollection marcaCollection = new MarcaCollection();
        try {
            if (proyecto != null ) {

                db = (new BaseDatos(context)).getWritableDatabase();
                

               Cursor cursor = db.rawQuery("select c.*, ifnull(f.idCategoria,0) as cat from categoria c " +
" LEFT JOIN foto f ON  f.idCategoria = c.id where c.config="+idmarca+" and c.idproyecto="+proyecto.Id.toString()+" group by c.id ", null);

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
//                  mx.smartteam.entities.Marca marca2 = new mx.smartteam.entities.Marca();
//                        marca2.Id = -1; marca2.Nombre = "Todo";
//                        marcaCollection.add(marca2);                    
                    do {
                        mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
                        marca.Id = cursor.getInt(0);
                        String NombreSplit = cursor.getString(1);
                        String diaArray[] = NombreSplit.split("-");
                        marca.Nombre=diaArray[1];      
                        marca.bandera = cursor.getInt(7);
                        if (marca.bandera > 0) {
                            marca.bandera = 1;
                        }                     
                        marcaCollection.add(marca);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }

    public static MarcaCollection GetByProyectoCategoriaSos(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Categoria categoria, mx.smartteam.entities.Pop pop,String tipo) {

        MarcaCollection marcaCollection = new MarcaCollection();
        mx.smartteam.entities.Marca marca = null;
        try {
            if (proyecto != null ) {
                String query ="";
                db = (new BaseDatos(context)).getWritableDatabase();
                if("foto_anaquel"==tipo){
                   query = "SELECT m.*, ifnull(f.idmarca,0) as mar  FROM Marca m "
                           + "LEFT JOIN foto f ON f.idmarca=m.id and f.categoria = m.idcategoria"

                           + " INNER JOIN marcaconfig mc on mc.idmarca=m.id and m.idcategoria=mc.idcategoria "
                    + " WHERE  m.idproyecto="+ proyecto.Id.toString()+" and mc.modulo='"+tipo+"'   and mc.activo=1  ";
                }else{
                     query = "SELECT m.*, ifnull(s.idmarca,0) as mar,mc.max  FROM Marca m "
                    + "  LEFT JOIN Sos s ON s.idmarca = m.id AND m.idcategoria=s.idcategoria AND s.IdVisita = " + pop.IdVisita.toString()
                    + " INNER JOIN marcaconfig mc on mc.idmarca=m.id  and m.idcategoria=mc.idcategoria "
                    + " WHERE  m.idproyecto="+ proyecto.Id.toString()+" and mc.activo=1  AND  mc.modulo='"+tipo+"'";
                }
                if (categoria != null) {
                    query += " AND m.idcategoria=" + categoria.Id.toString();
                }
                query +=" Group by m.id order by mc.orden";

                Cursor cursor = db.rawQuery(query, null);

                //Nos aseguramos de que existe al menos un registro
                if(cursor.moveToFirst()) {
                    do {
                        marca = new mx.smartteam.entities.Marca();
                        marca.Id = cursor.getInt(0);
                        marca.Nombre = cursor.getString(1);
                        marca.bandera = cursor.getInt(6);
                        if("sos"==tipo){
                            marca.Max = cursor.getInt(7);
                        }
                        if (marca.bandera > 0) {
                            marca.bandera = 1;
                        }
                        marcaCollection.add(marca);
                    } while (cursor.moveToNext());
                }
            }   
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }
     public static MarcaCollection GetMarcaFotoAnaquel(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Categoria categoria, mx.smartteam.entities.Pop pop,String tipo) {

        MarcaCollection marcaCollection = new MarcaCollection();
        mx.smartteam.entities.Marca marca = null;
        try {
            if (proyecto != null ) {

                db = (new BaseDatos(context)).getWritableDatabase();
               
                String query = " Select m.id,m.nombre from marcaconfig mc"
                        + " INNER JOIN marca m on m.id = mc.idmarca"
                        + " WHERE  m.idproyecto="+ proyecto.Id.toString()+" and mc.modulo='"+tipo+"'   and mc.activo=1  ";
                if (categoria != null) {
                    query += " AND mc.idcategoria=" + categoria.Id.toString();
                }
                query +=" Group by m.id order by mc.orden";

                Cursor cursor = db.rawQuery(query, null);

                //Nos aseguramos de que existe al menos un registro
                if (cursor.moveToFirst()) {
                    do {
                        marca = new mx.smartteam.entities.Marca();
                        marca.Id = cursor.getInt(0);
                        marca.Nombre = cursor.getString(1);

                        

                        marcaCollection.add(marca);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return marcaCollection;
    }
    
    public static MarcaCollection GetAllXContestacion(Context context, mx.smartteam.entities.Proyecto proyecto, 
            mx.smartteam.entities.Pop pop, mx.smartteam.entities.SondeoModulos sondeo) {

        MarcaCollection marcaCollection = new MarcaCollection();
        mx.smartteam.entities.Marca marca = null;
        try {
            if (proyecto != null ) {

                db = (new BaseDatos(context)).getWritableDatabase();

                String query = "SELECT m.Id, m.Nombre FROM Marca m "
                        + " INNER JOIN marcaconfig mc ON mc.idmarca=m.id "
                        + " WHERE m.idproyecto = "+proyecto.Id.toString()+ " AND mc.activo=1   AND mc.idsondeo="+sondeo.Id+" GROUP BY m.id ORDER BY mc.orden";
                
                Cursor cursor = db.rawQuery(query, null);

                //Nos aseguramos de que existe al menos un registro
                if(cursor.moveToFirst()) {
                    do {
                        
                        marca = new mx.smartteam.entities.Marca();
                        marca.Id = cursor.getInt(0);
                        marca.Nombre = cursor.getString(1);
                        
                        String subquery = "SELECT idmarca FROM contestacionPregunta cp INNER JOIN  Contestacion c ON c.id = cp.idcontestacion "
                                + "WHERE c.idVisita = "+ pop.IdVisita +" and c.idsondeo = "+ sondeo.Id +" and cp.idmarca = " + marca.Id +" group by c.id limit 1";
                        Cursor cursor2 = db.rawQuery(subquery, null);
                            if(cursor2.moveToFirst()){
                                do{
                                    mx.smartteam.entities.Marca marca2 = new mx.smartteam.entities.Marca();
                                    marca2.Id = cursor.getInt(0);
                                    marca.bandera =1;

                                }while(cursor2.moveToNext());
                            }else{
                            marca.bandera =0;
                            }
                        
                        marcaCollection.add(marca);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("select", e.getMessage());
        } finally {
            db.close();
        }
        return marcaCollection;
    }
}
