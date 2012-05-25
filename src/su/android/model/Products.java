package su.android.model;

public class Products {
	
	public static final String ID = "_id";
	public static final String ID_MERCHANT = "id_comerciante";
	public static final String ID_CATEGORY = "id_cat";
	public static final String ID_SUBCATGORY = "id_subcat";
	public static final String NAME = "nome";
	public static final String DESCRIPTION = "descricao";
	public static final String PRICE = "preco";
	public static final String IMAGE = "img";
	
	private String id;
	private String id_merchant;
	private String id_cat;
	private String id_subcat;
	private String name;
	private String description;
	private String price;
	private String image;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getID_Merchant() {
		return id_merchant;
	}
	
	public void setID_Merchant(String id_merchant) {
		this.id_merchant = id_merchant;
	}
	
	public String getID_Catgory() {
		return id_cat;
	}
	
	public void setID_Catgory(String id_cat) {
		this.id_cat = id_cat;
	}
	
	public String getID_SubCatgory() {
		return id_subcat;
	}
	
	public void setID_SubCatgory(String id_subcat) {
		this.id_subcat = id_subcat;
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
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}



}
