package su.android.client;

import su.android.overlays.PoiMarkersOverlay;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.google.android.maps.MapView;

public class CustomMapView extends MapView {

	private int oldZoomLevel = -1;
	private PoiMarkersOverlay poisOverlay = null;
	//private ClusterMarkersOverlay clusterOverlay = null;
	
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

				//poiMarkersZoomMode();
			 
				oldZoomLevel = getZoomLevel(); 
				
				/*if(clusterOverlay.isActivated())
				{
					clusterOverlay.onViewChange();
					Log.i("onTouchEvent", "RefreshCluster");
				}*/
				
			}
			
		});
	}
	
	/*public void poiMarkersZoomMode()
	{
		if(clusterOverlay.isActivated())
		{
			clusterOverlay.deactivate();
			Log.i("ZoomLevel", "Remove ClusterOverlay");
		}
		if(!getOverlays().contains(poisOverlay))
		{			
			poisOverlay.onHandlePoiList(clusterOverlay.getPoiList());
			getOverlays().add(poisOverlay);
			Log.i("ZoomLevel", "Add poisOverlay");
		}
	}
	
	public void poiMarkersClusterMode()
	{
		if(clusterOverlay.isActivated())
		{
			clusterOverlay.deactivate();
			Log.i("ZoomLevel", "Remove ClusterOverlay");
		}
		if(!getOverlays().contains(poisOverlay))
		{
			getOverlays().add(poisOverlay);
			Log.i("ZoomLevel", "Add poisOverlay");
		}
	}*/
	
	/*public void clusterMarkersMode()
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
	}*/
	
	public void setPrimaryOverlays(PoiMarkersOverlay poisOverlay)
	{
		//this.clusterOverlay = clusterOverlay;
		//this.getOverlays().add(clusterOverlay);
		this.poisOverlay = poisOverlay;
		this.getOverlays().add(poisOverlay);
		Log.i("Primary Overlay", "Activate poisOverlay");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{
		super.onTouchEvent(ev);
		if (ev.getAction()==MotionEvent.ACTION_UP)
		{
			/*if(clusterOverlay.isActivated())
			{
				clusterOverlay.onViewChange();
				Log.i("onTouchEvent", "RefreshCluster");
			}*/
		}
		
		poisOverlay.hideAllBalloons();
		return true;
	}

	
}
