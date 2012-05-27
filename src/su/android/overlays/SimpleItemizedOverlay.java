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
import su.android.model.POI;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class SimpleItemizedOverlay extends BalloonItemizedOverlay<OverlayItemPOI> {

	private List<OverlayItemPOI> m_overlays;
	private MainScreen mainScreen;
	
	public SimpleItemizedOverlay(final MainScreen mainScreen) {
		super(boundCenter(mainScreen.getResources().getDrawable(android.R.drawable.ic_menu_myplaces)), mainScreen.getMap());
		this.m_overlays = new ArrayList<OverlayItemPOI>();
		setShowClose(false);
		setShowDisclosure(true);
		setSnapToCenter(false);
	}

	public void addOverlay(OverlayItemPOI overlay) {
	    m_overlays.add(overlay);
	    populate();
	}
	
	public void clear() {
	    m_overlays.clear();
	    populate();
	}

	@Override
	protected OverlayItemPOI createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItemPOI item) {
		POI poi = item.getPoi();
		
		Intent myIntent = new Intent(mainScreen.getApplicationContext(), InfoTabActivity.class);
		
		myIntent.putExtra("poiID", poi.getId());
		myIntent.putExtra("poiName", poi.getName());
		myIntent.putExtra("poiAddress", poi.getAddress());
		myIntent.putExtra("poiCategory", poi.getCategory());
		myIntent.putExtra("poiSubCategory", poi.getSubCategory());
		myIntent.putExtra("poiCatIcon", poi.getDefaultCategoryIcon().replace(".png", "_64.png"));
		
		if(poi.getPhotos() != null)
			if(!poi.getPhotos().isEmpty())
				myIntent.putExtra("poiPhoto01", poi.getPhotos().get(0));

		mainScreen.getApplicationContext().startActivity(myIntent);

		return true;
	}
	
	public boolean onHandlePoiList(List<POI> poiList)
	{
		this.clear();
		for (int i = 0; i < poiList.size(); i++) {
			POI poi = poiList.get(i);
			double lat = poi.getLocationArray()[0];
			double lng = poi.getLocationArray()[1];
			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			// Balloon
			OverlayItemPOI overlayItem = new OverlayItemPOI(point, poi.getName(),
					"Nº checkins: " + poi.getCheckinsCount(), poi);
			//TODO escolher o icone de acordo com as categorias
			//overlayItem.setMarker(marker);
			this.addOverlay(overlayItem);
		}
		return true;
	}
	
	public boolean onNotifyFilter(String category)
	{
//		if(category.equals(mainScreen.getApplicationContext().getResources().getString(R.string.all))){
//			for (int i=0; i < m_overlays.size(); i++){
//
//				POI poi = m_overlays.get(i).getPoi();				
//				//TODO escolher o icone de acordo com as categorias
//				//overlayItem.setMarker(marker);
//				this.addOverlay(overlayItem);
//			}
//		}
//		else
//		{
//			for (int i=0; i< poiList.size(); i++)
//			{
//				POI poi = poiList.get(i);
//				if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(category))
//				{
//					double lat = poi.getLocationArray()[0];
//					double lng = poi.getLocationArray()[1];
//					GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
//					// Balloon
//					OverlayItemPOI overlayItem = new OverlayItemPOI(point, poi.getName(),
//							"Nº checkins: " + poi.getCheckinsCount(), poi);
//					//TODO escolher o icone de acordo com as categorias
//					//overlayItem.setMarker(marker);
//					this.addOverlay(overlayItem);			
//				}
//			}	
//		}
		return false;
	}
	
}
