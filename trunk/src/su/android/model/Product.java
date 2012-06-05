package su.android.model;

public class Product 
{
	public static final String OWNER = "owner";
	public static final String CATEGORY = "category";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String PRICE = "preco";
	public static final String IMAGE_URL = "img_url";
	public static final String ID = "id";
	public static final String WEEK_DAY = "week_day";
	
	private String id;
	private String owner;
	private String category;
	private String name;
	private String description;
	private String price;
	private String imageUrl;
	private String weekDay;
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setWeekDay(String weekDay)
	{
		this.weekDay = weekDay;
	}
	
	public String getWeekDay()
	{
		return this.weekDay;
	}	

}
