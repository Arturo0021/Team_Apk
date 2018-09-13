package mx.smartteam.entities;

import java.io.Serializable;

public class ContestacionPregunta implements Serializable {

    public Integer Id;
    public Integer IdContestacion;
    public Long Fecha = 0L;
    public String Respuesta;
    
    public ContestacionPreguntaOpcionCollection ContestacionPreguntaOpcion;
	
	public ContestacionPregunta() {
		// TODO Auto-generated constructor stub
		this.ContestacionPreguntaOpcion=new ContestacionPreguntaOpcionCollection();
	}
}
