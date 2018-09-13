package mx.smartteam.business;

import android.content.Context;
import java.io.IOException;
import org.json.JSONException;


public class ScoredCard {
    
    public static class Download{
        public static mx.smartteam.entities.ScoredCardCollection getgetAllCalificacionesDiarias(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String Time) throws JSONException, IOException{
                
            mx.smartteam.entities.ScoredCardCollection scCollection = mx.smartteam.data.ScoredCard.Download.getAllCalificacionesDiarias(proyecto, usuario, Time);
                        
            return scCollection;
        }
    }
    
    
    public static void Insert(Context contect, mx.smartteam.entities.ScoredCard scard) throws Exception{
        if(scard == null){
            throw new Exception("Objeto SC no referenciado");
        }    
        
        mx.smartteam.data.ScoredCard.Insert(contect, scard);
    }
    
    public static mx.smartteam.entities.ScoredCardCollection getAllRegistros(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, int bandera) throws Exception{
        if(proyecto == null){
            throw new Exception("Objeto proyecto no referenciado");
        }
    
        if(usuario == null){
            throw new Exception("Objeto usuario no referenciado");
        }
        
        mx.smartteam.entities.ScoredCardCollection scCollection = mx.smartteam.data.ScoredCard.getAllMonth(context, proyecto, usuario, bandera);
        
        return scCollection ;
    }
    
}
