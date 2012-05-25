package su.android.client;

import su.android.markerclusterer.GeoClusterer;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class CustomMapView extends MapView {

	private int oldZoomLevel = -1;
	private GeoClusterer clusterer = null;

	public CustomMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CustomMapView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) 
	{
		  super(context, attrs, defStyle);
	}

	public CustomMapView(android.content.Context context, java.lang.String apiKey)
	{
		super(context, apiKey);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{
		if (ev.getAction()==MotionEvent.ACTION_UP) 
		{
			clusterer.onNotifyDrawFromCluster();
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void dispatchDraw(Canvas canvas) 
	{		  
		  if (getZoomLevel() != oldZoomLevel) 
		  {
			  oldZoomLevel = getZoomLevel();
			  clusterer.onNotifyDrawFromCluster();  
		  }
		  super.dispatchDraw(canvas);
	}

	public void setClusterAlgorithm(GeoClusterer clusterer) {
		this.clusterer = clusterer;
	}	
	
}
