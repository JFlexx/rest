package clase.datos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class enviaSolicitud {
	private String idSolicitante;
	private String msgSolicitud;
	
	public enviaSolicitud() {
		
	}
	
	public enviaSolicitud(String idSolicitante, String msgSolicitud) {
		this.idSolicitante= idSolicitante;
		this.msgSolicitud= msgSolicitud;
	}
	
	public String getIdSolicitante() {
		return idSolicitante;
	}
	
	public void setIdSolicitante(String idSolicitante) {
		this.idSolicitante= idSolicitante;
	}
	
	public String getMsgSolicitud() {
		return msgSolicitud;
	}
	
	public void setMsgSolicitud(String msgSolicitud) {
		this.msgSolicitud= msgSolicitud;
	}

}
