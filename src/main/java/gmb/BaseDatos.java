package gmb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.servlet.ServletException;

public class BaseDatos {
	Connection conn;
	private static final Logger log = Logger.getLogger(BaseDatos.class.getName());
	
	/**
	 * Execute an SQL statement weather is an INSERT or SELECT statement
	 * 
	 * @param SQL: String with the statement
	 * @param conn: database connection
	 * @throws SQLException
	 */
	
	public void EjecutarSQL(String SQL, Connection conn) throws SQLException {
		
		Statement secuencia = conn.createStatement(); 
		String CabeceraConsulta = SQL.substring(0, 6);
		if (CabeceraConsulta.equals("INSERT")) {
			secuencia.executeUpdate(SQL);
		}
		if (CabeceraConsulta.equals("SELECT")) {
			secuencia.executeQuery(SQL);
		}
		 
	}
	
	
	/**
	 * Connects to the SQL database of the project
	 * 
	 * @return Connection: database connection
	 * @throws ServletException
	 * @throws ClassNotFoundException
	 */
	
	public Connection ConectarDB() throws ServletException, ClassNotFoundException {
		//Método cuyo objetivo es conectarse a una base de datos desde un servlet
		
	    String url = System.getProperty("cloudsql");
	    
	    try {
	    	
	    	url = "jdbc:mysql://google/gmb_db?useSSL=false&amp;cloudSqlInstance=mov-prod3:europe-west1:mov-gmb&amp;socketFactory=com.google.cloud.sql.mysql.SocketFactory&amp;user=root&amp;password=FAYO0173";
	        url = "jdbc:mysql://google/gmb_db?useSSL=false&cloudSqlInstance=mov-prod3:europe-west1:mov-gmb&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=FAYO0173";
	        //url = "jdbc:mysql://google/gmb_db?cloudSqlInstance=mov-prod3:europe-west1:mov-gmb&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=FAYO0173&useSSL=false";
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	log.info("connecting to: " + url);
	        conn = DriverManager.getConnection(url);   
			  
	    } catch (SQLException e) {
	    	System.out.println("Unable to connect to Database");
	        System.out.println(e);  
	        
	    }
	    return conn;
	}

}
