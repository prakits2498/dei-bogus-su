package su.android.client;

import su.android.overlays.ClusterOverlay;
import su.android.overlays.SimpleItemizedOverlay;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class CustomMapView extends MapView {

	private int oldZoomLevel = -1;
	private SimpleItemizedOverlay itemsOverlay = null;
	private ClusterOverlay clusterOverlay = null;
	private State state = State.CLUSTER;
	
	private static enum State 
	{
		CLUSTER,
		PINS,
	};
	
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
	
	public void setPrimaryOverlays(ClusterOverlay clusterOverlay, SimpleItemizedOverlay itemsOverlay)
	{
		this.clusterOverlay = clusterOverlay;
		this.itemsOverlay = itemsOverlay;
		if(state == State.CLUSTER)
		{
			Log.i("Primary Overlay", "Activate ClusterOverlay");
			this.getOverlays().add(clusterOverlay);
		}
		else
		{
			Log.i("Primary Overlay", "Activate ItemsOverlay");
			this.getOverlays().add(itemsOverlay);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{
		if (ev.getAction()==MotionEvent.ACTION_UP)
		{
			if(state == State.CLUSTER)
			{
				clusterOverlay.notifyCluster();
				Log.i("onTouchEvent", "RefreshCluster");
			}
		}
		if(state == State.PINS)
		{
			itemsOverlay.hideAllBalloons();
			Log.i("onTouchEvent", "Clear Ballons");
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void dispatchDraw(Canvas canvas) 
	{		  
		  if (getZoomLevel() != oldZoomLevel) 
		  {			  
			  if (getZoomLevel() > 15) 
			  {
				if(this.getOverlays().contains(clusterOverlay))
				{
					this.getOverlays().remove(clusterOverlay);
					Log.i("ZoomLevel", "Remove ClusterOverlay");
				}
				if(!this.getOverlays().contains(itemsOverlay))
				{
					this.getOverlays().add(itemsOverlay);
					Log.i("ZoomLevel", "Add ItemsOverlay");
				}
				state = State.PINS;
				Log.i("ZoomLevel", "PINS");
			  }
			  else
			  {
				  if(this.getOverlays().contains(itemsOverlay))
				  {
					  this.getOverlays().remove(itemsOverlay);
					  Log.i("ZoomLevel", "Remove ItemsOverlay");
				  }
				  if(!this.getOverlays().contains(clusterOverlay))
				  {
					  this.getOverlays().add(clusterOverlay);
					  Log.i("ZoomLevel", "Add ClusterOverlay");
				  }
				  state = State.CLUSTER;	
				  Log.i("ZoomLevel", "CLUSTER");
			  }				 
			  oldZoomLevel = getZoomLevel();
		  }
		  super.dispatchDraw(canvas);
	}
	
}
