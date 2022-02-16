package clase.BBDD;

import java.net.URI;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


import javax.ws.rs.QueryParam;

import org.apache.naming.NamingContext;

import clase.datos.Amigos;
import clase.datos.Lectura;
import clase.datos.User;
import clase.datos.enviaSolicitud;
import clase.datos.movil;
import clase.memoria.Link;
import clase.memoria.LinkFriend;
import clase.memoria.LinkMovil;
import clase.memoria.LinkRecomendaciones;
import clase.memoria.LinkUser;
import clase.memoria.serv;
import clase.memoria.servFriendMovil2;
import clase.memoria.servLectura;
import clase.memoria.servRecomendaciones;
import clase.memoria.servfriendMovil;
import clase.memoria.usuarios;



@Path("/usuarios")
public class API {
	
	@Context
	private UriInfo uriInfo;
	@Context
	URI uriInfoUser;

	private DataSource ds;
	private Connection conn;
	InitialContext ctx;
	
	public API() {
		try {
			ctx = new InitialContext();
			NamingContext envCtx = (NamingContext) ctx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/myDeptosdb");
			conn = ds.getConnection();
			System.out.println("Se ha conectado a la BBDD");
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/************************************************************************** USUARIO *******************************************************/
	/**
	 * 
	 * @param id
	 * @return Datos del Usuario usando su ID
	 */
	@GET
	@Path("{idUser}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getDataIDUser(@PathParam("idUser") String id) {
		try {
			int int_id = Integer.parseInt(id);
			String sql = "SELECT * FROM Usuarios where id=" + int_id + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int columnsNumber= rsmd.getColumnCount();
//			while (rs.next()) {
//			    for (int i = 1; i <= columnsNumber; i++) {
//			        if (i > 1) System.out.print(",  ");
//			        String columnValue = rs.getString(i);
//			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//			    }
//			    System.out.println("");
//			}
			if (rs.next()) {
				User user= UserFromRS(rs);
				return Response.status(Response.Status.OK).entity(user).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}
		} catch (NumberFormatException e) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	
	/**
	 * 
	 * @param user
	 * @return Añade usuario a la red de lectura
	 */
	//Añadir usuario a la red de lectura
	@POST
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response addUser(User user) {
		//trataremos las excepciones con try-catch
		try {
			String sql = "INSERT INTO `myDeptosdb`.`Usuarios` (`name`, `email`, `edad`, `uname`) " + "VALUES ('"
					+ user.getName()+ "', '"+ user.getCorreo() + "', '" + user.getEdad()+ "', '" + user.getUserName() + "');";
//			System.out.println(sql);
			PreparedStatement ps=  conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 
			int affectedRows = ps.executeUpdate();
			ResultSet generatedID= ps.getGeneratedKeys();
			
			if(generatedID.next()) {
				user.setID(generatedID.getInt(1));
				String location = uriInfo.getAbsolutePath() + "/" + user.getID();
				return Response.status(Response.Status.CREATED).entity(user).header("Location", location).header("Content-Location", location).build();
			}
			
			//otro caso falla
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al crea usuario\n").build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear el usuario\n" + e.getStackTrace()).build();
		}
	}/*addUser*/
	
	
	/**
	 * 
	 * @param id
	 * @param newUser
	 * @return Actualiza los datos del usuario con cierto id
	 */
	@PUT
	@Path("{idUser}")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response updateDataUser(@PathParam("idUser") String id, User newUser) {
		try {
			User user1;
			int int_id= Integer.parseInt(id);
			String sql= "SELECT * FROM Usuarios where id=" + int_id + ";";
			System.out.println(sql);
			PreparedStatement ps= conn.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			
			if(rs.next()) {
				user1= UserFromRS(rs);
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}//Obtenemos datos
			
			/**Actualizamos Datos básicos menos el nombre de usuario**/
			user1.setName(newUser.getName());
			user1.setCorreo(newUser.getCorreo());
			user1.setEdad(newUser.getEdad());
			
			sql = "UPDATE `myDeptosdb`.`Usuarios` SET "
					+ "`name`='" + user1.getName() 
					+ "', `email`='" + user1.getCorreo()
					+ "', `edad`='" + user1.getEdad() + "' WHERE `id`="+ int_id+ ";";
			ps= conn.prepareStatement(sql);
			int affectedRows= ps.executeUpdate();
			String location= uriInfo.getBaseUri()+ "usuarios/" + user1.getID();
			return Response.status(Response.Status.OK).entity(user1).header("Content-Location", location).build();			
			
		}catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		}catch (SQLException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se ha encontrado el ID del usuario").build();
		}
		
	}/*updateUser*/
	
	
	
	/**
	 * 
	 * @param id
	 * @return Eliminar el usuario correspondiente de la red
	 */
	@DELETE
	@Path("{idUser}")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response deleteUser(@PathParam("idUser")String id) {
		try {
			User user1;
			int int_id= Integer.parseInt(id);
			String sql = "DELETE FROM `myDeptosdb`.`Usuarios` WHERE `id`='" + int_id + "';";
			PreparedStatement ps = conn.prepareStatement(sql);
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 1)
				return Response.status(Response.Status.NO_CONTENT).build();
			else 
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();		
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo eliminar el Usuario\n" + e.getStackTrace()).build();
		}
	}/*deleteUser*/
	
	

	/**
	 * 
	 * @param patronNombre
	 * @return Lista de usuarios filtrado a través de su nombre 
	 */
	@GET
//	@Path("name")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getUserFilt(@QueryParam("name") @DefaultValue("") String patronNombre) {//valor a defecto para evitar fallo del servicio en caso de que no se envie ningun patron de nombre
		//uriInfoUser= uriInfo.getAbsolutePath();//esto solo esta de prueba
		try {
			String sql = "SELECT * FROM Usuarios order by id";
			PreparedStatement ps= conn.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			
			serv servicio= new serv();
			ArrayList<LinkUser> user= servicio.getUsers();
			rs.beforeFirst();
			while(rs.next()) {
			
				if(rs.getString("name").startsWith(patronNombre)) {
					user.add(new LinkUser(uriInfo.getAbsolutePath() + "/"+ rs.getInt("id"), "self", rs.getString("name")));
				}
				
			}
			return Response.status(Response.Status.OK).entity(servicio).build();
				
		}catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		}catch (SQLException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		}
	}/*getUserFilt*/
	
/********************************************************************** LECTURAS ******************************************************/
	/**
	 * 
	 * @param id
	 * @return Datos del Usuario usando su ID(Nota: como cada usuario solo generará UNA lectua con calificacion
	 * 			no se tendrá en cuenta el id de lectura, bastaría con tener en cuenta el id de usuario)
	 * 			En este caso solo se da por echo que solo se puede realizar una lectura por usuario.
	 * 			--NOta: Si no se filtra por fecha se filtrará por defecto a fechas inferiores de 2021/12/21
	 * 				
	 */
	@GET
	@Path("{id}/lectura")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getLectura(@PathParam("id") String id,@QueryParam("fecha") @DefaultValue("2021-12-21") String fFin) {
		try {
//			if(!usuarioExiste(id)) {
//				return Response.status(Response.Status.NOT_FOUND).build();
//			}
			int int_id = Integer.parseInt(id);
//			System.out.println(fFin);
			String sql = "SELECT * FROM Lecturas where idLectura=" + int_id + " AND fecha_lectura <='"+ fFin+ "';";
//			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			servLectura servicio= new servLectura();
			ArrayList<LinkFriend> friendn= servicio.getLecturas();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int columnsNumber= rsmd.getColumnCount();
//			while (rs.next()) {
//			    for (int i = 1; i <= columnsNumber; i++) {
//			        if (i > 1) System.out.print(",  ");
//			        String columnValue = rs.getString(i);
//			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//			    }
//			    System.out.println("");
//			}
			rs.beforeFirst();
			while(rs.next()) {
				friendn.add(new LinkFriend(uriInfo.getAbsolutePath() + "/"+ rs.getInt("id"), "self", rs.getString("fecha_lectura")));	
			}
			return Response.status(Response.Status.OK).entity(servicio).build();

		} catch (NumberFormatException e) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	
	
	
	
	/**
	 * 
	 * @param id
	 * @param idlectura
	 * @return: Devuelve el recurso del libro de cierto usuario
	 */
	@GET
	@Path("{id}/lectura/{idlectura}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getLectura2(@PathParam("id") String id,@PathParam("idlectura") String idlectura) {
		try {
			if(!usuarioExiste(id)) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			int int_id = Integer.parseInt(id);
			int ind_idLectura = Integer.parseInt(idlectura);
			String sql = "SELECT * FROM Lecturas where id=" + idlectura + " AND idLectura= "+ int_id+";";
//			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int columnsNumber= rsmd.getColumnCount();
//			while (rs.next()) {
//			    for (int i = 1; i <= columnsNumber; i++) {
//			        if (i > 1) System.out.print(",  ");
//			        String columnValue = rs.getString(i);
//			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//			    }
//			    System.out.println("");
//			}
			rs.beforeFirst();
			if (rs.next()) {
				Lectura user= LecturaFromRS(rs);
				return Response.status(Response.Status.OK).entity(user).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}

		} catch (NumberFormatException e) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	
	
	
	
	/**
	 * 
	 * @param libro
	 * @param id
	 * @return Añade lectura de libro de un usuario con calificacion incluida
	 */
	@POST
	@Path("{id}/lectura")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createLectura(Lectura libro, @PathParam ("id") String id ) {
		try {
			int intId= Integer.parseInt(id);
			java.util.Date d= new Date();
			java.sql.Date date= new java.sql.Date(d.getTime());
			String sql = "INSERT INTO `myDeptosdb`.`Lecturas` (`idLectura`, `fecha_lectura`, `calificacion`, `autor`, `categoria`) " + "VALUES ('" + intId + "', '" + date+ "', '" + libro.getCalificacion() + "', '" + libro.getAutor()+ "', '" + libro.getCategoria()+"');";
			PreparedStatement ps= conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int affectedRows= ps.executeUpdate();
			ResultSet generatedID= ps.getGeneratedKeys();
			
			if(generatedID.next()) {
				libro.setIdLectura(generatedID.getInt(1));
				return Response.status(Response.Status.OK).entity(libro).build();
			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear el usuario").build();
	
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No puede añadir lectur").build();
		}catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear el usuario\n" + e.getStackTrace()).build();
		}
	}/*createLectura*/
	
	
	/**
	 * 
	 * @param id
	 * @param idLectura
	 * @return Borra la lectura del usuario
	 */
	@DELETE
	@Path("{id}/lectura/{idlibro}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteLectura(@PathParam("id") String id, @PathParam("idlibro") String idLectura) {
		try {
			int int_id2= Integer.parseInt(idLectura);
			int int_id= Integer.parseInt(id);
			String sql = "DELETE FROM `myDeptosdb`.`Lecturas` WHERE `id`='" + int_id2 + "';";
			PreparedStatement ps = conn.prepareStatement(sql);
			int affectedRows = ps.executeUpdate();
//			Link l = new Link(uriInfo.getAbsolutePath().toString(), "self");
//			int affectedRows = ps.executeUpdate();
			if (affectedRows == 1)
				return Response.status(Response.Status.NO_CONTENT).build();
			else 
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();		

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo eliminar la lectura\n" + e.getStackTrace()).build();
		}
	}/*deleteLectura*/
	
	
	/**
	 * 
	 * @param lectura
	 * @param id
	 * @param idLectura
	 * @return Actualiza las lectura de libros del usuario
	 */
	@PUT
	@Path("{id}/lectura/{idLectura}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateLectura(Lectura lectura, @PathParam("id") String id, @PathParam("idLectura")String idLectura) {
		try {
			Lectura lectUpdate;
			int intId= Integer.parseInt(id);
			int intIdLectura= Integer.parseInt(idLectura);//mirar después 
			java.util.Date d = new Date();
			java.sql.Date date = new java.sql.Date(d.getTime());
			String sql = "SELECT * FROM Lecturas where id=" + intIdLectura + ";";
			//System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				lectUpdate= LecturaFromRS(rs);
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}//Obtenemos datos
			
			lectUpdate.setCalificacion(lectura.getCalificacion());
			lectUpdate.setFechaLectura(lectura.getFechaLectura());
			lectUpdate.setAutor(lectura.getAutor());
			//lectUpdate.setIdLect(lectura.getIdLect());
			lectUpdate.setCategoria(lectura.getCategoria());
			
			//No se actualiza ni el idLectura ni el Id(Importante)
			sql = "UPDATE `myDeptosdb`.`Lecturas` SET " 
					+ "`fecha_lectura`='" + date
					+ "', `calificacion`='" + lectUpdate.getCalificacion()
					+ "', `autor`='" + lectUpdate.getAutor() 
					+ "', `categoria`='" + lectUpdate.getCategoria() + "' WHERE `id`="+ intIdLectura+ ";";
			//System.out.println(sql);
			ps= conn.prepareStatement(sql);
			int affectedRows= ps.executeUpdate();
			String location= uriInfo.getBaseUri()+ "lectura/" + lectUpdate.getIdLectura();
			return Response.status(Response.Status.OK).entity(lectUpdate).header("Content-Location", location).build();			
		}catch (SQLException e) {
			e.getMessage();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo actualizar la lectura del usuario\n" + e.getStackTrace()).build();
		}catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se ha encontrado el id de lectura\n" + e.getStackTrace()).build();
		}
	}/*updateLectura*/
	

/************************************************************************* AMIGOS ****************************************************************/	
	/**
	 * 
	 * @param id
	 * @param limite
	 * @return Devuelve la lista de amigos de cierto usuario
	 */
	@GET
	@Path("{id}/amigos")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getListaAmigos(@PathParam("id") String id, @QueryParam("limite")@DefaultValue("10000") int limite) {
//	System.out.println(uriInfo.getAbsolutePath());
//	System.out.println(uriInfo.getPath());
//	System.out.println(uriInfo.getAbsolutePathBuilder());
//	System.out.println(uriInfo.getBaseUri());
//	System.out.println(uriInfo.getBaseUriBuilder());
		try {
			int intId= Integer.parseInt(id);
			String sql = "SELECT * FROM Amigos where idAmigo = " + intId + " OR idAmistad = " + intId + " ORDER BY idAmigo LIMIT "+ limite+ ";";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			usuarios servicio= new usuarios();
			ArrayList<Link> users = servicio.getUsers();
			
			rs.beforeFirst();
			while (rs.next()) {
				if (rs.getInt("idAmistad") == intId) {		
					users.add(new Link(uriInfo.getAbsolutePath()+ "/" + rs.getInt("idAmigo"),"self"));
				}
				else {
					users.add(new Link(uriInfo.getAbsolutePath()+  "/" + rs.getInt("idAmistad"),"self"));
				}
			}
			return Response.status(Response.Status.OK).entity(servicio).build();
			
		}catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param idAmigo
	 * @return consigue la informacion de un amigo
	 */
	@GET
	@Path("{id}/amigos/{idAmigo}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getListaAmigos2(@PathParam("id") String id, @PathParam("idAmigo") String idAmigo) {
		try {
			int int_id = Integer.parseInt(idAmigo);
			String sql = "SELECT * FROM Usuarios where id=" + int_id + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user= UserFromRS(rs);
				return Response.status(Response.Status.OK).entity(user).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}
		}catch (NumberFormatException e) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	
	
	/**
	 * 
	 * @param solicitud
	 * @param idUser
	 * @return Añade amigo dentro de red de lectura
	 */
	@POST
	@Path("{id}/amigos")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addAmigo(enviaSolicitud solicitud, @PathParam("id") String idUser) {
//		System.out.println(uriInfo.getAbsolutePath());
//		System.out.println(uriInfo.getPath());
//		System.out.println(uriInfo.getAbsolutePathBuilder());
//		System.out.println(uriInfo.getBaseUri());
//		System.out.println(uriInfo.getBaseUriBuilder());
		try {
			int intId= Integer.parseInt(idUser);
			String sql = "SELECT * FROM Usuarios where uname = " + '"' +solicitud.getIdSolicitante()+ '"' + ";";
//			System.out.println(solicitud.getIdSolicitante());
//			System.out.println(solicitud.getMsgSolicitud());
			PreparedStatement ps= conn.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			int id_usuario=0;
			rs.beforeFirst();
			if (rs.next()) {
				id_usuario = rs.getInt(1);
			}
			
			String sql2 = "INSERT INTO `myDeptosdb`.`Amigos` (`idAmigo`, `idAmistad`) " + "VALUES ('" + intId + "', '" + id_usuario  + "');";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			int affectedRows = ps2.executeUpdate();
			rs.beforeFirst();
			if (rs.next()) {		
				LinkUser l = new LinkUser(uriInfo.getBaseUri() + "usuarios/" + id_usuario,"self", rs.getString("name"));
				return Response.status(Response.Status.CREATED).entity(l).build();

//				String location= uriInfo.getBaseUri()+ "usuarios/" + solicitud.getidSolicitado();
//				return Response.status(Response.Status.OK).entity(solicitud).header("Content-Location", location).build();			
			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo enviar la solicitud").build();

		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo crear el mensaje privado\n" + e.getStackTrace()).build();
		}
	}/*addAmigo*/
	
	
	/**
	 * 
	 * @param id
	 * @param idPeticion
	 * @return Elimina amigo de la red 
	 */
	@DELETE
	@Path("{id}/amigos/{id_amigo}")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response deleteAmigo(@PathParam("id") String id, @PathParam("id_amigo") String idPeticion) {
//		System.out.println(uriInfo.getAbsolutePath());
//		System.out.println(uriInfo.getPath());
//		System.out.println(uriInfo.getAbsolutePathBuilder());
//		System.out.println(uriInfo.getBaseUri());
//		System.out.println(uriInfo.getBaseUriBuilder());
		Amigos amigo;
		try {
			int intId= Integer.parseInt(id);
			int intIdPeticion= Integer.parseInt(idPeticion);
//			System.out.println("--------------------------------------------");
//			System.out.println("paso por aquí");
//			System.out.println(intId);
//			System.out.println(intIdPeticion);
			String sql = "SELECT * FROM Amigos where idAmigo = " + intId + " OR idAmistad = " + intIdPeticion + ";";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			if(rs.next()) {
				amigo= AmigoFromRS(rs);//Uso únicamente para comprobar que existe idAmigo e idAmistad en la tabla de Amigos
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("Amigo no encontrado, vuelva a intentarlo").build();
			}
			System.out.println("paso por aquí");
			//si todo va bien realizo el borrado de la lectura del usuario
			sql = "DELETE FROM `myDeptosdb`.`Amigos`" + " WHERE" + " idAmigo = " + intId + " AND idAmistad = "+ intIdPeticion + ";";
			PreparedStatement psNew= conn.prepareStatement(sql);
			int affectdRows = psNew.executeUpdate();
//			System.out.println("paso por aquí");
			usuarios servicio= new usuarios();
			ArrayList<Link> users = servicio.getUsers();
			users.add(new Link(uriInfo.getBaseUri()+"usuarios" + "/"+ rs.getInt("idAmigo") + "/"+ "amigos" ,"self"));//Devuelve uri de los amigos del usuario actual.
//			String location = uriInfo.getAbsolutePath() + "/" + amigo.getIdAmigo();
			return Response.status(Response.Status.OK).entity(servicio).build();			
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se ha encontrado el id del amigo").build();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No se pudo borrar el amigo indicado, Error Interno\n" + e.getStackTrace()).build();
		}
	}/*deleteAmigo*/
	
	
	
/********************************************************* Recomendaciones ************************************************************************/
	/**
	 * 
	 * @param id
	 * @param idPeticion
	 * @return Las recomendaciones se basarán en devolver libros que estén por una:
	 * 			--> calificación(por defecto califaciones >=) mayores a un número dado
	 * 			--> autores(por defecto "todos los autores") según el autor que se desee
	 * 			--> categoría(por defecto "todas las categorías") según la categoría que se desee
	 */
	@GET
	@Path("{id}/amigos/recomendaciones")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
//	public Response libroRecomendado(@PathParam("id") String id, @QueryParam("categoria") @DefaultValue("") String categoria, 
//									@QueryParam("calificacion") @DefaultValue("0") int calificacion, @QueryParam("autor") @DefaultValue("") String autor) {
	public Response libroRecomendado(@PathParam("id") String id, @QueryParam("calificacion") @DefaultValue("0") int calificacion ,
									@QueryParam("autor") @DefaultValue("") String autor,
									@QueryParam("categoria") @DefaultValue("") String categoria) {
		
//		System.out.println(uriInfo.getAbsolutePath());
//		System.out.println(uriInfo.getPath());
//		System.out.println(uriInfo.getAbsolutePathBuilder());
//		System.out.println(uriInfo.getBaseUri());
//		System.out.println(uriInfo.getBaseUriBuilder());
		try {
			int intId= Integer.parseInt(id);
			String sql = "SELECT * FROM Amigos where idAmigo = " + intId + " OR idAmistad = " + intId + ";";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			servRecomendaciones servicio= new servRecomendaciones();
			ArrayList<LinkRecomendaciones> recomendacion= servicio.getRecomendacion();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int columnsNumber= rsmd.getColumnCount();
//			while (rs.next()) {
//			    for (int i = 1; i <= columnsNumber; i++) {
//			        if (i > 1) System.out.print(",  ");
//			        String columnValue = rs.getString(i);
//			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//			    }
//			    System.out.println("");
//			}
			
			rs.beforeFirst();
			while (rs.next()) {
//				if (rs.getInt("idAmistad") == intId) {		
//					users.add(new Link(uriInfo.getBaseUri()+ "usuarios" + "/" + rs.getInt("idAmigo"),"self"));
//				}
//				else {
//					users.add(new Link(uriInfo.getBaseUri()+ "usuarios" + "/" + rs.getInt("idAmistad"),"self"));
//				}
				String sql2= "SELECT * FROM Lecturas where idLectura= " + rs.getInt("idAmistad") + " AND calificacion > '"+ calificacion+ "';";
				System.out.println(sql2);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
//				ResultSetMetaData rsmd = rs2.getMetaData();
//				int columnsNumber= rsmd.getColumnCount();
				
				rs2.beforeFirst();
//				while (rs2.next()) {
//				    for (int i = 1; i <= columnsNumber; i++) {
//				        if (i > 1) System.out.print(",  ");
//				        String columnValue = rs2.getString(i);
//				        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//				    }
//				    System.out.println("");
//				}
				while (rs2.next()) {
//					System.out.println("------------------------------------------------");
//					System.out.println(rs2.getInt("id"));
					if(rs2.getString("autor").startsWith(autor) && rs2.getString("categoria").startsWith(categoria)) {
						recomendacion.add(new LinkRecomendaciones(uriInfo.getAbsolutePath() + "/"+ rs2.getInt("id"), "self", rs2.getInt("calificacion"), rs2.getString("autor"), rs2.getString("categoria")));
					}
				} 
			}/*while*/
			return Response.status(Response.Status.OK).entity(servicio).build();

		}catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("No se pudieron convertir los índices a números").build();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
		
		
	}/*libroRecomendado*/
	
	

	/**
	 * 
	 * @param id
	 * @param idPeticion
	 * @return Devuelve la información del libro recomendado
	 */
	@GET
	@Path("{id}/amigos/recomendaciones/{idamigo}")
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
//	public Response libroRecomendado(@PathParam("id") String id, @QueryParam("categoria") @DefaultValue("") String categoria, 
//									@QueryParam("calificacion") @DefaultValue("0") int calificacion, @QueryParam("autor") @DefaultValue("") String autor) {
	public Response libroRecomendado2(@PathParam("id") String id, @PathParam("idamigo") String idAmigo) {
//		System.out.println(uriInfo.getAbsolutePath());
//		System.out.println(uriInfo.getPath());
//		System.out.println(uriInfo.getAbsolutePathBuilder());
//		System.out.println(uriInfo.getBaseUri());
//		System.out.println(uriInfo.getBaseUriBuilder());
		try {
			if(!usuarioExiste(id)) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			int int_id = Integer.parseInt(id);
			int intidLectura = Integer.parseInt(idAmigo);
			String sql = "SELECT * FROM Lecturas where id=" + intidLectura+";";
//			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			if (rs.next()) {
				Lectura user= LecturaFromRS(rs);
				return Response.status(Response.Status.OK).entity(user).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}

		}catch (NumberFormatException e) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}/*libroRecomendado*/
	

/***************************************************************** Móvil ****************************************************************/
	@GET
	@Path("{id}/movil")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getAppMovil(@PathParam("id") String id) {
//		System.out.println(id);
//		System.out.println(uriInfo.getAbsolutePath());
//		System.out.println(uriInfo.getPath());
//		System.out.println(uriInfo.getAbsolutePathBuilder());
//		System.out.println(uriInfo.getBaseUri());
//		System.out.println(uriInfo.getBaseUriBuilder());
		try {
			int int_id = Integer.parseInt(id);
//			System.out.println("Entro aqui al menos");
			
			/********** Información del usuario ************/
			String sql = "SELECT * FROM Usuarios where id=" + int_id + ";";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			movil movil= new movil();
			if (rs.next()) {
				User user= UserFromRS(rs);
				movil.setUser(user);

			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Elemento no encontrado").build();
			}
			
			/***************** Ultimo libro leido del usuario e info mediante una URI ***********************/
			sql= "SELECT MAX(id) FROM Lecturas where idLectura=" + int_id + ";";
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			servfriendMovil servicio= new servfriendMovil();
			ArrayList<Link> friend= servicio.getFriend();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int columnsNumber= rsmd.getColumnCount();
//			while (rs.next()) {
//			    for (int i = 1; i <= columnsNumber; i++) {
//			        if (i > 1) System.out.print(",  ");
//			        String columnValue = rs.getString(i);
//			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//			        System.out.println("Este es el valor="+ rs.getString("Max(id)"));
//			    }
//			    System.out.println("");
//			}
			rs.beforeFirst();
			while(rs.next()) {
				friend.add(new Link(uriInfo.getBaseUri()+"usuarios/" +int_id+ "/lectura/"+ rs.getString("Max(id)"), "self"));
				movil.setUltimoLibro(friend);
			}
			
			/***************** Numero de amigos del usuario ***********************/
			
			sql = "SELECT * FROM Amigos where idAmigo=" + int_id + " OR idAmistad = " + int_id +";";
//			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.beforeFirst();
			int numeroAmigos= 0;
			while(rs.next()) {
				numeroAmigos++;
			}
			movil.setAmigos(numeroAmigos);
			
			/********** Ultimo libro leido por los amigos del usuario e informacion a través de una uri por cada libro **************/
			
			sql= "SELECT * FROM Amigos where idAmigo=" + int_id + " OR idAmistad = " + int_id +";";
//			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			servFriendMovil2 servicio2= new servFriendMovil2();
			ArrayList<LinkMovil> friend2= servicio2.getFriend();
			rs.beforeFirst();
			while(rs.next()){
				String sql2= "SELECT MAX(id) FROM Lecturas where idLectura=" + rs.getInt("idAmistad") + ";";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				rs2.beforeFirst();
				while(rs2.next()) {
					friend2.add(new LinkMovil(uriInfo.getBaseUri()+"usuarios/" +rs.getInt("idAmistad")+ "/lectura/"+ rs2.getString("Max(id)"), "self",rs.getInt("idAmistad")));
					movil.setListaLectura(friend2);
				}
				
			}
			return Response.ok(movil).build();
		}catch (NumberFormatException e) {
			
			return Response.status(Response.Status.BAD_REQUEST).entity("No puedo parsear a entero").build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error de acceso a BBDD").build();
		}
	}
	

/******************************************************** variables auxiliares **************************************************************/
	
	/**
	 * @
	 * @param rs
	 * @return Función que ayuda a obtener los datos del usuario: siguiendo el mecanismo de GarajesRecurso:)
	 * @throws SQLException
	 */
	private User UserFromRS(ResultSet rs) throws SQLException {
		User user = new User(rs.getString("name"),rs.getString("email"),rs.getInt("edad"), rs.getString("uname"));
		user.setID(rs.getInt("id"));
		return user;
	}
	
	private Lectura LecturaFromRS(ResultSet rs) throws SQLException {
		Lectura lectura = new Lectura(rs.getInt("calificacion"),new Date(),rs.getInt("idLectura"), rs.getString("autor"), rs.getString("categoria"));
		lectura.setIdLectura(rs.getInt("id"));;
		return lectura;
	}
	
	private Amigos AmigoFromRS(ResultSet rs)throws SQLException{
		Amigos amigos= new Amigos(rs.getString("idAmigo"), rs.getString("idAmistad"));
		amigos.setIdAmigo(rs.getString("idAmigo"));
		return amigos;
	}
//	
//	private enviaSolicitud SolicitudFromRS(ResultSet rs) throws SQLException {
//		enviaSolicitud solicitud= new enviaSolicitud(rs.getString("idAmigo"), rs.getString("idAmistad"));
//	}
	
//	public Lectura(int idLect, int idUser, Date fechaLectura, int calificacion) {
//		super();
//		this.idUser= idUser;
//		this.idLect= idLect;
//		this.fechaLectura= fechaLectura;
//		this.calificacion= calificacion;
//	}
	/**
	 * 
	 * @param id
	 * @return Se ha seguido el modelo encontrado en esta página https://qastack.mx/dba/125886/check-if-a-user-exists-in-a-sql-server-database
	 * 			sirve para comprobar si el id del usuario existe en la base de datos
	 */
	public boolean usuarioExiste(String id) {
		try {
			String sql = "SELECT count(*) FROM myDeptosdb.Usuarios WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if (rs.getInt("count(*)") > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			return false;
		}

	}/*usuarioExiste*/
	
}//de API
