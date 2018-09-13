package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Pregunta;
import mx.smartteam.entities.PreguntaCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.Usuario;

public class ByCategorias extends Activity {
    private LinearLayout LayoutSondeo = null;
    private CategoriaCollection categoriaCollection = null;
    private Categoria CurrentCategoria = null;
    private SondeoModulos currentSondeo;
    private EnumFormulario currentFormulario;
    private Context context;
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private Pop currentPop;
    static boolean bandera = true;
    static Boolean flag = false;
    private Integer spCategoria;
    private ProductoCollection productoCollection = null;
    private TextView TxtProducto, TxtMarca,TxtCategoria;
    private Spinner spinProducto, spinMarca, spinCategoria;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_producto);
        setContentView(R.layout.activity_sondeo);
        context = this;
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        
        CreaLayout() ;
        /*TxtProducto = (TextView) findViewById(R.id.textCategoria); TxtProducto.setVisibility(View.GONE);
        TxtMarca = (TextView) findViewById(R.id.textMarca); TxtMarca.setVisibility(View.GONE);
        spinProducto = (Spinner) findViewById(R.id.spinProducto); spinProducto.setVisibility(View.GONE);
        spinMarca = (Spinner) findViewById(R.id.spinMarca); spinMarca.setVisibility(View.GONE);
        
          */
    }
    
    
    /* public class Pregunta extends AsyncTask<Object, Void, Pregunta> {

        @Override
        protected PRegunta doInBackground(Object... params) {

            // TODO Auto-generated method stub
            ProductoCollection productoCollectionAux = null;
            productoCollectionAux = new ProductoCollection();

            try {
                Thread.sleep(1000);
                if (params.length == 0) {
                    productoCollectionAux = mx.smartteam.business.Producto.GetByVisita(context, currentPop.IdVisita, currentPop.IdCadena, 0, 0, currentFormulario, currentSondeo == null ? 0 : currentSondeo.Id, currentUsuario.Id);
                    //productoCollectionAux = mx.smartteam.data.Producto.GetByProyecto(context, currentProyecto, currentPop, currentUsuario);
                } else {
                    Categoria cat = (Categoria) params[0];
                    Marca mar = (Marca) params[1];
                    productoCollectionAux = mx.smartteam.business.Producto.GetByVisita(context, currentPop.IdVisita, currentPop.IdCadena, cat.Id, mar.Id, currentFormulario, currentSondeo == null ? 0 : currentSondeo.Id, currentUsuario.Id);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProductoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }

            return productoCollectionAux;
        }

        @Override
        protected void onPostExecute(OpcionCollection resultado) {

           /* productoCollection = resultado;
            Spinner spinProducto = (Spinner) findViewById(R.id.spinProducto);
            spinProducto.setAdapter(new Producto.Adapter(getBaseContext(),
                    android.R.layout.simple_spinner_dropdown_item, resultado));
            
        }
    }
    */
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.producto_1, menu);
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
                Intent i = new Intent(context, SettingActivity.class);
                i.putExtra("usuario", currentUsuario);
                i.putExtra("proyecto", currentProyecto);
                startActivity(i);
                return true;*/

            case R.id.action_cancelar:
                finish();
                return true;

            case R.id.action_continuar:
                PreguntaCollection pCollection = new PreguntaCollection();
                Pregunta pregunta = new Pregunta();
                pCollection = Guardar();
                Intent  intent = new Intent(context , SondeoActivity.class);
                pregunta = pCollection.get(0);
                if(pregunta != null){
                    intent.putExtra("npCollection", pCollection);
                   // intent.putExtra("productoCollection", productoCollection);
                }
                
                intent.putExtra("usuario", currentUsuario);
                intent.putExtra("proyecto", currentProyecto);
                intent.putExtra("pop", currentPop);
                intent.putExtra("sondeo", currentSondeo);
                

                startActivity(intent);
                finish();  

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
     private void CreaLayout() {
        try {
            this.LayoutSondeo = null;
            this.LayoutSondeo = (LinearLayout) findViewById(R.id.LayoutSondeo);

            mx.smartteam.entities.PreguntaCollection preguntaCollection = null;
            
            preguntaCollection = mx.smartteam.business.Pregunta.GetPreguntaDinamica(context, currentSondeo, currentPop);//data.Pregunta.GetPreguntaBySondeo(context, currentSondeo);

            for (Pregunta pregunta : preguntaCollection) {
                Pregunta.Type tipo = mx.smartteam.entities.Pregunta.Type.valueOf(pregunta.Tipo.toLowerCase());
                
                switch (tipo)
                {
                    case combo:
                        this.LayoutSondeo.addView(PreguntaComboSelect(pregunta));   // PreguntaComboSelect
                        break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SondeoActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
     private View PreguntaComboSelect(final Pregunta pregunta) {
        OpcionCollection items = new OpcionCollection(); 
        items = pregunta.Opciones;
        ProductoCollection pp = new ProductoCollection();
        
        
        LinearLayout layout = new LinearLayout(this);
        layout.setTag(pregunta);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setId(pregunta.Id);
        
        TextView text = new TextView(this);
        text.setText(pregunta.Nombre);
        int position=0;
        for(int i = 0; i< items.size(); i++)
        {
            Producto p = new Producto();
            if(items.get(i).Selected.equals(true)){
                position = i;
            }
            
            p.Id = items.get(i).Id;
            p.Nombre = items.get(i).Nombre;
            pp.add(p);
        }
        
        productoCollection = pp;
        
        final Spinner spinnerx = new Spinner(this);
        //Categoria.Adapter adapter = new Categoria.Adapter(context, android.R.layout.simple_spinner_item, categoriaCollection);
          //  spinCategoria.setAdapter(adapter);
        //Opcion.Adapter_Combo adapterx = new Opcion.Adapter_Combo(context, android.R.layout.simple_spinner_item, pregunta.Opciones);
        //spinnerx.setAdapter(adapterx);
        spinnerx.setAdapter(new Producto.Adapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, pp));
        
        
        spinnerx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
            Object itex = adapter.getItemAtPosition(i);
            Producto pro = new Producto();
            pro = (mx.smartteam.entities.Producto) itex;
            // pregunta.Opciones.get(i).Selected = true;
             // ((TextView) adapter.getChildAt(0)).setTextColor(Color.BLUE);
            
            pregunta.Opciones = ValidarAllItem(pregunta.Opciones, pro.Id);
            //Toast.makeText(getApplicationContext(), "Id Item Selected" + pro.Id   , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView arg0) {
            Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        }
        }); 
        
        if(position > 0){
            spinnerx.setSelection(position);
        }
        
        layout.addView(text);
        layout.addView(spinnerx);

        return layout;
    }
    
    public OpcionCollection ValidarAllItem(OpcionCollection opcionCollection, int Id){
    //Procesamos info
        if (opcionCollection.size() > 0) {
            for(int i =0 ;i < opcionCollection.size(); i++ )
            {
                if(opcionCollection.get(i).Id.equals(Id) )
                {
                    opcionCollection.get(i).Selected = true;
                }
                else{
                opcionCollection.get(i).Selected = false;
                }
            
            }    
        }
        
        return opcionCollection;
    }
    
    
    private PreguntaCollection Guardar() {
        mx.smartteam.entities.PreguntaCollection preguntaCollection = null;        
        preguntaCollection = new PreguntaCollection();
        for (int i = 0; i < this.LayoutSondeo.getChildCount(); i++){
            LinearLayout view = (LinearLayout) this.LayoutSondeo.getChildAt(i);
            if (view != null) {
                Pregunta objPregunta = (Pregunta) view.getTag();
                if (objPregunta != null) {
                    if (!objPregunta.Tipo.equals("informativo")) {
                        if (objPregunta != null) {
                            preguntaCollection.add(objPregunta);
                        }
                    }
                }
            }
        }
        if(preguntaCollection.size() > 0){
        //new SondeoActivity.Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, preguntaCollection);
        }
        return preguntaCollection;
        
    }
     
}
