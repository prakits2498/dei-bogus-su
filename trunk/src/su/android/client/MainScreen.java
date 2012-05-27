package su.android.client;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.NormalActionBarItem;

import java.util.List;

import su.android.model.AppContext;
import su.android.model.POI;
import su.android.overlays.ClusterOverlay;
import su.android.overlays.SimpleItemizedOverlay;
import su.android.server.connection.ServerConnection;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;

import com.google.android.maps.MyLocationOverlay;

public class MainScreen extends GDMapActivity 
{
		
	//	Server connection
	private ServerConnection conn;
	//	Overlays
	private SimpleItemizedOverlay itemizedOverlay;
	private ClusterOverlay clusterOverlay;
	private MyLocationOverlay compass;
	//	Map Viewer
	private CustomMapView map;
	//	ActionBar viewers	
	private CategoryGridView categoryGridView;
	private BrowseDialog browseDialog;	
	//	Current Context 
	private AppContext currentContext;
	private Handler handler;
	
	public MainScreen() {
		conn = new ServerConnection();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setActionBarContentView(R.layout.main);
		addActionBarItem(Type.Eye, R.id.action_bar_slider); 
		addActionBarItem(Type.Compass, R.id.action_bar_category);
		addActionBarItem(getActionBar()
					.newActionBarItem(NormalActionBarItem.class)
					.setDrawable(new ActionBarDrawable(this,
								R.drawable.ic_search_light)),R.id.action_bar_search);		
		/**
		 * Map initialization
		 */
		map = (CustomMapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);
		map.getController().setZoom(13);	
		compass = new MyLocationOverlay(MainScreen.this, map);
		map.getOverlays().add(compass);		
//		LocationListener mlocListener = new MyLocationListener();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				1000, 0, mlocListener);
		this.prepareContext();
		/**
		 * Additional Views Initialization
		 */
		this.browseDialog = new BrowseDialog(this);
		this.categoryGridView = new CategoryGridView(this);		
		/**
		 * Overlays Initialization
		 */
		itemizedOverlay = new SimpleItemizedOverlay(this);
		clusterOverlay = new ClusterOverlay(this);
		//	Set the primary overlays in the map
		map.setPrimaryOverlays(clusterOverlay, itemizedOverlay);
		/**
		 * Start application
		 */
		handle();
	}

	public void prepareContext() {
		this.currentContext = new AppContext();
		Criteria hdCrit = new Criteria();
		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String mlocProvider = locationManager.getBestProvider(hdCrit, true);
		Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		double currentLatitude = currentLocation.getLatitude();
		double currentLongitude = currentLocation.getLongitude();
//		double currentLatitude = 40.2072;
//		double currentLongitude = -8.426428;
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int weekDayIndex = today.weekDay;
		int hour = today.hour;
		this.currentContext.setDayOfWeekIndex(weekDayIndex);
		this.currentContext.setHourOfDay(hour);
		this.currentContext.setLat(currentLatitude);
		this.currentContext.setLng(currentLongitude);
		Log.i("AppContext", this.currentContext.toString());
	}

	public void handle()
	{
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading POIs...");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() 
			{
				List<POI> poiList = conn.getPOIRecommendations(currentContext.getLat(),
						currentContext.getLng(), 
						currentContext.getDayOfWeek(), 
						currentContext.getHourOfDay(), 
						0.5, 
						50);
				onNotifyClusterOverlay(poiList);
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
		// TODO Auto-generated method stub
		compass.disableCompass();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		compass.enableCompass();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
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
	
	public boolean onNotifyClusterOverlay(List<POI> poiList)
	{
		return this.clusterOverlay.onHandlePoiList(poiList);
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