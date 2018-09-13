package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.triplei.Utilerias;
import mx.smartteam.entities.PopCollection;
import mx.smartteam.entities.RutasCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Rutas {

    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "/GetRutaUsuariov2";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
     
    public static class Download{
            
        public static mx.smartteam.entities.RutasCollection GetByproyectoUsuario(
                mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time) throws Exception {
                RutasCollection rutasCollection = new RutasCollection();
                
                ServiceURI service = new ServiceURI();
                try{
                    jsonString = new JSONStringer().object()
                            .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                            .key("Proyecto").object()
                            .key("Id").value(proyecto.Id.toString())
                            .key("Ufechadescarga").value(time.toString())
                            .endObject()
                            .endObject();
                
                    strEntity = new StringEntity(jsonString.toString());
                    jsonResult = service.HttpGet(METHOD_NAME, strEntity);
                    jsonArray = jsonResult.getJSONArray("GetRutaUsuariov2Result");
                    
                    for(int i = 0; i < jsonArray.length(); ++i ){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mx.smartteam.entities.Rutas rutas = new mx.smartteam.entities.Rutas();
                        rutas.Id = jsonObject.getInt("Id");
                        rutas.IdProyecto = jsonObject.getInt("IdProyecto");
                        rutas.IdUsuario = jsonObject.getInt("IdUsuario");
                        rutas.DeterminanteGSP = jsonObject.getInt("DeterminanteGSP");
                        rutas.Dia = jsonObject.getInt("Dia");
                        rutas.Orden = jsonObject.getInt("Orden");
                        rutas.Fecha = jsonObject.getLong("Fecha");
                        rutas.FechaSync = jsonObject.getLong("FechaSync");
                        rutas.StatusSync = jsonObject.getInt("Statussync");
                        rutas.activo = jsonObject.getInt("activo");
                        
                        rutasCollection.add(rutas); 
                    }
                }catch(Exception ex){
                     throw new Exception("GetRutasResponse", ex);
                }
            return rutasCollection;
        }
    }
    
    
    public static void Insert(Context context, mx.smartteam.entities.Rutas rutas) throws Exception {
        db = (new BaseDatos(context)).getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(Id) as cont FROM Rutas WHERE id ="+rutas.Id+";",null);
            if(cursor.moveToFirst()) {
                int contador=cursor.getInt(0);
                
                if(contador == 0){ // si no encuentra nada en la consulta anterior
                    db.execSQL("INSERT INTO Rutas "
                        + "(Id, IdProyecto, IdUsuario, DeterminanteGSP, Dia, Orden,Fecha,StatusSync,FechaSync,activo)" + " VALUES ("
                        + rutas.Id + "," + rutas.IdProyecto + "," + rutas.IdUsuario + "," + rutas.DeterminanteGSP + ","
                        + rutas.Dia + "," + rutas.Orden + "," + rutas.Fecha + "," + rutas.StatusSync + "," + rutas.FechaSync+ " , "+ rutas.activo
                        + ")");    
                }else{
                    ContentValues values = new ContentValues();
                    values.put("IdProyecto", rutas.IdProyecto.toString());
                    values.put("IdUsuario", rutas.IdUsuario.toString());
                    values.put("DeterminanteGSP", rutas.DeterminanteGSP.toString());
                    values.put("Dia", rutas.Dia.toString());
                    values.put("Orden", rutas.Orden.toString());
                    values.put("Fecha", rutas.Fecha.toString());
                    values.put("StatusSync", rutas.StatusSync.toString());
                    values.put("FechaSync", rutas.FechaSync.toString());
                    values.put("activo", rutas.activo.toString());
                    db.update("Rutas", values, "Id=" + rutas.Id.toString(), null);
                }
            }
        }catch(Exception ex){
            ex.getMessage().toString();
        }      
        db.close();
    }

    public static mx.smartteam.entities.PopCollection GetByRutasToday(final Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.PopCollection popCollection = new PopCollection();
        
        if (usuario != null && proyecto != null) {
            db = (new BaseDatos(context)).getWritableDatabase();

            String qquery = "";
            qquery = "SELECT " +
            " p.Activo ,p.Altitud ,p.CP ,p.Calle ,p.Colonia ,p.Id ,p.Latitud ," +
            " p.Longitud , p.determinanteGSP ,p.direccion ,p.idCadena ,p.idCanal , p.idGrupo ,  " +
            " p.sucursal ,p.cadena , datetime(r.Fecha, 'unixepoch', 'localtime' ) as nfecha" +
            " FROM Rutas r " +
            " INNER JOIN Pop p on p.determinantegsp = r.determinantegsp AND r.idproyecto = p.idproyecto " +
            " WHERE r.idusuario = ? AND r.idproyecto = ?  " +
            " AND datetime( r.Fecha, 'unixepoch', 'localtime' )  Like  '%" + Utilerias.getFecha() + "%'  ";
            
        Cursor cursor = db.rawQuery(qquery,new String[]{usuario.Id.toString(),proyecto.Id.toString()});
         
        if (cursor.moveToFirst()) {
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
                 
                 Cursor t = db.rawQuery(
                        " SELECT * FROM POPVISITA "
                      + " WHERE  IdProyecto=? AND "
                      + " DeterminanteGSP=? AND "
                      + " IdUsuario=? AND datetime(FechaCrea, 'unixepoch', 'localtime' ) "
                      + " Like  '%" + Utilerias.getFecha() + "%'  ",
                    new String[]{
                        proyecto.Id.toString(),
                        pop.DeterminanteGSP.toString(),
                        usuario.Id.toString()
                    }
                );

                /*
                    *  Si se repite el determinante el mismo dia no hara nada, pero si no existe ningun registro se agrega :) hasta aqui vamos bien 
                */
                if(t.getCount()==0){
                  long fechacrea = Utilerias.getFechaUnix();
                  db.execSQL("INSERT INTO POPVISITA (IdProyecto, DeterminanteGSP, IdUsuario, IdStatus, FechaCrea )"
                            + " VALUES  (" + proyecto.Id.toString()+","+pop.DeterminanteGSP+","+ usuario.Id.toString()+", 0 ,"+ fechacrea + " );");
                  
                }

                popCollection.add(pop);
            } while (cursor.moveToNext());
        }
       }
        db.close();
        return popCollection;

    }    
      
} /*
   *  END CLASS
  */