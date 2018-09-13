package mx.smartteam.entities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import java.io.Serializable;
import mx.triplei.R;

public class Opcion implements Serializable{

    public String Nombre;
    public Integer Id;
    public String Titulo;
    public Integer IdProyecto;
    public Boolean Selected = false;
    public String FechaCrea;
    public Integer StatusSync;
    public Long FechaSync=0L;
    public Integer Status = 0;
    public Integer Constestacion=0;   
    public Integer nOpcion=0;
    public Integer Activo;
    //public Integer IdContestacionPreguntaOpcion;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public Integer getIdProyecto() {
        return IdProyecto;
    }

    public void setIdProyecto(Integer IdProyecto) {
        this.IdProyecto = IdProyecto;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean Selected) {
        this.Selected = Selected;
    }

    public String getFechaCrea() {
        return FechaCrea;
    }

    public void setFechaCrea(String FechaCrea) {
        this.FechaCrea = FechaCrea;
    }

    public Integer getStatusSync() {
        return StatusSync;
    }

    public void setStatusSync(Integer StatusSync) {
        this.StatusSync = StatusSync;
    }

    public Long getFechaSync() {
        return FechaSync;
    }

    public void setFechaSync(Long FechaSync) {
        this.FechaSync = FechaSync;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    public Integer getConstestacion() {
        return Constestacion;
    }

    public void setConstestacion(Integer Constestacion) {
        this.Constestacion = Constestacion;
    }

    public Integer getnOpcion() {
        return nOpcion;
    }

    public void setnOpcion(Integer nOpcion) {
        this.nOpcion = nOpcion;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }
    
    
    
    

    public static class AdapterMultiple extends ArrayAdapter<Opcion> {

        private ArrayList<Opcion> items;
        private LayoutInflater inflator;

        public AdapterMultiple(Activity context, ArrayList<Opcion> items) {
            super(context, R.layout.itemlist, items);
            this.items = items;
            inflator = context.getLayoutInflater();


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.itemlist, null);
                holder = new ViewHolder();
                //   holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.chk = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.chk
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton view,
                            boolean isChecked) {
                        int getPosition = (Integer) view.getTag();
                        items.get(getPosition).Selected = view
                                .isChecked();

                    }
                });
                convertView.setTag(holder);
                //convertView.setTag(R.id.title, holder.title);
                convertView.setTag(R.id.checkbox, holder.chk);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.chk.setTag(position);

            //holder.title.setText(items.get(position).Nombre);
            holder.chk.setChecked(items.get(position).Selected);

            holder.chk.setText(items.get(position).Nombre);
            holder.chk.setTextColor(Color.BLACK);
            return convertView;
        }

        static class ViewHolder {

            protected TextView title;
            protected CheckBox chk;
        }

        public OpcionCollection GetItemsSelected() {

            OpcionCollection opcionSelect = new OpcionCollection();
            for (Opcion item : items) {

                if (item.Selected) {
                    opcionSelect.add(item);

                }

            }

            return opcionSelect;
        }
    }

    public static class AdapterSingleSelection extends ArrayAdapter<Opcion> {

        private ArrayList<Opcion> items;
        private LayoutInflater inflator;

        public AdapterSingleSelection(Activity context, ArrayList<Opcion> items) {
            super(context, R.layout.itemlist_1, items);
            this.items = items;
            inflator = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AdapterSingleSelection.ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.itemlist_1, null);
                holder = new AdapterSingleSelection.ViewHolder();
                //   holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.rad = (RadioButton) convertView.findViewById(R.id.radiobutton);
                
                
                holder.rad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton view,
                            boolean isChecked) {
                        int getPosition = (Integer) view.getTag();
                        items.get(getPosition).Selected = view
                                .isChecked();

                    }
                });
                convertView.setTag(holder);
                //convertView.setTag(R.id.title, holder.title);
                convertView.setTag(R.id.radiobutton, holder.rad);
            } else {
                holder = (AdapterSingleSelection.ViewHolder) convertView.getTag();
            }
            holder.rad.setTag(position);

            //holder.title.setText(items.get(position).Nombre);
            holder.rad.setChecked(items.get(position).Selected);

            holder.rad.setText(items.get(position).Nombre);
            return convertView;
        }

        static class ViewHolder {

            protected TextView title;
            protected RadioButton rad;
        }

        public OpcionCollection GetItemsSelected() {

            OpcionCollection opcionSelect = new OpcionCollection();
            for (Opcion item : items) {

                if (item.Selected) {
                    opcionSelect.add(item);

                }

            }

            return opcionSelect;
        }
    }

    public static class Adapter_Multiple extends ArrayAdapter<Opcion> {

        private OpcionCollection items;
        private Context context;
        private LayoutInflater inflater;

        public Adapter_Multiple(Context context, OpcionCollection items) {

            //super(context, R.layout.item_multipleselect, items);
            super(context, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, items);
            this.items = items;
            inflater = LayoutInflater.from(context);// context.getLayoutInflater();
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Opcion myOpcion = this.items.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
            }

            if (myOpcion != null) {
                TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                text.setText(Html.fromHtml("<small>" + myOpcion.Nombre + "</small>"));
                text.setTextColor(Color.BLACK);
               // text.setBackgroundColor(Color.WHITE);
                //text.setTextSize(12);
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {

            Opcion myOpcion = this.items.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            }

            if (myOpcion != null) {

                TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                text.setText(myOpcion.Nombre.trim());
                text.setTextColor(Color.BLACK);
                text.setHeight(60);
            }

            return convertView;
        }
    }
    
    // Implementamos las banderitas que tendran las cosas estas 
    
    public static class Adapter_Combo extends ArrayAdapter<Opcion> {
        private ArrayList<Opcion> items;
        private Context context;
        private LayoutInflater inflater;

        public Adapter_Combo(Context context, int textViewResourceId,ArrayList<Opcion> items) {

            //super(context, R.layout.item_multipleselect, items);
            super(context, R.layout.spinner_item, 1, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);// context.getLayoutInflater();
            
        }
        
        public View getCustomView(int position, View convertView, ViewGroup parent){
        
            View row = inflater.inflate(R.layout.spinner_item, parent, false);
            
            Opcion myOpcion = this.items.get(position);
            
            if(myOpcion != null){
                TextView text = (TextView) row.findViewById(R.id.spinnerItem);
                text.setTextColor(Color.BLACK);
                text.setText(myOpcion.Nombre.trim());
                
                if(myOpcion.Status.equals(1)){
                    ImageView image = (ImageView)row.findViewById(R.id.ic_done);
                    image.setImageResource(R.drawable.ic_done);
                }
            }
            return row;
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
        
            return getCustomView(position, convertView, parent);
        }
        
        
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent){
        
            return getCustomView(position, convertView, parent);
        }
    
    }

    
    public static class AdapterNothing extends ArrayAdapter<Opcion> {
        private ArrayList<Opcion> items;
        private Context context;
        private LayoutInflater inflater;

        public AdapterNothing(Context context, int textViewResourceId,ArrayList<Opcion> items) {

            //super(context, R.layout.item_multipleselect, items);
            super(context, R.layout.spinner_item, 1, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);// context.getLayoutInflater();
            
        }
        
        public View getCustomView(int position, View convertView, ViewGroup parent){
        
            View row = inflater.inflate(R.layout.spinner_item, parent, false);
            
            Opcion myOpcion = this.items.get(position);
            
            if(myOpcion != null){
                TextView text = (TextView) row.findViewById(R.id.spinnerItem);
                text.setTextColor(Color.BLACK);
                text.setText(myOpcion.Nombre.trim());
                
                /*if(myOpcion.Status.equals(1)){
                    ImageView image = (ImageView)row.findViewById(R.id.ic_done);
                    image.setImageResource(R.drawable.ic_done);
                }*/
            }
            return row;
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
        
            return getCustomView(position, convertView, parent);
        }
        
        
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent){
        
            return getCustomView(position, convertView, parent);
        }
    
    }

}
