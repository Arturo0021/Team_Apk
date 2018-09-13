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
public class UsuarioProyecto {
    
    
    public static void Insert(Context context, mx.smartteam.entities.UsuarioProyecto usuarioProyecto) throws Exception{
    
        if (usuarioProyecto==null) {
            throw new Exception("Object [usuarioProyecto] no referenciado");
        }
        
        mx.smartteam.data.UsuarioProyecto.Insert(context, usuarioProyecto);
        
    
    }
    
    
}
