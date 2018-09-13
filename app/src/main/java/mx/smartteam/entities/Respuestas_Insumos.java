package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class Respuestas_Insumos implements Serializable{
    
    public Integer Id;
    public Integer IdConfig;
    public String Respuesta;
    public String Fecha;
    public Integer Determinante;
    public Integer IdUsuario;
    public Integer IdVisita;
    

    public Respuestas_Insumos() {
    
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getIdConfig() {
        return IdConfig;
    }

    public void setIdConfig(Integer IdConfig) {
        this.IdConfig = IdConfig;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(String Respuesta) {
        this.Respuesta = Respuesta;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public Integer getDeterminante() {
        return Determinante;
    }

    public void setDeterminante(Integer Determinante) {
        this.Determinante = Determinante;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public Integer getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(Integer IdVisita) {
        this.IdVisita = IdVisita;
    }
    
}
