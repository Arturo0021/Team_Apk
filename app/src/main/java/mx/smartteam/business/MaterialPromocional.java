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
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.data.ServiceURI;
import mx.smartteam.entities.MaterialPromocionalCollection;
import mx.smartteam.entities.ProductoCollection;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class MaterialPromocional {
/************************************************************************************************/
    public static void Insert(Context context, mx.smartteam.entities.MaterialPromocional materialPromocional) throws Exception {
        if (materialPromocional == null) {
            throw new Exception("Object materialPromocional no referenciado");
        }
        mx.smartteam.data.MaterialPromocional.Insert(context, materialPromocional);
    }
/************************************************************************************************/
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.MaterialPromocional materialPromocional) throws Exception {

        if (materialPromocional == null) {
            throw new Exception("Object materialPromocional no referenciado");
        }
        mx.smartteam.data.MaterialPromocional.UpdateStatusSync(context, materialPromocional);
    }
/************************************************************************************************/
    public static mx.smartteam.entities.MaterialPromocionalCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        mx.smartteam.entities.MaterialPromocionalCollection materialPromocionalCollection = new MaterialPromocionalCollection();

        if (popVisita == null) {
            throw new Exception("Object vista no referenciada");
        }

        materialPromocionalCollection = mx.smartteam.data.MaterialPromocional.GetByVisita(context, popVisita);
        return materialPromocionalCollection;
    }
/************************************************************************************************/
     public static mx.smartteam.entities.ProductoCollection 
                GetProductosByVisita(Context context, 
                                     mx.smartteam.entities.Proyecto proyecto , 
                                     mx.smartteam.entities.Usuario usuario,
                                     mx.smartteam.entities.Pop pop
                                     ) 
     throws Exception {

        mx.smartteam.entities.ProductoCollection materialPromocionalCollection = new ProductoCollection();
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
     
        materialPromocionalCollection = mx.smartteam.data.MaterialPromocional.GetProductosByVisita(context, proyecto, usuario, pop);
        return materialPromocionalCollection;
    }
                
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) throws Exception{
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado - existe_contestacion");
        }
        
        int existe = mx.smartteam.data.MaterialPromocional.existe_contestacion(context, pop);
        
        return existe;
    }
/************************************************************************************************/
       public static mx.smartteam.entities.MaterialPromocional GetInfoByVisita
        (Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop, mx.smartteam.entities.Producto producto) throws Exception {

         mx.smartteam.entities.MaterialPromocional materialPromocional = null;
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
       
              materialPromocional = mx.smartteam.data.MaterialPromocional.GetInfoByVisita(context, proyecto, usuario, pop, producto);
        return materialPromocional;
    }
/************************************************************************************************/
        public static mx.smartteam.entities.MaterialPromocional GetInfoByVisita2
        (Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario,
                mx.smartteam.entities.Pop pop, String producto) throws Exception {

         mx.smartteam.entities.MaterialPromocional materialPromocional = null;
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
       
              materialPromocional = mx.smartteam.data.MaterialPromocional.GetInfoByVisita2(context, proyecto, usuario, pop, producto);
        return materialPromocional;
    }
/************************************************************************************************/
           public static void Update(Context context, mx.smartteam.entities.MaterialPromocional materialPromocional) throws Exception{
        
         if (materialPromocional==null) {
             throw new Exception("Oject Bodega no referenciado");
         }
         
         mx.smartteam.data.MaterialPromocional.Update(context, materialPromocional);
    }
/************************************************************************************************/        
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.MaterialPromocional materialPromocional) throws Exception {

            if (visita == null) {
                throw new Exception("Object visita no referenciado");
            }
            if (materialPromocional == null) {
                throw new Exception("Object materialPromocional no referenciado");
            }
            mx.smartteam.data.MaterialPromocional.Upload.Insert(visita, materialPromocional);
        }
    }
/************************************************************************************************/
}
