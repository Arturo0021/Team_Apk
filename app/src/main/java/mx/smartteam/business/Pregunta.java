/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.entities.PreguntaCollection;


public class Pregunta {

    public static class Download {

        public static PreguntaCollection GetPreguntasBySondeo(mx.smartteam.entities.Proyecto proyecto, mx.smartteam.entities.Usuario usuario, mx.smartteam.entities.Sondeo sondeo, String time)
                throws Exception {

            if (proyecto == null) {
                throw new Exception("Object proyecto no referenciado");
            }

            PreguntaCollection preguntaCollection = mx.smartteam.data.Pregunta.Download.GetPreguntas(proyecto, usuario, sondeo, time);

            return preguntaCollection;

        }
    }

    public static void Insert(Context context, mx.smartteam.entities.Pregunta pregunta) throws Exception {

        if (pregunta == null) {
            throw new Exception("Object pregunta no referenciado");
        }
        mx.smartteam.data.Pregunta.Insert(context, pregunta);

    }

    public static mx.smartteam.entities.PreguntaCollection GetPreguntaBySondeo(Context context, mx.smartteam.entities.SondeoModulos sondeo) throws Exception {
        PreguntaCollection preguntaCollection;

        if (sondeo == null) {
            throw new Exception("Object sondeo no referenciado");
        }

        preguntaCollection = mx.smartteam.data.Pregunta.GetPreguntaBySondeo(context, sondeo);

        return preguntaCollection;
    }

     public static mx.smartteam.entities.PreguntaCollection GetPreguntaDinamica(Context context, mx.smartteam.entities.SondeoModulos sondeo, mx.smartteam.entities.Pop pop) throws Exception {
        PreguntaCollection preguntaCollection;

        if (sondeo == null) {
            throw new Exception("Object sondeo no referenciado");
        }
        if(pop == null){
            throw new Exception("Object tienda no referenciado");
        }

        preguntaCollection = mx.smartteam.data.Pregunta.GetPreguntaDinamica(context, sondeo, pop);

        return preguntaCollection;
    }

    public static mx.smartteam.entities.PreguntaCollection GetNPreguntaDinamica(Context context, mx.smartteam.entities.SondeoModulos sondeo, mx.smartteam.entities.Pop pop,
                                                                                mx.smartteam.entities.Opcion nopcion, mx.smartteam.entities.Pregunta pregunta1)throws Exception {
        PreguntaCollection preguntaCollection;

        if (sondeo == null) {
            throw new Exception("Object sondeo no referenciado");
        }
        if(pop == null){
            throw new Exception("Object tienda no referenciado");
        }

        preguntaCollection = mx.smartteam.data.Pregunta.GetNPreguntaDinamica(context, sondeo, pop,nopcion, pregunta1);

        return preguntaCollection;
    }
    public static mx.smartteam.entities.Pregunta GetPreguntaTipo3(Context context, mx.smartteam.entities.Sondeo sondeo) throws Exception {
        
        if (sondeo == null) {
            throw new Exception("Object sondeo no referenciado");
        }
        mx.smartteam.entities.Pregunta pregunta=new mx.smartteam.entities.Pregunta();
        pregunta=mx.smartteam.data.Pregunta.GetPreguntaTipo3(context, sondeo);
        
    return pregunta;
    } 

    public static mx.smartteam.entities.Pregunta GetPreguntaNContestaciones(Context context, mx.smartteam.entities.Sondeo sondeo) throws Exception 
    {    
        if (sondeo == null) {
            throw new Exception("Object sondeo no referenciado");
        }
        mx.smartteam.entities.Pregunta pregunta=new mx.smartteam.entities.Pregunta();
        pregunta=mx.smartteam.data.Pregunta.GetPreguntaNContestaciones(context, sondeo);
        
    return pregunta;
    }     
        
    
    public static PreguntaCollection GetPreguntaByContestacion(Context context, mx.smartteam.entities.Contestacion contestacion) {
        PreguntaCollection preguntaCollection;

        preguntaCollection = mx.smartteam.data.Pregunta.GetPreguntaByContestacion(context, contestacion);

        return preguntaCollection;
    }

}
