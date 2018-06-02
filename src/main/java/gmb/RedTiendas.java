package gmb;

	
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mybusiness.v4.MyBusiness;
import com.google.api.services.mybusiness.v4.model.Account;
import com.google.api.services.mybusiness.v4.model.ListAccountsResponse;
import com.google.api.services.mybusiness.v4.model.ListLocationsResponse;
import com.google.api.services.mybusiness.v4.model.ListReviewsResponse;
import com.google.api.services.mybusiness.v4.model.Location;
import com.google.api.services.mybusiness.v4.model.Review;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.DeadlineExceededException;
	
public class RedTiendas {
	
		Connection conn;
		//private static final java.io.File DATA_STORE_DIR = new java.io.File("/home/rafa/we");
		private static final String APPLICATION_NAME = "Google My Business";
		private static final Logger log = Logger.getLogger(BaseDatos.class.getName());
		//private static FileDataStoreFactory dataStoreFactory;
		private static HttpTransport httpTransport;
		protected Writer W = new StringWriter();
		//private static final JsonFactory JSON_FACTORY = JacksonFactory();
		static JsonFactory JSON_FACTORY = new JacksonFactory();
		private static MyBusiness mybusiness;
		private static String flagError = "1";
		private static int contError = 0;
		private static int contOK = 0;
		private static String SQL = "";
		private static String SQLold = "";
		static qhrea Q = new qhrea();
		
	public RedTiendas() {
			
	}
		
			
			
			
			/*
			 * Returns all locations for the specified account.
			 * @param accountName The account for which to return locations.
			 * @returns List A list of all locations for the specified account.
			 */
			public static List<Location> listLocations(String accountName, MyBusiness mybusiness) throws Exception {
			  com.google.api.services.mybusiness.v4.MyBusiness.Accounts.Locations.List locationsList =
			      mybusiness.accounts().locations().list(accountName);
			  //String filter = "location=MADRID";
			  //locationsList.setFilter(filter);
			  
			  //System.out.println("Número de tiendas (lo último): " + locationsList.size());
			  ListLocationsResponse responses = locationsList.execute();  
			 
			  List<Location> locations = responses.getLocations();
			//locations.batchGet(accountName, getLocationsRequest);
			locations = responses.getLocations(); //borrar si no estás probando
			  // Poner marca (*) aquí para no traerte toda la info
			  
				while (responses.getNextPageToken() != null)
			    	   {
			    	    locationsList.setPageToken(responses.getNextPageToken());
			    		System.out.println("Pasa " + responses.getNextPageToken());
			    	    responses = locationsList.execute();
			    	    locations.addAll(responses.getLocations());
			    	    
			    	   } 
			  
			  // Poner marca aaquí (*) para no traerte toda la info
			  
			  
			  return locations;
			}

			
			/**
			 * Returns a list of reviews.
			 * @param locationName Name of the location to retrieve reviews for.
			 * @return List A list of reviews.
			 * @throws Exception
			 */
			
			
	public static List<Review> listReviews(String locationName, MyBusiness mybusiness) throws Exception {

	
			  MyBusiness.Accounts.Locations.Reviews.List reviewsList = 
			  mybusiness.accounts().locations().reviews().list(locationName);
			  //log.info("Tienda | " + reviewsList.getPageSize());
			  reviewsList.setPageSize(50);
			  //reviewsList.setOrderBy("rating");
			  //reviewsList.setPageToken("nextPageToken");
			  //log.info("Time Left: " + ApiProxy.getCurrentEnvironment().getRemainingMillis());
			  //log.info("RequestID: " + ApiProxy.getCurrentEnvironment().getAttributes().get("com.google.appengine.runtime.request_log_id"));
			  ListReviewsResponse responses = reviewsList.execute();
			  List<Review> reviews = responses.getReviews();
			  int numReviews = 0;
			  if (responses.getReviews()!=null) {
				  numReviews = responses.getReviews().size();
			  
			  
			  log.info("numReviews: " + numReviews);
			  while (responses.getNextPageToken() != null)
	    	   {
				reviewsList.setPageToken(responses.getNextPageToken());
	    		
	    		log.info("Review | " + responses.getNextPageToken());
	    	    responses = reviewsList.execute();
	    	    reviews.addAll(responses.getReviews());
	    	    
	    	   } 
			  }
			  //ListReviewsResponse response = reviewsList.execute();
			  //List<Review> reviewsD = response.getReviews();			 
			  return reviews;
		
	}
			
					
			
	public static StringBuilder buildString2CSV(List<Location> tiendas, MyBusiness mybusiness, Connection conn) throws Exception {
				
		StringBuilder sb = new StringBuilder();
		sb.append("Ciudad; ");
		sb.append("Dirección; ");
		sb.append("Localización; ");
		sb.append("Identificador; ");
		sb.append("Comentarios; ");
		sb.append("Valoración; ");
		sb.append("Fecha de creación; ");
		sb.append("Fecha de actuación; ");
		sb.append('\n');
				  
		for (int i = 1; i < tiendas.size(); i++) {
			String tienda_i = tiendas.get(i).getName();	
			List<Review> valoraciones = listReviews(tienda_i, mybusiness);
			for (int j = 0; j < valoraciones.size(); j++) {
				String ciudad = tiendas.get(i).getAddress().getLocality().replace(";", "").replace("'", "");
				String direccion = tiendas.get(i).getAddress().getAddressLines().toString().replace(";", "").replace("'", "");
				String localizacion = tiendas.get(i).getName().replace(";", "").replace("'", "");
				String ReviewId = valoraciones.get(j).getReviewId().replace(";", "").replace("'", "");
				String Comment = valoraciones.get(j).getComment().replaceAll("\n", "").replace("'", "");
				String StarRating = valoraciones.get(j).getStarRating().replace(";", "").replace("'", "");
				String CreateTime = valoraciones.get(j).getCreateTime().replace(";", "").replace("'", "");
				String UpdateTime = valoraciones.get(j).getUpdateTime().replace(";", "").replace("'", "");
						  
				sb.append(ciudad);
				sb.append("; ");
				sb.append(direccion);
				sb.append("; ");
				sb.append(localizacion);
				sb.append("; ");
				if (ReviewId != null) {sb.append(ReviewId);} else {sb.append(""); ReviewId = "";}
				sb.append("; ");
				if (Comment != null) {sb.append(Comment);} else {sb.append(""); Comment = "";}
				sb.append("; ");
				if (StarRating != null) {sb.append(StarRating);} else {sb.append(""); StarRating = "";}
				sb.append("; ");
				if (CreateTime != null) {sb.append(CreateTime);} else {sb.append(""); CreateTime = "";}
				sb.append("; ");
				if (UpdateTime != null) {sb.append(UpdateTime);} else {sb.append(""); UpdateTime = "";}
				sb.append('\n');
			}
				
		}
				 
		return sb;
	}
	
	public static void loadReview(Location tienda, Review review, Connection conn) throws ClassNotFoundException, ServletException, SQLException {
		
		Statement secuencia = conn.createStatement(); 
		erroresGMB errores = new erroresGMB();
		
		String ciudad = errores.esNull("Ciudad", tienda, review).replace(";", "").replace("'", "");
		String direccion = errores.esNull("Dirección", tienda, review).replace(";", "").replace("'", "");					
		String localizacion = errores.esNull("Localización", tienda, review).replace(";", "").replace("'", "");					
		String CodigoTienda = errores.esNull("CodigoTienda", tienda, review).replace(";", "").replace("'", "");					
		String ReviewId = errores.esNull("ReviewId", tienda, review).replace(";", "").replace("'", "");					
		String Comment = errores.esNull("Comment", tienda, review).replaceAll("\n", "").replace("'", "")
				.replaceAll("[^\\x00-\\x7f]", ""); //.replaceAll("[^\\dA-Za-z. ,_-áéíóú],", "");  // Replace all the caracters but numbers, letters and points
		//log.info("Comentario valoración " + j + "| " + Comment);
		String StarRating = errores.esNull("StarRating", tienda, review).replace(";", "").replace("'", "");				
		String CreateTime = errores.esNull("CreateTime", tienda, review).replace(";", "").replace("'", "");				
		String UpdateTime = errores.esNull("UpdateTime", tienda, review).replace(";", "").replace("'", "");
	  
		 SQL = "INSERT INTO gmb_db.Valoraciones_temp (Ciudad, Dirección, Localización, CodigoTienda, Identificación, Comentarios, Valoración, Fechacreación, Fechaactuación) VALUES ('" 
		    + ciudad + "', '" + direccion + "', '" + localizacion + "', '" + CodigoTienda 
		    + "', '" +  ReviewId + "', '" + Comment + "', '" + StarRating + "', '"  
		    + CreateTime + "', '" + UpdateTime + "')";
		  
		 try {
			secuencia.executeUpdate(SQL);
		} catch (SQLException e) {
			log.info("SQL Loop Error | SQL: " + SQL + " | Error: " + e);
		}
		
	}
	
	
	
	
	public static void loadReviews2DB(List<Location> tiendas, MyBusiness mybusiness) throws Exception  {
		log.info("loadReviews2DB | Start");
		BaseDatos DB = new BaseDatos();
		Connection conn;
		Statement secuencia;
		conn = DB.ConectarDB();
		secuencia = conn.createStatement(); 
		SQL = "delete FROM gmb_db.Valoraciones;";
		secuencia.executeUpdate(SQL);  
		log.info("Se borra gmb_db.Valoraciones_temp | " + "OK");
		log.info("loadReviews2DB | Número de tiendas: " + tiendas.size());
			for (int i = 1; i < tiendas.size(); i++) {
			  log.info("Tienda número | " + i);
			  log.info("Tienda Localidad | " + tiendas.get(i).getAddress().getLocality());
			  String tienda_i = tiendas.get(i).getName();	
			 
				List<Review> valoraciones = listReviews(tienda_i, mybusiness);
				
				if (valoraciones != null) {
					for (int j = 0; j < valoraciones.size(); j++) {
						
						loadReview(tiendas.get(i), valoraciones.get(j), conn);
					
					}
				} 
		   }
			
			
		  SQL = "delete FROM gmb_db.Valoraciones_temp where Identificación = ''";
		  secuencia.executeUpdate(SQL);
		  
		  
		  SQL = "INSERT INTO gmb_db.Valoraciones (Ciudad, Dirección, Localización, CodigoTienda, Identificación, Comentarios, Valoración, Fechacreación, Fechaactuación) "
		  		+ "SELECT DISTINCT * FROM gmb_db.Valoraciones_temp "
		  		+ "WHERE Identificación != '' AND CodigoTienda IS NOT NULL AND CodigoTienda != '' ORDER BY Fechaactuación";
		  secuencia.executeUpdate(SQL);
		  
		  
		  
		  
		  log.info("Actualiza gmb_db.Valoraciones de gmb_db.Valoraciones_temp.Identificación | " + SQL);
		  SQL = "delete FROM gmb_db.Valoraciones_temp;";
		  secuencia.executeUpdate(SQL);  
		  log.info("Se borra gmb_db.Valoraciones_temp | " + "OK");
		  conn.close();
		
			
		
		log.info("loadReviews2DB | End");
	}
	
	
		
		public void DownloadReviews() throws Exception {
			
			log.info("DownloadReviews | Start");
			MyBusiness mybusiness = AccesoAPIs.Connect2GMB("/client_secrets.json"); 
			MyBusiness.Accounts.List accountsList = mybusiness.accounts().list();
			ListAccountsResponse response = accountsList.execute();
			List<Location> tiendas = listLocations("accounts/105070584962891673973", mybusiness);
			loadReviews2DB(tiendas, mybusiness);
			//GestionValoraciones(tiendas, mybusiness, conn);
			log.info("DownloadReviews | End");
		}
		
		
		public StringBuilder listarTodasValoraciones(Connection conn) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("Ciudad;");
			sb.append("Direccion;");
			sb.append("Localizacion;");
			sb.append("CodigoTienda;");
			sb.append("Identificacion;");
			sb.append("Comentarios;");
			sb.append("Valoracion;");
			sb.append("Fechacreacion;");
			sb.append("Fechaactualizacion;");
			sb.append('\n');
			String selectQuery = "SELECT * FROM gmb_db.Valoraciones";
			String qrea = "";
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectQuery);
                int numCols = rs.getMetaData().getColumnCount();
                int z = 0;
                System.out.println("Número de columnas: " + numCols);
                
                
                while(rs.next()) {
                	z = z +1 ;
                	System.out.println("Por aquí: " + z);
                	System.out.println("rs: " + rs.getString(1));
                    for (int i = 1; i <= numCols; i++) {
                    	qrea = rs.getString(i);
                    	if (qrea!=null) {
                    		qrea.replace(";","");
                    		sb.append(qrea).append(";");
                    	} else {
                    		sb.append("").append(";");
                    	}

                    	
                    }
                    
                    sb.append('\n');
                }
                
            } catch (Exception e) {
			
            	sb.append(qrea);
            	sb.append(e);
            	sb.append("Existe algún problema con la base de datos");
            	
            }
		
			
			return sb;
			
		}
		
						
			public static void main(String[] args) throws Exception {
				
				//ExtraerValoraciones();
			  
			  }
	
	
}
