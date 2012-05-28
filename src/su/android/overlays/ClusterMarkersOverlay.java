package su.android.overlays;

import java.util.ArrayList;
import java.util.List;

import su.android.client.CustomMapView;
import su.android.client.MainScreen;
import su.android.client.R;
import su.android.mapviewutil.GeoBounds;
import su.android.markerclusterer.ClusteringAlgorithm;
import su.android.markerclusterer.GeoCluster;
import su.android.markerclusterer.MarkerBitmap;
import su.android.model.POI;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;

import com.google.android.maps.GeoPoint;
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
		//	Clear cluster marker overlays
		clusterer.clearAlgorithm();			
		// create clusterer instance
		// add geoitems for clustering
		clusterer.addItems(poiList);
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
//						MarkerBitmap mkrBmp = markerIcons.get(0);
//						Bitmap bmp = mkrBmp.getBitmapNormal();
//						
//						Point grid = mkrBmp.getGrid();
//						Point gridReal = new Point((int) (grid.x * clusterer.getDensity() + 0.5f),
//								(int) (grid.y * clusterer.getDensity() + 0.5f));
//						canvas.drawBitmap(bmp, p.x - gridReal.x, p.y - gridReal.y, paint);
						//Draw a circle
						 Paint circle = new Paint(Paint.ANTI_ALIAS_FLAG);
						 //the circle to mark the spot
						 int perc = 0;
						 if(clusterer.getTotalCheckins() == 0)
						 {
							 perc = 0;
						 }
						 else
						 {
							 perc = (100*cluster.getTotalCheckins())/clusterer.getTotalCheckins();
						 }
						 if(perc < 11)
						 {
							 circle.setColor(Color.parseColor("#CCE0FF"));
						 }
						 else if(perc < 21)
						 {
							 circle.setColor(Color.parseColor("#99C2FF"));
						 }
						 else if(perc < 31)
						 {
							 circle.setColor(Color.parseColor("#66A3FF"));
						 }
						 else if(perc < 41)
						 {
							 circle.setColor(Color.parseColor("#3385FF"));
						 }
						 else if(perc < 51)
						 {
							 circle.setColor(Color.parseColor("#0066FF"));
						 }
						 else if(perc < 61)
						 {
							 circle.setColor(Color.parseColor("#0052CC"));
						 }
						 else
						 {
							 circle.setColor(Color.parseColor("#003D99"));
						 }
						 
						 circle.setAlpha(200);
						 // int radius = metersToRadius(1000, mapView,
						 // (double) center_.getLatitudeE6() / 1000000);
						 canvas.drawCircle(p.x, p.y, 40, circle);
						//End of circle	
						FontMetrics metrics = paint.getFontMetrics();
						int txtHeightOffset = (int) ((metrics.bottom + metrics.ascent) / 2.0f);
						int x = p.x;
						int y = p.y - txtHeightOffset;
						canvas.drawText(cluster.getItems().size()+"/"+cluster.getTotalCheckins()+"/"+perc, x, y, paint);
					}
				}
			}
		}
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView)
	{
		GeoCluster selectedCluster = null;
		for(GeoCluster cluster: clusters)
		{
			Projection proj = mapView.getProjection();
			Point pos = proj.toPixels(p, null);
			GeoPoint gpCenter = cluster.getLocation();
			Point ptCenter = proj.toPixels(gpCenter, null);
			final int GridSizePx = (int) ((ClusteringAlgorithm.GRIDSIZE / 2) * clusterer.getDensity() + 0.5f);
			if (pos.x >= ptCenter.x - GridSizePx
					&& pos.x <= ptCenter.x + GridSizePx
					&& pos.y >= ptCenter.y - GridSizePx
					&& pos.y <= ptCenter.y + GridSizePx) {
				Log.i("ClusterMarker", "TAPPED!");
				selectedCluster = cluster;
				break;
			}
		}
		if(selectedCluster != null)
		{
			List<POI> poiList = clusterer.getClusterPoiList(selectedCluster);
			mainScreen.onNotifyItemsOverlay(poiList);
			Projection pro = mapView.getProjection();
			Point ppt = pro.toPixels(selectedCluster.getCenter(), null);
			mapView.getController().setZoom(16);
			mapView.getController().zoomInFixing(ppt.x, ppt.y);
			((CustomMapView)mapView).poiMarkersClusterMode();
		}
		return true;
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
		this.poiList = poiList;
		return onNotifyFilter(mainScreen.getCurrentAppContext().getCategory());		
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
	
	public List<POI> getPoiList()
	{
		return this.poiList;
	}
}
