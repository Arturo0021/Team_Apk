/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author jaime.bautista
 */
public class Rutas {

    public static class Download {
        
        public static mx.smartteam.entities.RutasCollection GetRutas(
                mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time
                ) throws Exception{
            try {
                
                if(proyecto == null){
                    throw new Exception("Object proyecto no referenciado");
                }
                
                if(usuario == null){
                throw new Exception("Object usuario no referenciado");
                }
                
                mx.smartteam.entities.RutasCollection rutasCollection = mx.smartteam.data.Rutas.Download.GetByproyectoUsuario(proyecto, usuario, time);
                
                
                  return rutasCollection;
                
            } catch (Exception e) {
            
            throw new Exception("GetRutas", e);}
        }
    
    }
    
    public static void Insert(Context context, mx.smartteam.entities.Rutas rutas) throws Exception{
    
        if(rutas == null ){
            throw new Exception("Object rutas no referenciado");
            
        }
        mx.smartteam.data.Rutas.Insert(context, rutas);
    }
    
    public static mx.smartteam.entities.PopCollection GetByRutasToday
            (Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) 
            throws Exception{
       
        
        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }
        
        mx.smartteam.data.Rutas.GetByRutasToday(context, usuario, proyecto);
        
        return null;
    }
    
}
