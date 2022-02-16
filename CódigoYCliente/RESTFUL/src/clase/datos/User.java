package clase.datos;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	private int ID; 
	private String name;
	private String correo;
	private int edad;
	private String userName;
	
	public User() {
		
	}
	
	public User(String name, String correo, int edad, String userName) {
		this.ID= ID;
		this.name= name;
		this.correo= correo;
		this.edad= edad;
		this.userName= userName;
		
	}
	
	@XmlAttribute(required=false)
	public int getID() {
		return ID;
	}
	
	public void setID(int id) {
		this.ID= id;
	}
	
	
	//degetName
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name= name;
	}
	
	//de correo
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(String correo) {
		this.correo= correo;
	}
	
	// de edad
	public int getEdad() {
		return edad;
	}
	
	public void setEdad(int Edad) {
		this.edad= Edad;
	}
	
	//de userName
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName= userName;
	}
	
	
}
