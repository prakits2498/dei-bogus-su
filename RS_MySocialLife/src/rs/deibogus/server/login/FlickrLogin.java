package rs.deibogus.server.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;

/**
 * Bridge Design Pattern
 * @author bfurtado, durval
 *
 */
public class FlickrLogin implements ILoginImplementor {

	Flickr f;
	RequestContext requestContext;
	String frob = "";
	private static Auth auth;
	private static String apiKey = "32eff8810bfb81ddea86b6d50e6e5fe8";
	private static String secret = "36ae9ccecbf2b1b3";

	public String GenerateUrl() {
		try {
			f = new Flickr(apiKey,secret,new REST());
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return "error Generating Flickr Auth URL";
		}
		//Flickr.debugStream = false;
		requestContext = RequestContext.getRequestContext();
		AuthInterface authInterface = f.getAuthInterface();

		try {
			frob = authInterface.getFrob();
		} catch (FlickrException e) {
			e.printStackTrace();
			return "error Generating Flickr Auth URL";
		} catch (IOException e) {
			e.printStackTrace();
			return "error Generating Flickr Auth URL";
		} catch (SAXException e) {
			e.printStackTrace();
			return "error Generating Flickr Auth URL";
		}
		System.out.println("frob: " + frob);

		URL url=null;
		try {
			url = authInterface.buildAuthenticationUrl(Permission.DELETE, frob);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "error Generating Flickr Auth URL";
		} //URL para onde temos de mandar user para dar autorizacao
		System.out.println("Press return after you granted access at this URL:");
		System.out.println(url.toExternalForm());
		return url.toExternalForm();
	}

	@Override
	public boolean login(String user, String pass) {
		try {
			auth = f.getAuthInterface().getToken(frob);
			System.out.println("Authentication success");
			// This token can be used until the user revokes it.
			System.out.println("Token: " + auth.getToken());
			System.out.println("nsid: " + auth.getUser().getId());
			System.out.println("Realname: " + auth.getUser().getRealName());
			System.out.println("Username: " + auth.getUser().getUsername());
			System.out.println("Permission: " + auth.getPermission().getType());

			requestContext.setAuth(auth);
			//Flickr.debugRequest = false;
			//Flickr.debugStream = false;
		} catch (FlickrException e) {
			System.out.println("Authentication failed");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//not used
	public boolean login2(String user, String pass) {
		try {
			f = new Flickr(apiKey,secret,new REST());
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return false;
		}
		//Flickr.debugStream = false;
		requestContext = RequestContext.getRequestContext();
		AuthInterface authInterface = f.getAuthInterface();

		try {
			frob = authInterface.getFrob();
		} catch (FlickrException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("frob: " + frob);

		URL url=null;
		try {
			url = authInterface.buildAuthenticationUrl(Permission.DELETE, frob);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return false;
		} //URL para onde temos de mandar user para dar autorizacao
		System.out.println("Press return after you granted access at this URL:");
		System.out.println(url.toExternalForm());

		BufferedReader infile = new BufferedReader ( new InputStreamReader (System.in) );
		try {
			String line = infile.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		try {
			auth = authInterface.getToken(frob);
			System.out.println("Authentication success");
			// This token can be used until the user revokes it.
			System.out.println("Token: " + auth.getToken());
			System.out.println("nsid: " + auth.getUser().getId());
			System.out.println("Realname: " + auth.getUser().getRealName());
			System.out.println("Username: " + auth.getUser().getUsername());
			System.out.println("Permission: " + auth.getPermission().getType());

			requestContext.setAuth(auth);
			//Flickr.debugRequest = false;
			//Flickr.debugStream = false;
		} catch (FlickrException e) {
			System.out.println("Authentication failed");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
