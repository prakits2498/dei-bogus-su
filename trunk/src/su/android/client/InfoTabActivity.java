package su.android.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import greendroid.app.ActionBarActivity;
import greendroid.app.GDTabActivity;

public class InfoTabActivity extends GDTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        
        String poiID = b.getString("poiID");
        String poiName = b.getString("poiName");
        System.out.println("POI ID: "+poiID+" POI Name: "+poiName);
        
        final String aboutText =  getString(R.string.app_name);
        final Intent aboutIntent = new Intent(this, AboutActivity.class);
        aboutIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(aboutText, aboutText, aboutIntent);

        final String productsText =  getString(R.string.app_name);
        final Intent productsIntent = new Intent(this, ProductsActivity.class);
        
        productsIntent.putExtra("poiID", poiID);
        productsIntent.putExtra("poiName", poiName);
        productsIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(productsText, productsText, productsIntent);
        
        TextView poiNameTv = (TextView) findViewById(R.id.poiname);
        poiNameTv.setText(poiName);
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
