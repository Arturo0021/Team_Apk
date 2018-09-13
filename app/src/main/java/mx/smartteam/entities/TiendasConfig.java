package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author ivan.guerra
 */
public class TiendasConfig implements Serializable{

       public Integer Estatus;
       public String tipo;

    public TiendasConfig() {
        
    }
       
    public TiendasConfig(Integer Estatus, String tipo) {
        this.Estatus = Estatus;
        this.tipo = tipo;
    }

    public Integer getEstatus() {
        return Estatus;
    }

    public void setEstatus(Integer Estatus) {
        this.Estatus = Estatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

       
    
}
