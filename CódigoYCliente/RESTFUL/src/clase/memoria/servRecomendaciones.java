package clase.memoria;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="recomendaciones")
public class servRecomendaciones {
	private ArrayList<LinkRecomendaciones> recomendacion;
	
	public servRecomendaciones() {
		this.recomendacion= new ArrayList<LinkRecomendaciones>();
	}
	
	
	@XmlElement(name="recomendacion")
	public ArrayList<LinkRecomendaciones> getRecomendacion(){
		return recomendacion;
	}
	
	public void setFriend(ArrayList<LinkRecomendaciones> recomendacion) {
		this.recomendacion= recomendacion;
	}

}
