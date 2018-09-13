package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Anaquel;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class PreciosActivity extends Activity {

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
    private Integer idFoto = 0;
    EditText EditExistencia, EditTotal,EditComentarios, EditTramos, EditFAlturaTecho,
            EditFAlturaOjos, EditFAlturaManos, EditFAlturaSuelo;
    TextView existencia, precio, prodFren, Coment, Precio,precioSug,precioProm,Textpsug,Textpprom;
    private EnumFormulario currentFormulario;
    private Integer spCategoria,spMarca;
    private String tipoform;
    Sucursal sucursal =new Sucursal();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anaquel_precio);
        this.context = this;
        productoCollection = new ProductoCollection();
        ArrayList<mx.smartteam.entities.Producto> ProductosPorMarca = (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollection");
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        this.tipoform = (String) bundle.get("tipoform") == null ? null : (String) bundle.get("tipoform");

        for (int i = 0; i < ProductosPorMarca.size(); i++) {
            productoCollection.add(ProductosPorMarca.get(i));
        }
        if (productoCollection.size() >= 1) {
            this.currentProducto = productoCollection.get(0);
        }

        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        onLoadData();
    }

    protected void onLoadData() {
        try {
            currentAnaquel = mx.smartteam.business.Anaquel.GetInfoByVisita(context, currentPop, currentProducto, "PRECIO");
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null) {
            this.setTitle(Html.fromHtml("<small><strong>" + currentProducto.Nombre + "</strong></small>"));
            this.EditTotal = (EditText) findViewById(R.id.EditTotal);
            this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);
            this.precioSug = (TextView) findViewById(R.id.psug);
            this.precioProm = (TextView) findViewById(R.id.pprom);
            this.Textpsug = (TextView) findViewById(R.id.Textpsug);
            this.Textpprom = (TextView) findViewById(R.id.Textpprom);
            
            if(this.currentProducto.ppromocion!=null && this.currentProducto.ppromocion!=0.0){
                this.precioProm.setText(""+currentProducto.ppromocion);
            }else{
                //this.precioProm.setText("Precio no cargado");
                this.precioProm.setVisibility(View.GONE);
                this.Textpprom.setVisibility(View.GONE);
            }
            if(this.currentProducto.psugerido!=null && this.currentProducto.psugerido!=0.0){
                this.precioSug.setText(""+currentProducto.psugerido);
            }else{
                //this.precioSug.setText("Precio no cargado");
                this.precioSug.setVisibility(View.GONE);
                this.Textpsug.setVisibility(View.GONE);
            }
        }

        if (currentAnaquel != null) {
            if (currentAnaquel.Precio != null) {
                if (currentAnaquel.Precio != 0) {
                    EditTotal.setText("" + currentAnaquel.Precio);
                } else {
                    EditTotal.setText("");
                }
                if (currentAnaquel.Comentario == null) {
                    EditComentarios.setText("");
                } else {
                    EditComentarios.setText("" + currentAnaquel.Comentario);
                }
            }
        } else {
            EditTotal.setText("");
            EditComentarios.setText("");
        }
    }
    
    public void goBack(){
        Intent ii;
        ii = new Intent(PreciosActivity.this , ProductoActivity.class);
        ii.putExtra("usuario", currentUsuario);
        ii.putExtra("proyecto", currentProyecto);
        ii.putExtra("pop", currentPop);
        ii.putExtra("item", currentFormulario);
        ii.putExtra("spCategoria", spCategoria);
        ii.putExtra("spMarca", spMarca);
        ii.putExtra("tipo", tipoform);
        startActivity(ii);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_siguiente:
                Siguiente();
            return true;
                
            case R.id.action_guardar:
                Guardar();
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

    @Override
    protected ProgressDialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                progDialog = new ProgressDialog(this);
                progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDialog.setTitle(R.string.title_dialog);
                progDialog.setMessage(getResources().getString(R.string.message_espere_dialog));
                return progDialog;
            case 1:
                progDialog = new ProgressDialog(this);
                progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progDialog.setMessage("Dollars in checking account:");
                return progDialog;
            default:
                return null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissDialog(0);
            String confirmacion = (String) msg.obj;
            Toast.makeText(context, confirmacion, Toast.LENGTH_LONG).show();
        }
    };

    private void Guardar() {
        try {
            final Anaquel objAnaquel = new Anaquel();
            objAnaquel.IdProyecto = currentProyecto.Id;
            objAnaquel.IdUsuario = currentUsuario.Id;
            objAnaquel.DeterminanteGSP = currentPop.DeterminanteGSP;
            objAnaquel.Sku = currentProducto.SKU;
            objAnaquel.IdVisita = currentPop.IdVisita;
            objAnaquel.Precio = EditTotal.getText().toString().isEmpty() ? null : Double.parseDouble(EditTotal.getText().toString());
            objAnaquel.Comentario = EditComentarios.getText().toString();
            //objAnaquel.Fanaquel = Integer.parseInt("1");
            objAnaquel.Tipo = "PRECIO";
            
            if(currentAnaquel != null){
                objAnaquel.IdFoto = currentAnaquel.IdFoto;
            }
            
            if(objAnaquel.Precio < currentProducto.Minimo && currentProducto.Minimo > 0 ){
                DialogToPrecio(objAnaquel, currentProducto,1);  
            }else if(objAnaquel.Precio > currentProducto.Maximo && currentProducto.Maximo > 0 ){
                DialogToPrecio(objAnaquel, currentProducto,0);
            }else{
                new Thread(new Save(objAnaquel)).start();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void foto() {
        Intent i;
        mx.smartteam.entities.Foto objFoto = new  mx.smartteam.entities.Foto();
        objFoto.Tipo = objFoto.Tipo.Precio;
        i = new Intent(this, CameraActivity.class);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("producto",currentProducto);
        i.putExtra("pop", currentPop);
        i.putExtra("opcionfoto",7);/* Precios */
        i.putExtra("foto", objFoto);
        startActivity(i);
        super.onStop();
    }

    public void GuardarFoto(){}
    
    private void Siguiente() {
        Guardar();
        productoCollection.remove(currentProducto);
        if (productoCollection.size() > 0) {
            if (productoCollection.size() == 1) {
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

    public void ThisElements() {
        setContentView(R.layout.activity_anaquel);
        this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));
        this.context = this;
        currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        try {
            anaquelC = mx.smartteam.business.Anaquel.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop, productoCollection.get(0).Nombre);
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null && productoCollection.size() > 0) {
            this.setTitle(Html.fromHtml("<small><strong>" + currentProducto.Nombre + "</strong></small>"));
            this.EditFAlturaTecho = (EditText) findViewById(R.id.EditFAlturaTecho);
            this.EditFAlturaOjos = (EditText) findViewById(R.id.EditFAlturaOjos);
            this.EditFAlturaManos = (EditText) findViewById(R.id.EditFAlturaManos);
            this.EditFAlturaSuelo = (EditText) findViewById(R.id.EditFAlturaSuelo);
            this.EditTotal = (EditText) findViewById(R.id.EditTotal);
            this.EditExistencia = (EditText) findViewById(R.id.EditExistencia);
            this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);
            EditTotal.setVisibility(View.GONE);
            EditComentarios.setVisibility(View.GONE);
            EditExistencia.setVisibility(View.GONE);
            existencia.setVisibility(View.GONE);
            rbSi.setVisibility(View.GONE);
            rbNo.setVisibility(View.GONE);
            prodFren.setVisibility(View.GONE);
            layaut = (LinearLayout) findViewById(R.id.LeyautFrentes);
            layaut.setVisibility(View.VISIBLE);
            EditFAlturaTecho.setVisibility(View.GONE);
            EditFAlturaOjos.setVisibility(View.GONE);
            EditFAlturaManos.setVisibility(View.GONE);
            EditFAlturaSuelo.setVisibility(View.GONE);
        } else {
            finish();
        }

        if (anaquelC.Sku != 0) {
            if (anaquelC.Cantidad != 0 || anaquelC.Cantidad != null) {
                EditExistencia.setText("" + anaquelC.Cantidad);
            } else {
                EditExistencia.setText("");
            }

            if (anaquelC.Comentario != null || !anaquelC.Comentario.equals("null")) {
                EditComentarios.setText("" + anaquelC.Comentario);
            } else {
                EditComentarios.setText("");
            }
        } else {
            EditExistencia.setText("");
            EditComentarios.setText("");
        }
    }

    class Save implements Runnable {
        private final Anaquel anaquel;
        
        public Save(Anaquel anaquel) {
            this.anaquel = anaquel;
        }

        @Override
        public void run() {
            try {
                int idfoto = PhotoActivity.idfoto;        
                if(idfoto>0){
                    this.anaquel.IdFoto=idfoto;
                }
                if (currentAnaquel != null) {
                    this.anaquel.Id = currentAnaquel.Id;
                    mx.smartteam.business.Anaquel.Update(context, this.anaquel);
                } else {
                    if(anaquel.Precio != null){
                        mx.smartteam.business.Anaquel.Insert(context, this.anaquel);
                    }
                }
                PhotoActivity.idfoto = 0;
                
                
                
                
                runOnUiThread(new Runnable() {
                                public void run() 
                                { 
                                    try {
                                            // Mandamos el mensaj
                                        String msg = mx.smartteam.data.Anaquel.countInsertVisita(context, currentPop);
                                    
                                        if(msg != null){
                                            String[] parts = msg.split(",");
                                            int part1 = Integer.parseInt(parts[0]); // anio
                                            int part2 = Integer.parseInt(parts[1]); 
                                            
                                            
                                        Toast.makeText(context, "LLevas "+ part1 + " capturas de " + part2, Toast.LENGTH_LONG).show();
                                        }
                                        
                                        
                                        
                                    } catch (Exception ex) {
                                        Logger.getLogger(Sucursal.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } 
                            }); 
                
                
                goBack();
            } catch (Exception e) {
                Log.e("Error Save[Anaquel]", e.getMessage());
            }
        }
    }

    public void DialogToPrecio(final mx.smartteam.entities.Anaquel anaquel, mx.smartteam.entities.Producto producto, int bandera) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>Precios</font>"));
        Drawable d=sucursal.setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        String mensaje = "";
        
        switch(bandera){
                case 0:
                    mensaje = "El precio supera el máximo permitido de $ "+ producto.Maximo + "\n, Verifique por favor!";
                break;
            
                case 1:
                    mensaje = "El precio no supera el mínimo permitido de $ "+producto.Minimo+ "\n , Verifique por favor!";
                break;
        }
        
        alertDialogBuilder
        .setMessage(mensaje)
        .setCancelable(false)
        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // new Thread(new Save(anaquel)).start();
                
                
                
            }
        });
        //.setNegativeButton("Cancelar", null);
        
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        super.onStop();
    }
}
