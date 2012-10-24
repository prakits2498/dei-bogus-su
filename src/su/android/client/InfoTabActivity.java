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

/**
 * Activity dos detalhes do POI -- parte de cima, antes das Tabs
 * @author bfurtado
 *
 */
public class InfoTabActivity extends GDTabActivity {

	private HashMap<String, String> poiExtras = new HashMap<String, String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getExtrasPOI();
        
        // ******* MENU LUNCH TAB *******
        final String menuLunchText =  getString(R.string.menuLunch);
        Intent menuLunchIntent = new Intent(this, MenuLunchActivity.class);
        
        menuLunchIntent = putExtraPOIDetails(menuLunchIntent);
        
        menuLunchIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(menuLunchText, menuLunchText, menuLunchIntent); //O style do titulo da tab esta em gd_styles.xml em TabIndicator

        // ******* MENU DINNER TAB *******
        final String menuDinnerText = getString(R.string.menuDinner);
        Intent menuDinnerIntent = new Intent(this, MenuDinnerActivity.class);
        
        menuDinnerIntent = putExtraPOIDetails(menuDinnerIntent);
        
        menuDinnerIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(menuDinnerText, menuDinnerText, menuDinnerIntent);
        
        // ******* DADOS DA CANTINA *******
        TextView poiNameTv = (TextView) findViewById(R.id.poiNameID);
        poiNameTv.setText(poiExtras.get("poiName"));
        
        TextView poiAddressTv = (TextView) findViewById(R.id.poiAddressID);
        poiAddressTv.setText(poiExtras.get("poiAddress"));
        
        ImageView imageV = (ImageView) findViewById(R.id.poiCatIconID);
		ImageLoader imageLoader = new ImageLoader(imageV.getContext());
		imageLoader.DisplayImage(poiExtras.get("poiCatIcon"), imageV);
		
		//System.out.println(poiExtras.get("poiCatIcon"));
		
    }
    
    //Extras vindos do PoiMarkersOverlay
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
        
        
        int x = (int) Math.round(b.getDouble("poiAffluence"));
        if(x<10) 
        	poiExtras.put("poiAffluence", " "+Integer.toString(x));
        else
        	poiExtras.put("poiAffluence", Integer.toString(x));

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
