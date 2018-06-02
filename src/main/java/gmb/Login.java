package gmb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn;
	Login service = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		 
		try {
		      String url;

		      Properties properties = new Properties();
		      try {
		        properties.load(
		            getServletContext().getResourceAsStream("/WEB-INF/classes/config.properties"));
		        
		        url = properties.getProperty("sqlUrl");
		      } catch (IOException e) {
		        log("no property", e);  // Servlet Init should never fail.
		        return;
		      }

		      log("connecting to: " + url);
		      try {
		        Class.forName("com.mysql.cj.jdbc.Driver");
		        conn = DriverManager.getConnection(url);
		        
				  
		      } catch (ClassNotFoundException e) {
		        throw new ServletException("Error loading JDBC Driver", e);
		      } catch (SQLException e) {
		        throw new ServletException("Unable to connect to PostGre", e);
		      }

		    } finally {
		      // Nothing really to do here.
		    }
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Autentica service = new Autentica();
		String username = request.getParameter("username");
        String password = request.getParameter("password");
         
        String message = service.doLogin(username, password, conn);
        response.getWriter().write(message);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
