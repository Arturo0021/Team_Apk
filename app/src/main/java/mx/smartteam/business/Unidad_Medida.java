package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author ivan.guerra
 */
public class Unidad_Medida {
    
    public static mx.smartteam.entities.Unidad_MedidaCollection DownloadUnidad(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        if(proyecto.getId() == null) {
            throw new Exception();
        }
        
        mx.smartteam.entities.Unidad_MedidaCollection collection = mx.smartteam.data.Unidad_Medida.DownloadUnidad(context, proyecto);
        
        return collection;
    }
    
}
