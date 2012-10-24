package su.android.model;


import java.util.HashMap;

public class MonthlyEventsRequest {
	private int month;
	private int year;
	private int idUser;
	//private List<String> listEvents;
	HashMap<String, Integer> listEvents;
	
	public MonthlyEventsRequest() {

	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public HashMap<String, Integer> getListEvents() {
		return listEvents;
	}

	public void setListEvents(HashMap<String, Integer> listEvents) {
		this.listEvents = listEvents;
	}

}