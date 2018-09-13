/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.data.BaseDatos;
import mx.smartteam.data.ServiceURI;
import mx.smartteam.entities.PopVisitaCollection;
import mx.smartteam.exceptions.PopException;
import mx.smartteam.exceptions.PopException.NoSeguirHastaSincronizar;
import mx.smartteam.exceptions.PopException.NoVisitasPendientesPorSync;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONStringer;

/**
 *
 * @author edgarin.lara
 */
public class PopVisita {

    public static mx.smartteam.entities.PopVisita Insert(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        if (popVisita == null) {
            throw new Exception("Object pop no esta referenciado");
        }

        popVisita = mx.smartteam.data.PopVisita.Insert(context, popVisita);

        return popVisita;
    }

    public static void UpdateStatusSync(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        if (popVisita==null) {
            throw new Exception("Object [popVisita] no referenciado");
        }
        
        mx.smartteam.data.PopVisita.UpdateStatusSync(context, popVisita);
    }

    public static mx.smartteam.entities.PopVisita GetById(Context context, Integer Id) {
        mx.smartteam.entities.PopVisita visita = mx.smartteam.data.PopVisita.GetById(context, Id);
        return visita;
    }

    //Obitne visitas pendientes po sincronizar    
    public static mx.smartteam.entities.PopVisitaCollection GetPendings
            (Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto)
            throws Exception {
        mx.smartteam.entities.PopVisitaCollection visitaCollection = null;

        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }

        visitaCollection = mx.smartteam.data.PopVisita.GetVisitsPending(context, usuario, proyecto);
        if (visitaCollection.size() == 0) {
            throw new NoVisitasPendientesPorSync();
        }

        return visitaCollection;
    }
    public static int existen_visitas_abiertas(Context context) {

        int existe = mx.smartteam.data.PopVisita.existen_visitas_abiertas(context);
        
        return existe;
    }
    
    public static mx.smartteam.entities.PopVisitaCollection ForzeUpChanges
            (Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception{
        mx.smartteam.entities.PopVisitaCollection visitaCollection = null;
        
        if (usuario == null){
            throw new Exception("Objecto usuario no referenciado");
        }
        
        if (proyecto == null){
            throw new Exception ("Objeto proyecto no referenciado");
        }
        
        visitaCollection = mx.smartteam.data.PopVisita.ForceUpChanges(context, usuario, proyecto);
        
        if (visitaCollection.size() > 0){
             throw new NoSeguirHastaSincronizar();
        }
        
        return visitaCollection;
    }    
       //Obitne visitas pendientes po sincronizar    
    public static mx.smartteam.entities.PopVisitaCollection NoborrarBD(Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception {
        mx.smartteam.entities.PopVisitaCollection visitaCollection = null;

        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }

     //   visitaCollection = mx.smartteam.data.PopVisita.GetVisitsPending(context, usuario, proyecto);
        //if (visitaCollection.size() == 0) {
             mx.smartteam.data.DeleteTables.EliminarTablas(context,usuario, proyecto);// Receteamos las tablas de la BD
        //}
       // else{
        // throw new NoSeguirHastaSincronizar();
        
        //}

        return visitaCollection;
    }
    

    public static void UpdateStatus(Context context, mx.smartteam.entities.PopVisita visita) throws Exception {


        if (visita != null) {
            mx.smartteam.data.PopVisita.UpdateStatus(context, visita);

        }



    }

    public static void UpdateStatusAndCord(Context context, mx.smartteam.entities.PopVisita visita) throws Exception {

        if (visita != null) {
            mx.smartteam.data.PopVisita.UpdateStatusAndCord(context, visita);

        }
    }
    
    
    public static String ChangeStatusStore(Context context, mx.smartteam.entities.Proyecto proyecto , mx.smartteam.entities.Usuario usuario) throws Exception {
            String result = null;
            
            mx.smartteam.data.PopVisita.ChangeStatusVisit(context, proyecto, usuario); //RespuestaSondeo.UpdateStatusSync(context, contestacion);
            return result;
        }
    
      public static void FindOpenStore(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }
      mx.smartteam.entities.PopVisitaCollection visitaCollection = null;    
      
      visitaCollection = mx.smartteam.data.PopVisita.FindOpenStore(context, proyecto, usuario);
          
        if (visitaCollection.size() > 0) {
           throw new PopException.DebesSincronizar();
        }
    }
    
    public static mx.smartteam.entities.PopVisitaCollection go_to_Sincronitation(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }
        mx.smartteam.entities.PopVisitaCollection visitaCollection = null;    
      
         visitaCollection = mx.smartteam.data.PopVisita.FindOpenStore(context, proyecto, usuario);
          
       return visitaCollection;
    } 
      
      public static void OnlyTablet(Context context, mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }
      mx.smartteam.entities.PopVisitaCollection visitaCollection = null;    
      
      visitaCollection = mx.smartteam.data.PopVisita.OnlyTablet(context, proyecto, usuario);
          
    if (visitaCollection.size() > 0) {
       throw new PopException.DebesSincronizar();
    }
    }      
      

    public static void VerifyStatusMarket(Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto)throws Exception {
        mx.smartteam.entities.PopVisitaCollection visitaCollection = null;
        if (usuario == null) {
            throw new Exception("Object usuario no referenciado");
        }

        if (proyecto == null) {
            throw new Exception("Object proyecto no referenciado");
        }
    visitaCollection = mx.smartteam.data.PopVisita.VerifyStatusMarket(context, proyecto, usuario);
    if (visitaCollection.size() > 0) {
       throw new PopException.NoAbrir();     
    
    }
//        else{
//         throw new Exception("NOO puedes pasar");
//        }
    
    }
    
    
    
    public static class Upload {

        public static void Insert(mx.smartteam.entities.PopVisita popVisita) throws Exception {

            if (popVisita == null) {
                throw new Exception("Object popvisita no referenciado");
            }
            mx.smartteam.data.PopVisita.Upload.Insert(popVisita);
        }

        public static void AsistenciaEntrada(mx.smartteam.entities.PopVisita popVisita) throws Exception {
            if (popVisita == null) {
                throw new Exception("Object popvisita no referenciado");
            }
            if (popVisita.FotoEntrada != null || popVisita.FechaEntrada != null) {
                mx.smartteam.data.PopVisita.Upload.AsistenciaEntrada(popVisita);
            }
        }

        public static void AsistenciaSalida(mx.smartteam.entities.PopVisita popVisita) throws Exception {

            if (popVisita == null) {
                throw new Exception("Object popvisita no referenciado");
            }

            if (popVisita.FotoSalida != null && popVisita.FechaSalida != null) {
                mx.smartteam.data.PopVisita.Upload.AsistenciaSalida(popVisita);
            }
        }
    }
    
    public static String CloseDay(Context context, mx.smartteam.entities.Pop popVisita) throws Exception{
    
        if (popVisita==null) {
            throw new Exception("Object [popVisita] no referenciado");
        }
        String visita =  mx.smartteam.data.PopVisita.CloseDay(context, popVisita);
 
        return visita;
    
    }
    
    
    
    public static mx.smartteam.entities.PopVisitaCollection GetAll(final Context context, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Proyecto proyecto) throws Exception {
        if(usuario == null){
            throw new Exception("Object [popVisita] no referenciado");        
        }
        
        if(proyecto == null){
                    throw new Exception("Object [popVisita] no referenciado");
        }
        PopVisitaCollection visitas = mx.smartteam.data.PopVisita.GetAll(context, usuario, proyecto);
        return visitas;
    }
    

}//END CLASS
    
