package mx.smartteam.business;

import android.content.Context;

public class Notificaciones {

    public static class Download {

        public static mx.smartteam.entities.NotificacionesCollection GetNotificaciones(
                mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {
            try {

                if (proyecto == null) {
                    throw new Exception("Object proyecto no referenciado");
                }

                
                mx.smartteam.entities.NotificacionesCollection notificacionesCollection =
                        mx.smartteam.data.Notificaciones.Download.GetbyproyectoUsuario(proyecto, time);

                return notificacionesCollection;
            } catch (Exception e) {

                throw new Exception("GetNotificaciones", e);
            }
        }
        
        public static mx.smartteam.entities.NotificacionesCollection GetMensajesByUsuario(
                mx.smartteam.entities.Proyecto proyecto, String time,
                mx.smartteam.entities.Usuario usuario) throws Exception {
            try {

                if (proyecto == null) {
                    throw new Exception("Object proyecto no referenciado");
                }

                
                mx.smartteam.entities.NotificacionesCollection notificacionesCollection =
                        mx.smartteam.data.Notificaciones.Download.GetMensajesByUsuario(proyecto, time, usuario);

                return notificacionesCollection;
            } catch (Exception e) {

                throw new Exception("GetNotificaciones", e);
            }
        }
        
    }

    public static void Insert(Context context, mx.smartteam.entities.Notificaciones notificaciones) throws Exception {
        if (notificaciones == null) {
            throw new Exception("Object notificaciones no referenciado");
        }
        mx.smartteam.data.Notificaciones.Insert(context, notificaciones);
    }

    public static mx.smartteam.entities.NotificacionesCollection GetNotification(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.NotificacionesCollection notificacionesCollection = null;

        if (proyecto == null) {
            throw new Exception("objeto proyecto no referenciado");
        }

        notificacionesCollection = mx.smartteam.data.Notificaciones.GetbyNotificaciones(context, proyecto);
        if (notificacionesCollection.size() == 0) {
            //No hay mensajes
        }
        return notificacionesCollection;
    }
}
