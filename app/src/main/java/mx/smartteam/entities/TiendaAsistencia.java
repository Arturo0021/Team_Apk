package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class TiendaAsistencia implements Serializable{
    
    public Integer Total_de_Asistencias;
    public String Cadena;
    public String Fecha;
    public String Folio;
    public String Promotor;
    public String Salida;
    public String Sucursal;
    public String Clasificacion;
    public String Trayectoria;
    public String Usuario;

    public TiendaAsistencia() {
        
    }
    
    public TiendaAsistencia(Integer Total_de_Asistencias, String Cadena, String Fecha, String Folio, String Promotor, String Salida, String Sucursal, String Clasificacion, String Trayectoria, String Usuario) {
        this.Total_de_Asistencias = Total_de_Asistencias;
        this.Cadena = Cadena;
        this.Fecha = Fecha;
        this.Folio = Folio;
        this.Promotor = Promotor;
        this.Salida = Salida;
        this.Sucursal = Sucursal;
        this.Clasificacion = Clasificacion;
        this.Trayectoria = Trayectoria;
        this.Usuario = Usuario;
    }

    public Integer getTotal_de_Asistencias() {
        return Total_de_Asistencias;
    }

    public void setTotal_de_Asistencias(Integer Total_de_Asistencias) {
        this.Total_de_Asistencias = Total_de_Asistencias;
    }

    public String getCadena() {
        return Cadena;
    }

    public void setCadena(String Cadena) {
        this.Cadena = Cadena;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String Folio) {
        this.Folio = Folio;
    }

    public String getPromotor() {
        return Promotor;
    }

    public void setPromotor(String Promotor) {
        this.Promotor = Promotor;
    }

    public String getSalida() {
        return Salida;
    }

    public void setSalida(String Salida) {
        this.Salida = Salida;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

    public String getClasificacion() {
        return Clasificacion;
    }

    public void setClasificacion(String Clasificacion) {
        this.Clasificacion = Clasificacion;
    }

    public String getTrayectoria() {
        return Trayectoria;
    }

    public void setTrayectoria(String Trayectoria) {
        this.Trayectoria = Trayectoria;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    
    
}
