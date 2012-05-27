package su.android.overlays;

import java.util.ArrayList;
import java.util.List;

import su.android.client.MainScreen;
import su.android.client.R;
import su.android.mapviewutil.GeoItem;
import su.android.markerclusterer.GeoClusterer;
import su.android.markerclusterer.MarkerBitmap;
import su.android.model.POI;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.google.android.maps.Overlay;

public class ClusterOverlay extends Overlay {

	private List<MarkerBitmap> markerIcons;
	private MainScreen mainScreen;
	private GeoClusterer clusterer;
	
	public ClusterOverlay(MainScreen mainScreen)
	{
		this.mainScreen = mainScreen;
		
		markerIcons = new ArrayList<MarkerBitmap>();
		/**
		 * 
		 * Clustering initialization
		 * 
		 */
		markerIcons.add(new MarkerBitmap(BitmapFactory.decodeResource(
				mainScreen.getResources(), R.drawable.balloon_s_n), BitmapFactory
				.decodeResource(mainScreen.getResources(), R.drawable.balloon_s_s),
				new Point(20, 20), // new Point( src_nrm.width/src_nrm.height,
				// src_nrm.width/src_nrm.height) ??
				14, 10));
		// large icon. 100 will be ignored.
		markerIcons.add(new MarkerBitmap(BitmapFactory.decodeResource(
				mainScreen.getResources(), R.drawable.balloon_l_n), BitmapFactory
				.decodeResource(mainScreen.getResources(), R.drawable.balloon_l_s),
				new Point(28, 28),
				16, 100));
		float screenDensity = mainScreen.getResources().getDisplayMetrics().density;
		clusterer = new GeoClusterer(mainScreen.getMap(), markerIcons, screenDensity);
	}
	
	public void notifyCluster() 
	{
		clusterer.onNotifyDrawFromCluster();
	}

	public void cluster(List<POI> poiList) {
		clusterer.removeCluster();
		// create clusterer instance
		// add geoitems for clustering
		for (int i = 0; i < poiList.size(); i++) {
			POI poi = poiList.get(i);
			double lat = poi.getLocationArray()[0];
			double lng = poi.getLocationArray()[1];
			clusterer.addItem(new GeoItem(i, (int) (lat * 1E6),
					(int) (lng * 1E6), poi.getCheckinsCount()));
		}
		// now redraw the cluster. it will create markers.
		clusterer.redraw();
		mainScreen.getMap().invalidate();	
	}

	public boolean onNotifyFilter(String category)
	{
		return false;
	}
	
	public boolean onHandlePoiList(List<POI> poiList) 
	{
		System.out.println("cluster");
		cluster(poiList);
		return true;
	}

}
