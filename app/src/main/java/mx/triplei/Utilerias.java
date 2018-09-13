/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.triplei;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author jaime.bautista
 */
public class Utilerias {

    public static String getFecha() { // Fecha
        Date dn = new Date();
        String fecha = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(dn);
        return fecha;
    }
    
    public static int ValidarFechas(String fechaVisita){
        if(!"".equals(fechaVisita)){
            String fechahoy = getFecha();
            if(fechahoy.equals(fechaVisita)){
                return 1;
            }
        }
        return 0;
    }
    
    //

    public static String getFechaHora() { //Fecha hora y minuto
        Date dn = new Date();
        String hora;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        hora = sdf.format(dn);
        return hora;
    }

    public static Long getFechaUnix() { // fecha UNIX
        long fechac = System.currentTimeMillis() / 1000;

        return fechac;
    }

    public static String getFechaHoraandSeg() { //Fecha, Hora, Min y Segundo
        Date dn = new Date();
        String hora;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hora = sdf.format(dn);
        return hora;
    }

    public static boolean deleteDirectory(File path) { // Elimina archivos de un directorio
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }

            for (File child : files) {
                boolean deleted = child.delete();
            }

        }

        path.delete();
        return (path.delete());
    }

    public static String SumaFecha() { // Te suma dias a la fecha actual 

        Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int calendarTime = Calendar.DAY_OF_MONTH;
            int temp = calendar.get(calendarTime);
            calendar.set(calendarTime, temp + 15);
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat();
            formatoFecha.setTimeZone(TimeZone.getTimeZone("GMT-6"));
            Date fechaSum = calendar.getTime();
            formatoFecha.applyPattern("dd/MM/yyyy HH:mm:ss");
            String fechaRespuesta = formatoFecha.format(fechaSum);

        return fechaRespuesta;
    }
   
    public static long SumaFechaUnix(){

    long fecha = System.currentTimeMillis() / 1000;
        fecha= fecha + 172800;// 1209600;cada semana 604800
    return fecha ;
    }
    
            
    public static double sizeFileMB() {

        File file = new File("/mnt/sdcard/external_sd/smartteam");  // Get length of file in bytes
        double fileSizeInBytes = file.length();  // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        double fileSizeInKB = fileSizeInBytes / 1000;  // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        double fileSizeInMB = fileSizeInKB / 1000;
        return fileSizeInMB; //Retornamos el tamaño de la base de datos
        
    }
    
    
     public static boolean isTablet(Context context) { /*   Verificamos si es tablet si es tableta retorna 'true'    */
    return (context.getResources().getConfiguration().screenLayout
            & Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
  }
     
     
     
    public static String RestarFecha() { // Te suma dias a la fecha actual 

        Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int calendarTime = Calendar.DAY_OF_MONTH;
            int temp = calendar.get(calendarTime);
            calendar.set(calendarTime, temp - 8);
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat();
            formatoFecha.setTimeZone(TimeZone.getTimeZone("GMT-6"));
            Date fechaSum = calendar.getTime();
            formatoFecha.applyPattern("yyyy-MM-dd HH:mm:ss");
            String fechaRespuesta = formatoFecha.format(fechaSum);

        return fechaRespuesta;
    }
     
     
    public static String UnixToDate(long fecha){
        long timeStamp = (long) fecha * 1000;
        Date d = new Date(timeStamp);
        SimpleDateFormat formatoFecha = new SimpleDateFormat();
        formatoFecha.setTimeZone(TimeZone.getTimeZone("GMT-6"));
        Date fechaSum = d;
        formatoFecha.applyPattern("yyyy-MM-dd");
        String fechaRespuesta = formatoFecha.format(fechaSum);
        return fechaRespuesta;
        
    }
     
    public static Boolean packageExists(String package_Name, Context context){

        PackageManager pm = context.getPackageManager();

        try {
            pm.getPackageInfo(package_Name, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    
    public static String getUsuarioPreference(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("user", null);
    }
    
    public static String getPasswordPreference(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("pass", null);
    }
     
}
