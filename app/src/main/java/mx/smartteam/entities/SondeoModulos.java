package mx.smartteam.entities;

import java.io.Serializable;

public class SondeoModulos implements Serializable , Comparable<Object>{
	
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
        public String Titulo;	
        public PreguntaCollection Pregunta;
        public String tposm;
        public Integer Requerido;

    public int compareTo(Object another) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public PreguntaCollection getPregunta() {
        return Pregunta;
    }

    public void setPregunta(PreguntaCollection Pregunta) {
        this.Pregunta = Pregunta;
    }

    public String getTposm() {
        return tposm;
    }

    public void setTposm(String tposm) {
        this.tposm = tposm;
    }

    public Integer getRequerido() {
        return Requerido;
    }

    public void setRequerido(Integer Requerido) {
        this.Requerido = Requerido;
    }
    
    
    
}