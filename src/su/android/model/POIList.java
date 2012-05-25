package su.android.model;

import java.util.List;

public class POIList 
{
	private List<POI> poiList;
	
	public POIList()
	{		
		poiList = null;
	}
	
	public void setPoiList(List<POI> poiList)
	{
		this.poiList = poiList;
	}
	
	public List<POI> getPoiList()
	{
		return poiList;
	}
}
