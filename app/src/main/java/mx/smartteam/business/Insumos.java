package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author ivan.guerra
 */
public class Insumos {
    
    public static mx.smartteam.entities.InsumosCollection DownloadInsumos(Context context,mx.smartteam.entities.Proyecto proyecto) throws Exception{
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        mx.smartteam.entities.InsumosCollection collection = mx.smartteam.data.Insumos.DownloadInsumos(context, proyecto);
        
        return collection;
    }
    
    public static mx.smartteam.entities.InsumosCollection getInsumos(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Areas_Insumos areas_Insumos, mx.smartteam.entities.Usuario usuarios,mx.smartteam.entities.Pop pop) throws Exception {
        
        if(proyecto.getId() == null) {
            throw new Exception("Objecto Proyecto No Referenciado - getInsumos");
        }
        
        if(areas_Insumos.getId() == null) {
            throw new Exception("Objecto Area_Insumos No Referenciado - getInsumos");
        }
        
        mx.smartteam.entities.InsumosCollection collection = mx.smartteam.data.Insumos.getInsumos(context, proyecto, areas_Insumos, usuarios, pop);
        
        return collection;
    }
    
}
