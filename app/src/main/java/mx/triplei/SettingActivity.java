
package mx.triplei;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.triplei.R;
import static mx.triplei.ProductoActivity.config;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Pregunta;
import mx.smartteam.entities.MovilConfigCollection;
import mx.smartteam.entities.MovilConfig;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;
/**
 *
 * @author jaime.bautista
 */
public class SettingActivity extends Activity implements View.OnClickListener{

    private LinearLayout LayoutSondeo = null;
    private Context context;
    private Usuario currentUsuario;
    private Proyecto currentProyecto;
    private MovilConfig movilConfigq;
    private MovilConfigCollection movilConfigCollection;
    private MovilConfig movilConfig;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.context = this;
             setContentView(R.layout.activity_setting);
             this.currentUsuario = getIntent().getSerializableExtra("usuario") == null ? null : (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
             this.currentProyecto = getIntent().getSerializableExtra("proyecto") == null ? null : (mx.smartteam.entities.Proyecto) getIntent().getSerializableExtra("proyecto");
                if (this.currentUsuario != null && this.currentProyecto != null ) {

            this.setTitle(Html.fromHtml("<small><strong>Settings</strong></small>"));
            CreaLayout();
                }
    }
  
    
    public void onClick(View view) {
        try {
            String s = ((RadioButton) view).getText().toString();
          
                   if(s.equals("Sku")){
                        movilConfig.Tipo = "sku";
                      
                   }
                   if(s.equals("Nombre"))
                    {
                        movilConfig.Tipo = "nombre"; 
                    }
                   
                   if(s.equals("offline"))
                   {
                        movilConfig.Conexion = "offline";                       
                   }
                   if(s.equals("online"))
                    {
                        movilConfig.Conexion = "online";
                    }
                     
        }
    	 catch (Exception e1) {
    	   e1.printStackTrace();
    	 }
    	}
    
    
    
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sondeo, menu);
                if(mx.triplei.ProductoActivity.config){
               menu.removeItem(R.id.action_foto);
            }
                Sucursal sucursal =new Sucursal();
                Drawable d=sucursal.setIconMenu(context);
                ActionBar actionBar = getActionBar();
                actionBar.setIcon(d);
        return true;
    } 
     
      @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                
                Guardar();
                Toast.makeText(context, "Configuración guardada con éxito", Toast.LENGTH_LONG).show();
                finish();
                return true;
            case R.id.action_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
   private void CreaLayout() {
        try {
            this.LayoutSondeo = (LinearLayout) findViewById(R.id.LayoutSettings);
                movilConfig = mx.smartteam.data.MovilConfig.GetConfig(context, currentUsuario);
                 movilConfigCollection = mx.smartteam.data.MovilConfig.GetByGpsConfig(context, currentUsuario);

                 this.LayoutSondeo.addView(PreguntaConexion(movilConfig));
                 this.LayoutSondeo.addView(PreguntaProducto(movilConfig));



        } catch (Exception ex) {
            Logger.getLogger(SondeoActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
   
   
    private View PreguntaProducto(MovilConfig movilConfig ) {
         
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setTag(movilConfig);
        TextView text = new TextView(this);
        text.setText("Busqueda de productos por:");
       
        RadioGroup group = new RadioGroup(this);
                
        RadioButton radS = new RadioButton(this);
        radS.setId(0);
        radS.setText("Nombre");
        radS.setOnClickListener(this);
        RadioButton radN = new RadioButton(this);
        radN.setId(1);
        radN.setText("Sku");
        radN.setOnClickListener(this);
        
        if("nombre".equals(movilConfig.Tipo)){
        radS.toggle();
        
        }else{
        radN.toggle();
        }
        
        
        group.addView(radN);
        group.addView(radS);


        layout.addView(text);
        layout.addView(group, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }
    
    
      private View PreguntaConexion(MovilConfig movilConfig ) {
         
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setTag(movilConfig);
        TextView text = new TextView(this);
        text.setText("Modo de conexión:");
       
        RadioGroup group = new RadioGroup(this);
                
        RadioButton radS = new RadioButton(this);
        radS.setId(0);
        radS.setText("offline");
        radS.setOnClickListener(this);
        RadioButton radN = new RadioButton(this);
        radN.setId(1);
        radN.setText("online");
        radN.setOnClickListener(this);
        
        if("offline".equals(movilConfig.Conexion)){
        radS.toggle();
        
        }else{
        radN.toggle();
        }


        group.addView(radN);
        group.addView(radS);


        layout.addView(text);
        layout.addView(group, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }


    private void Guardar() {


        mx.smartteam.data.MovilConfig.ConfigTipo(context, currentUsuario, movilConfig);
        

    }

}
