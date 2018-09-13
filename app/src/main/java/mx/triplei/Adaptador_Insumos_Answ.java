package mx.triplei;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import mx.triplei.R;

/**
 *
 * @author ivan.guerra
 */
public class Adaptador_Insumos_Answ extends BaseAdapter{
    
    private Context context;
    private int layout;
    private List<mx.smartteam.entities.Insumos> insumos;
    
    public Adaptador_Insumos_Answ(Context context, int layout, List<mx.smartteam.entities.Insumos> insumos) {
        this.context = context;
        this.layout = layout;
        this.insumos = insumos;
    }

    public int getCount() {
        return this.insumos.size();
    }

    public Object getItem(int position) {
        return this.insumos.get(position);
    }

    public long getItemId(int Id) {
        return Id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
       
        View view = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.adaptador_insumos_answ, null);
        mx.smartteam.entities.Insumos ins = insumos.get(position);
        
        LinearLayout linear_layout_p = (LinearLayout)view.findViewById(R.id.linear_layout_p);
        View view_p = (View)view.findViewById(R.id.view_p);
        TextView text_insumo = (TextView)view.findViewById(R.id.text_insumo);
        TextView text_respuesta = (TextView)view.findViewById(R.id.text_respuesta);
        TextView text_fecha = (TextView)view.findViewById(R.id.text_fecha);
        ImageView image_view = (ImageView)view.findViewById(R.id.image_view);
        
        text_insumo.setText(ins.getNombre());
        if(ins.getRespuesta() == null) {
            text_respuesta.setText("");
        } else {
            text_respuesta.setText(ins.getRespuesta() + " " + ins.getAbreviatura());
        }
        
        text_fecha.setText(ins.getFecha());
        image_view.setBackgroundColor(Color.parseColor(ins.getColor()));
        linear_layout_p.setBackgroundColor(Color.parseColor(ins.getColor()));
        if(ins.getEstatus() == 1) {
            image_view.setImageResource(R.drawable.flagsync);
        }else{
            image_view.setImageResource(R.drawable.flagnosync);
        }
        
        return view;
    }
    
}
