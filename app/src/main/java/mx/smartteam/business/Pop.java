/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.data.BaseDatos;

/**
 *
 * @author edgarin.lara
 */
public class Pop {
    
    public static class Download {
        
        public static mx.smartteam.entities.PopCollection GetByProyecto(mx.smartteam.entities.Proyecto proyecto, String time, mx.smartteam.entities.Usuario usuario) throws Exception {
            
            try {
                
                if (proyecto == null) {
                    throw new Exception("Objeto [proyecto] no referenciado");
                }
                if(usuario == null){
                    throw  new Exception("Objeto [user] no referenciado");
                }
                 
                mx.smartteam.entities.PopCollection popCollection = mx.smartteam.data.Pop.Sinc_GetByProyecto(proyecto,time,usuario);
                
//                if (popCollection.size() == 0) {
//                    throw new Exception("El proyecto no cuenta con tiendas registradas");
//                }
                return popCollection;
            } catch (Exception e) {
                throw new Exception("GetByPop", e);
            }
        }
    }
    
    public static void Insert(final Context context, final mx.smartteam.entities.Pop pop,
                    mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto
    ) throws Exception {
        
        if (pop == null) {
            throw new Exception("Object pop no referenciado");
        }
        
        mx.smartteam.data.Pop.Insert(context, pop, usuario, proyecto);
    }

    public static int ValidarFechaVisita(final Context context, mx.smartteam.entities.Pop pop) throws Exception {
        if (pop == null) {
            throw new Exception("Object pop no referenciado");
        }
        return mx.smartteam.data.Pop.ValidarFechaVisita(context, pop);
    }
    
    public static void GetAll() {
    }
}
