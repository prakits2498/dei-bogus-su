package su.android.model;

public class Meal 
{	
	private String id;
	private String sopa;
	private String carne;
	private String peixe;
	private String price;
	
	private String day;
	private String month;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSopa() {
		return sopa;
	}
	
	public void setSopa(String sopa) {
		this.sopa = sopa;
	}
	
	public String getCarne() {
		return carne;
	}
	
	public void setCarne(String carne) {
		this.carne = carne;
	}
	
	public String getPeixe() {
		return peixe;
	}
	
	public void setPeixe(String peixe) {
		this.peixe = peixe;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

}
