package mx.smartteam.entities;

import java.io.Serializable;

public class Sondeo implements Serializable {
	
	public Integer Id;
	public Integer IdProyecto;
	public String Nombre;
	public Integer Activo;
	public Integer Orden;
	public String Tipo;
        public Integer StatusSync;
        public Long FechaSync=0L;
        public Integer IdentificadorVentas;
        public Integer IdGrupoSondeo;
        public String NombreSondeo;
        public String Title;
        public Integer Requerido;
	
	public PreguntaCollection Pregunta;
	
	public Sondeo() {
		// TODO Auto-generated constructor stub
		this.Pregunta=new PreguntaCollection();
	}

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getIdProyecto() {
        return IdProyecto;
    }

    public void setIdProyecto(Integer IdProyecto) {
        this.IdProyecto = IdProyecto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Integer getActivo() {
        return Activo;
    }

    public void setActivo(Integer Activo) {
        this.Activo = Activo;
    }

    public Integer getOrden() {
        return Orden;
    }

    public void setOrden(Integer Orden) {
        this.Orden = Orden;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public Integer getStatusSync() {
        return StatusSync;
    }

    public void setStatusSync(Integer StatusSync) {
        this.StatusSync = StatusSync;
    }

    public Long getFechaSync() {
        return FechaSync;
    }

    public void setFechaSync(Long FechaSync) {
        this.FechaSync = FechaSync;
    }

    public Integer getIdentificadorVentas() {
        return IdentificadorVentas;
    }

    public void setIdentificadorVentas(Integer IdentificadorVentas) {
        this.IdentificadorVentas = IdentificadorVentas;
    }

    public Integer getIdGrupoSondeo() {
        return IdGrupoSondeo;
    }

    public void setIdGrupoSondeo(Integer IdGrupoSondeo) {
        this.IdGrupoSondeo = IdGrupoSondeo;
    }

    public String getNombreSondeo() {
        return NombreSondeo;
    }

    public void setNombreSondeo(String NombreSondeo) {
        this.NombreSondeo = NombreSondeo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public Integer getRequerido() {
        return Requerido;
    }

    public void setRequerido(Integer Requerido) {
        this.Requerido = Requerido;
    }

    public PreguntaCollection getPregunta() {
        return Pregunta;
    }

    public void setPregunta(PreguntaCollection Pregunta) {
        this.Pregunta = Pregunta;
    }
        
        

}
