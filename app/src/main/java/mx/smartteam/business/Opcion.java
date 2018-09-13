/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author edgarin.lara
 */
public class Opcion {

    public static void Insert(Context context, mx.smartteam.entities.Opcion opcion) throws Exception {

        if (opcion == null) {
            throw new Exception("Object [opcion] no referenciado");
        }
        mx.smartteam.data.Opcion.Insert(context, opcion);
    }

    public static mx.smartteam.entities.OpcionCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
        mx.smartteam.entities.OpcionCollection opcionCollection = null;

        if (proyecto != null) {
            opcionCollection = mx.smartteam.data.Opcion.GetByProyecto(context, proyecto);
        }
        return opcionCollection;
    }

    public static class Download {

        public static mx.smartteam.entities.OpcionCollection GetOpcionesFoto(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time ) throws Exception {
            mx.smartteam.entities.OpcionCollection opcionCollection = null;

            if (proyecto != null && usuario != null) {
                opcionCollection = mx.smartteam.data.Opcion.Download.GetOpcionesFoto(proyecto, usuario, time);
            }

            return opcionCollection;
        }
    }
}
