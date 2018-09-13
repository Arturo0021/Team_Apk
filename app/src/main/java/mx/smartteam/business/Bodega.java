/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.data.ServiceURI;
import mx.smartteam.entities.BodegaCollection;
import mx.smartteam.entities.ProductoCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class Bodega {
    
 /**********************************************************************************************************/
    
    public static void Insert(Context context, mx.smartteam.entities.Bodega bodega) throws Exception {
        if (bodega == null) {
            throw new Exception("Object bodega no referenciado");
        }
        
        mx.smartteam.data.Bodega.Insert(context, bodega);
    }
      /**********************************************************************************************************/      
           public static void Update(Context context, mx.smartteam.entities.Bodega bodega) throws Exception{
        
         if (bodega==null) {
             throw new Exception("Oject Bodega no referenciado");
         }
         
         mx.smartteam.data.Bodega.Update(context, bodega);
         
      

    } 
 /**********************************************************************************************************/
    
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.Bodega bodega) throws Exception {
        if (bodega == null) {
            throw new Exception("Object bodega no referenciado");
        }
        mx.smartteam.data.Bodega.UpdateStatusSync(context, bodega);
        
    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) throws Exception {
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado - existe_contestacion");
        }
        
        int existe = mx.smartteam.data.Bodega.existe_contestacion(context, pop);
        
        return existe;
    }
    
 /**********************************************************************************************************/
    
    public static mx.smartteam.entities.BodegaCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {
        
        mx.smartteam.entities.BodegaCollection bodegaCollection = new BodegaCollection();
        if (popVisita == null) {
            throw new Exception("Object popVisita no referenciado");
        }
        bodegaCollection = mx.smartteam.data.Bodega.GetByVisita(context, popVisita);
        
        return bodegaCollection;
    }
    
     /**********************************************************************************************************/
    
       public static mx.smartteam.entities.ProductoCollection GetProductosByVisita
        (Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop) throws Exception {

        mx.smartteam.entities.ProductoCollection bodegaCollection = new ProductoCollection();
         if (proyecto == null) 
        {
            throw new Exception("Object Proyecto no referenciado");
        }
         if (usuario == null) 
        {
            throw new Exception("Object Usuario no referenciado");
        }
         if (pop == null) 
        {
            throw new Exception("Object Pop no referenciado");
        }

        bodegaCollection = mx.smartteam.data.Bodega.GetProductosByVisita(context,proyecto,usuario ,pop);
        return bodegaCollection;
    }
  /**********************************************************************************************************/
       public static mx.smartteam.entities.Bodega GetInfoByVisita
        (Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop, mx.smartteam.entities.Producto producto) throws Exception {

         mx.smartteam.entities.Bodega bodega = null;
        if (pop == null) 
        {
            throw new Exception("Object popvisita no referenciado");
        }
       
              bodega = mx.smartteam.data.Bodega.GetInfoByVisita(context,proyecto,usuario ,pop, producto);
        return bodega;
    }

  /**********************************************************************************************************/
               public static mx.smartteam.entities.Bodega GetInfoByVisita2
        (Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop, String producto) throws Exception {

         mx.smartteam.entities.Bodega bodega = null;
        if (pop == null) 
        {
            throw new Exception("Object popvisita no referenciado");
        }
       
              bodega = mx.smartteam.data.Bodega.GetInfoByVisita2(context,proyecto,usuario ,pop, producto);
        return bodega;
    }
        
/**********************************************************************************************************/

    public static class Upload {
        
        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Bodega bodega) throws Exception {
            if (visita == null) {
                throw new Exception("Object visita no referenciado");
            }
            
            if (bodega == null) {
                throw new Exception("Object bodega no referenciado");
            }
            mx.smartteam.data.Bodega.Upload.Insert(visita, bodega);
            
        }
    }
   /**********************************************************************************************************/
}
