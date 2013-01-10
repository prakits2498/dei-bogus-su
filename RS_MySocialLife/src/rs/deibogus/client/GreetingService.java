package rs.deibogus.client;

import java.util.ArrayList;

import rs.deibogus.shared.Foto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String confirmLogin(String input, String network) throws IllegalArgumentException;
	String registerUser(String input) throws IllegalArgumentException;
	ArrayList<Foto> getPhotos() throws IllegalArgumentException;
	String getURL() throws IllegalArgumentException;
	String deletePhoto(Foto foto) throws IllegalArgumentException;
}
