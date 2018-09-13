package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class TiendaFrentes implements Serializable {

    public Integer CantAnaquel;
    public Integer Folio;
    public Integer Manos;
    public Integer Ojo;
    public Integer Suelo;
    public Integer Techo;
    public String Cadena;
    public String Categoria;
    public String Clasificacion;
    public String ComentarioAnaquel;
    public String Fecha;
    public String Marca;
    public String Producto;
    public String Sucursal;

    public TiendaFrentes() {
        
    }
    
    public TiendaFrentes(Integer CantAnaquel, Integer Folio, Integer Manos, Integer Ojo, Integer Suelo, Integer Techo, String Cadena, String Categoria, String Clasificacion, String ComentarioAnaquel, String Fecha, String Marca, String Producto, String Sucursal) {
        this.CantAnaquel = CantAnaquel;
        this.Folio = Folio;
        this.Manos = Manos;
        this.Ojo = Ojo;
        this.Suelo = Suelo;
        this.Techo = Techo;
        this.Cadena = Cadena;
        this.Categoria = Categoria;
        this.Clasificacion = Clasificacion;
        this.ComentarioAnaquel = ComentarioAnaquel;
        this.Fecha = Fecha;
        this.Marca = Marca;
        this.Producto = Producto;
        this.Sucursal = Sucursal;
    }

    public Integer getCantAnaquel() {
        return CantAnaquel;
    }

    public void setCantAnaquel(Integer CantAnaquel) {
        this.CantAnaquel = CantAnaquel;
    }

    public Integer getFolio() {
        return Folio;
    }

    public void setFolio(Integer Folio) {
        this.Folio = Folio;
    }

    public Integer getManos() {
        return Manos;
    }

    public void setManos(Integer Manos) {
        this.Manos = Manos;
    }

    public Integer getOjo() {
        return Ojo;
    }

    public void setOjo(Integer Ojo) {
        this.Ojo = Ojo;
    }

    public Integer getSuelo() {
        return Suelo;
    }

    public void setSuelo(Integer Suelo) {
        this.Suelo = Suelo;
    }

    public Integer getTecho() {
        return Techo;
    }

    public void setTecho(Integer Techo) {
        this.Techo = Techo;
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

    public String getComentarioAnaquel() {
        return ComentarioAnaquel;
    }

    public void setComentarioAnaquel(String ComentarioAnaquel) {
        this.ComentarioAnaquel = ComentarioAnaquel;
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

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String Producto) {
        this.Producto = Producto;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

    
    
}
