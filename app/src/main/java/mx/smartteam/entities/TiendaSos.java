package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class TiendaSos implements Serializable {

    public Integer Folio;
    public String Cadena;
    public String Categoria;
    public String Clasificacion;
    public String Fecha;
    public String Marca;
    public String Sucursal;
    public String Valor;
    public String Usuario;
    public String Promotor;

    public TiendaSos() {
        
    }

    public TiendaSos(Integer Folio, String Cadena, String Categoria, String Clasificacion, String Fecha, String Marca, String Sucursal, String Valor, String Usuario, String Promotor) {
        this.Folio = Folio;
        this.Cadena = Cadena;
        this.Categoria = Categoria;
        this.Clasificacion = Clasificacion;
        this.Fecha = Fecha;
        this.Marca = Marca;
        this.Sucursal = Sucursal;
        this.Valor = Valor;
        this.Usuario = Usuario;
        this.Promotor = Promotor;
    }

    public Integer getFolio() {
        return Folio;
    }

    public void setFolio(Integer Folio) {
        this.Folio = Folio;
    }

    public String getCadena() {
        return Cadena;
    }

    public void setCadena(String Cadena) {
        this.Cadena = Cadena;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public String getClasificacion() {
        return Clasificacion;
    }

    public void setClasificacion(String Clasificacion) {
        this.Clasificacion = Clasificacion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String Valor) {
        this.Valor = Valor;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getPromotor() {
        return Promotor;
    }

    public void setPromotor(String Promotor) {
        this.Promotor = Promotor;
    }
    
   
    
}
