package su.android.model;

public class AppContext 
{
	private double lng;
	private double lat;	
	private int dayWeekIndex;
	private int hourOfDay;
	public static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	public double getLng() 
	{
		return lng;
	}
	
	public void setLng(double lng) 
	{
		this.lng = lng;
	}
	
	public double getLat() 
	{
		return lat;
	}
	
	public void setLat(double lat) 
	{
		this.lat = lat;
	}
	
	public int getDayOfWeekIndex() 
	{
		return dayWeekIndex;
	}
	
	public void setDayOfWeekIndex(int dayWeekIndex)
	{
		this.dayWeekIndex = dayWeekIndex;
	}
	
	public String getDayOfWeek()
	{
		return AppContext.days[dayWeekIndex];
	}
	
	public int getHourOfDay() 
	{
		return hourOfDay;
	}
	
	public void setHourOfDay(int hourOfDay) 
	{
		this.hourOfDay = hourOfDay;
	}
	
	public String toString()
	{
		return "[LOCATION: "+lat+", "+lng+"] [DAY: "+this.getDayOfWeek()+"] [HOUR: "+hourOfDay+"]";
	}
	
	
}
