package mx.triplei;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import mx.triplei.R;
/**
 *
 * @author ivan.guerra
 */
public class Adaptador_Area_Insumos extends BaseAdapter {

    private Context context;
    private int layout;
    private List<mx.smartteam.entities.Areas_Insumos> area_insumos;
    
    public Adaptador_Area_Insumos(Context context, int layout, List<mx.smartteam.entities.Areas_Insumos> area_insumos) {
        this.context = context;
        this.layout = layout;
        this.area_insumos = area_insumos;
    }
    
    public int getCount() {
        return this.area_insumos.size();
    }

    public Object getItem(int position) {
        return this.area_insumos.get(position);
    }

    public long getItemId(int Id) {
        return Id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        View view = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.adaptador_area_insumos, null);
        mx.smartteam.entities.Areas_Insumos area_ins = area_insumos.get(position);
        
        View view_color = (View)view.findViewById(R.id.view_color);
        View view_color_down = (View)view.findViewById(R.id.view_color_down);
        TextView txt_area = (TextView)view.findViewById(R.id.txt_area);
        TextView txt_texto = (TextView)view.findViewById(R.id.txt_texto);
        ImageView image_view = (ImageView)view.findViewById(R.id.image_view);
        
        
        txt_area.setText(area_ins.getNombre());
        view_color.setBackgroundColor(Color.parseColor(area_ins.getColor()));
        view_color_down.setBackgroundColor(Color.parseColor(area_ins.getColor()));
        txt_texto.setText(area_ins.getDescripcion());
        /*if(area_ins.get){
        
            image_view.setImageResource(R.drawable.flagsync);
            
        }*/
        
        return view;
    }
    
}
