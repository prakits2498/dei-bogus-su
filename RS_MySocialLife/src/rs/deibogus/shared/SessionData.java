package rs.deibogus.shared;

import java.util.ArrayList;

import com.aetrion.flickr.auth.Auth;
import com.google.gdata.client.photos.PicasawebService;


public class SessionData {
	private String flickrFrob; //o objecto f não o tem, e em vez de tar a criar outra session attribute, é mais limpo estar aqui
	private Auth flickrAuth;	//ibidem
	private String picasaUsername;
	private boolean isFlickr = false;
	private boolean isPicasa = false;
	private PicasawebService service;
	private String userName;
	private int id;
	private ArrayList<Foto> catalogo;
	
	public SessionData(){
		catalogo = new ArrayList<Foto>();
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

	public boolean isFlickr() {
		return isFlickr;
	}

	public void setFlickr(boolean isFlickr) {
		this.isFlickr = isFlickr;
	}

	public boolean isPicasa() {
		return isPicasa;
	}

	public void setPicasa(boolean isPicasa) {
		this.isPicasa = isPicasa;
	}

	public String getPicasaUsername() {
		return picasaUsername;
	}

	public void setPicasaUsername(String picasaUsername) {
		this.picasaUsername = picasaUsername;
	}

	public ArrayList<Foto> getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(ArrayList<Foto> catalogo) {
		this.catalogo = catalogo;
	}
	
	

}
