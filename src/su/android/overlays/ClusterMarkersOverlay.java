package su.android.overlays;

import java.util.ArrayList;
import java.util.List;

import su.android.client.MainScreen;
import su.android.client.R;
import su.android.mapviewutil.GeoBounds;
import su.android.mapviewutil.GeoItem;
import su.android.markerclusterer.ClusteringAlgorithm;
import su.android.markerclusterer.GeoCluster;
import su.android.markerclusterer.MarkerBitmap;
import su.android.model.POI;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class ClusterMarkersOverlay extends Overlay{

	private List<MarkerBitmap> markerIcons;
	private MainScreen mainScreen;
	private ClusteringAlgorithm clusterer;
	private List<GeoCluster> clusters;
	private List<POI> poiList;
	private List<POI> unusedPoiList;
	private boolean activated = true;
	private GeoBounds lastBounds = null;
	
	public ClusterMarkersOverlay(MainScreen mainScreen)
	{
		this.mainScreen = mainScreen;
		this.clusters = new ArrayList<GeoCluster>();
		this.poiList = null;
		this.unusedPoiList = new ArrayList<POI>();
		
		this.lastBounds = getBounds();
		
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
		clusterer = new ClusteringAlgorithm(mainScreen.getMap(), markerIcons, screenDensity);
	}	
	


	public void cluster(List<POI> poiList) 
	{		
		Log.i("Cluster", "Restart");
		this.poiList = poiList;
		//	Clear cluster marker overlays
		clusterer.clearAlgorithm();			
		// create clusterer instance
		// add geoitems for clustering
		for (int i = 0; i < poiList.size(); i++) 
		{
			POI poi = poiList.get(i);
			double lat = poi.getLocationArray()[0];
			double lng = poi.getLocationArray()[1];
			clusterer.addItem(new GeoItem(i, (int) (lat * 1E6),
					(int) (lng * 1E6), poi.getCheckinsCount()));
		}
		// create markers.		
		clusters = clusterer.getClusters();
	}

	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow)
	{
		if(isActivated())
		{
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			paint.setTextSize(markerIcons.get(0).getTextSize() * clusterer.getDensity());
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			
			synchronized(clusters)
			{
				for(GeoCluster cluster: clusters)
				{
					// cluster_.onNotifyDrawFromMarker();
					Projection proj = mapView.getProjection();
					Point p = proj.toPixels(cluster.getCenter(), null);
					this.lastBounds = new GeoBounds(proj.fromPixels(0, 0),
							proj.fromPixels(mapView.getWidth(), mapView.getHeight()));
					if (this.lastBounds.isInBounds(cluster.getCenter()))
					{
						MarkerBitmap mkrBmp = markerIcons.get(0);
						Bitmap bmp = mkrBmp.getBitmapNormal();
						
						Point grid = mkrBmp.getGrid();
						Point gridReal = new Point((int) (grid.x * clusterer.getDensity() + 0.5f),
								(int) (grid.y * clusterer.getDensity() + 0.5f));
						canvas.drawBitmap(bmp, p.x - gridReal.x, p.y - gridReal.y, paint);
						// Draw a circle
						// Paint circle = new Paint(Paint.ANTI_ALIAS_FLAG);
						// the circle to mark the spot
						// circle.setColor(Color.parseColor("#88ff0000"));
						// circle.setAlpha(35); // trasparenza
						// // int radius = metersToRadius(1000, mapView,
						// // (double) center_.getLatitudeE6() / 1000000);
						// canvas.drawCircle(p.x, p.y, 56, circle);
						// End of circle	
						FontMetrics metrics = paint.getFontMetrics();
						int txtHeightOffset = (int) ((metrics.bottom + metrics.ascent) / 2.0f);
						int x = p.x;
						int y = p.y - txtHeightOffset;
						canvas.drawText(""+cluster.getItems().size(), x, y, paint);
					}
				}
			}
		}
	}
	
	public void activate()
	{
		this.activated = true;
	}
	
	public void deactivate()
	{
		this.activated = false;
	}
	
	public boolean onNotifyFilter(String category)
	{		
		//	Remove all items to garbage
		unusedPoiList.addAll(poiList);
		poiList.clear();
		if(category.equals(mainScreen.getResources().getString(R.string.all)))
		{
			poiList.addAll(unusedPoiList);
			unusedPoiList.clear();			
		}
		else
		{			
			for(POI poi: unusedPoiList)
			{
				if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(category))
				{					
					this.poiList.add(poi);	
				}
			}
		}
		cluster(poiList);
		return true;	
	}
	
	public boolean onViewChange()
	{
		if(activated && !isSameViewport())
		{			
			cluster(poiList);
			mainScreen.getMap().postInvalidate();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isSameViewport() 
	{
		GeoBounds currentBounds = getBounds();
		if(lastBounds.equals(currentBounds))
		{
			Log.i("Viewport", "Same");
			this.lastBounds = currentBounds;
			return true;
		}
		Log.i("Viewport", "Different");
		this.lastBounds = currentBounds;
		return false;		
	}

	public boolean onHandlePoiList(List<POI> poiList) 
	{
		if(activated)
		{
			cluster(poiList);
			mainScreen.getMap().postInvalidate();
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isActivated() 
	{	
		return activated;
	}

	private GeoBounds getBounds() 
	{
		Projection proj = mainScreen.getMap().getProjection();
		GeoBounds bounds = new GeoBounds(proj.fromPixels(0,0),proj.fromPixels(mainScreen.getMap().getWidth(),mainScreen.getMap().getHeight()));
		return bounds;
	}
}
