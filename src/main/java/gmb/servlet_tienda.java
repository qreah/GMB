package gmb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class servlet_tienda
 */
@WebServlet("/servlet_tienda")
public class servlet_tienda extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servlet_tienda() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		RedTiendas tiendas = new RedTiendas();
		String codigo = request.getParameter("codigo");
		qhrea Q = new qhrea();
		ServletContext contexto = request.getServletContext();
		String PropiedadesConexion = "/WEB-INF/classes/config.properties";
		BaseDatos DB = new BaseDatos();
		Q.enviarMail("rafael.fayosoliver@telefonica.com", "Tiendas", "Alguien trata de acceder a la aplicación");
		
		if (codigo.equals("34RT6")) {
			
			
			Connection conn;
			try {
				conn = DB.ConectarDB();
				response.setHeader("Content-Disposition",
	                     "attachment;filename=valoraciones.csv");
				//StringBuilder sb = tiendas.ObtenerValoraciones(conn);
				
				StringBuilder sb = tiendas.listarTodasValoraciones(conn);
				
				out.println(sb.toString());
				conn.close();
				
				
			} catch (Exception e) {
				out.println("Error");
				e.printStackTrace();
				
			}
		 
		} else {
			out.println("El código es incorrecto");
			
			
		}
	}
	
	
	
	

}
