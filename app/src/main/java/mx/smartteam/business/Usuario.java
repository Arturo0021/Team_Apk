/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

/**
 *
 * @author edgarin.lara
 */
import android.content.Context;
import android.util.Log;
import mx.smartteam.data.*;
import mx.smartteam.entities.*;
import mx.smartteam.exceptions.*;

public class Usuario {

    public static mx.smartteam.entities.Usuario Autentication(mx.smartteam.entities.Usuario usuario) throws Exception {
        mx.smartteam.entities.Usuario EntityUsuario = null;

        if (usuario.Usuario == null || usuario.Usuario == "") {
            throw new UsuarioException.UserEmpty();
        }

        if (usuario.Password == null || usuario.Password == "") {
            throw new UsuarioException.PassEmpty();
        }


        EntityUsuario = mx.smartteam.data.Usuario.Sinc_Autenticar(usuario.Usuario, usuario.Password);

        if (EntityUsuario == null) {
            throw new UsuarioException.ErrorAutenticar();
        }



        return EntityUsuario;
    }

    public static void Insert(Context context, mx.smartteam.entities.Usuario usuario) throws Exception {


        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        mx.smartteam.data.Usuario.Insert(context, usuario);


    }
    
    public static mx.smartteam.entities.Usuario GetByUsuario2(Context context, String usuario) throws Exception {
       
        mx.smartteam.entities.Usuario usuario1 = new mx.smartteam.entities.Usuario();
        
        if(usuario == null){
            throw new Exception("Objeto Usuario No Referenciado");
        }
        
         usuario1 = mx.smartteam.data.Usuario.GetByUsuario2(context, usuario);
         
        return usuario1;
    }
    
    public static void UpdateDevices(Context context, mx.smartteam.entities.Usuario usuario) throws Exception{
        
        if(usuario.Id == null) {
            throw new Exception("Objeto Usuario No Referenciado");
        }
        
        mx.smartteam.data.Usuario.UpdateDevices(context, usuario);
        
    }
    
}
