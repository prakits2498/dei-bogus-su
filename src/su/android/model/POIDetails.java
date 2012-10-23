package su.android.model;

import java.util.ArrayList;
import java.util.List;

public class POIDetails {
	
	private POI poi;
	private List<Product> products;
	
	public POIDetails()
	{		
		poi = null;
		products = new ArrayList<Product>();
	}
		
	public void setPOI(POI poi)
	{
		this.poi = poi;
	}
	
	public POI getPOI()
	{
		return poi;
	}
	
	public void addProduct(Product product)
	{
		this.products.add(product);
	}
	
	public void setProducts(List<Product> products)
	{
		this.products = products;
	}
	
	public List<Product> getProductList()
	{
		return this.products;
	}
}
