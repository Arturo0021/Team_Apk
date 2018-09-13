package mx.smartteam.entities;

import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import android.content.Context;

import android.widget.Toast;
import java.io.Serializable;

public class Usuario implements Serializable {

	public String Usuario;
	public String Password;
	public Integer Id;
	public Proyecto Proyecto;
	public String IMEI;
        public String Sim;
        public String Telefono;
	public String Nombre;
        public Integer Tipo;
        
        public Usuario() {
            
        }       

    public Usuario(String Usuario, String Password, Integer Id, Proyecto Proyecto, String IMEI, String Sim, String Telefono, String Nombre, Integer Tipo) {
        this.Usuario = Usuario;
        this.Password = Password;
        this.Id = Id;
        this.Proyecto = Proyecto;
        this.IMEI = IMEI;
        this.Sim = Sim;
        this.Telefono = Telefono;
        this.Nombre = Nombre;
        this.Tipo = Tipo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Proyecto getProyecto() {
        return Proyecto;
    }

    public void setProyecto(Proyecto Proyecto) {
        this.Proyecto = Proyecto;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getSim() {
        return Sim;
    }

    public void setSim(String Sim) {
        this.Sim = Sim;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Integer getTipo() {
        return Tipo;
    }

    public void setTipo(Integer Tipo) {
        this.Tipo = Tipo;
    }
        
        
       
}
