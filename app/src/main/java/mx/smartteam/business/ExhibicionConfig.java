package mx.smartteam.business;

import android.content.Context;

public class ExhibicionConfig {
    
    public static class Download{
    
        public static mx.smartteam.entities.ExhibicionConfigCollection GetExhibicionConfig(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario ,String time)throws Exception{
            try{
                if(proyecto == null){
                    throw new Exception("Objeto Sondeo no referenciado");
                }

                if(usuario == null){
                    throw new Exception("Objeto Sondeo no referenciado");
                }

                mx.smartteam.entities.ExhibicionConfigCollection exhibicionConfigCollection = mx.smartteam.data.ExhibicionConfig.Download.GetExhibicionConfig(proyecto,usuario, time);
                return exhibicionConfigCollection;

            }catch(Exception ex){
            throw new Exception("GetExhibicionConfig", ex);
            }
            
        }
    
    }
    
    
    public static void Insert(Context context,  mx.smartteam.entities.ExhibicionConfig exhibicionConfig)throws Exception{
        if(exhibicionConfig == null){
            throw new Exception("Object exhibicionConfig no referenciado");
        }
    
        mx.smartteam.data.ExhibicionConfig.Insert(context, exhibicionConfig);
    
    }
    
    
}
