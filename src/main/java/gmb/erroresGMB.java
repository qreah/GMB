package gmb;

import java.util.logging.Logger;

import com.google.api.services.mybusiness.v4.model.Location;
import com.google.api.services.mybusiness.v4.model.Review;

public class erroresGMB {


	private static final Logger log = Logger.getLogger(erroresGMB.class.getName());
	//private qhrea Qhrea = new qhrea();
	
	public String esNull(String TipoCampo, Location tienda, Review review) {
		
		String resultado = "";
		
		if (TipoCampo.equals("Ciudad")) {
			if (tienda.getAddress().getLocality() != null) {
				resultado = tienda.getAddress().getLocality();
			} else {resultado = "" ;}
		}
		
		
		if (TipoCampo.equals("Dirección")) {
			if (tienda.getAddress().getAddressLines() != null) {
				resultado = tienda.getAddress().getAddressLines().toString(); 
			} else {resultado = "" ;}
			
		}
		
		if (TipoCampo.equals("Localización")) {
			if (tienda.getName() != null) {
				resultado = tienda.getName();
			} else {resultado = "" ;}
		}
		
		if (TipoCampo.equals("CodigoTienda")) {
			if (tienda.getStoreCode() != null) {
				resultado = tienda.getStoreCode();
			} else {resultado = "" ;}
		}
		
		if (TipoCampo.equals("ReviewId")) {
			if (review.getReviewId() != null) {
				resultado = review.getReviewId();
			} else {resultado = "" ;}
		}
		
		if (TipoCampo.equals("Comment")) {
			if (review.getComment() != null) {
				resultado = review.getComment();
			} else {resultado = "" ;}
		}
		
		if (TipoCampo.equals("StarRating")) {
			if (review.getStarRating() != null) {
				resultado = review.getStarRating();
			} else {resultado = "" ;}
		}
		
		if (TipoCampo.equals("CreateTime")) {
			if (review.getCreateTime() != null) {
				resultado = review.getCreateTime();
			} else {resultado = "" ;}
		}
		
		if (TipoCampo.equals("UpdateTime")) {
			if (review.getUpdateTime() != null) {
				resultado = review.getUpdateTime();
			} else {resultado = "" ;}
		}
		
		
		
		return resultado;
		
		
		
	}
	
	
	
}
