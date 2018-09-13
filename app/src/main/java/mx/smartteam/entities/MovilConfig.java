package mx.smartteam.entities;

/**
 *
 * @author jaime.bautista
 */
public class MovilConfig {
    public OpcionCollection Opciones;
 public  enum Type{nombre,sku}
 public enum TypeConexion{online,offline}
    public Integer gpsconfig;
    public String Tipo;   
    public String Conexion;
}
