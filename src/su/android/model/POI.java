package su.android.model;

import java.util.List;

public class POI {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	@Deprecated
	public static final String LOCATION = "location";
	public static final String LOC = "loc";
	public static final String CHECKINS_COUNT = "checkinsCount";
	public static final String USERS_COUNT = "users_count";
	public static final String CATEGORY = "category";
	public static final String SUB_CATEGORY = "sub_category";
	public static final String DEFAULT_CATEGORY_ICON = "default_category_icon";
	public static final String KEYWORDS = "keywords";
	public static final String ADDRESS = "address";
	public static final String PHOTOS = "photos";
	
	private String id;
	private String name;
	private Double lat;
	private Double lng;
	@Deprecated
	private String location;
	private int checkinsCount;
	private int usersCount;
	
	private String address;
	private String category;
	private String subCategory;
	private String defaultCategoryIcon;
	private List<String> keywords;
	private List<String> photos;
	
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
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getDefaultCategoryIcon() {
		return defaultCategoryIcon;
	}

	public void setDefaultCategoryIcon(String defaultCategoryIcon) {
		this.defaultCategoryIcon = defaultCategoryIcon;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}
	
	public String toString()
	{
		return "[ID: "+id+"] [Lat: "+lat+" Lng: "+lng+"] [Name: "+name+"] [Cat: "+category+"] [SubCat: "+subCategory+"] [Address: "+address+"] [Photos: "+photos+"]";
	}

}
