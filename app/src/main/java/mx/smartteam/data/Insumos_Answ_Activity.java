package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.entities.Respuestas_Insumos;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author ivan.guerra
 */
public class Insumos_Answ_Activity {
        
    private static SQLiteDatabase db = null;
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;
    
    public static void InsertRespuestas(Context context, mx.smartteam.entities.Insumos insumos, mx.smartteam.entities.Usuario usuario,mx.smartteam.entities.Pop pop, String Respuesta) throws Exception{
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        String query = "SELECT COUNT(1) FROM " + BaseDatos.TABLE_RESPUESTA_INSUMOS
                            + " WHERE " + BaseDatos.COLUMN_IDCONFIG + " = " +  insumos.getIdConfig()
                                + " AND " + BaseDatos.COLUMN_IDUSUARIO + " = " + usuario.getId()
                                + " AND " + BaseDatos.COLUMN_DETERMINANTE + " = " + pop.getDeterminanteGSP() 
                                + " AND " + BaseDatos.COLUMN_IDVISITA + " = " + pop.getIdVisita() + ";";
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
            int existe = cursor.getInt(0);
            
            if(existe == 0) { // Inserta
                
                String Insert = "INSERT INTO " + BaseDatos.TABLE_RESPUESTA_INSUMOS   
                                    + "("
                                        + BaseDatos.COLUMN_IDCONFIG + ", "
                                        + BaseDatos.COLUMN_IDUSUARIO + ", "
                                        + BaseDatos.COLUMN_RESPUESTA + ", "
                                        + BaseDatos.COLUMN_DETERMINANTE + ", "
                                        + BaseDatos.COLUMN_IDVISITA
                                    + ") VALUES ("
                                        + insumos.getIdConfig() + ", "
                                        + usuario.getId() + ", "
                                        + "'" + Respuesta + "', "
                                        + pop.getDeterminanteGSP() + ", "
                                        + pop.getIdVisita()
                                    + ");";
                
                db.execSQL(Insert);
            } else { // Actualiza
                
                String actualiza = "UPDATE " + BaseDatos.TABLE_RESPUESTA_INSUMOS
                                        + " SET " + BaseDatos.COLUMN_RESPUESTA + " = '" + Respuesta + "'"
                                    + " WHERE " + BaseDatos.COLUMN_IDCONFIG + " = " + insumos.getIdConfig()
                                        + " AND " + BaseDatos.COLUMN_IDUSUARIO + " = " + usuario.getId()
                                        + " AND " + BaseDatos.COLUMN_DETERMINANTE + " = " + pop.getDeterminanteGSP() 
                                        + " AND " + BaseDatos.COLUMN_IDVISITA + " = " + pop.getIdVisita() 
                                        + " AND " + BaseDatos.COLUMN_ESTATUS + " = 0;";
                db.execSQL(actualiza);
            }
            
        }
        
    }
    
    public static void getAnswer(Context context) {
        
        db = (new BaseDatos(context)).getWritableDatabase();
        String query = "SELECT " 
                            + BaseDatos.COLUMN_IDCONFIG + ", "
                            + BaseDatos.COLUMN_IDUSUARIO + ", "
                            + BaseDatos.COLUMN_RESPUESTA + ", "
                            + "DATETIME(" + BaseDatos.COLUMN_FECHA + ", 'unixepoch', 'localtime'), "
                            + BaseDatos.COLUMN_DETERMINANTE + ", "
                            + BaseDatos.COLUMN_IDVISITA
                        + " FROM " + BaseDatos.TABLE_RESPUESTA_INSUMOS
                            + " WHERE " + BaseDatos.COLUMN_ESTATUS + " = 0";
        Cursor cursor = db.rawQuery(query, null);
        
        if(cursor.moveToFirst()) {
            
            do {
                
                mx.smartteam.entities.Respuestas_Insumos insumo = new Respuestas_Insumos();
                insumo.setIdConfig(cursor.getInt(0));
                insumo.setIdUsuario(cursor.getInt(1));
                insumo.setRespuesta(cursor.getString(2));
                insumo.setFecha(cursor.getString(3));
                insumo.setDeterminante(cursor.getInt(4));
                insumo.setIdVisita(cursor.getInt(5));
                
                try{
                    
                    uploadAnswerToCloud(insumo);
                    
                } catch(Exception e) {
                    e.getMessage();
                }
                
            } while(cursor.moveToNext());
            
        }
        
    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop){
        
        db = (new BaseDatos(context)).getWritableDatabase();
        int existe = 0;
        
        String query = "SELECT COUNT(1) FROM " + BaseDatos.TABLE_RESPUESTA_INSUMOS 
                            + " WHERE " + BaseDatos.COLUMN_IDVISITA + " = " + pop.getIdVisita() + ";";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            existe = cursor.getInt(0);
        }
        
        return existe;
    }
    
    public static void uploadAnswerToCloud(mx.smartteam.entities.Respuestas_Insumos insumo) throws Exception{
        
        ServiceURI wcf = new ServiceURI();
        METHOD_NAME = "/InsertRespuestaInsumo";
        
                jsonString = new JSONStringer()
                    .object()
                        .key("RespuestaInsumo")
                            .object()
                                .key("IdConfiguracion").value(insumo.getIdConfig())
                                .key("Respuesta").value(insumo.getRespuesta())
                                .key("Fecha").value(insumo.getFecha())
                            .endObject()
                        .key("Usuario")
                            .object()
                                .key("Id").value(insumo.getIdUsuario())
                            .endObject()
                        .key("Tienda")
                            .object()
                                .key("DeterminanteGSP").value(insumo.getDeterminante())
                            .endObject()
                    .endObject();
        strEntity = new StringEntity(jsonString.toString(),"UTF-8");
        jsonResult = wcf.HttpGet(METHOD_NAME, strEntity);
        
        if (!jsonResult.getString("InsertRespuestaInsumoResult").equals("OK")) {
            throw new Exception("Error el el servicio [InsertRespuestaInsumo]");
        } else {
            
            String update = "UPDATE " + BaseDatos.TABLE_RESPUESTA_INSUMOS
                                +  " SET " + BaseDatos.COLUMN_ESTATUS + " = 1" 
                            + " WHERE " + BaseDatos.COLUMN_IDCONFIG + " = " + insumo.getIdConfig()
                                + " AND " + BaseDatos.COLUMN_IDVISITA + " = " + insumo.getIdVisita() + ";";
            db.execSQL(update);
            
        }
        
    }
    
}
