package clase.datos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import clase.memoria.Link;
import clase.memoria.LinkMovil;


@XmlRootElement(name="movil")
public class movil {
	private User usuario;//
	private int amigos;//
	private ArrayList<Link>  ultimoLibro;//
	private ArrayList<LinkMovil>  listaLecturas;
	
	
	public movil() {
		amigos=0;
	}
	
	/**** datos basicos del usuario****/
	public User getUser() {
		return usuario;
	}
	
	public void setUser(User usuario) {
		this.usuario= usuario;
	}
	
	
	/**** Ultimo libro leido por el usuario e informacion****/
	@XmlElement(name= "ultimoLibro")
	public ArrayList<Link>  getUltimoLibro() {
		return ultimoLibro;
	}
	
	public void setUltimoLibro(ArrayList<Link>  ultimoLibro) {
		this.ultimoLibro= ultimoLibro;
	}
	
	/**** ultimos libros leido por sus amigos ****/
	@XmlElement(name= "LibrosAmigos")
	public ArrayList<LinkMovil>  getListaLectura() {
		return listaLecturas;
	}
	
	public void setListaLectura(ArrayList<LinkMovil>  listaLecturas) {
		this.listaLecturas= listaLecturas;
	}
	
	/**** numero de amigos****/
	public int getAmigos() {//devuelve el nº de amigos del usuario correspondiente
		return amigos;
	}
	
	public void setAmigos(int amigos) {
		this.amigos= amigos;
	}
	
	
}
