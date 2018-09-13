package mx.smartteam.business;

/**
 *
 * @author ivan.guerra
 */
public class TiendasAsistencia {

    public static mx.smartteam.entities.TiendaAsistenciaCollection DownloadAsistencia(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop) throws Exception {
        
        if(proyecto.getId() == null) {
            throw new Exception("Objeto Proyecto No Referenciado");
        }
        
        if(pop.DeterminanteGSP == null) {
            throw new Exception("Objeto Pop No Referenciado");
        }
        
        mx.smartteam.entities.TiendaAsistenciaCollection collection = mx.smartteam.data.TiendasAsistencia.DownloadAsistencia(proyecto, pop);
        
        return collection;
    }
    
}
