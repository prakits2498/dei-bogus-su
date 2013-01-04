package rs.deibogus.shared;

import com.aetrion.flickr.Flickr;
import com.google.gdata.client.photos.PicasawebService;

public class SessionData {
	private Flickr f;
	private PicasawebService service;
	private String userName;
	private int id;
	
	public SessionData(){
		f = new Flickr();
	}
	
	public Flickr getF() {
		return f;
	}
	public void setF(Flickr f) {
		this.f = f;
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

}
