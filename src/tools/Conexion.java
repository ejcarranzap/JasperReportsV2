package tools;

import java.sql.Connection;
import java.sql.DriverManager;
 
public class Conexion {
	
	
	public static String cnString = "jdbc:postgresql://localhost:5436/test";
	public static String user = "admin";
	public static String pass = "1234";
	public static String clsName = "org.postgresql.Driver";
	/*public static String cnString = "jdbc:jtds:sqlserver://192.200.9.131:1433;DatabaseName=DB_GENESIS_CENTRAL;integratedSecurity=true";
	public static String user = "sa";
	public static String pass = "bofasa1$";
	public static String clsName = "net.sourceforge.jtds.jdbc.Driver";*/
 
	public static Connection conectar() {
		Connection con = null;
		//System.out.println("Start connection.");
		try {
			
			Class.forName(clsName);		
			con = DriverManager.getConnection(cnString,user,pass);
			if (con != null) {
				//System.out.println("Connection complete");
			}
 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return con;
	}
}
