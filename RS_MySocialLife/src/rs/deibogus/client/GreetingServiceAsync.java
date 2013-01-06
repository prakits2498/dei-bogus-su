package rs.deibogus.client;

import java.util.ArrayList;

import rs.deibogus.shared.Foto;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void confirmLogin(String input, String network, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getPhotos(AsyncCallback<ArrayList<Foto>> callback) throws IllegalArgumentException;
	void getURL(AsyncCallback<String> callback) throws IllegalArgumentException;
}
