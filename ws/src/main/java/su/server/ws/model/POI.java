package su.server.ws.model;


public class POI {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String LOC = "loc";
	
	private String id;
	private String name;
	private Double lat;
	private Double lng;
	
	
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

}
