package mx.smartteam.data;

import android.content.ContentValues;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.MarcaCollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import mx.smartteam.entities.MarcaInactivaCollection;

public class MarcaInactiva {

    private static String METHOD_NAME = "/GetMarcaInactiva";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

     
        public static MarcaInactivaCollection GetMarcaInactiva(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception {

            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.MarcaInactivaCollection marcainactivaCollection = new MarcaInactivaCollection();

                    jsonString = new JSONStringer().object()
                            .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
                            .key("Proyecto").object()
                            .key("Id").value(proyecto.Id.toString()) 
                            .key("Ufechadescarga").value(time.toString())
                            .endObject()
                       .endObject();
                    
                     

            strEntity = new StringEntity(jsonString.toString());

            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult
                    .getJSONArray("GetMarcaInactivaResult");

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
               mx.smartteam.entities.MarcaInactiva marcaInactiva = new mx.smartteam.entities.MarcaInactiva();
                marcaInactiva.id = jsonObject.getInt("Id");
                marcaInactiva.idProyecto = jsonObject.getInt("IdProyecto");
                marcaInactiva.idMarca = jsonObject.getInt("IdMarca");
                marcaInactiva.activo = jsonObject.getInt("Activo");
                marcaInactiva.idSondeo = jsonObject.getString("IdSondeo");
                marcaInactiva.modulo = jsonObject.getString("Modulo");
               // marcaInactiva.fechaCrea = jsonObject.getString("fechaCrea");
                marcaInactiva.fechaModi = jsonObject.getString("FechaMod");

                marcainactivaCollection.add(marcaInactiva);

            }
            return marcainactivaCollection;

        }
    }

   
    //marcas inactivas
     
        public static void InsertMarcaInactiva(Context context) {
       db = (new BaseDatos(context)).getWritableDatabase();
       db.execSQL("INSERT INTO MarcaInactiva select 1,137,1067,1,null,'anaquel_precios','2017-01-26','2017-01-26' WHERE NOT EXISTS (SELECT * FROM MarcaInactiva WHERE idproyecto=164 and idmarca=1497 and modulo='anaquel_precios')");
        db.execSQL("INSERT INTO MarcaInactiva select 2,137,1066,1,null,'anaquel_precios','2017-01-26','2017-01-26' WHERE NOT EXISTS (SELECT * FROM MarcaInactiva WHERE idproyecto=164 and idmarca=1531 and modulo='sos')");
        
    } 
         public static void SelectMarcaInactiva(Context context, mx.smartteam.entities.MarcaInactiva marcainactiva) {        
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT count(id) as cont FROM MarcaInactiva WHERE idProyecto="+marcainactiva.idProyecto+" and idmarca="+marcainactiva.idMarca+" and modulo is '" +marcainactiva.modulo+ "' and idsondeo is "+marcainactiva.idSondeo+"",null);
            if (cursor.moveToFirst()) {
         
                    int contador=cursor.getInt(0);
                    if (contador==0){
                          db.execSQL("INSERT INTO MarcaInactiva"
                        + " (id,idProyecto,idMarca,activo,idSondeo,modulo,fechaCrea,fechaModi)" + " SELECT "
                        + marcainactiva.id + ","
                        + marcainactiva.idProyecto + ","
                        + marcainactiva.idMarca + ","
                        + marcainactiva.activo + ","
                        + marcainactiva.idSondeo + ",'"
                        + marcainactiva.modulo + "','"
                        + marcainactiva.fechaCrea + "','"                        
                        + marcainactiva.fechaModi+"'"
                        );
                    }else{
                        db.execSQL("update MarcaInactiva set activo="+marcainactiva.activo+",fechaModi='"+marcainactiva.fechaModi+"' where idproyecto="+marcainactiva.idProyecto
                                +" and  idMarca="+marcainactiva.idMarca+" and idSondeo is "+marcainactiva.idSondeo+" and modulo is '"+marcainactiva.modulo+"'");
                        
                    }
   
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        
    }
     
    

  


}
