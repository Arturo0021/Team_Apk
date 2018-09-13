package mx.triplei;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import mx.triplei.R;
import mx.smartteam.exceptions.UsuarioException;

public class MainActivity extends Activity {
    mx.smartteam.entities.Usuario entityUsuario;
    mx.smartteam.entities.Proyecto entityProyecto;
    private ProgressBar secondBar = null;
    private TextView var_text;
    private int progressStatus = 0;
    private SharedPreferences preferences;
    private Handler handler = new Handler();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        context = this;
         secondBar = (ProgressBar)findViewById(R.id.secondBar);
         var_text = (TextView)findViewById(R.id.var_text);
         
         preferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
         
            entityUsuario = new mx.smartteam.entities.Usuario();
            
        Boolean existe_package = Utilerias.packageExists("mx.smartteam", context);

    if (existe_package) {
        Uri package_del = Uri.parse("package:mx.smartteam");
        Intent uninstall = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, package_del);
        startActivity(uninstall);
        finish();
    } else {
            
            try {
            
                entityUsuario = mx.smartteam.business.Usuario.GetByUsuario2(context, Utilerias.getUsuarioPreference(preferences));
                context = this;
                secondBar.setVisibility(View.VISIBLE);

                /*Drawable progressDrawable = secondBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(Color.MAGENTA, android.graphics.PorterDuff.Mode.MULTIPLY );
                secondBar.setProgressDrawable(progressDrawable);*/
            
            } catch(Exception e) {
                e.getMessage();
            }
         
         
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Intents Valida a que pantalla apuntar dependiendo de la condicion
                    mx.smartteam.entities.VersionUsuario versionUsuario = mx.smartteam.business.VersionesUsuarios.getVersionUsuario(context);
                    Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                    Intent intentSucursal = new Intent(MainActivity.this, Sucursal.class);
                    
                    int numero = (int) (Math.random() * 100) + 1;
                     while (progressStatus < 100) {
                        progressStatus += 1;                  
                        handler.post(new Runnable() {
                            public void run() {
                                secondBar.setProgress(progressStatus);
                                var_text.setText("Cargando...        " + progressStatus + " %");
                            }
                        });
                         Thread.sleep(numero);
                    }
                    
                if(versionUsuario != null) { // Validar este IF
                    
                    String version_apk = getResources().getString(R.string.version_comparar_server_vs_apk);
                    
                    if(!TextUtils.isEmpty(Utilerias.getUsuarioPreference(preferences)) && !TextUtils.isEmpty(Utilerias.getPasswordPreference(preferences)) && versionUsuario.Actual == Integer.parseInt(version_apk))
                    {
                        
                        entityProyecto = mx.smartteam.data.UsuarioProyecto.GetProyectoByUsuario(context, entityUsuario);
                        intentSucursal.putExtra("proyecto", entityProyecto);
                        intentSucursal.putExtra("usuario", entityUsuario);
                        startActivity(intentSucursal);
                        finish();
                    
                    } else {
                        
                        removePreference();
                        startActivity(intentLogin);
                        finish();
                   
                     }
                } else {
                    
                    if(!TextUtils.isEmpty(Utilerias.getUsuarioPreference(preferences)) && !TextUtils.isEmpty(Utilerias.getPasswordPreference(preferences)))
                    {
                        
                        entityProyecto = mx.smartteam.data.UsuarioProyecto.GetProyectoByUsuario(context, entityUsuario);
                        intentSucursal.putExtra("proyecto", entityProyecto);
                        intentSucursal.putExtra("usuario", entityUsuario);
                        startActivity(intentSucursal);
                        finish();
                    
                    } else {
                        
                        removePreference();
                        startActivity(intentLogin);
                        finish();
                   
                     }
                        
                }    
                     
                     
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }).start();
    }
  }
    
    private void removePreference() {
         preferences.edit().clear().apply();
    }
    
}
