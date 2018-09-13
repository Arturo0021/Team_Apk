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
import mx.smartteam.entities.EnumFormulario;
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

public class PDinamicaCategoriaActivity extends Activity {

    private ProductoCollection productoCollection = null;
    private Producto currentProducto;
    private EnumFormulario currentFormulario;
    private Context context;
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private Pop currentPop;
    private SondeoModulos currentSondeo;
    private Spinner spinProducto, spinCategoria, spinMarca;
    private TextView TxtProducto, textCategoria, textMarca;
    static boolean porProducto = true, config = false;
    private int modulo = 0;
    private String tipo = "";
    private int option = 0;
    private Pregunta currentPregunta, currentPregunta2;
    private Opcion currenOpcion, currenOpcion2; 
    private Integer spCategoria, spMarca;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if (savedInstanceState == null) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        context = this;
        this.porProducto = true;
        //Recuperamos la informacion envianda en el intento
        textMarca = (TextView) findViewById(R.id.textMarca);
        textCategoria = (TextView) findViewById(R.id.textCategoria);
        
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        //this.currentProducto = (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
        if(currentSondeo.Id==420){
            textCategoria.setText("CATEGORIA/PACK");
       }
        spinMarca = (Spinner) findViewById(R.id.spinMarca);

        new Categorias().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        
        spinProducto = (Spinner) findViewById(R.id.spinProducto); spinProducto.setVisibility(View.GONE);
        TxtProducto = (TextView) findViewById(R.id.textProducto); TxtProducto.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
          /*  case R.id.action_config:
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
                
                if (spinCategoria != null)
                {
                    Object selectItem = spinCategoria.getSelectedItem();

                    if(selectItem != null)
                    {
                        if(spinMarca != null)
                        {
                            Object selectItem2 = spinMarca.getSelectedItem();
                        
                            if(selectItem2 != null)
                            {
                                currenOpcion = (mx.smartteam.entities.Opcion) selectItem;
                                currenOpcion2 = (mx.smartteam.entities.Opcion) selectItem2;
                                Intent intent = new Intent();
                                intent.putExtra("opcion", currenOpcion);
                                intent.putExtra("pregunta", currentPregunta);
                                
                                intent.putExtra("opcion2", currenOpcion2);
                                if(currenOpcion2.Status == 0 ){
                                    currentPregunta2.ContestacionPreguntaId = 0;
                                }
                                intent.putExtra("pregunta2", currentPregunta2);
                                
                                
                                
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
                        }
                    }
                } else 
                {
                    Toast.makeText(getBaseContext(), "Seleccione un producto por favor", Toast.LENGTH_LONG).show();
                }
             
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

      
    protected class OnItemSelectedListener_Categoria implements Spinner.OnItemSelectedListener 
    {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) 
        {
            Opcion.AdapterNothing adapterCategoria = (Opcion.AdapterNothing) adapterView.getAdapter();       
            if(adapterCategoria!= null)
            {
                Object selectItem = spinCategoria.getSelectedItem();      
                Opcion nopcion = new Opcion();
                nopcion = (Opcion)selectItem;
                if(nopcion != null)
                {
                    new NPregunta().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, nopcion );
                }    
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) 
        {
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

        protected void onPostExecute(Pregunta Pregunta)
        {
            currentPregunta = null;
            if(Pregunta != null)
            {
               currentPregunta = Pregunta;
               textMarca.setText(Pregunta.Nombre);
            }
            
            Opcion.AdapterNothing adapter = new Opcion.AdapterNothing(context, android.R.layout.simple_spinner_item, Pregunta.Opciones);
            
            spinCategoria.setAdapter(adapter);
            spinCategoria.setOnItemSelectedListener(new OnItemSelectedListener_Categoria());
            
        }
    }
     
     
    public class NPregunta extends AsyncTask<Object, Void, Pregunta>
    {
        @Override
        protected Pregunta doInBackground(Object... params) 
        {
            PreguntaCollection preguntaCollection = null;
            Pregunta pregunta = new Pregunta();
            try 
            {
                Opcion objOpcion = (Opcion) params[0];
                Thread.sleep(100);
                preguntaCollection = mx.smartteam.business.Pregunta.GetNPreguntaDinamica(context, currentSondeo, currentPop, objOpcion, currentPregunta);
                pregunta = preguntaCollection.get(0);
            } catch (Exception ex) 
            {
                Logger.getLogger(PreguntaDinamicaActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pregunta;

        }

        protected void onPostExecute(Pregunta Pregunta)
        {
            currentPregunta2 = null;
            if(Pregunta != null)
            {
               currentPregunta2 = Pregunta;
               textMarca.setText(Pregunta.Nombre);
            }
            Opcion.Adapter_Combo adapter = new Opcion.Adapter_Combo(context, android.R.layout.simple_spinner_item, currentPregunta2.Opciones);
            spinMarca.setAdapter(adapter);
            
        }
    }

}
