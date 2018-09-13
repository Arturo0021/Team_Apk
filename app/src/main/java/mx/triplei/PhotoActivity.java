package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.ExhibicionConfig;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.SondeoModulos;

public class PhotoActivity extends Activity {
     String filepath;
    private Uri uri;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Producto currentProducto;
    private Categoria currentCategoria;
    private Marca currentMarca;
    private Pop currentPop;
    private ListView listClasificacion = null;
    private EditText inputComentario;
    private Categoria.Adapter_Single adapterClasificacion;
    private Opcion.Adapter_Multiple adapterSubClasificacion;
    private Dialog.Clasificacion dgClasificacion;
    private Dialog.SubClasificacion dlgSubClasificacion;
    private Dialog.Comentario dlgComentario;
    Context myContext;
    private ListView listSubClasificacion = null;
    private byte[] imageBytes;
    private String ruta,tpoFoto,tipoform;
    String rescueComentario;
    static int idfoto=0, opcionfoto = 0;
    private Foto foto=null;
    private Integer intopcion = 0,tiposondeo;
    private Integer idCategoria,Categoriaid, Marcaid;
    private Long SKU;
    private EnumFormulario currentFormulario;
    private Foto currentFoto;
    private SondeoModulos currentSondeo;
    private Opcion currenOpcion = null;
    private int p1, p2;
    private ExhibicionConfig exhibicionConfig ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        myContext = this;
        Bundle bundle = getIntent().getExtras();
        uri = getIntent().getData();
        //this.currentFormulario = (EnumFormulario) bundle.get("item");
        this.opcionfoto = bundle.get("opcionfoto") == null ? 0 : (Integer) bundle.get("opcionfoto");
        this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentProducto = getIntent().getSerializableExtra("producto") == null ? null : (mx.smartteam.entities.Producto) getIntent().getSerializableExtra("producto");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        this.currentCategoria = getIntent().getSerializableExtra("categoria") == null ? null : (mx.smartteam.entities.Categoria) getIntent().getSerializableExtra("categoria");
        this.currentMarca = getIntent().getSerializableExtra("marca") == null ? null : (mx.smartteam.entities.Marca) getIntent().getSerializableExtra("marca");
        this.currentFormulario = bundle.get("item") == null ? null : (EnumFormulario) bundle.get("item");
        this.currentFoto = getIntent().getSerializableExtra("foto") == null ? null : (mx.smartteam.entities.Foto) getIntent().getSerializableExtra("foto");
        this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        this.tipoform = (String) bundle.get("tipo") == null ? null : (String) bundle.get("tipo");
        intopcion = bundle.get("intopcion") == null ? 0 : (Integer) bundle.get("intopcion"); //  intopcion
        ruta = bundle.getString("ruta");
        tpoFoto = bundle.getString("tpoFoto");
        tiposondeo  = bundle.get("tiposondeo") == null ? 0 : (Integer) bundle.get("tiposondeo");
        this.currenOpcion = getIntent().getSerializableExtra("opcion") == null ? null : (mx.smartteam.entities.Opcion) getIntent().getSerializableExtra("opcion");
        this.p1 = bundle.get("p1") == null ? 0 : (Integer) bundle.get("p1");
        this.p2 = bundle.get("p2") == null ? 0 : (Integer) bundle.get("p2");
        this.exhibicionConfig = getIntent().getSerializableExtra("exhibicionconfig") == null ? null :(mx.smartteam.entities.ExhibicionConfig) getIntent().getSerializableExtra("exhibicionconfig");

        ImageView photoView = (ImageView) findViewById(R.id.photo);
        photoView.setImageURI(uri);
        
        switch(opcionfoto){
            case 2: /* Solo aplica para los tipo fotos  */ 
                try {
                    CategoriaCollection categoriaCollection = mx.smartteam.business.Categoria.GetCategoriaFotoByProyecto(myContext, currentProyecto);
                    OpcionCollection opcionCollection = mx.smartteam.business.Opcion.GetByProyecto(myContext, currentProyecto);
                    dgClasificacion = new Dialog().new Clasificacion(myContext, categoriaCollection);
                    dlgSubClasificacion = new Dialog().new SubClasificacion(myContext, opcionCollection);
                    dlgComentario = new Dialog().new Comentario(myContext);
                } catch (Exception ex) { ex.getMessage().toString();}
            break;
            case 3:
                    /* Sondeos */
            break;
                
            case 4: /* Exh Adicional*/
                
            break;
            
            case 5: /* Mat Promocional*/
                
            break;
                
            case 6: /* Bodega */
                
            break;
                
                      
            case 7: /* precios*/
                
            break;
                
                  
            case 8: /* SOS */
                
            break;
        
            case 9: /*Ftoto XP */
                
            break;
        }
        
        filepath = ruta;
        File imagefile = new File(filepath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , out);
        byte[] b = out.toByteArray();
        imageBytes = b;
        imagefile.deleteOnExit();  //Eliminamos al finalizar la actividad
    }//END ONCREATE
    
    public void Eliminar(){
        File imagefile = new File(filepath);
        imagefile.delete();
    
    }
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putString("inputComentario", inputComentario.getText().toString());
        super.onSaveInstanceState(outBundle);
    }
    
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rescueComentario = savedInstanceState.getString("inputComentario");
        inputComentario.setText(rescueComentario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch(opcionfoto){ /* Elegir el Menu segun el portal   */
            case 2: /* */
                getMenuInflater().inflate(R.menu.foto, menu);
            break;
                
            case 3: /* Sondeos */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
                
            case 4: /* Exh Adicional*/
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
            
            case 5: /* Mat Promocional*/
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
                
            case 6: /* Bodega */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
                
                      
            case 7: /* precios*/
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
                
                  
            case 8: /* SOS */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
        
            case 9: /*Ftoto XP */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;

            case 10: /*Ftoto Anaquel  */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
                
            case 11: /*Ftoto Adicional */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
            
            case 12: /*Ftoto Adicional */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
                
            case 13: /*Ftoto Adicional */
                getMenuInflater().inflate(R.menu.foto_1, menu);
            break;
        }
       
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(this);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                Guardar();
            break;

            case R.id.action_cancelar:
                Eliminar();
                finish();
            break;

            case R.id.action_clasificacion:
                dgClasificacion.Show();
            break;

            case R.id.action_subclasificacion:
                dlgSubClasificacion.Show();
            break;

            case R.id.action_comentarios:
                dlgComentario.Show();
            break;
        }
        return true;
    }

    public class OnItemClickListener_Clasificacion implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        }
    }

    public class Dialog {
        public class Clasificacion extends AlertDialog.Builder {
            private CategoriaCollection items;
            private AlertDialog alertDialog;

            public Clasificacion(Context context, CategoriaCollection items) {
                super(context);
                super.setTitle( Html.fromHtml("<font color='#FFFFF'>Clasificación</font>"));
                LayoutInflater inflater = LayoutInflater.from(super.getContext());
                this.items = items;

                View viewClasf = inflater.inflate(R.layout.clasificacion_foto, null);
                listClasificacion = (ListView) viewClasf.findViewById(R.id.listClasf);
                listClasificacion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapterClasificacion = new Categoria.Adapter_Single(super.getContext(), items);
                listClasificacion.setAdapter(adapterClasificacion);
                super.setView(viewClasf);
                super.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                super.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });                
                alertDialog = super.create();
            }
            private void Show() {                
                alertDialog.show();
            }
        }

        public class SubClasificacion extends AlertDialog.Builder {
            private OpcionCollection items;
            private AlertDialog alertDialog;
            public SubClasificacion(Context context, OpcionCollection items) {
                super(context);
                super.setTitle( Html.fromHtml("<font color='#FFFFF'>"+(items.size() > 0 ? items.get(0).Titulo : "Sub Clasificación")+"</font>"));   
                this.items = items;
                LayoutInflater inflater = LayoutInflater.from(super.getContext());
                View viewClasf = inflater.inflate(R.layout.clasificacion_foto, null);
                listSubClasificacion = (ListView) viewClasf.findViewById(R.id.listClasf);
                listSubClasificacion.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                //Activity myActi=    (Activity) super.getContext().getSystemService(Context.ACTIVITY_SERVICE);
                adapterSubClasificacion = new Opcion.Adapter_Multiple(super.getContext(), items);
                listSubClasificacion.setAdapter(adapterSubClasificacion);
                super.setView(viewClasf);
                super.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                super.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });               
                alertDialog = super.create();
            }

            private void Show() {
                alertDialog.show();
            }
        }

        public class Comentario extends AlertDialog.Builder {
            private AlertDialog alertDialog;
            public Comentario(Context context) {
                super(context);
                super.setTitle( Html.fromHtml("<font color='#FFFFF'>Comentario</font>"));
                inputComentario = new EditText(myContext);
                inputComentario.setSingleLine();
                int maxLength = 250;    
                inputComentario.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
                
                super.setView(inputComentario);
                super.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = inputComentario.getText().toString().trim();
                    }
                });
                super.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alertDialog = super.create();
            }
            private void Show() {
                alertDialog.show();
            }
        }
    }
    
    private mx.smartteam.entities.Categoria GetSelectClasificacion() {
        Categoria catSel = null;
        if (listClasificacion != null) {
            for (Long l : listClasificacion.getCheckItemIds()) {
                catSel = adapterClasificacion.getItem(l.hashCode());
                if (catSel != null) {
                    return catSel;
                }
            }
        }
        return catSel;
    }
    
    private OpcionCollection GetSelectSubClasificacion() {
        Opcion opcSelects = null;
        OpcionCollection opcionCollection = new OpcionCollection();
        String sep = "";
        if (listSubClasificacion != null) {
            for (Long l : listSubClasificacion.getCheckItemIds()) {
                opcSelects = adapterSubClasificacion.getItem(l.hashCode());
                if (opcSelects != null) {
                    opcionCollection.add(opcSelects);
                }
            }
        }
        return opcionCollection;
    }
      
    private void Guardar() {
        if (imageBytes != null) {
            
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            Foto objFoto = new Foto();
            objFoto.IdVisita = currentPop.IdVisita;
            objFoto.Foto = encodedImage;
            OpcionCollection opcionCollection = GetSelectSubClasificacion();
            
            switch(opcionfoto){
                case 2: /* FOTO */ 
                    objFoto.Tipo = Foto.Type.Foto;
                    Categoria categoria = GetSelectClasificacion();
                    if (categoria != null) {
                        objFoto.IdCategoria = categoria.Id;
                    }
                    objFoto.Comentario = inputComentario.getText().toString();
                break;
                    
                case 3:    /* Sondeos */
                    objFoto.Tipo = Foto.Type.Sondeo;
                    switch(tiposondeo){
                        case 8:
                            objFoto.Comentario = currentFoto.Comentario;
                            objFoto.IdSondeo = currentSondeo.Id;
                        break; 
                        
                        case 6:
                            objFoto.Comentario = currentFoto.Comentario;
                            objFoto.IdSondeo = currentSondeo.Id;
                        break; 
                        
                        case 5:
                            objFoto.IdSondeo = currentSondeo.Id;
                            objFoto.IdCategoria = p1;
                            objFoto.nOpcion = p2;
                            
                        break;
                            
                        case 4:
                            objFoto.IdSondeo = currentSondeo.Id;
                        break; 
                            
                        case 3:
                            objFoto.IdSondeo = currentSondeo.Id;
                            objFoto.nOpcion = currenOpcion.Id;
                            
                        break;
                        
                        case 2:
                            objFoto.IdSondeo = currentSondeo.Id;
                            if(currentMarca != null){
                                objFoto.Marcaid = currentMarca.Id;
                            }
                        break;
                            
                        case 1:
                            objFoto.SKU = currentProducto.SKU;
                            objFoto.IdSondeo = currentSondeo.Id;
                        break;
                            
                        case 0:
                            objFoto.IdSondeo = currentSondeo.Id;
                        break; 
                    }
                    
                break;
                    
                case 4: /*  Exhibicion Adicional*/
                   objFoto.Tipo = Foto.Type.Exhibicion;
                   objFoto.SKU = currentProducto.SKU;
                break;
                    
                case 5: /*  Material  Promocional*/
                   objFoto.Tipo = Foto.Type.MaterialPromocional;
                   objFoto.SKU = currentProducto.SKU;
                break;
                
                case 6: /*  Bodega  */
                   objFoto.Tipo = Foto.Type.Bodega;
                   objFoto.SKU = currentProducto.SKU;
                break;
                    
                case 7: /*  Precios  */
                   objFoto.Tipo = Foto.Type.Precio;
                   objFoto.SKU = currentProducto.SKU;
                break;
                
                case 8: /*  Sos  */
                   objFoto.Tipo = Foto.Type.Sos;
                   objFoto.Categoriaid = currentCategoria.Id;
                   objFoto.Marcaid = currentMarca.Id;
                break;
                
                case 9: /*  Foto XP  */
                   objFoto.Tipo = Foto.Type.foto_producto;
                   objFoto.Categoriaid = currentCategoria.Id;
                   objFoto.Marcaid = currentMarca.Id;
                   objFoto.SKU = currentProducto.SKU;
                break;
                
                case 10: /*  Foto anaquel  */
                   objFoto.Tipo = Foto.Type.foto_anaquel;
                   objFoto.Categoriaid = currentCategoria.Id;
                   objFoto.Marcaid = currentMarca.Id;
                break;
                    
                case 11:/* foto adicional*/ 
                    objFoto.Tipo = Foto.Type.foto_adicional;
                    objFoto.IdCategoria = currentFoto.IdCategoria;
                    objFoto.Categoriaid = currentCategoria.Id;
                break;
                    
                case 12:/* foto adicional*/ 
                    objFoto.Tipo = Foto.Type.Anaquel;
                    objFoto.SKU = currentProducto.SKU;
                break;
                    
                    
                case 13:
                    objFoto.Tipo = Foto.Type.Sod;
                    objFoto.idExhibicionConfig = exhibicionConfig.Id;   
                break;
            }
            
            new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objFoto, opcionCollection);
        } else {
            Toast.makeText(myContext, "Ud. no ha capturado la imagen, verifique por favor", Toast.LENGTH_LONG).show();
        }
    }  
    
       class Save extends AsyncTask<Object, Void, Integer> {
        private Object[] parameters; private ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(myContext);
            pd.setTitle("Enviando imagen");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            // TODO Auto-generated method stub
            Integer result=0;
            Foto foto = (Foto) params[0];
            /* Validamos la info antes de insertar*/
                if(foto.IdCategoria == null){foto.IdCategoria = 0;}
                if(foto.IdSondeo == null){foto.IdSondeo = 0;}
                if(foto.nOpcion==null){foto.nOpcion = 0;}
                if(foto.Marcaid == null){foto.Marcaid =0;}
                if(foto.SKU == null){foto.SKU =0L;}
                if(foto.idExhibicionConfig == null){foto.idExhibicionConfig = 0;}
            
            result = mx.smartteam.data.Foto.Insert(myContext, foto);
            if (result != null) {
                OpcionCollection opciones = (OpcionCollection) params[1];
                for (Opcion opcion : opciones) {
                    try {
                        mx.smartteam.business.Foto.Opcion.Insert(myContext, result, opcion);
                    } catch (Exception ex) {
                        Logger.getLogger(FotoActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            delpicture(ruta);
            return result;
        }

        protected void onPostExecute(Integer resultado) {
            pd.dismiss();
            if (resultado != null && resultado.equals(0)) {
                Toast.makeText(myContext, "Error al guardar la foto", Toast.LENGTH_LONG).show();
            } else {
                Eliminar();
                Toast.makeText(myContext, "Fotografia guardada correctamente",Toast.LENGTH_LONG).show();

                /*vamos a regresar*/
                switch(opcionfoto){
                    case 9:
                        Intent i2 = new Intent("mx.smartteam.FotoProducto");
                        i2.putExtra("item", currentFormulario);
                        i2.putExtra("usuario", currentUsuario);
                        i2.putExtra("proyecto", currentProyecto);
                        i2.putExtra("pop", currentPop);
                        i2.putExtra("tipo", tipoform);
                        startActivity(i2);
                    break;
                        
                    case 10:
                        Intent i = new Intent("mx.smartteam.bymarca");
                        i.putExtra("item", currentFormulario);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                    break;
                        
                    case 11:
                        Intent i3 = new Intent(PhotoActivity.this, FotoAdicional.class);
                        i3.putExtra("item", currentFormulario);
                        i3.putExtra("tipo", currentFormulario.foto_adicional.toString());
                        i3.putExtra("usuario", currentUsuario);
                        i3.putExtra("proyecto", currentProyecto);
                        i3.putExtra("pop", currentPop);
                        startActivity(i3);
                    break;
                
                }
                finish();
            }
        }
    }
       
        public String delpicture(String ruta){
            File file = new File(ruta)                                                                     ;
            boolean deleted = file.delete();
            return null;
        }

}   /*  END CLASS   */
