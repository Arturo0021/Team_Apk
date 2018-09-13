package mx.smartteam.entities;
import mx.triplei.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;

public class Exhibicion implements Serializable{
    public Integer Id;
    public String Nombre;
    public Integer Bandera;
    public Integer Orden;
    
    public static class Adapter extends ArrayAdapter<Exhibicion>{

        private ArrayList<Exhibicion> items = null;
        private Context context;
        private LayoutInflater inflater;

        public Adapter(Context context, int textViewResourceId,ArrayList<Exhibicion> items) {
            super(context, R.layout.spinner_item, 1, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }
        
        public View getCustomView(int position , View convertView, ViewGroup parent ){
            View row = inflater.inflate(R.layout.spinner_item,parent, false);
            Exhibicion myExhibicion = this.items.get(position);
            TextView text = (TextView) row.findViewById(R.id.spinnerItem);
            text.setText(myExhibicion.Nombre.trim());
            text.setTextColor(Color.BLACK);
            if(myExhibicion.Bandera.equals(1)){
                ImageView image = (ImageView) row.findViewById(R.id.ic_done);
                image.setImageResource(R.drawable.ic_done);
            }
            return row;
        }
        
        public View getView(int position, View convertView, ViewGroup parent){
        
            return getCustomView(position, convertView, parent);
        }
        
        public View getDropDownView(int position,View convertView, ViewGroup parent){
        
            return getCustomView(position, convertView, parent);
        }
        
        
    }

    
}
