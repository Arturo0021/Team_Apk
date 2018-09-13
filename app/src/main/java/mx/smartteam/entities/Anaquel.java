/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author edgarin.lara
 */
public class Anaquel implements Serializable{
    
    public Integer Id;
    public Integer IdProyecto;
    public Integer IdUsuario;
    public Integer DeterminanteGSP;
    public Long Sku=0L;
    public Integer Cantidad;
    public Double Precio;
    public String Comentario;
    public Integer Suelo;
    public Integer Manos;
    public Integer Ojos;
    public Integer Techo;
    public String FechaCrea;
    public Integer IdVisita;
    public String Tipo;
    public Integer Fanaquel;
    public Integer Fprecio;
    public Integer IdFoto;
    public Integer StatusSync;
    public String FechaSync;
    
    
}
