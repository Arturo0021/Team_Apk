package mx.triplei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.ThreadPoolExecutor;
import mx.triplei.R;
import mx.smartteam.entities.Anaquel;
import mx.smartteam.entities.AnaquelCollection;
import mx.smartteam.entities.Bodega;
import mx.smartteam.entities.BodegaCollection;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.CategoriasConfigCollection;
import mx.smartteam.entities.ExhibicionAdicional;
import mx.smartteam.entities.ExhibicionAdicionalCollection;
import mx.smartteam.entities.Formulario;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.FotoCollection;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.MaterialPromocional;
import mx.smartteam.entities.MaterialPromocionalCollection;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.PopVisitaCollection;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.ProductoCadenaCollection;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.RespuestaSondeoCollection;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.UsuarioProyecto;
import mx.smartteam.entities.Notificaciones;
import mx.smartteam.entities.Rutas;
import mx.smartteam.entities.Contestacion;
import mx.smartteam.entities.ExhibicionConfigCollection;
import mx.smartteam.entities.MarcaConfigCollection;
import mx.smartteam.entities.Usuario;
import mx.smartteam.entities.Version;

public class SincronizarActivity extends Activity {

    AlertDialog.Builder builderAlertPhone;
    AlertDialog Alertshow;
    ProgressDialog progressDoalog;
    View view;
    public mx.smartteam.entities.Usuario currentUsuario = null;
    mx.smartteam.entities.Usuario UsuarioInfo;
    public mx.smartteam.entities.Proyecto currentProyecto = null;
    mx.smartteam.entities.PopVisitaCollection countvisitas;
    public Context context = null;
    TableLayout tableLayout;
    private List<Map<String, String>> tableList = new ArrayList<Map<String, String>>();
    private ThreadPoolExecutor executor = null;//(ThreadPoolExecutor) Executors.newFixedThreadPool(8);
    public mx.smartteam.entities.consicCollection consicCollection = null;
    int _numerovisitas = 0, ta, aument = 0;
    String time = null;
    String telefonoInsert = "";
    boolean insertDataUser = false;
     //<editor-fold>
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
            setContentView(R.layout.activity_sincronizar);
        context = this;
        tableLayout = (TableLayout) findViewById(R.id.tableLayoutVisita);
        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        
        
        if (currentUsuario != null) {
            try {
                // Primeramente registramos el usuario que sincroniza
                // Establecemos la configuracion para ver 
                mx.smartteam.business.Usuario.Insert(context, currentUsuario);
                UsuarioInfo = mx.smartteam.business.Usuario.GetByUsuario2(context, currentUsuario.Usuario);
                
                    if(UsuarioInfo.getTelefono() == null || UsuarioInfo.getTelefono().toString().length() < 13) {

                            builderAlertPhone = new AlertDialog.Builder(SincronizarActivity.this);
                                       builderAlertPhone.setIcon(R.drawable.logoteam_new);
                                       builderAlertPhone.setTitle(getString(R.string.Tel));
                                       builderAlertPhone.setMessage(R.string.phoneMessage);
                                       builderAlertPhone.setCancelable(false);

                                       View viewInflater = getLayoutInflater().inflate(R.layout.inflater_alert_phone, null);

                                       final EditText txt_numberphone = (EditText)viewInflater.findViewById(R.id.txt_numberphone);
                                       Button var_celphone = (Button)viewInflater.findViewById(R.id.var_celphone);

                                       var_celphone.setOnClickListener(new View.OnClickListener(){
                                           public void onClick(View v) {
                                               if(txt_numberphone.getText().toString().isEmpty()) {
                                                   telefonoInsert = "";
                                               } else {
                                                   telefonoInsert = txt_numberphone.getText().toString();
                                               }
                                                   new Thread(new GetProyecto()).start();
                                             }
                                       });

                           builderAlertPhone.setView(viewInflater);
                           Alertshow = builderAlertPhone.create();
                           Alertshow.show();

                           insertDataUser = true;

                    } else {
                        new Thread(new GetProyecto()).start();
                    }
                
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", ex.getMessage());
            }
           
        }

    }

    protected void onResume() {
        super.onResume();
        Visitas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sincronizar, menu);
       
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.enviar_email:
                enviarCorreo();
                break;

            case R.id.action_data:
                mensajeAlert();
                break;
            case R.id.action_download:
                    Download();
                break;
            case R.id.action_upload:
                if (currentUsuario != null && currentProyecto != null) {
                    try {
                        countvisitas = mx.smartteam.business.PopVisita.GetPendings(context, currentUsuario, currentProyecto);
                        if (countvisitas != null) {
                            _numerovisitas = countvisitas.size();
                        }
                        Uploads(); //Produccion
                    } catch (Exception e) {
                        Toast.makeText(context, "No hay Visitas Pendientes por sincronizar", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        return true;
    }

    public void mensajeAlert() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle( Html.fromHtml("<font color='#FFFFF'>Importante</font>"));
        //dialogo1.setTitle("Importante");
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconAlert(context);
        dialogo1.setIcon(d);
        dialogo1.setMessage("Se eliminaran todos los datos!");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                if (currentUsuario != null && currentProyecto != null) {

                    new Thread(new DeleteDatabase()).start();

                }

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }

    private HashMap<String, String> createTable(String key, String name) {
        HashMap<String, String> table = new HashMap<String, String>();
        table.put(key, name);
        return table;
    }

    private void Visitas() {
        try {
            TableRow row = null;
            TextView txtUsuario, txtPop, txtFecha, txtStatus;
            ImageView image;
            tableLayout.removeAllViewsInLayout();
            PopVisitaCollection visitas = mx.smartteam.business.PopVisita.GetAll(context, currentUsuario, currentProyecto); //data.PopVisita.GetAll(context, currentUsuario, currentProyecto);
            for (PopVisita popVisita : visitas) {
                row = new TableRow(this);
                row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                image = new ImageView(this);
                if (popVisita.StatusSync.equals(0)) { image.setImageResource(R.drawable.flagnosync);} else { image.setImageResource(R.drawable.flagsync); }
                txtUsuario = new TextView(this);
                txtPop = new TextView(this);
                txtFecha = new TextView(this);
                txtUsuario.setText(mx.smartteam.data.Usuario.GetOne(context, currentUsuario.Id).Nombre);
                txtPop.setText(popVisita.DeterminanteGSP.toString());
                txtFecha.setText(popVisita.FechaCrea);
                txtUsuario.setTextColor(Color.BLACK);
                txtPop.setTextColor(Color.BLACK);
                txtFecha.setTextColor(Color.BLACK);
                row.addView(txtUsuario, new TableRow.LayoutParams(0));
                row.addView(txtPop, new TableRow.LayoutParams(0));
                row.addView(txtFecha, new TableRow.LayoutParams(0));
                row.addView(image, new TableRow.LayoutParams(0));
                tableLayout.addView(row, new TableLayout.LayoutParams());
            }
        } catch (Exception e) {
            Toast.makeText(context, "qwer" + e.getMessage(), Toast.LENGTH_LONG);
        }
    }
    
    private Handler HandlerProyecto = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Visitas();
        }
    };
    
    private Handler HandlerExceptions = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Exception exception = (Exception) msg.obj;
            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error", exception.getMessage());
        }
    };

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDoalog.incrementProgressBy(10);
        }
    };

    Handler handleup = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressDoalog.incrementProgressBy(1);
        }
    };
       //</editor-fold>

    /* ******************************* Download ******************************* */
    //Hilos de sincronizacion
    //<editor-fold>
    // Obtiene los pryectos a los que pertenece el usuario autenticado
    class GetProyecto implements Runnable {
        final ProgressDialog pd;
        
        public GetProyecto() {
            if(UsuarioInfo.getTelefono() == null || UsuarioInfo.getTelefono().toString().length() < 13) {
                Alertshow.dismiss();
            }
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Cargando...</font>"));
            pd.setMessage("Espere por favor...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        public void run() {
            final mx.smartteam.entities.ProyectoCollection EntityProyectoCollection;
            try {
                TelephonyManager TManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                
                //String tel = .toString(); 
               
                if(TManager != null) {
                   
                    currentUsuario.Sim = TManager.getSimSerialNumber() == null ? "" : TManager.getSimSerialNumber() ;
                    currentUsuario.IMEI = TManager.getDeviceId() == null ? "" : TManager.getDeviceId();
                    currentUsuario.Telefono = TManager.getLine1Number() + " " + telefonoInsert;//telefonoInsert;
                    //Toast.makeText(context, telefonoInsert, Toast.LENGTH_LONG).show();
                }
                //Descargamos los proyectos a los que el usuario esta asignado
                EntityProyectoCollection = mx.smartteam.business.Proyecto.GetByUsuario(currentUsuario);
                
                if(insertDataUser) {
                    mx.smartteam.business.Usuario.UpdateDevices(context, currentUsuario);
                }

                for (Proyecto proyecto : EntityProyectoCollection) {
                    // Registramos los proyectos a los que pertenece el usuario
                    mx.smartteam.entities.UsuarioProyecto usuarioProyecto = new UsuarioProyecto();
                    usuarioProyecto.IdUsuario = currentUsuario.Id;
                    usuarioProyecto.IdProyecto = proyecto.Id;
                    mx.smartteam.business.UsuarioProyecto.Insert(context, usuarioProyecto);

                    // Registramos el proyecto
                    mx.smartteam.business.Proyecto.Insert(context, proyecto);
                }

                //De los pryecto a los que pertenece el usuario solo consideramos el primero para descargar toda la informacion
                if (EntityProyectoCollection != null) {
                    currentProyecto = EntityProyectoCollection.get(0);
                   /* try {
                    
                       mx.smartteam.entities.Insumos_ConfigCollection insumo_config = mx.smartteam.business.Insumos_Config.DownloadConfig(currentProyecto);
                       mx.smartteam.entities.Areas_InsumosCollection areas_insumos = mx.smartteam.business.Areas_Insumos.DownloadAreas(currentProyecto);
                       mx.smartteam.entities.InsumosCollection insumos = mx.smartteam.business.Insumos.DownloadInsumos(currentProyecto);
                       mx.smartteam.entities.Unidad_MedidaCollection unidad = mx.smartteam.business.Unidad_Medida.DownloadUnidad(currentProyecto);
                        
                    } catch (Exception e){
                        e.getMessage();
                    }*/
                }

                Message msg = new Message();
                msg.obj = "";
                HandlerProyecto.sendMessage(msg);

                pd.dismiss();
                
            } catch (Exception ex) {
                Log.e("GetProyectos", ex.getMessage());
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
                pd.dismiss();
            }
        }
    }

    // Obtiene tiendas del proyecto
    class Getrutas implements Runnable {
        public void run() {
            try {
                mx.smartteam.entities.RutasCollection rutasCollection = mx.smartteam.business.Rutas.Download.GetRutas(currentProyecto, currentUsuario, time);
                for (Rutas pop : rutasCollection) {
                    mx.smartteam.business.Rutas.Insert(context, pop);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.obj = e;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetNotificaciones implements Runnable {
        public void run() {
            try {
                mx.smartteam.entities.NotificacionesCollection notificacionesCollection = mx.smartteam.business.Notificaciones.Download.GetNotificaciones(currentProyecto, time);
                for (Notificaciones notificaciones : notificacionesCollection) {
                    mx.smartteam.business.Notificaciones.Insert(context, notificaciones);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.obj = e;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetPop implements Runnable {
        @Override
        public void run() {
            try {
                mx.smartteam.entities.PopCollection popCollection = mx.smartteam.business.Pop.Download.GetByProyecto(currentProyecto, time, currentUsuario);
                for (Pop pop : popCollection) {
                    mx.smartteam.business.Pop.Insert(context, pop,currentUsuario, currentProyecto);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.obj = e;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetFormulario implements Runnable {
        @Override
        public void run() {
            try {
                mx.smartteam.entities.FormularioCollection formularioCollection = mx.smartteam.business.Formulario.Download.GetByProyectoAndUsuario(currentProyecto, currentUsuario, time);
                for (Formulario formulario : formularioCollection) {
                    mx.smartteam.business.Formulario.Insert(context, formulario);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetSondeo implements Runnable {
        @Override
        public void run() {
            try {
                mx.smartteam.entities.SondeoCollection sondeoCollection = mx.smartteam.business.Sondeo.Download.GetByProyectoAndUsuario(currentProyecto, currentUsuario, time);
                for (Sondeo sondeo : sondeoCollection) {
                    mx.smartteam.business.Sondeo.Insert(context, sondeo);
                    new Thread(new GetPreguntasPorSondeo(sondeo)).start();
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetCategoria implements Runnable {
        @Override
        public void run() {
            try {
                CategoriaCollection categoriaCollection = mx.smartteam.business.Categoria.Download.GetCategoriaProductoByProyecto(currentProyecto, time);
                for (mx.smartteam.entities.Categoria categoria : categoriaCollection) {
                    mx.smartteam.business.Categoria.Insert(context, categoria);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetMarca implements Runnable {
        @Override
        public void run() {
            try {
                MarcaCollection marcaCollection = mx.smartteam.business.Marca.Download.GetMarcaByProyecto(currentProyecto, time);
                for (mx.smartteam.entities.Marca marca : marcaCollection) {
                    mx.smartteam.business.Marca.Insert(context, marca);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetProducto implements Runnable {
        @Override
        public void run() {
            ProductoCollection productoCollection;
            try {
                productoCollection = mx.smartteam.business.Producto.Downdload.GetProductosByProyecto(currentProyecto, time);
                for (mx.smartteam.entities.Producto producto : productoCollection) {
                    mx.smartteam.business.Producto.Insert(context, producto);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }

        }
    }

  /*  class GetProductosByidCadena implements Runnable {
        private mx.smartteam.entities.ProductoCadena ProductoCadena;
        public GetProductosByidCadena(mx.smartteam.entities.ProductoCadena productoCadena) {
            this.ProductoCadena = productoCadena;
        }
        @Override
        public void run() {
            ProductoCollection productoCollection;
            try {
                productoCollection = mx.smartteam.business.Producto.Downdload.GetProductosByidCadena(currentProyecto, time, this.ProductoCadena);

                for (mx.smartteam.entities.Producto producto : productoCollection) {
                    mx.smartteam.business.Producto.Insert(context, producto);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }*/

    class GetPreguntasPorSondeo implements Runnable {
        private mx.smartteam.entities.Sondeo Sondeo;
        public GetPreguntasPorSondeo(mx.smartteam.entities.Sondeo sondeo) {
            this.Sondeo = sondeo;
        }
        @Override
        public void run() {
            PreguntaCollection preguntaCollection;
            try {
                preguntaCollection = mx.smartteam.business.Pregunta.Download.GetPreguntasBySondeo(currentProyecto, currentUsuario, this.Sondeo, time);
                for (mx.smartteam.entities.Pregunta pregunta : preguntaCollection) {
                    mx.smartteam.business.Pregunta.Insert(context, pregunta);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class GetProductoPorCadena implements Runnable {
        @Override
        public void run() {
            try {
                ProductoCadenaCollection productoCadenaCollection = mx.smartteam.business.ProductoCadena.Download.GetProductosCadena(currentProyecto, time);
                for (mx.smartteam.entities.ProductoCadena productoCadena : productoCadenaCollection) {
                    mx.smartteam.business.ProductoCadena.Insert(context, productoCadena);
                }
            } catch (Exception ex) {
                Message msg = new Message();
                msg.obj = ex;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

    class DeleteDatabase implements Runnable {
        final ProgressDialog pd;
        public DeleteDatabase() {
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Eliminando Base de datos</font>"));
            //pd.setTitle("Eliminando Base de datos");
            pd.setMessage("Espere por favor");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        public void run() {
            try {
                mx.smartteam.entities.PopVisitaCollection visitas;
                mx.smartteam.business.PopVisita.NoborrarBD(context, currentUsuario, currentProyecto);
                Thread.sleep(3000);
                pd.dismiss();
                Intent i = new Intent(context, SincronizarActivity.class);
                i.putExtra("usuario", currentUsuario);
                startActivity(i);
                finish();
            } catch (Throwable e) {
                pd.dismiss();
                Message msg = new Message();
                msg.obj = e;
                HandlerExceptions.sendMessage(msg);
            }
        }
    }

   //</editor-fold>  
    public void UploadTest() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection = null;
                    seguimientoGpsCollection = mx.smartteam.business.SeguimientoGps.GetSeguimientoGps(context, currentProyecto, currentUsuario);
                    if (seguimientoGpsCollection.size() > 0) {
                        String strResult = null;
                        strResult = mx.smartteam.business.SeguimientoGps.Upload.Insert(currentProyecto, currentUsuario, seguimientoGpsCollection);
                        if (strResult.equals("OK")) {
                            mx.smartteam.business.SeguimientoGps.ActualizarStatus(context, currentProyecto, currentUsuario);
                        }
                    }// END

                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        }).start();

    }// UPLOAD TEST

    /*  UPLOAD  */
     //<editor-fold>
    public void Uploads() {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMax(_numerovisitas);//pasar paremetro 
        progressDoalog.setTitle( Html.fromHtml("<font color='#FFFFF'>Upload</font>"));
        Sucursal sucursal= new Sucursal();
        Drawable d=sucursal.setIconAlert(context);
        progressDoalog.setIcon(d);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setMessage("Espere por favor...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();
        new Thread(new Runnable() {
            public void run() {
                mx.smartteam.entities.PopVisitaCollection visitas;
                mx.smartteam.entities.SondeoCollection sondeoCollection;
                mx.smartteam.entities.SodCollection sodCollection;
                try {
                    mx.smartteam.business.PopVisita.ChangeStatusStore(context, currentProyecto, currentUsuario); //data.RespuestaSondeo.UpdateStatusSync(context, contestacion);
                    visitas = mx.smartteam.business.PopVisita.GetPendings(context, currentUsuario, currentProyecto);
                    for (final PopVisita popVisita : visitas) {
                        //Creamos la visita
                        mx.smartteam.business.PopVisita.Upload.Insert(popVisita);
                        
                        /* Quitar en vrsion 31
                        SondeoCollection sCollection = new SondeoCollection();
                        sCollection = mx.smartteam.data.Sondeo.GetByProyecto(context, currentProyecto);
                        */
                        
                        /*  Subir Todas las imagenes */
                        
                        int count = 0;
                        count = mx.smartteam.business.Foto.countFotos(context, popVisita);
                        if (count > 0) {
                            for (int ii = 0; ii < count; ii++) 
                            {
                                FotoCollection fotoCollection = mx.smartteam.business.Foto.FotosVisitas(context, popVisita);
                                for (Foto foto : fotoCollection){
                                    String result = "";
                                    result = mx.smartteam.business.Foto.Upload.InsertV2(context, popVisita, foto);
                                    if(result.equals("OK")){
                                        foto.StatusSync = 1;
                                        mx.smartteam.business.Foto.UpdateStatusSync(context, foto);
                                    }
                                }
                            }
                        }
                        
                       /* Subimos SOD */
                       /* Tener integridad en SOD */
                        sodCollection = mx.smartteam.business.Sod.getAllnotUpload(context, popVisita);
                        if(sodCollection.size() > 0){
                            String result = "";
                            
                            result = mx.smartteam.business.Sod.SodInsert(popVisita, sodCollection);
                            
                            if(result.equals("OK")){
                            
                            mx.smartteam.business.Sod.UpateSodByVisita(context, popVisita);
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
                        
                         //Bodega
                         BodegaCollection bodegaCollection = mx.smartteam.business.Bodega.GetByVisita(context, popVisita);
                         for (final Bodega bodega : bodegaCollection) {
                         if (bodega.StatusSync.equals(0)) {
                         mx.smartteam.business.Bodega.Upload.Insert(popVisita, bodega);
                         bodega.StatusSync = 1;
                         mx.smartteam.business.Bodega.UpdateStatusSync(context, bodega);
                         }
                         }

                         //Material promocional
                         MaterialPromocionalCollection materialPromocionalCollection = mx.smartteam.business.MaterialPromocional.GetByVisita(context, popVisita);
                         for (final MaterialPromocional materialPromocional : materialPromocionalCollection) {
                         if (materialPromocional.StatusSync.equals(0)) {
                         mx.smartteam.business.MaterialPromocional.Upload.Insert(popVisita, materialPromocional);
                         materialPromocional.StatusSync = 1;
                         mx.smartteam.business.MaterialPromocional.UpdateStatusSync(context, materialPromocional);
                         }
                         }


                         //Exhibicion adicional
                         ExhibicionAdicionalCollection exhibicionAdicionalCollection = mx.smartteam.business.ExhibicionAdicional.GetByVisita(context, popVisita);
                         for (final ExhibicionAdicional exhibicionAdicional : exhibicionAdicionalCollection) {
                         if (exhibicionAdicional.StatusSync.equals(0)) {
                         mx.smartteam.business.ExhibicionAdicional.Upload.Insert(popVisita, exhibicionAdicional);
                         exhibicionAdicional.StatusSync = 1;
                         mx.smartteam.business.ExhibicionAdicional.UpdateStatusSync(context, exhibicionAdicional);
                         }
                         }

                         //Sondeos
                        sondeoCollection = mx.smartteam.business.Sondeo.GetByProyecto2(context, currentProyecto);
                         if(sondeoCollection.size() > 0){
                            for (final Sondeo sondeo : sondeoCollection) {
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
                    
                         //Enviamos el seguimiento
                         mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection = null;
                         seguimientoGpsCollection = mx.smartteam.business.SeguimientoGps.GetSeguimientoGps(context, currentProyecto, currentUsuario);
                         if(seguimientoGpsCollection.size()>0)
                         {
                         String strResult= null;
                         strResult = mx.smartteam.business.SeguimientoGps.Upload.Insert(currentProyecto, currentUsuario, seguimientoGpsCollection);
                         if(strResult.equals("OK"))
                         {
                         mx.smartteam.business.SeguimientoGps.ActualizarStatus(context, currentProyecto, currentUsuario);
                         }
                        
                         }// END
                         
                        mx.smartteam.entities.SosCollection sosCollection = null;
                        sosCollection = mx.smartteam.business.Sos.getSosCollection(context, popVisita);
                        if (sosCollection.size() > 0) {
                            String strResult = null;

                            strResult = mx.smartteam.business.Sos.Upload.Insert(popVisita, sosCollection);

                            if (strResult.equals("OK")) {
                                mx.smartteam.business.Sos.UpdateStatusSync(context, popVisita);
                            }
                        }

                        //Una ves enviada toda la informacion cambiamos el status de la visita
                        popVisita.StatusSync = 1;
                        mx.smartteam.business.PopVisita.UpdateStatusSync(context, popVisita);
                        handleup.sendMessage(handleup.obtainMessage());
                    }

                    mx.smartteam.data.Insumos_Answ_Activity.getAnswer(context);
                    
                    consicCollection = mx.smartteam.business.UtilSincronizacion.getbyconsinc(context, currentProyecto, currentUsuario);
                    time = consicCollection.get(0).UFechaDescarga;
                    mx.smartteam.entities.NotificacionesCollection notificacionesCollection = mx.smartteam.business.Notificaciones.Download.GetNotificaciones(currentProyecto, time);
                    for (Notificaciones notificaciones : notificacionesCollection) {
                        mx.smartteam.business.Notificaciones.Insert(context, notificaciones);
                    }
                    
                    Message msg = new Message();
                    msg.obj = "";
                    HandlerProyecto.sendMessage(msg);

                    progressDoalog.dismiss();

                    Intent i = new Intent(context, SincronizarActivity.class);
                    i.putExtra("usuario", currentUsuario);
                    startActivity(i);
                    finish();
                } catch (Exception ex) {//END
                    progressDoalog.dismiss();
                    Message msg = new Message();
                    msg.obj = ex;
                    HandlerExceptions.sendMessage(msg);
                } 
            }
        }).start();

    }/* UPLOAD */
//</editor-fold>
    /*END UPLOAD*/

    /*   Download    */
    //<editor-fold>
    public void Download() {
        
        progressDoalog = new ProgressDialog(SincronizarActivity.this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMax(100);
        progressDoalog.setTitle(Html.fromHtml("<font color='#FFFFF'>Download</font>"));
        Sucursal sucursal= new Sucursal();
        Drawable d=sucursal.setIconAlert(context);
        progressDoalog.setIcon(d);
        progressDoalog.setMessage("Espere por favor...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mx.smartteam.entities.PopVisitaCollection visitas;
                mx.smartteam.entities.SondeoCollection sondeoCollection;
                mx.smartteam.entities.Version versionx = new mx.smartteam.entities.Version();                
                try {
                    consicCollection = mx.smartteam.business.UtilSincronizacion.getbyconsinc(context, currentProyecto, currentUsuario);
                    time = consicCollection.get(0).UFechaDescarga;
                    
                    //Obtiene la ultima version del software
                    versionx.IdApp = 1;
                    versionx.version = Integer.parseInt(getString(R.string.version_actual));
                    mx.smartteam.entities.VersionCollection versionCollection = mx.smartteam.business.Version.Download.GetByVersion(versionx);
                    for (Version versiones : versionCollection) {
                        mx.smartteam.business.Version.Insert(context, versiones);
                    }

                    /* Mandar y Descargar información de la nueva app */
                    mx.smartteam.entities.VersionUsuarioCollection versionUsuarioCollection = mx.smartteam.business.VersionesUsuarios.Download.GetVersionesUsuarios(versionx, currentUsuario);
                    if(versionUsuarioCollection.size() > 0 ){
                        for(mx.smartteam.entities.VersionUsuario versionUsuario : versionUsuarioCollection){
                            mx.smartteam.data.VersionesUsuarios.Insert(context, versionUsuario);
                        }
                    }
                    // Descargamos exhibiciones config
                    ExhibicionConfigCollection exhibicionConfigCollection  = mx.smartteam.business.ExhibicionConfig.Download.GetExhibicionConfig(currentProyecto, currentUsuario,time);
                    for(mx.smartteam.entities.ExhibicionConfig exhibicionConfig : exhibicionConfigCollection ){
                        mx.smartteam.business.ExhibicionConfig.Insert(context, exhibicionConfig);
                    }
                    
                    if(exhibicionConfigCollection.size()>0){
                        mx.smartteam.entities.ExhibicionCollection exhibicionCollection = mx.smartteam.business.Exhibicion.Download.GetExhibicion();
                        for(mx.smartteam.entities.Exhibicion exhibicion : exhibicionCollection ){
                            mx.smartteam.business.Exhibicion.Insert(context, exhibicion);
                        }
                    }
                    
                    mx.smartteam.entities.PonderacionCollection ponderacionCollection = mx.smartteam.business.Ponderacion.Download.getAllPonderacion(currentProyecto, time);
                    for(mx.smartteam.entities.Ponderacion ponderacion : ponderacionCollection){
                       mx.smartteam.business.Ponderacion.Insert(context, ponderacion);
                    }
                    
                    mx.smartteam.entities.ScoredCardCollection scoredcardCollection = mx.smartteam.business.ScoredCard.Download.getgetAllCalificacionesDiarias(currentProyecto, currentUsuario, time);
                    for(mx.smartteam.entities.ScoredCard scard : scoredcardCollection){
                        mx.smartteam.business.ScoredCard.Insert(context, scard);
                    }
                    
                    //Obtiena la configuracion del GPS                    
                    mx.smartteam.entities.MovilConfigCollection movilConfigCollection = mx.smartteam.business.MovilConfig.GetByMovilConfig(currentUsuario, currentProyecto); //data.MovilConfig.Download.GetByMovilConfig(currentUsuario, currentProyecto);
                    for (mx.smartteam.entities.MovilConfig movilConfig : movilConfigCollection) {
                        mx.smartteam.data.MovilConfig.configGPS(context, currentUsuario, movilConfig);
                       // mx.smartteam.data.MovilConfig.ActualizaGps(context, currentUsuario, movilConfig);offline
                    }
                    handle.sendMessage(handle.obtainMessage());/* 10% */

                    //Obtiene tiendas
                    mx.smartteam.entities.PopCollection popCollection = mx.smartteam.business.Pop.Download.GetByProyecto(currentProyecto, time, currentUsuario);
                    for (Pop pop : popCollection) {
                        mx.smartteam.business.Pop.Insert(context, pop,currentUsuario, currentProyecto);
                    }
                    handle.sendMessage(handle.obtainMessage());/* 20% */
                    
                    //Obtenemos Notificaciones
                    mx.smartteam.entities.NotificacionesCollection notificacionesCollection2 = mx.smartteam.business.Notificaciones.Download.GetMensajesByUsuario(currentProyecto, time, currentUsuario);
                    for (Notificaciones notificaciones2 : notificacionesCollection2) {
                        mx.smartteam.business.Notificaciones.Insert(context, notificaciones2);
                    }
                    
                    //Obtenemos Notificaciones
                    mx.smartteam.entities.NotificacionesCollection notificacionesCollection = mx.smartteam.business.Notificaciones.Download.GetNotificaciones(currentProyecto, time);
                    for (Notificaciones notificaciones : notificacionesCollection) {
                        mx.smartteam.business.Notificaciones.Insert(context, notificaciones);
                    }
                    
                    mx.smartteam.entities.MetaCadenaCollection metaCadenaCollection = mx.smartteam.business.MetaCadena.getAllMetaCadena(currentProyecto, time);
                    for(mx.smartteam.entities.MetaCadena metaCadena : metaCadenaCollection){
                        mx.smartteam.business.MetaCadena.Insert(context, metaCadena);
                    }
                    handle.sendMessage(handle.obtainMessage()); /* 30% */ 
                    
                    //Obtenemos los formularios    
                    mx.smartteam.entities.FormularioCollection formularioCollection = mx.smartteam.business.Formulario.Download.GetByProyectoAndUsuario(currentProyecto, currentUsuario, time);
                    for (Formulario formulario : formularioCollection) {
                        mx.smartteam.business.Formulario.Insert(context, formulario);
                    }
                    handle.sendMessage(handle.obtainMessage());/* 40% */
                    
                    //Obtiene categorias del proyecto
                    CategoriaCollection categoriaProductoCollection = mx.smartteam.business.Categoria.Download.GetCategoriaProductoByProyecto(currentProyecto, time);
                    for (mx.smartteam.entities.Categoria categoria : categoriaProductoCollection) {
                        mx.smartteam.business.Categoria.Insert(context, categoria);
                    }
                    handle.sendMessage(handle.obtainMessage());/* 50% */

                    //Obtiene categoria foto del proyecto
                    CategoriaCollection categoriaFotoCollection = mx.smartteam.business.Categoria.Download.GetCategoriaFotoByProyecto(currentProyecto, currentUsuario, time);
                    for (mx.smartteam.entities.Categoria categoria : categoriaFotoCollection) {
                        mx.smartteam.business.Categoria.Insert(context, categoria);
                    }
                    handle.sendMessage(handle.obtainMessage()); /* 60% */
                    
                    //Obtiene opciones 
                    OpcionCollection opcionFotoCollection = mx.smartteam.business.Opcion.Download.GetOpcionesFoto(currentProyecto, currentUsuario, time);
                    for (mx.smartteam.entities.Opcion opcion : opcionFotoCollection) {
                        mx.smartteam.business.Opcion.Insert(context, opcion);
                    }

                    //Obtiene Marcas
                    MarcaCollection marcaCollection = mx.smartteam.business.Marca.Download.GetMarcaByProyecto(currentProyecto, time);
                    for (mx.smartteam.entities.Marca marca : marcaCollection) {
                        mx.smartteam.business.Marca.Insert(context, marca);
                    }
                    
                    //obtiene CategoriasConfig
                    CategoriasConfigCollection configCategoriasCollection = mx.smartteam.business.Categoria.Download.GetCategoriasConfig(currentProyecto,currentUsuario, time);
                    for (mx.smartteam.entities.CategoriasConfig configCategorias : configCategoriasCollection) {
                         mx.smartteam.business.Categoria.InsertCategoriasConfig(context,configCategorias);
                    }
                    
                    //Obtiene las Marcasconfig
                    MarcaConfigCollection marcaconfigCollection =mx.smartteam.business.Marca.Download.GetMarcaconfig(currentProyecto,currentUsuario, time);
                    for(mx.smartteam.entities.MarcaConfig marcaConfig: marcaconfigCollection){
                        mx.smartteam.business.Marca.InsertMarcaConfig(context, marcaConfig);
                    }
                    handle.sendMessage(handle.obtainMessage()); /* 70% */
                    
                    //obtieneproductos     
                    ProductoCollection productoCollection = mx.smartteam.business.Producto.Downdload.GetProductosAll(currentProyecto, time);
                        for (mx.smartteam.entities.Producto producto : productoCollection) {
                            mx.smartteam.business.Producto.Insertp(context, producto);
                    }
                        //obtiene productoscadenas
                    ProductoCadenaCollection productoCadenaCollection = mx.smartteam.business.ProductoCadena.Download.GetConjuntoCadenasbyUsuario(currentProyecto, currentUsuario,time);
                    for (mx.smartteam.entities.ProductoCadena productoCadena : productoCadenaCollection) {
                            mx.smartteam.business.ProductoCadena.Insert(context, productoCadena);
                    }
                    handle.sendMessage(handle.obtainMessage());/* 80% */

                    //Obtiene Rutas
                    mx.smartteam.entities.RutasCollection rutasCollection = mx.smartteam.business.Rutas.Download.GetRutas(currentProyecto, currentUsuario, time);
                    for (mx.smartteam.entities.Rutas rutas : rutasCollection) {
                        mx.smartteam.business.Rutas.Insert(context, rutas);
                    }
                    handle.sendMessage(handle.obtainMessage());/* 90% */

                    //Obtiene sondeos
                    sondeoCollection = mx.smartteam.business.Sondeo.Download.GetByProyectoAndUsuario(currentProyecto, currentUsuario, time);
                    for (Sondeo sondeo : sondeoCollection) {
                        mx.smartteam.business.Sondeo.Insert(context, sondeo);
                        //Obtiene preguntas
                        PreguntaCollection preguntaCollection = mx.smartteam.business.Pregunta.Download.GetPreguntasBySondeo(currentProyecto, currentUsuario, sondeo, time);
                        for (mx.smartteam.entities.Pregunta pregunta : preguntaCollection) {
                            mx.smartteam.business.Pregunta.Insert(context, pregunta);
                        }
                    }
                    
                    // ACTUALIZAMOS EL ESTADO DE DESCARGA cuando se ha descargado todo sin ningun problema
                    mx.smartteam.business.UtilSincronizacion.UpdateSincro(context, currentProyecto, currentUsuario);
                    handle.sendMessage(handle.obtainMessage()); /* ### 100% descarga perfecta   ### */
                    Thread.sleep(1000);
                    progressDoalog.dismiss();
                    
                    //new Thread(new GetProyecto()).start();
                
                } catch (Throwable ex) {
                    progressDoalog.dismiss(); Message msg = new Message();
                    msg.obj = ex; HandlerExceptions.sendMessage(msg);
                }
            }
        }).start();
    }
//</editor-fold>
    
/* END DOWNLOAD */

    public void enviarCorreo() {
        try {
            Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
            itSend.setType("plain/text");
            itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"integraskuonline@gmail.com"});
            itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, currentUsuario.Id + currentProyecto.Id + Utilerias.getFechaUnix());
            itSend.putExtra(android.content.Intent.EXTRA_TEXT, "se adjunta archivo encriptado de DB");
            String stemail = "file:///mnt/sdcard/external_sd/smartteam"; 
            itSend.putExtra(Intent.EXTRA_STREAM, Uri.parse(stemail));
            startActivity(itSend);
        } catch (Exception e) {
            e.getMessage().toString();
        }
    }

}  /* END CLASS  */
