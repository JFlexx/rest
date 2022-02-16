package clase.memoria;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name="usuarios")
public class serv {
	private ArrayList<LinkUser> usuarios;
	
	public serv() {
		this.usuarios= new ArrayList<LinkUser>();
	}
	
	
	@XmlElement(name="usuario")
	public ArrayList<LinkUser> getUsers(){
		return usuarios;
	}
	
	public void setUsers(ArrayList<LinkUser> Users) {
		this.usuarios= Users;
	}
	
}
