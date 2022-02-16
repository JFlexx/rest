package clase.memoria;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;


@XmlRootElement(name="usuarios")
public class usuarios {
	private ArrayList<Link> usuarios;
	
	public usuarios() {
		this.usuarios= new ArrayList<Link>();
	}
	
	
	@XmlElement(name="usuario")
	public ArrayList<Link> getUsers(){
		return usuarios;
	}
	
	public void setUsers(ArrayList<Link> Users) {
		this.usuarios= Users;
	}
	
}
