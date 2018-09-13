
package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.triplei.Utilerias;
import mx.smartteam.entities.JobCollection;
import mx.smartteam.exceptions.UsuarioException;
import mx.smartteam.exceptions.UsuarioException.JobException;


public class Job {
    
    private static SQLiteDatabase db = null;
    
    public static void Insert(Context context){
        
        db = (new BaseDatos(context)).getWritableDatabase();
        
        db.execSQL("INSERT INTO Job (Fecha) Values (" + Utilerias.SumaFechaUnix()+");");
        
        db.close();
    }
    
    
    public static mx.smartteam.entities.JobCollection  GetbyJob(Context context) throws JobException{
        
        mx.smartteam.entities.JobCollection jobCollection = new JobCollection();
        db = (new BaseDatos(context)).getWritableDatabase(); 
        long fecha =  Utilerias.getFechaUnix() ;
        
        try{
                Cursor cursor = db.rawQuery("SELECT  Id, datetime(Fecha, 'unixepoch','localtime') as fecha from job "
                        + " WHERE " 
                         +" datetime(Fecha, 'unixepoch', 'localtime' ) >= datetime('"+ fecha + "', 'unixepoch', 'localtime' ) order by ID desc limit 1" ,   null);
                      if(cursor.moveToFirst()){
                      do{
                          mx.smartteam.entities.Job job = new mx.smartteam.entities.Job();
                          job.Id = cursor.getInt(0);
                          jobCollection.add(job);
 
                      }while(cursor.moveToNext());
                      } 
            
            }catch(Exception e){
            
                throw  new UsuarioException.JobException();
                
            }
            db.close();
    return jobCollection;
    }
    
      public static void  BorrarRegistros(Context context) throws JobException {
         db = (new BaseDatos(context)).getWritableDatabase();

         
         try{
                    Cursor cursor = db.rawQuery("Select * From Job limit 1",    null);
                    if(cursor.moveToFirst()){
                      do{
                                db.execSQL("DELETE FROM Anaquel Where StatusSync = 1");
                                db.execSQL("DELETE FROM Bodega  Where StatusSync = 1");
                                db.execSQL("DELETE FROM Contestacion Where StatusSync = 1");
                                db.execSQL("DELETE FROM ContestacionPregunta Where StatusSync = 1");
                                db.execSQL("DELETE FROM ExhibicionAdicional Where StatusSync = 1");
                                db.execSQL("DELETE FROM Foto Where StatusSync = 1");
                                db.execSQL("DELETE FROM MaterialPromocional Where StatusSync = 1");
                        }while(cursor.moveToNext());
                      }
                    
         }catch (Exception e){
         throw new UsuarioException.JobException();
         
         }
        db.close(); 
     }   
    
 
}
