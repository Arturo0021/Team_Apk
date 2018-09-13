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
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Producto;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.Categoria;

public class ByMarca extends Activity {

    private CategoriaCollection categoriaCollection = null;
    private MarcaCollection marcaCollection = null;
    private Categoria CurrenCategoria;
    private Marca CurrenMarca;
    private EnumFormulario currentFormulario;
    private Context context;
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private Pop currentPop;
    private Spinner spinProducto, spinCategoria, spinMarca;
    private TextView TxtProducto;
    static boolean bandera = true;
    static Boolean flag = false;
    private Integer spCategoria, spMarca;
    private String tipo = "";
    public int opcionfoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        context = this;
        
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = bundle.get("item") == null ? null : (EnumFormulario) bundle.get("item");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");

        if (this.currentFormulario != null && this.currentPop != null && this.currentProyecto != null && this.currentUsuario != null) {
            new ByMarca.Categorias().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new ByMarca.Marcas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        spinProducto = (Spinner) findViewById(R.id.spinProducto);
        spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
        spinMarca = (Spinner) findViewById(R.id.spinMarca);
        TxtProducto = (TextView) findViewById(R.id.textProducto);
        TxtProducto.setVisibility(View.GONE);
        spinProducto.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(currentFormulario.toString()=="foto_anaquel"){
            getMenuInflater().inflate(R.menu.foto_anaquel, menu);
        }else{
           getMenuInflater().inflate(R.menu.producto_1, menu); 
        }
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        Object selectItemCategoria = spinCategoria.getSelectedItem();
        Object selectItemMarca = spinMarca.getSelectedItem(); 

        switch (item.getItemId()) {
            case R.id.action_cancelar:
                finish();
            return true;
                
            case R.id.action_foto:
                CurrenCategoria = (mx.smartteam.entities.Categoria) selectItemCategoria;
                CurrenMarca = (mx.smartteam.entities.Marca) selectItemMarca;
                i = new Intent(ByMarca.this, CameraActivity.class);
                i.putExtra("usuario", currentUsuario);
                i.putExtra("proyecto", currentProyecto);
                i.putExtra("pop", currentPop);
                i.putExtra("item", currentFormulario);
                i.putExtra("opcionfoto",10);/* Foto Anaquel */                   
                i.putExtra("categoria", CurrenCategoria);
                i.putExtra("marca", CurrenMarca);
                startActivity(i);  
                finish();
            return true;
                
            case R.id.action_continuar:
                CurrenCategoria = (mx.smartteam.entities.Categoria) selectItemCategoria;
                CurrenMarca = (mx.smartteam.entities.Marca) selectItemMarca;
                spCategoria = spinCategoria.getSelectedItemPosition();
                spMarca = spinMarca.getSelectedItemPosition();

                Intent intent = new Intent(ByMarca.this, Sos.class);
                intent.putExtra("usuario", currentUsuario);
                intent.putExtra("proyecto", currentProyecto);
                intent.putExtra("pop", currentPop);
                intent.putExtra("item", currentFormulario);
                intent.putExtra("marca", CurrenMarca);
                intent.putExtra("categoria", CurrenCategoria);
                intent.putExtra("spCategoria", spCategoria);
                intent.putExtra("spMarca", spMarca);
                startActivity(intent);
                finish();                
            return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    protected class OnItemSelectedListener_Categoria implements Spinner.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Categoria.Adapter adapterCategoria = (Categoria.Adapter) adapterView.getAdapter();
            CurrenCategoria = adapterCategoria.getItem(position);
            if (CurrenCategoria != null) {
                new ByMarca.Marcas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    protected class OnItemSelectedListener_Marca implements Spinner.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Marca.Adapter adapterMarca = (Marca.Adapter) adapterView.getAdapter();
            CurrenMarca = adapterMarca.getItem(position);
            if (CurrenCategoria != null && CurrenMarca != null) {
                //  new ByMarca.Productos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria, CurrenMarca);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    protected class OnItemSelectedListener_Producto implements Spinner.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Producto.Adapter adapterProducto = (Producto.Adapter) adapterView.getAdapter();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public class Categorias extends AsyncTask<Void, Void, CategoriaCollection> {
        @Override
        protected CategoriaCollection doInBackground(Void... params) {
            CategoriaCollection categoriaCollection = null;
            try {
                tipo="cc.modulo='"+currentFormulario.toString()+"'";
                categoriaCollection = mx.smartteam.business.Categoria.GetCategoriaProductoByProyecto(context, currentProyecto,tipo);
            } catch (Exception ex) {
                Logger.getLogger(ProductoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return categoriaCollection;
        }

        protected void onPostExecute(CategoriaCollection categoriaCollection) {
            Spinner spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
            Categoria.Adapter adapter = new Categoria.Adapter(context, android.R.layout.simple_spinner_item, categoriaCollection);
            spinCategoria.setAdapter(adapter);
            spinCategoria.setOnItemSelectedListener(new ByMarca.OnItemSelectedListener_Categoria());
            if (spCategoria > 0) {
                spinCategoria.setSelection(spCategoria);
            }
        }
    }

    public class Marcas extends AsyncTask<Categoria, Void, MarcaCollection> {
        @Override
        protected MarcaCollection doInBackground(Categoria... categoria) {
            MarcaCollection marcaCollection = null;
            try {
                String tipo="";
                Thread.sleep(1000);
                if (categoria.length == 0) {
                    tipo=currentFormulario.toString();
                    marcaCollection = mx.smartteam.data.Marca.GetByProyectoCategoriaSos(context, currentProyecto,null,currentPop,tipo);
                } else {
                    tipo=currentFormulario.toString();
                    marcaCollection = mx.smartteam.data.Marca.GetByProyectoCategoriaSos(context, currentProyecto, CurrenCategoria, currentPop,tipo);
                }
            } catch (Exception ex) {}
            return marcaCollection;
        }

        protected void onPostExecute(MarcaCollection marcaCollection) {
            spinMarca.setAdapter(new Marca.Adapter(getBaseContext(),android.R.layout.simple_spinner_dropdown_item, marcaCollection));
            int x = 0;
            x = spinMarca.getAdapter().getCount();
            if( x > spMarca  ){
                if (spMarca > 0) {
                    spinMarca.setSelection(spMarca);
                }
            }
        }
    }
}
