package su.server.ws.model;


public class POI {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String LOC = "loc";
	
	private String id;
	private String name;
	private Double lat;
	private Double lng;
	private String address;
	private String category;
	private int capacity;
	
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
	
	public Double[] getLocationArray() {
		Double[] array = {lat, lng};		
		return array;
	}
	
	public void setLocation(Double lat, Double lng) {						
		this.lat = lat;
		this.lng = lng;
	}
	

	public String toString()
	{
		return "[ID: "+id+"] [Lat: "+lat+" Lng: "+lng+"] [Name: "+name+"]";
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
