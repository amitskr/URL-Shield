/*
 *
 *   Created by Amitskr & VnjVibhash on 2/21/24, 10:32 AM
 *   Copyright Ⓒ 2024. All rights reserved Ⓒ 2024 http://vivekajee.in/
 *   Last modified: 2/29/24, 1:59 PM
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *   except in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENS... Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *    either express or implied. See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.asvk.urlshield.utilities.methods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.widget.Toast;

import com.asvk.urlshield.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Static utilities related to urls
 */
public interface UrlUtils {

    /**
     * Returns an intent that will open the given url, with an optional package
     *
     * @param url         the url that will be opened
     * @param packageName the package that will be opened, null to let android choose
     * @return the converted intent
     */
    static Intent getViewIntent(String url, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (packageName != null) intent.setPackage(packageName);
        return intent;
    }

    /**
     * Opens an url removing this app from the chooser
     *
     * @param url  url to open
     * @param cntx base context
     */
    static void openUrlRemoveThis(String url, Context cntx) {

        // get packages that can open the url
        List<Intent> intents = new ArrayList<>();
        for (String pack : PackageUtils.getOtherPackages(getViewIntent(url, null), cntx)) {
            intents.add(getViewIntent(url, pack));
        }

        // check if none
        if (intents.isEmpty()) {
            Toast.makeText(cntx, R.string.toast_noBrowser, Toast.LENGTH_SHORT).show();
            return;
        }

        // create chooser
        Intent chooserIntent = Intent.createChooser(intents.remove(0), cntx.getString(R.string.title_choose));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[0]));

        // open
        PackageUtils.startActivity(chooserIntent, R.string.toast_noBrowser, cntx);
    }
}
