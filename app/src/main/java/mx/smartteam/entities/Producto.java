package mx.smartteam.entities;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import mx.triplei.R;

public class Producto implements Serializable {

    public Integer Id;
    public Integer IdCategoria;
    public Integer IdMarca;
    public Long SKU;
    public String Nombre;
    public String Descripcion;
    public Double Maximo;
    public Double Minimo;
    public Double Precio;
    public Integer MuestraEnPedido;
    public Integer MuestraEnAnaquel;
    public Integer MuestraEnBodega;
    public Integer MuestraEnExcibicionAdicional;
    public Integer MuestraEnMatPop;
    public Integer MuestraEnFrentesFrio;
    public String StatusTipo;
    public Integer IdProyecto;
    public Integer StatusSync;
    public Long FechaSync = 0L;
    public Integer bandera = 0;
    static View convertViewx = null;
    public String Barcode;
    public Integer activo;
    public Integer orden;
     public Double psugerido;
    public Double ppromocion;

    public static class Adapter extends ArrayAdapter<Producto> {

        private ArrayList<Producto> items = null;
        private Context context;
        private LayoutInflater inflater;

        //mx.smartteam.business.Contestacion answer = new mx.smartteam.business.Contestacion();

        public Adapter(Context context, int textViewResourceId, ArrayList<Producto> items) {
            super(context, R.layout.spinner_item, 1, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);

        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.spinner_item, parent, false);
            Producto myProducto = this.items.get(position);

            if (myProducto != null) {
                TextView text = (TextView) row.findViewById(R.id.spinnerItem);
                text.setText(myProducto.Nombre.trim());
                text.setTextColor(Color.BLACK);
                
                
                if (myProducto.bandera.equals(1)) {
                    
                    ImageView image=(ImageView)row.findViewById(R.id.ic_done);
                    image.setImageResource(R.drawable.ic_done);
                }
            }
            return row;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);

        }


        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {

          

            return getCustomView(position, convertView, parent);
        }

    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(Integer IdCategoria) {
        this.IdCategoria = IdCategoria;
    }

    public Integer getIdMarca() {
        return IdMarca;
    }

    public void setIdMarca(Integer IdMarca) {
        this.IdMarca = IdMarca;
    }

    public Long getSKU() {
        return SKU;
    }

    public void setSKU(Long SKU) {
        this.SKU = SKU;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Double getMaximo() {
        return Maximo;
    }

    public void setMaximo(Double Maximo) {
        this.Maximo = Maximo;
    }

    public Double getMinimo() {
        return Minimo;
    }

    public void setMinimo(Double Minimo) {
        this.Minimo = Minimo;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double Precio) {
        this.Precio = Precio;
    }

    public Integer getMuestraEnPedido() {
        return MuestraEnPedido;
    }

    public void setMuestraEnPedido(Integer MuestraEnPedido) {
        this.MuestraEnPedido = MuestraEnPedido;
    }

    public Integer getMuestraEnAnaquel() {
        return MuestraEnAnaquel;
    }

    public void setMuestraEnAnaquel(Integer MuestraEnAnaquel) {
        this.MuestraEnAnaquel = MuestraEnAnaquel;
    }

    public Integer getMuestraEnBodega() {
        return MuestraEnBodega;
    }

    public void setMuestraEnBodega(Integer MuestraEnBodega) {
        this.MuestraEnBodega = MuestraEnBodega;
    }

    public Integer getMuestraEnExcibicionAdicional() {
        return MuestraEnExcibicionAdicional;
    }

    public void setMuestraEnExcibicionAdicional(Integer MuestraEnExcibicionAdicional) {
        this.MuestraEnExcibicionAdicional = MuestraEnExcibicionAdicional;
    }

    public Integer getMuestraEnMatPop() {
        return MuestraEnMatPop;
    }

    public void setMuestraEnMatPop(Integer MuestraEnMatPop) {
        this.MuestraEnMatPop = MuestraEnMatPop;
    }

    public Integer getMuestraEnFrentesFrio() {
        return MuestraEnFrentesFrio;
    }

    public void setMuestraEnFrentesFrio(Integer MuestraEnFrentesFrio) {
        this.MuestraEnFrentesFrio = MuestraEnFrentesFrio;
    }

    public String getStatusTipo() {
        return StatusTipo;
    }

    public void setStatusTipo(String StatusTipo) {
        this.StatusTipo = StatusTipo;
    }

    public Integer getIdProyecto() {
        return IdProyecto;
    }

    public void setIdProyecto(Integer IdProyecto) {
        this.IdProyecto = IdProyecto;
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

    public Integer getBandera() {
        return bandera;
    }

    public void setBandera(Integer bandera) {
        this.bandera = bandera;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Double getPsugerido() {
        return psugerido;
    }

    public void setPsugerido(Double psugerido) {
        this.psugerido = psugerido;
    }

    public Double getPpromocion() {
        return ppromocion;
    }

    public void setPpromocion(Double ppromocion) {
        this.ppromocion = ppromocion;
    }
    
    
    
    

}
