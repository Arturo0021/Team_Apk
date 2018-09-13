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
public class Adapter_Frentes_Anaquel extends BaseAdapter{
    
    private Context context;
    private int layout;
    private List<mx.smartteam.entities.TiendaFrentes> frentes;
    
    public Adapter_Frentes_Anaquel(Context context, int layout, List<mx.smartteam.entities.TiendaFrentes> frentes) {
        this.context = context;
        this.layout = layout;
        this.frentes = frentes;
    }

    public int getCount() {
        return frentes.size();
    }

    public Object getItem(int position) {
        return frentes.get(position);
    }

    public long getItemId(int Id) {
        return Id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.adapter_frentes_anaquel, null);
        
        mx.smartteam.entities.TiendaFrentes fr = frentes.get(position);
        
        TextView t_texto1 = (TextView)v.findViewById(R.id.t_texto1);
        TextView t_texto2 = (TextView)v.findViewById(R.id.t_texto2);
        TextView t_texto3 = (TextView)v.findViewById(R.id.t_texto3);
        TextView t_texto4 = (TextView)v.findViewById(R.id.t_texto4);
        TextView t_texto5 = (TextView)v.findViewById(R.id.t_texto5);
        TextView t_texto6 = (TextView)v.findViewById(R.id.t_texto6);
        TextView t_texto7 = (TextView)v.findViewById(R.id.t_texto7);
        TextView t_texto8 = (TextView)v.findViewById(R.id.t_texto8);
        
        t_texto1.setText(fr.getMarca());
        t_texto2.setText(fr.getProducto());
        t_texto3.setText("" + fr.getCantAnaquel());
        t_texto4.setText("" + fr.getManos());
        t_texto5.setText("" + fr.getOjo());
        t_texto6.setText("" + fr.getSuelo());
        t_texto7.setText("" + fr.getTecho());
        t_texto8.setText(fr.getComentarioAnaquel());
        
        return v;
    }
    
    
    
    
}
