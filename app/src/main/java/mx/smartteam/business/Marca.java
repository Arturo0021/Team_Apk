/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.MarcaConfigCollection;
import mx.smartteam.entities.MarcaInactivaCollection;

/**
 *
 * @author edgarin.lara
 */
public class Marca {

    public static class Download {

        public static MarcaCollection GetMarcaByProyecto(mx.smartteam.entities.Proyecto proyecto,String time) throws Exception {


            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            MarcaCollection marcaCollection = mx.smartteam.data.Marca.Download
                    .GetMarcas(proyecto, time);

            return marcaCollection;



        }
        public static MarcaInactivaCollection GetMarcaInactiva(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception 
        {
           if (proyecto == null && usuario == null) {
                throw new Exception("Object proyecto no referenciado");
            }
            MarcaInactivaCollection marcainactivaCollection = mx.smartteam.data.MarcaInactiva.Download
                    .GetMarcaInactiva(proyecto,usuario, time);
            return marcainactivaCollection;
        }
          public static MarcaConfigCollection GetMarcaconfig(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception {
            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }
            MarcaConfigCollection marcaConfigCollection = mx.smartteam.data.MarcaConfig.Download.GetMarcaConfig(proyecto,usuario, time);
            return marcaConfigCollection;
        }
    }

    public static void Insert(Context context, mx.smartteam.entities.Marca marca) throws Exception {
        if (marca == null) {
            throw new Exception("Object marca no referenciado");
        }

        mx.smartteam.data.Marca.Insert(context, marca);

    }
      public static void InsertMarcaConfig(Context context, mx.smartteam.entities.MarcaConfig marcaConfig) throws Exception {
        if (marcaConfig == null) {
            throw new Exception("Object marca no referenciado");
        }

        mx.smartteam.data.MarcaConfig.InsertMarcaConfig(context, marcaConfig);

    }
   
    public static void SelectMarcaInac(Context context , mx.smartteam.entities.MarcaInactiva marcainactiva) throws Exception {
         mx.smartteam.data.MarcaInactiva.SelectMarcaInactiva(context,marcainactiva);
    }
    
    public static MarcaCollection GetAllXContestacion(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Pop pop, mx.smartteam.entities.SondeoModulos sondeo)
        throws Exception{
        
        if(proyecto != null){
            throw new Exception("Objeto proyecto no referenciado");
        }
        if(pop != null){
            throw new Exception("Objeto Pop no referenciado");
        }
        if(sondeo != null){
            throw new Exception("Objecto Sondeo no referenciado");
        }
        
        MarcaCollection marcaCollection = mx.smartteam.data.Marca.GetAllXContestacion(context, proyecto, pop, sondeo);
       
        return marcaCollection;
    }
    
    
}
