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
import mx.smartteam.entities.ExhibicionAdicionalCollection;
import mx.smartteam.entities.ProductoCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class ExhibicionAdicional {
 /**********************************************************************************************************/
    public static void Insert(Context context, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) throws Exception {

        if (exhibicionAdicional == null) {
            throw new Exception("Object exhibicionAdicional no referenciado ");
        }
        mx.smartteam.data.ExhibicionAdicional.Insert(context, exhibicionAdicional);
    }
 /**********************************************************************************************************/
    public static void Update(Context context, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) throws Exception{
        
         if (exhibicionAdicional==null) {
             throw new Exception("Oject Bodega no referenciado");
         }
         
         mx.smartteam.data.ExhibicionAdicional.Update(context, exhibicionAdicional);
         
      

    }
 /*********************************************************************************************************/
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) throws Exception {
        if (exhibicionAdicional == null) {
            throw new Exception("Object exhibicionAdicional no referenciado ");
        }
        mx.smartteam.data.ExhibicionAdicional.UpdateStatusSync(context, exhibicionAdicional);
    }
 /**********************************************************************************************************/
    public static mx.smartteam.entities.ExhibicionAdicionalCollection GetByVisita(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {
        mx.smartteam.entities.ExhibicionAdicionalCollection exhibicionAdicionalCollection = new ExhibicionAdicionalCollection();

        if (popVisita == null) {
            throw new Exception("Object popVisita no referenciado");
        }
        exhibicionAdicionalCollection = mx.smartteam.data.ExhibicionAdicional.GetByVisita(context, popVisita);
        return exhibicionAdicionalCollection;
    }
  /**********************************************************************************************************/   
 public static mx.smartteam.entities.ProductoCollection GetProductosByVisita
        (Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop) throws Exception {

        mx.smartteam.entities.ProductoCollection exhibicionAdicionalCollection = new ProductoCollection();
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

        exhibicionAdicionalCollection = mx.smartteam.data.ExhibicionAdicional.GetProductosByVisita(context, proyecto, usuario, pop);
        return exhibicionAdicionalCollection;
    }
  /**********************************************************************************************************/
public static mx.smartteam.entities.ExhibicionAdicional  GetInfoByVisita(Context context,mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,
                              mx.smartteam.entities.Pop pop, mx.smartteam.entities.Producto producto) throws Exception {

         mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional = null;
        if (producto == null) 
        {
            throw new Exception("Object popvisita no referenciado");
        }
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
 exhibicionAdicional=  mx.smartteam.data.ExhibicionAdicional.GetInfoByVisita(context,proyecto,usuario ,pop, producto);
        return exhibicionAdicional;
    }          
 /**********************************************************************************************************/
              public static mx.smartteam.entities.ExhibicionAdicional 
              GetInfoByVisita2(Context context, 
                              mx.smartteam.entities.Proyecto proyecto , 
                              mx.smartteam.entities.Usuario usuario,
                              mx.smartteam.entities.Pop pop, 
                              String producto) 
throws Exception {

         mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional = null;
        if (producto == null) 
        {
            throw new Exception("Object popvisita no referenciado");
        }
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
 exhibicionAdicional=  mx.smartteam.data.ExhibicionAdicional.GetInfoByVisita2(context,proyecto,usuario ,pop, producto);
        return exhibicionAdicional;
    } 
              
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) throws Exception {
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado - existe_contestacion");
        }
        
        int existe = mx.smartteam.data.ExhibicionAdicional.existe_contestacion(context, pop);
        
        return existe;
    }
 /**********************************************************************************************************/
    public static class Upload {

        public static void Insert (mx.smartteam.entities.PopVisita visita,mx.smartteam.entities.ExhibicionAdicional exhibicionAdicional) throws Exception{
            if (visita==null) {
                throw new Exception("Object visita no referenciado");
            }
            
            if (exhibicionAdicional==null) {
                throw new Exception("Object exhibicionAdicional no referenciado");
            }

            
            mx.smartteam.data.ExhibicionAdicional.Upload.Insert(visita, exhibicionAdicional);
        }
    }
  /**********************************************************************************************************/
}
