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
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.widget.ListView;

public class SearchableActivity extends GDListActivity
{
	private List<POI> pois = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ProgressDialog dialog = ProgressDialog.show(SearchableActivity.this, "", "Searching.");
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			String query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this.getApplicationContext(),
					SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			ServerConnection conn = new ServerConnection();
			//pois = conn.searchPOIS(query); //TODO
			dialog.dismiss();
			List<Item> items = new ArrayList<Item>();
			for(POI poi: pois)
			{
				String mainCat1 = getResources().getString(R.string.mainCat1);
				String mainCat2 = getResources().getString(R.string.mainCat2);
				String mainCat3 = getResources().getString(R.string.mainCat3);
				String mainCat4 = getResources().getString(R.string.mainCat4);
				String mainCat5 = getResources().getString(R.string.mainCat5);
				String mainCat6 = getResources().getString(R.string.mainCat6);
				if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(mainCat1))
				{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_arts_entertainment));
				}
				else if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(mainCat2))
				{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_food));
				}
				else if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(mainCat3))
				{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_coffee));
				}
				else if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(mainCat4))
				{	
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_nightlife));
				}
				else if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(mainCat5))
				{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_shops));
				}
				else if(poi.getCategory() != null && poi.getCategory().equalsIgnoreCase(mainCat6))
				{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_outdoors));
				}
				else
				{
					items.add(new ThumbnailItem(poi.getName(), poi.getAddress(),R.drawable.pin_default));
				}
			}
			final ItemAdapter adapter = new ItemAdapter(this, items);
			setListAdapter(adapter);
		}
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
