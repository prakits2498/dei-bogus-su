package su.android.client;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.util.List;

import su.android.model.AppContext;
import su.android.model.POI;
import su.android.overlays.PoiMarkersOverlay;
import su.android.server.connection.ServerConnection;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MyLocationOverlay;

public class MainScreen extends GDMapActivity 
{
	//	Server connection
	private ServerConnection conn;
	
	
	//	Overlays
	private PoiMarkersOverlay itemizedOverlay;
	//private ClusterMarkersOverlay clusterOverlay;
	private MyLocationOverlay compass;
	
	
	//	Map Viewer
	private CustomMapView map;
	
	
	//	ActionBar viewers	
	private CategoryGridView categoryGridView;
	private BrowseDialog browseDialog;	
	
	
	//	Current Context 
	private AppContext currentContext;

	
	public MainScreen() {
		conn = new ServerConnection();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setActionBarContentView(R.layout.main);
		
		addActionBarItem(getActionBar()
				.newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this,
							R.drawable.slider_icon)), R.id.action_bar_slider); 
		addActionBarItem(getActionBar()
				.newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this,
							R.drawable.search_cat_icon)), R.id.action_bar_category);
		addActionBarItem(getActionBar()
					.newActionBarItem(NormalActionBarItem.class)
					.setDrawable(new ActionBarDrawable(this,
								R.drawable.search_icon)),R.id.action_bar_search);		
		/**
		 * Map initialization
		 */
		map = (CustomMapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);		
		map.addZoomListenter();
		map.setSatellite(true);
		compass = new MyLocationOverlay(MainScreen.this, map);
		map.getOverlays().add(compass);		
//		LocationListener mlocListener = new MyLocationListener();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				1000, 0, mlocListener);
		
		
		//************* GPS *************
		// Set the map viewport to Coimbra
		/*Criteria hdCrit = new Criteria();
		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String mlocProvider = locationManager.getBestProvider(hdCrit, true);
		Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);*/
		
		//40.186300, -8.414211
		
		GeoPoint point = new GeoPoint((int)(40.186300*1E6), (int)(-8.414211*1E6));
		
		map.getController().animateTo(point);
		map.getController().setZoom(13);
		
		// Capture the context
		this.prepareContext();
		
		/**
		 * Additional Views Initialization
		 */
		this.browseDialog = new BrowseDialog(this);
		this.categoryGridView = new CategoryGridView(this);
		
		
		/**
		 * Overlays Initialization
		 */
		itemizedOverlay = new PoiMarkersOverlay(this); //POIS
		//clusterOverlay = new ClusterMarkersOverlay(this);
		
		//	Set the primary overlays in the map
		map.setPrimaryOverlays(itemizedOverlay);
		/**
		 * Start application
		 */
		handle();
	}

	public void prepareContext() {
		this.currentContext = new AppContext();
//		Criteria hdCrit = new Criteria();
//		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
//		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		String mlocProvider = locationManager.getBestProvider(hdCrit, true);
//		Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
//		double currentLatitude = currentLocation.getLatitude();
//		double currentLongitude = currentLocation.getLongitude();
//		double currentLatitude = 40.2072;
//		double currentLongitude = -8.426428;
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int weekDayIndex = today.weekDay;
		int hour = today.hour;
		if(hour == 23 || (hour >= 0 && hour < 7))
		{
			hour = 4;
		}
		else if(hour > 6 && hour < 12)
		{
			hour = 0;
		}
		else if(hour >= 12 && hour < 15)
		{
			hour = 1;
		}
		else if(hour >= 15 && hour < 20)
		{
			hour = 2;
		}
		else if(hour >= 20 && hour < 23)
		{
			hour = 3;
		}
		String category = getResources().getString(R.string.all);
		this.currentContext.setCategory(category);
		this.currentContext.setDayOfWeekIndex(weekDayIndex);
		this.currentContext.setHourOfDay(hour);
//		this.currentContext.setLat(currentLatitude);
//		this.currentContext.setLng(currentLongitude);
		Log.i("AppContext", this.currentContext.toString());
	}

	public void handle()
	{
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading POIs...");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() 
			{
				//TODO ir buscar as cantinas ˆ BD
				List<POI> poiList = conn.getPOIRecommendations(
						currentContext.getDayOfWeek(), 
						currentContext.getHourOfDay(), 
						60);
				
				//onNotifyClusterOverlay(poiList);
				onNotifyItemsOverlay(poiList);
				progressDialog.dismiss();
			}

		}, 1000);
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) 
		{
			case R.id.action_bar_slider: 
				this.browseDialog.onActivateDialog();
				return true;		
			case R.id.action_bar_search:
				onSearchRequested(); 
				return true;		
			case R.id.action_bar_category: 
				this.categoryGridView.onActivateCategory(item.getItemView()); 
				return true;		
			default:
				return super.onHandleActionBarItemClick(item, position);
		}
	}

	@Override
	protected void onPause() {
		compass.disableCompass();
		super.onPause();
	}

	@Override
	protected void onResume() {
		compass.enableCompass();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public boolean onNotifyItemsOverlay(List<POI> poiList)
	{
		return this.itemizedOverlay.onHandlePoiList(poiList);
	}
	
	public boolean onNotifyItemsOverlay(String category)
	{
		return this.itemizedOverlay.onNotifyFilter(category);
	}

	public CustomMapView getMap()
	{
		return this.map;
	}
	
	public AppContext getCurrentAppContext()
	{
		return currentContext;
	}
	
//	/**
//	 * onCreateOptionsMenu handler
//	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		MenuItem menu_Revert = menu.add(0, 1, 0, "Display GeoItems");
//		menu_Revert.setIcon(android.R.drawable.ic_menu_myplaces);
//		return true;
//	}
//
//	/**
//	 * onOptionsItemSelected handler since clustering need MapView to be created
//	 * and visible, this sample do clustering here.
//	 */
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case 1:
//			pins();
//			break;
//		}
//		return true;
//	}
}