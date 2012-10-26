package su.android.model;



public class TestObject
{
	private String item1;
	private String item2;
	
	private String type;
	private MenuDetails menuDetails;
	private String userID;
	private boolean dinner;
	
	public TestObject(String item1, String item2, String type, MenuDetails menuDetails, String userID, boolean dinner) 
	{
		this.item1 = item1;
		this.item2 = item2;
		this.setType(type);
		this.setMenuDetails(menuDetails);
		this.setUserID(userID);
		this.dinner = dinner;
	}
	
	public String[] getItems() 
	{
		String[] items = {item1, item2};
		return items;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MenuDetails getMenuDetails() {
		return menuDetails;
	}

	public void setMenuDetails(MenuDetails menuDetails) {
		this.menuDetails = menuDetails;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public boolean isSopa() {
		if(this.type.equals("sopa"))
			return true;
		else
			return false;
	}
	
	public boolean isCarne() {
		if(this.type.equals("carne"))
			return true;
		else
			return false;
	}
	
	public boolean isPeixe() {
		if(this.type.equals("peixe"))
			return true;
		else
			return false;
	}
	
	public boolean isDinner() {
		if(this.dinner)
			return true;
		else
			return false;
	}
	
	public boolean isLunch() {
		if(this.dinner)
			return false;
		else
			return true;
	}
	
	public String getTypeOfMeal() {
		if(this.dinner)
			return "dinner";
		else
			return "lunch";
	}
	
}
