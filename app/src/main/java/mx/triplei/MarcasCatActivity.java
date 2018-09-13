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
import android.widget.Spinner;
import android.widget.Toast;
import mx.triplei.R;


import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.Usuario;

public class MarcasCatActivity extends Activity {

    private Spinner spinMarca;
    private Context context; 
    private EnumFormulario currentFormulario;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private Pop currentPop;
    private Marca currentMarca= null;
    private SondeoModulos currentSondeo = null;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_bymarca);
        
        spinMarca = (Spinner) findViewById(R.id.sp_marcascat);
        
        context = this;

        //Recuperamos la informacion envianda en el intento
        Bundle bundle = getIntent().getExtras();
        this.currentFormulario = (EnumFormulario) bundle.get("item");

        this.currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        this.currentProyecto = (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
        this.currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        //this.currentSondeo = (mx.smartteam.entities.Sondeo) getIntent().getSerializableExtra("sondeo");
        this.currentSondeo = (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
        
        setTitle("Marcas");
        
        new Marcas().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        
    }/*Fin onCreate*/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generico2, menu);
        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(context);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cancelar:
                finish();
                return true;
                
            case R.id.action_continuar:
                //Aqui nos dirigimos al sondeo que debe ser contestado.
                if(spinMarca != null){
                    Object selectItem = spinMarca.getSelectedItem();
                    if(selectItem != null){
                    currentMarca = (mx.smartteam.entities.Marca)selectItem;
                   
                    Intent i = new Intent(MarcasCatActivity.this, SondeoActivity.class); 
                        i.putExtra("sondeo", currentSondeo);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop); 
                        i.putExtra("marca", currentMarca);
                    startActivity(i);
                    
                    }
                }
                
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    
     @Override
    protected void onResume() {
        super.onResume(); 
            new Marcas().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
    
    
    
 public class Marcas extends AsyncTask<Void, Void, MarcaCollection> {

        @Override
        protected MarcaCollection doInBackground(Void... categoria) {

            MarcaCollection marcaCollection = null;
            
                marcaCollection = mx.smartteam.data.Marca.GetAllXContestacion(context, currentProyecto, currentPop, currentSondeo);
                        
            return marcaCollection;
        }

        protected void onPostExecute(MarcaCollection marcaCollection) {

           // Spinner spinMarca = (Spinner) findViewById(R.id.spinMarca);

            spinMarca.setAdapter(new Marca.Adapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, marcaCollection));
            //spinMarca.setOnItemSelectedListener(new OnItemSelectedListener_Marca());
                       
        }
    }
    
}
