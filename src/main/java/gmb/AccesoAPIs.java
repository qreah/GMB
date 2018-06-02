package gmb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mybusiness.v4.MyBusiness;

  
// Clase que tiene como objetivo abstraer el 
// acceso a las APIs de todos los proveedores de los programas normales de funcionamiento

public class AccesoAPIs {
	 
	
	// Attributes from Google MyBusiness
	private static HttpTransport httpTransport;
	static JsonFactory JSON_FACTORY = new JacksonFactory();
	
	/*
	 *  Connecto to the Google MyBusiness API
	 *  Input:
	 *  	- String CredentialsFile: file with information. Ex: "/client_secrets.json"
	 *  Output:
	 *  	- MyBusiness object with the specified user credentials
	 */
	
	public static MyBusiness Connect2GMB(String CredentialsFile) throws Exception {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		String refreshToken = "1/xcMBF3Aw-0fsZGloJ4xGdnCzXxm0u-uhi1j_56akUkQ";
		
		// Reading credentias from json file 
		InputStream in = RedTiendas.class.getResourceAsStream(CredentialsFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		System.out.println(out.toString());   //Prints the string content read from input stream
		reader.close();
		        
		InputStream secrets = RedTiendas.class.getResourceAsStream(CredentialsFile);
		
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
			new InputStreamReader(secrets));
			  
		JSON_FACTORY = JacksonFactory.getDefaultInstance();
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setRefreshToken(refreshToken);
		tokenResponse.getAccessToken();
		      
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
		        .setJsonFactory(JSON_FACTORY)
		        .setClientSecrets(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret())
		        .build()
		        .setFromTokenResponse(tokenResponse);
		      
			  		  
		// Calls Mybusiness.Builder to create a new instance named 'mybusiness'.
		return new MyBusiness.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName("GMB").build();
	}
	

	public static void main(String[] args) {
		

	}

}
