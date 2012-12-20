package rs.deibogus.server.login;

import com.google.gdata.client.photos.PicasawebService;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public interface ILoginImplementor {
	public void login(String user, String pass);
}
