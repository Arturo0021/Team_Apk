/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.CategoriasConfigCollection;

/**
 *
 * @author edgarin.lara
 */
public class Categoria {

    public static class Download {

        public static CategoriaCollection GetCategoriaProductoByProyecto(mx.smartteam.entities.Proyecto proyecto, String time) throws Exception {
            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            CategoriaCollection categoriaCollection = mx.smartteam.data.Categoria.Download.GetCategoriasProducto(proyecto, time);
            return categoriaCollection;
        }
          public static CategoriasConfigCollection GetCategoriasConfig(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,String time) throws Exception {
            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }
            CategoriasConfigCollection configcategoriaCollection = mx.smartteam.data.CategoriaConfig.Download.GetCategoriaConfig(proyecto,usuario, time);
            return configcategoriaCollection;
        }

        public static CategoriaCollection GetCategoriaFotoByProyecto(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time) throws Exception {
            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            CategoriaCollection categoriaCollection = mx.smartteam.data.Categoria.Download.GetCategoriasFoto(proyecto, usuario, time);
            return categoriaCollection;
        }
    }

    public static CategoriaCollection GetCategoriaProductoByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto,String tipo) throws Exception {

        CategoriaCollection categoriaCollection = null;
        if (proyecto == null) {
            throw new Exception("Object [proyecto] no referenciado");
        }
        categoriaCollection = mx.smartteam.data.Categoria.GetCategoriaProductoByProyecto(context, proyecto,tipo);
        return categoriaCollection;
    }

    public static CategoriaCollection GetCategoriaFotoByProyecto(Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        CategoriaCollection categoriaCollection = null;
        if (proyecto == null) {
            throw new Exception("Object [proyecto] no referenciado");
        }
        categoriaCollection = mx.smartteam.data.Categoria.GetCategoriaFotoByProyecto(context, proyecto);
        return categoriaCollection;
    }

    public static void Insert(Context context, mx.smartteam.entities.Categoria categoria) throws Exception {

        if (categoria == null) {
            throw new Exception("Objet categoria no referenciado");
        }

        mx.smartteam.data.Categoria.Insert(context, categoria);
    }
    public static void InsertCategoriasConfig(Context context, mx.smartteam.entities.CategoriasConfig configCategorias) throws Exception {

        if (configCategorias == null) {
            throw new Exception("Objet categoria no referenciado");
        }
        mx.smartteam.data.CategoriaConfig.InsertCategoriaConfig(context, configCategorias);
    }
}
