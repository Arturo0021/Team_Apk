package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class Insumos implements Serializable {
    
    public Integer Id;
    public String Nombre;
    public Integer Activo;
    public Double ValorMin;
    public Double ValorMax;
    public Double Salto;
    public String medida;
    public String Abreviatura;
    public String Respuesta;
    public String Fecha;
    public String Color;
    public Integer IdConfig;
    public Integer Determinante;
    public Integer IdUsuario;
    public Integer Estatus;
    

    public Insumos() {
        
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

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }

    public Double getValorMin() {
        return ValorMin;
    }

    public void setValorMin(Double ValorMin) {
        this.ValorMin = ValorMin;
    }

    public Double getValorMax() {
        return ValorMax;
    }

    public void setValorMax(Double ValorMax) {
        this.ValorMax = ValorMax;
    }

    public Double getSalto() {
        return Salto;
    }

    public void setSalto(Double Salto) {
        this.Salto = Salto;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getAbreviatura() {
        return Abreviatura;
    }

    public void setAbreviatura(String Abreviatura) {
        this.Abreviatura = Abreviatura;
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

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public Integer getIdConfig() {
        return IdConfig;
    }

    public void setIdConfig(Integer IdConfig) {
        this.IdConfig = IdConfig;
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

    public Integer getEstatus() {
        return Estatus;
    }

    public void setEstatus(Integer Estatus) {
        this.Estatus = Estatus;
    }
    
}
