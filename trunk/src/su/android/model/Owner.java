package su.android.model;

public class Owner {
	
	public static final String USERNAME = "Name";
	public static final String PASSWORD = "Password";
	public static final String POI_ID = "PoiID";
	
	
	private String name;
	private String password;
	private String poiId;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPoiId() {
		return poiId;
	}
	
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}	
}
