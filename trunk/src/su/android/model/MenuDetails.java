package su.android.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Lista de Refeiçoes da semana
 * @author bfurtado
 *
 */
public class MenuDetails {
	
	private String poiID;
	private List<Meal> menuLunch;
	private List<Meal> menuDinner;
	
	public MenuDetails()
	{		
		menuLunch = new ArrayList<Meal>();
		menuDinner = new ArrayList<Meal>();
	}
		
	public void setPOI(String poiID)
	{
		this.poiID = poiID;
	}
	
	public String getPOI()
	{
		return poiID;
	}
	
	public void addLunch(Meal meal)
	{
		this.menuLunch.add(meal);
	}
	
	public void addDinner(Meal meal)
	{
		this.menuDinner.add(meal);
	}
	
	public void setMenuLunch(List<Meal> menu)
	{
		this.menuLunch = menu;
	}
	
	public void setMenuDinner(List<Meal> menu)
	{
		this.menuDinner = menu;
	}
	
	public List<Meal> getMenuLunch()
	{
		return this.menuLunch;
	}
	
	public List<Meal> getMenuDinner()
	{
		return this.menuDinner;
	}
}
