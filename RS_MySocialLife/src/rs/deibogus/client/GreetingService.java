package rs.deibogus.client;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import rs.deibogus.shared.Foto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String input) throws IllegalArgumentException;
	String confirmLogin(String input, String network) throws IllegalArgumentException;
	ArrayList<Foto> getPhotos(String rede) throws IllegalArgumentException;
	String getURL() throws IllegalArgumentException;
	String deletePhoto(Foto foto) throws IllegalArgumentException;
}
