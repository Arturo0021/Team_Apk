package mx.smartteam.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UsuarioProyecto {

    private static SQLiteDatabase db = null;

    public static void Insert(final Context context, final mx.smartteam.entities.UsuarioProyecto usuarioProyecto) throws Exception{
        db = (new BaseDatos(context)).getWritableDatabase();
        db.execSQL("INSERT INTO USUARIOPROYECTO"
                + " (IdUsuario,IdProyecto)"
                + " SELECT " + usuarioProyecto.IdUsuario + "," + usuarioProyecto.IdProyecto
                + " WHERE NOT EXISTS (SELECT 1 FROM USUARIOPROYECTO WHERE IdUsuario=" + usuarioProyecto.IdUsuario + " AND IdProyecto=" + usuarioProyecto.IdProyecto + ")");
        db.close();

    }

    public static mx.smartteam.entities.Proyecto GetProyectoByUsuario(final Context context, final mx.smartteam.entities.Usuario usuario) {

        mx.smartteam.entities.Proyecto entityProyecto = null;
        try {
            if (usuario != null) {
                db = (new BaseDatos(context)).getWritableDatabase();
                
                String query = "SELECT Id, Nombre "
                                    + " FROM PROYECTO AS p"
                                        + " INNER JOIN USUARIOPROYECTO AS up ON p.ID = up.IDPROYECTO "
                                + "WHERE up.IDUSUARIO = " + usuario.getId() + ";";
                
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    // Recorremos el cursor hasta que no haya más registros
                    do {
                        entityProyecto = new mx.smartteam.entities.Proyecto();
                        entityProyecto.Id = cursor.getInt(0);
                        entityProyecto.Nombre = cursor.getString(1);
                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            Log.i("Insert", e.getMessage());
        } finally {
            db.close();
        }

        return entityProyecto;
    }
}
