package su.server.ws.model;

import java.util.HashMap;

public class DayEventsRequest {
	private int day;
	private int month;
	private int year;
	private int idUser;
	//private List<String> listEvents;
	HashMap<String, String> LunchEvents;
	HashMap<String, String> DinnerEvents;
	
	public DayEventsRequest() {

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

	public HashMap<String, String> getLunchEvents() {
		return this.LunchEvents;
	}

	public void setLunchEvents(HashMap<String, String> LunchEvents) {
		this.LunchEvents = LunchEvents;
	}

	public HashMap<String, String> getDinnerEvents() {
		return DinnerEvents;
	}

	public void setDinnerEvents(HashMap<String, String> dinnerEvents) {
		DinnerEvents = dinnerEvents;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}