package mx.triplei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;
import mx.triplei.R;

/**
 *
 * @author ivan.guerra
 */
public class Adaptador_Sos extends BaseAdapter{

    private Context context;
    private int layout;
    private List<mx.smartteam.entities.TiendaSos> sos;
    private Double totalsos;
    
    public Adaptador_Sos(Context context, int layout, List<mx.smartteam.entities.TiendaSos> sos, Double totalsos) {
        this.context = context;
        this.layout = layout;
        this.sos = sos;
        this.totalsos = totalsos;
    }
    
    public int getCount() {
        return sos.size();
    }

    public Object getItem(int position) {
        return sos.get(position);
    }

    public long getItemId(int Id) {
        return Id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.adapter_list_sos, null);
        
        mx.smartteam.entities.TiendaSos s = sos.get(position);
        double res = 0.0F;
        //TextView txt_titulo = (TextView)v.findViewById(R.id.txt_titulo);
        TextView txt_texto1 = (TextView)v.findViewById(R.id.txt_texto1);
        TextView txt_texto2 = (TextView)v.findViewById(R.id.txt_texto2);
        TextView txt_texto3 = (TextView)v.findViewById(R.id.txt_texto3);
        TextView txt_texto4 = (TextView)v.findViewById(R.id.txt_texto4);
        TextView txt_texto5 = (TextView)v.findViewById(R.id.txt_texto5);
        TextView txt_texto6 = (TextView)v.findViewById(R.id.txt_texto6);
        
        res = Double.parseDouble(s.getValor()) * 100 / totalsos;
        DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00");
        res = new Double(df2.format(res)).doubleValue();   
        
        txt_texto1.setText(s.getCategoria());
        txt_texto2.setText(s.getMarca());
        txt_texto3.setText(s.getClasificacion());
        txt_texto4.setText(s.getFecha());
        txt_texto5.setText(s.getValor());
        txt_texto6.setText("" + res + " %");
        
        
        return v;
    }
    
}
