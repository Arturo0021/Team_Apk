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
public class Adaptador_Asistencia extends BaseAdapter{
    
    private Context context;
    private int layout;
    private List<mx.smartteam.entities.TiendaAsistencia> asistencia;
    
    public Adaptador_Asistencia(Context context, int layout, List<mx.smartteam.entities.TiendaAsistencia> asistencia) {
        this.context = context;
        this.layout = layout;
        this.asistencia = asistencia;
    }

    public int getCount() {
        return asistencia.size();
    }

    public Object getItem(int position) {
        return asistencia.get(position);
    }

    public long getItemId(int Id) {
        return Id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.adapter_asistencia, null);
        
        mx.smartteam.entities.TiendaAsistencia as = asistencia.get(position);
        
        TextView txt_texto1 = (TextView)v.findViewById(R.id.txt_texto1);
        TextView txt_texto2 = (TextView)v.findViewById(R.id.txt_texto2);
        TextView txt_texto3 = (TextView)v.findViewById(R.id.txt_texto3);
        TextView txt_texto4 = (TextView)v.findViewById(R.id.txt_texto4);
        TextView txt_texto5 = (TextView)v.findViewById(R.id.txt_texto5);
        TextView txt_texto6 = (TextView)v.findViewById(R.id.txt_texto6);
        TextView txt_texto7 = (TextView)v.findViewById(R.id.txt_texto7);
        TextView txt_texto8 = (TextView)v.findViewById(R.id.txt_texto8);
        
        txt_texto1.setText(as.getCadena());
        txt_texto2.setText(as.getSucursal());
        txt_texto3.setText(as.getFolio());
        txt_texto4.setText(as.getPromotor());
        txt_texto5.setText(as.getUsuario());
        txt_texto6.setText(as.getFecha());
        txt_texto7.setText(as.getSalida());
        txt_texto8.setText(as.getTrayectoria());
        
        return v;
        
    }
    
}
