/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.entities;

import java.io.Serializable;

/**
 *
 * @author jaime.bautista
 */
public class SeguimientoGps implements Serializable{
    
    public Integer LogId;
    public Integer UsuarioId ;
    public Integer ProyectoId;
    public String Fecha;
    public Long FechaSync = 0L;
    public Double Latitud;
    public Double Longitud;
    public Integer determinanteGsp;
    
    
}
