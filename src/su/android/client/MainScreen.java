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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
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

	/*TextView readingDay;
	TextView readingHour;
	SeekBar seekBarDay;
	SeekBar seekBarHour;
	boolean browse=false;*/

	// marker icons
	private List<MarkerBitmap> markerIconBmps_ = new ArrayList<MarkerBitmap>();

	int x, y;
	GeoPoint touchedPoint;

	Drawable d;
	List<Overlay> overlayList;

	ServerConnection conn;

	private int oldZoomLevel = -1;

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

		addActionBarItem(Type.Eye, R.id.action_bar_slider); //TODO icon calendario com um relogio
		addActionBarItem(Type.Compass, R.id.action_bar_category);
		addActionBarItem(
				getActionBar().newActionBarItem(NormalActionBarItem.class)
				.setDrawable(
						new ActionBarDrawable(this,
								R.drawable.ic_search_light)),
								R.id.action_bar_search);

		map = (CustomMapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);


		//SEEKBAR
		/*readingDay = (TextView) findViewById(R.id.readingDay);
		readingHour = (TextView) findViewById(R.id.readingHour);

		seekBarDay = (SeekBar)findViewById(R.id.seekbarDay);
		seekBarDay.setMax(6);
		seekBarDay.setOnSeekBarChangeListener(new MySeekBarListener(readingDay));

		seekBarHour = (SeekBar)findViewById(R.id.seekbarHour);
		seekBarHour.setMax(23);
		seekBarHour.setOnSeekBarChangeListener(new MySeekBarListener(readingHour));

		BrowsingOff();*/


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
				pins();
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

		//overlayList.add(itemizedOverlay);
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
				"Thursday", "Friday", "Saturday" };
		return days[index];
	}

	public AppContext setupContext() {
		AppContext appContext = new AppContext();
		Criteria hdCrit = new Criteria();
		hdCrit.setAccuracy(Criteria.ACCURACY_FINE);
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
		/*System.out.println(weekDayIndex);
		seekBarDay.setProgress(weekDayIndex);
		readingDay.setText(GetDayString(weekDayIndex));
		seekBarHour.setProgress(hour);
		readingHour.setText(GetHourString(hour));*/

		return appContext;

	}

	public void cluster() {
		Log.i("Cluster", "Running");
		clusterer.removeCluster();
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

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) 
		{
			case R.id.action_bar_slider: 
				createBrowseDialog();
				return true;		
			case R.id.action_bar_search:
				onSearchRequested(); 
				return true;		
			case R.id.action_bar_category: 
				onShowGrid(item.getItemView()); 
				return true;		
			default:
				return super.onHandleActionBarItemClick(item, position);
		}
		
	}

	private void createBrowseDialog()
	{
		final String [] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


		LayoutInflater inflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_day_hour, null);

		final TextView dayValue = (TextView) layout.findViewById(R.id.dayLabelValue);
		final TextView hourValue = (TextView) layout.findViewById(R.id.hourLabelValue);

		AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this)
		.setView(layout);
		final SeekBar sb = (SeekBar)layout.findViewById(R.id.dayBar);
		final SeekBar sbHour = (SeekBar)layout.findViewById(R.id.hourBar);
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int weekDayIndex = today.weekDay;
		int hour = today.hour;
		sb.setProgress(weekDayIndex);
		dayValue.setText(days[weekDayIndex]);
		sbHour.setProgress(hour);
		hourValue.setText(hour+"H");
		builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String day = getDayOfWeek(sb.getProgress());
				int hour = sbHour.getProgress();
				AppContext appCtxt = MainScreen.this.setupContext();
				poiList = conn.getPOIRecommendations(appCtxt.getLat(), appCtxt.getLng(), day, hour, 0.5, 100);		
				cluster();
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.setTitle("Browse Mode");
		alertDialog.show();

		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				//Do something here with new value
				Log.d("Seek Day bar", "Value "+progress);
				dayValue.setText(days[progress]);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

		sbHour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				//Do something here with new value
				hourValue.setText(String.valueOf(progress));

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	public void onShowGrid(View v) {
		mGrid.show(v);
	}

	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			Toast.makeText(MainScreen.this, "Item " + position + " clicked",
					Toast.LENGTH_SHORT).show();

			if (position == 0) {
				//TODO pesquisa por categoria
			}
			else if(position == 1){
				//TODO pesquisa por categoria
			}
		}
	};

	private void prepareQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.gd_action_bar_compose, R.string.procuraCategoria));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.gd_action_bar_compose, R.string.percentagemCheckins));
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
				if (oldZoomLevel != map.getZoomLevel()) {

					if(!map.getOverlays().contains(itemizedOverlay)) {
						//pins();
						map.getOverlays().add(itemizedOverlay);
					}
				}
			} else {
				if(map.getOverlays().contains(itemizedOverlay)) {
					int pos = map.getOverlays().indexOf(itemizedOverlay);
					map.getOverlays().remove(pos);
					//itemizedOverlay.removeOverlay();

					map.invalidate();

				}
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

			//if (stop - start > 1500) { // 1.5 seconds

			//AQUI DENTRO ERA ONDE ESTAVA ORIGINALMENTE OS SLIDEBARS DO ALVARO

			//}

			return false;
		}
	}

}