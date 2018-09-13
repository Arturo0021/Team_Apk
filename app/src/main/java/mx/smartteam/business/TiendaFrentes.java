package mx.smartteam.business;

/**
 *
 * @author ivan.guerra
 */
public class TiendaFrentes {
    
    public static mx.smartteam.entities.TiendaFrentesCollection DownloadFrentes(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) throws Exception {
        
         if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        if(pop.DeterminanteGSP == null) {
            throw new Exception("Objeto Pop No Referenciado");
        }
        
        mx.smartteam.entities.TiendaFrentesCollection collection = mx.smartteam.data.TiendaFrentes.DownloadFrentes(proyecto, pop);
        
        return collection;
    }
    
}
