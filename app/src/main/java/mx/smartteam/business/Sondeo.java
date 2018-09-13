/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.SondeoCollection;
import mx.smartteam.entities.SondeoModulosCollection;

/**
 *
 * @author edgarin.lara
 */
public class Sondeo {

    public static class Download {

        public static mx.smartteam.entities.SondeoCollection GetByProyectoAndUsuario(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, String time) throws Exception {
            try {

                if (proyecto == null) {
                    throw new Exception("Object proyecto no referenciado");
                }

                if (usuario == null) {
                    throw new Exception("Object usuario no referenciado");
                }

                mx.smartteam.entities.SondeoCollection sondeoCollection = mx.smartteam.data.Sondeo.Download.GetByProyectoUsuario(proyecto, usuario, time);
                return sondeoCollection;

            } catch (Exception e) {
                throw new Exception("GetOrdenSondeos1", e);
            }

        }
    }

    public static int get_Exist_Sondeo(Context context, mx.smartteam.entities.SondeoModulos modulos, mx.smartteam.entities.Pop pop) throws Exception{
        
        if(modulos == null) {
            throw new Exception("Objeto Modulos No Referenciado");
        }
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado");
        }
        
        int exist = mx.smartteam.data.Sondeo.get_Exist_Sondeo(context, modulos, pop);
        
        return exist;
    }
    
    public static void Insert(Context context,
            mx.smartteam.entities.Sondeo sondeo) throws Exception {

        if (sondeo == null) {
            throw new Exception("Object sondeo no referenciado");
        }

        mx.smartteam.data.Sondeo.Insert(context, sondeo);

    }

    /**
     * ***************************************************************************
     * public static int GetInfoByVisita (Context context,
     * mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario
     * usuario, mx.smartteam.entities.Pop pop) throws Exception {
     *
     * // mx.smartteam.entities.BodegaCollection bodegaCollection = new
     * BodegaCollection(); if (pop == null) { throw new Exception("Object
     * popvisita no referenciado"); } int res =
     * mx.smartteam.data.Anaquel.GetInfoByVisita(context,proyecto,usuario ,pop);
     * return res; }   
    *************************************************************************
     */
    public static mx.smartteam.entities.SondeoCollection GetByProyecto(final Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetByProyecto(context, proyecto);
        return sondeoCollection;
    }
      public static mx.smartteam.entities.SondeoModulosCollection GetByProyectosm(final Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.SondeoModulosCollection sondeoCollection = new SondeoModulosCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetByProyectosm(context, proyecto);
        return sondeoCollection;
    }
    
      public static mx.smartteam.entities.SondeoCollection GetByProyecto2(final Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {

        mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetByProyecto2(context, proyecto);
        return sondeoCollection;
    }
      
    public static mx.smartteam.entities.SondeoCollection GetGruposSondeos(final Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
    mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetGruposSondeos(context, proyecto);
        return sondeoCollection;
    
    }
        public static mx.smartteam.entities.SondeoModulosCollection GetGruposSondeossm(final Context context, mx.smartteam.entities.Proyecto proyecto) throws Exception {
    mx.smartteam.entities.SondeoModulosCollection sondeoCollection = new SondeoModulosCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetGruposSondeossm(context, proyecto);
        return sondeoCollection;
    
    }
    
    public static mx.smartteam.entities.SondeoCollection GetAllGruposSondeos(final Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Sondeo currentSondeo) throws Exception {
    mx.smartteam.entities.SondeoCollection sondeoCollection = new SondeoCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetAllGruposSondeos(context, proyecto, currentSondeo);
        return sondeoCollection;
    
    } 
      public static mx.smartteam.entities.SondeoModulosCollection GetAllGruposSondeossm(final Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.SondeoModulos currentSondeo) throws Exception {
    mx.smartteam.entities.SondeoModulosCollection sondeoCollection = new SondeoModulosCollection();
        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado ");
        }
        sondeoCollection = mx.smartteam.data.Sondeo.GetAllGruposSondeossm(context, proyecto, currentSondeo);
        return sondeoCollection;
    
    }

    public static mx.smartteam.entities.Marca GetMarcaByVisita(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario,
            mx.smartteam.entities.Pop pop) throws Exception {

        mx.smartteam.entities.Marca marca = new mx.smartteam.entities.Marca();
        /* if (pop == null) 
        {
            throw new Exception("Object popvisita no referenciado");
        }*/
        marca = mx.smartteam.data.Sondeo.GetMarcaByVisita(context, proyecto, usuario, pop);
        return marca;
    }

 

    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.RespuestaSondeoCollection respuestaCollection) throws Exception {

            if (visita == null) {
                throw new Exception("Object visita no referenciado ");
            }

            if (sondeo == null) {
                throw new Exception("Object sondeo no referenciado ");
            }

            if (respuestaCollection.size() > 0) {
                mx.smartteam.data.Sondeo.Upload.Insert(visita, sondeo, respuestaCollection);
            }

        }
    }

    public static class Upload2 {

        public static void Insert2(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo, mx.smartteam.entities.RespuestaSondeo respuestas) throws Exception {

            if (visita == null) {
                throw new Exception("Object visita no referenciado ");
            }

            if (sondeo == null) {
                throw new Exception("Object sondeo no referenciado ");
            }

            if (respuestas == null) {
                throw new Exception("Object respuestas no referenciado ");
            }

            mx.smartteam.data.Sondeo.Upload2.Insert(visita, sondeo, respuestas);

        }
    }

}
