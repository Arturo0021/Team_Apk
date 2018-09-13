package mx.triplei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import mx.triplei.R;

/**
 *
 * @author ivan.guerra
 */
public class Adaptador_ExAdicional extends BaseAdapter{

    private Context context;
    private int layout;
    private List<mx.smartteam.entities.TiendaExAdicional> exadic;
    
    public Adaptador_ExAdicional(Context context, int layout, List<mx.smartteam.entities.TiendaExAdicional> exadic) {
        this.context = context;
        this.layout = layout;
        this.exadic = exadic;
    }
    
    public int getCount() {
        return exadic.size();
    }

    public Object getItem(int position) {
        return exadic.get(position);
    }

    public long getItemId(int Id) {
        return Id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.adapter_exadicional, null);
        
        mx.smartteam.entities.TiendaExAdicional ex = exadic.get(position);
        
        TextView t_text1 = (TextView)v.findViewById(R.id.t_text1);
        TextView t_text2 = (TextView)v.findViewById(R.id.t_text2);
        TextView t_text3 = (TextView)v.findViewById(R.id.t_text3);
        TextView t_text4 = (TextView)v.findViewById(R.id.t_text4);
        TextView t_fecha = (TextView)v.findViewById(R.id.t_fecha);
        
        t_text1.setText(ex.getDepartamento());
        t_text2.setText(ex.getProducto());
        t_fecha.setText(ex.getFecha());
        t_text3.setText(ex.getTipo());
        t_text4.setText("" + ex.getCantidad());
        
        return v;
        
    }
    
}
