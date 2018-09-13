package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Exhibicion;
import mx.smartteam.entities.ExhibicionCollection;
import mx.smartteam.entities.ExhibicionConfig;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class ExhibicionActivity extends Activity {
    
    private Spinner spinCategoria, spinMarca, spinExh;
    private TextView txtexh;
    private Context context;
    private String titul = "Exh. Adicional";
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private Pop currentpop;
    private CategoriaCollection categoriaCollection = null;
    private Categoria CurrenCategoria;
    private MarcaCollection marcaCollection = null;
    private EnumFormulario currentFormulario;
    private String tipo = "",tipoform;
    private Marca CurrenMarca;
    private ExhibicionCollection eCollection  = null;
    private ExhibicionConfig exhibicionConfig = null;
    private int positionCat = 0, positionMarca = 0, positionExh = 0, bandera = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_producto);
            
            context = this;
            spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
            spinMarca = (Spinner) findViewById(R.id.spinMarca);
            spinExh = (Spinner) findViewById(R.id.spinProducto);
            txtexh = (TextView) findViewById(R.id.textProducto);
            setTitle(titul);
            
            Bundle bundle = getIntent().getExtras();
            this.currentFormulario = (EnumFormulario) bundle.get("item") == null ? null : (EnumFormulario) bundle.get("item"); ;
            currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
            currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
            currentpop =  (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
            this.tipoform = (String) bundle.get("tipo");
            positionCat = (Integer) bundle.getInt("positionCat") == null ? 0 : (Integer) bundle.getInt("positionCat");
            positionMarca = (Integer) bundle.getInt("positionMarca") == null ? 0 : (Integer) bundle.getInt("positionMarca");
            positionExh = (Integer) bundle.getInt("positionExh") == null ? 0 : (Integer) bundle.getInt("positionExh") ;
            
            exhibicionConfig = new mx.smartteam.entities.ExhibicionConfig();
            
            if(currentProyecto != null && currentUsuario != null && currentpop != null){
                new Categorias().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                new Marcas().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                Toast.makeText(context, "Validar con el ejecutivo de cuenta", Toast.LENGTH_LONG).show();
            }
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
    public boolean onOptionsItemSelected(MenuItem Item){
        try{
            switch(Item.getItemId()){
                case R.id.action_cancelar:
                    finish();
                return true;
                case R.id.action_continuar:  
                    GotoNextActivity();
                    finish();
                return true;
                    
                default:
            }
        }catch(Exception ex ){
            ex.getMessage();
        }
          return true;
    }

    public void GotoNextActivity(){
    
        Intent i = new Intent(ExhibicionActivity.this, SodActivity.class);
        getPositionFilters();
        i.putExtra("proyecto", currentProyecto);
        i.putExtra("usuario", currentUsuario);
        i.putExtra("pop", currentpop);
        i.putExtra("tipo", tipoform);
        i.putExtra("exhibicionConfig", exhibicionConfig);
        i.putExtra("positionCat", positionCat);
        i.putExtra("positionMarca",positionMarca);
        i.putExtra("positionExh", positionExh);
        i.putExtra("item", currentFormulario);
        i.putExtra("bandera", bandera);
        startActivity(i);
        
    }
    
    public void getPositionFilters(){
        positionCat=0; positionMarca = 0; positionExh =0;
        Object selectItem3 = spinExh.getSelectedItem();
        if(selectItem3 != null){
            positionExh = spinExh.getSelectedItemPosition();
            Exhibicion ex = null;
            ex = (mx.smartteam.entities.Exhibicion) selectItem3;
            exhibicionConfig.Id = ex.Id;
            bandera = ex.Bandera;
        }
        
        positionCat = spinCategoria.getSelectedItemPosition();
        positionMarca = spinMarca.getSelectedItemPosition();
        
    }
    
    
    protected class OnItemSelectedListener_Categoria implements Spinner.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            try{
                Categoria.Adapter adapterCategoria = (Categoria.Adapter) adapterView.getAdapter();
                CurrenCategoria = adapterCategoria.getItem(position);
                if (CurrenCategoria != null) {
                    new ExhibicionActivity.Marcas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria);
                }
            }catch(Exception e){
                e.getMessage().toString();    
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    protected class OnItemSelectedListener_Marca implements Spinner.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            try{
                Marca.Adapter adapterMarca = (Marca.Adapter) adapterView.getAdapter();
                CurrenMarca = adapterMarca.getItem(position);
                
                if(CurrenCategoria != null && CurrenMarca != null){
                    new Exhibiciones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria, CurrenMarca);
                }
            }catch(Exception ex){
                Logger.getLogger(ExhibicionActivity.class.getName()).log(Level.SEVERE,null,ex);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    
    public class Categorias extends AsyncTask<Void, Void, CategoriaCollection>{
        @Override
        protected CategoriaCollection doInBackground(Void... arg0) {
            try{
                String tipo = " cc.modulo='sod' ";
                categoriaCollection = mx.smartteam.business.Categoria.GetCategoriaProductoByProyecto(context, currentProyecto, tipo);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return categoriaCollection;
        }
    
        protected void onPostExecute(CategoriaCollection categoriaCollection){
            try{
                
                Categoria.Adapter adapter = new Categoria.Adapter(context, android.R.layout.simple_spinner_item, categoriaCollection);
                spinCategoria.setAdapter(adapter);
                spinCategoria.setOnItemSelectedListener(new OnItemSelectedListener_Categoria());
                
                if(positionCat > 0 ){
                    spinCategoria.setSelection(positionCat);
                    positionCat =0;
                }
                
            }catch(Exception ex){
                ex.printStackTrace();
            }    
        }
    }
    
    public class Marcas extends AsyncTask<Categoria, Void, MarcaCollection> {
        @Override
        protected MarcaCollection doInBackground(Categoria... categoria)
        {
            try{
                marcaCollection = null;
                String tipo="";
                if (categoria.length == 0) {
                    if(currentFormulario!=null){
                        tipo="mc.modulo='"+tipoform+"'";
                    }
                    marcaCollection = mx.smartteam.data.Marca.GetByProyecto(context, currentProyecto,tipo);
                } else {
                    if(currentFormulario!=null){
                        tipo=" mc.modulo = '"+tipoform+"'";
                    }
                    marcaCollection = mx.smartteam.data.Marca.GetByProyectoCategoria(context, currentProyecto, categoria[0],tipo);
                }
            }catch(Exception e){
                e.getMessage().toString();
            }
            return marcaCollection;
        }

         protected void onPostExecute(MarcaCollection marcaCollection) {
            try{
                spinMarca.setAdapter(new Marca.Adapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, marcaCollection));
                spinMarca.setOnItemSelectedListener(new OnItemSelectedListener_Marca());
                
                if(positionMarca > 0){
                    spinMarca.setSelection(positionMarca);
                    positionMarca =0;
                }
                
            }catch(Exception ex){
                ex.getMessage().toString();
            }
        }
   }
    
    public class Exhibiciones extends AsyncTask<Object, Void, mx.smartteam.entities.ExhibicionCollection>{
        @Override
        protected ExhibicionCollection doInBackground(Object... params) {
            ExhibicionCollection exhibicionCollection = new ExhibicionCollection();
            try{
                Thread.sleep(800);
                Categoria cat = (Categoria) params[0];
                Marca mar = (Marca) params[1];
                if(cat.Id == null){ cat.Id =0; }
                if(mar.Id == null){ mar.Id =0; }
                mx.smartteam.entities.ExhibicionCollection eCollection = mx.smartteam.business.Exhibicion.GetAll(context, currentpop.IdVisita, cat.Id , mar.Id);
                return eCollection;
            }catch(Exception ex){
                Logger.getLogger(ExhibicionActivity.class.getName()).log(Level.SEVERE, null, ex) ;
            }
            return exhibicionCollection;
        }
        
        @Override
        protected void onPostExecute(ExhibicionCollection resultado){
            eCollection = resultado;
            spinExh.setAdapter(new Exhibicion.Adapter(getBaseContext(),android.R.layout.simple_spinner_dropdown_item, resultado));
            if(positionExh > 0){
                spinExh.setSelection(positionExh);
                positionExh = 0;
            }
            
        }
    }
    
}
