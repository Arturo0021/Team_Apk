
package mx.smartteam.business;

import android.content.Context;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.VersionCollection;

/**
 *
 * @author jaime.bautista
 */
public class Version {

    public static class Download {

        public static mx.smartteam.entities.VersionCollection GetByVersion(mx.smartteam.entities.Version version) throws Exception {

            mx.smartteam.entities.VersionCollection versionCollection = mx.smartteam.data.Version.Download.GetByVersion(version);

            return versionCollection;
        }
    }
    
    public static void Insert(Context context, mx.smartteam.entities.Version version) throws Exception{
        if(version == null){
            throw new Exception ("Object Version no referenciado");
        }
        
        mx.smartteam.data.Version.Insert(context, version);
    }
    
   public static mx.smartteam.entities.VersionCollection GetByVersion(Context context) throws Exception{
       mx.smartteam.entities.VersionCollection versionCollection = null;
       
       versionCollection = mx.smartteam.data.Version.GetByVersion(context);
               
               
       return versionCollection;
   } 
    
   public static mx.smartteam.entities.Version getVersion(Context context, Integer idversion){
   
   mx.smartteam.entities.Version version = null;
       
        try {
            
            version = mx.smartteam.data.Version.GetVersion(context, idversion);
            
        } catch (Exception ex) {
            Logger.getLogger(Version.class.getName()).log(Level.SEVERE, null, ex);
        }
               
               
       return version;
   
   
   }
   
}
