package mx.smartteam.business;

import android.content.Context;

public class MetaCadena {

    
    public static mx.smartteam.entities.MetaCadenaCollection 
        getAllMetaCadena(mx.smartteam.entities.Proyecto proyecto, String time) throws Exception{
            if(proyecto == null){
                throw new Exception("Objeto Proyecto no referenciado");
            }
            
        mx.smartteam.entities.MetaCadenaCollection 
                metaCadena = mx.smartteam.data.MetaCadena.Download.getAllMetaCadena(proyecto, time);
    return metaCadena;    
    }
        
        
    public static void Insert(Context context, mx.smartteam.entities.MetaCadena metaCadena) throws Exception{
    if(metaCadena == null){
        throw new Exception("MetaCadena no referenciado");
    }
    
        mx.smartteam.data.MetaCadena.Insert(context, metaCadena);
    
    }
    
    
    public static mx.smartteam.entities.MetaCadenaCollection getAll(Context context , int bandera , mx.smartteam.entities.Proyecto proyecto) throws Exception{
        if(proyecto == null){
             throw new Exception("Object mes no referenciado ");
        }
        
        mx.smartteam.entities.MetaCadenaCollection metaCollection = mx.smartteam.data.MetaCadena.getMensajesXMes(context, bandera, proyecto);
        
        return metaCollection;
    }
    
    
}
