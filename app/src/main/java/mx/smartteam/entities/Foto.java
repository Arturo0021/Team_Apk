package mx.smartteam.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Dictionary;

public class Foto implements Serializable{

    public static enum Type {
        Entrada, Salida, Foto, Sondeo , Sos, Exhibicion, Bodega , Anaquel, Precio, MaterialPromocional,foto_anaquel,foto_adicional,foto_producto,Sod
    }

    public Integer Id;
    public String Foto;
    public Type Tipo;
    public String Comentario;
    public String Nombre;
    public Integer IdVisita;
    public String FechaCrea;
    public Integer StatusSync;
    public String FechaSync;
    public Integer IdCategoria;
    public Integer nOpcion = 0;
    
    public Integer Orden = 0;
    public Integer FotosPermitidas = 0;
    public Integer FotosCapturadas = 0;
    public Boolean PermiteComentario = false;
    public String NombreOpcion;

    public CategoriaCollection Categoria;
    public OpcionCollection Opcion;
    public Integer contador;
    public Integer IdSondeo=0;
    public Integer Categoriaid=0;
    public Integer Marcaid=0;
    public Long SKU=null;
    public Integer idExhibicionConfig=null;
            
    public Foto() {
        // TODO Auto-generated constructor stub
        this.Categoria = new CategoriaCollection();
        this.Opcion = new OpcionCollection();
    }
}
