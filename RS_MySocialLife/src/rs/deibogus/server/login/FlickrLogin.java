package rs.deibogus.server.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import rs.deibogus.shared.SessionData;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.activity.ActivityInterface;
import com.aetrion.flickr.activity.Event;
import com.aetrion.flickr.activity.Item;
import com.aetrion.flickr.activity.ItemList;
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
	Auth auth;
	private static String apiKey = "32eff8810bfb81ddea86b6d50e6e5fe8";
	private static String secret = "36ae9ccecbf2b1b3";

	public String GenerateUrl(SessionData session) {
		try {
			f = new Flickr(apiKey,secret,new REST());
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return "Error Generating Flickr Auth URL";
		}
		
		//Flickr.debugStream = false;
		requestContext = RequestContext.getRequestContext();
		AuthInterface authInterface = f.getAuthInterface();

		try {
			frob = authInterface.getFrob();
			session.setFlickrFrob(frob);
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
		System.out.println("frob dentro do Flickr login: " + frob);

		URL url=null;
		try {
			url = authInterface.buildAuthenticationUrl(Permission.DELETE, frob);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "error Generating Flickr Auth URL";
		} //URL para onde temos de mandar user para dar autorizacao
		System.out.println("Press return after you granted access at this URL:");
		System.out.println(url.toExternalForm());
		return url.toExternalForm() + " " + frob;
	}

	@Override
	public boolean login(String frobs, String pass,SessionData sessao) {
		try {
			this.frob = sessao.getFlickrFrob();
			System.out.println("FlickrLogin - login - " + frob);
			this.f = new Flickr(apiKey,secret,new REST());
			auth = f.getAuthInterface().getToken(frob);
			sessao.setFlickrAuth(auth);
			System.out.println("Authentication success");
			// This token can be used until the user revokes it.
			System.out.println("Token: " + auth.getToken());
			System.out.println("nsid: " + auth.getUser().getId());
			System.out.println("Realname: " + auth.getUser().getRealName());
			System.out.println("Username: " + auth.getUser().getUsername());
			System.out.println("Permission: " + auth.getPermission().getType());

//			sessao.getRequestContext().setAuth(auth);
//			this.requestContext = sessao.getRequestContext();
			this.requestContext = RequestContext.getRequestContext();
			this.requestContext.setAuth(sessao.getFlickrAuth());
			//sessao.setFlickr(true);
			System.out.println("VAI TESTAR SHOW ACTIVITY");
			showActivity();
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
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void showActivity() throws FlickrException, IOException, SAXException {
        ActivityInterface iface = f.getActivityInterface();
        ItemList list = iface.userComments(10, 0);
        for (int j = 0; j < list.size(); j++) {
            Item item = (Item) list.get(j);
            System.out.println("Item " + (j + 1) + "/" + list.size() + " type: " + item.getType());
            System.out.println("Item-id:       " + item.getId() + "\n");
            ArrayList events = (ArrayList) item.getEvents();
            for (int i = 0; i < events.size(); i++) {
                System.out.println("Event " + (i + 1) + "/" + events.size() + " of Item " + (j + 1));
                System.out.println("Event-type: " + ((Event) events.get(i)).getType());
                System.out.println("User:       " + ((Event) events.get(i)).getUser());
                System.out.println("Username:   " + ((Event) events.get(i)).getUsername());
                System.out.println("Value:      " + ((Event) events.get(i)).getValue() + "\n");
            }
        }

        
        ActivityInterface iface2 = f.getActivityInterface();
        list = iface2.userPhotos(50, 0, "300d");
        for (int j = 0; j < list.size(); j++) {
            Item item = (Item) list.get(j);
            System.out.println("Item " + (j + 1) + "/" + list.size() + " type: " + item.getType());
            System.out.println("Item-id:       " + item.getId() + "\n");
            ArrayList events = (ArrayList) item.getEvents();
            for (int i = 0; i < events.size(); i++) {
                System.out.println("Event " + (i + 1) + "/" + events.size() + " of Item " + (j + 1));
                System.out.println("Event-type: " + ((Event) events.get(i)).getType());
                if (((Event) events.get(i)).getType().equals("note")) {
                    System.out.println("Note-id:    " + ((Event) events.get(i)).getId());
                } else if (((Event) events.get(i)).getType().equals("comment")) {
                    System.out.println("Comment-id: " + ((Event) events.get(i)).getId());
                }
                System.out.println("User:       " + ((Event) events.get(i)).getUser());
                System.out.println("Username:   " + ((Event) events.get(i)).getUsername());
                System.out.println("Value:      " + ((Event) events.get(i)).getValue());
                System.out.println("Dateadded:  " + ((Event) events.get(i)).getDateadded() + "\n");
            }
        }
    }

	@Override
	public boolean registerUser(String user, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

}
