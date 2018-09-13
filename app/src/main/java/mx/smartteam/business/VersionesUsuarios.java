
package mx.smartteam.business;

import android.content.Context;

public class VersionesUsuarios {
    
    public static class Download{
    
        public static mx.smartteam.entities.VersionUsuarioCollection GetVersionesUsuarios(mx.smartteam.entities.Version version, mx.smartteam.entities.Usuario usuario) throws Exception{
            if(version == null){
                throw new Exception("Objeto version no referenciado");
            }
            
            if(usuario == null){
                throw new Exception("Objeto usuario no referenciado");
            }
            
            mx.smartteam.entities.VersionUsuarioCollection versionUsuarioCollection = mx.smartteam.data.VersionesUsuarios.Download.GetByVersionXUsuario(version, usuario);
            return versionUsuarioCollection;
        }
    
    }
    
    public static void Insert(Context context, mx.smartteam.entities.VersionUsuario versionUsuario) throws Exception {
        if(versionUsuario == null){
            throw new Exception("Objeto VersionUsuario no referenciado");
        }
        mx.smartteam.data.VersionesUsuarios.Insert(context, versionUsuario);
    }
    
    public static mx.smartteam.entities.VersionUsuario getVersionUsuario(Context context)throws Exception {
        mx.smartteam.entities.VersionUsuario versionUsuario = null;
        versionUsuario = mx.smartteam.data.VersionesUsuarios.getVersionUsuario(context);
        return versionUsuario;
    }
   
}
