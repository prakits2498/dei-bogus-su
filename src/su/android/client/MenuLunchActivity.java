package su.android.client;

import greendroid.app.GDActivity;
import greendroid.widget.PageIndicator;
import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;

import java.util.ArrayList;

import su.android.model.Meal;
import su.android.model.MenuDetails;
import su.android.model.TestAdapter;
import su.android.model.TestObject;
import su.android.model.TestReceiver;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * Tab Menu -- Nos detalhes da Cantina
 * @author bfurtado
 *
 */
public class MenuLunchActivity extends GDActivity {

	private static int PAGE_COUNT = 7;
	private static int PAGE_MAX_INDEX = PAGE_COUNT - 1;

	private PageIndicator mPageIndicatorNext;
	private PageIndicator mPageIndicatorPrev;
	private PageIndicator mPageIndicatorOther;

	ServerConnection conn;

	public MenuDetails menuDetails;
	public String dayOfWeek = "";
	public String poiID = "";
	public String userID;
	public String idMeal;
	
	private ListDisplay<TestObject> selectableTable;
	
	public MenuLunchActivity() {
		conn = ServerConnection.getInstance();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.paged_view);
		
		loadMenuDetails();

		final PagedView pagedView = (PagedView) findViewById(R.id.paged_view);
		pagedView.setOnPageChangeListener(mOnPagedViewChangedListener);
		pagedView.setAdapter(new PhotoSwipeAdapter());

		mPageIndicatorNext = (PageIndicator) findViewById(R.id.page_indicator_next);
		mPageIndicatorNext.setDotCount(PAGE_MAX_INDEX);
		mPageIndicatorNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pagedView.smoothScrollToNext();
			}
		});

		mPageIndicatorPrev = (PageIndicator) findViewById(R.id.page_indicator_prev);
		mPageIndicatorPrev.setDotCount(PAGE_MAX_INDEX);
		mPageIndicatorPrev.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pagedView.smoothScrollToPrevious();
			}
		});

		mPageIndicatorOther = (PageIndicator) findViewById(R.id.page_indicator_other);
		mPageIndicatorOther.setDotCount(PAGE_COUNT);

		setActivePage(pagedView.getCurrentPage());
	}

	private void loadMenuDetails() {
		Bundle b = new Bundle();
		b = getIntent().getExtras();

		poiID = b.getString("poiID");
		userID = b.getString("userID");
		
		menuDetails = conn.getMenuDetails(poiID);

		if (menuDetails != null) {
			PAGE_COUNT = 7;
			PAGE_MAX_INDEX = PAGE_COUNT - 1;
		} 
	}

	private void setActivePage(int page) {
		mPageIndicatorOther.setActiveDot(page);
		mPageIndicatorNext.setActiveDot(PAGE_MAX_INDEX - page);
		mPageIndicatorPrev.setActiveDot(page);
	}

	private OnPagedViewChangeListener mOnPagedViewChangedListener = new OnPagedViewChangeListener() {

		@Override
		public void onStopTracking(PagedView pagedView) {
		}

		@Override
		public void onStartTracking(PagedView pagedView) {
		}

		@Override
		public void onPageChanged(PagedView pagedView, int previousPage,
				int newPage) {
			setActivePage(newPage);
		}
	};

	private class PhotoSwipeAdapter extends PagedAdapter {

		private boolean sameMenu = false;
		
		@Override
		public int getCount() {
			return PAGE_COUNT;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ArrayList<TestObject> menuList = new ArrayList<TestObject>();

			Meal dinner = getMeal(position);
			menuList = createMenuTable(dinner, menuList);
	    	
	    	if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.menu_details, parent, false);
			}
	    	
	    	selectableTable = new ListDisplay<TestObject> (menuList, convertView.getContext(), new TestAdapter(convertView.getContext()), position);

	    	TestReceiver t = new TestReceiver(convertView.getContext());
	    	selectableTable.setSelectionReceiver(t);

			convertView = selectableTable.getDisplay();

			return convertView;
		}
		
		public Meal getMeal(int position) {
			Meal lunch = null;
			
			if (menuDetails != null) {
				if (!menuDetails.getMenuDinner().isEmpty()) {
					lunch = menuDetails.getMenuLunch().get(position);
					idMeal = menuDetails.getMenuLunch().get(position).getId();
				} else {
					// CANTINAS COM MENU IGUAL TODOS OS DIAS --- BAGUETES E PIZZAS
					lunch = menuDetails.getMenuLunch().get(0);
					idMeal = menuDetails.getMenuLunch().get(0).getId();
					sameMenu = true;
				}
				
			}
			
			return lunch;
		}
		
		public ArrayList<TestObject> createMenuTable(Meal lunch, ArrayList<TestObject> menuList) {
			if(lunch != null) {
	    		if(sameMenu) {
	    			menuList.add(new TestObject(lunch.getCarne(), "", "carne", menuDetails, userID, idMeal, false));
	    		} else {
	    			String[] sopa = lunch.getSopa().replace("|", "#").split("#");
	    			String[] carne = lunch.getCarne().replace("|", "#").split("#");
	    			String[] peixe = lunch.getPeixe().replace("|", "#").split("#");
	    			menuList.add(new TestObject(sopa[0], sopa[1]+" kcal", "sopa", menuDetails, userID, idMeal, false));
	    			menuList.add(new TestObject(carne[0], carne[1]+" kcal", "carne", menuDetails, userID, idMeal, false));
	    			menuList.add(new TestObject(peixe[0], peixe[1]+" kcal", "peixe", menuDetails, userID, idMeal, false));
	    		}
	    	}
			
			return menuList;
		}
	}

}
