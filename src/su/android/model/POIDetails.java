package su.android.model;

import java.util.List;

public class POIDetails {
	
	private Merchant merchant;
	private List<Products> products;
	private List<Promotions> promotions;
	
	public POIDetails()
	{		
		merchant = null;
		products = null;
		promotions = null;
	}
	
	public void setMerchant(Merchant merchant)
	{
		this.merchant = merchant;
	}
	
	public Merchant getMerchant()
	{
		return merchant;
	}
	
	public void setProductList(List<Products> products)
	{
		this.products = products;
	}
	
	public List<Products> getProductList()
	{
		return products;
	}
	
	public void setPromotionList(List<Promotions> promotions)
	{
		this.promotions = promotions;
	}
	
	public List<Promotions> getPromotionList()
	{
		return promotions;
	}

}
