package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.ExhibicionConfig;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

public class SodActivity extends Activity {
    private Context context = null; private String tipo = "",tipoform;
    private EnumFormulario currentFormulario; private Pop currentPop; private Proyecto currentProyecto; 
    private Usuario currentUsuario; private int positionCat = 0, positionMarca = 0, positionExh = 0;
    private ExhibicionConfig exhibicionConfig ; private int bandera =0;
    private EditText boxUI1; private EditText boxUI2;
    private mx.smartteam.entities.Sod sod ;
    ProgressDialog progressDoalog;
    String mensaje = "";       
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.sod);
        context = this;
        InicializandoUI();
        Bundle bundle = getIntent().getExtras();
        currentFormulario = (EnumFormulario) bundle.get("item");
        currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        positionCat = (Integer) bundle.getInt("positionCat"); positionMarca = (Integer) bundle.getInt("positionMarca"); positionExh = (Integer) bundle.getInt("positionExh");
        tipoform = (String) bundle.get("tipo"); bandera = (Integer) bundle.getInt("bandera");
        exhibicionConfig = (mx.smartteam.entities.ExhibicionConfig) getIntent().getSerializableExtra("exhibicionConfig");
        sod = new mx.smartteam.entities.Sod();
        if(bandera>0){
            if(exhibicionConfig.Id != null && exhibicionConfig.Id > 0){
                try {
                // Ir por la info a la base de datos.
                    sod = mx.smartteam.business.Sod.GetOne(context, exhibicionConfig, currentPop);
                    boxUI1.setText(sod.Valor.toString());
                    boxUI2.setText(sod.Comentario);
                } catch (Exception ex) {
                    Logger.getLogger(SodActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        } else{ 
            boxUI1.setText("0");
        }
    }

    public void InicializandoUI(){
        boxUI1 = (EditText) findViewById(R.id.boxsod);
        boxUI2 = (EditText) findViewById(R.id.boxsodcomentario);
    }

    public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.anquel_sondeo_1, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {
         try{
            switch (Item.getItemId()) {
                case R.id.action_siguiente://tomar foto 
                    GoToPhoto();
                return true;

                case R.id.action_guardar: //guardar 
                    ValidacionDatos();
                return true;

                case R.id.action_cancelar: // Salir
                    finish();
                return true; 
                default:
            }
        }catch(Exception e){
            Toast.makeText(context,"" + e.getMessage() , Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(Item);
    }
    
    public void GoToPhoto(){
        mx.smartteam.entities.Foto objFoto = new mx.smartteam.entities.Foto();
        objFoto.Tipo = objFoto.Tipo.Sod;
        
        Intent intent = new Intent(SodActivity.this, CameraActivity.class);
        intent.putExtra("usuario", currentUsuario);
        intent.putExtra("proyecto", currentProyecto);
        intent.putExtra("pop", currentPop);
        // intent.putExtra("categoria", currentCategoria);
        // intent.putExtra("marca", CurrenMarca);
        intent.putExtra("exhibicionconfig", exhibicionConfig);
        intent.putExtra("foto", objFoto);
        intent.putExtra("opcionfoto",13);/* Sod */
        startActivity(intent);
    }
    
    
    
    public void ValidacionDatos() throws Exception {
        if(boxUI1.getText().toString().isEmpty()){
            throw new Exception("Total no puede ser nulo,\n Verifique por favor...");
        }
        
        if(boxUI1.getText().toString().equals(".")){
            throw new Exception("Valor en el campo total no reconocido,\n Verifique por favor...");
        }
           
        sod.Valor = Double.parseDouble(boxUI1.getText().toString());
        
        if(sod.Valor == 00 ){
            throw new Exception("Valor en Cero no aplica,\n Verifique por favor...");
        }
        
        
        sod.Comentario = boxUI2.getText().toString();
        sod.IdExhibicionConfig = exhibicionConfig.Id;
        sod.idVisita = currentPop.IdVisita;
        new Save().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sod);
    }
    
    class Save extends AsyncTask<mx.smartteam.entities.Sod, Void, mx.smartteam.entities.Sod>{
        private ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            pd = new ProgressDialog(context);
            pd.setTitle( Html.fromHtml("<font color='#FFFFF'>Guardando</font>"));
            pd.setMessage("Espere por favor...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
        
        @Override
        protected mx.smartteam.entities.Sod doInBackground(mx.smartteam.entities.Sod... arg0) {
            mx.smartteam.entities.Sod entitysod;
            entitysod = (mx.smartteam.entities.Sod) arg0[0];
           
            
             String carrier= null;
                try{
                    if(bandera > 0){
                        carrier =  mx.smartteam.business.Sod.Update(context, sod);
                       if(carrier.equals("OK")){
                           mensaje = "actualizada";
                       }
                    }else{
                       carrier =  mx.smartteam.business.Sod.Insert(context, sod, currentPop, exhibicionConfig);
                       if(carrier.equals("OK")){
                           mensaje = "guardada";
                       }
                    }
                    
                }catch(Exception ex){
                
                }
            
            return entitysod;
        }
    
        @Override
        protected void onPostExecute(mx.smartteam.entities.Sod entitysod) {
            pd.dismiss();
            Toast.makeText(context, "Información "+ mensaje + " con éxito", Toast.LENGTH_LONG).show();
            GoBack();
            finish();
        }
    }
    
    public void GoBack(){
        Intent i = new Intent(SodActivity.this, ExhibicionActivity.class);
            i.putExtra("item", currentFormulario);
            i.putExtra("proyecto", currentProyecto);
            i.putExtra("usuario", currentUsuario);
            i.putExtra("pop", currentPop);
            i.putExtra("tipo", tipoform);
            i.putExtra("positionCat", positionCat);
            i.putExtra("positionMarca",positionMarca);
            i.putExtra("positionExh", positionExh);
        startActivity(i);
        
    }

}
