package mx.smartteam.entities;

import java.io.Serializable;

public class ScoredCard implements Serializable{
    public Integer Id;
    public Integer IdProyecto;
    public Integer IdUsuario;
    public Integer IdCadena;
    public Integer Registros;
    public Long Fecha = 0L;
    public String nombre ;
    public String extra ;
    public int ponderacion;
    public Integer determinanteGSP;
    public String sucursal;
    
}
