/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.entities.AnaquelCollection;
import mx.smartteam.entities.ProductoCollection;

/**
 *
 * @author edgarin.lara
 */
public class Anaquel {

    /**
     * *********************************************************************************************************
     */
    public static void Insert(Context context, mx.smartteam.entities.Anaquel anaquel) throws Exception {

        if (anaquel == null) {
            throw new Exception("Oject Anaquel no referenciado");
        }

        mx.smartteam.data.Anaquel.Insert(context, anaquel);

    }

    /**
     * *********************************************************************************************************
     */
    public static void Update(Context context, mx.smartteam.entities.Anaquel anaquel) throws Exception {

        if (anaquel == null) {
            throw new Exception("Oject Anaquel no referenciado");
        }

        mx.smartteam.data.Anaquel.Update(context, anaquel);

    }

    /**
     * *********************************************************************************************************
     */
    public static mx.smartteam.entities.ProductoCollection
            GetProductosByVisita(Context context,
                    mx.smartteam.entities.Proyecto proyecto,
                    mx.smartteam.entities.Usuario usuario,
                    mx.smartteam.entities.Pop pop)
            //  ,mx.smartteam.entities.Producto producto) 
            throws Exception {

        mx.smartteam.entities.ProductoCollection anaquelCollection = new ProductoCollection();
        if (proyecto == null) {
            throw new Exception("Object Proyecto no referenciado");
        }
        if (usuario == null) {
            throw new Exception("Object Usuario no referenciado");
        }
        if (pop == null) {
            throw new Exception("Object Pop no referenciado");
        }

        anaquelCollection = mx.smartteam.data.Anaquel.GetProductosByVisita(context, proyecto, usuario, pop);//, producto);
        return anaquelCollection;
    }

    /**
     * *********************************************************************************************************
     */
    public static mx.smartteam.entities.ProductoCollection
            GetProductosByVisita2(Context context,
                    mx.smartteam.entities.Proyecto proyecto,
                    mx.smartteam.entities.Usuario usuario,
                    mx.smartteam.entities.Pop pop)
            //  ,mx.smartteam.entities.Producto producto) 
            throws Exception {

        mx.smartteam.entities.ProductoCollection anaquelCollection = new ProductoCollection();
        if (proyecto == null) {
            throw new Exception("Object Proyecto no referenciado");
        }
        if (usuario == null) {
            throw new Exception("Object Usuario no referenciado");
        }
        if (pop == null) {
            throw new Exception("Object Pop no referenciado");
        }

        anaquelCollection = mx.smartteam.data.Anaquel.GetProductosByVisita2(context, proyecto, usuario, pop);//, producto);
        return anaquelCollection;
    }

    /**
     * *********************************************************************************************************
     */
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.Anaquel anaquel) throws Exception {

        if (anaquel == null) {
            throw new Exception("Object anaquel no referenciado");
        }

        mx.smartteam.data.Anaquel.UpdateStatusSync(context, anaquel);
    }

    /**
     * *********************************************************************************************************
     */
    public static mx.smartteam.entities.AnaquelCollection GetByVisita(final Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        mx.smartteam.entities.AnaquelCollection anaquelCollection = new AnaquelCollection();
        if (popVisita == null) {
            throw new Exception("Object popvisita no referenciado");

        }
        anaquelCollection = mx.smartteam.data.Anaquel.GetByVisita(context, popVisita);
        return anaquelCollection;
    }

    /**
     * *********************************************************************************************************
     */
    public static mx.smartteam.entities.Anaquel GetInfoByVisita(Context context, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Producto producto,String tipo) throws Exception {

        mx.smartteam.entities.Anaquel anaquel = null;
        if (pop == null) {
            throw new Exception("Object popvisita no referenciado");
        }

        anaquel = mx.smartteam.data.Anaquel.GetInfoByVisita(context,  pop, producto,tipo);
        return anaquel;
    }

    /**
     * *********************************************************************************************************
     */
    public static mx.smartteam.entities.Anaquel GetInfoByVisita2(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,
            mx.smartteam.entities.Pop pop, String producto) throws Exception {

        mx.smartteam.entities.Anaquel anaquel = null;
        if (pop == null) {
            throw new Exception("Object popvisita no referenciado");
        }

        anaquel = mx.smartteam.data.Anaquel.GetInfoByVisita2(context, proyecto, usuario, pop, producto);
        return anaquel;
    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop, String tipo) throws Exception{
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado - existe_contestacion");
        }
        
        if(tipo.equals("")) {
            throw new Exception("Variable tipo No Referenciado - existe_contestacion");
        }
        
        int existe = mx.smartteam.data.Anaquel.existe_contestacion(context, pop, tipo);
        
        return existe;
    }
    

    /**
     * *********************************************************************************************************
     */
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Anaquel anaquel) throws Exception {

            if (visita == null) {
                throw new Exception("Object visita no referencido");

            }
            if (anaquel == null) {
                throw new Exception("Object anaquel no referencido");

            }
            mx.smartteam.data.Anaquel.Upload.Insert(visita, anaquel);

        }
    }
    /**
     * *********************************************************************************************************
     */

}
