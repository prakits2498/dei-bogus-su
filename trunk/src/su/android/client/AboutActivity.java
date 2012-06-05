/*
 * Copyright (C) 2011 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        
        TextView afluenciaTv = (TextView) findViewById(R.id.afluencia_text_ID);
        afluenciaTv.setText(poiExtras.get("poiAffluence")+"%");
        afluenciaTv.setTypeface(fontBold);
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
    }
    
}
