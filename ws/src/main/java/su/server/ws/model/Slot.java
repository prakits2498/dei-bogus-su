package su.server.ws.model;

public class Slot {

	private int day;
	private int month;
	private int hour;
	private int minute;
	private int reservados;
	private int availability;
	private boolean isAvailable;
	
	private String idSlot;
	private String idPOI;
	private String idMeal;
	
	public Slot() {
		
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

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getReservados() {
		return reservados;
	}

	public void setReservados(int reservados) {
		this.reservados = reservados;
	}

	public String getIdSlot() {
		return idSlot;
	}

	public void setIdSlot(String idSlot) {
		this.idSlot = idSlot;
	}

	public String getIdPOI() {
		return idPOI;
	}

	public void setIdPOI(String idPOI) {
		this.idPOI = idPOI;
	}

	public String getIdMeal() {
		return idMeal;
	}

	public void setIdMeal(String idMeal) {
		this.idMeal = idMeal;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	
}
