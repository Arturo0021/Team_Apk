package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import mx.smartteam.entities.PonderacionCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Ponderacion {
        private static String METHOD_NAME = "";
        private static StringEntity strEntity = null;
        private static JSONObject jsonResult = null;
        private static JSONArray jsonArray = null;
        private static JSONStringer jsonString = null;
        private static SQLiteDatabase db = null;
    
    public static class Download {
        
        public static mx.smartteam.entities.PonderacionCollection getAllPonderaciones(mx.smartteam.entities.Proyecto proyecto, String time)
            throws JSONException, UnsupportedEncodingException, IOException{
            METHOD_NAME = "/GetPonderaciones";            
            ServiceURI service = new ServiceURI();
            
            mx.smartteam.entities.PonderacionCollection ponderacionCollection = new PonderacionCollection();
            
            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString())
                    .key("Ufechadescarga").value(time)
                    .endObject().endObject();
            
            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult.getJSONArray("GetPonderacionesResult");

            for(int i = 0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.Ponderacion Entityponderacion = new mx.smartteam.entities.Ponderacion();
                Entityponderacion.Id = jsonObject.getInt("id");
                Entityponderacion.IdProyecto = jsonObject.getInt("idProyecto");
                Entityponderacion.IdCadena = jsonObject.getInt("idCadena");
                Entityponderacion.Ponderacion = jsonObject.getInt("ponderacion");
                Entityponderacion.Activo = jsonObject.getInt("activo");
                
            ponderacionCollection.add(Entityponderacion);
            }
            return ponderacionCollection;
        }
    }
    
    public static void 
        Insert(Context context, mx.smartteam.entities.Ponderacion ponderacion){
        db = (new BaseDatos(context)).getWritableDatabase();
    
        String qquery = "SELECT count(1) as cont FROM Ponderaciones WHERE id = " + ponderacion.Id.toString();
        
        Cursor cursor  = db.rawQuery(qquery, null);
        if(cursor.moveToFirst()){
            int contador = cursor.getInt(0);
            if(contador ==0){
                db.execSQL("INSERT INTO Ponderaciones "
                        + "(Id,IdProyecto,IdCadena,Ponderacion,Activo) VALUES("
                        + ponderacion.Id  + " , "
                        + ponderacion.IdProyecto + " , "
                        + ponderacion.IdCadena + " , "
                        + ponderacion.Ponderacion +" , "
                        + ponderacion.Activo 
                        + ");" 
                );
            }else{
                ContentValues values = new ContentValues();
                    values.put("Ponderacion", ponderacion.Ponderacion.toString());
                    values.put("Activo",ponderacion.Activo.toString());
                db.update("Ponderaciones", values, "Id=" + ponderacion.Id.toString(), null);
            }
        }
    }
        
        
        
        
        
        
}
