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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import greendroid.app.ActionBarActivity;
import greendroid.app.GDTabActivity;

public class InfoTabActivity extends GDTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final String aboutText =  getString(R.string.app_name);
        final Intent aboutIntent = new Intent(this, AboutActivity.class);
        aboutIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(aboutText, aboutText, aboutIntent);

        final String licenseText =  getString(R.string.app_name);
        final Intent licenseIntent = new Intent(this, InfoPOIActivity.class);
        licenseIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
        addTab(licenseText, licenseText, licenseIntent);
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
