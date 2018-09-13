package mx.smartteam.entities;

import java.io.Serializable;
import mx.smartteam.exceptions.PopException.NoExiste;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import mx.triplei.R;

public class Pop implements Serializable{

    /* propiedades privadas */
    private Context context;

    /* propiedades publicas */
    public Integer Id;
    public Integer DeterminanteGSP;
    public Integer DeterminanteTienda;
    public Integer Facturacion;
    public Integer IdCanal;
    public Integer IdGrupo;
    public Integer IdCadena;
    public Integer IdFormato;
    public String Sucursal;
    public String Direccion;
    public Integer CP;
    public Integer IdPais;
    public Integer IdEstado;
    public Integer IdMunicipio;
    public Integer IdCiudad;
    public String Telefonos;
    public Double Latitud;
    public Double Longitud;
    public Double Altitud;
    public Integer Activo;
    public String Calle;
    public String Numero;
    public String Colonia;
    public Integer NuevoPunto;
    public Double RangoGPS;
    public String Nielsen;
    public String Cadena;
    public Integer StatusSync;
    public Long FechaSync=0L;
    //public String Nombre;
    public Integer Estatus;
    public Integer NumFotos;
    public Integer IdProyecto;
    public Integer IdUsuario;
    public Integer Ractiva;

    //External realation
    public Integer IdVisita;
    public Integer IdStatus;
    public Long Fecha=0L;
    
    
    
    public Pop(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public Pop() {
    }

    /* Metodos */
    public String Buscar(int IdProyecto, int IdUsuario) throws NoExiste {
        String result = "";
        /*

         String responts = ServiceClient.GetStringHttp(String.format(
         "%s?idProyecto=%s&idFolio=%s&idUsuario=%s",
         Ajustes.Url_ValidaTienda, IdProyecto, this.DeterminanteGSP,
         IdUsuario));

         if (responts.equals("BAD")) {
         throw new PopException.NoExiste();
         }

         ObtieneInformacion(responts);*/

        return result;
    }

    // Metodo que envia el registro de visita en la tienda
    public void Enviar(int IdProyecto, int IdUsuario) {
        StringBuilder strParamns = new StringBuilder();
        strParamns.append(String.format("?idProyecto=%s&", IdProyecto));
        strParamns.append(String.format("determinanteGPS=%s&",
                this.DeterminanteGSP));
        strParamns.append(String.format("idUsuario=%s&", IdUsuario));
        strParamns.append(String.format("latitud=%s&", this.Latitud));
        strParamns.append(String.format("longitud=%s&", this.Longitud));
        strParamns.append(String.format("idStatus=%s&", 1));
        strParamns.append(String.format("num_fotos=%s", 1));

        /*String responts = ServiceClient.GetStringHttp(Ajustes.Url_EnviaTienda
         + strParamns.toString());

         if (responts.trim().equals("OK")) {
         Toast.makeText(this.context, "Informaci�n registrada",
         Toast.LENGTH_LONG).show();
         }*/

    }

    private void ObtieneInformacion(String responts) {
        if (!responts.isEmpty()) {
            String[] separated = responts.split("\\|");

            if (separated != null && separated[0].equals("OK")) {

                this.Id = Integer.parseInt(separated[1]);
                this.Sucursal = separated[2];
                this.Activo = Integer.parseInt(separated[3]);
                this.Latitud = Double.parseDouble(separated[5]);
                this.Longitud = Double.parseDouble(separated[6]);
                this.Direccion = separated[7];
            }

            /*
             * for (String item : separated) { System.out.println("item = " +
             * item); }
             */

        }
    }

    public static class AdapterItemsIco extends ArrayAdapter<Pop> {

        private PopCollection items;
        private Context context;

        public AdapterItemsIco(Context context, PopCollection items) {
            super(context, R.layout.itemlisticon, items);
            this.items = items;
            this.context = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(this.context).inflate(
                    R.layout.itemlisticon, null);

            Pop pop = this.items.get(position);
            if (pop != null) {
                ImageView imgIcon = (ImageView) view.findViewById(R.id.itemicon);
                TextView Text = (TextView) view.findViewById(R.id.text);

                
                Text.setText(" " + pop.Cadena + "  "+pop.Sucursal + " - " + pop.DeterminanteGSP);
                Text.setTextColor(Color.BLACK);
                
                switch (pop.IdStatus) {
                    case 0://No visitada
                        
                        if(pop.Ractiva == 0){
                            imgIcon.setImageResource(R.drawable.ch_desactivada_visitar);
                            Text.setTextColor(Color.GRAY);
                        }else{
                            imgIcon.setImageResource(R.drawable.ch_sin_visitar);
                            Text.setTextColor(Color.BLACK);
                        }
                        
                        
                        
                        break;

                    case 1://En visita
                        imgIcon.setImageResource(R.drawable.ch_en_visita);
                        Text.setTextColor(Color.BLACK);
                        break;

                    case 2://Visitada
                        imgIcon.setImageResource(R.drawable.ch_tienda_visitada);
                        Text.setTextColor(Color.BLACK);
                        break;
                }

                
            }

            return view;
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getDeterminanteGSP() {
        return DeterminanteGSP;
    }

    public void setDeterminanteGSP(Integer DeterminanteGSP) {
        this.DeterminanteGSP = DeterminanteGSP;
    }

    public Integer getDeterminanteTienda() {
        return DeterminanteTienda;
    }

    public void setDeterminanteTienda(Integer DeterminanteTienda) {
        this.DeterminanteTienda = DeterminanteTienda;
    }

    public Integer getFacturacion() {
        return Facturacion;
    }

    public void setFacturacion(Integer Facturacion) {
        this.Facturacion = Facturacion;
    }

    public Integer getIdCanal() {
        return IdCanal;
    }

    public void setIdCanal(Integer IdCanal) {
        this.IdCanal = IdCanal;
    }

    public Integer getIdGrupo() {
        return IdGrupo;
    }

    public void setIdGrupo(Integer IdGrupo) {
        this.IdGrupo = IdGrupo;
    }

    public Integer getIdCadena() {
        return IdCadena;
    }

    public void setIdCadena(Integer IdCadena) {
        this.IdCadena = IdCadena;
    }

    public Integer getIdFormato() {
        return IdFormato;
    }

    public void setIdFormato(Integer IdFormato) {
        this.IdFormato = IdFormato;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public Integer getCP() {
        return CP;
    }

    public void setCP(Integer CP) {
        this.CP = CP;
    }

    public Integer getIdPais() {
        return IdPais;
    }

    public void setIdPais(Integer IdPais) {
        this.IdPais = IdPais;
    }

    public Integer getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(Integer IdEstado) {
        this.IdEstado = IdEstado;
    }

    public Integer getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(Integer IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public Integer getIdCiudad() {
        return IdCiudad;
    }

    public void setIdCiudad(Integer IdCiudad) {
        this.IdCiudad = IdCiudad;
    }

    public String getTelefonos() {
        return Telefonos;
    }

    public void setTelefonos(String Telefonos) {
        this.Telefonos = Telefonos;
    }

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double Latitud) {
        this.Latitud = Latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double Longitud) {
        this.Longitud = Longitud;
    }

    public Double getAltitud() {
        return Altitud;
    }

    public void setAltitud(Double Altitud) {
        this.Altitud = Altitud;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String Numero) {
        this.Numero = Numero;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String Colonia) {
        this.Colonia = Colonia;
    }

    public Integer getNuevoPunto() {
        return NuevoPunto;
    }

    public void setNuevoPunto(Integer NuevoPunto) {
        this.NuevoPunto = NuevoPunto;
    }

    public Double getRangoGPS() {
        return RangoGPS;
    }

    public void setRangoGPS(Double RangoGPS) {
        this.RangoGPS = RangoGPS;
    }

    public String getNielsen() {
        return Nielsen;
    }

    public void setNielsen(String Nielsen) {
        this.Nielsen = Nielsen;
    }

    public String getCadena() {
        return Cadena;
    }

    public void setCadena(String Cadena) {
        this.Cadena = Cadena;
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

    public Integer getEstatus() {
        return Estatus;
    }

    public void setEstatus(Integer Estatus) {
        this.Estatus = Estatus;
    }

    public Integer getNumFotos() {
        return NumFotos;
    }

    public void setNumFotos(Integer NumFotos) {
        this.NumFotos = NumFotos;
    }

    public Integer getIdProyecto() {
        return IdProyecto;
    }

    public void setIdProyecto(Integer IdProyecto) {
        this.IdProyecto = IdProyecto;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public Integer getRactiva() {
        return Ractiva;
    }

    public void setRactiva(Integer Ractiva) {
        this.Ractiva = Ractiva;
    }

    public Integer getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(Integer IdVisita) {
        this.IdVisita = IdVisita;
    }

    public Integer getIdStatus() {
        return IdStatus;
    }

    public void setIdStatus(Integer IdStatus) {
        this.IdStatus = IdStatus;
    }

    public Long getFecha() {
        return Fecha;
    }

    public void setFecha(Long Fecha) {
        this.Fecha = Fecha;
    }
    
    
    
    
    
}
