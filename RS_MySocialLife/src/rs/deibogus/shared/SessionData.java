package rs.deibogus.shared;

import com.aetrion.flickr.auth.Auth;
import com.google.gdata.client.photos.PicasawebService;

public class SessionData {
	private String flickrFrob; //o objecto f n�o o tem, e em vez de tar a criar outra session attribute, � mais limpo estar aqui
	private Auth flickrAuth;	//ibidem
	private PicasawebService service;
	private String userName;
	private int id;
	
	public SessionData(){
		
	}
	
	public PicasawebService getService() {
		return service;
	}
	public void setService(PicasawebService service) {
		this.service = service;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getFlickrFrob() {
		return flickrFrob;
	}

	public void setFlickrFrob(String flickrFrob) {
		this.flickrFrob = flickrFrob;
	}

	public Auth getFlickrAuth() {
		return flickrAuth;
	}

	public void setFlickrAuth(Auth flickrAuth) {
		this.flickrAuth = flickrAuth;
	}

}
