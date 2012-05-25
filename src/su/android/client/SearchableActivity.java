package su.android.client;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SearchableActivity extends ListActivity
{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
	    // Get the intent, verify the action and get the query
//	    Intent intent = getIntent();
//	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//	      String query = intent.getStringExtra(SearchManager.QUERY);
//	      
////	      doMySearch(query);
//	    }
	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final ProgressDialog dialog = ProgressDialog.show(SearchableActivity.this, "", "Loading.");
		final String item = (String) getListAdapter().getItem(position);	
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	 dialog.dismiss();
	        	 Toast.makeText(SearchableActivity.this, item + " selected", Toast.LENGTH_SHORT).show();
	         } 
	    }, 2000); 		
	}
}
