package mx.smartteam.business;

/**
 *
 * @author ivan.guerra
 */
public class TiendaExAdicional {

    public static mx.smartteam.entities.TiendaExAdicionalCollection DownloadExAdicional(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) throws Exception {
        
         if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        if(pop.DeterminanteGSP == null) {
            throw new Exception("Objeto Pop No Referenciado");
        }
        
        mx.smartteam.entities.TiendaExAdicionalCollection collection = mx.smartteam.data.TiendaExAdicional.DownloadExAdicional(proyecto, pop);
        
        return collection;
    }
    
}
