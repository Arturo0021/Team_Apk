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
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.OpcionCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Opcion {
    
    private static String METHOD_NAME = "/";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
    
    public static void Insert(Context context, mx.smartteam.entities.Opcion opcion) throws Exception {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        switch(opcion.StatusSync){
            case 1 : 
                
                db.execSQL("INSERT INTO Opcion"
                + " (Id,Nombre,IdProyecto,Titulo,FechaSync,StatusSync)" + " SELECT "
                + opcion.Id + ",'"
                + opcion.Nombre + "',"
                + opcion.IdProyecto + ",'"
                + opcion.Titulo + "',"
                + opcion.FechaSync+ ","
                + opcion.StatusSync
                + " WHERE NOT EXISTS(SELECT 1 FROM Opcion WHERE ID="
                + opcion.Id + " )");
               
                break;
            case 2 : 
                db.execSQL("INSERT INTO Opcion"
                + " (Id,Nombre,IdProyecto,Titulo,FechaSync,StatusSync)" + " SELECT "
                + opcion.Id + ",'"
                + opcion.Nombre + "',"
                + opcion.IdProyecto + ",'"
                + opcion.Titulo + "',"
                + opcion.FechaSync+ ","
                + opcion.StatusSync
                + " WHERE NOT EXISTS(SELECT 1 FROM Opcion WHERE ID="
                + opcion.Id + " )");
               
                  
         ContentValues values = new ContentValues();
            values.put("Nombre", opcion.Nombre.toString());
            values.put("IdProyecto", opcion.IdProyecto.toString());
            values.put("Titulo", opcion.Titulo.toString());
            values.put("StatusSync", opcion.StatusSync.toString());
            values.put("FechaSync", opcion.FechaSync.toString());
            
            db.update("Opcion", values, "Id=" + opcion.Id.toString(), null);
        
         
                
                break;
        }
        
        db.close();
    }
    
    public static OpcionCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) {
        OpcionCollection opcionCollection = new OpcionCollection();       
        
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id,nombre,idproyecto,titulo FROM Opcion WHERE idproyecto=?", new String[]{proyecto.Id.toString()});

        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                mx.smartteam.entities.Opcion opcion = new mx.smartteam.entities.Opcion();
                opcion.Id = cursor.getInt(0);
                opcion.Nombre = cursor.getString(1);
                opcion.IdProyecto = cursor.getInt(2);
                opcion.Titulo = cursor.getString(3);
                opcionCollection.add(opcion);
            } while (cursor.moveToNext());
        }        
        db.close();
        return opcionCollection;
    }
    
    public static class Download {
        
        public static mx.smartteam.entities.OpcionCollection GetOpcionesFoto(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time) throws Exception {
            
            METHOD_NAME = "/GetOpcionesFoto";
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.OpcionCollection opcionCollection = new OpcionCollection();
            
            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time.toString())
                    .endObject()
                    .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                    .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            
            jsonArray = jsonResult
                    .getJSONArray("GetOpcionesFotoResult");
            
            
            
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Opcion opcion = new mx.smartteam.entities.Opcion();
                opcion.Id = jsonObject.getInt("Id");
                opcion.Nombre = jsonObject.getString("Nombre");
                opcion.Titulo = jsonObject.getString("Titulo");
                opcion.IdProyecto = proyecto.Id;
                opcion.FechaSync = jsonObject.getLong("FechaSync");
                opcion.StatusSync = jsonObject.getInt("Statussync");
                
                opcionCollection.add(opcion);
            }
            return opcionCollection;
        }
    }
}
