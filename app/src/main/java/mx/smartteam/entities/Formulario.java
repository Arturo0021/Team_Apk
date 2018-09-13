package mx.smartteam.entities;



public class Formulario {
	
        public Integer Id;
	public Integer IdProyecto;
	public String Nombre;
	public Integer Activo;
	public Integer Orden;
	public String Tipo;
        public Integer StatusSync;
        public Long FechaSync=0L;  
        public String Titulo;
        public Integer requerido;

    public Formulario() {   
    
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

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public Integer getRequerido() {
        return requerido;
    }

    public void setRequerido(Integer requerido) {
        this.requerido = requerido;
    }
        
        
}



