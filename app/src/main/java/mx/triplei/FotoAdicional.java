package mx.triplei;


import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
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
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.Usuario;

public class FotoAdicional extends Activity {

    private GlobalSettings settings;
    private ProductoCollection productoCollection = null, productoCollectionB = null;
    private CategoriaCollection categoriaCollection = null;
    private MarcaCollection marcaCollection = null,marcaCollection2;
    private Categoria CurrenCategoria;
    private Marca CurrenMarca,CurrenMarca2;
    private Producto currentProducto;
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
    private String tipo = "",tipoform;
    private int option = 0;
    private Integer spCategoria, spMarca=0, gMarca=0;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public int opcionfoto;
    Sucursal sucursal =new Sucursal();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if (savedInstanceState == null) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_adicional);
        context = this;
        this.porProducto = true;
        //Recuperamos la informacion envianda en el intento
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");
        this.tipoform = (String) bundle.get("tipo");
        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        //this.currentProducto = (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        spCategoria = bundle.get("spCategoria") == null ? 0 : (Integer) bundle.get("spCategoria");
        spMarca = bundle.get("spMarca") == null ? 0 : (Integer) bundle.get("spMarca");
        spinCategoria = (Spinner) findViewById(R.id.spinCategoria);
        spinMarca = (Spinner) findViewById(R.id.spinMarca);
        
        new Marcas().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        new Categorias().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);  
        
        spinProducto = (Spinner) findViewById(R.id.spinProducto);
        TxtProducto = (TextView) findViewById(R.id.textProducto);
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.foto_anaquel, menu);
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
        Object selectItemMarcaconfig= spinProducto.getSelectedItem(); 
        switch (item.getItemId()) {
            
           case R.id.action_foto:
                    opcionfoto = 1;
                   
                CurrenCategoria = (mx.smartteam.entities.Categoria) selectItemCategoria;/* categorias*/
                CurrenMarca = (mx.smartteam.entities.Marca) selectItemMarca;
                CurrenMarca2 = (mx.smartteam.entities.Marca) selectItemMarcaconfig;
                Foto foto = new Foto();
                foto.IdCategoria = CurrenMarca2.Id;
                    i = new Intent(FotoAdicional.this, CameraActivity.class);
                    i.putExtra("usuario", currentUsuario);
                    i.putExtra("proyecto", currentProyecto);
                    i.putExtra("pop", currentPop);
                    i.putExtra("opcionfoto", 11); /* Foto Adicional*/                 
                    i.putExtra("categoria", CurrenCategoria);
                    i.putExtra("foto", foto);
                    i.putExtra("item", currentFormulario);
                    startActivity(i);  
                    finish();
                    return true;
            

            case R.id.action_cancelar:
                finish();
                return true;

            
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    

   

  

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.putInt("option", 1);
    }

   
    protected class OnItemSelectedListener_Categoria implements Spinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            try{

                Categoria.Adapter adapterCategoria = (Categoria.Adapter) adapterView.getAdapter();
                CurrenCategoria = adapterCategoria.getItem(position);
                if (CurrenCategoria != null) {
                  new Marcas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria);
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

                if(spMarca>0){
                    position = spMarca;
                    //spMarca=0;
                }else{
                    
                }
                CurrenMarca = adapterMarca.getItem(position);
                if (CurrenCategoria != null && CurrenMarca != null) {
                    new Productos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria, CurrenMarca);

                }
            } catch (Exception ex) {
                Logger.getLogger(ProductoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    protected class OnItemSelectedListener_Producto implements Spinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

            //Producto.Adapter adapterProducto = (Producto.Adapter) adapterView.getAdapter();
            //CurrenProducto = adapterProducto.getItem(position);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public class Categorias extends AsyncTask<Void, Void, CategoriaCollection> {

        @Override
        protected CategoriaCollection doInBackground(Void... params) {
            //CategoriaCollection categoriaCollection = null;

            try {
                if(currentFormulario!=null){tipo="cc.modulo='"+tipoform+"'";}
                    if(currentSondeo!=null){tipo="cc.idSondeo="+currentSondeo.Id;}
                //categoriaCollection = mx.smartteam.business.Categoria.GetCategoriaProductoByProyecto(context, currentProyecto);
                categoriaCollection = mx.smartteam.business.Categoria.GetCategoriaProductoByProyecto(context, currentProyecto,tipo);
            } catch (Exception ex) {
                Logger.getLogger(ProductoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return categoriaCollection;

        }

        protected void onPostExecute(CategoriaCollection categoriaCollection) {
            try{

                Spinner spinCategoria = (Spinner) findViewById(R.id.spinCategoria);

                Categoria.Adapter adapter = new Categoria.Adapter(context, android.R.layout.simple_spinner_item, categoriaCollection);
                spinCategoria.setAdapter(adapter);
                spinCategoria.setOnItemSelectedListener(new OnItemSelectedListener_Categoria());

                if (spCategoria > 0) {
                    spinCategoria.setSelection(spCategoria);
                }
            }catch(Exception e){
                e.getMessage().toString();
                
            }
        }
    }

    public class Marcas extends AsyncTask<Categoria, Void, MarcaCollection> {

        @Override
        protected MarcaCollection doInBackground(Categoria... categoria)
        {
            try{
                Spinner spinProducto = (Spinner) findViewById(R.id.spinProducto);
                marcaCollection = null;
                if (categoria.length == 0) {
                    String tipo="";
                    if(currentFormulario!=null){tipo="cc.modulo='"+tipoform+"'";}
                    if(currentSondeo!=null){tipo="idSondeo="+currentSondeo.Id;}
                    marcaCollection = mx.smartteam.data.Marca.GetMarcasFotos(context, currentProyecto);
                } else {
                    String tipo="";
                    if(currentFormulario!=null){tipo="mc.modulo='"+tipoform+"'";}
                    if(currentSondeo!=null){tipo="mc.idSondeo="+currentSondeo.Id;}
                    marcaCollection = mx.smartteam.data.Marca.GetMarcasFotos(context, currentProyecto);
                }
                if(marcaCollection.size()==0){  
                      new Productos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CurrenCategoria, CurrenMarca);
                }
            }catch(Exception e){
                e.getMessage().toString();
                
            }
                            return marcaCollection;

        }

        protected void onPostExecute(MarcaCollection marcaCollection) {
            try{
                // spinMarca = (Spinner) findViewById(R.id.spinMarca);

                spinMarca.setAdapter(new Marca.Adapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, marcaCollection));
                spinMarca.setOnItemSelectedListener(new OnItemSelectedListener_Marca());
                if(gMarca > 0){
                    if(marcaCollection.size()>0){
                        for(int i =0; i < marcaCollection.size(); i++){
                            int t = marcaCollection.get(i).Id;
                            if( t == gMarca){
                                spMarca = i;
                                break;
                            }
                        }
                    }
                }
                
                int x =0;
                x= spinMarca.getAdapter().getCount();
                if(x > spMarca ){

                    if (spMarca > 0) {
                        spinMarca.setSelection(spMarca);
                    }
                }
            }catch(Exception ex){
                ex.getMessage().toString();
            }
        }
    }

    public class Productos extends AsyncTask<Object, Void, MarcaCollection> {

        @Override
        protected MarcaCollection doInBackground(Object... params) {

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
                                        marcaCollection2 = mx.smartteam.data.Marca.GetMarcasXConfig(context, currentProyecto,mar.Id);

                  //  productoCollectionAux = mx.smartteam.business.Producto.GetByVisita(context, currentPop.IdVisita, currentPop.IdCadena, cat.Id, mar.Id, currentFormulario, currentSondeo == null ? 0 : currentSondeo.Id, currentUsuario.Id);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProductoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }

            return marcaCollection2;
        }

        @Override
        protected void onPostExecute(MarcaCollection resultado) {
            try{

                marcaCollection2 = resultado;
                Spinner spinProducto = (Spinner) findViewById(R.id.spinProducto);
                spinProducto.setAdapter(new Marca.Adapter(getBaseContext(),
                        android.R.layout.simple_spinner_dropdown_item, resultado));
                if(spMarca>0){
                    spMarca=0;
                }
            } catch (Exception ex) {
                Logger.getLogger(ProductoActivity.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    
  
    
    private  AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        super.setTitle( Html.fromHtml("<font color='#FFFFF'>"+title+"</font>"));   
        //downloadDialog.setTitle(title);
        Drawable d=sucursal.setIconAlert(context);
        downloadDialog.setIcon(d);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                mx.smartteam.entities.Producto EntityProducto = new mx.smartteam.entities.Producto();
                //Toast.makeText(context, format , Toast.LENGTH_LONG).show();
                EntityProducto.Barcode = contents;
                
                new ProductoGetOne().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, EntityProducto);

            }
        }
    }

    class ProductoGetOne extends AsyncTask<mx.smartteam.entities.Producto, Void, mx.smartteam.entities.Producto> {

        private ProgressDialog pd;
        private Exception exception = null;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle("Buscando Producto");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected mx.smartteam.entities.Producto doInBackground(mx.smartteam.entities.Producto... arg0) {
            mx.smartteam.entities.Producto EntityProducto = null;

            try {

                EntityProducto = mx.smartteam.business.Producto.GetOne(context, arg0[0]);
            } catch (Exception ex) {
                exception = ex;
            }
            return EntityProducto;

        }

        @Override
        protected void onPostExecute(mx.smartteam.entities.Producto EntityProducto) {
            pd.dismiss();
            try{
                if (EntityProducto != null) {
                    gMarca = 0; spCategoria=0;
                    /* posicionar la Categoria y Marca*/
                    Categoria cat = new Categoria();
                    cat.Id = EntityProducto.IdCategoria;
                    gMarca = EntityProducto.IdMarca;
                    for(int i =0; i < categoriaCollection.size(); i++){
                        if(categoriaCollection.get(i) != null){
                            int x = categoriaCollection.get(i).Id;
                            if( x == cat.Id){
                                spCategoria = i;
                                break; // detenemos el ciclo y continuamos con la ejecución.
                            }
                        }
                    }
                    spinCategoria.setSelection(spCategoria);
                    new Marcas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cat);
                     
                } else {
                    Toast.makeText(context, "El producto no se encuentra, favor de validar", Toast.LENGTH_LONG).show();
                }
            }catch(Exception ex){
            ex.getMessage().toString();
            }
        }
    }
    
    
    
    
    
    
    
  
}
