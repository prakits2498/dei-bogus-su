package su.android.model;

public class Reserva {

	private String userID;
	private String poiID;
	private String priceMeal;
	private String slot;
	private Meal meal;
	
	public Reserva() {
		
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPoiID() {
		return poiID;
	}

	public void setPoiID(String poiID) {
		this.poiID = poiID;
	}

	public String getPriceMeal() {
		return priceMeal;
	}

	public void setPriceMeal(String priceMeal) {
		this.priceMeal = priceMeal;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}
	
	
}
