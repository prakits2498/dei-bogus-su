package su.android.model;


import java.util.List;

public class Reserva {

	private String userID;
	private String poiID;
	private String slotID;
	private String priceMeal;
	private List<Slot> slots;
	private Meal meal;


	private int day;
	private int month;
	
	private boolean creditos;
	private boolean paid;
	private String userCredits;
	
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

	public String getSlotID() {
		return slotID;
	}

	public void setSlotID(String slotID) {
		this.slotID = slotID;
	}

	public boolean isCreditos() {
		return creditos;
	}

	public void setCreditos(boolean creditos) {
		this.creditos = creditos;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getUserCredits() {
		return userCredits;
	}

	public void setUserCredits(String userCredits) {
		this.userCredits = userCredits;
	}
	
	
}
