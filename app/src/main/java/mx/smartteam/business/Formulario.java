/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.FormularioCollection;
import mx.smartteam.entities.SondeoModulosCollection;

/**
 *
 * @author edgarin.lara
 */
public class Formulario {

    public static class Download {

        public static mx.smartteam.entities.FormularioCollection GetByProyectoAndUsuario(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time) throws Exception {
            try {
                if (proyecto == null) {
                    throw new Exception("Object proyecto no referenciado");
                }
                if (usuario == null) {
                    throw new Exception("Object usuario no referenciado");
                }
                mx.smartteam.entities.FormularioCollection formularioCollection = mx.smartteam.data.Formulario.Download.GetByProyectoUsuario(proyecto, usuario, time);
                return formularioCollection;

            } catch (Exception e) {
                throw new Exception("GetByProyectoAndUsuario", e);
            }
        }
    }

    public static void Insert(final Context context,
            final mx.smartteam.entities.Formulario formulario) throws Exception {

        if (formulario == null) {
            throw new Exception("Objet formulario no referenciado");
        }

        mx.smartteam.data.Formulario.Insert(context, formulario);

    }

    public static mx.smartteam.entities.FormularioCollection GetByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
        
        if(proyecto == null){
            throw new Exception("Objet formulario no referenciado");
        }
        
        FormularioCollection formularioCollection = mx.smartteam.data.Formulario.GetByProyecto(context, proyecto);
        return formularioCollection;
    }
    
    public static mx.smartteam.entities.SondeoModulosCollection GetByProyectosm(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
        
        if(proyecto == null){
            throw new Exception("Objet formulario no referenciado");
        }
        
        SondeoModulosCollection sondeoModulosCollection = mx.smartteam.data.Formulario.GetByProyectosm(context, proyecto);
        return sondeoModulosCollection;
    }

    public static mx.smartteam.entities.SondeoModulosCollection GetFlagFoto(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
        if(proyecto == null){
            throw new Exception("Objet formulario no referenciado");
        }
        
        SondeoModulosCollection sondeoModulosCollection = mx.smartteam.data.Formulario.GetFlagFoto(context, proyecto);
        
        return sondeoModulosCollection;
    }
    
    
    
    
    
}
