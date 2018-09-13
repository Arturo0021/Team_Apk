package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author ivan.guerra
 */
public class Insumos_Config {
 
    public static mx.smartteam.entities.Insumos_ConfigCollection DownloadConfig(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        mx.smartteam.entities.Insumos_ConfigCollection collection = mx.smartteam.data.Insumos_Config.DownloadConfig(context, proyecto);
        
        return collection;
    }
    
}
