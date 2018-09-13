
package mx.smartteam.entities;

import java.io.Serializable;
import java.math.BigInteger;

public class ExhibicionConfig implements Serializable{
    public Integer Id;
    public Integer IdSondeoOrden;
    public Integer IdCategoria;
    public Integer IdMarca;
    public Integer IdExhibicion;
    public Double Ponderacion;
    public Integer Activo;
    public String FechaCrea;
    public String FechaMod;
}
