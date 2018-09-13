package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.entities.PonderacionCollection;

public class Ponderacion {
    
    public static class Download{

        public static mx.smartteam.entities.PonderacionCollection 
                                getAllPonderacion(mx.smartteam.entities.Proyecto proyecto, String Time)throws Exception {
            
            if(proyecto == null){
                throw new Exception("Object proyecto no referenciado");
            }
            
            PonderacionCollection ponderacionCollection
                    = mx.smartteam.data.Ponderacion.Download.getAllPonderaciones(proyecto, Time);
            
            return ponderacionCollection;
    
        }
    
    }
    
    public static void Insert(Context context, mx.smartteam.entities.Ponderacion ponderacion)throws Exception{
        if(ponderacion == null){
            throw new Exception("Object ponderacion no referenciado");
        }
        mx.smartteam.data.Ponderacion.Insert(context, ponderacion);
        
    }
    
    
}







































