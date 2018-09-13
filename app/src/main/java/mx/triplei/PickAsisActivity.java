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
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Foto;


public class PickAsisActivity extends Activity {
    Context myContext,context;
    String filepath, ruta;;
    private Uri uri;
    private Integer optionSel = 0;
    private Integer fentrada = 0;
    private mx.smartteam.entities.PopVisita popVisita;
    private mx.smartteam.entities.Pop currentPop;
    private byte[] imageBytes;
    Sucursal sucursal =new Sucursal();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this; String titulo = "";
        this.context = this;
        Bundle bundle = getIntent().getExtras();
        uri = getIntent().getData();
        ruta = bundle.getString("ruta");
        fentrada = bundle.get("fentrada") == null ? 0 : (Integer) bundle.get("fentrada");
        this.currentPop = getIntent().getSerializableExtra("pop") == null ? null : (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        setContentView(R.layout.activity_photo);
        
        switch(fentrada){
            case 1: optionSel = 0; titulo = "FOTO ENTRADA"; break;
            case 2: optionSel = 1; titulo = "FOTO SALIDA"; break;
            case 3: MensajeAsistencia(); titulo = "ASISTENCIA"; break;
        }
        //titulo
        this.setTitle(Html.fromHtml("<small><strong>"+ titulo+"</strong></small>"));
        ImageView photoView = (ImageView) findViewById(R.id.photo);
        photoView.setImageURI(uri);
        
        if (currentPop != null) {
            this.popVisita = mx.smartteam.data.PopVisita.GetById(myContext, currentPop.IdVisita);
        }
        
        //Convertimos la imagen a bytearray para que posteriormente se convierta a base64
        filepath = ruta;
        File imagefile = new File(filepath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Bitmap bm = BitmapFactory.decodeStream(fis);
        int countByArray = bm.getRowBytes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        if(countByArray >3000){
            bm.compress(Bitmap.CompressFormat.JPEG, 78 , out);    
        }
        else 
        {
            bm.compress(Bitmap.CompressFormat.JPEG, 100 , out);    
        }
        byte[] b = out.toByteArray(); 
        imageBytes = b;
       imagefile.deleteOnExit();//Eliminamos al finalizar la actividad  
    }
    
    public void MensajeAsistencia(){
        final CharSequence[] items = {"Entrada", "Salida"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Drawable d=sucursal.setIconAlert(myContext);
        builder.setIcon(d);
        builder.setTitle( Html.fromHtml("<font color='#FFFFF'>Asistencia</font>"));
        //builder.setTitle("Asistencia");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Entrada")) {
                    dialog.cancel();
                    optionSel = 0;
                }
                if (items[item].equals("Salida")) {
                    dialog.cancel();
                    optionSel = 1;
                    fentrada=2;
                }
            }
        });
        builder.create().show();
    }
    
    
 public void Eliminar(){
    File imagefile = new File(filepath);
    imagefile.delete();
    
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                try {
                    Guardar();
                } catch (Exception e) {
                    Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_cancelar:
                Eliminar();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    
    private void Guardar() throws Exception {

        if (imageBytes == null) {
            throw new Exception("Ud. no ha capturado una imagen verifique por favor");
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setTitle("SmartTeam");
        alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>SmartTeam</font>"));
        Drawable d=sucursal.setIconAlert(myContext);
        alertDialogBuilder.setIcon(d);
        alertDialogBuilder
                .setMessage("Desea utilizar la Imagen?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                Foto entityFoto = new Foto();
                entityFoto.Foto = encodedImage;
                entityFoto.IdVisita = popVisita.Id;
                if (optionSel == 0) {
                    entityFoto.Tipo = Foto.Type.Entrada;
                } else {
                    entityFoto.Tipo = Foto.Type.Salida;
                }
                entityFoto.IdCategoria = 0;
                entityFoto.IdSondeo = 0;
                entityFoto.nOpcion = 0;
                entityFoto.Marcaid =0;
                entityFoto.SKU =0L;
/*

        if(foto.Categoriaid > 0 && foto.Categoriaid!= null ){values.put("Categoria", foto.Categoriaid);}
                        */
                new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entityFoto);

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //CapturarImagen();
                dialog.cancel();
            }
        });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    
    
      class Save extends AsyncTask<Foto, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(myContext);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Registrando foto</font>"));
            //pd.setTitle("Registrando foto");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(Foto... params) {

            String result = null;
            Foto foto = (Foto) params[0];
            try {
                foto.IdSondeo = 0; foto.idExhibicionConfig = 0; foto.SKU = 0L; 
                foto.Marcaid = 0; foto.Categoriaid = 0; foto.nOpcion = 0; foto.IdCategoria = 0;
                
                mx.smartteam.business.Foto.Insert(myContext, foto);
                result = "OK";
            } catch (Exception ex) {
                result = "-1";
                Logger.getLogger(FotoAsistenciaActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        protected void onPostExecute(String resultado) {
            pd.dismiss();
            if (resultado.trim().equals("OK")) {
                Toast.makeText(myContext, "Foto registrada correctamente", Toast.LENGTH_LONG).show();
                Eliminar();
                finish();
                if (fentrada == 2){
                    AccionCerrarDia();
                }
            } else {
                Toast.makeText(myContext, "Error al enviar la foto",Toast.LENGTH_LONG).show();
            }
            
        }
    }
      
       @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.foto_asistencia, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }
        public  void AccionCerrarDia() {

        if (currentPop != null) {
        }

        new CerrarDia().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
        
    class  CerrarDia extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Cerrando visita</font>"));
            //pd.setTitle("Cerrando visita");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... param) {

            String result = null;
            try {
                result = mx.smartteam.business.PopVisita.CloseDay(context, currentPop);
            } catch (Exception ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultado) {
            pd.dismiss();
            if (resultado.trim().equals("OK")) {
                Toast.makeText(context, "Tienda cerrada correctamente", Toast.LENGTH_LONG).show();
                finish();
                Intent i;
                i = new Intent(MenuPrincipal.FINISH_ALERT);      
                sendBroadcast(i);
                //MenuPrincipal.finish(context);
            } else {
                Toast.makeText(context, "No es posible cerrar Tienda, verifique por favor", Toast.LENGTH_LONG).show();
            }
        }
    }
       
    }

