package mx.triplei;

import android.app.ActionBar;
import mx.smartteam.entities.Producto;
import mx.smartteam.exceptions.AnaquelException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Bodega;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class BodegaActivity extends Activity {

    private RadioGroup rad;
    private RadioButton rbSi, rbNo;
    private LinearLayout layaut;
    private Producto currentProducto;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private Context context;
    private EditText EditExistencia, EditTarima, EditComentarios;
    private Bodega bodegaB = null;
    private String existenciaAnt = "";
    private Bodega bodegaC = null;
    private ProductoCollection productoCollection = null;
    private EnumFormulario currentFormulario;
    private Integer spCategoria, spMarca;
    private String tipoform;
    Sucursal sucursal =new Sucursal();

    /**
     * ****************************************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);
        this.context = this;

        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        this.tipoform = (String) bundle.get("tipoform") == null ? null : (String) bundle.get("tipoform");
        ArrayList<mx.smartteam.entities.Producto> ProductosPorMarca = (ArrayList<mx.smartteam.entities.Producto>) getIntent().getSerializableExtra("productoCollection");
        productoCollection = new ProductoCollection();
        for (int i = 0; i < ProductosPorMarca.size(); i++) {
            productoCollection.add(ProductosPorMarca.get(i));
        }

        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

        try {
            bodegaB = mx.smartteam.business.Bodega.GetInfoByVisita(context, currentProyecto, currentUsuario, currentPop, currentProducto);
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (this.currentUsuario != null && this.currentProyecto != null && this.currentPop != null && this.currentProducto != null) {
            this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));

            this.EditExistencia = (EditText) findViewById(R.id.EditExistencia);
            this.EditTarima = (EditText) findViewById(R.id.EditTarima);
            this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);

            if (bodegaB != null) {
                if (bodegaB.Cantidad != 0) {
                    EditExistencia.setText(bodegaB.Cantidad + "");
                } else {
                    EditExistencia.setText("");
                }

                if (bodegaB.Tarima != 0) {
                    EditTarima.setText(bodegaB.Tarima + "");
                } else {
                    EditTarima.setText("");
                }

                if (bodegaB.Comentario != null || !bodegaB.Comentario.equals("null")) {
                    EditComentarios.setText(bodegaB.Comentario + "");
                } else {
                    EditComentarios.setText("");
                }

            } else {
                EditExistencia.setText("");
                EditTarima.setText("");
                EditComentarios.setText("");

            }

        }
    }

    /**
     * ***************************************************************************
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anquel_sondeo, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        if (mx.triplei.ProductoActivity.porProducto) {
            menu.removeItem(R.id.action_siguiente);
        } else {
            menu.removeItem(R.id.action_guardar);
        }
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
    private void foto() {
        mx.smartteam.entities.Foto objFoto = new mx.smartteam.entities.Foto();
        objFoto.Tipo = objFoto.Tipo.Bodega;
        Intent i;
        i = new Intent(this, CameraActivity.class);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("pop", currentPop);
        i.putExtra("producto", currentProducto);
        i.putExtra("opcionfoto",6);/* Bodega */
        i.putExtra("foto", objFoto);

        startActivity(i);
        super.onStop();
    }

    /**
     * ***************************************************************************
     */
    private void Guardar() {
        try {
            mx.smartteam.entities.Bodega objBodega = new Bodega();

            if (this.EditExistencia.getText().toString().isEmpty()) {
                throw new AnaquelException.CantidaEmpty();
            }

            objBodega.IdProyecto = currentProyecto.Id;
            objBodega.IdUsuario = currentUsuario.Id;
            objBodega.DeterminanteGSP = currentPop.DeterminanteGSP;
            objBodega.Sku = currentProducto.SKU;
            objBodega.Cantidad = Integer.parseInt(this.EditExistencia.getText().toString());
            objBodega.Tarima = this.EditTarima.getText().toString().isEmpty() ? null : Integer.parseInt(this.EditTarima.getText().toString());
            objBodega.Comentario = this.EditComentarios.getText().toString();
            objBodega.IdVisita = currentPop.IdVisita;

            if (bodegaB != null) {
                objBodega.IdFoto = bodegaB.IdFoto;
            }

            new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objBodega);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ***************************************************************************
     */
    public void esperarYCerrar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                ReDirect();
            }
        }, milisegundos);
    }

    public void ReDirect() {

        Intent intent = new Intent("mx.smartteam.producto");
        intent.putExtra("item", EnumFormulario.existencias_bodega);
        intent.putExtra("usuario", currentUsuario);
        intent.putExtra("proyecto", currentProyecto);
        intent.putExtra("pop", currentPop);
        startActivity(intent);
        finish();
    }

    /**
     * ***************************************************************************
     */
    private void OtroProducto() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+R.string.title_dialog+"</font>"));   
        //alertDialogBuilder.setTitle(R.string.title_dialog);
        Drawable d=sucursal.setIconAlert(context);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder.setMessage("¿Desea capturar otro producto?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent("mx.smartteam.producto");
                        intent.putExtra("item", EnumFormulario.existencias_bodega);
                        intent.putExtra("usuario", currentUsuario);
                        intent.putExtra("proyecto", currentProyecto);
                        intent.putExtra("pop", currentPop);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

        //                Intent i = new Intent("mx.smartteam.menu");
                        //                startActivity(i);
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * ***************************************************************************
     */
    public void goBack() {
        Intent ii;
        ii = new Intent(BodegaActivity.this, ProductoActivity.class);
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

    private void Siguiente() {

        Guardar();

        if (productoCollection.size() > 0) {
            if (productoCollection.size() == 1) {
                Guardar();

            } else {
                for (int i = 0; i < productoCollection.size(); i++) {

                    if (currentProducto.SKU.equals(productoCollection.get(i).SKU)) {

                        productoCollection.remove(i);
                        ThisElements();

                    }
                    if (productoCollection.size() > 0) {
                        currentProducto = productoCollection.get(0);
                    } else {
                        finish();
                    }
                }
            }
        } else {
            finish();
        }

    }

    /**
     * ***************************************************************************
     */
    public void ThisElements() {
        setContentView(R.layout.activity_bodega);
        this.context = this;
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");

        try {
            bodegaC = mx.smartteam.business.Bodega.GetInfoByVisita2(context, currentProyecto, currentUsuario, currentPop, productoCollection.get(0).Nombre);
        } catch (Exception ex) {
            ex.getMessage();
        }

        if (this.currentUsuario != null && this.currentProyecto != null
                && this.currentPop != null && this.currentProducto != null && productoCollection.size() > 0) {

            this.setTitle(Html.fromHtml("<small><strong>" + productoCollection.get(0).Nombre + "</strong></small>"));
            this.EditExistencia = (EditText) findViewById(R.id.EditExistencia);
            this.EditTarima = (EditText) findViewById(R.id.EditTarima);
            this.EditComentarios = (EditText) findViewById(R.id.EditComentarios);
        } else {
            //   ReDirect();   
        }

        if (bodegaC == null) {
            EditExistencia.setText("");
            EditTarima.setText("");
            EditComentarios.setText("");
        } else {
            if (bodegaC.Sku != 0 && bodegaC.Cantidad != null) {
                if (bodegaC.Cantidad != 0) {
                    EditExistencia.setText(bodegaC.Cantidad + "");
                } else {
                    EditExistencia.setText("");
                }

                if (bodegaC.Tarima != 0) {
                    EditTarima.setText(bodegaC.Tarima + "");
                } else {
                    EditTarima.setText("");
                }

                if (bodegaC.Comentario != null || !bodegaC.Comentario.equals("null")) {
                    EditComentarios.setText(bodegaC.Comentario + "");
                } else {
                    EditComentarios.setText("");
                }
            }
        }

        bodegaB = bodegaC;
    }

    /**
     * ****************************************************************************
     */
    class Save extends AsyncTask<Bodega, Void, String> {

        private ProgressDialog pd;
        private Bodega bodega;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle("Registrando informacion");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(Bodega... param) {

            String result = null;

            if (param.length > 0) {
                try {
                    int idfoto = PhotoActivity.idfoto;
                    if (idfoto > 0) {
                        param[0].IdFoto = idfoto;
                    }
                    if (bodegaB != null) {
                        param[0].Id = bodegaB.Id;

                        mx.smartteam.business.Bodega.Update(context, param[0]);

                    } else {
                        mx.smartteam.business.Bodega.Insert(context, param[0]);
                    }
                    PhotoActivity.idfoto = 0;
                    result = "OK";
                } catch (Exception ex) {
                    result = "-1";
                    Logger.getLogger(BodegaActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();
            if (resultado != null && resultado.equals("OK")) {
                Toast.makeText(context, "Informacion registrada correctamente", Toast.LENGTH_LONG).show();
                /*if (productoCollection.size() == 1) {
                    productoCollection.remove(0);
                }*/
                goBack();
                //  OtroProducto();
            } else {
                Toast.makeText(context, "Error al enviar la informacion", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * *******************************************************************************************************
     */
}
