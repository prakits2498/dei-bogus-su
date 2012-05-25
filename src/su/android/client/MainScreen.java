package su.android.client;

import greendroid.app.GDMapActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.List;

import su.android.mapviewutil.GeoItem;
import su.android.mapviewutil.SimpleItemizedOverlay;
import su.android.markerclusterer.GeoClusterer;
import su.android.markerclusterer.MarkerBitmap;
import su.android.model.AppContext;
import su.android.model.POI;
import su.android.server.connection.ServerConnection;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainScreen extends GDMapActivity {
	/** Called when the activity is first created. */
	CustomMapView map;
	long start;
	long stop;
	MyLocationOverlay compass;
	MapController controller;
	LocationManager locationManager;
	GeoClusterer clusterer;
	Drawable drawable;
	Drawable drawable2;
	private QuickActionWidget mGrid;

	TextView readingDay;
	TextView readingHour;
	SeekBar seekBarDay;
	SeekBar seekBarHour;
	boolean browse=false;

	// marker icons
	private List<MarkerBitmap> markerIconBmps_ = new ArrayList<MarkerBitmap>();

	int x, y;
	GeoPoint touchedPoint;

	Drawable d;
	List<Overlay> overlayList;

	ServerConnection conn;

	private int oldZoomLevel = -1;
	private int markersLastIndex = -1;

	List<POI> poiList = null;
	SimpleItemizedOverlay itemizedOverlay;

	public MainScreen() {
		conn = new ServerConnection();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.main);

		prepareQuickActionGrid();

		addActionBarItem(Type.Locate, R.id.action_bar_pois);

		addActionBarItem(
				getActionBar().newActionBarItem(NormalActionBarItem.class)
				.setDrawable(
						new ActionBarDrawable(this,
								R.drawable.ic_search_light)),
								R.id.action_bar_search);

		map = (CustomMapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);


		//SEEKBAR
		readingDay = (TextView) findViewById(R.id.readingDay);
		readingHour = (TextView) findViewById(R.id.readingHour);

		seekBarDay = (SeekBar)findViewById(R.id.seekbarDay);
		seekBarDay.setMax(6);
		seekBarDay.setOnSeekBarChangeListener(new MySeekBarListener(readingDay));

		seekBarHour = (SeekBar)findViewById(R.id.seekbarHour);
		seekBarHour.setMax(23);
		seekBarHour.setOnSeekBarChangeListener(new MySeekBarListener(readingHour));



		BrowsingOff();


		float screenDensity = this.getResources().getDisplayMetrics().density;
		clusterer = new GeoClusterer(map, markerIconBmps_, screenDensity);
		map.setClusterAlgorithm(clusterer);

		overlayList = map.getOverlays();
		compass = new MyLocationOverlay(MainScreen.this, map);
		overlayList.add(compass);


		Touchy t = new Touchy();
		overlayList.add(t);
		System.out.println("NUMERO DE OVERLAYS DEPOIS DO TOUCHY:" + map.getOverlays().size());

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener mlocListener = new MyLocationListener();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, mlocListener);
		// prepare for marker icons.
		// small icon for maximum 10 items
		markerIconBmps_.add(new MarkerBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.balloon_s_n), BitmapFactory
				.decodeResource(getResources(), R.drawable.balloon_s_s),
				new Point(20, 20), // new Point( src_nrm.width/src_nrm.height,
				// src_nrm.width/src_nrm.height) ??
				14, 10));
		// large icon. 100 will be ignored.
		markerIconBmps_.add(new MarkerBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.balloon_l_n), BitmapFactory
				.decodeResource(getResources(), R.drawable.balloon_l_s),
				new Point(28, 28), // new Point( src_nrm.width/src_nrm.height,
				// src_nrm.width/src_nrm.height) ??
				16, 100));

		controller = map.getController();
		controller.setZoom(13);

		final ProgressDialog progressDialog = ProgressDialog.show(
				MainScreen.this, "", "Loading POIs...");

		loadInitialPOIs();


		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				teste();
				progressDialog.dismiss();
			}

		}, 1000);


		//PREPARA�AO DO PINAMENTO
		drawable = getResources().getDrawable(R.drawable.ic_pin);

		itemizedOverlay = new SimpleItemizedOverlay(drawable, this.map);
		itemizedOverlay.setShowClose(false);
		itemizedOverlay.setShowDisclosure(true);
		itemizedOverlay.setSnapToCenter(false);


	}

	public void loadInitialPOIs() {
		AppContext appCtxt = setupContext();
		poiList = conn.getPOIRecommendations(appCtxt.getLat(),
				appCtxt.getLng(), 0.5, 100);

	}

	/**
	 * onCreateOptionsMenu handler
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem menu_Revert = menu.add(0, 1, 0, "Display GeoItems");
		menu_Revert.setIcon(android.R.drawable.ic_menu_myplaces);
		return true;
	}

	/**
	 * onOptionsItemSelected handler since clustering need MapView to be created
	 * and visible, this sample do clustering here.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			pins();
			break;
		}
		return true;
	}

	public void pins() {

		for (int i = 0; i < poiList.size(); i++) {
			POI poi = poiList.get(i);

			double lat = poi.getLocationArray()[0];
			double lng = poi.getLocationArray()[1];

			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));


			// Balloon
			OverlayItemPOI overlayItem = new OverlayItemPOI(point, poi.getName(),
					"Nº checkins: " + poi.getCheckinsCount(), poi);

			//TODO escolher o icone de acordo com as categorias
			//overlayItem.setMarker(marker);

			itemizedOverlay.addOverlay(overlayItem);

		}

		overlayList.add(itemizedOverlay);
	}

	public void teste() {
		AppContext appContext = setupContext();
		GeoPoint point = new GeoPoint((int) (appContext.getLat() * 1E6),
				(int) (appContext.getLng() * 1E6));
		controller.animateTo(point);
		//List<POI> poiList = conn.getPOIRecommendations(appContext.getLat(), appContext.getLng(), 0.5, 100);
		cluster();
	}

	private String getDayOfWeek(int index) {
		String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Sunday" };
		return days[index];
	}

	public AppContext setupContext() {
		AppContext appContext = new AppContext();
		Criteria hdCrit = new Criteria();
		hdCrit.setAccuracy(Criteria.ACCURACY_FINE);
		// String mlocProvider = locationManager.getBestProvider(hdCrit, true);

		// Location currentLocation = locationManager
		// .getLastKnownLocation(mlocProvider);
		// double currentLatitude = currentLocation.getLatitude();
		// double currentLongitude = currentLocation.getLongitude();

		double currentLatitude = 40.2072;
		double currentLongitude = -8.426428;

		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int weekDayIndex = today.weekDay;
		int hour = today.hour;
		String weekDay = getDayOfWeek(weekDayIndex);
		appContext.setDayOfWeek(weekDay);
		appContext.setHourOfDay(hour);
		appContext.setLat(currentLatitude);
		appContext.setLng(currentLongitude);
		String text = "Day " + weekDay + " Hour " + hour + " latitude "
				+ currentLatitude + " longitude " + currentLongitude;
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
		.show();
		
		//sliders
		System.out.println(weekDayIndex);
		seekBarDay.setProgress(weekDayIndex);
		readingDay.setText(GetDayString(weekDayIndex));
		seekBarHour.setProgress(hour);
		readingHour.setText(GetHourString(hour));
		
		return appContext;
		
	}

	public void cluster() {
		Log.i("Cluster", "Running");
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
		map.invalidate();

		// now you can see items clustered on the map.
		// zoom in/out to see how icons change.
	}

	public void BrowsingOn(){
		seekBarDay.setVisibility(View.VISIBLE);
		seekBarHour.setVisibility(View.VISIBLE);
		readingDay.setVisibility(View.VISIBLE);
		readingHour.setVisibility(View.VISIBLE);
	}

	public void BrowsingOff(){
		seekBarDay.setVisibility(View.GONE);
		seekBarHour.setVisibility(View.GONE);
		readingDay.setVisibility(View.GONE);
		readingHour.setVisibility(View.GONE);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case R.id.action_bar_pois: {
			if(!browse){
				BrowsingOn();
				System.out.println("BROWSE ON BITCHEEEEEEEEEEEEEEEEEEEES");
				browse = true;
			}
			else{
				BrowsingOff();
				browse = false;
				System.out.println("BROWSE OFF BITCHEEEEEEEEEEEEEEEEEEEES");
			}
			return true;
		}
		case R.id.action_bar_search:
			// startActivity(new Intent(this, InfoTabActivity.class));
			// startActivity(new Intent(this, MapPinMapActivity.class));
			onShowGrid(item.getItemView());
			return true;

		default:
			return super.onHandleActionBarItemClick(item, position);
		}

	}

	public void onShowGrid(View v) {
		mGrid.show(v);
	}

	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			Toast.makeText(MainScreen.this, "Item " + position + " clicked",
					Toast.LENGTH_SHORT).show();

			if (position == 0) {

			}
		}
	};

	private void prepareQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.gd_action_bar_compose, R.string.gd_compose));
		/*
		 * mGrid.addQuickAction(new MyQuickAction(this,
		 * R.drawable.gd_action_bar_export, R.string.gd_export));
		 * mGrid.addQuickAction(new MyQuickAction(this,
		 * R.drawable.gd_action_bar_share, R.string.gd_share));
		 * mGrid.addQuickAction(new MyQuickAction(this,
		 * R.drawable.gd_action_bar_search, R.string.gd_search));
		 * mGrid.addQuickAction(new MyQuickAction(this,
		 * R.drawable.gd_action_bar_edit, R.string.gd_edit));
		 * mGrid.addQuickAction(new MyQuickAction(this,
		 * R.drawable.gd_action_bar_locate, R.string.gd_locate));
		 */
		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	private static class MyQuickAction extends QuickAction {

		private static final ColorFilter BLACK_CF = new LightingColorFilter(
				Color.BLACK, Color.BLACK);

		public MyQuickAction(Context ctx, int drawableId, int titleId) {
			super(ctx, buildDrawable(ctx, drawableId), titleId);
		}

		private static Drawable buildDrawable(Context ctx, int drawableId) {
			Drawable d = ctx.getResources().getDrawable(drawableId);
			d.setColorFilter(BLACK_CF);
			return d;
		}

	}
	
	public String GetDayString(int dia){
		String print=null;
		switch(dia){
			case 0: print = "Domingo";break;
			case 1: print = "Segunda Feira";break;
			case 2: print = "Ter�a Feira";break;
			case 3: print = "Quarta Feira";break;
			case 4: print = "Quinta Feira";break;
			case 5: print = "Sexta";break;
			case 6: print = "S�bado";break;
			default:break;
		}
		
		return print;
	}
	
	public String GetHourString(int hora){
		String print=null;
		switch(hora){
			case 0: print = "00:00";break;
			case 1: print = "01:00";break;
			case 2: print = "02:00";break;
			case 3: print = "03:00";break;
			case 4: print = "04:00";break;
			case 5: print = "05:00";break;
			case 6: print = "06:00";break;
			case 7: print = "07:00";break;
			case 8: print = "08:00";break;
			case 9: print = "09:00";break;
			case 10: print = "10:00";break;
			case 11: print = "11:00";break;
			case 12: print = "12:00";break;
			case 13: print = "13:00";break;
			case 14: print = "14:00";break;
			case 15: print = "15:00";break;
			case 16: print = "16:00";break;
			case 17: print = "17:00";break;
			case 18: print = "18:00";break;
			case 19: print = "19:00";break;
			case 20: print = "20:00";break;
			case 21: print = "21:00";break;
			case 22: print = "22:00";break;
			case 23: print = "23:00";break;
			default:break;
		}
		
		return print;
	}

	public class MySeekBarListener implements OnSeekBarChangeListener {

		TextView legenda;

		public MySeekBarListener(TextView legend){
			this.legenda = legend;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			//if(seekBar.getId() == )
			String print=null;

			if(seekBar.getMax() == 6){
				print = GetDayString(seekBar.getProgress());
			}
			else{
				print = GetHourString(seekBar.getProgress());
			}
			//Log.i("DEBUG",Integer.toString(seekBar.getId()));	
			legenda.setText(print);
		}

	}

	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {

			// loc.getLatitude();
			// loc.getLongitude();
			//
			// String Text = "My current location is: " + "Latitud = "
			// + loc.getLatitude() + "Longitud = " + loc.getLongitude();
			//
			// Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
			// .show();
		}

		@Override
		public void onProviderDisabled(String provider) {

			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onProviderEnabled(String provider) {

			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	}/* End of Class MyLocationListener */

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

	// Layer on top of the map
	class Touchy extends Overlay {

		public boolean onTouchEvent(MotionEvent e, MapView m) {

			if (map.getZoomLevel() > 15) {
				System.out.println("ZOOM: " + map.getZoomLevel()
						+ " old zoom: " + oldZoomLevel);
				if (oldZoomLevel != map.getZoomLevel()) {

					if(!map.getOverlays().contains(itemizedOverlay)) {
						System.out.println("DESENHA PINS!");
						pins();
					}
				}
			} else {
				System.out.println("old zoom: " + oldZoomLevel);
				//if (oldZoomLevel > 15) {

				if(map.getOverlays().contains(itemizedOverlay)) {
					System.out.println("VAI REMOVER PINS!!!");
					int pos = map.getOverlays().indexOf(itemizedOverlay);
					System.out.println("A POSI�AO DO ITEMIZED OVERLAY �: " + pos);
					map.getOverlays().remove(pos);
					itemizedOverlay.removeOverlay();

					map.invalidate();

				}

				//}
			}

			oldZoomLevel = map.getZoomLevel();

			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();

				touchedPoint = map.getProjection().fromPixels(x, y);

			}

			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}

			return false;
		}
	}

}