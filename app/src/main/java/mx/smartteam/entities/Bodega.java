/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.entities;

import java.util.Date;
import org.apache.http.entity.SerializableEntity;

/**
 *
 * @author edgarin.lara
 */
public class Bodega {
    public Integer Id;
    public Integer IdProyecto;
    public Integer IdUsuario;
    public Integer DeterminanteGSP;
    public Long Sku=0L;
    public Integer Cantidad;
    public Integer Tarima;
    public String Comentario;
    public String FechaCrea;
    public Integer IdVisita;
    public Integer IdFoto;
    //Sincroizacion
    public Integer StatusSync;
    public Date FechaSync;
}
