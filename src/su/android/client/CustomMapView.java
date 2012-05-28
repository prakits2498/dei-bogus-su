package su.android.client;

import java.util.List;

import su.android.overlays.ClusterMarkersOverlay;
import su.android.overlays.PoiMarkersOverlay;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class CustomMapView extends MapView {

	private int oldZoomLevel = -1;
	private PoiMarkersOverlay itemsOverlay = null;
	private ClusterMarkersOverlay clusterOverlay = null;
	
	public CustomMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomMapView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) 
	{
		  super(context, attrs, defStyle);
	}

	public CustomMapView(android.content.Context context, java.lang.String apiKey)
	{
		super(context, apiKey);
	}
	
	public void addZoomListenter()
	{
		this.getZoomButtonsController().setOnZoomListener(new OnZoomListener(){

			@Override
			public void onVisibilityChanged(boolean visible) 
			{
				// If the zoom widget is visible or not
			}

			@Override
			public void onZoom(boolean zoomIn) 
			{					
				if(zoomIn)
				{
					  if(getZoomLevel() < 18)
					  {
						  getZoomButtonsController().setZoomInEnabled(true);
						  getController().zoomIn();						
					  }
					  else
					  {
						  getZoomButtonsController().setZoomInEnabled(false);
					  }
				}
				else
				{
					  if(getZoomLevel() > 13)
					  {
						  getZoomButtonsController().setZoomOutEnabled(true);
						  getController().zoomOut();						 
					  }
					  else
					  {
						  getZoomButtonsController().setZoomOutEnabled(false);
					  }
				}			
				
				if (getZoomLevel() > 17)
				{
					poiMarkersZoomMode();
				}
				else
				{
					clusterMarkersMode();
				}				 
				oldZoomLevel = getZoomLevel(); 
				
				if(clusterOverlay.isActivated())
				{
					clusterOverlay.onViewChange();
					Log.i("onTouchEvent", "RefreshCluster");
				}
				
			}
			
		});
	}
	
	public void poiMarkersZoomMode()
	{
		if(clusterOverlay.isActivated())
		{
			clusterOverlay.deactivate();
			Log.i("ZoomLevel", "Remove ClusterOverlay");
		}
		if(!getOverlays().contains(itemsOverlay))
		{			
			itemsOverlay.onHandlePoiList(clusterOverlay.getPoiList());
			getOverlays().add(itemsOverlay);
			Log.i("ZoomLevel", "Add ItemsOverlay");
		}
	}
	
	public void poiMarkersClusterMode()
	{
		if(clusterOverlay.isActivated())
		{
			clusterOverlay.deactivate();
			Log.i("ZoomLevel", "Remove ClusterOverlay");
		}
		if(!getOverlays().contains(itemsOverlay))
		{
			getOverlays().add(itemsOverlay);
			Log.i("ZoomLevel", "Add ItemsOverlay");
		}
	}
	
	public void clusterMarkersMode()
	{
		if(getOverlays().contains(itemsOverlay))
		{
			getOverlays().remove(itemsOverlay);
			Log.i("ZoomLevel", "Remove ItemsOverlay");
		}
		if(!clusterOverlay.isActivated())
		{
			clusterOverlay.activate();
			Log.i("ZoomLevel", "Add ClusterOverlay");
		}
	}
	
	public void setPrimaryOverlays(ClusterMarkersOverlay clusterOverlay, PoiMarkersOverlay itemsOverlay)
	{
		this.clusterOverlay = clusterOverlay;
		this.getOverlays().add(clusterOverlay);
		this.itemsOverlay = itemsOverlay;
		Log.i("Primary Overlay", "Activate ClusterOverlay");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{
		super.onTouchEvent(ev);
		if (ev.getAction()==MotionEvent.ACTION_UP)
		{
			if(clusterOverlay.isActivated())
			{
				clusterOverlay.onViewChange();
				Log.i("onTouchEvent", "RefreshCluster");
			}
		}
		
		itemsOverlay.hideAllBalloons();
		return true;
	}

	
}
