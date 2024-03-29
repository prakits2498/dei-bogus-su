package su.android.overlays;

import su.android.model.POI;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PoiMarker extends OverlayItem {

	private POI poi;
	
	public PoiMarker(GeoPoint point, String title, String snippet, POI poi) {
		super(point, title, snippet);
		this.poi = poi;
	}

	public POI getPoi() {
		return poi;
	}

	public void setPoi(POI poi) {
		this.poi = poi;
	}
	
	

}
