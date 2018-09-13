package mx.smartteam.entities;

import java.io.Serializable;

public class Pregunta implements  Serializable{

    public enum Type {
        abierta, numerico, decimal, multiple, unicaradio, sino, informativo , fecha, hora, combo
    }

    public Integer Id;
    public String Nombre;
    public String Tipo;
    public Object Respuesta;
    public OpcionCollection Opciones;
    public Boolean Requerido;
    public Integer Longitud;
    public Integer IdSondeo;
    public Integer StatusSync;
    public Long FechaSync = 0L;
    public Integer ContestacionPreguntaId;
    public String clave;
    public Integer bandera=0;
    public Integer Activo;
    public Pregunta() {
        // TODO Auto-generated constructor stub
        this.Opciones = new OpcionCollection();
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public Object getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(Object Respuesta) {
        this.Respuesta = Respuesta;
    }

    public OpcionCollection getOpciones() {
        return Opciones;
    }

    public void setOpciones(OpcionCollection Opciones) {
        this.Opciones = Opciones;
    }

    public Boolean getRequerido() {
        return Requerido;
    }

    public void setRequerido(Boolean Requerido) {
        this.Requerido = Requerido;
    }

    public Integer getLongitud() {
        return Longitud;
    }

    public void setLongitud(Integer Longitud) {
        this.Longitud = Longitud;
    }

    public Integer getIdSondeo() {
        return IdSondeo;
    }

    public void setIdSondeo(Integer IdSondeo) {
        this.IdSondeo = IdSondeo;
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

    public Integer getContestacionPreguntaId() {
        return ContestacionPreguntaId;
    }

    public void setContestacionPreguntaId(Integer ContestacionPreguntaId) {
        this.ContestacionPreguntaId = ContestacionPreguntaId;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getBandera() {
        return bandera;
    }

    public void setBandera(Integer bandera) {
        this.bandera = bandera;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }
    
    
    

}
