package rs.deibogus.server;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import rs.deibogus.client.GreetingService;
import rs.deibogus.server.login.AppLogin;
import rs.deibogus.server.login.FlickrLogin;
import rs.deibogus.server.login.ILogin;
import rs.deibogus.server.login.Login;
import rs.deibogus.server.login.PicasaLogin;
import rs.deibogus.server.socialmanager.FlickrManager;
import rs.deibogus.server.socialmanager.ISocialManager;
import rs.deibogus.server.socialmanager.PicasaManager;
import rs.deibogus.server.socialmanager.SocialManager;
import rs.deibogus.shared.SessionData;
import rs.deibogus.shared.Foto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	HttpServletRequest request;
	HttpSession session;
	SessionData profile;

	public String greetServer(String input) throws IllegalArgumentException {
		return confirmLogin(input, "");
	}

	public String confirmLogin(String input, String network) throws IllegalArgumentException {
		String[] aux = new String[2];
		if(!input.equals("")) {
			input = escapeHtml(input);
			aux = input.split(" ");
		}

		ILogin login;
		if(network.equals("app")){
			profile = new SessionData();
			request = this.getThreadLocalRequest();
			session = request.getSession();
			session.setAttribute("session", profile);

			login = new Login(new AppLogin());
		}
		else if(network.equals("flickr")) {
			login = new Login(new FlickrLogin());

			request = this.getThreadLocalRequest();
			session = request.getSession();
			profile = (SessionData)session.getAttribute("session");
			//aux[0] = (String)session.getAttribute("frob");
			//aux[1] = "";
		}
		else{
			login = new Login(new PicasaLogin());

			request = this.getThreadLocalRequest();
			session = request.getSession();
			profile = (SessionData)session.getAttribute("session");
		}

		if(login.confirmLogin(aux[0], aux[1],profile)){
			if(network.equals("flickr"))
				profile.setFlickr(true);
			else if(network.equals("picasa"))
				profile.setPicasa(true);
			return "login success";
		}

		throw new IllegalArgumentException("Login Error");
	}

	public String getURL() throws IllegalArgumentException {
		FlickrLogin fl = new FlickrLogin();
		String[] aux = fl.GenerateUrl((SessionData)session.getAttribute("session")).split(" ");
		System.out.println("URL: " + aux[0] + " --------  frob: " + aux[1]);
		SessionData teste = (SessionData)session.getAttribute("session");
		System.out.println("Frob tirado da session: " + teste.getFlickrFrob());
		//request = this.getThreadLocalRequest();
		//session = request.getSession();
		//session.setAttribute("frob", aux[1]);
		//return fl.GenerateUrl();
		return aux[0];
	}

	public ArrayList<Foto> getPhotos() throws IllegalArgumentException {
		ISocialManager socialManager = null;
		ArrayList<Foto> result = new ArrayList<Foto>();

		request = this.getThreadLocalRequest();
		session = request.getSession();
		profile = (SessionData)session.getAttribute("session");

		if(profile.isFlickr()) {
			try {
				socialManager = new SocialManager(new FlickrManager(profile.getFlickrAuth()));
				ArrayList<Foto> temp = socialManager.getAllPhotos(profile);
				result.addAll(temp);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(profile.isPicasa()){
			socialManager = new SocialManager(new PicasaManager(profile.getService()));
			ArrayList<Foto> temp = socialManager.getAllPhotos(profile);
			result.addAll(temp);
		}


		if(profile.getCatalogo().size() == 0){
			for(Foto photo : result){
				profile.getCatalogo().add(photo);
			}
		}
		/*else{
			ArrayList<Foto> temp = new ArrayList<Foto>(profile.getCatalogo());
			for(Foto photo : result){
				for(Foto photoCat : temp){
					System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
					if(!(photoCat.getId().equals(photo.getId()))){
						profile.getCatalogo().add(photo);
					}
				}
			}
		}*/
		//TODO COMPLETAR ISTO!!


		return profile.getCatalogo();
		//throw new IllegalArgumentException("Photos Error");
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
