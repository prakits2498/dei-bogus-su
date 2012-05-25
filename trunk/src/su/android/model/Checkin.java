package su.android.model;

import java.util.Date;

public class Checkin 
{
	public static final String ID = "id";
	public static final String POI_ID = "poiID";
	public static final String HOUR = "hour";
	public static final String WEEK_DAY = "weekDay";
	public static final String DATE = "date";
	
	private String id;
	private String poiId;
	private int hour;
	private String weekDay;
	private Date date;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPoiId() {
		return poiId;
	}
	
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public String getWeekDay() {
		return weekDay;
	}
	
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
