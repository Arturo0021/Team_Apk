package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Exhibicion {
    private static String METHOD_NAME = "/GetExhibicionCat";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
    
    public static class Download {
    
        public static mx.smartteam.entities.ExhibicionCollection GetExhibicion() throws Exception {
            mx.smartteam.entities.ExhibicionCollection exhibicionCollection = new mx.smartteam.entities.ExhibicionCollection();
            ServiceURI service = new ServiceURI();
            try{
            
                jsonResult = service.HttpGet(METHOD_NAME, null);
                jsonArray = jsonResult.getJSONArray("GetExhibicionCatResult");
                
                for(int i = 0; i< jsonArray.length(); i++ ){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mx.smartteam.entities.Exhibicion exhibicion = new mx.smartteam.entities.Exhibicion();
                    exhibicion.Id = jsonObject.getInt("Id");
                    exhibicion.Nombre = jsonObject.getString("Nombre");
                    exhibicionCollection.add(exhibicion);
                }
                
            }catch(Exception ex){
                throw new Exception("GetExhibiciones");
            }
            
            return exhibicionCollection;
        }

    }

            
    public static void Insert(Context context, mx.smartteam.entities.Exhibicion exhibicion) throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT COUNT(Id) FROM Exhibicion WHERE Id = " + exhibicion.Id , null);
            if(cursor.moveToFirst()){
                int contador = cursor.getInt(0);
                if(contador == 0){
                    db.execSQL("INSERT INTO Exhibicion " + "(Id, Nombre) VALUES ("+ exhibicion.Id +",'" + exhibicion.Nombre + "'"+ ")");   
                } else {
                     ContentValues values = new ContentValues();
                        values.put("Id", exhibicion.Id);
                        values.put("Nombre", exhibicion.Nombre);
                    db.update("Exhibicion", values, "Id = " + exhibicion.Id , null);
                }
            }
        }catch(Exception ex){
             ex.printStackTrace();
        }
        db.close();
    }
    
    
    public static mx.smartteam.entities.ExhibicionCollection GetAll(Context context, Integer IdVisita, Integer IdCategoria, Integer IdMarca)throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
        mx.smartteam.entities.ExhibicionCollection exhibicionCollection = new mx.smartteam.entities.ExhibicionCollection();
        String qquery = null;
                     /*ifnull( s.i*d,0)*/ 
        qquery = " SELECT ec.id,e.nombre, ifnull(s.id,0) "
                + " FROM Exhibicion e "
                + " INNER JOIN ExhibicionConfig ec ON ec.IdExhibicion = e.id AND ec.activo = 1 AND ec.Idcategoria = "+ IdCategoria + " AND ec.idmarca = "+ IdMarca
                + " LEFT JOIN Sod s ON s.idExhibicionConfig = ec.id AND s.idvisita = "+ IdVisita +" ;";
        
        Cursor cursor = db.rawQuery(qquery, null);
        
        if(cursor.moveToFirst()) {
            do{
                mx.smartteam.entities.Exhibicion exhibicion = new mx.smartteam.entities.Exhibicion();
                exhibicion.Id = cursor.getInt(0);
                exhibicion.Nombre = cursor.getString(1);
                exhibicion.Bandera = cursor.getInt(2);
                if(exhibicion.Bandera > 0){
                    exhibicion.Bandera = 1;
                }
                exhibicionCollection.add(exhibicion);
            } while(cursor.moveToNext());
        }
        
        db.close();
        return exhibicionCollection;
    }

}
