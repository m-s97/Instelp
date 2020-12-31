package Test;
import java.sql.*;

public class Javaconnect {
    Connection conn=null;
    static String JDBC_DRIVER="com.mysql.jdbc.Driver";
    
    public static Connection ConnecrDB()
    {
        try{
            Class.forName(JDBC_DRIVER);
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Delivery","root","root");
            return conn;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
