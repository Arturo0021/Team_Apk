/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import android.util.Log;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.ProyectoCollection;
import mx.smartteam.exceptions.UsuarioException;

/**
 *
 * @author edgarin.lara
 */
public class Proyecto {

    public static mx.smartteam.entities.ProyectoCollection GetByUsuario(mx.smartteam.entities.Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Objeto Usuario Null");
        }

        mx.smartteam.entities.ProyectoCollection proyectoCollection = new ProyectoCollection();
        proyectoCollection = mx.smartteam.data.Proyecto.Sinc_GetByUsuario(usuario);
        if (proyectoCollection.size() == 0) {
            throw new Exception("El usuario no esta asignado a un proyecto, verifique por favor");
        }



        return proyectoCollection;

    }

public static void Insert(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{

        
            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");       
            }            
            mx.smartteam.data.Proyecto.Insert(context, proyecto);

    }
}
