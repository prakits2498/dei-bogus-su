package su.android.model;

public class POI {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	@Deprecated
	public static final String LOCATION = "location";
	public static final String LOC = "loc";
	public static final String CHECKINS_COUNT = "checkinsCount";
	public static final String USERS_COUNT = "users_count";
	
	private String id;
	private String name;
	private Double lat;
	private Double lng;	
	@Deprecated
	private String location;
	private int checkinsCount;
	private int usersCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Deprecated
	public String getLocation() {
		return location;
	}
	
	@Deprecated
	public void setLocation(String location) {
		this.location = location;
		String[] split = location.split(",");
		lat = Double.parseDouble(split[0]);
		lng = Double.parseDouble(split[1]);
	}
	
	public Double[] getLocationArray() {
		Double[] array = {lat, lng};		
		return array;
	}
	
	public void setLocation(Double lat, Double lng) {						
		this.lat = lat;
		this.lng = lng;
	}
	
	public int getCheckinsCount() {
		return checkinsCount;
	}
	
	public void setCheckinsCount(int checkinsCount) {
		this.checkinsCount = checkinsCount;
	}
	
	public int getUsersCount() {
		return usersCount;
	}
	
	public void setUsersCount(int usersCount) {
		this.usersCount = usersCount;
	}
	
	public String toString()
	{
		return "[ID: "+id+"] [Lat: "+lat+" Lng: "+lng+"] [Name: "+name+"]";
	}
	
	
}
