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
import mx.smartteam.entities.CategoriasConfigCollection;
import mx.smartteam.entities.MarcaInactivaCollection;

public class CategoriaConfig {

    private static String METHOD_NAME = "/GetCategoriaConfig";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

     
        public static CategoriasConfigCollection GetCategoriaConfig(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception 
        {
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.CategoriasConfigCollection configCategoriasCollection = new CategoriasConfigCollection();
            jsonString = new JSONStringer().object()
                    
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString()) 
                    .key("Ufechadescarga").value(time.toString()).endObject()
                    .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
               .endObject();
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            jsonArray = jsonResult
                    .getJSONArray("GetCategoriaConfigResult");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.CategoriasConfig configCategorias = new mx.smartteam.entities.CategoriasConfig();
                configCategorias.Id = jsonObject.getInt("Id");
                configCategorias.IdFormulario = jsonObject.getInt("IdFormulario");
                configCategorias.IdCategoria = jsonObject.getInt("IdCategoria");
                configCategorias.Activo = jsonObject.getInt("Activo");
                configCategorias.IdSondeo = jsonObject.getString("IdSondeo");
                configCategorias.Modulo = jsonObject.getString("Modulo");
                configCategorias.Orden = jsonObject.getInt("Orden");
//                configCategorias.FechaCrea = jsonObject.getString("fechaCrea");
//                configCategorias.FechaModi = jsonObject.getString("FechaMod");
                configCategoriasCollection.add(configCategorias);
            }
            return configCategoriasCollection;
        }
    }

   
     
        public static void InsertCategoriaConfig2(Context context)
        {
            db = (new BaseDatos(context)).getWritableDatabase();
            db.execSQL("insert into CategoriasConfig values(1,189,64,1,null,'existencias_bodega',null,null,2)");
            db.execSQL("insert into CategoriasConfig values(2,189,66,0,null,'existencias_bodega',null,null,4)");
            db.execSQL("insert into CategoriasConfig values(3,189,67,0,null,'existencias_bodega',null,null,1)");
            db.execSQL("insert into CategoriasConfig values(4,189,220,1,null,'existencias_bodega',null,null,3)");
            db.execSQL("insert into CategoriasConfig values(5,189,64,0,null,'sos',null,null,4)");
            db.execSQL("insert into CategoriasConfig values(6,189,66,1,null,'sos',null,null,1)");
            db.execSQL("insert into CategoriasConfig values(7,189,67,1,null,'sos',null,null,3)");
            db.execSQL("insert into CategoriasConfig values(8,189,220,0,null,'sos',null,null,2)");
        } 
        
         public static void InsertCategoriaConfig(Context context, mx.smartteam.entities.CategoriasConfig configCategorias) {        
        try {
            db = (new BaseDatos(context)).getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT count(Id) as cont FROM CategoriasConfig WHERE  IdCategoria="+configCategorias.IdCategoria+" and Modulo is '" +configCategorias.Modulo+ "' and Idsondeo is "+configCategorias.IdSondeo+"",null);
            if (cursor.moveToFirst()) {         
                    int contador=cursor.getInt(0);
                    if (contador==0){
                          db.execSQL("INSERT INTO CategoriasConfig"
                        + " (Id,IdFormulario,IdCategoria,Activo,IdSondeo,Modulo,FechaCrea,FechaModi,Orden)" + " SELECT "
                        + configCategorias.Id + ","
                        + configCategorias.IdFormulario + ","
                        + configCategorias.IdCategoria + ","
                        + configCategorias.Activo + ","
                        + configCategorias.IdSondeo + ",'"
                        + configCategorias.Modulo + "','"
                        + configCategorias.FechaCrea + "','"                        
                        + configCategorias.FechaModi+"',"
                        + configCategorias.Orden
                        );
                    }else{
                        db.execSQL("update CategoriasConfig set Activo="+configCategorias.Activo+", Orden="+configCategorias.Orden+",FechaModi='"+configCategorias.FechaModi+"' where "
                                + "   IdCategoria="+configCategorias.IdCategoria+" and IdSondeo is "+configCategorias.IdSondeo+" and Modulo is '"+configCategorias.Modulo+"'");                        
                    }   
            }
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        
    }
     
    

  


}
