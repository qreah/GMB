package gmb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CronTiendas
 */
@WebServlet("/CronTiendas")
public class CronTiendas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BaseDatos.class.getName());
	Connection conn;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CronTiendas() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Download the data from the API, the information about the stores, and put them in the gmb_db database
	 * The servlet uses the class RedTiendas with the method DownloadReviews()
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("CronTiendas | Start");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		RedTiendas tiendas = new RedTiendas();
		qhrea Q = new qhrea();
		request.getServletContext();
		try {
			tiendas.DownloadReviews();
			Q.enviarMail("rafael.fayosoliver@telefonica.com", "Ha ido bien el Cron", "OK");
			log.info("CronTiendas | Status: OK");
			
		} catch (Exception e) {
			e.printStackTrace();
			Q.enviarMail("rafael.fayosoliver@telefonica.com", "Ha ido mal el Cron", e.toString());
			log.info("CronTiendas | Status: ERROR");
		}
		
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
		formateador.format(ahora);
		out.println("Ok. Actualización de tiendas realizada con éxito: " + formateador.format(ahora));
		
		log.info("CronTiendas | End");
		
	}

}
