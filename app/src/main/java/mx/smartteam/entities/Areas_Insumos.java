package mx.smartteam.entities;

import java.io.Serializable;
/**
 *
 * @author ivan.guerra
 */
public class Areas_Insumos implements Serializable {
    
    public Integer Id;
    public String Nombre;
    public String Color;
    public Integer Activo;
    public String Descripcion;

    public Areas_Insumos() {
    
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

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
}
