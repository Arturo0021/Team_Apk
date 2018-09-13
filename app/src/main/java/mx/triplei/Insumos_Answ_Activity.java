package mx.triplei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import mx.triplei.R;

/**
 *
 * @author ivan.guerra
 */
public class Insumos_Answ_Activity extends Activity{

    Context context;
    mx.smartteam.entities.Proyecto proyectos;
    mx.smartteam.entities.Areas_Insumos areas_insumos;
    mx.smartteam.entities.InsumosCollection insumos_collection;
    mx.smartteam.entities.Usuario currentUsuario;
    mx.smartteam.entities.Pop currentPop;
    public static int var_i = 0;
    ListView list_insumositem;
    
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.insumos_answ_activity);
        context = this;
        proyectos = (mx.smartteam.entities.Proyecto)getIntent().getSerializableExtra("proyectos");
        areas_insumos = (mx.smartteam.entities.Areas_Insumos)getIntent().getSerializableExtra("areas_insumos");
        currentUsuario = (mx.smartteam.entities.Usuario) getIntent().getSerializableExtra("usuario");
        currentPop = (mx.smartteam.entities.Pop) getIntent().getSerializableExtra("pop");
        
        
        list_insumositem = (ListView)findViewById(R.id.list_insumositem);
        
        
        try {
            
            insumos_collection = mx.smartteam.business.Insumos.getInsumos(context, proyectos, areas_insumos, currentUsuario, currentPop);
            Adaptador_Insumos_Answ adapter = new Adaptador_Insumos_Answ(context, R.layout.adaptador_insumos_answ, insumos_collection);
            list_insumositem.setAdapter(adapter);
            
            list_insumositem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                    //Toast.makeText(context, insumos_collection.get(position).Nombre, Toast.LENGTH_LONG).show();
                    double minimo = insumos_collection.get(position).ValorMin; // no tocar este se itera
                    double valorMin = insumos_collection.get(position).ValorMin; // este mantiene el valor principal
                    double maximo = insumos_collection.get(position).ValorMax;
                    double salto  = insumos_collection.get(position).Salto;
                    final int idConfig = insumos_collection.get(position).IdConfig;
                    final String av = insumos_collection.get(position).Abreviatura;
                    boolean varbreak = false;
                    int numberObject = 1;
                    
                    //final int val_var_i = var_i;
                    
                    while (!varbreak) {
                        if(minimo == maximo) {
                            varbreak = true;
                        } else if(minimo > maximo){
                            varbreak = true;
                        } else {
                            numberObject ++;
                            minimo = minimo + salto;
                        }
                    }
                    
                    final String object[] = new String[numberObject];
                    final String objectsaved[] = new String[numberObject];
                    for(int i = 0; i < numberObject; i++){
                        object[i] = "" + valorMin + " " + av;
                        objectsaved[i] = "" + valorMin;
                        valorMin = valorMin + salto;
                    }
                      
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Dialog);
                    builder.setTitle("CANTIDAD UTILIZADA");
                    
                    builder.setSingleChoiceItems(object, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            var_i = i;
                        }
                    });
                    
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            
                            // Aqui Debes Guardar Toda La Informacion 
                            try {
                                
                                mx.smartteam.business.Insumos_Answ_Activity.InsertRespuestas(context, insumos_collection.get(position), currentUsuario, currentPop, objectsaved[var_i]);
                                executeAdapterLoad();
                                
                            } catch(Exception e) {
                                e.getMessage();
                            }
                            
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                
                }
                
            });
           
 
        } catch(Exception e) {
            e.getMessage();
        }
        
     }
    
    public void executeAdapterLoad() {
        
         try {
            
            insumos_collection = mx.smartteam.business.Insumos.getInsumos(context, proyectos, areas_insumos, currentUsuario, currentPop);
            Adaptador_Insumos_Answ adapter = new Adaptador_Insumos_Answ(context, R.layout.adaptador_insumos_answ, insumos_collection);
            list_insumositem.setAdapter(adapter);
 
        } catch(Exception e) {
            e.getMessage();
        }
        
    }
       @Override
    protected void onResume() { 
        super.onResume();
            
       executeAdapterLoad();
        
    }
    
}
