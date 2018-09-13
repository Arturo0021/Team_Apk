/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.entities;

/**
 *
 * @author edgarin.lara
 */
public class RespuestaSondeo {
    public Integer IdContestacion;
    public Integer Id;
    public Integer IdPregunta;
    public Integer IdSondeo;
    public Integer IdVisita;
    public String Respuesta;
    public String FechaCrea;
    public String Tipo;
    public Integer StatusSync;
    public String FechaSync;
    public OpcionCollection Opciones;
    public Long Sku=0L;
    public Integer IdMarca;
    public Integer IdCategoria;
    public Integer IdOpcion =0;

    public RespuestaSondeo() {
        Opciones = new OpcionCollection();
    }
}
