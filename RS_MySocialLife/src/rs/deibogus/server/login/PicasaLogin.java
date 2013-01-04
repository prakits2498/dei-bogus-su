package rs.deibogus.server.login;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.util.AuthenticationException;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class PicasaLogin implements ILoginImplementor {

	private PicasawebService service;
	//private static PicasaLogin instance;
	
	public PicasaLogin() {
	}
	
	/*public static PicasaLogin getInstance() {
		if (instance==null)
			instance = new PicasaLogin();
		return instance;
	}*/
	
	@Override
	public boolean login(String user, String pass) {
		
		this.service = new PicasawebService("exampleClient");

		if (user != null && pass != null) {
			try {
				service.setUserCredentials(user, pass);
				return true;
			} catch (AuthenticationException e) {
				throw new IllegalArgumentException("Illegal username/password combination.");
			}
		}
		
		return false;
	}
	
	public PicasawebService getPicasaService() {
		return this.service;
	}

}
