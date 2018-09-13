package mx.smartteam.entities;

/**
 *
 * @author jaime.bautista
 */
public class Notificaciones{
    
    public Integer Id;
    public String Tipo;
    public String Cuerpo;
    public Long CapturaFecha = 0L;
    public Long FechaFin = 0L;
    public Long FechaEnvio = 0L; 
    public Integer IdProyecto;
    public Integer StatusSync;
    public Long FechaSync=0L;
   
}
