package su.android.model;

public class Promotion 
{
	public static final String OWNER = "owner";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String WEEK_DAY = "week_day";
	public static final String HOUR_DAY = "hour_day";
	
	private String owner;
	private String description;
	private String id;
	private String weekDay;
	private int hourDay;
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	

	public void setWeekDay(String weekDay)
	{
		this.weekDay = weekDay;
	}
	
	public String getWeekDay()
	{
		return this.weekDay;
	}
	
	public int getHourDay()
	{
		return this.hourDay;
	}
	
	public void setHourDay(int hourDay)
	{
		this.hourDay = hourDay;
	}

}
