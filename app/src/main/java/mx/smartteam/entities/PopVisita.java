package mx.smartteam.entities;

import java.util.Date;

import android.text.method.DateTimeKeyListener;
import java.io.Serializable;

public class PopVisita implements Serializable {
	
	public Integer Id;
	public Integer IdProyecto;
	public Integer DeterminanteGSP;
	public Integer IdUsuario;
	public String FechaCrea;
	public Double Latitud;
	public Double Longitud;
	public Integer IdStatus;
        public Boolean Abierta;
        public StringBuilder FotoEntrada;
        public String FechaEntrada;
        public StringBuilder FotoSalida;
        public String FechaSalida;
        public String FechaCierre;
        //Sync
        public Integer StatusSync;
        public String FechaSync;
        
	

}
