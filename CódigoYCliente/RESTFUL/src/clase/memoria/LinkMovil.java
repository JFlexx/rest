package clase.memoria;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;

public class LinkMovil {
	private URL url;
	private int idAmigo; 
	private String rel;
	
	@XmlAttribute(name="href")
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(URL url) {
		this.url= url;
	}
	
	@XmlAttribute
	public String getRel() {
		return rel;
	}
	
	public void setRel(String rel) {
		this.rel= rel;
	}
	
	@XmlAttribute
	public int getIdAmigo() {
		return idAmigo;
	}
	
	public void setIdAmigo(int idAmigo) {
		this.idAmigo= idAmigo;
	}
	
	/**Constructor**/
	public LinkMovil(String url, String rel, int idAmigo){
		try {
			this.url= new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.rel= rel;
		this.idAmigo= idAmigo;
	}
	
	public LinkMovil() {
		//nada
	}
	
}
