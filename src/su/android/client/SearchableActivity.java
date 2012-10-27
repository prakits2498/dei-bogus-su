package su.android.client;

import greendroid.app.GDListActivity;
import greendroid.widget.ItemAdapter;
import greendroid.widget.item.Item;
import greendroid.widget.item.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

import su.android.model.POI;
import su.android.server.connection.ServerConnection;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

public class SearchableActivity extends GDListActivity
{
	private List<POI> pois = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			ServerConnection conn = new ServerConnection();
			pois = conn.getPOIRecommendations(getIntent().getExtras().getString("dayOfWeek"), getIntent().getExtras().getInt("hour"), 1);

			List<Item> items = new ArrayList<Item>();
			for(POI poi: pois)
			{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_default));
			}
			final ItemAdapter adapter = new ItemAdapter(this, items);
			setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(final ListView l, View v, final int position, long id) {
		final ProgressDialog dialog = ProgressDialog.show(SearchableActivity.this, "", "Loading.");
		final POI item = pois.get(position);
		Handler handler = new Handler(); 
		handler.postDelayed(new Runnable() { 
			public void run() { 
				dialog.dismiss();
				Intent myIntent = new Intent(SearchableActivity.this, InfoTabActivity.class);
				myIntent.putExtra("poiID", item.getId());
				myIntent.putExtra("poiName", item.getName());
				startActivity(myIntent);
			} 
		}, 2000); 		
	}
}
