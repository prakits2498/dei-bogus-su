package su.android.model;

public class MonthlyEventsRequest {
	private int month;
	private int year;
	private int id_utilizador;
	
	public MonthlyEventsRequest(int month, int year, int id_utilizador) {
		super();
		this.month = month;
		this.year = year;
		this.id_utilizador = id_utilizador;
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

	public int getId_utilizador() {
		return id_utilizador;
	}

	public void setId_utilizador(int id_utilizador) {
		this.id_utilizador = id_utilizador;
	}
	
}
