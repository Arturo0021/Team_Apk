/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.triplei.GlobalSettings;
import mx.triplei.Utilerias;
import mx.smartteam.exceptions.UsuarioException;
import mx.smartteam.exceptions.UsuarioException.PassEmpty;
import android.database.Cursor;
import android.util.Log;

/**
 *
 * @author jaime.bautista
 */
public class UtilSincronizacion {
    private static SQLiteDatabase db = null;
    
    
    public static void UpdateSincro(Context context,  mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception {

        db = (new BaseDatos(context)).getWritableDatabase();
        
        db.execSQL("INSERT INTO Regsincro (Download, IdProyecto, IdUsuario ) VALUES( "+Utilerias.getFechaUnix()+", "+proyecto.Id + ", "+usuario.Id+" );");
        
        //db.execSQL("UPDATE COMSINCRO SET Download = strftime('%s','now') WHERE IdUsuario =  AND IdProyecto=  "+proyecto.Id+";");   
        db.close();
    }
    
    
    public static mx.smartteam.entities.consicCollection SendLastDownload(final Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario ) throws Exception{         
       //mx.smartteam.entities.VersionCollection versionCollection = new VersionCollection();
        
        
        mx.smartteam.entities.consicCollection consicCollection = new mx.smartteam.entities.consicCollection();
        db = (new BaseDatos(context)).getWritableDatabase();
        
        try{
        Cursor cursor = db.rawQuery("SELECT"
                + " MAX(Id), Fechaactual,Upload,datetime(Download, 'unixepoch','localtime') as UFechaDescarga, IdProyecto, IdUsuario FROM Regsincro where   "
                + "datetime(Download, 'unixepoch','localtime') > 0 AND IdProyecto="+proyecto.Id+"   AND IdUsuario ="+usuario.Id+" ; ", null);        
             if (cursor.moveToFirst()) {
                do {
                    mx.smartteam.entities.cosinc consic = new mx.smartteam.entities.cosinc();
                    consic.id = cursor.getInt(0);
                    consic.Fechaactual = cursor.getString(1);
                    consic.fechasincronizacion = cursor.getString(2);
                    consic.UFechaDescarga= cursor.getString(3);
                    if (consic.UFechaDescarga == null){
                    
                    consic.UFechaDescarga = "0";
                    }
                    
                    consic.Idproyecto = cursor.getInt(4);
                    consic.Idusuario = cursor.getInt(5);
                                        
                    consicCollection.add(consic);
                    
                   
                 } while (cursor.moveToNext());
            }
        
             if(consicCollection.size() == 0){
            mx.smartteam.entities.cosinc consic = new mx.smartteam.entities.cosinc();
           consic.Fechaactual= "0";
           
           consic.UFechaDescarga = "0";
           //consic.Fechaactual ="0";
           consicCollection.add(consic);
       }
             
             
             
        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }
        
        return consicCollection;
        
    }   
    public static void CompareDownload(Context context) throws Exception {                                               
        db = (new BaseDatos(context)).getWritableDatabase();
        Cursor t;
        t = db.rawQuery("SELECT datetime(Download, 'unixepoch', 'localtime' ) , MAX(Id)  FROM Regsincro where datetime"
                + "(Download, 'unixepoch', 'localtime' ) Like '%" + Utilerias.getFecha() + "%'", null);
 
         if (t.getCount()==0){
            // Si no hay fecha de hoy mostrar mensaje de que debe descargar
            throw new mx.smartteam.exceptions.GpsException.Inactivo();         
         }
    }

}
