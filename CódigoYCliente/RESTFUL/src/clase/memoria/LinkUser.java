package clase.memoria;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;

public class LinkUser {

	private URL url;
	private String name; 
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
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name= name;
	}
	
	/**Constructor**/
	public LinkUser(String url, String rel, String name){
		try {
			this.url= new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.rel= rel;
		this.name= name;
	}
	
	public LinkUser() {
		
	}
	
}
