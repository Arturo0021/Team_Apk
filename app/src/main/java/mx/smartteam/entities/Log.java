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
public class Log implements Serializable{
    public Integer LogId;
    public Integer UsuarioId ;
    public Integer ProyectoId;
    public Long Fecha = 0L;
}
