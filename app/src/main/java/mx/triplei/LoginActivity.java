package mx.triplei;

import mx.smartteam.exceptions.UsuarioException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Version;

public class LoginActivity extends Activity {

    String apk = ".apk";
    private SQLiteDatabase db = null;
    private Context context;
    private EditText txtUsuario, txtPass;
    private String status;
    protected static final String TAG = "DownloadMgr";
    private DownloadManager dMgr;
    private long downloadId;
    String urldownload;
    String nombre;
    String versionstring;
    int version;
    boolean device, autoTimeZone,autoTime;
    private int t = 0;
    Sucursal sucursal; 
    private ImageView logo=null;
    private int band = 0 ;
    SharedPreferences preferences;
    
    @Override
    public void onCreate(Bundle icicle) {
        DeleteApk();        
        super.onCreate(icicle);   
        this.context = this;
        // Generate SharedPreference
        preferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
     
        setContentView(R.layout.activity_login);
        device = Utilerias.isTablet(context) ;
        autoTime = IsAutoTimeEnabled();
        autoTimeZone = IsAutoTimeZoneEnabled();
        versionstring = getString(R.string.version_actual);
        version = Integer.parseInt(versionstring);
        
        Toast.makeText(context, "Versión Actual " + version, Toast.LENGTH_SHORT).show();
        this.getActionBar().setDisplayShowTitleEnabled(false);
        this.getActionBar().setDisplayShowHomeEnabled(false);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPass = (EditText) findViewById(R.id.txtPass);
        
        ImageView logo = (ImageView) findViewById(R.id.logologin);
        
        getOverflowMenu();

        try {//Job
            mx.smartteam.entities.JobCollection jobcollection =   mx.smartteam.business.Job.GetbyJob(context);
            if(jobcollection.size() == 0){
                //Procedimiento
                mx.smartteam.business.Job.EliminarRegistros(context);
                mx.smartteam.business.Job.Insert(context);
            }
            
            new Thread(new findUpdates()).start();
            
        } catch (Exception a) {
            MessageForUpdate("La Base de Datos NO se \n encuentra actualizada");
        }

        GlobalSettings state = ((GlobalSettings) getApplicationContext());
        state.verNotificaciones();
        status = (String.valueOf(state.valorconseguirNotificacion()));
        try {
            mx.smartteam.entities.VersionCollection versionCollection = mx.smartteam.business.Version.GetByVersion(context);

        } catch (Exception ex) {
            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        File mediaStorageDir = new File("/mnt/sdcard/Pictures/Team");
        Utilerias.deleteDirectory(mediaStorageDir);
         
        
        if(device == false)
        {
            if(autoTime== false)
             {
            Intent i = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                   startActivity(i);
            }
            
            if(autoTimeZone== false)
            {
                Intent i = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                       startActivity(i);
            }
        }
        
        setTitle("Pruebas");
        setTitleColor(Color.RED);
        
    }

    
    public boolean IsAutoTimeEnabled() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        // For JB+
        return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) > 0;
    }
    // For older Android versions
    return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME, 0) > 0;
    }
    
    
     public boolean IsAutoTimeZoneEnabled() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        // For JB+
        return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) > 0;
    }
    // For older Android versions
    return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0) > 0;
    }
     
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_entrar:
                OnEntrar();
                break;

            case R.id.action_sincronizar:
                OnSincronizar();
                break;

            case R.id.acerca_de:
                Acercade();
                break;

        }
        return true;
    }

    public void OnEntrar() {
        try {
            if (txtUsuario.getText().toString().isEmpty()){
                throw new UsuarioException.UserEmpty();
            }

            if (txtPass.getText().toString().isEmpty()){
                throw new UsuarioException.PassEmpty();
            }
            // Guarda las Credenciales en el SharedPreference    
            savePreference(txtUsuario.getText().toString(), txtPass.getText().toString());
                
            mx.smartteam.entities.Usuario entityUsuario = new mx.smartteam.entities.Usuario();
            entityUsuario.Usuario = txtUsuario.getText().toString().trim();
            entityUsuario.Password = txtPass.getText().toString().trim();

            new GetUsuarioDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entityUsuario);

        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void OnSincronizar() {
        try {
            if (txtUsuario.getText().toString().isEmpty()) {
                throw new UsuarioException.UserEmpty();
            }

            if (txtPass.getText().toString().isEmpty()) {
                throw new UsuarioException.PassEmpty();
            }

            mx.smartteam.entities.Usuario EntityUsuario = new mx.smartteam.entities.Usuario();
            EntityUsuario.Usuario = txtUsuario.getText().toString();
            EntityUsuario.Password = txtPass.getText().toString();

            new GetUsuario().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, EntityUsuario);
            
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        } 
    }

    class GetUsuarioDB extends AsyncTask<mx.smartteam.entities.Usuario, Void, mx.smartteam.entities.Usuario> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Validando usuario</font>"));
            //pd.setTitle("Validando usuario");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected mx.smartteam.entities.Usuario doInBackground(mx.smartteam.entities.Usuario... arg0) {
            mx.smartteam.entities.Usuario entityUsuario = mx.smartteam.data.Usuario.GetByUsuario(context, arg0[0]);
            return entityUsuario;
        }

        @Override
        protected void onPostExecute(mx.smartteam.entities.Usuario entityUsuario) {
            pd.dismiss();
            try {
                if (entityUsuario == null) {
                    throw new UsuarioException.ErrorAutenticar();
                }

                mx.smartteam.entities.Proyecto entityProyecto = mx.smartteam.data.UsuarioProyecto.GetProyectoByUsuario(context, entityUsuario);
                
                if (entityProyecto == null) {
                    throw new UsuarioException.UsuarioSinProyecto();
                }
                
                if(device == true){ /* Solo para Tabletas */
                        mx.smartteam.business.PopVisita.OnlyTablet(context, entityProyecto, entityUsuario);
                }
                else{/*  Telefonos  */   
                        mx.smartteam.business.PopVisita.FindOpenStore(context, entityProyecto, entityUsuario);
                }

                  mx.smartteam.business.Log.Insert(context, entityUsuario, entityProyecto);
               
                Intent intent = new Intent(LoginActivity.this, Sucursal.class);
                intent.putExtra("usuario", entityUsuario);
                intent.putExtra("proyecto", entityProyecto);

                startActivity(intent);
                finish();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    class GetUsuario extends AsyncTask<mx.smartteam.entities.Usuario, Void, mx.smartteam.entities.Usuario> {

        private ProgressDialog pd;
        private Exception exception = null;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Validando credencial</font>"));
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected mx.smartteam.entities.Usuario doInBackground(mx.smartteam.entities.Usuario... arg0) {
            mx.smartteam.entities.Usuario EntityUsuario = null;
            try {
                EntityUsuario = mx.smartteam.business.Usuario.Autentication(arg0[0]);
            } catch (Exception ex) {
                exception = ex;
                Log.e("Autenticacion", ex.getMessage());
            }
            return EntityUsuario;
        }

        @Override
        protected void onPostExecute(mx.smartteam.entities.Usuario EntityUsuario) {
            pd.dismiss();
            if (this.exception != null) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {

                Intent intent = new Intent(LoginActivity.this, SincronizarActivity.class);
                intent.putExtra("usuario", EntityUsuario);
                startActivity(intent);
            }
        }
    }

    public void MessageForUpdate(String mensaje) {
        Toast msgt = new Toast(getApplicationContext());
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.lytLayout));
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMensaje);
        txtMsg.setText(mensaje);
        msgt.setDuration(Toast.LENGTH_LONG);
        msgt.setView(layout);
        msgt.show();
    }

    private void DeleteApk() {
        File file = new File("/mnt/sdcard/external_sd/Team.apk");
        boolean deleted = file.delete();
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoTime = IsAutoTimeEnabled();
        autoTimeZone = IsAutoTimeZoneEnabled();
        dMgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        
        if(device == false){
            if(autoTime== false){
                Intent i = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                startActivity(i);
                autoTime = IsAutoTimeEnabled();
                autoTimeZone = IsAutoTimeZoneEnabled();
            }
            if(autoTimeZone== false){
                Intent i = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                startActivity(i);
                autoTime = IsAutoTimeEnabled();
                autoTimeZone = IsAutoTimeZoneEnabled();
            }
        }
        
        /*if(band == 0){
            new Thread(new findUpdates()).start();
        }*/
    }
    
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        private String Tag;
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            long doneDownloadId = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
            if (downloadId == doneDownloadId) {
                Log.v(TAG, "Mensaje de consola de Descarga Finalizada.");
            }
        }
    };

    public void Actualizacion(final Version version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("IMPORTANTE. Verificar si hay información por subir, ya que se perderá permanentemente.  \n  ")
            .setTitle("Instalador Team")
            .setCancelable(false)
            .setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
            .setPositiveButton("Continuar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fileName = Environment.getExternalStorageDirectory() + "/external_sd/"+version.nombreapk+".apk";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class DowmloadInstall extends AsyncTask<Object, Object, Object> {
        private ProgressDialog pd;
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Download App</font>"));
            //pd.setTitle("Download App");
            pd.setMessage("Espere por favor...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected Object doInBackground(Object... paramss) {
            try {
                for (int i = 0; i <= 30; i++) {
                    Thread.sleep(1100);
                    if (i == 15) {
                        String fileName = Environment.getExternalStorageDirectory() + "/external_sd/Team.apk";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            pd.dismiss();

        }
    }

    public void Acercade() {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        String acercade = getString(R.string.acerca_de);
        final View acercadeLayout = inflater.inflate(R.layout.acercade, null);
        final WebView webview = (WebView) acercadeLayout.findViewById(R.id.webView1);
            webview.setBackgroundColor(Color.GREEN);
            String htmlText = "<html><body style=\"text-align:justify\"> </body></Html>";
            String myData = acercade;
            String sversion = "<b>Versi&oacute;n instalada " + version + "</b>";
            webview.loadData(String.format(htmlText, myData, sversion), "text/html", "utf-8");
        alertDialog.setCancelable(false);
        Drawable d=setIconAlert();
        alertDialog.setIcon(d);
        alertDialog.setTitle( Html.fromHtml("<font color='#FFFFF'>Acerca de TEAM</font>"));
        alertDialog.setMessage(Html.fromHtml(htmlText+ myData+"<br><br>" +sversion));
        //alertDialog.setView(acercadeLayout);
        alertDialog.setButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }//END ACERCADE           
   
    public void etReset(){
        //Quitamos el contenido 
        txtUsuario.setText(null);
        txtPass.setText(null);
    }
    
    public Bitmap setIconLogin(){
         Drawable d=null;
         byte[] decodedString=null;
         Bitmap decodedByte=null;
         String image=""; 
         try{
             decodedString = Base64.decode(image, Base64.DEFAULT);
             decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
         }catch(Exception e){
             e.getMessage();
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();             
         }     
         return decodedByte;
     }
    
    public Drawable setIconAlert(){
         Drawable d=null;
         byte[] decodedString=null;
         Bitmap decodedByte=null;
         String image=""; 
         try{
             //image = "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";
             image = "iVBORw0KGgoAAAANSUhEUgAAADoAAAA5AQMAAABd4arFAAAAAXNSR0ICQMB9xQAAAAZQTFRFAAAA////pdmf3QAAAAF0Uk5TAEDm2GYAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAZdEVYdFNvZnR3YXJlAE1pY3Jvc29mdCBPZmZpY2V/7TVxAAAA/UlEQVQoz02RUW7EIAxEB/HB33KDcpPlYI0ajobUiyD1Auwf1UZxZyCRGkXwBDb2jAFEM8xvN2vanZkNQSCcgkSYQbk9YJVgeCAy2h1ICJ0wCJ5pvhNACA0Z2AgVueDJSkXAQwbsRSv/d9XK23dTgKAjFCa6V1fuBv8aC0I7FsR6wgtS3RfsJZcF7EvgToQhiAf8Kcid0r4rdTaJFRRpE+CGuq42U/APIQ14U3o4EeeDzpDaXbTebazqsX2tftSqX81f4JaKJ9xvXyCllPwBfDakacg+3ZBRC27rGJ/kCXzT+XH5TNc1gjhHADaIKMg16qVrcOX/KP09XNgc9x//laTLS8+NhQAAAABJRU5ErkJggg==";
             decodedString = Base64.decode(image, Base64.DEFAULT);
             decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
             d = new BitmapDrawable(getResources(), decodedByte);
         }catch(Exception e){
             e.getMessage();
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();             
         }     
         return d;
    }
    
    class findUpdates implements Runnable {
        public void run() {
            try {
                Looper.prepare();
                mx.smartteam.entities.VersionUsuario versionUsuario = mx.smartteam.business.VersionesUsuarios.getVersionUsuario(context);
                if(versionUsuario != null){
                    
                    String version_apk = getResources().getString(R.string.version_comparar_server_vs_apk);
                    
                    if(versionUsuario.Actual != Integer.parseInt(version_apk)) {
                        mx.smartteam.entities.Version version = mx.smartteam.business.Version.getVersion(context, versionUsuario.Nueva);
                        
                        if(version != null) {
                            if(version.version != Integer.parseInt(getString(R.string.version_actual)) ){
                                urldownload = version.url;
                                nombre = version.nombreapk;
                                    //verificamos si el archivo existe
                                String rutapath = Environment.getExternalStorageDirectory()+ "/external_sd/" +nombre+".apk";
                                File file = new File(rutapath);

                                if(file.exists()){ /* Si existe debemos preguntar si desea instalar */
                                    Actualizacion(version);
                                    
                                } else { /* No exite debemos descargar el archivo del servidor */
                                    URL u = new URL(urldownload + nombre +".apk");
                                    InputStream is = u.openStream();
                                    DataInputStream dis = new DataInputStream(is);
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/external_sd/" + nombre + ".apk"));
                                    while ((length = dis.read(buffer))>0) {
                                        fos.write(buffer, 0, length);
                                    }
                                }
                                band = 1;                            
                            
                            }
                        }
                    }
                }
                Looper.loop();
            } catch (Exception ex) {
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void savePreference(String usuario, String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", usuario.trim());
        editor.putString("pass", pass.trim());
        editor.apply();
    }
    
    private void setCredencialesidExists() {
        String user = Utilerias.getUsuarioPreference(preferences);
        String pass = Utilerias.getPasswordPreference(preferences);
        
        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)) {
            txtUsuario.setText(user);
            txtPass.setText(pass);
        }
       
    }
    
} //END CLASS LOGIN

