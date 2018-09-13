/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author jaime.bautista
 */
public class SeguimientoGps {
    
    public static String  hola()
    {
        
        String hola = mx.smartteam.data.SeguimientoGps.Hola();
        
        return hola;
    }
    
    
    public static void Insert(Context context, mx.smartteam.entities.SeguimientoGps seguimientoGps)
            throws Exception {
        if (seguimientoGps == null) {
            throw new Exception("Object Usuario no referenciado");
        }

        mx.smartteam.data.SeguimientoGps.Insert(context, seguimientoGps);

    }
     
     
     public static mx.smartteam.entities.SeguimientoGpsCollection GetSeguimientoGps(Context context,mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario) throws Exception{
         if(proyecto == null){
             throw  new Exception("Object Proyecto no referenciado");
         }
         
         if(usuario == null){
             throw  new Exception("Object Usuario no referenciado");
         }
         
         mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection = null;
         
         seguimientoGpsCollection = mx.smartteam.data.SeguimientoGps.GetSeguimientoGps(context, proyecto, usuario);
         
         
         return seguimientoGpsCollection;
     }
     
     public static void ActualizarStatus(Context context , mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario)throws Exception
     {
         String strResult;
         if(proyecto == null)
         {
             throw new Exception("Objeto Proyecto no referenciado");
         }
         if(usuario == null)
         {
             throw new Exception("Objeto Usuario no referenciado");
         }
         
         strResult = mx.smartteam.data.SeguimientoGps.ActualizarStatus(context, proyecto, usuario);
         
         
     }
     
     
     public static class Upload {
        
            
            public static String Insert(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection)throws Exception
            {
                String strResult = null;
                if(proyecto == null)
                {
                    throw new Exception("Object proyecto no referenciado");
                }
                if(usuario == null)
                {
                    throw new Exception("Object usuario no referenciado");
                }
                
                if(seguimientoGpsCollection.size() > 0)
                {
                        strResult = mx.smartteam.data.SeguimientoGps.Upload.Insert(proyecto, usuario, seguimientoGpsCollection);
                }
            
                return strResult;
            }
            
            
    }
    
}
