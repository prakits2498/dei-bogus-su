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

package su.android.mapviewutil;

import java.util.ArrayList;

import su.android.client.InfoTabActivity;
import su.android.client.OverlayItemPOI;
import su.android.model.POI;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class SimpleItemizedOverlay extends BalloonItemizedOverlay<OverlayItemPOI> {

	private ArrayList<OverlayItemPOI> m_overlays = new ArrayList<OverlayItemPOI>();
	private Context c;
	
	public SimpleItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
	}

	public void addOverlay(OverlayItemPOI overlay) {
	    m_overlays.add(overlay);
	    populate();
	}
	
	public void removeOverlay() {
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
		
		Toast.makeText(c, "onBalloonTap for poi " + poi.getName(),
				Toast.LENGTH_LONG).show();
		
		Intent myIntent = new Intent(c, InfoTabActivity.class);
		
		myIntent.putExtra("poiID", poi.getId());
		myIntent.putExtra("poiName", poi.getName());
		myIntent.putExtra("poiAddress", poi.getAddress());
		myIntent.putExtra("poiCategory", poi.getCategory());
		myIntent.putExtra("poiSubCategory", poi.getSubCategory());
		myIntent.putExtra("poiCatIcon", poi.getDefaultCategoryIcon());
		
		if(poi.getPhotos() != null)
			if(!poi.getPhotos().isEmpty())
				myIntent.putExtra("poiPhoto01", poi.getPhotos().get(0));

		
		c.startActivity(myIntent);

		return true;
	}
	
}
