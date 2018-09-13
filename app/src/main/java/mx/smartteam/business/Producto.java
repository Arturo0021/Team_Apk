/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.ProductoCollection;

/**
 *
 * @author edgarin.lara
 */
public class Producto {

    public static class Downdload {

        public static ProductoCollection GetProductosByProyecto(mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {

            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            ProductoCollection productoCollection = mx.smartteam.data.Producto.Download
                    .GetProductos(proyecto, time);

            return productoCollection;
        }

        //Aqui Normal
        public static ProductoCollection GetProductosAll(mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {

            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            ProductoCollection productoCollection = mx.smartteam.data.Producto.Download.GetProductosAll(proyecto, time);

            return productoCollection;
        }

    }

    public static void Insert(Context context, mx.smartteam.entities.Producto producto) throws Exception {

        if (producto == null) {
            throw new Exception("Object producto no referenciado");
        }
        mx.smartteam.data.Producto.Insert(context, producto);

    }

    public static void Insertp(Context context, mx.smartteam.entities.Producto producto) throws Exception {

        if (producto == null) {
            throw new Exception("Object producto no referenciado");
        }
        mx.smartteam.data.Producto.Insertp(context, producto);

    }

    public static ProductoCollection GetByVisita(Context context, Integer idVisita, Integer idcadena, Integer idcategoria, Integer idmarca, EnumFormulario formulario, int idsondeo, Integer idusuario) throws Exception {
        if (idVisita == null) {
            throw new Exception("Object visita no referenciado");
        }
        ProductoCollection productoCollection = null;
        productoCollection = mx.smartteam.data.Producto.GetByVisita(context, idVisita, idcadena, idcategoria, idmarca, formulario, idsondeo, idusuario);
        if (productoCollection == null) {
            productoCollection = new ProductoCollection();
        }
        return productoCollection;

    }
    
    public static mx.smartteam.entities.Producto GetOne(Context context, mx.smartteam.entities.Producto EntityProducto)throws Exception {
    
        String Barcode = EntityProducto.Barcode;
                
        mx.smartteam.entities.Producto producto = new mx.smartteam.entities.Producto();
        if(Barcode == null ){
            throw new Exception("Object Producto no referenciado");
        }
        producto = mx.smartteam.data.Producto.GetOne(context, Barcode);
        
        return producto;
    }
    

}
