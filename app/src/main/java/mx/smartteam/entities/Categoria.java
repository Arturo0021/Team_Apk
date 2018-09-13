package mx.smartteam.entities;

import android.app.Activity;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.io.Serializable;

public class Categoria implements Serializable{

    public enum Type {

        Producto, Foto
    }
    public Integer Id;
    public String Nombre;
    public Boolean Selected;
    public Integer IdProyecto;
    public Type Tipo;
    public Integer StatusSync;
    public Long FechaSync=0L;
    public Integer Config;
    public Integer Activo;

    public static class Adapter extends ArrayAdapter<Categoria> {

        private ArrayList<Categoria> items;
        private Context context;
        private LayoutInflater inflater;

        public Adapter(Context context, int textViewResourceId,
                ArrayList<Categoria> items) {
            super(context, android.R.layout.simple_spinner_dropdown_item, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Categoria myCategoria = this.items.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
                
            }
            if (myCategoria != null) {
                TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                text.setText(myCategoria.Nombre.trim());
                text.setTextColor(Color.BLACK);
                
            }

            return convertView;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {

            Categoria myCategoria = this.items.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            }

            if (myCategoria != null) {

                TextView text = (TextView) convertView.findViewById(android.R.id.text1);

                text.setText(Html.fromHtml("<small>" + myCategoria.Nombre.trim() + "</small>"));
                text.setHeight(60);
                
            }

            return convertView;
        }
    }

    public static class Adapter_Single extends ArrayAdapter<Categoria> {

        private CategoriaCollection items;
        //private Context context;
        private LayoutInflater inflater;
        //private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();

        public Adapter_Single(Context context, CategoriaCollection items) {
            super(context, android.R.layout.simple_list_item_single_choice, android.R.id.text1, items);
            this.items = items;
            inflater = LayoutInflater.from(context);// context.getLayoutInflater();            

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Categoria myCategoria = this.items.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_single_choice, null);
            }

            if (myCategoria != null) {
                TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                text.setText(items.get(position).Nombre.trim());
                text.setTextColor(Color.WHITE);
               // text.setText("Prueba");
               // text.setBackgroundColor(Color.WHITE);//CAMBIAR A WHITE

            }
            return convertView;
        }
    }
}
