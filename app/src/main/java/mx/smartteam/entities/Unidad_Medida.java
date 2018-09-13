package mx.smartteam.entities;

import java.io.Serializable;
/**
 *
 * @author ivan.guerra
 */
public class Unidad_Medida implements Serializable {
    
    public Integer Id;
    public String Nombre;
    public String Abreviatura;

    public Unidad_Medida() {
        
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

    public String getAbreviatura() {
        return Abreviatura;
    }

    public void setAbreviatura(String Abreviatura) {
        this.Abreviatura = Abreviatura;
    }
    
}
