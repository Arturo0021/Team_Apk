package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author ivan.guerra
 */
public class Areas_Insumos {
 
    public static mx.smartteam.entities.Areas_InsumosCollection DownloadAreas(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        mx.smartteam.entities.Areas_InsumosCollection collection = mx.smartteam.data.Areas_Insumos.DownloadAreas(context, proyecto);
        
        return collection;
    }
    
    public static mx.smartteam.entities.Areas_InsumosCollection getArea_Insumos(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        mx.smartteam.entities.Areas_InsumosCollection collection = mx.smartteam.data.Areas_Insumos.getArea_Insumos(context, proyecto);
        
        return collection;
    }
    
}
