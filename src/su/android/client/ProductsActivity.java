package su.android.client;

import greendroid.app.GDActivity;
import greendroid.widget.PageIndicator;
import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;
import su.android.model.POIDetails;
import su.android.model.Products;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;

public class ProductsActivity extends GDActivity {

	private final Handler mHandler = new Handler();

	private static int PAGE_COUNT = 7;
	private static int PAGE_MAX_INDEX = PAGE_COUNT - 1;

	private PageIndicator mPageIndicatorNext;
	private PageIndicator mPageIndicatorPrev;
	private PageIndicator mPageIndicatorOther;

	private String poiName;

	ServerConnection conn;

	public POIDetails poiDetails;

	public ProductsActivity() {
		conn = ServerConnection.getInstance();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setActionBarContentView(R.layout.paged_view);
		
		loadPOIDetails();

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

	private void loadPOIDetails() {
		Bundle b = new Bundle();
		b = getIntent().getExtras();

		String poiID = b.getString("poiID");
		poiName = b.getString("poiName");

		poiDetails = conn.getPOIDetails(poiID);

		if (poiDetails != null) {
			if (!poiDetails.getProductList().isEmpty()) {
				PAGE_COUNT = poiDetails.getProductList().size();
				PAGE_MAX_INDEX = PAGE_COUNT - 1;
			}
		} else {
			PAGE_COUNT = 1;
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
						R.layout.product_info, parent, false);
			}

			viewProductDetails(position, convertView);

			return convertView;
		}

		public void viewProductDetails(int position, View convertView) {
			Products product = null;

			if (poiDetails != null) {
				if (!poiDetails.getProductList().isEmpty()) {
					product = poiDetails.getProductList().get(position);
				}
			}

			if (product != null) {
				TextView tv = (TextView) convertView.findViewById(R.id.productNameID);
				tv.setText(product.getName());

				TextView priceTv = (TextView) convertView.findViewById(R.id.productPriceID);
				priceTv.setText(product.getPrice());

				ImageView imageV = (ImageView) convertView.findViewById(R.id.productImageID);
				ImageLoader imageLoader = new ImageLoader(imageV.getContext());
				imageLoader.DisplayImage(product.getImage(), imageV);

				TextView descriptionTv = (TextView) convertView.findViewById(R.id.productDescriptionID);
				descriptionTv.setText(product.getDescription());
			}
		}

	}

}
