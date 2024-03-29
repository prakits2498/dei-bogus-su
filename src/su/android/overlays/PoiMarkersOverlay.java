/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package su.android.overlays;

import java.util.ArrayList;
import java.util.List;

import su.android.client.InfoTabActivity;
import su.android.client.MainScreen;
import su.android.client.R;
import su.android.model.POI;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.fedorvlasov.lazylist.ImageLoader;
import com.google.android.maps.GeoPoint;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class PoiMarkersOverlay extends BalloonItemizedOverlay<PoiMarker> {

	private List<PoiMarker> items;
	private List<PoiMarker> unusedItems;
	private MainScreen mainScreen;
	
	public PoiMarkersOverlay(final MainScreen mainScreen) {
		super(boundCenter(mainScreen.getResources().getDrawable(android.R.drawable.ic_menu_myplaces)), mainScreen.getMap());
		this.mainScreen = mainScreen;
		this.items = new ArrayList<PoiMarker>();
		this.unusedItems = new ArrayList<PoiMarker>();
		this.mainScreen = mainScreen;
		setShowClose(false);
		setShowDisclosure(true);
		setSnapToCenter(false);
	}

	@Override
	protected PoiMarker createItem(int i) 
	{
		if(items.size() > 0)
		{
			return items.get(i);
		}
		else
		{
			return null;
		}
	}

	@Override
	public int size() 
	{
		return items.size();
	}

	@Override
	protected boolean onBalloonTap(int index, PoiMarker item) {
		if(item != null)
		{
			POI poi = item.getPoi();
			
			Intent myIntent = new Intent(mainScreen.getApplicationContext(), InfoTabActivity.class);
			
			myIntent.putExtra("userID", Integer.toString(mainScreen.getCurrentAppContext().getIdUser()));
			
			myIntent.putExtra("poiID", poi.getId());
			myIntent.putExtra("poiName", poi.getName());
			myIntent.putExtra("poiAddress", poi.getAddress());
			myIntent.putExtra("poiCategory", poi.getCategory());
			//myIntent.putExtra("poiCatIcon", poi.getDefaultCategoryIcon().replace(".png", "_64.png"));
			myIntent.putExtra("poiCatIcon", "https://dl.dropbox.com/u/999549/aLaCarteIcons/"+poi.getCategory()+".png");
	
			mainScreen.startActivity(myIntent);
	
			return true;
		}
		return false;
	}
	
	public boolean onHandlePoiList(List<POI> poiList)
	{		
		this.hideAllBalloons();
		items.clear();
		for (int i = 0; i < poiList.size(); i++) {
			POI poi = poiList.get(i);
			double lat = poi.getLocationArray()[0];
			double lng = poi.getLocationArray()[1];
			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			// Balloon
			PoiMarker overlayItem = new PoiMarker(point, poi.getName(),
					"Carregue para ver os menus", poi);
			
			String mainCat1 = mainScreen.getResources().getString(R.string.mainCat1);
			String mainCat2 = mainScreen.getResources().getString(R.string.mainCat2);
			String mainCat3 = mainScreen.getResources().getString(R.string.mainCat3);
			String mainCat4 = mainScreen.getResources().getString(R.string.mainCat4);
			String mainCat5 = mainScreen.getResources().getString(R.string.mainCat5);
			String mainCat6 = mainScreen.getResources().getString(R.string.mainCat6);
			
			if(poi.getCategory().equals("cantina")) {
				Drawable drawable = mainScreen.getResources().getDrawable(R.drawable.map_pin_03);
				boundCenter(drawable);
				overlayItem.setMarker(drawable);
			} else if(poi.getCategory().equals("bar")) {
				Drawable drawable = mainScreen.getResources().getDrawable(R.drawable.map_pin_baguetes_03);
				boundCenter(drawable);
				overlayItem.setMarker(drawable);
			} else if(poi.getCategory().equals("vegetariano")) {
				Drawable drawable = mainScreen.getResources().getDrawable(R.drawable.map_pin_veg_03);
				boundCenter(drawable);
				overlayItem.setMarker(drawable);
			} else if(poi.getCategory().equals("grelhados")) {
				Drawable drawable = mainScreen.getResources().getDrawable(R.drawable.map_pin_bbq_03);
				boundCenter(drawable);
				overlayItem.setMarker(drawable);
			} else if(poi.getCategory().equals("pizza")) {
				Drawable drawable = mainScreen.getResources().getDrawable(R.drawable.map_pin_pizza_03);
				boundCenter(drawable);
				overlayItem.setMarker(drawable);
			} else if(poi.getCategory().equals("sandes")) {
				Drawable drawable = mainScreen.getResources().getDrawable(R.drawable.map_pin_baguetes_03);
				boundCenter(drawable);
				overlayItem.setMarker(drawable);
			}

			this.items.add(overlayItem);
		}
		onNotifyFilter(mainScreen.getCurrentAppContext().getCategory());
		populate();
		mainScreen.getMap().postInvalidate();
		return true;
	}
	
	public boolean onNotifyFilter(String category)
	{
		//	Remove all items to garbage
		this.hideAllBalloons();
		unusedItems.addAll(items);
		items.clear();
		if(category.equals(mainScreen.getResources().getString(R.string.all)))
		{
			items.addAll(unusedItems);
			unusedItems.clear();			
		}
		else
		{			
			for(PoiMarker item: unusedItems)
			{
				POI poi = item.getPoi();
				if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(category))
				{					
					this.items.add(item);	
				}
			}	
		}
		if(items.size() > 0)
		{
			populate();
			mainScreen.getMap().postInvalidate();
		}
		return true;
	}
	
}
