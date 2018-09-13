/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.ProductoCadenaCollection;

/**
 *
 * @author edgarin.lara
 */
public class ProductoCadena {

    public static class Download {

        public static ProductoCadenaCollection GetProductosCadena(mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {

            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            ProductoCadenaCollection productoCadenaCollection = mx.smartteam.data.ProductoCadena.Download
                    .GetProductosCadena(proyecto, time);


            return productoCadenaCollection;
        }
        
        public static ProductoCadenaCollection GetConjuntoCadenasbyUsuario(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time)throws Exception{
            if(proyecto == null){
                throw new Exception("Object proyecto no referenciado");
            }
            if(usuario == null){
                throw new Exception("Object usuario no referenciado");
            }
            
            ProductoCadenaCollection productoCadenaCollection = mx.smartteam.data.ProductoCadena.Download
                    .GetConjuntoCadenasbyUsuario(proyecto, usuario,time);
            
            return productoCadenaCollection;
        }
        
        
    }//END DOWNLOAD

public static void Insert(Context context, mx.smartteam.entities.ProductoCadena productoCadena) throws Exception{
        if (productoCadena == null) {
            throw new Exception("Object productocadena no referenciado");
        }
        
        mx.smartteam.data.ProductoCadena.Insert(context, productoCadena);

    }

}
