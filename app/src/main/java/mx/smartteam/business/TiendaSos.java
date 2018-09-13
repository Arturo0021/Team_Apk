package mx.smartteam.business;

/**
 *
 * @author ivan.guerra
 */
public class TiendaSos {

    public static mx.smartteam.entities.TiendaSosCollection DownloadSos(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) throws Exception{
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        if(pop.DeterminanteGSP == null) {
            throw new Exception("Objeto Pop No Referenciado");
        }
        
        mx.smartteam.entities.TiendaSosCollection collection = mx.smartteam.data.TiendaSos.DownloadSos(proyecto, pop);
        
        return collection;
    }
    
}
