
package mx.triplei;

import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.FormularioCollection;
import mx.smartteam.entities.MenuCollection;
import mx.smartteam.entities.SondeoCollection;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
//import mx.smartteam.Sucursal.DescargaSondeos;
import mx.smartteam.settings.ServiceClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.FotoCollection;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.Sondeo;
import mx.smartteam.entities.Notificaciones;
import mx.smartteam.entities.SondeoModulos;
import mx.smartteam.entities.SondeoModulosCollection;


public class newSondeoActivity extends ListActivity{
    private Context context;
    private mx.smartteam.entities.Usuario currentUsuario = null;
    // proyecto actual
    private mx.smartteam.entities.Proyecto currentProyecto = null;
    //private mx.smartteam.entities.Sondeo currentSondeo = null;
    private mx.smartteam.entities.SondeoModulos currentSondeo = null;
    private mx.smartteam.entities.Pop currentPop = null;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_principal);
            this.context = this;
    
            //this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.Sondeo) getIntent().getSerializableExtra("sondeo");
            this.currentSondeo = getIntent().getSerializableExtra("sondeo") == null ? null : (mx.smartteam.entities.SondeoModulos) getIntent().getSerializableExtra("sondeo");
            currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
            currentProyecto = (mx.smartteam.entities.Proyecto) getIntent() .getSerializableExtra("proyecto");
            currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
            try {
                CreaMenu();
            } catch (Exception ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.setTitle(Html.fromHtml("<small><strong>" + currentPop.Cadena + " " + currentPop.Sucursal
                    + "</strong></small>"));

            ActionBar actionBar = getActionBar();
            actionBar.setDisplayUseLogoEnabled(false);
 
    }
    
    
     private void CreaMenu() throws Exception {
       // SondeoCollection sondeoCollection = mx.smartteam.business.Sondeo.GetAllGruposSondeos(context, currentProyecto, currentSondeo);
        SondeoModulosCollection sondeoCollection = mx.smartteam.business.Sondeo.GetAllGruposSondeossm(context, currentProyecto, currentSondeo);
        
        mx.smartteam.entities.MenuCollection menuCollection = new MenuCollection();


        //for (mx.smartteam.entities.Sondeo sondeo : sondeoCollection) {
        for (mx.smartteam.entities.SondeoModulos sondeo : sondeoCollection) {


            mx.smartteam.entities.Menu menu = new mx.smartteam.entities.Menu();
            menu.Id = sondeo.Id;

            menu.Key = EnumFormulario.sondeo;
            menu.Name = sondeo.Nombre;
            menu.Order = sondeo.Orden;
            menu.Icon = R.drawable.ch_sondeo;
            menu.Tag = sondeo;
            menu.Identiventas = sondeo.IdentificadorVentas;
            menuCollection.add(menu);
        }

         setListAdapter(new AdaptadorMenu(menuCollection));

    }
public void onListItemClick(ListView parent, View v, int position, long id) {
        mx.smartteam.entities.Menu opcion = (mx.smartteam.entities.Menu) parent.getItemAtPosition(position);// MenuOper.get(position);
        //appConfiguration.CurreMenu = opcion;
        try{
        Intent i;
        if (opcion != null) {

            switch (opcion.Key) {
                    
                case sondeo:
                    if (opcion.Identiventas == 1) {
                        try{
                        i = new Intent("mx.smartteam.producto");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        }catch(Exception e){
                            e.getMessage().toString();
                        }
                        
                        super.onStop();
                    } else if(opcion.Identiventas == 2){
                        i = new Intent("mx.smartteam.marcascatacivity");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        i.putExtra("opcionmarca",2);
                        startActivity(i);
                        super.onStop();
                    } else if(opcion.Identiventas == 3){
                        i = new Intent("mx.smartteam.preguntadinamicaactivity");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    } else if(opcion.Identiventas == 5){ //2 preguntas
                        i = new Intent("mx.smartteam.pdinamicacategoriaactivity");
                        i.putExtra("item", opcion.Key);
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        startActivity(i);
                        super.onStop();
                    }
                    else {
                        i = new Intent("mx.smartteam.sondeo");
                        i.putExtra("sondeo", (SondeoModulos) opcion.Tag);
                        i.putExtra("usuario", currentUsuario);
                        i.putExtra("proyecto", currentProyecto);
                        i.putExtra("pop", currentPop);
                        
                        startActivity(i);
                        super.onStop();
                    }
                    finish();
                    break;
                // finish();
                default:
                    break;
            }
        }
        }catch(Exception e){
            e.getMessage().toString();
        }

    }

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generico, menu);
//            if (modulo==1) {
//            menu.removeItem(R.id.by_marca);
//            menu.removeItem(R.id.by_product);
//        }
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    
    class AdaptadorMenu extends ArrayAdapter<mx.smartteam.entities.Menu> {

        private MenuCollection menuCollection;

        public AdaptadorMenu(MenuCollection menuCollection) {
            // TODO Auto-generated constructor stub
            super(newSondeoActivity.this, R.layout.itemmenu, menuCollection);
            this.menuCollection = menuCollection;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.itemmenu, parent, false);

            mx.smartteam.entities.Menu menu = this.menuCollection.get(position);

            TextView label = (TextView) row.findViewById(R.id.label);
            label.setText(menu.Name);
            label.setTextColor(Color.BLACK);

            ImageView icon = (ImageView) row.findViewById(R.id.menuicon);
            icon.setImageResource(menu.Icon);

            return row;
        }
    }
    
}
