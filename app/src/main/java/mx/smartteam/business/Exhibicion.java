package mx.smartteam.business;

import android.content.Context;

public class Exhibicion {
    
    public static class Download{
    
        public static mx.smartteam.entities.ExhibicionCollection GetExhibicion() throws Exception{
            mx.smartteam.entities.ExhibicionCollection exhibicionCollection = mx.smartteam.data.Exhibicion.Download.GetExhibicion();
            return exhibicionCollection;
        }
    
    }
    
    public static void Insert(Context context, mx.smartteam.entities.Exhibicion exhibicion) throws Exception {
        if(exhibicion == null) {
            throw new Exception("Objeto exhibicion no referenciado");
        }
        mx.smartteam.data.Exhibicion.Insert(context, exhibicion);
    }

    public static mx.smartteam.entities.ExhibicionCollection GetAll(Context context, Integer IdVisita, Integer IdCategoria, Integer IdMarca) throws Exception{
        if(IdVisita == 0 || IdVisita == null){
            throw new Exception("Visita no referenciada");
        }
        if(IdCategoria == 0 || IdCategoria == null){
            throw new Exception("Categoria no referenciada");
        }
        if(IdMarca == 0 || IdMarca == null){
            throw new Exception("Marca no referenciada");
        }
        
        mx.smartteam.entities.ExhibicionCollection exhibicionCollection = mx.smartteam.data.Exhibicion.GetAll(context, IdVisita, IdCategoria, IdMarca);
        return exhibicionCollection;
    }
    
}
