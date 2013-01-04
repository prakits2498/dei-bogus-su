package rs.deibogus.server;

import rs.deibogus.client.GreetingService;
import rs.deibogus.server.login.AppLogin;
import rs.deibogus.server.login.FlickrLogin;
import rs.deibogus.server.login.ILogin;
import rs.deibogus.server.login.Login;
import rs.deibogus.server.login.PicasaLogin;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		return confirmLogin(input, "");
	}
	
	public String confirmLogin(String input, String network) throws IllegalArgumentException {
		input = escapeHtml(input);
		String[] aux = input.split(" ");
		
		ILogin login;
		if(network.equals("app"))
			login = new Login(new AppLogin());
		else if(network.equals("flickr"))
			login = new Login(new FlickrLogin());
		else
			login = new Login(new PicasaLogin());
		
		//System.out.println(aux[0]+" "+aux[1]);
		if(login.confirmLogin(aux[0], aux[1])) 
			return "login success";
		
		throw new IllegalArgumentException("Login Error");
	}
	
	public String getURL() throws IllegalArgumentException {
		FlickrLogin fl = new FlickrLogin();
		return fl.GenerateUrl();
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
