package mx.triplei;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Pregunta;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.Usuario;

public class PreguntaDinamicaActivity extends Activity {

    private GlobalSettings settings;
    private ProductoCollection productoCollection = null, productoCollectionB = null;
    private CategoriaCollection categoriaCollection = null;
    private MarcaCollection marcaCollection = null;
    private Categoria CurrenCategoria;
    private Marca CurrenMarca;
    private Producto currentProducto;
    private Pregunta currentPregunta;
    private Opcion currenOpcion; 
    private EnumFormulario currentFormulario;
    private Context context;
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private Pop currentPop;
    private SondeoModulos currentSondeo;
    private Spinner spinProducto, spinCategoria, spinMarca;
    private TextView TxtProducto;
    static boolean porProducto = true, config = false;
    private int modulo = 0;
    private String tipo = "";
    private int option = 0;
    private Integer spCategoria, spMarca;
    private TextView TxtMarca,TxtCategoria;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if (savedInstanceState == null) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        context = this;
        this.porProducto = true;
        //Recuperamos la informacion envianda en el intento
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        //this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.Sondeo) getIntent().getSerializableExtra("sondeo");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        
        spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
        TxtCategoria = (TextView) findViewById(R.id.textCategoria);
        spinMarca = (Spinner) findViewById(R.id.spinMarca);spinMarca.setVisibility(View.GONE);
        TxtMarca = (TextView) findViewById(R.id.textMarca); TxtMarca.setVisibility(View.GONE);
        spinProducto = (Spinner) findViewById(R.id.spinProducto); spinProducto.setVisibility(View.GONE);
        TxtProducto = (TextView) findViewById(R.id.textProducto);TxtProducto.setVisibility(View.GONE);
        
        new Categorias().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.producto, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
           /* case R.id.action_config:
                config = true;
                Intent i = new Intent(context, SettingActivity.class);
                i.putExtra("usuario", currentUsuario);
                i.putExtra("proyecto", currentProyecto);
                startActivity(i);
                return true;*/

            case R.id.action_cancelar:
                finish();
                return true;

            case R.id.action_continuar:

                Spinner spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
                
                if (spinCategoria != null) {
                    Object selectItem = spinCategoria.getSelectedItem();

                    if (selectItem != null) {
                        currenOpcion = (mx.smartteam.entities.Opcion) selectItem;
                        
                        Intent intent = new Intent();
                        intent.putExtra("opcion", currenOpcion);
                        intent.putExtra("pregunta", currentPregunta);
                        intent.putExtra("usuario", currentUsuario);
                        intent.putExtra("proyecto", currentProyecto);
                        intent.putExtra("pop", currentPop);
                        intent.putExtra("sondeo", currentSondeo);
                        intent.putExtra("item", currentFormulario);

                        intent.setAction("mx.triplei.sondeo");
                        startActivity(intent);
                        finish();
                        break;
                        }

                    

                } else {
                    Toast.makeText(getBaseContext(), "Seleccione un producto por favor", Toast.LENGTH_LONG).show();
                }
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
    

    protected class OnItemSelectedListener_Categoria implements Spinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            Categoria.Adapter adapterCategoria = (Categoria.Adapter) adapterView.getAdapter();
            CurrenCategoria = adapterCategoria.getItem(position);
            if (CurrenCategoria != null) {
              //  new Marcas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria);

                //marcaCollectionCategoria = marcaCollection;
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public class Categorias extends AsyncTask<Void, Void, Pregunta> {

        @Override
        protected Pregunta doInBackground(Void... params) {
            PreguntaCollection preguntaCollection = null;
            Pregunta pregunta = new Pregunta();
            
            try {
                preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaDinamica(context, currentSondeo, currentPop);
                pregunta = preguntaCollection.get(0);
            } catch (Exception ex) {
                Logger.getLogger(PreguntaDinamicaActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pregunta;

        }

        protected void onPostExecute(Pregunta Pregunta) {
            currentPregunta = null;
            Spinner spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
            if(Pregunta != null){
               currentPregunta = Pregunta;
            TxtCategoria.setText(Pregunta.Nombre);
            }
            
            //Categoria.Adapter adapter = new Categoria.Adapter(context, android.R.layout.simple_spinner_item, preguntaCollection);
            Opcion.Adapter_Combo adapter = new Opcion.Adapter_Combo(context, android.R.layout.simple_spinner_item, Pregunta.Opciones);
            
            spinCategoria.setAdapter(adapter);
            //spinCategoria.setOnItemSelectedListener(new OnItemSelectedListener_Categoria());

            
        }
    }


}
