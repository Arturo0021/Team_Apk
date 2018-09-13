
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.entities.ContestacionCollection;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.RespuestaSondeoCollection;


public class Contestacion {
    
    //context, popVisita, sondeo
    
    
      public static mx.smartteam.entities.ContestacionCollection ContestacionGetByVisita(Context context, mx.smartteam.entities.PopVisita popVisita, mx.smartteam.entities.Sondeo sondeo) throws Exception {
         mx.smartteam.entities.ContestacionCollection  contestacionCollection =null;// new ContestacionCollection();
         if (popVisita == null) {
            throw new Exception("Object popVisita no referenciado");
            }
        if (sondeo == null){
            throw new Exception ("Object sondeo no referenciado");
            }
        
     contestacionCollection =  mx.smartteam.data.Contestacion.ContestacionGetByVisita(context, popVisita, sondeo);

        return contestacionCollection;
    }
      
      
      
      public static ProductoCollection getProductosContestados(Context context,
              mx.smartteam.entities.Pop pop, mx.smartteam.entities.Sondeo sondeo, 
              mx.smartteam.entities.Usuario usuario,
              mx.smartteam.entities.Proyecto proyecto
      ) throws Exception {
                   
         mx.smartteam.entities.ProductoCollection  productoCollection = new ProductoCollection();
        
        if (pop == null){
            throw new Exception ("Object sondeo no referenciado");
            } 
         
        if (sondeo == null){
            throw new Exception ("Object sondeo no referenciado");
            }
        if (usuario==null){
        throw new Exception ("Object usuario no referenciado");
        }
         if (proyecto==null){
        throw new Exception ("Object usuario no referenciado");
        }
     productoCollection =  mx.smartteam.data.Contestacion.getProductosContestados(context, pop, sondeo, usuario, proyecto);

        return productoCollection;
    }
    
    public static mx.smartteam.entities.RespuestaSondeoCollection GetByVisitaSondeop
        (final Context context, mx.smartteam.entities.PopVisita popVisita, mx.smartteam.entities.Sondeo sondeo
         , mx.smartteam.entities.Contestacion contestacion
            ) throws Exception {
        if(popVisita == null){
        throw new Exception("Objeto [proyecto] no referenciado");
        }
        if(sondeo == null){
        throw new Exception("Objeto [proyecto] no referenciado");
        }       
        if(contestacion == null){
        throw new Exception("Objeto [proyecto] no referenciado");
        }       
        RespuestaSondeoCollection respuestaSondeoCollection = mx.smartteam.data.RespuestaSondeo.GetByVisitaSondeop(context, popVisita, sondeo, contestacion);
        return respuestaSondeoCollection;
    }
      
        public static String UpdateStatusSync(Context context, mx.smartteam.entities.Contestacion contestacion) {
            String result = null;
            
            mx.smartteam.data.RespuestaSondeo.UpdateStatusSync(context, contestacion);
            return result;
        }

       public static class Upload {
        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.ContestacionCollection contestacion) throws Exception {
            
            if (visita==null) {
                throw new Exception("Object visita no referenciado ");
            }            
            
            if (sondeo==null) {
                throw new Exception("Object sondeo no referenciado ");
            }
            
            if (contestacion.size()>0) {
                //Insertar

                mx.smartteam.data.Contestacion.Upload.Insert(visita, sondeo, null);
            }
            
            
            
        }
    }
    
    
}
