package clase.datos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Amigos {
	private String idAmigo;
	private String idAmistad;
	
	public Amigos(){
		
	}
	
	public Amigos(String idAmigo, String idAmistad){
		this.idAmigo= idAmigo;
		this.idAmistad= idAmistad;
	}
	
	public String getIdAmigo() {
		return idAmigo;
	}
	
	public void setIdAmigo(String idAmigo) {
		this.idAmigo= idAmigo;
	}
	
	public String getIdAmistad() {
		return idAmistad;
	}
	
	public void serIdAmistad(String idAmistad) {
		this.idAmistad= idAmistad;
	}
	
	
}
