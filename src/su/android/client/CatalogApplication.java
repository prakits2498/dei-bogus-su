/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
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

import greendroid.app.GDApplication;
import android.content.Intent;
import android.net.Uri;

public class CatalogApplication extends GDApplication {

    @Override
    public Class<?> getHomeActivityClass() {
        return MainScreen.class;
    }
    
    @Override
    public Intent getMainApplicationIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.uc.pt/sasuc/ServicosApoioEstudantes/Alimentacao/UnidadesAlimentares"));
    }

}
