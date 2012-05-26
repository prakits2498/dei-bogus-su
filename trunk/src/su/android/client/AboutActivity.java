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
import su.android.model.POIDetails;
import su.android.model.Promotions;
import su.android.server.connection.ServerConnection;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends GDActivity {
	
	private ServerConnection conn;
	private POIDetails poiDetails;
	private Promotions promotion;
	
	public AboutActivity() {
		conn = ServerConnection.getInstance();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarContentView(R.layout.about);
        
        loadPOIDetails();
        
        if(poiDetails != null) {
        	final TextView aboutText = (TextView) findViewById(R.id.about);
        	aboutText.setText(promotion.getDescription());
        	//aboutText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    
    private void loadPOIDetails() {
		Bundle b = new Bundle();
		b = getIntent().getExtras();

		String poiID = b.getString("poiID");

		poiDetails = conn.getPOIDetails(poiID);
		
		if(poiDetails != null) {
			//TODO isto só está a ir buscar uma promotion, ver se o indice zero é a mais recente
			promotion = poiDetails.getPromotionList().get(0); 
		}
		
	}
    
}
