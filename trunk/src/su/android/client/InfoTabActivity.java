package su.android.client;

import java.util.HashMap;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import greendroid.app.ActionBarActivity;
import greendroid.app.GDTabActivity;

public class InfoTabActivity extends GDTabActivity {

	private HashMap<String, String> poiExtras = new HashMap<String, String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getExtrasPOI();

        final String aboutText =  getString(R.string.app_name);
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        
        aboutIntent = putExtraPOIDetails(aboutIntent);
        
        aboutIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(aboutText, aboutText, aboutIntent);

        final String productsText =  getString(R.string.app_name);
        Intent productsIntent = new Intent(this, ProductsActivity.class);
        
        productsIntent = putExtraPOIDetails(productsIntent);
        
        productsIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(productsText, productsText, productsIntent);
        
        TextView poiNameTv = (TextView) findViewById(R.id.poiname);
        poiNameTv.setText(poiExtras.get("poiName"));
        
        TextView poiAddressTv = (TextView) findViewById(R.id.poiAddressID);
        poiAddressTv.setText(poiExtras.get("poiAddress"));
        
        ImageView imageV = (ImageView) findViewById(R.id.poiIconCategory);
		ImageLoader imageLoader = new ImageLoader(imageV.getContext());
		imageLoader.DisplayImage(poiExtras.get("poiCatIcon"), imageV);
		
    }
    
    private void getExtrasPOI() {
    	Bundle b = new Bundle();
        b = getIntent().getExtras();
        
        poiExtras.put("poiID", b.getString("poiID"));
        poiExtras.put("poiName", b.getString("poiName"));
        poiExtras.put("poiAddress", b.getString("poiAddress"));
        poiExtras.put("poiCategory", b.getString("poiCategory"));
        poiExtras.put("poiSubCategory", b.getString("poiSubCategory"));
        poiExtras.put("poiCatIcon", b.getString("poiCatIcon"));
        poiExtras.put("poiPhoto01", b.getString("poiPhoto01"));
    }
    
    private Intent putExtraPOIDetails(Intent intent) {
    	for(String key : poiExtras.keySet()) {
    		intent.putExtra(key, poiExtras.get(key));
    	}
    	
    	return intent;
    }
    
    @Override
    public int createLayout() {
        return R.layout.info;
    }

    public void onAppUrlClicked(View v) {
        final Uri appUri = Uri.parse(getString(R.string.app_name));
        startActivity(new Intent(Intent.ACTION_VIEW, appUri));
    }
}
