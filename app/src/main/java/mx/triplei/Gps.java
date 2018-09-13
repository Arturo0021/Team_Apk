/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.triplei;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class Gps extends Service implements LocationListener {

    private final Context mContext;
    // flag for GPS status
    private boolean isGPSEnabled = false;
    // flag for network status
    private boolean isNetworkEnabled = false;
    // flag for GPS status
    private boolean canGetLocation = false;
    public Location Location; // location
    private double latitude = 0; // latitude
    private double longitude = 0; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 3000; // 1 minute
    // Declaring a Location Manager
    protected LocationManager locationManager;
    Sucursal sucursal =new Sucursal();

    public Gps(Context context) {
        this.mContext = context;
        //Instacionamos el servicion de localizacion
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // Obtenemos el status del GPS
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //Obtenermos el status de Red
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //Verificamos que los servicios de localizacion esten activos e iniciar la el servicio
        if (!isGPSEnabled || !isNetworkEnabled) {
            ShowSettings();
        } else {
            StartUsingGPS();
            //locationManager.removeUpdates(Gps.this);
        }

        //getLocation();
    }

    public boolean GPSEnabled() {
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return false;
    }

    public boolean NetworkEnabled() {
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }

    public void StartUsingGPS() {
        try {
            if (isGPSEnabled) {
                if (locationManager != null) {

                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (location != null) {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }

                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "StartUsingGPS" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void StartUsingNetwork() {
        try {
            if (isNetworkEnabled) {
                if (locationManager != null) {

                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (location != null) {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "StartUsingNetwork" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public Location getLocation() {
        try {

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                if (isGPSEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (Location != null) {
                            latitude = Location.getLatitude();
                            longitude = Location.getLongitude();
                        }
                    }

                }
                if (isNetworkEnabled) {
                    if (Location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            Location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (Location != null) {
                                latitude = Location.getLatitude();
                                longitude = Location.getLongitude();
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            Toast.makeText(this, "getLocation" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        return Location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     *
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(Gps.this);
        }
    }

    /**
     * Function to get latitude
     *
     */
    public double getLatitude() {
        if (Location != null) {
            latitude = Location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     *
     */
    public double getLongitude() {
        if (Location != null) {
            longitude = Location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     *
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     *
     */
    public void ShowSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        Drawable d=sucursal.setIconAlert(mContext);
        alertDialog.setIcon(d);
        // Setting Dialog Title
        alertDialog.setTitle( Html.fromHtml("<font color='#FFFFF'>Servicios de ubicación</font>"));   
        //alertDialog.setTitle("Servicios de ubicación");
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage("El GPS o la ubicación por Wi-Fi estan desactivadas, verifique por favor");

        // On pressing Settings button
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null) {
                this.Location = location;
                //Toast.makeText(mContext, "Latitud="  + location.getLatitude() + " Longitud=" + location.getLongitude(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "onLocationchange" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
