package su.android.client;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.text.DecimalFormat;
import java.util.List;

import su.android.model.AppContext;
import su.android.model.POI;
import su.android.overlays.PoiMarkersOverlay;
import su.android.server.connection.ServerConnection;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MyLocationOverlay;

public class MainScreen extends GDMapActivity 
{
	//	Server connection
	private ServerConnection conn;
	
	//	Overlays
	private PoiMarkersOverlay poisOverlay;
	private MyLocationOverlay compass;
	
	//	Map Viewer
	private CustomMapView map;
	
	//	ActionBar viewers	
	private CategoryGridView categoryGridView;
	
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
							R.drawable.calendar_icon)), R.id.action_bar_category);
		addActionBarItem(getActionBar()
					.newActionBarItem(NormalActionBarItem.class)
					.setDrawable(new ActionBarDrawable(this,
								R.drawable.search_icon)),R.id.action_bar_search);
		addActionBarItem(getActionBar()
				.newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this,
							R.drawable.euro_icon_03)),R.id.action_bar_credits);
		
		
		/**
		 * Map initialization
		 */
		map = (CustomMapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);		
		map.addZoomListenter();
		map.setSatellite(true);
		compass = new MyLocationOverlay(MainScreen.this, map);
		map.getOverlays().add(compass);		

		//************* GPS *************
		// Set the map viewport to Coimbra
		/*Criteria hdCrit = new Criteria();
		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String mlocProvider = locationManager.getBestProvider(hdCrit, true);
		Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);*/
		
		GeoPoint point = new GeoPoint((int)(40.186300*1E6), (int)(-8.414211*1E6));
		
		map.getController().animateTo(point);
		map.getController().setZoom(14);
		
		// Capture the context
		this.prepareContext();
		
		/**
		 * Additional Views Initialization
		 */
		this.categoryGridView = new CategoryGridView(this);
		
		
		/**
		 * Overlays Initialization
		 */
		poisOverlay = new PoiMarkersOverlay(this); 
		
		//	Set the primary overlays in the map
		map.setPrimaryOverlays(poisOverlay);
		/**
		 * Start application
		 */
		handle();
	}

	public void prepareContext() {
		this.currentContext = new AppContext();

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
		this.currentContext.setIdUser(this.getIntent().getExtras().getInt("idUser"));
		Log.i("AppContext", this.currentContext.toString());
	}

	public void handle()
	{
		final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() 
			{
				List<POI> poiList = conn.getPOIRecommendations(
						currentContext.getDayOfWeek(), 
						currentContext.getHourOfDay(), 
						60);
				
				onNotifyItemsOverlay(poiList);
				progressDialog.dismiss();
			}

		}, 1000);
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) 
		{		
			case R.id.action_bar_search:
				//onSearchRequested();
				Intent i = new Intent(getApplicationContext(), SearchableActivity.class);
				i.putExtra("dayOfWeek", this.currentContext.getDayOfWeek());
				i.putExtra("hour", this.currentContext.getHourOfDay());
				startActivity(i);
				return true;		
			case R.id.action_bar_category:
				Intent ii = new Intent(getApplicationContext(), SimpleCalendarViewActivity.class);
				ii.putExtra("idUser", this.currentContext.getIdUser());
				startActivity(ii);
				//this.categoryGridView.onActivateCategory(item.getItemView()); 
				return true;
			case R.id.action_bar_credits:
				double creditos = conn.getCredits(this.currentContext.getIdUser());

		        DecimalFormat df = new DecimalFormat("#.##");
		        
				// get your custom_toast.xml ayout
				LayoutInflater inflater = getLayoutInflater();
 
				View layout = inflater.inflate(R.layout.custom_toast,
				  (ViewGroup) findViewById(R.id.custom_toast_layout_id));
				//layout.setBackgroundColor(R.color.white);
 
				// set a dummy image
				ImageView image = (ImageView) layout.findViewById(R.id.image);
				image.setImageResource(R.drawable.euro_icon_blue_big_03);
 
				// set a message
				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText("Tem " + df.format(creditos) + "Û de saldo");
				//text.setTextColor(R.color.myTurquesa);
 
				// Toast...
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
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
		return this.poisOverlay.onHandlePoiList(poiList);
	}
	
	public boolean onNotifyItemsOverlay(String category)
	{
		return this.poisOverlay.onNotifyFilter(category);
	}

	public CustomMapView getMap()
	{
		return this.map;
	}
	
	public AppContext getCurrentAppContext()
	{
		return currentContext;
	}
}