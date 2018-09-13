package mx.smartteam.business;

/**
 *
 * @author ivan.guerra
 */
public class TiendasConfig {

    public static mx.smartteam.entities.TiendasConfigCollection DownloadConfig(mx.smartteam.entities.Proyecto proyecto) throws Exception {
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        mx.smartteam.entities.TiendasConfigCollection collection = mx.smartteam.data.TiendasConfig.DownloadConfig(proyecto);
        
        return collection;
    }
    
}
