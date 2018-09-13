package mx.smartteam.entities;

import java.io.Serializable;
import java.sql.Date;

public class Proyecto  implements Serializable{
	
        public Integer Id;
	public String Nombre;
	public  Date FechaInicio;
	public Date FechaFin;
	public Integer  Activo;
	public Integer  IdUsuario;
	public String ArchivoProductos;
	public Integer  IdCreador;
        public Integer Ficha;
	
        public Proyecto() {
            
        }

    public Proyecto(Integer Id, String Nombre, Date FechaInicio, Date FechaFin, Integer Activo, Integer IdUsuario, String ArchivoProductos, Integer IdCreador, Integer Ficha) {
        this.Id = Id;
        this.Nombre = Nombre;
        this.FechaInicio = FechaInicio;
        this.FechaFin = FechaFin;
        this.Activo = Activo;
        this.IdUsuario = IdUsuario;
        this.ArchivoProductos = ArchivoProductos;
        this.IdCreador = IdCreador;
        this.Ficha = Ficha;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date FechaInicio) {
        this.FechaInicio = FechaInicio;
    }

    public Date getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Date FechaFin) {
        this.FechaFin = FechaFin;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getArchivoProductos() {
        return ArchivoProductos;
    }

    public void setArchivoProductos(String ArchivoProductos) {
        this.ArchivoProductos = ArchivoProductos;
    }

    public Integer getIdCreador() {
        return IdCreador;
    }

    public void setIdCreador(Integer IdCreador) {
        this.IdCreador = IdCreador;
    }

    public Integer getFicha() {
        return Ficha;
    }

    public void setFicha(Integer Ficha) {
        this.Ficha = Ficha;
    }
        
        

}
