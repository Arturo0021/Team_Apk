package mx.smartteam.entities;

import java.io.Serializable;

public class Contestacion implements Serializable {

    public Integer Id;
    public Integer IdSondeo;
    public Integer IdVisita;
    public Integer IdUsuario;
    public Integer IdFoto;
    public Long Sku;

    public String FechaCrea;
    public Integer StatusSync;
    public String FechaSync;
    public Integer IdOpcion = 0;

    public ContestacionPreguntaCollection ContestacionPregunta;

    public Contestacion() {
        // TODO Auto-generated constructor stub
        this.ContestacionPregunta = new ContestacionPreguntaCollection();
    }
}
