package mx.triplei;

import mx.smartteam.entities.SondeoModulosCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.MenuCollection;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Anaquel;
import mx.smartteam.entities.AnaquelCollection;
import mx.smartteam.entities.Contestacion;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.FotoCollection;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Notificaciones;
import mx.smartteam.entities.Pregunta;
import mx.smartteam.entities.RespuestaSondeoCollection;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.SondeoModulos;

public class MenuPrincipal extends ListActivity {
    public static final String FINISH_ALERT = "finish_alert";
    public String _statusEnVisita;
    private mx.smartteam.entities.Usuario currentUsuario = null;
    private mx.smartteam.entities.Proyecto currentProyecto = null;
    private mx.smartteam.entities.SondeoModulosCollection sondeoGrupoCollection;
    private static mx.smartteam.entities.Pop currentPop = null;
    private mx.smartteam.entities.MovilConfig movilconfig = null;
    public mx.smartteam.entities.consicCollection consicCollection = null;
    private mx.smartteam.entities.SondeoModulosCollection sondeoModulos;
    private GlobalSettings appConfiguration;
    private Context context;
    public int opcionfoto;
    String time = null;
    boolean device, autoTimeZone, autoTime;
    public Integer globalsettings =0;
    Integer bSync = 0;
    ListView listView = null;
    public static int tipofoto; 
    Sucursal sucursal =new Sucursal();
    private mx.smartteam.entities.Pregunta objPregunta=null;
    private EditText edit;   
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_principal);
            this.context = this;
            device = Utilerias.isTablet(context);
            currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
            currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
            currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
            
            try {
                mx.smartteam.entities.PopVisitaCollection collection = mx.smartteam.business.PopVisita.go_to_Sincronitation(context, currentProyecto, currentUsuario);
                
                if(collection.size() > 0){
                    Intent intent = new Intent(context, SincronizarActivity.class);
                    intent.putExtra("usuario", currentUsuario);
                    startActivity(intent);
                }
                
                CreaMenu();
            } catch (Exception ex) {
                 Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setTitle(Html.fromHtml("<small><strong>" + currentPop.Cadena + " " + currentPop.Sucursal+ "</strong></small>"));

            ActionBar actionBar = getActionBar();
            actionBar.setDisplayUseLogoEnabled(false);
            //Validando la conexion   
            this.registerReceiver(this.finishAlert, new IntentFilter(FINISH_ALERT)); 
    }

    private boolean checkInternetConection() 
    {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] info =  connec.getAllNetworkInfo();    
           for (int i = 0; i<info.length; i++){
                   if (info[i].getState() == NetworkInfo.State.CONNECTED){}
           }
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
        connec.getNetworkInfo(1).getSubtype();
        return true;
        }else if (
        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) 
        {
        return false;
        }
        return false;
    }
     
    public void checkNetworkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        } else if (mobile.isAvailable()) {
            Toast.makeText(this,"Mobile " + mobile.getExtraInfo() +"\n" +mobile.getReason()+"\n" +mobile.getSubtypeName()+"\n"+mobile.getTypeName()+"\n", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
        }
    }
     
    public void Validate() {
        try
        {
            movilconfig = mx.smartteam.business.MovilConfig.GetConfig(context, currentUsuario);
            if(movilconfig != null && movilconfig.Conexion.equals("online")){
                
                if(device){/* aqui es una tablet*/
                //Toast.makeText(context, "11111111", Toast.LENGTH_LONG).show();
                    Upload();                       
                }else if(checkInternetConection()){
                //Lanzamos el servicio para que envie la informacion al server
                    consicCollection = mx.smartteam.business.UtilSincronizacion.getbyconsinc(context, currentProyecto, currentUsuario);
                    time = consicCollection.get(0).UFechaDescarga;
                    
                        Upload();                       
                    
                
                }
                
                /*if(checkInternetConection()){
                        //Lanzamos el servicio para que envie la informacion al server
                    consicCollection = mx.smartteam.business.UtilSincronizacion.getbyconsinc(context, currentProyecto, currentUsuario);
                    time = consicCollection.get(0).UFechaDescarga;
                    if(bSync == 0){
                        Upload();                       
                    }
                }*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     }
    
    
    public boolean isConnected(){
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = cm.getActiveNetworkInfo();

    if ( networkInfo != null && networkInfo.isConnectedOrConnecting()){
        return true;
    }else{
        return false;
    }
}
    
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        final mx.smartteam.entities.Menu opcion = (mx.smartteam.entities.Menu) parent.getItemAtPosition(position);
        int existe = 0;
        if(opcion.Requerido == 1) {
            
            for(int vuelta = 0; vuelta < sondeoModulos.size(); vuelta ++) {
                
                if(sondeoModulos.get(vuelta).getRequerido() == 1) {
                    
                    try{
                        
                        if(sondeoModulos.get(vuelta).getTipo().equalsIgnoreCase("sondeos")) {
                            existe = mx.smartteam.business.Sondeo.get_Exist_Sondeo(context, sondeoModulos.get(vuelta), currentPop);  
                        } else {
                             existe = verifica_formularios(sondeoModulos.get(vuelta));
                        }
                        
                        if(existe == 0){
                            if(vuelta == position) {
                                start_Activity(opcion);
                                break;
                            } else {
                                Toast.makeText(context, "Necesita Realizar la Tarea [" + sondeoModulos.get(vuelta).getNombre() + "]", Toast.LENGTH_LONG).show();
                                break;
                            }
                        } else {
                            if(vuelta == position) {
                                start_Activity(opcion);
                                break;
                            } else {
                                continue;
                            }
                        }
                        
                    } catch(Exception e) {
                        e.getMessage();
                    }
                    
                }
                
            }
            
        } else {
            start_Activity(opcion);
        }
    }
    //Hasta aquí me quede, hay que generar los querys por cada uno de los modulos que se presenten
    public int verifica_formularios(mx.smartteam.entities.SondeoModulos modulo){
        int ok = 0;
        
        try {
                if(modulo.getNombre().equalsIgnoreCase("asistencia")) {
                        ok = mx.smartteam.business.Foto.BuscarFotoEntrada(context, currentPop);
                } else if(modulo.getNombre().equalsIgnoreCase("cerrar_dia")) {
                        ok = mx.smartteam.business.Foto.BuscarFotoSalida(context, currentPop);
                } else if (modulo.getNombre().equalsIgnoreCase("insumos")) {
                        ok = mx.smartteam.business.Insumos_Answ_Activity.existe_contestacion(context, currentPop);
                } else if(modulo.getNombre().equalsIgnoreCase("anaquel_frentes")) {
                        ok = mx.smartteam.business.Anaquel.existe_contestacion(context, currentPop, "ANAQUEL");
                } else if(modulo.getNombre().equalsIgnoreCase("anaquel_precios")) {
                        ok = mx.smartteam.business.Anaquel.existe_contestacion(context, currentPop, "PRECIO");
                } else if(modulo.getNombre().equalsIgnoreCase("sod")){
                        ok = mx.smartteam.business.Sod.existe_contestacion(context, currentPop);
                } else if(modulo.getNombre().equalsIgnoreCase("bodega")){
                        ok = mx.smartteam.business.Bodega.existe_contestacion(context, currentPop);
                } else if(modulo.getNombre().equalsIgnoreCase("ex adicional")){
                        ok = mx.smartteam.business.ExhibicionAdicional.existe_contestacion(context, currentPop);
                } else if(modulo.getNombre().equalsIgnoreCase("material pop")){
                        ok = mx.smartteam.business.MaterialPromocional.existe_contestacion(context, currentPop);
                } else if(modulo.getNombre().equalsIgnoreCase("fotos")){
                        ok = mx.smartteam.business.Foto.existe_foto(context, currentPop);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        return ok;
    }
    
    public void start_Activity(final mx.smartteam.entities.Menu opcion) {
        
        final Intent i;
        
         if (opcion != null) {
            switch (opcion.Key) {
                case foto:
                    opcionfoto = 2;
                    i = new Intent(MenuPrincipal.this, CameraActivity.class);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    i.putExtra("opcionfoto", opcionfoto);
                    startActivity(i);
                    super.onStop();
                    break;
                case asistencia:
                    FotoEntradaSalida(3);
                    super.onStop();
                break;
                    
                case insumos:
                        i = new Intent(MenuPrincipal.this, Insumos_Activity.class);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    break;
                    
                case cerrarDia:
                    int fotos = 0;
                    try{
                        fotos = mx.smartteam.business.Foto.BuscarFotoSalida(context, currentPop);
                        if(fotos==0){//Foto de salida
                            // Insertat FotoEntrada 0 Salida 1
                            FotoEntradaSalida(2);
                        }else{
                            AccionCerrarDia();
                        }
                    }catch(Exception ex){ ex.getMessage().toString(); }
                break;
                
                case exhibiciones_adicionales:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                    
                case existencias_anaquel:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);  
                    super.onStop();
                break;
                    
                case existencias_bodega:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                    
                case existencias_frentes_en_frio:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                    
                case anaquel_frentes:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                        
                case anaquel_precios:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                        
                case material_promocional:
                    i = new Intent("mx.triplei.producto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                    
                case sos:
                    i = new Intent("mx.triplei.bymarca");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                    
                case sod:
                    i = new Intent("mx.triplei.exhibicionactivity");//"mx.smartteam.exhibicionactivity"
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break;
                    
                case foto_anaquel:
                    i = new Intent("mx.triplei.bymarca");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop();
                break; 
                
                case foto_adicional:
                    i = new Intent("mx.triplei.FotoAdicional");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop(); 
                break;
                
                case foto_producto:
                    i = new Intent("mx.triplei.FotoProducto");
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop(); 
                break;
                    
                case scoredcard:
                    i = new Intent(MenuPrincipal.this, ScoredCard.class);
                    i.putExtra("item", opcion.Key);
                    i.putExtra("tipo", opcion.tipo);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    startActivity(i);
                    super.onStop(); 
                break;
                    
                    
                case grupoxsondeos:    
                    try{
                        i = new Intent(MenuPrincipal.this,mx.triplei.newSondeoActivity.class);
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                       super.onStop();
                    }catch(Exception e){
                        e.getMessage().toString();
                    }
                break;    
                    
                case sondeo:
                    if (opcion.Identiventas == 1) {
                        i = new Intent("mx.triplei.producto");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    } else if(opcion.Identiventas == 2){
                        i = new Intent("mx.triplei.marcascatacivity");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        i.putExtra("opcionmarca",2);
                        startActivity(i);
                        super.onStop();
                    } else if(opcion.Identiventas == 3){
                        i = new Intent("mx.triplei.preguntadinamicaactivity");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    } else if(opcion.Identiventas == 5){ //2 preguntas
                        i = new Intent("mx.triplei.pdinamicacategoriaactivity");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    }else if(opcion.Identiventas == 6){ //2 preguntas
                        objPregunta= new Pregunta();
                        mx.smartteam.entities.Sondeo sondeo=new mx.smartteam.entities.Sondeo();
                        sondeo.Id=opcion.Id;
                        try{
                       objPregunta=mx.smartteam.business.Pregunta.GetPreguntaTipo3(context, sondeo);
                        }catch (Exception e)
                        {
                            e.getMessage().toString();
                        }
                        
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        edit = new EditText(this);
                        dialogo1.setView(edit);
                        dialogo1.setTitle( Html.fromHtml("<font color='#FFFFF'>PREGUNTA</font>"));
                        dialogo1.setMessage(objPregunta.Nombre);            
                        dialogo1.setCancelable(false);  
                        String resp=edit.getText().toString();
                        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialogo1, int id) {
                                String resp=edit.getText().toString();
                                if(resp.equals(""))
                                {
                                    Toast.makeText(context, "Necesitas Capturar Datos", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    objPregunta.Respuesta=resp;
                                    in = new Intent("mx.triplei.sondeo");
                                    in.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                                    in.putExtra("usuario", currentUsuario);
                                    in.putExtra("proyecto", currentProyecto);
                                    in.putExtra("pop", currentPop);
                                    in.putExtra("pregunta3", objPregunta);
                                    startActivity(in);                           
                                }
                            }  
                        });  
                        
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialogo1, int id) {                    
                            }
                        });            
                        dialogo1.show();         
                        super.onStop();
                    } else if(opcion.Identiventas == 7){
                        objPregunta= new Pregunta();
                        mx.smartteam.entities.Sondeo sondeo=new mx.smartteam.entities.Sondeo();
                        sondeo.Id=opcion.Id;
                        try{
                            objPregunta=mx.smartteam.business.Pregunta.GetPreguntaTipo3(context, sondeo);
                            objPregunta.Respuesta =  UUID.randomUUID().toString();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                                                
                        i = new Intent(MenuPrincipal.this, AndroidCamera.class);
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        i.putExtra("pregunta3", objPregunta);
                        startActivity(i);
                    
                    }else if(opcion.Identiventas == 8){
                        objPregunta= new Pregunta();
                        mx.smartteam.entities.Sondeo sondeo=new mx.smartteam.entities.Sondeo();
                        sondeo.Id=opcion.Id;
                        try{
                            objPregunta=mx.smartteam.business.Pregunta.GetPreguntaNContestaciones(context, sondeo);
                            objPregunta.Respuesta =  UUID.randomUUID().toString();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        
                        i = new Intent("mx.triplei.sondeo");
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        i.putExtra("preguntan8", objPregunta);
                        startActivity(i);
                        super.onStop();                    
                    
                    } else {
                        i = new Intent("mx.triplei.sondeo");
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    }
                    break;

                default: break;
            }
        }
    }

    public  void AccionCerrarDia() {
        new CerrarDia().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sincronizar, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    private void CreaMenu() throws Exception {
        SondeoModulosCollection formularioCollection = mx.smartteam.business.Formulario.GetByProyectosm(context, currentProyecto);
        SondeoModulosCollection sondeoCollection = mx.smartteam.business.Sondeo.GetByProyectosm(context, currentProyecto);
        sondeoGrupoCollection = mx.smartteam.business.Sondeo.GetGruposSondeossm(context, currentProyecto);
        
        mx.smartteam.entities.MenuCollection menuCollection = new MenuCollection();
        sondeoModulos = new SondeoModulosCollection();
        sondeoModulos.addAll(sondeoGrupoCollection);
        sondeoModulos.addAll(sondeoCollection);
        sondeoModulos.addAll(formularioCollection);
        Collections.sort(sondeoModulos, new Comparator<SondeoModulos>() {
            @Override
            public int compare(SondeoModulos p1, SondeoModulos p2) {
                return new Integer(p1.Orden).compareTo(new Integer(p2.Orden));
            }
        });
        
        for (mx.smartteam.entities.SondeoModulos formulario : sondeoModulos) {
            mx.smartteam.entities.Menu menu = null;
            if (formulario.Nombre.equals("asistencia")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.asistencia;
                menu.Name = formulario.Titulo;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_asistencia;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("anaquel")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.existencias_anaquel;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_existencia_anaquel;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("existencias_frentes_en_frio")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.existencias_frentes_en_frio;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_existencia_anaquel;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("anaquel_frentes")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.anaquel_frentes;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_existencia_anaquel;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("anaquel_precios")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.anaquel_precios;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.price;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("fotos")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.foto;
                menu.Name = formulario.Titulo;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_foto;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("bodega")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.existencias_bodega;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_existencia_bodega;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("cerrar_dia")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.cerrarDia;
                menu.Name ="Cerrar Tienda";
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.cerrar_dia;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("material_pop")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.material_promocional;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_mat_promocional;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("ex_adicional")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.exhibiciones_adicionales;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_exh_adicional;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("sos")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.sos;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_existencia_anaquel;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if(formulario.Nombre.equals("sod")){
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.sod;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre.replace("_", " ");
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_exchiadic;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            
            if (formulario.Nombre.equals("foto_anaquel")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.foto_anaquel;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.foto_anaquel;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("foto_adicional")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.foto_adicional;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.foto_adicional;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            if (formulario.Nombre.equals("foto_producto")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.foto_producto;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.foto_producto;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            
            if (formulario.Nombre.equals("scoredcard")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.scoredcard;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.scoredcard;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
            
             if (formulario.IdGrupoSondeo != null) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.grupoxsondeos;
                menu.Name = formulario.NombreSondeo;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.conjuntos;
                menu.Tag = formulario;
                menu.Identiventas = formulario.IdentificadorVentas;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }       
             if (formulario.tposm !=null) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.sondeo;
                menu.Name = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.ch_sondeo;
                menu.Tag = formulario;
                menu.Identiventas = formulario.IdentificadorVentas;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
            }
             if(formulario.Nombre.equals("insumos")) {
                menu = new mx.smartteam.entities.Menu();
                menu.Id = formulario.Id;
                menu.Key = EnumFormulario.insumos;
                menu.Name = formulario.Titulo;
                menu.tipo = formulario.Nombre;
                menu.Order = formulario.Orden;
                menu.Icon = R.drawable.scoredcard;
                menu.Tag = formulario;
                menu.Requerido = formulario.Requerido == null ? 0 : formulario.Requerido;
             }
            if (menu != null) {
                menuCollection.add(menu);
            }
        }
   
        // Empezar a trabajar aqui @Arturo
        setListAdapter(new AdaptadorMenu(menuCollection));
    }

    class AdaptadorMenu extends ArrayAdapter<mx.smartteam.entities.Menu> {
        private MenuCollection menuCollection;
        public AdaptadorMenu(MenuCollection menuCollection) {
            super(MenuPrincipal.this, R.layout.itemmenu, menuCollection);
            this.menuCollection = menuCollection;
        }

        
        // TODO: VIEW OF ADAPTER
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.itemmenu, parent, false);
            mx.smartteam.entities.Menu menu = this.menuCollection.get(position);
            TextView label = (TextView) row.findViewById(R.id.label);
            TextView text_anexo = (TextView) row.findViewById(R.id.text_anexo);
            LinearLayout list_view_ = (LinearLayout)row.findViewById(R.id.list_view_);
            label.setText(menu.Name);
            label.setTextColor(Color.BLACK);
            
            if(menu.Requerido != null && menu.Requerido == 1){
                text_anexo.setText("Requerido");
            } 
            
            ImageView icon = (ImageView) row.findViewById(R.id.menuicon);
            icon.setImageResource(menu.Icon);
            return row;
        }
    }

    class  CerrarDia extends AsyncTask<Void, Void, String> {
        private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Cerrando visita</font>"));
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... param) {
            String result = null;
            try {
                result = mx.smartteam.business.PopVisita.CloseDay(context, currentPop);
            } catch (Exception ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultado) {
            pd.dismiss();
            if (resultado.trim().equals("OK")) {
                Toast.makeText(context, "Dia cerrado correctamente", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(context, "No es posible cerrar visita, verifique por favor", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ir_a_sincronizar:
                try {
                    if(valida_requeridos_contestados()){
                        //AccionCerrarDia();
                        Intent go_sinc = new Intent(MenuPrincipal.this, SincronizarActivity.class);
                        go_sinc.putExtra("usuario", currentUsuario);
                        startActivity(go_sinc);
                        finish();
                    } else {
                         Toast.makeText(context, "Faltan Tareas Por Realizar", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public boolean valida_requeridos_contestados() {
        boolean ok = false;
        int existe = 0;
        
            for(int vuelta = 0; vuelta < sondeoModulos.size(); vuelta ++) {
                if(sondeoModulos.get(vuelta).getRequerido() == 1) {
                    try {
                        
                         if(sondeoModulos.get(vuelta).getTipo().equalsIgnoreCase("sondeos")) {
                            existe = mx.smartteam.business.Sondeo.get_Exist_Sondeo(context, sondeoModulos.get(vuelta), currentPop);  
                          } else {
                             existe = verifica_formularios(sondeoModulos.get(vuelta));
                          }
                         
                        if(existe == 0) {
                            ok = false;
                            break;
                        } else {
                            ok = true;
                        }
                        
                    } catch(Exception e) {
                        e.getMessage();
                    }
                } else {
                    ok = true;
                }
            }
        
        return ok;
    }

    public boolean IsAutoTimeEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) > 0; /*fOR jELLY bEAN +*/ 
        }
        // For older Android versions
        return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME, 0) > 0;
    }

    public boolean IsAutoTimeZoneEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) > 0; /* For JB+*/
        }
        // For older Android versions
        return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0) > 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            if(currentPop == null) {
                this.finishAffinity();
            }
            
            int t = mx.smartteam.business.Pop.ValidarFechaVisita(context, currentPop);
        
            if(t == 0){
               // this.finishAffinity();
            }
            //this.finishAffinity();
            tipofoto =0;
        
            SondeoModulosCollection formularioCollection = null; 
            formularioCollection = mx.smartteam.business.Formulario.GetFlagFoto(context, currentProyecto);
            
            if(formularioCollection!=null){           
                int fotos = 0;      
                fotos = mx.smartteam.business.Foto.BuscarFotoEntrada(context, currentPop);
                if(fotos==0){
                    // Insertat FotoEntrada 1 Salida 2 Asistencia = 3
                    FotoEntradaSalida(1);
                }
            }
                         
            Validate();
            autoTime = IsAutoTimeEnabled();  autoTimeZone = IsAutoTimeZoneEnabled();

            if (device == false) {
                if (autoTime == false) {
                    Intent i = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    startActivity(i);
                    autoTime = IsAutoTimeEnabled();
                    autoTimeZone = IsAutoTimeZoneEnabled();
                }
                if (autoTimeZone == false) {
                    Intent i = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    startActivity(i);
                    autoTime = IsAutoTimeEnabled();
                    autoTimeZone = IsAutoTimeZoneEnabled();
                }
            }
        
        }catch(Exception ex){
        }
    }
    
    public void FotoEntradaSalida(final int enter) { /* Para usar este metodo se tiene que validar que no exista alguna otra imagen de entrada o salida*/ 
        String mensaje = ""; tipofoto=0;
        
        switch(enter) {
            case 1: //Entrada
                mensaje = "FOTO DE ENTRADA";
                break;
            case 2: // SALIDA
                mensaje = "FOTO DE SALIDA";
                break;
            case 3: // DEFAULT
                mensaje = "ASISTENCIA";
                break;
        }
        
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+mensaje+"</font>"));
        Drawable d=sucursal.setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        final RadioGroup radioGroup = new RadioGroup(this);
        RadioButton radionormal = new RadioButton(this);
        radionormal.setId(1);
        radionormal.setChecked(true);
        radionormal.setText("NORMAL");
        RadioButton radioselfie = new RadioButton(this);
        radioselfie.setId(2);
        radioselfie.setText("SELFIE");
        radioGroup.addView(radionormal);
        radioGroup.addView(radioselfie);
                   
	alertDialogBuilder
        .setMessage("Selecciona tipo de foto:")
        .setView(radioGroup)                                
        .setCancelable(false)
        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                
                Intent i = new Intent (MenuPrincipal.this, CameraActivity.class);
                i.putExtra("pop", currentPop);
                i.putExtra("opcionfoto", 1);
                i.putExtra("fentrada", enter);
                
                if(checkedRadioButtonId == 1){
                    tipofoto=0;/* variable para determinar el tipo de camara*/
                }else {
                    tipofoto=1;/* variable para determinar el tipo de camara*/
                }
                startActivity(i);
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        super.onStop();
    }
    
    public void showDialogListView(View view){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        Drawable d=sucursal.setIconAlert(context);
        builder.setIcon(d);
        builder.setCancelable(true);
        builder.setView(listView);
        AlertDialog dialog=builder.create();
        dialog.show();
   }
   
    public void Upload(){
        //Toast.makeText(context, "Subiendo información", Toast.LENGTH_LONG).show();     
        new Thread(new Runnable(){
            public void run() {
                mx.smartteam.entities.PopVisitaCollection visitas;
                mx.smartteam.entities.SondeoCollection sondeoCollection;
                mx.smartteam.entities.SodCollection sodCollection;
                
                try {
                    visitas = mx.smartteam.business.PopVisita.GetPendings(context, currentUsuario, currentProyecto);
                    bSync=1;
                    for (final PopVisita popVisita : visitas) {
                        mx.smartteam.business.PopVisita.Upload.Insert(popVisita);
                        
                        int count = 0;
                        count = mx.smartteam.business.Foto.countFotos(context, popVisita);
                        if (count > 0) {
                            for (int ii = 0; ii < count; ii++) {
                                FotoCollection fotoCollection = mx.smartteam.business.Foto.FotosVisitas(context, popVisita);
                                for (Foto foto : fotoCollection){
                                    String result = mx.smartteam.business.Foto.Upload.InsertV2(context, popVisita, foto);
                                    if(result.equals("OK")){
                                        foto.StatusSync = 1;
                                        mx.smartteam.business.Foto.UpdateStatusSync(context, foto);
                                    }
                                }
                            }
                        }
                        
                        
                        mx.smartteam.entities.ScoredCardCollection scoredcardCollection = mx.smartteam.business.ScoredCard.Download.getgetAllCalificacionesDiarias(currentProyecto, currentUsuario, time);
                        for(mx.smartteam.entities.ScoredCard scard : scoredcardCollection){
                            mx.smartteam.business.ScoredCard.Insert(context, scard);
                        }
                        
                        //Sondeos
                        sondeoCollection = mx.smartteam.business.Sondeo.GetByProyecto2(context, currentProyecto);
                        if(sondeoCollection.size() > 0){
                            for (final Sondeo sondeo : sondeoCollection) {
                                if(sondeo.IdentificadorVentas == 7 || sondeo.IdentificadorVentas == 8){
                                    mx.smartteam.entities.ContestacionCollection contestacionCollection;
                                    contestacionCollection = mx.smartteam.business.Contestacion.ContestacionGetByVisita(context, popVisita, sondeo);

                                    for (final Contestacion contestacion : contestacionCollection) {
                                    final RespuestaSondeoCollection respuestaSondeoCollection = mx.smartteam.business.Contestacion.GetByVisitaSondeop(context, popVisita, sondeo, contestacion);//data.RespuestaSondeo.GetByVisitaSondeop(context, popVisita, sondeo, contestacion);
                                    mx.smartteam.business.Sondeo.Upload.Insert(popVisita, sondeo, respuestaSondeoCollection);
                                    //Actualizar Status y Fecha de Sincronizacion 

                                    contestacion.StatusSync = 1;
                                    mx.smartteam.business.Contestacion.UpdateStatusSync(context, contestacion);//data.RespuestaSondeo.UpdateStatusSync(context, contestacion);

                                    }
                                }
                            }
                        }
                        
                        //Anaquel
                        AnaquelCollection anaquelCollection = mx.smartteam.business.Anaquel.GetByVisita(context, popVisita);
                        for (final Anaquel anaquel : anaquelCollection) {
                            if (anaquel.StatusSync.equals(0)) {
                                mx.smartteam.business.Anaquel.Upload.Insert(popVisita, anaquel);
                                anaquel.StatusSync = 1;
                                mx.smartteam.business.Anaquel.UpdateStatusSync(context, anaquel);
                            }
                        }
                        
                       sodCollection = mx.smartteam.business.Sod.getAllnotUpload(context, popVisita);
                        if(sodCollection.size() > 0){
                            String result = "";
                            
                            result = mx.smartteam.business.Sod.SodInsert(popVisita, sodCollection);
                            
                            if(result.equals("OK")){
                            
                            mx.smartteam.business.Sod.UpateSodByVisita(context, popVisita);
                            }
                        }
                        
                        /*mx.smartteam.entities.NotificacionesCollection notificacionesCollection = mx.smartteam.business.Notificaciones.Download.GetNotificaciones(currentProyecto,time);
                        if(notificacionesCollection.size()>0){
                            for (Notificaciones notificaciones : notificacionesCollection){
                                mx.smartteam.business.Notificaciones.Insert(context, notificaciones);
                            }
                        }*/
                    }
                    bSync =0;
                } catch (Throwable ex) {//END
                    ex.getMessage().toString();
                    bSync =0;
                }
            }
        }).start();
    }
    
    BroadcastReceiver finishAlert = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(finishAlert);
    }
}
