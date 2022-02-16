package clase.memoria;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

public class LinkFriend {

	private URL url;
	private String date; 
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
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date= date;
	}
	
	/**Constructor**/
	public LinkFriend(String url, String rel, String date){
		try {
			this.url= new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.rel= rel;
		this.date= date;
	}
	
	
	
}
