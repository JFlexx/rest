package clase.memoria;

import java.util.ArrayList;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="lectura")
public class servLectura {
	private ArrayList<LinkFriend> lectura;
		
	public servLectura() {
		this.lectura= new ArrayList<LinkFriend>();
	}
	
	
	@XmlElement(name="lecturas")
	public ArrayList<LinkFriend> getLecturas(){
		return lectura;
	}
	
	public void setLecturas(ArrayList<LinkFriend> friend) {
		this.lectura= friend;
	}
}
