package su.server.ws.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista de Refeições da semana
 * @author bfurtado
 *
 */
public class MenuDetails {
	
	private POI poi;
	private List<Meal> menuLunch;
	private List<Meal> menuDinner;
	
	public MenuDetails()
	{		
		poi = null;
		menuLunch = new ArrayList<Meal>();
		menuDinner = new ArrayList<Meal>();
	}
		
	public void setPOI(POI poi)
	{
		this.poi = poi;
	}
	
	public POI getPOI()
	{
		return poi;
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
