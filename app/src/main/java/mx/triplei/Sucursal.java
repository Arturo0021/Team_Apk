package mx.triplei;

import android.app.ActionBar;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.Pop;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.CategoriasConfigCollection;
import mx.smartteam.entities.Formulario;
import mx.smartteam.entities.MarcaConfigCollection;
import mx.smartteam.entities.NotificacionesCollection;
import mx.smartteam.entities.Pop.AdapterItemsIco;
import mx.smartteam.entities.ProductoCadenaCollection;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.exceptions.PopException;

public class Sucursal extends Activity {
   
    public String _statusdeVisita;
    public String _statusEnVisita;
    public String _statusVisitado;
    // usuario actual
    private mx.smartteam.entities.Usuario currentUsuario = null;
    // proyecto actual
    private mx.smartteam.entities.Proyecto currentProyecto = null;
    // Tienda actual
    private mx.smartteam.entities.Pop currentPop = null;
    private Integer IdUsuario = 0;
    private Integer IdProyecto = 0;
    private EditText txtFolio = null;
    private Pop pop = null;
    private mx.smartteam.entities.MenuCollection MenuOpe;
    GlobalSettings appConfiguration;
    //private PopCollection Tiendas;
    private Context context=null;
    private TextView var_nombre;
    private ListView ListTiendas;
    public String dd, comstated, compared, mensajeinicial, status, statusver;
    ProgressDialog progressDoalog;
    private Gps gps = null;
    int aumentar = 0;
    int estadogps = 1;
    
    SharedPreferences preferences;
    
    //int executeThread=0;
   boolean device, autoTimeZone,autoTime;
   public mx.smartteam.entities.consicCollection consicCollection = null;
    String time = null;
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursal);
        
        context = this;
        preferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        currentUsuario = (mx.smartteam.entities.Usuario)getIntent().getSerializableExtra("usuario");
        currentProyecto = (mx.smartteam.entities.Proyecto)getIntent().getSerializableExtra("proyecto");
        
        
        if(currentUsuario.Tipo == null || currentUsuario.Tipo == 0)
        {
            currentUsuario = mx.smartteam.data.Usuario.GetByUsuario(context, currentUsuario);
        }
        
        if(currentProyecto.Ficha == null || currentProyecto.Ficha ==0 || currentProyecto == null){
            currentProyecto = mx.smartteam.data.Proyecto.GetFicha(context, currentProyecto.Id);
        }
        
       
        // Recuperamos los objetos
        txtFolio = (EditText) findViewById(R.id.txtFolio);
        ListTiendas = (ListView) findViewById(R.id.ListTiendas);
        device = Utilerias.isTablet(context) ;
        var_nombre = (TextView)findViewById(R.id.var_nombre);
        
        try {
            mx.smartteam.entities.Usuario usuario = mx.smartteam.business.Usuario.GetByUsuario2(context, currentUsuario.Usuario);
            var_nombre.setText(usuario.Nombre + " - " + usuario.Usuario);
            
            // TODO: modulo pendien de Notificaciones
            mx.smartteam.entities.MovilConfigCollection movilConfigCollection = mx.smartteam.data.MovilConfig.GetByGpsConfig(context, currentUsuario);
            
            if(movilConfigCollection.size()>0){
                if (estadogps == movilConfigCollection.get(0).gpsconfig) {
                    gps = new Gps(context);
                    gps.StartUsingGPS();
                }else{
                    GetbyNotificaciones();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            /*  Busca Visitas asignadas el dia de hoy  */
            GetPopVisitsToday();

            GetPopVisits();
        } catch (Exception ex) {
            Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    private void ShowDialog(final Integer folio) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle( Html.fromHtml("<font color='#FFFFF'>Ficha T&eacute;cnica</font>"));
            //alertDialog.setTitle("Servicios de ubicación");
            Drawable d=setIconAlert(context);
            alertDialog.setIcon(d);
            alertDialog.setCancelable(false);// seguridad
            // Setting Dialog Message
            alertDialog.setMessage("Desea ver el resumen de la tienda?");
            // On pressing Settings button
            alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mx.smartteam.entities.Pop pop = new Pop();
                    pop.DeterminanteGSP = folio;
                    ReDirect(pop);
                }
            });
            // on pressing cancel button
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                     new BuscarFolio().execute(folio.toString());
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDoalog.incrementProgressBy(25);
        }
    };
    
    public void ReDirect(final mx.smartteam.entities.Pop popSelect){
                    
                    progressDoalog = new ProgressDialog(context);
                    progressDoalog.setCancelable(false);
                    progressDoalog.setMax(100);
                    progressDoalog.setTitle(Html.fromHtml("<font color='#FFFFF'>Cargando...</font>"));
                    progressDoalog.setMessage("Espere por favor...");
                    progressDoalog.show();
                    
                        new Thread(
                                new Runnable() {
                                    public void run() {
                                        
                                    try {
                                        
                                
            
                                        if(currentUsuario.Tipo == 3) {
                                            mx.smartteam.entities.TiendasConfigCollection ConfigCollection = mx.smartteam.business.TiendasConfig.DownloadConfig(currentProyecto);
                                            
                                            if(ConfigCollection.size() > 0){
                                                mx.smartteam.entities.TiendaAsistenciaCollection Asistenciacollection = mx.smartteam.business.TiendasAsistencia.DownloadAsistencia(currentProyecto, popSelect);
                                                handle.sendMessage(handle.obtainMessage());
                                                mx.smartteam.entities.TiendaSosCollection SosCollection = mx.smartteam.business.TiendaSos.DownloadSos(currentProyecto, popSelect);
                                                handle.sendMessage(handle.obtainMessage());
                                                mx.smartteam.entities.TiendaFrentesCollection FrentesCollection = mx.smartteam.business.TiendaFrentes.DownloadFrentes(currentProyecto, popSelect);
                                                handle.sendMessage(handle.obtainMessage());
                                                mx.smartteam.entities.TiendaExAdicionalCollection ExAdicCollection = mx.smartteam.business.TiendaExAdicional.DownloadExAdicional(currentProyecto, popSelect);
                                                handle.sendMessage(handle.obtainMessage());

                                                Intent intent = new Intent(Sucursal.this, SubMenu.class);
                                                intent.putExtra("ConfigCollection", ConfigCollection);
                                                intent.putExtra("Asistenciacollection", Asistenciacollection);
                                                intent.putExtra("SosCollection", SosCollection);
                                                intent.putExtra("FrentesCollection", FrentesCollection);
                                                intent.putExtra("ExAdicCollection", ExAdicCollection);
                                                progressDoalog.dismiss();    
                                                
                                                startActivity(intent);
                                            }
                                    }
                                        progressDoalog.dismiss();
                                    } catch (Exception e) {
                                            e.getMessage();
                                            progressDoalog.dismiss();
                                    }
                                    
                                    }
                                }
                             ).start();
    
    
    }
    
    
    public boolean IsAutoTimeEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) > 0;
        }
        return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME, 0) > 0;
    }
    
    public boolean IsAutoTimeZoneEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) > 0;
        }
        return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0) > 0;
    }
    
    private void GetbyNotificaciones() throws Exception {
        GlobalSettings state = ((GlobalSettings) getApplicationContext());
        state.valorconseguirNotificacion();
        status = String.valueOf(state.valorconseguirNotificacion());
        GlobalSettings state2 = ((GlobalSettings) getApplicationContext());
        state2.verNotificaciones();
        statusver = String.valueOf(state2.valorconseguirNotificacion());
        GlobalSettings stater = ((GlobalSettings) getApplicationContext());
        stater.comp();
        compared = String.valueOf(stater.valorconseguirNotificacion());
        if (compared != status) {
            if (status == statusver) {
                try {
                    NotificacionesCollection notificaciones = mx.smartteam.business.Notificaciones.GetNotification(context, currentProyecto);
                    // Toast.makeText(context, "Hoa"+notificaciones.get(0).Mensaje, Toast.LENGTH_LONG).show();
                    if (notificaciones.size() > 0) {
                        for (int i = 0; i < notificaciones.size(); i++) {
//                         Thread.sleep(1000);
                            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                            LayoutInflater inflater = getLayoutInflater();
                            final View checkboxLayout = inflater.inflate(R.layout.checkxml, null);
                            final CheckBox cb = (CheckBox) checkboxLayout.findViewById(R.id.checkBox);
                            alertDialog.setCancelable(false);
                            //final TextView tx = (TextView) checkboxLayout.findViewById(R.id.message);
                            alertDialog.setTitle( Html.fromHtml("<font color='#ffffff'>"+notificaciones.get(i).Tipo+"</font>"));

                            alertDialog.setView(checkboxLayout);
                            Drawable d=setIconAlert(context);
                            alertDialog.setIcon(d);
                            alertDialog.setMessage(notificaciones.get(i).Cuerpo);

                            //alertDialog.setMessage(notificaciones.get(i).Cuerpo);//Obtenemos el mensaje a mostrar
                            //// Setting OK Button
                            cb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (((CheckBox) v).isChecked()) {
                                        GlobalSettings state3 = ((GlobalSettings) getApplicationContext());
                                        state3.comp();
                                        status = String.valueOf(state3.valorconseguirNotificacion());
                                    }
                                    if (!((CheckBox) v).isChecked()) {
                                        GlobalSettings state4 = ((GlobalSettings) getApplicationContext());
                                        state4.verNotificaciones();
                                        status = String.valueOf(state4.valorconseguirNotificacion());
                                    }
                                }
                            });
                            alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (status == statusver) {
                                        GlobalSettings state4 = ((GlobalSettings) getApplicationContext());
                                        state4.verNotificaciones();
                                        status = String.valueOf(state4.valorconseguirNotificacion());
                                    } else if (status == compared) {
                                        GlobalSettings state4 = ((GlobalSettings) getApplicationContext());
                                        state4.comp();
                                        status = String.valueOf(state4.valorconseguirNotificacion());
                                    }
                                    dialog.dismiss();
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }//termina for
                        ctrlVibrator();
                    } //se termina el IF


                } catch (Exception e) {
                    Toast.makeText(Sucursal.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        autoTime = IsAutoTimeEnabled();
        autoTimeZone = IsAutoTimeZoneEnabled();
        Download();
        
        try {
            super.onResume();
        File mediaStorageDir = new File("/mnt/sdcard/Pictures/Team/");
        Utilerias.deleteDirectory(mediaStorageDir);
            mx.smartteam.entities.MovilConfigCollection movilConfigCollection = mx.smartteam.data.MovilConfig.GetByGpsConfig(context, currentUsuario);
            if(movilConfigCollection.size()>0){
                if (estadogps == movilConfigCollection.get(0).gpsconfig) {
                    if (gps != null) {
                        if (gps.GPSEnabled() && gps.NetworkEnabled()) {
                            gps.StartUsingGPS();
                           new Location().execute();
                           //new Thread(new  Location2()).start();
                        } else {
                            gps.ShowSettings();
                        }
                    }
                }//Estadogps
            }
            // Muestra las tiendas visitadas en el dia
            GetPopVisits(); // 
        } catch (Exception ex) {
            Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void GetPopVisits() throws Exception {

        mx.smartteam.entities.PopCollection popCollection = mx.smartteam.data.PopVisita.GetPopVisits(context, currentUsuario, currentProyecto);

        final Pop.AdapterItemsIco adapter = new AdapterItemsIco(this.context, popCollection);
        
        ListTiendas.setAdapter(adapter);
        try{
            ListTiendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final Pop popSelect = adapter.getItem(position);
                    if (popSelect != null) {

                        //Verificamos si la visita ha sido sincronizada para cambiar su estatus y asi poder sync nuevamente
                        PopVisita visita = mx.smartteam.data.PopVisita.GetById(context, popSelect.IdVisita);
                        if (visita != null && visita.StatusSync.equals(1)) {
                            visita.StatusSync = 0;
                            mx.smartteam.data.PopVisita.UpdateStatusSync(context, visita);
                        }

                        if (popSelect.IdStatus.equals(2)) {
                            ReOpeningPop(popSelect);
                        }

                        if (popSelect.IdStatus.equals(0)) {
                            // visita.IdStatus = 1;
                            OpeningPop(popSelect);
                            Toast.makeText(context, "Tienda sin visitar" + popSelect.IdVisita, Toast.LENGTH_SHORT);
                            //Debemos de actualizar el estatus a 1 y pasarle las coordenadas de donde se esta accediendo
                        }

                        if (popSelect.IdStatus.equals(1)) {
                            currentPop = popSelect;
                            Intent intent = new Intent(Sucursal.this, MenuPrincipal.class);
                            intent.putExtra("usuario", currentUsuario);
                            intent.putExtra("proyecto", currentProyecto);
                            intent.putExtra("pop", currentPop);
                            startActivity(intent);
                        }
                    }
                }
            });
            
            ListTiendas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //throw new UnsupportedOperationException("Not supported yet.");
                         final Pop popSelect = adapter.getItem(position);
                        try {
                            if(currentUsuario.Tipo == 3 && currentProyecto.getFicha() == 1) {
                                ReDirect(popSelect);
                            }else{
                                Toast.makeText(context, "Función no Valida", Toast.LENGTH_LONG).show();
                            }
                        } catch(Exception e) {
                            e.getMessage();
                        }
                         
                        return false;
                }
                
            });
            
            
        }catch(Exception e){
        e.getMessage().toString();
    }
    }

    private void GetPopVisitsToday() throws Exception {
        //insertamos tiendas con ruta a popvisita
        mx.smartteam.business.Rutas.GetByRutasToday(context, currentUsuario, currentProyecto);

    }
    /*
     * Reabrir tienda     
     */

    private void ReOpeningPop(final Pop pop) {
        // Mensaje de confirmacion
        StringBuilder strMsg = new StringBuilder();
        strMsg.append(String.format("La sucursal %s ya se encuentra cerrada!", pop.Cadena.toString() + " " + pop.Sucursal.toString()));
        strMsg.append("\n La desea abrir?");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        Drawable d=setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FFFFF'>Team</font>"))
                .setMessage(strMsg.toString())
                .setCancelable(false)
                .setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            mx.smartteam.business.PopVisita.VerifyStatusMarket(context, currentUsuario, currentProyecto);

                            currentPop = pop;

                            Intent intent = new Intent(Sucursal.this, MenuPrincipal.class);
                            intent.putExtra("usuario", currentUsuario);
                            intent.putExtra("proyecto", currentProyecto);
                            intent.putExtra("pop", currentPop);

                            mx.smartteam.entities.PopVisita visita = mx.smartteam.business.PopVisita.GetById(context, pop.IdVisita);
                            visita.IdStatus = 1;
                            mx.smartteam.business.PopVisita.UpdateStatus(context, visita);

                            startActivity(intent);



                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder
                .create();
        // show it
        alertDialog.show();
    }
    /*
     * Abriendo tienda
     */

    private void OpeningPop(final Pop pop) {
        // Mensaje de confirmacion
        StringBuilder strMsg = new StringBuilder();
        strMsg.append(String.format("Desea abrir la Sucursal %s ?",pop.Cadena + " " + pop.Sucursal));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Drawable d=setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FFFFF'>Team</font>"))
        .setMessage(strMsg.toString())
        .setCancelable(false)
        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                try {
                    mx.smartteam.business.PopVisita.VerifyStatusMarket(context, currentUsuario, currentProyecto);
                    currentPop = pop;
                    mx.smartteam.entities.PopVisita visita = mx.smartteam.business.PopVisita.GetById(context, pop.IdVisita);
                    mx.smartteam.entities.MovilConfigCollection movilConfigCollection = mx.smartteam.data.MovilConfig.GetByGpsConfig(context, currentUsuario);
                    if(movilConfigCollection .size()  > 0  ){
                        if (estadogps == movilConfigCollection.get(0).gpsconfig) {//
                            visita.Latitud = gps.Location.getLatitude();   // Cambiando para hacer pruebas
                            visita.Longitud = gps.Location.getLongitude(); //Cambio para hacer pruebas
                        }else{
                            visita.Latitud = null;
                            visita.Longitud = null;
                        }
                    }
                    else {
                        visita.Latitud = null;
                        visita.Longitud = null;
                    }
                    mx.smartteam.business.PopVisita.UpdateStatusAndCord(context, visita);
                    Intent intent = new Intent(Sucursal.this, MenuPrincipal.class);
                    intent.putExtra("usuario", currentUsuario);
                    intent.putExtra("proyecto", currentProyecto);
                    intent.putExtra("pop", currentPop);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        })
        .setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sucursal, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_buscar:
                
                try {
                    
                    //Verifica si la tienda ha sido visitada
                    mx.smartteam.entities.PopVisita popVisitaAux = mx.smartteam.data.PopVisita.FindPopOpenNowByFolio(context, currentProyecto, currentUsuario, Integer.parseInt(txtFolio.getText().toString()));
                    if (popVisitaAux != null) {
                        throw new PopException.TiendaAbierta();
                    }
                    if (txtFolio.getText().toString().isEmpty()) {
                        throw new PopException.FolioInvalido();
                    }
                    
                    if(currentUsuario.Tipo == 3 && currentProyecto.getFicha() == 1) {
                        ShowDialog(Integer.parseInt(txtFolio.getText().toString()));
                    }else{
                         new BuscarFolio().execute(txtFolio.getText().toString());
                    }
                    
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                }
                
                break;
                    
            case R.id.close_session:
                
                removePreference();
                logout();
                
                break;
                
            case R.id.menu_sincronizar:
                    
                int existe = mx.smartteam.business.PopVisita.existen_visitas_abiertas(context);
                    
                    if(existe > 0) {
                        Toast.makeText(context, "Existen Tiendas Abiertas Verifique Por Favor", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(Sucursal.this, SincronizarActivity.class);
                        intent.putExtra("usuario", currentUsuario);
                        startActivity(intent);
                    }
                
                    break;
                
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }
    
    class BuscarFolio extends AsyncTask<String, Void, mx.smartteam.entities.Pop> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Buscando folio</font>"));
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected mx.smartteam.entities.Pop doInBackground(String... params) {
            try {
                mx.smartteam.entities.Pop entityPop = mx.smartteam.data.Pop.GetByDeterminante(context, currentProyecto, Integer.parseInt(params[0]), currentUsuario);
                return entityPop;
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final mx.smartteam.entities.Pop resultado) {
            pd.dismiss();
            try {
                if (resultado == null) {
                    throw new PopException.NoExiste();
                }
                // Mensaje de confirmacion
                StringBuilder strMsg = new StringBuilder();
                strMsg.append(String.format(
                        "La determinante %s corresponde a la sucursal ",
                        txtFolio.getText().toString()));
                strMsg.append(resultado.Cadena + " ");
                strMsg.append(resultado.Sucursal);
                strMsg.append("\n Desea continuar?");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                //alertDialogBuilder.setTitle("Team");
                alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FFFFF'>Team</font>"));

                Drawable d=setIconAlert(context);
                alertDialogBuilder.setIcon(d);
                alertDialogBuilder.setMessage(strMsg.toString()).setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mx.smartteam.entities.MovilConfigCollection movilConfigCollection = mx.smartteam.data.MovilConfig.GetByGpsConfig(context, currentUsuario);
                            mx.smartteam.business.PopVisita.VerifyStatusMarket(context, currentUsuario, currentProyecto);
                            mx.smartteam.entities.PopVisita popVisita = new PopVisita();
                            popVisita.IdProyecto = currentProyecto.Id;
                            popVisita.DeterminanteGSP = resultado.DeterminanteGSP;
                            popVisita.IdUsuario = currentUsuario.Id;
                            if(movilConfigCollection.size() > 0 ){
                            if (estadogps == movilConfigCollection.get(0).gpsconfig) {
                                popVisita.Latitud = gps.Location.getLatitude();
                                popVisita.Longitud = gps.Location.getLongitude();
                            } else {
                                popVisita.Latitud = null;
                                popVisita.Longitud = null;
                            }
                            }else{  
                                popVisita.Latitud = null;
                                popVisita.Longitud = null;  
                            }
                            popVisita.IdStatus = 1;
                            popVisita = mx.smartteam.business.PopVisita.Insert(context, popVisita);
                            currentPop = resultado;
                            currentPop.IdVisita = popVisita.Id;
                            // Registramos la visita a la tienda
                            Intent intent = new Intent(Sucursal.this, MenuPrincipal.class);
                            intent.putExtra("usuario", currentUsuario);
                            intent.putExtra("proyecto", currentProyecto);
                            intent.putExtra("pop", currentPop);
                            startActivity(intent);


                        } catch (Exception ex) {
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                        .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {

            if (message.obj != null) {
                String[] data = message.obj.toString().split("\\|");

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .create();

                // Setting Dialog Title
                alertDialog.setTitle(Html.fromHtml("<font color='#FFFFF'>"+data[0]+"</font>"));

                Drawable d=setIconAlert(context);
                alertDialog.setIcon(d);
                // Setting Dialog Message
                alertDialog.setMessage(data[1]);

                // Setting Icon to Dialog
                // alertDialog.setIcon(R.drawable.tick);

                // Setting OK Button
                alertDialog.setButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your
                                // code here to
                                // execute after
                                // dialog closed
                                // Toast.makeText(getApplicationContext(),
                                // "You clicked on OK",
                                // Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                // Showing Alert Message
                alertDialog.show();

            }

            // Bitmap imagen = (Bitmap) message.obj;
            // Log.i("HANDLER",
            // "Se recibio descarga, size: " +
            // imagen.getByteCount());
            // iv.setImageBitmap(imagen);
        }
    };

    class Location extends AsyncTask<Void, Void, android.location.Location> {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Obteniendo ubicación</font>"));
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected android.location.Location doInBackground(Void... param) {
            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                        if (gps.Location != null) {
                            break;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(Sucursal.this, "doInBackground" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
            return gps.Location;
        }

        protected void onPostExecute(android.location.Location location) {
            pd.dismiss();
            try {
                if (location != null) {
                    //Toast.makeText(context, "Latitud=" + location.getLatitude() + " Longitud=" + location.getLongitude(), Toast.LENGTH_LONG).show();
                    gps.stopUsingGPS();
                    GetbyNotificaciones();
                    //GetbyNotificacionesPruebas();
                } else {
                    ShowDialog();
                    Toast.makeText(context, "Error al obtener la ubicacion", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(Sucursal.this, "" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

        private void ShowDialog() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle( Html.fromHtml("<font color='#FFFFF'>Servicios de ubicación</font>"));
            //alertDialog.setTitle("Servicios de ubicación");
            Drawable d=setIconAlert(context);
            alertDialog.setIcon(d);
            alertDialog.setCancelable(false);// seguridad
            // Setting Dialog Message
            alertDialog.setMessage("Error al obtener la ubicación desea intentar nuevamente o triangular?");
            // On pressing Settings button
            alertDialog.setPositiveButton("Intentar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    new Location().execute();
                }
            });
            // on pressing cancel button
            alertDialog.setNegativeButton("Triangular", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    gps.StartUsingNetwork();
                    new Location().execute();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }

    class Location2 implements Runnable {

        private ProgressDialog pd;

        public Location2() {

            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Obteniendo ubicación</font>"));
            //pd.setTitle("Obteniendo ubicación");
            Drawable d=setIconAlert(context);
            pd.setIcon(d);
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        public void run() {
            try {
                Looper.prepare();
                for (int i = 0; i < 5; i++) {

                    Thread.sleep(5000);
                    if (gps.Location != null) {
                        break;
                    }
                }

                pd.dismiss();
                if (gps.Location != null) {
                    Toast.makeText(context, "Latitud=" + gps.Location.getLatitude() + " Longitud=" + gps.Location.getLongitude(), Toast.LENGTH_LONG);
                    gps.stopUsingGPS();
                    GetbyNotificaciones();
                    
                } else {
                    
                            DialogoCuestion();  
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                            // Setting Dialog Title
//                            alertDialog.setTitle("Servicios de ubicación");
//                            alertDialog.setCancelable(false);// seguridad
//                            // Setting Dialog Message
//                            alertDialog.setMessage("Error al obtener la ubicación desea intentar nuevamente o triangular?");
//                            // On pressing Settings button
//                            alertDialog.setPositiveButton("Intentar", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                    //  new Location2().execute();
//
//                                    new Thread(new Location2()).start();
//                                }
//                            });
//                            // on pressing cancel button
//                            alertDialog.setNegativeButton("Triangular", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    gps.StartUsingNetwork();
//                                    new Thread(new Location2()).start();
//                                }
//                            });
//                            // Showing Alert Message
//                            alertDialog.show();
                        }
                   
                        Looper.loop();

            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }//End Class Location2
    
   void DialogoCuestion()throws Exception   {

     
            try {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                // Setting Dialog Title
                //alertDialog.setTitle("Servicios de ubicación");
                alertDialog.setTitle( Html.fromHtml("<font color='#FFFFF'>Servicios de ubicación</font>"));

                Drawable d=setIconAlert(context);
                alertDialog.setIcon(d);
                alertDialog.setCancelable(false);// seguridad
                // Setting Dialog Message
                alertDialog.setMessage("Error al obtener la ubicación desea intentar nuevamente o triangular?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Intentar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  new Location2().execute();

                        new Thread(new Location2()).start();
                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton("Triangular", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gps.StartUsingNetwork();
                        new Thread(new Location2()).start();
                    }
                });
                // Showing Alert Message
                alertDialog.show();

            } catch (Exception e) {
                e.getStackTrace();
            }


    
     }
     
    class GetLocation implements Runnable {

        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    txtFolio.setText("Itento " + i);
                    //Toast.makeText(context, "Ola " +i , Toast.LENGTH_LONG).show();
                }
                if (gps.Location != null) {
                    txtFolio.setText("Latitud=" + gps.Location.getLatitude() + " Longitud=" + gps.Location.getLongitude());
                }
            } catch (Exception e) {
                Toast.makeText(Sucursal.this, "metGeolocation" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ctrlVibrator() {
        final Vibrator Vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Vibrador.vibrate(1500);
    }
   
    public void Download()
    {
        Toast.makeText(context, "Actualizando información", Toast.LENGTH_LONG).show();     
            new Thread(new Runnable()
            {
                public void run() 
                {
                    mx.smartteam.entities.SondeoCollection sondeoCollection;
                    try 
                    {
                        consicCollection = mx.smartteam.business.UtilSincronizacion.getbyconsinc(context, currentProyecto, currentUsuario);
                        time = consicCollection.get(0).UFechaDescarga;            
                        //descargamos las rutas
                        
                        mx.smartteam.entities.RutasCollection rutasCollection = mx.smartteam.business.Rutas.Download.GetRutas(currentProyecto, currentUsuario, time);
                        for (mx.smartteam.entities.Rutas rutas : rutasCollection) {
                            mx.smartteam.business.Rutas.Insert(context, rutas);
                        }
                        if(rutasCollection.size()>0){
                            GetPopVisitsToday();
                            runOnUiThread(new Runnable() {
                                public void run() 
                                { 
                                    try {
                                        //* The Complete ProgressBar does not appear**/
                                        GetPopVisits();
                                    } catch (Exception ex) {
                                        Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } 
                            }); 
                            
                        }
                        
                        mx.smartteam.entities.MetaCadenaCollection metaCadenaCollection = mx.smartteam.business.MetaCadena.getAllMetaCadena(currentProyecto, time);
                        for(mx.smartteam.entities.MetaCadena metaCadena : metaCadenaCollection){
                            mx.smartteam.business.MetaCadena.Insert(context, metaCadena);
                        }
                        
                        //Actualiza productos                
                        ProductoCollection productoCollection = mx.smartteam.business.Producto.Downdload.GetProductosAll(currentProyecto, time);
                        for (mx.smartteam.entities.Producto producto : productoCollection)
                        {
                             mx.smartteam.business.Producto.Insertp(context, producto);
                        }  
                        //Actualiza Productos cadena
                        ProductoCadenaCollection productoCadenaCollection = mx.smartteam.business.ProductoCadena.Download.GetConjuntoCadenasbyUsuario(currentProyecto, currentUsuario,time);
                        for (mx.smartteam.entities.ProductoCadena productoCadena : productoCadenaCollection) 
                        {                       
                            mx.smartteam.business.ProductoCadena.Insert(context, productoCadena);
                        }
                        //actualiza tiendas
                        mx.smartteam.entities.PopCollection popCollection = mx.smartteam.business.Pop.Download.GetByProyecto(currentProyecto, time, currentUsuario);
                        for (Pop pop : popCollection) {
                            mx.smartteam.business.Pop.Insert(context, pop,currentUsuario, currentProyecto);
                        }
                        //actualiza sondeos
                        sondeoCollection = mx.smartteam.business.Sondeo.Download.GetByProyectoAndUsuario(currentProyecto, currentUsuario, time);
                        for (Sondeo sondeo : sondeoCollection) {
                        mx.smartteam.business.Sondeo.Insert(context, sondeo);                       
                        }
                        //actualiza formularios
                        mx.smartteam.entities.FormularioCollection formularioCollection = mx.smartteam.business.Formulario.Download.GetByProyectoAndUsuario(currentProyecto, currentUsuario, time);
                        for (Formulario formulario : formularioCollection) {
                        mx.smartteam.business.Formulario.Insert(context, formulario);
                        }/**/
                        //actualiza configuracion de las categorias
                        CategoriasConfigCollection configCategoriasCollection = mx.smartteam.business.Categoria.Download.GetCategoriasConfig(currentProyecto,currentUsuario, time);
                        for (mx.smartteam.entities.CategoriasConfig configCategorias : configCategoriasCollection) {
                              mx.smartteam.business.Categoria.InsertCategoriasConfig(context,configCategorias);                          
                        }
                        MarcaConfigCollection marcaconfigCollection =mx.smartteam.business.Marca.Download.GetMarcaconfig(currentProyecto,currentUsuario, time);
                        for(mx.smartteam.entities.MarcaConfig marcaConfig: marcaconfigCollection){
                            mx.smartteam.business.Marca.InsertMarcaConfig(context, marcaConfig);
                        }
                        
                      //Obtenemos Notificaciones
                    mx.smartteam.entities.NotificacionesCollection notificacionesCollection2 = mx.smartteam.business.Notificaciones.Download.GetMensajesByUsuario(currentProyecto, time, currentUsuario);
                    for (mx.smartteam.entities.Notificaciones notificaciones2 : notificacionesCollection2) {
                        mx.smartteam.business.Notificaciones.Insert(context, notificaciones2);
                    }   
                        
                    } catch (Throwable ex) {//END
                      ex.getMessage().toString();
                    }
                }
            }).start();
    }
    
    public Drawable setIconMenu(Context context){
         Drawable d=null;
         byte[] decodedString=null;
         Bitmap decodedByte=null;
         String image=""; 
         try{
             image = "iVBORw0KGgoAAAANSUhEUgAAADoAAAA5AQMAAABd4arFAAAAAXNSR0ICQMB9xQAAAAZQTFRFAAAA////pdmf3QAAAAF0Uk5TAEDm2GYAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAZdEVYdFNvZnR3YXJlAE1pY3Jvc29mdCBPZmZpY2V/7TVxAAAA/UlEQVQoz02RUW7EIAxEB/HB33KDcpPlYI0ajobUiyD1Auwf1UZxZyCRGkXwBDb2jAFEM8xvN2vanZkNQSCcgkSYQbk9YJVgeCAy2h1ICJ0wCJ5pvhNACA0Z2AgVueDJSkXAQwbsRSv/d9XK23dTgKAjFCa6V1fuBv8aC0I7FsR6wgtS3RfsJZcF7EvgToQhiAf8Kcid0r4rdTaJFRRpE+CGuq42U/APIQ14U3o4EeeDzpDaXbTebazqsX2tftSqX81f4JaKJ9xvXyCllPwBfDakacg+3ZBRC27rGJ/kCXzT+XH5TNc1gjhHADaIKMg16qVrcOX/KP09XNgc9x//laTLS8+NhQAAAABJRU5ErkJggg==";
             decodedString = Base64.decode(image, Base64.DEFAULT);
             decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
             d = new BitmapDrawable(context.getResources(), decodedByte);
         }catch(Exception e){
             e.getMessage();
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();             
         }     
         return d;
     }
     public Drawable setIconAlert(Context context){
         Drawable d=null;
         byte[] decodedString=null;
         Bitmap decodedByte=null;
         String image=""; 
         try{

               image = "iVBORw0KGgoAAAANSUhEUgAAADoAAAA5AQMAAABd4arFAAAAAXNSR0ICQMB9xQAAAAZQTFRFAAAA////pdmf3QAAAAF0Uk5TAEDm2GYAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAZdEVYdFNvZnR3YXJlAE1pY3Jvc29mdCBPZmZpY2V/7TVxAAAA/UlEQVQoz02RUW7EIAxEB/HB33KDcpPlYI0ajobUiyD1Auwf1UZxZyCRGkXwBDb2jAFEM8xvN2vanZkNQSCcgkSYQbk9YJVgeCAy2h1ICJ0wCJ5pvhNACA0Z2AgVueDJSkXAQwbsRSv/d9XK23dTgKAjFCa6V1fuBv8aC0I7FsR6wgtS3RfsJZcF7EvgToQhiAf8Kcid0r4rdTaJFRRpE+CGuq42U/APIQ14U3o4EeeDzpDaXbTebazqsX2tftSqX81f4JaKJ9xvXyCllPwBfDakacg+3ZBRC27rGJ/kCXzT+XH5TNc1gjhHADaIKMg16qVrcOX/KP09XNgc9x//laTLS8+NhQAAAABJRU5ErkJggg==";

             decodedString = Base64.decode(image, Base64.DEFAULT);
             decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
             d = new BitmapDrawable(context.getResources(), decodedByte);
         }catch(Exception e){
             e.getMessage();
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();             
         }     
         return d;
     }
     
     private void logout() {
         
         Intent intent = new Intent(Sucursal.this, LoginActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
         
     }
     
     private void removePreference() {
         preferences.edit().clear().apply();
     }
     
}//END CLASS
