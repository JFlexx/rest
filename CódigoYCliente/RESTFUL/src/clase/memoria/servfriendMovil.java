package clase.memoria;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="lectura")
public class servfriendMovil {
	private ArrayList<Link> friend;
	
	public servfriendMovil() {
		this.friend= new ArrayList<Link>();
	}
	
	
	@XmlElement(name="lecturas")
	public ArrayList<Link> getFriend(){
		return friend;
	}
	
	public void setFriend(ArrayList<Link> friend) {
		this.friend= friend;
	}
}
