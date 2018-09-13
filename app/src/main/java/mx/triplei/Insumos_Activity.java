package mx.triplei;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import mx.triplei.R;
import mx.smartteam.entities.Proyecto;

/**
 *
 * @author ivan.guerra
 */
public class Insumos_Activity extends Activity{
    
    mx.smartteam.entities.Proyecto proyectos;
    mx.smartteam.entities.Areas_InsumosCollection areas_insumos;
    mx.smartteam.entities.Usuario currentUsuario;
    mx.smartteam.entities.Pop currentPop;
    
    ListView list_item;
    Context context;
    
     public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.insumos_activity);
        context = this;
        
        
        list_item = (ListView)findViewById(R.id.list_item);
        
        proyectos = (mx.smartteam.entities.Proyecto)getIntent().getSerializableExtra("proyecto");
        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        new ThreadInsumos().execute(proyectos);

        
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Dispara el evento clic para pasar a la pantalla de insumos

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                Intent intent = new Intent(Insumos_Activity.this, Insumos_Answ_Activity.class);
                intent.putExtra("areas_insumos", areas_insumos.get(position));
                intent.putExtra("proyectos", proyectos);
                intent.putExtra("pop", currentPop);
                intent.putExtra("usuario", currentUsuario);
                startActivity(intent);
                
            }
        });
        
     }
     
     class ThreadInsumos extends AsyncTask<mx.smartteam.entities.Proyecto, Void, Boolean> {
            
         private ProgressDialog dialog;
        
         protected void onPreExecute() {
            dialog = new ProgressDialog(Insumos_Activity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.message_espere_dialog));
            dialog.setCancelable(false);
            dialog.show();
        }
         
        @Override
        protected Boolean doInBackground(Proyecto... params) {
            
            try {
                
                mx.smartteam.business.Insumos_Config.DownloadConfig(context, params[0]);
                mx.smartteam.business.Areas_Insumos.DownloadAreas(context, params[0]);
                mx.smartteam.business.Insumos.DownloadInsumos(context, params[0]);
                mx.smartteam.business.Unidad_Medida.DownloadUnidad(context, params[0]);
                
                areas_insumos = mx.smartteam.business.Areas_Insumos.getArea_Insumos(context, params[0]);
                //mx.smartteam.data.Insumos_Answ_Activity.getAnswer(context);
                
            } catch (Exception e) {
                e.getMessage();
            }
            
            return true;
        }
        
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            Adaptador_Area_Insumos adapter = new Adaptador_Area_Insumos(context, R.layout.adaptador_area_insumos, areas_insumos);
            list_item.setAdapter(adapter);
        }
         
     }
     
     
}
