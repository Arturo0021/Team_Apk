/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.triplei;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;
import mx.smartteam.settings.ServiceClient;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author edgarin.lara
 */
public class Notificaciones extends IntentService {

    private String usuario = null, proyecto = null, tienda = null;
    private Intent intent;

    public Notificaciones() {
        super("Servicio de notificaciones");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent = intent;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            this.usuario = bundle.getString("usuario");
            this.proyecto = bundle.getString("proyecto");
            this.tienda = bundle.getString("tienda");

            new ObtenerNotificacion().execute(this.usuario, this.proyecto, this.tienda);



        }


        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ObtenerNotificacion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... param) {
            try {

                ServiceClient.Notificacion notificacion = new ServiceClient.Notificacion(param);// new ServiceClient.Folios.CerrarDia(param);

                return notificacion.GetResult();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Notificaciones.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Notificaciones.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Notificaciones.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(Notificaciones.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;


        }

        @Override
        protected void onPostExecute(String resultado) {

            if (!resultado.isEmpty()) {
                Bundle extras = intent.getExtras();
                Messenger messenger = (Messenger) extras.get("messenger");

                Message msg = Message.obtain();
                msg.obj = resultado;
                try {
                    messenger.send(msg);
                } catch (RemoteException ex) {
                    Logger.getLogger(Notificaciones.class.getName()).log(Level.SEVERE, null, ex);
                }



            }

        }
    }
}
