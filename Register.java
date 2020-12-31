// Api Testing - done at message layer
package Test;
import java.sql.*;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/hello")
public class Register {
	//static JsonObject jsonObject;
	List<Object[]> list;
	static String latitude;
	static String longitude;
	static JsonObjects cart1;
	static Boolean cartflag = false;
	static Boolean forgotflag = false;
	static DriverRegister dr = new DriverRegister();
	
													// for sending complete data to driver
	static SendDetailsToDriverList sd = new SendDetailsToDriverList();
	static sendDetailsToDriver send = new sendDetailsToDriver();
	
	static MongoClient mongoclient;
	static Forgottens pro;
	static Optimized_path opt = new Optimized_path();
	
	public static String DATABASE = "Delivery";
	public static String DATABASEURL = "jdbc:mysql://localhost:3306/Delivery";
	public static String DRIVER = "com.mysql.jdbc.Driver";
	public static String PASSWORD = "root";
	
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	static Statement stmt=null;
	static String tablename=null;
	static ResultSet rs=null;
	static HashMap<String,String> map = new HashMap<>();
	static getSetRegister g = new getSetRegister(); 
	
	// ====================================================== FOR BOTH (REGISTER) ==================================================
	
	@Path("/register")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static void sayHello(@QueryParam("username") String username,@QueryParam("password") String password,@QueryParam("emailid") String email)
	{
		System.out.println("received is: "+ username);
		g.setEmail(email);
		g.setName(username);
		g.setPassword(password);
		
		System.out.println(g.getEmail()+" "+g.getName());
		map.put("username", username);
		map.put("password", password);
		map.put("emailid", email);
		
		insertData(username,email,83765,password);
	
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	
	public static String insertData(String username,String email,int phone,String password) 
	{
		System.out.println("Inside insert");
		try
		{
			System.out.println("before driver");
			//Class.forName(DRIVER);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver");
			conn = DriverManager.getConnection(DATABASEURL,"root","root");
			System.out.println("Connected");
			stmt = conn.createStatement();
		      
		      String sql = "INSERT INTO registration " +
		                   "VALUES ('"+username+"', '"+email+"',"+ phone+",'"+password+"')";
		      stmt.executeUpdate(sql);
		      System.out.println("Doneeee");
		      return "Registered";
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return "Fail";
		}
		
	}
	// -----------------------------------------------------------------------------------------------------------------------------
	
	// ========================================= LOGIN (FOR BOTH) =====================================================================
	
	@Path("/login")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static int login(@QueryParam("username") String username,@QueryParam("password") String password, @QueryParam("Latitude") String Latitude,@QueryParam("Longitude") String Longitude)
	{
		String pass="";
		latitude = Latitude;
		longitude = Longitude;
		
		System.out.println("inside login lat: "+latitude+" "+longitude);
		conn=Javaconnect.ConnecrDB();
		System.out.println("Connected");
		String sqlLogin="Select password from registration where Name=?";

		try
		{
			pstmt = conn.prepareStatement(sqlLogin);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			//System.out.println("meghana");
			while(rs.next())
			{
				pass = rs.getString("Password");
				System.out.println(pass);
				if(pass.equals(password))
				{
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
//	----------------------------------------------------------------------------------------------------------------------------
	
// =================================================== FOR BOTH (ACCEPTING CART) ===============================================================
	
	@Path("/finalCart")
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static List<JsonObject> cart(JsonObjects cart) throws Exception{
		cart1=cart;
		cartflag = true;
		System.out.println("cartflag is set true");
		System.out.println("cart flag set: "+cartflag);
		
		for(int i=0;i<cart.getCart().size();i++)
		{
			System.out.println(cart.getCart().get(i).placeName);			
			System.out.println(cart.getCart().get(i).placeLat);
			System.out.println(cart.getCart().get(i).placeLong);
			System.out.println(cart.getCart().get(i).placeLocation);
			for(int j=0;j<cart.getCart().get(i).menu.size();j++)
			{
				System.out.println(cart.getCart().get(i).menu.size());
				System.out.println(cart.getCart().get(i).menu.get(j).productName);
				System.out.println(cart.getCart().get(i).menu.get(j).productPrice);
				System.out.println(cart.getCart().get(i).menu.get(j).quantity);
			}
			
		}
		
		// SENDING CART TO MONGO DB (java mongo connectivity)..
		
		MongoClient mongoclient = new MongoClient("localhost",27017);
		System.out.println("Connected Successfully to Mongo");
		//DB db = mongoclient.getDB("delivery"); 
		
		MongoDatabase database = mongoclient.getDatabase("instelp");  
//		
//	      // Creating a collection 
	      System.out.println("Collection created successfully"); 

//	      // Retrieving a collection
	      MongoCollection<Document> collection = database.getCollection("cart"); 
	      System.out.println("Collection myCollection selected successfully"+database.getName()); 
	      
	      Document doc = new Document();
	      
	      
	      //doc= new BasicDBObject();
	      for(int i=0;i<cart.getCart().size();i++)
			{
	    	  Document docu = new Document();
	    	  docu.append("placeName", cart.getCart().get(i).placeName);
	    	  docu.append("placeLat",cart.getCart().get(i).placeLat);
	    	  docu.append("placeLong",cart.getCart().get(i).placeLong);
	    	  docu.append("placeLocation",cart.getCart().get(i).placeLocation);
				for(int j=1;j<=cart.getCart().get(i).menu.size();j++)
				{
					Document doc1 = new Document();
					doc1.append("productName",cart.getCart().get(i).menu.get(j-1).productName);
					doc1.append("productPrice",cart.getCart().get(i).menu.get(j-1).productPrice);
					doc1.append("quantity",cart.getCart().get(i).menu.get(j-1).quantity);
					docu.append("PRODUCT "+j, doc1);
				}
				doc.append("FROM "+cart.getCart().get(i).placeName, docu);
			}
	     collection.insertOne(doc);
	     System.out.println("Cart inserted in Database");
		return cart.getCart();

	}
	// -------------------------------------------------------------------------------------------------------------------------------
	
	// ============================================== FOR BOTH (ACCEPT FORGOTTEN DETAILS) ==================================================
	
	@Path("/forgotten")
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static List<Forgotten> forgotten1(Forgottens product) throws IOException{
		
		System.out.println("Delivery of Personal Belonging method called ");
		forgotflag = true;
		System.out.println();
		System.out.println("Details Received: ");
		System.out.println();
		
			System.out.println(product.getProduct().get(0).typeOfProduct);
			System.out.println(product.getProduct().get(0).sizeOfProduct);
			System.out.println(product.getProduct().get(0).weightOfProduct);
			System.out.println(product.getProduct().get(0).srcAddr);
			System.out.println(product.getProduct().get(0).destAddr);
			System.out.println(product.getProduct().get(0).destLat);
			System.out.println(product.getProduct().get(0).destLong);
			//return ;
		
		pro = product;
		
		// for driver
		sendDetailsToDriver temp = new sendDetailsToDriver();
		List<sendDetailsToDriver> toDriver = new ArrayList();
		
		temp.sizeOfProduct = pro.getProduct().get(0).sizeOfProduct;
		temp.weightOfProduct = pro.getProduct().get(0).weightOfProduct;
		temp.typeOfProduct = pro.getProduct().get(0).typeOfProduct;
		temp.srcAddr = pro.getProduct().get(0).srcAddr;
		temp.srcLat = pro.getProduct().get(0).srcLat;
		temp.srcLong = pro.getProduct().get(0).srcLong;
		temp.destAddr = pro.getProduct().get(0).destAddr;
		temp.destLat = pro.getProduct().get(0).destLat;
		temp.destLong = pro.getProduct().get(0).destLong;

		toDriver.add(temp);
		sd.setDetails(toDriver);

		
		
		return product.getProduct();
	}
// -----------------------------------------------------------------------------------------------------------------------------------
	
// =================================================================================================== USED WHERE ???????

	@Path("/sendCart")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public static List<JsonObject> sendcart() throws IOException{
		
		System.out.println("inside cart ");

		return cart1.getCart();
	}
	
// ==========================================================  FOR MOBILE ===============================================================
	
	
	@Path("/mobile/sendCoordinatesToDriver")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static List<JsonObject> sendCoordinatesToDriverMobile() throws IOException{
		System.out.println("Mobile call for Rearranging the cart ");
		JsonObjects cart2 = new JsonObjects();
		System.out.println(" inside sendCoordinatesToDriver ");
		//+nouse.getCart().get(1).placeName);
		List<JsonObject> Lst = new ArrayList();
		System.out.println("fake lst"+Lst.size());
		
		String path[] = opt.sendAdjtoReg();
		for(int i=0;i<path.length - 1 ;i++)
		{
			System.out.println(" is it here  "+path[i]);
		}
		System.out.println("======================");
		System.out.println(cart1.getCart().size());
		System.out.println("----"+cart1.getCart().get(0).placeName);
		System.out.println(cart1.getCart().get(0).placeName);
		System.out.println(cart1.getCart().get(0).placeName);
		for(int i=0;i<path.length - 1;i++)
		{
//			System.out.println("inside for loop");
			
			int x = Integer.parseInt(path[i]);
			System.out.println("x: "+x);
			JsonObject obj = new JsonObject();
			obj.menu = new ArrayList<MenuJson>();
			System.out.println(" obj.placename "+cart1.getCart().get(x).placeName);
			obj.placeName = cart1.getCart().get(x).placeName;
			System.out.println("2 "+obj.placeName);
			obj.placeLat = cart1.getCart().get(x).placeLat;
			obj.placeLong = cart1.getCart().get(x).placeLong;
			obj.placeLocation = cart1.getCart().get(x).placeLocation;
			obj.cust_lat = latitude;
			obj.cust_long = longitude;
			obj.flag = "cart";
			
			for(int j=0;j<cart1.getCart().get(x).menu.size();j++)
			{
				MenuJson mj = new MenuJson();
				mj.productName = cart1.getCart().get(x).menu.get(j).productName;
				mj.quantity = cart1.getCart().get(x).menu.get(j).quantity;
				mj.productPrice = cart1.getCart().get(x).menu.get(j).productPrice;
				
				obj.menu.add(mj);
			}
			Lst.add(obj);
			
	//		System.out.println("proper lst "+Lst.size()+" ");
		}
		cart2.setCart(Lst);
		return cart2 .getCart();
		//return 1;
	}
//---------------------------------------------------------------------------------------------------------------------------------------------------- 
	
//	==========================================================  FOR WEBSITE ===============================================================
	
	@Path("/sendCoordinatesToDriver")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public static List<JsonObject> sendCoordinatesToDriver(JsonObjects nouse) throws IOException{
		System.out.println("***************************");
		JsonObjects cart2 = new JsonObjects();
		System.out.println("inside sendCoordinatesToDriver......");
		List<JsonObject> Lst = new ArrayList();
		//System.out.println("fake lst"+Lst.size());
		
		String path[] = opt.sendAdjtoReg();
		for(int i=0;i<path.length - 1 ;i++)
		{
			System.out.println(" is it here  "+path[i]);
		}
		System.out.println("======================");
		System.out.println(cart1.getCart().size());
		System.out.println("----"+cart1.getCart().get(0).placeName);
		System.out.println(cart1.getCart().get(0).placeName);
		System.out.println(cart1.getCart().get(0).placeName);
		for(int i=0;i<path.length - 1;i++)
		{
			//System.out.println("inside for loop");
			
			int x = Integer.parseInt(path[i]);
			System.out.println("x: "+x);
			JsonObject obj = new JsonObject();
			obj.menu = new ArrayList<MenuJson>();
			System.out.println(" obj.placename "+cart1.getCart().get(x).placeName);
			obj.placeName = cart1.getCart().get(x).placeName;
			System.out.println("2 "+obj.placeName);
			obj.placeLat = cart1.getCart().get(x).placeLat;
			obj.placeLong = cart1.getCart().get(x).placeLong;
			obj.placeLocation = cart1.getCart().get(x).placeLocation;
			obj.cust_lat = latitude;
			obj.cust_long = longitude;
			obj.flag = "cart";
			
			
			for(int j=0;j<cart1.getCart().get(x).menu.size();j++)
			{
				MenuJson mj = new MenuJson();
				mj.productName = cart1.getCart().get(x).menu.get(j).productName;
				mj.quantity = cart1.getCart().get(x).menu.get(j).quantity;
				mj.productPrice = cart1.getCart().get(x).menu.get(j).productPrice;
				
				obj.menu.add(mj);
			}
			
			Lst.add(obj);
			
			//System.out.println("proper lst "+Lst.size()+" ");
		}
		cart2.setCart(Lst);
		return cart2 .getCart();
	}
//	
//	----------------------------------------------------------------------------------------------------------------------------------------

//	==========================================================  SAME FOR MOBILE & WEB ===============================================================
	
	@Path("/sendForgotten")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public static List<Forgotten> sendForgotten() throws IOException{
		
		System.out.println("Details to driver for Parcel Module Sent");
				
		return pro.getProduct();
	}
	
//------------------------------------------------------------------------------------------------------------------------------------------
	
	public static List<JsonObject> cartToOptimizedPath() throws IOException{
		
		System.out.println("inside cart ");

		return cart1.getCart();
	}
	
// =================================================  SAME FOR MOBILE & WEB (SEND FLAG) ============================================================
	
	@Path("/sendflag")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static int sendFlag(String tp) throws IOException{
		
		System.out.println("inside send flag (cart/forgotten) to driver "+tp);
 
		if(cartflag == true)
		{
			System.out.println("cart flag: "+cartflag);
			//cartflag = false;
			System.out.println("cart flag after: "+cartflag);
			return 1;
		}
		else if(forgotflag == true)
		{
			forgotflag = false;
			return 0;
		}
		else
		{
			return 99;
		}
	}
	
// ---------------------------------------------------------------------------------------------------------------------------------------
	
	
// ================================================ FOR Mobile & WEBSITE SEND EVERY DETAILS ===============================================================


	@Path("/sendCompleteDetails")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static List<sendDetailsToDriver> senDetails() throws IOException{
		
		System.out.println("Details to driver sent  ");
				
		return sd.getDetails();
	}
	
// ---------------------------------------------------------------------------------------------------------------------------------------
		
	
	
// ------------------------------------------  Cart Arranged According to Location For Driver   ------------------------------------------
	
	public static void cartArranged(String path[])
	{
		System.out.println("in cart arranged");
		List<sendDetailsToDriver> toDriver = new ArrayList();
		int num = dr.index;
		System.out.println("index returned: "+num);
		int[] x = new int[path.length - 1];

		for(int i=0;i<path.length - 1 ;i++)
		{
			System.out.println(" is it here string path[]  "+path[i]);
			x[i] = Integer.parseInt(path[i]);
			System.out.println(" int x: "+x[i]);			
		}
		System.out.print("======================");
		System.out.println(cart1.getCart().size());
			
		for(int i=0;i<path.length - 1;i++)
		{
				System.out.println("inside for loop "+x[i]);
				
				// for details to driver 
				sendDetailsToDriver temp = new sendDetailsToDriver();
				if(x[i] == 0)
				{
					temp.menu = new ArrayList<MenuJson>();
					temp.placeName = cart1.getCart().get(num).placeName;
					System.out.println("name of place: "+temp.placeName);
					temp.placeLat = cart1.getCart().get(num).placeLat;
					temp.placeLong = cart1.getCart().get(num).placeLong;
					temp.placeLocation = cart1.getCart().get(num).placeLocation;
					temp.cust_lat = latitude;
					temp.cust_long = longitude;
					temp.flag = "cart";
					
					for(int j=0;j<cart1.getCart().get(num).menu.size();j++)
					{
						MenuJson mj = new MenuJson();
						mj.productName = cart1.getCart().get(num).menu.get(j).productName;
						mj.quantity = cart1.getCart().get(num).menu.get(j).quantity;
						mj.productPrice = cart1.getCart().get(num).menu.get(j).productPrice;
						
						temp.menu.add(mj);
					}
					toDriver.add(temp);
				}
				else if(x[i] == num)
				{
					temp.menu = new ArrayList<MenuJson>();
					temp.placeName = cart1.getCart().get(0).placeName;
					System.out.println("name of place: "+temp.placeName);
					temp.placeLat = cart1.getCart().get(0).placeLat;
					temp.placeLong = cart1.getCart().get(0).placeLong;
					temp.placeLocation = cart1.getCart().get(0).placeLocation;
					temp.cust_lat = latitude;
					temp.cust_long = longitude;
					temp.flag = "cart";
					
					for(int j=0;j<cart1.getCart().get(0).menu.size();j++)
					{
						MenuJson mj = new MenuJson();
						mj.productName = cart1.getCart().get(0).menu.get(j).productName;
						mj.quantity = cart1.getCart().get(0).menu.get(j).quantity;
						mj.productPrice = cart1.getCart().get(0).menu.get(j).productPrice;
						
						temp.menu.add(mj);
					}
					toDriver.add(temp);
				}
				else
				{
					temp.menu = new ArrayList<MenuJson>();
					temp.placeName = cart1.getCart().get(x[i]).placeName;
					System.out.println("name of place: "+temp.placeName);
					temp.placeLat = cart1.getCart().get(x[i]).placeLat;
					temp.placeLong = cart1.getCart().get(x[i]).placeLong;
					temp.placeLocation = cart1.getCart().get(x[i]).placeLocation;
					temp.cust_lat = latitude;
					temp.cust_long = longitude;
					temp.flag = "cart";
					
					for(int j=0;j<cart1.getCart().get(x[i]).menu.size();j++)
					{
						MenuJson mj = new MenuJson();
						mj.productName = cart1.getCart().get(x[i]).menu.get(j).productName;
						mj.quantity = cart1.getCart().get(x[i]).menu.get(j).quantity;
						mj.productPrice = cart1.getCart().get(x[i]).menu.get(j).productPrice;
						
						temp.menu.add(mj);
					}
					toDriver.add(temp);
				}
			}
			sd.setDetails(toDriver);	
			
// ------------------- sd is set with value----------------------------------------------------------------------------------			
	}
}

