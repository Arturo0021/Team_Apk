/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DeleteTables{
     private static SQLiteDatabase db = null;
    
     public static void EliminarTablas(final Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception{
   db = (new BaseDatos(context)).getWritableDatabase();
 /*
 * Tablas que no se modifican

    Las tablas que se recetean:      

*/
 
       
       String q= ("Select * from Anaquel where StatusSync = 0 ");
       Cursor cursor= db.rawQuery(q, null);
       
       if (cursor != null){
            db.execSQL("DELETE FROM Anaquel");
            db.execSQL("DELETE FROM Bodega");
            db.execSQL("DELETE FROM ExhibicionAdicional");
            db.execSQL("DELETE FROM Foto");
            db.execSQL("DELETE FROM FotoOpcion");
            db.execSQL("DELETE FROM MaterialPromocional");
            db.execSQL("DELETE FROM PopVisita");
            db.execSQL("DELETE FROM RespuestaOpcion");
            db.execSQL("DELETE FROM respuestaSondeo");
            db.execSQL("DELETE FROM rutas");
            db.execSQL("DELETE FROM Marca");
            db.execSQL("DELETE FROM Formulario");
            db.execSQL("DELETE FROM Categoria");
            db.execSQL("DELETE FROM Opcion");
            db.execSQL("DELETE FROM Pop");
            db.execSQL("DELETE FROM Pregunta");
            db.execSQL("DELETE FROM PreguntaOpcion");
            db.execSQL("DELETE FROM Producto");
            db.execSQL("DELETE FROM ProductoCadena");
            db.execSQL("DELETE FROM Sondeo");
            db.execSQL("DELETE FROM configgps");
            db.execSQL("DELETE FROM Notificaciones");
            db.execSQL("DELETE FROM Opcion");
            db.execSQL("DELETE FROM Contestacion");
            db.execSQL("DELETE FROM ContestacionPregunta");
            db.execSQL("DELETE FROM ContestacionPreguntaOpcion");
            db.execSQL("DELETE FROM Sos");
            db.execSQL("DELETE FROM SeguimientoGps");
            db.execSQL("DELETE FROM categoriasconfig");
            db.execSQL("DELETE FROM marcaconfig");
//            db.execSQL("UPDATE COMSINCRO SET Download = null where IdProyecto = "+ proyecto.Id+" AND ");
            db.execSQL("UPDATE Regsincro SET Download = null");// WHERE IdUsuario = "+ usuario.Id+ " AND IdProyecto=  "+proyecto.Id+";"); 
            db.close();
       //Esta se usuaria si es que varios usuarios usaran el mimo dispositivo.     
       //DELETE FROM Anaquel where IdUsuario= IdUsuario
       }
       
    }

     
     public static void EliminarFotosVisita(final Context context, mx.smartteam.entities.PopVisita visita) throws Exception{
   db = (new BaseDatos(context)).getWritableDatabase();
 
       
       String q= ("Select * from Anaquel where StatusSync = 0 ");
       Cursor cursor= db.rawQuery(q, null);
       
       if (cursor != null){
            db.execSQL("DELETE FROM Anaquel");
            db.close();
       }
       
    }
     
     public static void comprobarSincronizacion(final Context contex) throws Exception {
     
     
     }


}
