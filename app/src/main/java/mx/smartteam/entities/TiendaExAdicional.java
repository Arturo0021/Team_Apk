package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class TiendaExAdicional implements Serializable {

    public Integer Cantidad;
    public Integer Folio;
    public String Cadena;
    public String Clasificacion;
    public String Departamento;
    public String Fecha;
    public String Producto;
    public String Promotor;
    public String Sucursal;
    public String Tipo;
    public String Usuario;
    
    public TiendaExAdicional() {
        
    }

    public TiendaExAdicional(Integer Cantidad, Integer Folio, String Cadena, String Clasificacion, String Departamento, String Fecha, String Producto, String Promotor, String Sucursal, String Tipo, String Usuario) {
        this.Cantidad = Cantidad;
        this.Folio = Folio;
        this.Cadena = Cadena;
        this.Clasificacion = Clasificacion;
        this.Departamento = Departamento;
        this.Fecha = Fecha;
        this.Producto = Producto;
        this.Promotor = Promotor;
        this.Sucursal = Sucursal;
        this.Tipo = Tipo;
        this.Usuario = Usuario;
    }

    public Integer getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Integer Cantidad) {
        this.Cantidad = Cantidad;
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

    public String getClasificacion() {
        return Clasificacion;
    }

    public void setClasificacion(String Clasificacion) {
        this.Clasificacion = Clasificacion;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String Departamento) {
        this.Departamento = Departamento;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String Producto) {
        this.Producto = Producto;
    }

    public String getPromotor() {
        return Promotor;
    }

    public void setPromotor(String Promotor) {
        this.Promotor = Promotor;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    
    
}
