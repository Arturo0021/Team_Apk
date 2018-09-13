package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author jaime.bautista
 */
public class MovilConfig {
    
    
    public static mx.smartteam.entities.MovilConfigCollection GetByMovilConfig( mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto)throws Exception{
        
        if(usuario == null){
        throw new Exception("Object usuario no referenciado");
        }
        if(proyecto == null){
        
        throw new Exception("Object proyecto no referenciado");
        }
        mx.smartteam.entities.MovilConfigCollection movilConfigCollection = mx.smartteam.data.MovilConfig.Download.GetByMovilConfig(usuario, proyecto);
        return movilConfigCollection;
    }
    
    
    
    public static mx.smartteam.entities.MovilConfigCollection GetByGpsConfig(final Context context, mx.smartteam.entities.Usuario usuario) throws Exception{
    
        if(usuario == null){
       throw new Exception("objeto usuario no referenciado");
        
        }
       mx.smartteam.entities.MovilConfigCollection movilConfigCollection =mx.smartteam.data.MovilConfig.GetByGpsConfig(context, usuario);
        return movilConfigCollection;
    }
    
    
    public static mx.smartteam.entities.MovilConfig GetConfig(final Context context, mx.smartteam.entities.Usuario usuario) throws Exception
    {
        if(usuario == null){
                throw new Exception("objeto usuario no referenciado");
        }
        mx.smartteam.entities.MovilConfig movilConfig =mx.smartteam.data.MovilConfig.GetConfig(context, usuario);
        return movilConfig;
    } 
    
}
