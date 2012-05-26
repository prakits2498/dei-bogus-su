package su.android.client;

import java.util.List;

import su.android.model.POI;
import su.android.server.connection.ServerConnection;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
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
//	       super.onCreate(savedInstanceState);
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
	    {
	    	String query = intent.getStringExtra(SearchManager.QUERY);
	    	ServerConnection conn = new ServerConnection();
		    List<POI> list = conn.searchPOIS(query);
		    ArrayAdapter<POI> adapter = new ArrayAdapter<POI>(this,
					android.R.layout.simple_list_item_1, list);
			setListAdapter(adapter);
	    }
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
