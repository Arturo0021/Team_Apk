package mx.triplei;

import android.app.ActionBar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.settings.Ajustes;
import mx.smartteam.settings.ServiceClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import mx.triplei.R;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class ClasificacionProducto extends Activity {

    private GlobalSettings appConfiguration;
    private ProductoCollection productoCollection = new ProductoCollection();
    private CategoriaCollection categoriaCollection = new CategoriaCollection();
    private MarcaCollection marcaCollection = new MarcaCollection();
    private Categoria CurrenCategoria;
    private Marca CurrenMarca;
    private Producto CurrenProducto;
    private EnumFormulario meFormulario;
    private Proyecto currentProyecto;
    private Usuario currentUsuario;
    private Pop currentPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion_producto);
        Bundle bundle = getIntent().getExtras();

        this.meFormulario = (EnumFormulario) bundle.get("item");

        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");


        appConfiguration = ((GlobalSettings) getApplicationContext());
        ObtieneClasificacioProducto();


        /*actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
         | ActionBar.DISPLAY_SHOW_HOME);
         */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_clasificacion_producto, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(this);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.actionbar_producto, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // respond to menu item selection

        switch (item.getItemId()) {
            case R.id.action_continuar:
                if (this.CurrenProducto != null && this.CurrenProducto.Nombre != "Todo") {

                    appConfiguration.CurrenProducto = this.CurrenProducto;
                    appConfiguration.ProductoCollection = this.productoCollection;
                    appConfiguration.CategoriaCollection = this.categoriaCollection;
                    appConfiguration.MarcaCollection = this.marcaCollection;

                    switch (this.meFormulario) {
                        case existencias_anaquel:
                            Intent i = new Intent("mx.smartteam.anaquel");
                            startActivity(i);
                            finish();
                            break;

                        case existencias_frentes_en_frio:
                            Intent iif = new Intent("mx.smartteam.frentes");
                            startActivity(iif);
                            finish();
                            break;

                        case existencias_bodega:
                            Intent ii = new Intent("mx.smartteam.bodega");
                            startActivity(ii);
                            finish();
                            break;

                        case material_promocional:
                            Intent iii = new Intent("mx.smartteam.materialpromocional");
                            startActivity(iii);
                            finish();
                            break;

                        case exhibiciones_adicionales:
                            Intent iv = new Intent("mx.smartteam.exhibicionadicional");
                            startActivity(iv);
                            finish();
                            break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Seleccione un producto por favor", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_cancelar:
                finish();
                break;
        }




        return true;
    }

    private void ObtieneClasificacioProducto() {
        String params = String.format(
                "?idUsuario=%s&idProyecto=%s&idVersion=0",
                this.appConfiguration.EntityUsuario.Id,
                this.appConfiguration.EntityProyecto.Id);
        String respons = ServiceClient
                .GetStringHttp(Ajustes.Url_Update_productos + params);

        if (respons != null) {
            new Categorias().execute(respons);
            new Marcas().execute(respons);
            new Productos().execute(respons);

            // Obtenmos los productos

        }
    }

    public class Categorias extends AsyncTask<String, Void, CategoriaCollection> {

        @Override
        protected CategoriaCollection doInBackground(String... params) {
            // TODO Auto-generated method stub

            // Declarcion de variables
            CategoriaCollection categoriaCollectionAux = new CategoriaCollection();
            Integer i = 0, count = 2;
            int indexCategoria = params[0].indexOf("CATEGORIAS");
            int indexMarca = params[0].indexOf("MARCAS");

            Categoria catTodo = new Categoria();
            catTodo.Id = -1;
            catTodo.Nombre = "Todo";
            categoriaCollectionAux.add(catTodo);

            String strCategorias = params[0].substring(indexCategoria + 11,
                    indexMarca);
            String[] arrayCategorias = strCategorias.split("\\|");

            for (int j = 0; j < arrayCategorias.length / count; j++) {

                String[] copyCategoria = new String[count];
                System.arraycopy(arrayCategorias, i, copyCategoria, 0, count);
                if (copyCategoria.length > 0) {
                    Categoria objCategoria = new Categoria();
                    objCategoria.Id = Integer.parseInt(copyCategoria[0]);
                    objCategoria.Nombre = copyCategoria[1];
                    categoriaCollectionAux.add(objCategoria);
                }
                i += count;
            }
            return categoriaCollectionAux;

        }

        protected void onPostExecute(CategoriaCollection resultado) {
            categoriaCollection = resultado;

            Spinner spinCategoria = (Spinner) findViewById(R.id.spinCategoria);

            Categoria.Adapter adapter = new Categoria.Adapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, resultado);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCategoria.setAdapter(adapter);

            // evento para selecionar un item
            spinCategoria
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView,
                        View view, int position, long id) {

                    MarcaCollection marcaCollectionCategoria = null;
                    Spinner spinMarca = (Spinner) findViewById(R.id.spinMarca);

                    Categoria.Adapter adapterCategoria = (Categoria.Adapter) adapterView
                            .getAdapter();

                    CurrenCategoria = adapterCategoria
                            .getItem(position);

                    if (CurrenCategoria != null
                            && CurrenCategoria.Nombre.equals("Todo")) {
                        // Obtenemos las marcas por categoria;
                        marcaCollectionCategoria = marcaCollection;

                    } else {
                        // Obtenemos las marcas por categoria
                        marcaCollectionCategoria = marcaCollection
                                .GetByCategoria(productoCollection,
                                CurrenCategoria);
                    }

                    spinMarca
                            .setAdapter(new Marca.Adapter(
                            getBaseContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            marcaCollectionCategoria));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

        }
    }

    public class Marcas extends AsyncTask<String, Void, MarcaCollection> {

        @Override
        protected MarcaCollection doInBackground(String... params) {
            // TODO Auto-generated method stub

            MarcaCollection marcaCollectionAux = new MarcaCollection();
            Integer i = 0, count = 2;
            int indexMarca = params[0].indexOf("MARCAS");
            int indexVersion = params[0].indexOf("VERSION");

            Marca catMarca = new Marca();
            catMarca.Id = -1;
            catMarca.Nombre = "Todo";
            marcaCollectionAux.add(catMarca);

            String strMarcas = params[0]
                    .substring(indexMarca + 7, indexVersion);
            String[] arrayMarcas = strMarcas.split("\\|");

            for (int j = 0; j < arrayMarcas.length / count; j++) {

                String[] copyMarca = new String[count];
                System.arraycopy(arrayMarcas, i, copyMarca, 0, count);
                if (copyMarca.length > 0) {
                    Marca objMarca = new Marca();
                    objMarca.Id = Integer.parseInt(copyMarca[0]);
                    objMarca.Nombre = copyMarca[1];
                    marcaCollectionAux.add(objMarca);
                }
                i += count;
            }
            return marcaCollectionAux;
        }

        protected void onPostExecute(MarcaCollection resultado) {

            marcaCollection = resultado;
           
            Spinner spinMarca = (Spinner) findViewById(R.id.spinMarca);

            spinMarca.setAdapter(new Marca.Adapter(getBaseContext(),
                    android.R.layout.simple_spinner_dropdown_item, resultado));  

            // evento para selecionar un item
            spinMarca.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView,
                        View view, int position, long id) {

                    Producto.Adapter productoAdapter = null;

                    Marca.Adapter adapterMarca = (Marca.Adapter) adapterView
                            .getAdapter();

                    CurrenMarca = adapterMarca.getItem(position);

                    if (CurrenMarca.Nombre.equals("Todo")
                            && CurrenMarca.Nombre.equals("Todo")) {
                        productoAdapter = new Producto.Adapter(
                                getBaseContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                productoCollection);
                    } else {
                        productoAdapter = new Producto.Adapter(
                                getBaseContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                productoCollection.GetByCategoriaMarca(
                                CurrenCategoria, CurrenMarca));
                    }

                    Spinner spinProducto = (Spinner) findViewById(R.id.spinProducto);
                    spinProducto.setAdapter(productoAdapter);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

        }
    }

    public class Productos extends AsyncTask<String, Void, ProductoCollection> {

        @Override
        protected ProductoCollection doInBackground(String... params) {
            // TODO Auto-generated method stub
            ProductoCollection productoCollectionAux = new ProductoCollection();
            int indexCategoria = params[0].indexOf("CATEGORIAS");

            Producto catProducto = new Producto();
            catProducto.SKU = new Long( -1);
            catProducto.Nombre = "Todo";
            productoCollectionAux.add(catProducto);

            String strProductos = params[0].substring(10, indexCategoria);
            String[] arrayProductos = strProductos.split("\\|");

            Integer i = 0, count = 14;

            for (int j = 0; j < arrayProductos.length / count; j++) {

                String[] copyProd = new String[count];
                System.arraycopy(arrayProductos, i, copyProd, 0, count);
                if (copyProd.length > 0) {
                    Producto objProducto = new Producto();
                    objProducto.IdCategoria = Integer.parseInt(copyProd[0]);
                    objProducto.IdMarca = Integer.parseInt(copyProd[1]);
                    objProducto.SKU = Long.parseLong(copyProd[2]);
                    objProducto.Nombre = copyProd[3];
                    objProducto.Descripcion = copyProd[4];
                    objProducto.Maximo = Double.parseDouble(copyProd[5]);
                    objProducto.Minimo = Double.parseDouble(copyProd[6]);
                    objProducto.Precio = Double.parseDouble(copyProd[7]);
                    objProducto.MuestraEnPedido = Integer.parseInt(copyProd[8]);
                    objProducto.MuestraEnAnaquel = Integer
                            .parseInt(copyProd[9]);
                    objProducto.MuestraEnBodega = Integer
                            .parseInt(copyProd[10]);
                    objProducto.MuestraEnExcibicionAdicional = Integer
                            .parseInt(copyProd[11]);
                    objProducto.MuestraEnMatPop = Integer
                            .parseInt(copyProd[12]);
                    objProducto.MuestraEnFrentesFrio = Integer
                            .parseInt(copyProd[13]);

                    productoCollectionAux.add(objProducto);
                }
                i += count;
            }
            return productoCollectionAux;
        }

        protected void onPostExecute(ProductoCollection resultado) {
            productoCollection = resultado;
            Spinner spinProducto = (Spinner) findViewById(R.id.spinProducto);
            spinProducto.setAdapter(new Producto.Adapter(getBaseContext(),
                    android.R.layout.simple_spinner_dropdown_item, resultado));

            // evento para selecionar un item
            spinProducto
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView,
                        View view, int position, long id) {

                    Producto.Adapter adapterProducto = (Producto.Adapter) adapterView
                            .getAdapter();

                    CurrenProducto = adapterProducto.getItem(position);

                    openOptionsMenu();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });
        }
    }

    private void CreaMenu(Menu menu) {
        MenuItem item = menu.add(0, 0, 0, "Item");
        item.setAlphabeticShortcut('a');
        //item.setIcon(R.drawable.ch_seleccion);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

    }
}
