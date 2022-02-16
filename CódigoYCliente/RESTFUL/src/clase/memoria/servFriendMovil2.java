package clase.memoria;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="lectura")
public class servFriendMovil2 {
private ArrayList<LinkMovil> friend;
	
	public servFriendMovil2() {
		this.friend= new ArrayList<LinkMovil>();
	}
	
	
	@XmlElement(name="lecturas")
	public ArrayList<LinkMovil> getFriend(){
		return friend;
	}
	
	public void setFriend(ArrayList<LinkMovil> friend) {
		this.friend= friend;
	}
}
