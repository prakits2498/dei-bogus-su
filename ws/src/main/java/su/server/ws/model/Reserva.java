package su.server.ws.model;



import java.util.List;

public class Reserva {

	private String userID;
	private String poiID;
	private String priceMeal;
	private List<Slot> slots;
	private Meal meal;
	
	private int day;
	private int month;
	
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

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
	
}
