
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.exceptions.UsuarioException.LogException;

public class Log {
    
    public static void Insert
            (Context context, 
            mx.smartteam.entities.Usuario usuario, 
            mx.smartteam.entities.Proyecto proyecto)
            throws Exception  
    {
    if(usuario == null){
        throw  new Exception ("Object Usuario no referenciado");
    }
    if(proyecto == null){
        throw new Exception("Object Proyeccto no referenciado");
    }
        
    mx.smartteam.data.Log.Insert(context, usuario, proyecto);

    }
    

    public static mx.smartteam.entities.Log GetLog(Context context) throws LogException{
    
        mx.smartteam.entities.Log log = null;
        
        log = mx.smartteam.data.Log.GetLog(context);
        
       return log;
    }
    
    

}
