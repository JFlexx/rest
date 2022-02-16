package cliente;

import java.net.URI;
import java.text.ParseException;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Entity;
import org.glassfish.jersey.client.ClientConfig;

import clase.datos.Lectura;
import clase.datos.User;
import clase.datos.enviaSolicitud;

public class MiClienteLectura {


	public static void main(String[] args) throws ParseException {
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI());
		
		
		////////////////////////////////////////////////////////USUARIO/////////////////////////////////////////////////////////////////////////////
		
		/******************** Añade usuario a red de lectura(POST)***********************/
        System.out.println("Añadir usuario a la red de lectura.: ");
        User usuario= new User("Jason", "Jason100ry@gmail.com", 20, "Jason99");
        Response r= target.path("api").path("usuarios").request().post(Entity.xml(usuario));
        r.close();
        r = target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_XML).get(Response.class);
		
		String valor = r.readEntity(String.class);
        System.out.println("	EntidadXml: " + valor);	
        System.out.println("JSON:"+ target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_JSON).get(String.class));
        System.out.println("-------------------------------------------------------------------------");
		
        

        /*********** Obtiene los datos básicos del usuario(GET) *********************/
        System.out.println("Obteiene los datos básicos del usuario:");
        System.out.println("XML="+target.path("api").path("usuarios").path("1").request().accept(MediaType.APPLICATION_XML).get(String.class));
        System.out.println("JSON="+target.path("api").path("usuarios").path("1").request().accept(MediaType.APPLICATION_JSON).get(String.class));
        System.out.println("-------------------------------------------------------------------------");
        
        /****** Obtener datos del usuario filtrado por nombre(GET) *********/
        System.out.println("Obtiene un listado con todos los usuarios filtrado por nombre: ");
		System.out.println("XML="+target.path("api").path("usuarios").queryParam("name", "J").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").queryParam("name", "J").request().accept(MediaType.APPLICATION_JSON).get(String.class));
        System.out.println("-------------------------------------------------------------------------");
        
        
        /*************** Cambiar Datos básicos del Usuario(PUT) y devolvera la informacion del nuevo usuario **************/

        System.out.println("Cambiar Datos básicos de un Usuario menos el nombre de Usuario(name(String),correo(String),edad(int)): ");
        User actualizaUser= new User("Jason", "Jason@gmail.com",29, "NoCambiaElNombreDeUsuario");
        actualizaUser.setID(3);
        r= target.path("api").path("usuarios").path("1").request().accept(MediaType.APPLICATION_XML).put(Entity.xml(actualizaUser),Response.class);
        r.close();
        
		System.out.println("XML="+target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_XML).get(String.class));

		/************************************************ Obtener listado de usuarios ******************************************/
		// Petición 1(/api/garajes)
		System.out.println("Obtiene un listado con todos los usuarios: ");
		System.out.println("XML="+target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_XML).get(String.class));
        System.out.println("JSON"+target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_JSON).get(String.class));	
        System.out.println("-------------------------------------------------------------------------");
        
        
        /********************* Borrar usuario de la red **************************/
        System.out.println("Borra a usuario de la red de lectura:");
        Response response= target.path("api").path("usuarios").path("4").request().accept(MediaType.APPLICATION_XML).delete();
		response.close();
        System.out.println("XML="+target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_XML).get(String.class));
        System.out.println("JSON="+target.path("api").path("usuarios").request().accept(MediaType.APPLICATION_JSON).get(String.class));
        System.out.println("-------------------------------------------------------------------------");
        
       
        ///////////////////////////////////// LECTURA ////////////////////////////////////////////////
		
        /********************************* Añadir una lectura de un libro por un usuario **************************/
        System.out.println("Añade Una lectura de un libro por un usuario:");
        Date date= new Date();
//    System.out.println(date);
        Lectura lectura= new Lectura(10, date, 1, "Manuel", "Musical");
        Response r2= target.path("api").path("usuarios").path("1").path("lectura").request().post(Entity.xml(lectura));
        r2.close();
        System.out.println("XML="+target.path("api").path("usuarios").path("1").path("lectura").request().accept(MediaType.APPLICATION_XML).get(String.class));
        System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("lectura").request().accept(MediaType.APPLICATION_JSON).get(String.class));
        System.out.println("-------------------------------------------------------------------------");

		/******************************** Borra la lectura de un usuario ****************************************/
		System.out.println("-Borra La Lectura de un usuario:");
		Response response2= target.path("api").path("usuarios").path("1").path("lectura").path("6").request().accept(MediaType.APPLICATION_XML).delete();
		response2.close();
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("lectura").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("lectura").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

		/***************************** Editar la lectura de un libro(PUT) ***************************/
		System.out.println("-Edita la lectura de un libro:");
		Date date2= new Date();
		//el id y el idLectura no cambian, asi que da igual el valor que se le pase
		Lectura lectura2= new Lectura(10, date2, 8, "Federico", "Novela");
		Response r3= target.path("api").path("usuarios").path("1").path("lectura").path("8").request().accept(MediaType.APPLICATION_XML).put(Entity.xml(lectura2),Response.class);
		//Lectura lectura1= new Lectura(calificacion, fechaLectura, idLect, autor, categoria)
		r3.close();
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("lectura").path("8").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("lectura").path("8").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

        /******************************** Consulta libro leidos por un usuario, se puede filtrar por fecha ********************/
		System.out.println("-Consulta libro leidos por un usuario, se puede filtrar por fecha ");
		System.out.println("	Consulta Normal de lectura del usuario -->");
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("lectura").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("lectura").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("	Consulta usando filtro por fecha de lectura del usuario -->");
		System.out.println("XML"+target.path("api").path("usuarios").path("1").path("lectura").queryParam("fecha", "2021-04-21").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON"+target.path("api").path("usuarios").path("1").path("lectura").queryParam("fecha", "2021-04-21").request().accept(MediaType.APPLICATION_XML).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

		
		/*********************************Consulta información del libro del usuario ********************/
		System.out.println("-Consulta información del libro del usuario");
		System.out.println("XML=" +target.path("api").path("usuarios").path("1").path("lectura").path("1").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON=" +target.path("api").path("usuarios").path("1").path("lectura").path("1").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

		
		
/////////////////////////////////////////////// Amigos //////////////////////////////////////////////////////////////////////////	
		/**************** Añade amigo a la red de lectura(POST) *******************************/
		System.out.println("-Añade amigo a la red de lectura:");
		enviaSolicitud enviaSoli= new enviaSolicitud("Henry99", "mensaje");
		Response r4= target.path("api").path("usuarios").path("2").path("amigos").request().post(Entity.xml(enviaSoli));
		r4.close();
		System.out.println("XML=" +target.path("api").path("usuarios").path("2").path("amigos").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON=" +target.path("api").path("usuarios").path("2").path("amigos").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

		/*************** Eliminar un amigo(Delete) ***********/
		System.out.println("-Elimina un amigo de tu lista de amistad:");
		response= target.path("api").path("usuarios").path("2").path("amigos").path("3").request().accept(MediaType.APPLICATION_XML).delete();
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("amigos").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("amigos").request().accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("-------------------------------------------------------------------------");

		/************** Consigue la lista de amigos de un usuario(GET)*******/
		System.out.println("-Consigue la lista de amigos, se puede filtrar limitando la cantidad de información(limite): ");
		//Devuelve la lista de amigos del usuario 1, se filtrará  limitando la cantidad de informacion, en este caso solo 1 amigo. 
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("amigos").queryParam("limite", "2").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("amigos").queryParam("limite", "2").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

		
//		/************* COnsigue la información del amigo de un usuario(GET) ****/
//		//Esto está implementado por mi, ya que esto también se podría realiza, haciendo un get del usuario que se desee su informacion
//		//Esto se ha realizado para que sea mejor navegable, y se pueda acceder a la informacion de un usuario de manera mñás directa
//		System.out.println("-Consigue la información del amigo:");
//		System.out.println(target.path("api").path("usuarios").path("1").path("amigos").path("3").request().accept(MediaType.APPLICATION_XML).get(String.class));
//        System.out.println("-------------------------------------------------------------------------");

		////////////////////////////////////////// Libros Recomendados //////////////////////////////////////////////
		
		/******************************* Devuelve la recomendaciones de libros de amigos(GET) **************************************/
		//Devolverá una uri con el id del libro recomendado por cada amigo para acceder a la información del libro.
		//--> filtro calificacion: devuelve libros con calificaciones superiores a este parametro(Ejemplo "2")
		//--> filtro autor: filtra por nombre, no es necesario escribir el nombre completo, ya que filtra por las letras que contiene este parametro(Ejemplo "Cervan")
		//--> filtro categoria: filtra por categoria, no es necesario escribir la categoria completa, ya que filtra por las letras que contiene este parametro(ejemplor "Novel")
		System.out.println("-Devolverá las recomendaciones de libros de amigos, filtros(calificacion,autor,categoria): ");
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("amigos").path("recomendaciones").queryParam("calificacion", "5").queryParam("autor", "Charles").
				queryParam("categoria", "Novela").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("amigos").path("recomendaciones").queryParam("calificacion", "5").queryParam("autor", "Charles").
				queryParam("categoria", "Novela").request().accept(MediaType.APPLICATION_JSON).get(String.class));
        System.out.println("-------------------------------------------------------------------------");

		/***************************************** Aplicacion Movil(GET) *******************************************************/
		//Devuelve la Aplicación movil devuelve:
		//			--> Información del usuario
		// 			--> Uri para acceder al recurso del ultimo libro del usuario
		// 			--> Uri para acceder al recurso del ultimo libro de sus amigos
		//			--> Amigos totales del usuario
		System.out.println("-Devuelve aplicación móvil: ");
		System.out.println("XML="+target.path("api").path("usuarios").path("1").path("movil").request().accept(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("JSON="+target.path("api").path("usuarios").path("1").path("movil").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		System.out.println("-------------------------------------------------------------------------");

	}
	

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/RESTFUL/").build();
	}
}
