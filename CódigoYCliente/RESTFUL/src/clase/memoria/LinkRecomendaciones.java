package clase.memoria;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

public class LinkRecomendaciones {
	private URL url;
	private int calificaciones;
	private String autor;
	private String categoria;
	private String rel;
	
	/** URL **/
	@XmlAttribute(name="href")
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(URL url) {
		this.url= url;
	}
	
	/** rel **/
	@XmlAttribute
	public String getRel() {
		return rel;
	}
	
	public void setRel(String rel) {
		this.rel= rel;
	}
	
	/** Calificaciones**/
	@XmlAttribute
	public int getCalificaciones() {
		return calificaciones;
	}
	
	public void setCalificaciones(int calificaciones) {
		this.calificaciones= calificaciones;
	}
	
	
	/** Autor **/
	@XmlAttribute
	public String getAutor() {
		return autor;
	}
	
	public void setAutor(String autor) {
		this.autor= autor;
	}
	
	/** categoria **/
	@XmlAttribute
	public String getCategoria() {
		return categoria;
	}
	
	public void setCategoria(String categoria) {
		this.categoria= categoria;
	}
	
	/**Constructor**/
	public LinkRecomendaciones(String url, String rel, int calificaciones, String autor, String categoria){
		try {
			this.url= new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.rel= rel;
		this.calificaciones= calificaciones;
		this.autor= autor;
		this.categoria= categoria;
	}
	
	public LinkRecomendaciones() {
		//nada
	}
}
