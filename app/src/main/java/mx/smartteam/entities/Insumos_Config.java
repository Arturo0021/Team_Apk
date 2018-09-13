package mx.smartteam.entities;

import java.io.Serializable;
/**
 *
 * @author ivan.guerra
 */
public class Insumos_Config implements Serializable {

    public Integer Id;
    public Integer IdArea_Insumo;
    public Integer IdInsumo;
    public Integer Activo;
    public Integer IdProyecto;
    public Integer IdUnidad_Medida;
    public Double Valor_Min;
    public Double Valor_Max;
    public Double Salto;

    public Insumos_Config() {
        
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getIdArea_Insumo() {
        return IdArea_Insumo;
    }

    public void setIdArea_Insumo(Integer IdArea_Insumo) {
        this.IdArea_Insumo = IdArea_Insumo;
    }

    public Integer getIdInsumo() {
        return IdInsumo;
    }

    public void setIdInsumo(Integer IdInsumo) {
        this.IdInsumo = IdInsumo;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }

    public Integer getIdProyecto() {
        return IdProyecto;
    }

    public void setIdProyecto(Integer IdProyecto) {
        this.IdProyecto = IdProyecto;
    }

    public Integer getIdUnidad_Medida() {
        return IdUnidad_Medida;
    }

    public void setIdUnidad_Medida(Integer IdUnidad_Medida) {
        this.IdUnidad_Medida = IdUnidad_Medida;
    }

    public Double getValor_Min() {
        return Valor_Min;
    }

    public void setValor_Min(Double Valor_Min) {
        this.Valor_Min = Valor_Min;
    }

    public Double getValor_Max() {
        return Valor_Max;
    }

    public void setValor_Max(Double Valor_Max) {
        this.Valor_Max = Valor_Max;
    }

    public Double getSalto() {
        return Salto;
    }

    public void setSalto(Double Salto) {
        this.Salto = Salto;
    }
    
}
