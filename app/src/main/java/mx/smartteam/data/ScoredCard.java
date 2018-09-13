
package mx.smartteam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import mx.triplei.Utilerias;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ScoredCard {
    
    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static SQLiteDatabase db = null;
        
    public static class Download {
    
        public static mx.smartteam.entities.ScoredCardCollection getAllCalificacionesDiarias(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String Time)
            throws JSONException, UnsupportedEncodingException, IOException{
            METHOD_NAME = "/GetReporteTiendas";
            ServiceURI service = new ServiceURI();
            mx.smartteam.entities.ScoredCardCollection scoredCardCollection = new mx.smartteam.entities.ScoredCardCollection();  
            
                     jsonString = new JSONStringer().object()
                    .key("Proyecto").object()
                        .key("Id").value(proyecto.Id.toString())
                        .key("Ufechadescarga").value(Time).endObject()
                    .key("Usuario").object()
                        .key("Id").value(usuario.Id).endObject()
                    .endObject();
            
            strEntity = new StringEntity(jsonString.toString());
            
            jsonResult = service.HttpGet(METHOD_NAME, strEntity);

            jsonArray = jsonResult.getJSONArray("GetReporteTiendasResult");

            for(int i = 0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mx.smartteam.entities.ScoredCard scard = new mx.smartteam.entities.ScoredCard();
                scard.Registros = jsonObject.getInt("conteo");
                scard.IdCadena = jsonObject.getInt("idCadena");
                scard.Fecha = jsonObject.getLong("fecha");
                scard.determinanteGSP = jsonObject.getInt("determinanteGSP");
                scard.IdProyecto = proyecto.Id;
                scard.IdUsuario = usuario.Id;
              
               
            scoredCardCollection.add(scard);
            }

            return scoredCardCollection;
        }
    }  
     
    public static void Insert(Context context, mx.smartteam.entities.ScoredCard scored){
       
        db = (new BaseDatos(context)).getWritableDatabase();
    
        String qquery = "SELECT count(1) as cont FROM ScoredCard WHERE "
                + " IdProyecto = " + scored.IdProyecto.toString()
                + " AND IdUsuario = " + scored.IdUsuario.toString()
                + " AND IdCadena = " + scored.IdCadena.toString()
                + " AND determinanteGSP = " + scored.determinanteGSP.toString()
                + " AND date(Fecha,'unixepoch') = date('" + scored.Fecha + "','unixepoch') "
                ;
        
        Cursor cursor  = db.rawQuery(qquery, null);
        if(cursor.moveToFirst()){
            int contador = cursor.getInt(0);
            if(contador ==0){
                db.execSQL("INSERT INTO ScoredCard "
                        + "(idproyecto,idusuario,idCadena,Registros,Fecha,determinantegsp) VALUES("
                        + scored.IdProyecto + " , "
                        + scored.IdUsuario + " , "
                        + scored.IdCadena +" , "
                        + scored.Registros +" , "
                        + scored.Fecha + " , "
                        + scored.determinanteGSP
                        + ");" 
                );
            }else{ /*
                String qquery2 ="";
                qquery2 = "UPDATE ScoredCard SET "
                + " Registros = " + scored.Registros.toString()
                + " WHERE "        
                + " idproyecto = " + scored.IdProyecto.toString()
                + " AND idusuario = " + scored.IdUsuario.toString() 
                + " AND idCadena = " + scored.IdCadena.toString()
                + " AND date(Fecha,'unixepoch') = date('" + scored.Fecha + "','unixepoch')";
                */
               ContentValues values = new ContentValues();
     
                values.put("Registros", scored.Registros.toString());
                db.update("ScoredCard ",values, " idproyecto = " + scored.IdProyecto.toString()
                                +" AND idusuario = " + scored.IdUsuario.toString() 
                                + " AND idCadena = " + scored.IdCadena.toString()
                                + " AND determinanteGSP = " + scored.determinanteGSP.toString()
                                + " AND date(Fecha,'unixepoch') = date('" + scored.Fecha + "','unixepoch')"
                        , null);

            }
        }
       
    
    db.close();
    }
    
    public static mx.smartteam.entities.ScoredCardCollection 
        getAllMonth(Context context, 
        mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, int bandera){
            db = (new BaseDatos(context)).getWritableDatabase();
            
            String xfecha;
            xfecha = Utilerias.getFecha();
            String[] parts = xfecha.split("-");
            int part1 = Integer.parseInt(parts[0]); // anio
            int part2 = Integer.parseInt(parts[1]); // mes
            String smes;
            if(part2 <= 10){
                smes = "0"+part2; 
            }else{
            smes = ""+part2+"";
            }
            
            String fi = " AND DATE(s.FECHA,'unixepoch') >= strftime('%Y-%m-%d', '"+ part1+"-"+smes+"-01')";
            String ff = " AND DATE(s.FECHA,'unixepoch') <= strftime('%Y-%m-%d', '"+ part1+"-"+smes+"-"+ bandera+"')";        
            mx.smartteam.entities.ScoredCardCollection scoredCardCollection = new mx.smartteam.entities.ScoredCardCollection();  
        
            String qquery = 
                "Select DATE(s.FECHA,'unixepoch') AS nFecha , p.cadena, s.registros \n" +
                "  , pp.ponderacion, p.sucursal " +
                " FROM Scoredcard s " +
                " INNER JOIN pop p ON p.idcadena = s.idcadena AND p.idproyecto = s.idproyecto AND s.determinantegsp = p.determinantegsp \n" +
                " INNER JOIN Ponderaciones pp on pp.idproyecto = s.idproyecto AND pp.idcadena = s.idcadena\n" +
                " WHERE  s.idproyecto = "+ proyecto.Id + " AND s.idusuario =  " + usuario.Id + /* falta la fecha */
                fi +ff+
                " GROUP BY s.idproyecto, s.idusuario, s.idcadena, s.determinanteGSP , DATE(s.FECHA,'unixepoch') " +
                " ORDER BY s.fecha ASC ";
            
        Cursor cursor  = db.rawQuery(qquery, null);
        if(cursor.moveToFirst()){
            do {
                mx.smartteam.entities.ScoredCard scored = new mx.smartteam.entities.ScoredCard();
                scored.extra = cursor.getString(0);
                scored.nombre = cursor.getString(1);
                scored.Registros = cursor.getInt(2);
                scored.ponderacion = cursor.getInt(3);
                scored.sucursal = cursor.getString(4);
                        
                scoredCardCollection.add(scored);
                        
            } while(cursor.moveToNext());
        }
        return scoredCardCollection;
   }
    
    
    
    
}




























