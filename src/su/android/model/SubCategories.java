package su.android.model;

public class SubCategories {
	
	public static final String ID = "_id";
	public static final String Name = "nome";
	public static final String ID_CATEGORY = "id_cat";
	
	
	private String id;
	private String name;
	private String id_cat;
	
	
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
	
	public String getID_Catgory() {
		return id_cat;
	}
	
	public void setID_Catgory(String id_cat) {
		this.id_cat = id_cat;
	}

}
