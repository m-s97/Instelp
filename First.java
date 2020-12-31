package Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import jdk.nashorn.internal.parser.JSONParser;

@Path("/hello1")
public class First {

	getSetRegister g = new getSetRegister(); 
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String,String> sayHello(@QueryParam("id") int id)
	{
		System.out.println("received is: "+ id);
		//g.setEmai);
		
		System.out.println(g.getEmail()+" "+g.getName());
		
		//JSONParser parse = new JSONParser();
		
		//JSONObject jobj = (JSONObject)parse.parse(inline);
		
		HashMap<String,String> map = new HashMap<>();
		map.put("usernumber", Integer.toString(id));
//		map.put("password", password);
//		map.put("emailid", email);
		
		
		//return g;
		return map;
		//return Response.ok().entity(toJson(g)).build();
		
		//return Response.status(Response.Status.OK)
               // .entity("returning username: " + username + " email and password " + email+" "+password)
               // .build();
	}
	//,"application/json"
//	 @POST	
//	 @Produces(MediaType.APPLICATION_JSON)
//	 @Consumes("application/json") 
//	   public String createUser(@QueryParam("id") String id) throws IOException{
//	      System.out.println("id: "+id);
//		 return id;
//		 
//
//}
	
//	 @POST	
//	 @Produces(MediaType.APPLICATION_JSON)
//	 @Consumes("application/json") 
//	 public Map<String,String> sayHelloPost(@QueryParam("username") String username,@QueryParam("password") String password,@QueryParam("emailid") String email)
//	 {
//		 System.out.println("received is: "+ username);
//		g.setEmail(email);
//		g.setName(username);
//		g.setPassword(password);
//		
//		System.out.println(g.getEmail()+" "+g.getName());
//		
//		//JSONParser parse = new JSONParser();
//		
//		//JSONObject jobj = (JSONObject)parse.parse(inline);
//		
//		HashMap<String,String> map = new HashMap<>();
//		map.put("username", username);
//		map.put("password", password);
//		map.put("emailid", email);
//		
//		
//		//return g;
//		return map;
//		 
//
//}
}
