/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.business;

import android.content.Context;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.JobCollection; 
import mx.smartteam.exceptions.UsuarioException;
import mx.smartteam.exceptions.UsuarioException.JobException;

        
/**
 *
 * @author jaime.bautista
 */
public class Job {
    
    public static void Insert(Context context) {

        mx.smartteam.data.Job.Insert(context);

    }
    
    public static mx.smartteam.entities.JobCollection GetbyJob(Context context) {
        mx.smartteam.entities.JobCollection jobCollection = new JobCollection();
        try {

            jobCollection = mx.smartteam.data.Job.GetbyJob(context);


        } catch (JobException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jobCollection;
    }
    
    
    public static void EliminarRegistros(Context context) throws JobException {
        try {
                
                mx.smartteam.data.Job.BorrarRegistros(context);
                
        } catch (Exception e) {
            throw new UsuarioException.JobException();
        }
    
    
    }
    
}
