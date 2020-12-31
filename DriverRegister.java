package Test;
import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

@Path("/hellodriver")
public class DriverRegister {	
	static Connection conn1 = null;
	static PreparedStatement pstmt1 = null;
	static Statement stmt1=null;
	static String driver_latitude;
	static String driver_longitude;
	static int index = 0;
	static LatLongs driver_location = new LatLongs();
	
	static List<LatLong> tempList = new ArrayList() ;
	
	static LatLong driver_loc = new LatLong();
	static ResultSet rs=null;
	static HashMap<String,String> map = new HashMap<>();
	static getSetRegister g = new getSetRegister(); 
	
	// ------------------------------------------------  Driver Registration  ------------------------------------------------  
	
	@Path("/driverregister")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static void sayHelloDriver(@QueryParam("username") String username,@QueryParam("password") String password,@QueryParam("emailid") String email)
	{
		System.out.println("Driver new Username to be Added: "+ username);
		g.setEmail(email);
		g.setName(username);
		g.setPassword(password);
		
		System.out.println("Email-Id: "+g.getEmail());
		map.put("username", username);
		map.put("password", password);
		map.put("emailid", email);
		
		insertData1(username,email,83765,12345,password);
	}
	
	public static int insertData1(String username,String email,int phone,int aadhar,String password) 
	{
		System.out.println("Insert driver data in DB");
		try
		{
			System.out.println("before driver");
			
			conn1 = Javaconnect.ConnecrDB();
			
			System.out.println("Connected");
			stmt1 = conn1.createStatement();
			
			String sql1 = "INSERT INTO DriverRegister " +
		                   "VALUES ('"+username+"','"+email+"',"+ phone+","+aadhar+",'"+password+"')";
		      stmt1.executeUpdate(sql1);
		      System.out.println("Doneeee");
		      return 1;
		}
		catch(Exception e)
		{
			return 0;
		}
		
	}
	// ========================================================================================================================	

	
	// -----------------------------------------------------------
	
	@Path("/driverDoc")
	@GET
	public static void getDoc(@QueryParam("file") String username)
	{
		System.out.println("reached here");
		System.out.println("heelo "+ username);
	}
	// ------------------------------------------------------------
	
	
	// ------------------------------------------- Driver Login - WEBSITE -----------------------------------------------------
	
	@Path("/driverlogin/website")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static int login2(@QueryParam("username") String username,@QueryParam("password") String password,@QueryParam("Latitude") String Latitude,@QueryParam("Longitude") String Longitude)
	{
		String pass="";
		String phone="";
		System.out.println("Driver logged in - "+username+" "+Latitude+" "+Longitude);
//		driver_latitude = Latitude;
//		 driver_longitude = Longitude;
		
		driver_loc.latitude = Latitude;
		driver_loc.longitude = Longitude;
		driver_loc.driverName = username;
	
		//System.out.println("inside login lat: "+driver_location.getLatlng().get(0).latitude+"  "+driver_location.getLatlng().get(0).longitude);
		
		conn1=Javaconnect.ConnecrDB();
		//System.out.println("Connected");
		String sqlLogin="Select Password,PhoneNo from DriverRegister where Name=?";

		try
		{
			pstmt1 = conn1.prepareStatement(sqlLogin);
			pstmt1.setString(1, username);
			rs = pstmt1.executeQuery();
			
			while(rs.next())
			{
				pass = rs.getString("Password");
				phone = rs.getString("PhoneNo");
				System.out.println(pass);
				driver_loc.phone = phone;
				if(pass.equals(password))
				{
					tempList.add(driver_loc);
					driver_location.setLatlng(tempList);
					return 1;
				}
			}
			
		}                                                                                                                                                              									                                            																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																										
		catch(Exception e)
		{
			//e.printStackTrace();
			return 0;
		}
		return 99;
	}
	
	// -------------------------------------------------- MOBILE  ----------------------------------------------------------------
	
	@Path("/driverlogin/mobile")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static int login1(@QueryParam("username") String username,@QueryParam("password") String password,@QueryParam("Latitude") String Latitude,@QueryParam("Longitude") String Longitude)
	{
		String pass="";
		String phone="";
		System.out.println("Driver logged-in using mobile "+username+" "+Latitude+" "+Longitude);
//		driver_latitude = Latitude;
//		driver_longitude = Longitude;
		
		driver_loc.latitude = Latitude;
		driver_loc.longitude = Longitude;
		driver_loc.driverName = username;
		
		System.out.println("lat :"+Latitude);
		
		conn1=Javaconnect.ConnecrDB();
		System.out.println("Connected");
		String sqlLogin="Select Password,PhoneNo from DriverRegister where Name=?";

		try
		{
			pstmt1 = conn1.prepareStatement(sqlLogin);
			pstmt1.setString(1, username);
			rs = pstmt1.executeQuery();
			//System.out.println("meghana");
			while(rs.next())
			{
				pass = rs.getString("Password");
				phone = rs.getString("PhoneNo");
				System.out.println(pass);
				driver_loc.phone = phone;
				if(pass.equals(password))
				{
					tempList.add(driver_loc);
					driver_location.setLatlng(tempList);
					return 1;
				}
			}
			System.out.println("inside login lat: "+driver_location.getLatlng().get(0).latitude+"  "+driver_location.getLatlng().get(0).longitude);
		}                                                                                                                                                              									                                            																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																										
		catch(Exception e)
		{
			//e.printStackTrace();
			return 0;
		}
		return 99;

	}
	// ===================================================================================================================
	
	
	
	// --------------------------------------------- Send driver latlong WEBSITE -----------------------------------------
	
	@Path("/sendDriverLatLongForCal")
	@POST	
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public static List<LatLong> forgotten1(String sourceToMultiple) throws IOException{
			
		String[]  array = sourceToMultiple.split(",");
		int[] ar = new int[array.length];
		System.out.println("Distance matrix: ");
		 for(int i=0;i<array.length;i++)
	       {
	           System.out.print(" "+array[i]);
	       } 
		System.out.println();
		int max = 0;
		       
		System.out.println("size"+array.length);
		String d = "f";
		for(int i=0;i< array.length;i++)
		{
		//System.out.println( matrix.getMatrix().size()+"   ");
		
			if(array[i].contains("mins"))
			{
				 d=array[i].replaceAll(" mins","");
			}
			else if(array[i].contains("min"))
			{
				 d=array[i].replaceAll(" min","");
			}
			
			int num = Integer.parseInt(d);
			//System.out.println("mun: "+num);
			ar[i] = num;
			//System.out.println("ar[i]: "+ar[i] + "i: "+i);
		}		
		
		
		for(int i=0;i<array.length;i++)
		{
			int value = ar[i];
			if(max < value)
			{
				max = value;
				index = i;
			}
		}
		System.out.println("printing the maximum location's place "+index);
		
//		System.out.println("sending driver lat long for calculation to Front end ");
//		System.out.println(driver_location.getLatlng().get(0).latitude);
//		System.out.println(driver_location.getLatlng().get(0).longitude);
//		System.out.println(driver_location.getLatlng().get(0).driverName);
//		System.out.println(driver_location.getLatlng().get(0).phone);
//		
		return driver_location.getLatlng();
	}
	
	// ------------------------------------------------------ Mobile --------------------------------------------------------------
	
	@Path("/mobile/sendDriverLatLongForCal")
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static List<LatLong> forgottenMobile(String sourceToMultiple) throws IOException{
		
		System.out.println("sending driver lat long for calculation to Front end "+sourceToMultiple);
		
		String[]  waste = sourceToMultiple.split(":");
		String[]  array_w = waste[1].split("\"");
		String[]  array = array_w[1].split(",");
		int[] ar = new int[array.length];
		
		int max = 0;
		       
		System.out.println("size"+array.length);
		String d = "f";
		for(int i=0;i< array.length;i++)
		{
		//System.out.println( matrix.getMatrix().size()+"   ");
		
			if(array[i].contains("mins"))
			{
				 d=array[i].replaceAll(" mins","");
				 System.out.println("d: "+d);
			}
			else if(array[i].contains("min"))
			{
				 d=array[i].replaceAll(" min","");
				 System.out.println("d: "+d);
			}
			
			int num = Integer.parseInt(d);
			//System.out.println("mun: "+num);
			if(num == 1)
				num = 0;
			ar[i] = num;
			//System.out.println("ar[i]: "+ar[i] + "i: "+i);
		}		
		
		
		for(int i=0;i<array.length;i++)
		{
			int value = ar[i];
			if(max < value)
			{
				max = value;
				index = i;
			}
		}
		System.out.println("printing the maximum location's place "+index);
		
		System.out.println(driver_location.getLatlng().get(0).latitude);
		System.out.println(driver_location.getLatlng().get(0).longitude);
		System.out.println(driver_location.getLatlng().get(0).driverName);
		System.out.println(driver_location.getLatlng().get(0).phone);
		
		return driver_location.getLatlng();
	}
	// ==============================================================================================================================
	
}
