package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import mx.triplei.R;
import mx.smartteam.entities.Anaquel;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;


public class FrentesActivity extends Activity {

    ProgressDialog progDialog;
    private RadioGroup rad;
    private RadioButton rbSi, rbNo;
    private LinearLayout layaut;
    private Producto currentProducto;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private PopVisita currentPopVisita;
    private Context context;
    private Anaquel currentAnaquel = null, anaquelC = null, anaquelD = null;
    private ProductoCollection productoCollection = null;
    private EnumFormulario currentFormulario;
    private Integer spCategoria,spMarca;
    private Integer bandera=0;
    EditText EditExistencia, EditTotal,
            EditComentarios, EditTramos, EditFAlturaTecho,
            EditFAlturaOjos, EditFAlturaManos, EditFAlturaSuelo;
    TextView existencia, precio, prodFren, Coment, Precio;
    private String tipoform;
    /**
     * ****************************************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anaquel);

        this.context = this;
        //productoCollection = new ProductoCollection();
        //ArrayList<mx.smartteam.entities.Producto> ProductosPorMarca = (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollection");
        
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        this.tipoform = (String) bundle.get("tipoform") == null ? null : (String) bundle.get("tipoform");
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        
        setTitle(Html.fromHtml("<small><strong>" + currentProducto.Nombre + "</strong></small>"));
        EditFAlturaTecho = (EditText) findViewById(R.id.EditFAlturaTecho);
        EditFAlturaOjos = (EditText) findViewById(R.id.EditFAlturaOjos);
        EditFAlturaManos = (EditText) findViewById(R.id.EditFAlturaManos);
        EditFAlturaSuelo = (EditText) findViewById(R.id.EditFAlturaSuelo);
        EditTotal = (EditText) findViewById(R.id.EditTotal);
        EditExistencia = (EditText) findViewById(R.id.EditExistencia);
        EditComentarios = (EditText) findViewById(R.id.EditComentarios);
        
        onLoadData();
    }

    protected void onLoadData() {

        try {
            currentAnaquel = mx.smartteam.business.Anaquel.GetInfoByVisita(context, currentPop, currentProducto, "ANAQUEL");
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (currentAnaquel != null) {
            if (currentAnaquel.Cantidad != null) {
                if (currentAnaquel.Cantidad.equals(0)) {
                    EditExistencia.setText("");
                } else {
                    EditExistencia.setText("" + currentAnaquel.Cantidad);
                }
            } else {
                EditExistencia.setText("");
            }
            
            if (currentAnaquel.Techo != null) {
                if (currentAnaquel.Techo.equals(0)) {
                    EditFAlturaTecho.setText("");
                } else {
                    EditFAlturaTecho.setText("" + currentAnaquel.Techo);
                }
            } else {
                EditFAlturaTecho.setText("");
            }

            if (currentAnaquel.Ojos != null) {
                if (currentAnaquel.Ojos.equals(0)) {
                    EditFAlturaOjos.setText("");
                } else {
                    EditFAlturaOjos.setText("" + currentAnaquel.Ojos);
                }
            } else {
                EditFAlturaOjos.setText("");
            }

            if (currentAnaquel.Manos != null) {
                if (currentAnaquel.Manos.equals(0)) {
                    EditFAlturaManos.setText("");
                } else {
                    EditFAlturaManos.setText("" + currentAnaquel.Manos);
                }
            } else {
                EditFAlturaManos.setText("");
            }

            if (currentAnaquel.Suelo != null) {
                if (currentAnaquel.Suelo.equals(0)) {
                    EditFAlturaSuelo.setText("");
                } else {
                    EditFAlturaSuelo.setText("" + currentAnaquel.Suelo);
                }
            } else {
                EditFAlturaSuelo.setText("");
            }
            
            if (currentAnaquel.Comentario != null) {
                if (currentAnaquel.Comentario.equals("")) {
                    EditComentarios.setText("");
                } else {
                    EditComentarios.setText("" + currentAnaquel.Comentario);
                }
            } else {
                EditComentarios.setText("");
            }

        } else 
        {
            EditExistencia.setText("");
            EditFAlturaTecho.setText("");
            EditFAlturaOjos.setText("");
            EditFAlturaManos.setText("");
            EditFAlturaSuelo.setText("");
            EditComentarios.setText("");
            //EditTotal.setText("");
        }
    }

    /**
     * ***************************************************************************
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anquel_sondeo, menu);

        if (mx.triplei.ProductoActivity.porProducto) {
            menu.removeItem(R.id.action_siguiente);
        } else {
            menu.removeItem(R.id.action_guardar);
        }
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    /**
     * ****************************************************************************
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_siguiente:
                Siguiente();
                return true;

            case R.id.action_guardar:
                Guardar();
                //goBack();
                return true;

            case R.id.action_cancelar:
                finish();
                return true;

            case R.id.action_foto:
                foto();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * ***************************************************************************
     */
    @Override
    protected ProgressDialog onCreateDialog(int id) {
        switch (id) {
            case 0:                      // Spinner
                progDialog = new ProgressDialog(this);
                progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDialog.setTitle(R.string.title_dialog);
                progDialog.setMessage(getResources().getString(R.string.message_espere_dialog));

                //progThread = new ProgressThread(handler);
                //progThread.start();
                return progDialog;
            case 1:                      // Horizontal
                progDialog = new ProgressDialog(this);
                progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //progDialog.setMax(maxBarValue);
                progDialog.setMessage("Dollars in checking account:");
                //progThread = new ProgressThread(handler);
                //progThread.start();
                return progDialog;
            default:
                return null;
        }
    }
    /**
     * *********************************************************************************************************
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //confirmacion del registro de informacion
            dismissDialog(0);
            String confirmacion = (String) msg.obj;
            Toast.makeText(context, confirmacion, Toast.LENGTH_LONG).show();

            //Preguntamos si desea capturar otro producto
            // OtroProducto();
        }
    };

    /**
     * *********************************************************************************************************
     */
    private void Guardar() {

        try {
            final Anaquel objAnaquel = new Anaquel();

            objAnaquel.IdProyecto = currentProyecto.Id;
            objAnaquel.IdUsuario = currentUsuario.Id;
            objAnaquel.DeterminanteGSP = currentPop.DeterminanteGSP;
            objAnaquel.Sku = currentProducto.SKU;
            objAnaquel.IdVisita = currentPop.IdVisita;
            
            objAnaquel.Cantidad = EditExistencia.getText().toString().isEmpty() ? null : Integer.parseInt(EditExistencia.getText().toString());
            if(objAnaquel.Cantidad!= null){bandera++;}
            objAnaquel.Suelo = EditFAlturaSuelo.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaSuelo.getText().toString());
            if(objAnaquel.Suelo!= null){bandera++;}
            objAnaquel.Manos = EditFAlturaManos.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaManos.getText().toString());
            if(objAnaquel.Manos != null){bandera++;}
            objAnaquel.Ojos = EditFAlturaOjos.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaOjos.getText().toString());
            if(objAnaquel.Ojos!= null){bandera++;}
            objAnaquel.Techo = EditFAlturaTecho.getText().toString().isEmpty() ? null : Integer.parseInt(EditFAlturaTecho.getText().toString());
            if(objAnaquel.Techo!= null){bandera++;}
            
            objAnaquel.Comentario = EditComentarios.getText().toString().isEmpty() ? null : EditComentarios.getText().toString();
            if(objAnaquel.Comentario != null){bandera++;}
            
            objAnaquel.Tipo = "ANAQUEL";
            if(currentAnaquel!= null){
                objAnaquel.IdFoto = currentAnaquel.IdFoto;
            }
            
            new Thread(new Save(objAnaquel)).start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * *********************************************************************************************************
     */
    private void foto() {
        Intent i;
        mx.smartteam.entities.Foto  objFoto= new  mx.smartteam.entities.Foto();
        objFoto.Tipo = objFoto.Tipo.Anaquel;
        i = new Intent(this, CameraActivity.class);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("pop", currentPop);
        i.putExtra("opcionfoto",12); /* solo anaquel  */
        i.putExtra("producto", currentProducto);
        i.putExtra("foto", objFoto);
        startActivity(i);
        super.onStop();
    }

    /**
     * *********************************************************************************************************
     */
    private void Siguiente() {

        Guardar();

        productoCollection.remove(currentProducto);
        if (productoCollection.size() > 0) {
            if (productoCollection.size() == 1) {
                //Guardar();
                currentProducto = productoCollection.get(0);

            } else {
                currentProducto = productoCollection.get(productoCollection.size() - 1);
                onLoadData();
            }
            onLoadData();

        } else {
            goBack();
            this.finish();
        }

    }

    /**
     * *********************************************************************************************************
     */
    
    public void goBack(){
        Intent ii;
        ii = new Intent(FrentesActivity.this , ProductoActivity.class);
        ii.putExtra("usuario", currentUsuario);
        ii.putExtra("proyecto", currentProyecto);
        ii.putExtra("pop", currentPop);
        ii.putExtra("item", currentFormulario);
        ii.putExtra("spCategoria", spCategoria);
        ii.putExtra("spMarca", spMarca);
        ii.putExtra("tipo", tipoform);
        startActivity(ii);
        finish();
    }
       
    public void ThisElements() {

        //Alan
        setContentView(R.layout.activity_anaquel);
        //setContentView(R.layout.activity_anaquel);<--original
        //
        this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));

        this.context = this;
        currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

        try {
            //  if(currentProducto.bandera>0)
            //{
            anaquelC = mx.smartteam.business.Anaquel.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop, productoCollection.get(0).Nombre);
            // }
        } catch (Exception ex) {
            ex.getMessage();
        }

        //    anaquelC = mx.smartteam.business.Anaquel.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop,productoCollection.get(0).Nombre);
        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null && productoCollection.size() > 0) {

            //this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));
            this.setTitle(Html.fromHtml("<small><strong>" + currentProducto.Nombre + "</strong></small>"));
            this.EditFAlturaTecho = (EditText) findViewById(R.id.EditFAlturaTecho);
            this.EditFAlturaOjos = (EditText) findViewById(R.id.EditFAlturaOjos);
            this.EditFAlturaManos = (EditText) findViewById(R.id.EditFAlturaManos);
            this.EditFAlturaSuelo = (EditText) findViewById(R.id.EditFAlturaSuelo);
            this.EditTotal = (EditText) findViewById(R.id.EditTotal);
            this.EditExistencia = (EditText) findViewById(R.id.EditExistencia);
            //this.existencia = (TextView) findViewById(R.id.Existencia);
            this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);
            EditTotal.setVisibility(View.GONE);
            EditComentarios.setVisibility(View.GONE);
            EditExistencia.setVisibility(View.GONE);
            Coment.setVisibility(View.GONE);
            Precio.setVisibility(View.GONE);
            existencia.setVisibility(View.GONE);
            rbSi.setVisibility(View.GONE);
            rbNo.setVisibility(View.GONE);
            prodFren.setVisibility(View.GONE);
            layaut = (LinearLayout) findViewById(R.id.LeyautFrentes);
            layaut.setVisibility(View.VISIBLE);

        } else {
            finish();
        }

        if (anaquelC.Sku != 0) {
            if (anaquelC.Techo != null) {
                if (anaquelC.Techo.equals(0)) {
                    EditFAlturaTecho.setText("");
                } else {
                    EditFAlturaTecho.setText("" + anaquelC.Techo);
                }
            } else {
                EditTotal.setText("");
            }

            if (anaquelC.Ojos != null) {
                if (anaquelC.Ojos.equals(0)) {
                    EditFAlturaOjos.setText("");
                } else {
                    EditFAlturaOjos.setText("" + anaquelC.Ojos);
                }
            } else {
                EditTotal.setText("");
            }

            if (anaquelC.Manos != null) {
                if (anaquelC.Manos.equals(0)) {
                    EditFAlturaManos.setText("");
                } else {
                    EditFAlturaManos.setText("" + anaquelC.Manos);
                }
            } else {
                EditTotal.setText("");
            }

            if (anaquelC.Suelo != null) {
                if (anaquelC.Suelo.equals(0)) {
                    EditFAlturaSuelo.setText("");
                } else {
                    EditFAlturaSuelo.setText("" + anaquelC.Suelo);
                }
            } else {
                EditTotal.setText("");
            }

        } else {

            EditFAlturaTecho.setText("");
            EditFAlturaOjos.setText("");
            EditFAlturaManos.setText("");
            EditFAlturaSuelo.setText("");
        }

        //anaquelB = anaquelC;
    }

    /**
     * **************************************************************************
     */
    class Save implements Runnable {

        private final Anaquel anaquel;

        public Save(Anaquel anaquel) {
            this.anaquel = anaquel;
        }

        @Override
        public void run() {
            try {
                //verifica si ya existe el anaquel    
                //anaquelD = mx.smartteam.business.Anaquel.GetInfoByVisita(context, currentPop, currentProducto,"ANAQUEL");
                // Thread.sleep(2000);
                
                int idfoto= PhotoActivity.idfoto;
                if(idfoto > 0){
                    this.anaquel.IdFoto=idfoto;
                }
                if (currentAnaquel != null) {
                    this.anaquel.Id = currentAnaquel.Id;
                    mx.smartteam.business.Anaquel.Update(context, this.anaquel);
                } else {
                    if(bandera>0){
                        mx.smartteam.business.Anaquel.Insert(context, this.anaquel);
                    }
                }
                PhotoActivity.idfoto =0;
                bandera = 0;
                goBack();
                        
                        
            } catch (Exception e) {
                Log.e("Error Save[Anaquel]", e.getMessage());
            }

        }
    }


}
