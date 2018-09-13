
package mx.triplei;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import mx.triplei.R;

public class SubMenu extends Activity {

    ArrayList<mx.smartteam.entities.TiendasConfig> ConfigCollection;
    ArrayList<mx.smartteam.entities.TiendaAsistencia> Asistenciacollection;
    ArrayList<mx.smartteam.entities.TiendaSos> SosCollection;
    ArrayList<mx.smartteam.entities.TiendaFrentes> FrentesCollection;
    ArrayList<mx.smartteam.entities.TiendaExAdicional> ExAdicCollection;   
     
    Context context;
    
     public int[]ImagenesId;
     public String[]modulo;
  
    GridView gridview;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.submenu_activity);
        context = this;
   
        gridview = (GridView)findViewById(R.id.gridview);  

        try {
            
            ConfigCollection = (ArrayList<mx.smartteam.entities.TiendasConfig>)getIntent().getSerializableExtra("ConfigCollection");
            Asistenciacollection = (ArrayList<mx.smartteam.entities.TiendaAsistencia>)getIntent().getSerializableExtra("Asistenciacollection");
            SosCollection = (ArrayList<mx.smartteam.entities.TiendaSos>)getIntent().getSerializableExtra("SosCollection");
            FrentesCollection = (ArrayList<mx.smartteam.entities.TiendaFrentes>)getIntent().getSerializableExtra("FrentesCollection");
            ExAdicCollection = (ArrayList<mx.smartteam.entities.TiendaExAdicional>)getIntent().getSerializableExtra("ExAdicCollection");
            
            ImagenesId = new int[ConfigCollection.size()]; // Este Objeto Guarda las Imagenes
            modulo = new String[ConfigCollection.size()]; // Este Objeto Guarda un Identificador
            
            gridview.setAdapter(new ImageAdapter(this)); 
            
            int  a = 1;
            for(int i = 0; i < ConfigCollection.size(); i++) {
                
                ImagenesId[0] = R.drawable.todo2;
                modulo[0] = "general";
                
                if(ConfigCollection.get(i).tipo.equalsIgnoreCase("ASISTENCIA") && ConfigCollection.get(i).Estatus == 1) {
                    ImagenesId[a] = R.drawable.asistencia3;
                    modulo[a] = "asistencia";
                }
                if(ConfigCollection.get(i).tipo.equalsIgnoreCase("SOS") && ConfigCollection.get(i).Estatus == 1) {
                    ImagenesId[a] = R.drawable.sos2;
                    modulo[a] = "sos";
                }
                if(ConfigCollection.get(i).tipo.equalsIgnoreCase("FRENTES EN ANAQUEL") && ConfigCollection.get(i).Estatus == 1) {
                    ImagenesId[a] = R.drawable.anaquel2;
                    modulo[a] = "frentes";
                }
                if(ConfigCollection.get(i).tipo.equalsIgnoreCase("EXHIBICION ADICIONAL") && ConfigCollection.get(i).Estatus == 1) {
                    ImagenesId[a] = R.drawable.exadic2;
                    modulo[a] = "exadic";
                }
  
                a ++;
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }
 
    
     public class ImageAdapter extends BaseAdapter{ 
            public ImageAdapter(Context c){
                
            } 
            public int getCount() {
                return ImagenesId.length;
            }
            public Object getItem(int position) {
                return position; 
            } 
            public long getItemId(int position) { 
                return position;
            } 
            public View getView(final int position, View convertView, ViewGroup parent) { 
                
                    ImageView imageView; 
                    
                    if(convertView==null){
                        imageView = new ImageView(context);  
                        imageView.setLayoutParams(new GridView.LayoutParams(230,200));
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); 
                        imageView.setPadding(10,10, 10,10); 
                    }else{
                        imageView = (ImageView)convertView;
                    }
                    
                    imageView.setImageResource(ImagenesId[position]);
                    
                    imageView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) { 
                            
                            Intent intent = new Intent(SubMenu.this, FichaTecnica.class);
                            
                            if(modulo[position].equalsIgnoreCase("general")) {
                                intent.putExtra("Asistenciacollection", Asistenciacollection);
                                intent.putExtra("SosCollection", SosCollection);
                                intent.putExtra("FrentesCollection", FrentesCollection);
                                intent.putExtra("ExAdicCollection", ExAdicCollection);
                            } else if(modulo[position].equalsIgnoreCase("asistencia")) {
                                intent.putExtra("Asistenciacollection", Asistenciacollection);
                            } else if(modulo[position].equalsIgnoreCase("sos")) {
                                intent.putExtra("Asistenciacollection", Asistenciacollection);
                                intent.putExtra("SosCollection", SosCollection);
                            } else if(modulo[position].equalsIgnoreCase("frentes")) {
                                intent.putExtra("Asistenciacollection", Asistenciacollection);
                                intent.putExtra("FrentesCollection", FrentesCollection);
                            } else if(modulo[position].equalsIgnoreCase("exadic")) {
                                intent.putExtra("Asistenciacollection", Asistenciacollection);
                                intent.putExtra("ExAdicCollection", ExAdicCollection);
                            }
                        
                            intent.putExtra("identificador", modulo[position]);
                            startActivity(intent);
                        }
                    });
                    
                    return imageView; 
            }
        }


        public class ImageGalery extends BaseAdapter{
            
            public ImageGalery(Context c) { 
                
            }
            public int getCount() { 
                return ImagenesId.length;
            }
            public Object getItem(int position) {
                return position; 
            }
            public long getItemId(int position) { 
                return position; 
            }
            public View getView(
                int position, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(ImagenesId[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        }
    
    
}
