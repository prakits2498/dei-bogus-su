
package su.android.client;

import greendroid.app.GDActivity;

import java.util.HashMap;

import su.android.model.POIDetails;
import su.android.model.Promotion;
import su.android.server.connection.ServerConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;

public class AboutActivity extends GDActivity {
	
	private ServerConnection conn;
	private POIDetails poiDetails;
	private Promotion promotion;
	
	private HashMap<String, String> poiExtras = new HashMap<String, String>();
	
	public AboutActivity() {
		conn = ServerConnection.getInstance();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarContentView(R.layout.about);
        
        getExtrasPOI();

        loadPOIDetails();

        if(poiDetails != null) {
        	final TextView aboutText = (TextView) findViewById(R.id.poi_promotionID);
        	aboutText.setText(promotion.getDescription());
        	//aboutText.setMovementMethod(LinkMovementMethod.getInstance());
        	
        	ImageView imageV = (ImageView) findViewById(R.id.poi_photoID);
    		ImageLoader imageLoader = new ImageLoader(imageV.getContext());

    		if(poiExtras.get("poiPhoto01") != null) {
    			imageLoader.DisplayImage(poiExtras.get("poiPhoto01"), imageV);
    		} else {
    			imageV.setImageResource(R.drawable.image_default);
    		}

        }
        
        //Typeface fontLight = Typeface.createFromAsset(getAssets(),"fonts/Helvetica Neue.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(),"fonts/Helvetica Neue Bold.ttf");
        
        TextView afluenciaTextTv = (TextView) findViewById(R.id.afluencia_text_ID);
        afluenciaTextTv.setTypeface(fontBold);
        
        TextView afluenciaTv = (TextView) findViewById(R.id.afluenciaID);
        afluenciaTv.setText(poiExtras.get("poiAffluence")+"%");
        
        
    }
    
    private void loadPOIDetails() {
		poiDetails = conn.getPOIDetails(poiExtras.get("poiID"));
		
		if(poiDetails != null) {
			//TODO isto só está a ir buscar uma promotion, ver se o indice zero é a mais recente
			promotion = poiDetails.getPromotionList().get(0);
		}
		
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
        poiExtras.put("poiAffluence", b.getString("poiAffluence"));
    }
    
}
