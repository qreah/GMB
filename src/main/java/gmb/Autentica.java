package gmb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Autentica {

	public String doLogin(String username, String password, Connection con){
        
        String message = null;
        try {
            
            PreparedStatement statement = con.prepareStatement("SELECT USER_NAME, PASSWORD FROM USER WHERE USER_NAME = ? AND PASSWORD = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                message = "SUCCESS";
            }else{
                message = "FAILURE";
            }
        } catch (Exception e) {
            message = "FAILURE";
            e.printStackTrace();
        }
        return message;
    }
}
