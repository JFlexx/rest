package clase.datos;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lectura {
	
	private int idlectura;
	private int idLect; 
	private Date fechaLectura;//necesario para filtrar por fechas como se pide en la práctica
	private int calificacion;
	private String autor;
	private String categoria;
	
	public Lectura() {
		//nada
	}
	
	public Lectura(int calificacion, Date fechaLectura,int idLect, String autor, String categoria) {
		this.idlectura= idlectura;
		this.idLect= idLect;
		this.fechaLectura= fechaLectura;
		this.calificacion= calificacion;
		this.autor= autor; 
		this.categoria= categoria;

	}
	
	@XmlAttribute(required=false)
	public int getIdLectura() {
		return idlectura;
	}
	public void setIdLectura(int idlectura) {
		this.idlectura= idlectura;
	}
	
	@XmlAttribute(required=false)
	public Date getFechaLectura() {
		return fechaLectura;
	}
	
	public void setFechaLectura(Date fechaLectura) {
		this.fechaLectura= fechaLectura;
	}
	
	public int getCalificacion() {
		return calificacion;
	}
	
	public void setCalificacion(int calificacion) {
		this.calificacion= calificacion;
	}
	
	public int getIdLect() {
		return idLect;
	}
	public void setIdLect(int idLect) {
		this.idLect= idLect;
	}
	
	public String getAutor() {
		return autor;
	}
	
	public void setAutor(String autor) {
		this.autor= autor;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setCategoria(String categoria) {
		this.categoria= categoria;
	}
	
}
