package mx.triplei;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.smartteam.entities.SeguimientoGps;
import mx.smartteam.exceptions.UsuarioException.LogException;

public class ServicioGps extends Service 
{
    Handler newhandler = new Handler();
    LocationManager myLocationManager;
    String PROVIDER = LocationManager.NETWORK_PROVIDER;
    Location location;
     mx.smartteam.entities.Log log = null;
     int  determinanteGsp=0;
     public  int Usuario =0;
    @Override
    public void onCreate() 
    {
        myLocationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        myLocationManager.removeUpdates(myLocationListener);
        location = myLocationManager.getLastKnownLocation(PROVIDER);
              
        try {
            showMyLocation(location, log);
        } catch (Exception ex) {
            Logger.getLogger(ServicioGps.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) 
    {
        String tienda = intenc.getStringExtra("username");
        if (tienda != "" && tienda != null)
        {
            determinanteGsp = Integer.parseInt(tienda);   
        }
        
        if(determinanteGsp!=0){
        myLocationManager.removeUpdates(myLocationListener);
        }
        newhandler.removeCallbacks(theTime);
        newhandler.postDelayed(theTime, 1000);
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        
        determinanteGsp = 0;
        super.onDestroy();
        //Toast.makeText(this, "Servicio destruido", Toast.LENGTH_SHORT).show();
    }
    
    
    
    public Runnable theTime = new Runnable()
    {
        public void run()
        {         
            try 
            {
                log = mx.smartteam.business.Log.GetLog(getApplicationContext());
            } catch (LogException ex) {
                Logger.getLogger(ServicioGps.class.getName()).log(Level.SEVERE, null, ex);
            }
                myLocationManager.requestLocationUpdates(PROVIDER, 30 * 60 * 1000, 1000, myLocationListener);
                newhandler.postDelayed(this, 30 * 60 * 1000);
        }
    };

    
    private void showMyLocation(Location l, mx.smartteam.entities.Log log1) throws Exception 
    {
        
        if(log1 != null)
        {
            if (l != null) 
            {
                mx.smartteam.entities.SeguimientoGps seguimientoGps = null;
                        seguimientoGps = new mx.smartteam.entities.SeguimientoGps();
                seguimientoGps.ProyectoId = log1.ProyectoId;
                seguimientoGps.UsuarioId = log1.UsuarioId;
                seguimientoGps.Latitud = l.getLatitude();
                seguimientoGps.Longitud = l.getLongitude();
                seguimientoGps.determinanteGsp = determinanteGsp;
                if(seguimientoGps.determinanteGsp != 0){
                mx.smartteam.business.SeguimientoGps.Insert(getApplicationContext(), seguimientoGps);
                }
              // Toast.makeText(getApplicationContext(),  " ---- Latitude: " + l.getLatitude() + " ---- " + "Longitude: " + l.getLongitude(), Toast.LENGTH_LONG).show(); 
                
            }

        }
        
        
        
    }

    
    // <editor-fold>
       @Override
    public IBinder onBind(Intent intencion) 
    {
        return null;
    }
    
    private LocationListener myLocationListener = new LocationListener() 
    {
        @Override
        public void onLocationChanged(Location location) 
        {
            try {
                if(determinanteGsp!=0){
                showMyLocation(location, log);
                }
            } catch (Exception ex) {
                Logger.getLogger(ServicioGps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void onProviderDisabled(String provider) 
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) 
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) 
        {
            // TODO Auto-generated method stub
        }
    };
    
    // </editor-fold>
}