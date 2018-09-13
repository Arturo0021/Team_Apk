/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.entities.consicCollection;

/**
 *
 * @author jaime.bautista
 */
public class UtilSincronizacion {
    
   public static void UpdateSincro(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception{
    
        mx.smartteam.data.UtilSincronizacion.UpdateSincro(context, proyecto, usuario);
                
   }
   
    public static consicCollection getbyconsinc(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception{
        mx.smartteam.entities.consicCollection consicCollection = null;
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }

        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }
        consicCollection = mx.smartteam.data.UtilSincronizacion.SendLastDownload(context, proyecto,usuario);


        return consicCollection;
   
    }
   
   
   public static void CompareDownload(Context context) throws Exception{
   
       mx.smartteam.data.UtilSincronizacion.CompareDownload(context);
       
   
   }
   
   

}
