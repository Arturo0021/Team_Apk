
package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mx.smartteam.exceptions.UsuarioException;
import mx.smartteam.exceptions.UsuarioException.LogException;

public class Log {
    
    private static SQLiteDatabase db = null;
    
    public static void Insert( Context context,mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception{

                db = (new BaseDatos(context)).getWritableDatabase();
                db.execSQL("INSERT INTO Log"
                + " (UsuarioId,ProyectoId) VALUES ("
                + usuario.Id+ ","
                + proyecto.Id +
                " );");

                db.close();
            
    }
    
        public  static mx.smartteam.entities.Log GetLog(Context context) throws LogException{
            
            mx.smartteam.entities.Log log = null;
                    
                    log =  new mx.smartteam.entities.Log();      
            
            db = ( new BaseDatos(context)).getWritableDatabase()  ;
            try
            {
                Cursor cursor = db.rawQuery( "select Id, ProyectoId, UsuarioId from Log order by Fecha desc limit 1", null );
            
                if(cursor.moveToFirst())
                {
                    do{
                  
                        log.LogId = cursor.getInt(0);
                        log.ProyectoId = cursor.getInt(1);
                        log.UsuarioId = cursor.getInt(2);
                  
                    }while(cursor.moveToNext());
                }
            }
            catch(Exception ex)
            {
                    throw new UsuarioException.LogException();
            }
            db.close();
            return log;
        }


}
