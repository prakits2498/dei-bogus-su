package su.android.client;

import greendroid.app.GDActivity;
import greendroid.widget.PageIndicator;
import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;
import su.android.model.Meal;
import su.android.model.MenuDetails;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

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

		String poiID = b.getString("poiID");

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
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.menu_details, parent, false);
			}

			viewMenuDetails(position, convertView);

			return convertView;
		}

		public void viewMenuDetails(int position, View convertView) {
			Meal lunch = null;
			//Meal dinner = null;

			boolean same = false;

			if (menuDetails != null) {
				if (!menuDetails.getMenuDinner().isEmpty()) {
					lunch = menuDetails.getMenuLunch().get(position);
					//dinner = menuDetails.getMenuDinner().get(position);
				} else {
					// CANTINAS COM MENU IGUAL TODOS OS DIAS --- BAGUETES E PIZZAS
					lunch = menuDetails.getMenuLunch().get(0);
					//dinner = lunch;
					same = true;
				}
			}

			/*ImageView imageV = (ImageView) convertView.findViewById(R.id.product_photoID);
			ImageLoader imageLoader = new ImageLoader(imageV.getContext());
			imageLoader.DisplayImage(product.getImageUrl(), imageV);*/

			if (lunch != null) {

				TextView dayOfWeekTv = (TextView) convertView.findViewById(R.id.dayOfWeek_text_ID);
				dayOfWeekTv.setText(getDayOfWeek(position));

				if(same) {
					TextView lunchTv = (TextView) convertView.findViewById(R.id.sopa_ID);
					lunchTv.setText(lunch.getCarne());
				} else {
					TextView sopaTv = (TextView) convertView.findViewById(R.id.sopa_ID);
					TextView sopaKcalTv = (TextView) convertView.findViewById(R.id.sopaKcal);
					String[] aux = lunch.getSopa().replace("|", "#").split("#");
					sopaTv.setText(aux[0]);
					sopaKcalTv.setText(aux[1] +" Kcal");

					TextView carneTv = (TextView) convertView.findViewById(R.id.carne_ID);
					TextView carneKcalTv = (TextView) convertView.findViewById(R.id.carneKcal);
					aux = lunch.getCarne().replace("|", "#").split("#");
					carneTv.setText(aux[0]);
					carneKcalTv.setText(aux[1]+" Kcal");

					TextView peixeTv = (TextView) convertView.findViewById(R.id.peixe_ID);
					TextView peixeKcalTv = (TextView) convertView.findViewById(R.id.peixeKcal);
					aux = lunch.getPeixe().replace("|", "#").split("#");
					peixeTv.setText(aux[0]);
					peixeKcalTv.setText(aux[1]+" Kcal");
				}

				TextView priceTv = (TextView) convertView.findViewById(R.id.price_ID);
				priceTv.setText(lunch.getPrice()+" €");

			}
		}

		private String getDayOfWeek(int position) {
			switch(position) {
			case 0:
				return "Segunda";
			case 1:
				return "Terça";
			case 2: 
				return "Quarta";
			case 3:
				return "Quinta";
			case 4:
				return "Sexta";
			case 5:
				return "Sábado";
			case 6:
				return "Domingo";
			}
			
			return "";
		}
	}



}
