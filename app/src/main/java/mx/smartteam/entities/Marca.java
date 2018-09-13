package mx.smartteam.entities;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import mx.triplei.R;

public class Marca implements Serializable{

    public Integer Id;
    public String Nombre;
    public Integer IdCategoria;
    public Integer IdProyecto;
    public Integer StatusSync;
    public Long FechaSync = 0L;
    public Integer bandera;
    public Integer Orden;
    public Integer Max;

   public static class Adapter extends ArrayAdapter<Marca> {

        private ArrayList<Marca> items = null;
        private Context context;
        private LayoutInflater inflater;

        public Adapter(Context context, int textViewResourceId,
                ArrayList<Marca> items) {
            //super(context, android.R.layout.simple_spinner_dropdown_item, items);
            super(context, R.layout.spinner_item, 1, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);
            // inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.spinner_item, parent, false);
            Marca myMarca = this.items.get(position);

            if (myMarca != null) {
                TextView text = (TextView) row.findViewById(R.id.spinnerItem);
                text.setText(myMarca.Nombre.trim());
                text.setTextColor(Color.BLACK);
                
                
                if (myMarca.bandera.equals(1)) {
                    
                    ImageView image=(ImageView)row.findViewById(R.id.ic_done);
                    image.setImageResource(R.drawable.ic_done);
                    //image.setVisibility(View.VISIBLE);
                }
            }
            return row;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*
            Producto myProducto = this.items.get(position);
            if (convertView == null) {
                //convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
                convertView = inflater.inflate(R.layout.spinner_item, null);
            }
            if (myProducto != null) {
                TextView text = (TextView) convertView.findViewById(R.id.spinnerItem);
                text.setText(myProducto.Nombre.trim());
            }*/
            //convertViewx = convertView;
            return getCustomView(position, convertView, parent);

        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {

            /*
            Producto myProducto = null;
            myProducto = this.items.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent);
            }

            if (myProducto != null) {

                TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                text.setText(myProducto.Nombre.trim());

                if (myProducto.bandera.equals(1)) {
                    text.setPadding(0, 0, 10, 0);
                    text.setCompoundDrawablePadding(10);
                    text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
                }
                text.setHeight(60);
            }*/

            return getCustomView(position, convertView, parent);
        }

    }
}
