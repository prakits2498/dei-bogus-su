package su.android.model;

import java.util.ArrayList;
import java.util.List;

public class POIDetails {
	
	private POI poi;
	private List<Product> products;
	private List<Promotion> promotions;
	
	public POIDetails()
	{		
		poi = null;
		products = new ArrayList<Product>();
		promotions = new ArrayList<Promotion>();
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
	
	public void addPromotion(Promotion promotion)
	{
		this.promotions.add(promotion);
	}
	
	public void setProducts(List<Product> products)
	{
		this.products = products;
	}
	
	public void setPromotions(List<Promotion> promotions)
	{
		this.promotions = promotions;
	}
	
	public List<Promotion> getPromotionList()
	{
		return this.promotions;
	}
	
	public List<Product> getProductList()
	{
		return this.products;
	}
}
