package mx.smartteam.data;


import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import mx.smartteam.entities.MarcaConfigCollection;

public class MarcaConfig {

    private static String METHOD_NAME = "/GetMarcasConfig";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;

    public static class Download {

     
        public static MarcaConfigCollection GetMarcaConfig(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception
        {
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.MarcaConfigCollection marcaConfigCollection = new MarcaConfigCollection();
            jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                    .key("Id").value(proyecto.Id.toString()) 
                    .key("Ufechadescarga").value(time.toString()).endObject()
                    .key("Usuario").object().key("Id").value(usuario.Id.toString()).endObject()
               .endObject();
            strEntity = new StringEntity(jsonString.toString());
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);
            jsonArray = jsonResult
                    .getJSONArray("GetMarcasConfigResult");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.MarcaConfig marcaConfig = new mx.smartteam.entities.MarcaConfig();
                marcaConfig.Id = jsonObject.getInt("Id");
                marcaConfig.IdFormulario = jsonObject.getInt("IdFormulario");
                marcaConfig.IdCategoria = jsonObject.getInt("IdCategoria");
                marcaConfig.IdMarca = jsonObject.getInt("IdMarca");
                marcaConfig.Activo = jsonObject.getInt("Activo");
                marcaConfig.IdSondeo = jsonObject.getString("IdSondeo");
                marcaConfig.Modulo = jsonObject.getString("Modulo");
                marcaConfig.Orden = jsonObject.getInt("Orden");
                marcaConfig.Max = jsonObject.getInt("max");
                marcaConfigCollection.add(marcaConfig);
            }
            return marcaConfigCollection;
        }
    }

   
     
        public static void InsertMarcaconfig(Context context)
        {/*
            db = (new BaseDatos(context)).getWritableDatabase();
            db.execSQL("insert into MarcaConfig values(1,189,64,1065,1,null,'bodega',null,null,2)");
            db.execSQL("insert into MarcaConfig values(2,189,64,1064,0,null,'bodega',null,null,4)");
            db.execSQL("insert into MarcaConfig values(8,189,220,1102,1,null,'bodega',null,null,4)");
            db.execSQL("insert into MarcaConfig values(9,189,67,1066,1,null,'bodega',null,null,4)");
            db.execSQL("insert into MarcaConfig values(10,189,66,1067,1,null,'bodega',null,null,4)");
            
            db.execSQL("insert into MarcaConfig values(3,189,64,1065,1,null,'sos',null,null,2)");
            db.execSQL("insert into MarcaConfig values(4,189,64,1064,1,null,'sos',null,null,1)");
            db.execSQL("insert into MarcaConfig values(5,189,220,1102,1,null,'sos',null,null,4)");
            db.execSQL("insert into MarcaConfig values(6,189,67,1066,1,null,'sos',null,null,4)");
            db.execSQL("insert into MarcaConfig values(7,189,66,1067,0,null,'sos',null,null,4)");*/
            
        } 
        
        public static void InsertMarcaConfig(Context context, mx.smartteam.entities.MarcaConfig marcaConfig) {
            try {
                db = (new BaseDatos(context)).getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT COUNT(Id) as cont FROM MarcaConfig WHERE idcategoria="+marcaConfig.IdCategoria+" and IdMarca="+marcaConfig.IdMarca+" and Modulo is '" +marcaConfig.Modulo+ "' and Idsondeo is "+marcaConfig.IdSondeo+"",null);
                if (cursor.moveToFirst()) {
                    int contador=cursor.getInt(0);
                    if (contador==0){
                        db.execSQL("INSERT INTO MarcaConfig"
                            + " (Id,IdFormulario,IdCategoria,IdMarca,Activo,IdSondeo,Modulo,FechaCrea,FechaModi,Orden, Max)" + " SELECT "
                            + marcaConfig.Id + ","
                            + marcaConfig.IdFormulario + ","
                            + marcaConfig.IdCategoria + ","
                            + marcaConfig.IdMarca + ","
                            + marcaConfig.Activo + ","
                            + marcaConfig.IdSondeo + ",'"
                            + marcaConfig.Modulo + "','"
                            + marcaConfig.FechaCrea + "','"                        
                            + marcaConfig.FechaModi+"',"
                            + marcaConfig.Orden+","
                            + marcaConfig.Max
                        );
                    }else{
                        db.execSQL("UPDATE MarcaConfig SET Activo="+marcaConfig.Activo+", Orden="+marcaConfig.Orden+",FechaModi='"+marcaConfig.FechaModi+"', Max ="+ marcaConfig.Max+" WHERE "
                            + "  IdCategoria="+marcaConfig.IdCategoria+" AND  IdMarca="+marcaConfig.IdMarca+" and IdSondeo is "+marcaConfig.IdSondeo+" and Modulo is '"+marcaConfig.Modulo+"'");
                    }   
                }
            } catch (Exception e) {
                Log.i("Insert", e.getMessage());
            } finally {
                db.close();
            }
        }

}
