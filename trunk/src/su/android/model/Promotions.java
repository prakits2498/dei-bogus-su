package su.android.model;

public class Promotions {

	public static final String ID = "_id";
	public static final String ID_MERCHANT = "id_comerciante";
	public static final String DESCRIPTION = "descricao";
	
	
	private String id;
	private String name;
	private String description;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getID_Merchant() {
		return name;
	}
	
	public void setID_Merchant(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
