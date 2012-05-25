package su.android.client;

import greendroid.app.GDActivity;
import greendroid.widget.PageIndicator;
import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoPOIActivity extends GDActivity {

	private final Handler mHandler = new Handler();
	
	private static final int PAGE_COUNT = 7;
    private static final int PAGE_MAX_INDEX = PAGE_COUNT - 1;

    private PageIndicator mPageIndicatorNext;
    private PageIndicator mPageIndicatorPrev;
    private PageIndicator mPageIndicatorOther;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.poi_info);

		/*addActionBarItem(
				getActionBar().newActionBarItem(NormalActionBarItem.class)
						.setDrawable(
								new ActionBarDrawable(this,
										R.drawable.ic_search_light)),
				R.id.action_bar_view_info);*/

//		List<Item> items = new ArrayList<Item>();
//
//		items.add(new SeparatorItem("IceCream")); //POI NAME
//		items.add(new ThumbnailItem("Big IceCream", "aka Pyramid",
//				R.drawable.ic_map_64, "http://images4.fanpop.com/image/photos/24000000/ice-cream-Yummy-ice-cream-24070264-360-380.jpg"));
//		items.add(new DescriptionItem(
//				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));

		/*items.add(new SeparatorItem("Class 2"));
		items.add(new DrawableItem("Trikes", R.drawable.ic_pin));
		items.add(new DescriptionItem(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));

		items.add(new SeparatorItem("Class 3"));
		items.add(new ThumbnailItem("Multi-axis", "Looks like a tiny plane",
				R.drawable.ic_search_light));
		items.add(new DescriptionItem(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));

		items.add(new SeparatorItem("Class 4"));
		items.add(new ThumbnailItem("Auto-gyro", "A scary helicopter",
				R.drawable.ic_launcher));
		items.add(new DescriptionItem(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));

		items.add(new SeparatorItem("Class 5"));
		items.add(new DrawableItem("Hot air baloon", R.drawable.ic_map_64));
		items.add(new DescriptionItem(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));

		final Item item1 = new SeparatorItem("Class 6");
		final Item item2 = new TextItem("Airbus/Boeing planes");
		final Item item3 = new DescriptionItem(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in.");
		items.add(item1);
		items.add(item2);
		items.add(item3);

		final ProgressItem progressItem = new ProgressItem(
				"Removing intruders", true);
		items.add(progressItem);

		final ItemAdapter adapter = new ItemAdapter(this, items);
		setListAdapter(adapter);*/

		/*mHandler.postDelayed(new Runnable() {
			public void run() {
				adapter.remove(item1);
				adapter.remove(item2);
				adapter.remove(item3);
				adapter.remove(progressItem);
				adapter.insert(
						new ThumbnailItem("Ultralight aviation",
								"List of French 'ULM' classes",
								R.drawable.ic_map_64), 0);
				adapter.notifyDataSetChanged();
			}
		}, 8000);*/
		
		setActionBarContentView(R.layout.paged_view);
		
        TextView tv = (TextView) findViewById(R.id.page_view_title);
        tv.setText("cenas");

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
        public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
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
                convertView = getLayoutInflater().inflate(R.layout.paged_view_item, parent, false);
            }

            ((TextView) convertView).setText(Integer.toString(position));

            return convertView;
        }

    }

}
