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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Static utilities related to packages
 */
public interface PackageUtils {

    /**
     * Returns a list of packages that can open an intent, removing this app from the list
     *
     * @param baseIntent intent that the packages will be able to open
     * @param cntx       base context (and the app that will be filtered)
     * @return the list of other packages
     */
    static List<String> getOtherPackages(Intent baseIntent, Context cntx) {
        List<String> packages = new ArrayList<>();

        // get all packages
        PackageManager packageManager = cntx.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(baseIntent, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PackageManager.MATCH_ALL : 0);

        // filter the current app
        for (ResolveInfo resolveInfo : resolveInfos) {
            if (!resolveInfo.activityInfo.packageName.equals(cntx.getPackageName())) {
                packages.add(resolveInfo.activityInfo.packageName);
            }
        }

        return packages;
    }

    /**
     * Returns the app name of a package
     *
     * @param pack packagename to search
     * @param cntx base context
     * @return that app name
     */
    static String getPackageName(String pack, Context cntx) {
        final PackageManager pm = cntx.getPackageManager();
        try {
            // try getting the app label
            return pm.getApplicationLabel(pm.getApplicationInfo(pack, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // can't get the label
            e.printStackTrace();
            return cntx.getString(android.R.string.unknownName);
        }
    }

    /**
     * Wrapper for {@link Context#startActivity(Intent)} to catch thrown exceptions and show a toast instead
     */
    static void startActivity(Intent intent, int toastError, Context cntx) {
        try {
            cntx.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(cntx, toastError, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Wrapper for {@link Activity#startActivityForResult(Intent, int)} to catch thrown exceptions and show a toast instead
     */
    static void startActivityForResult(Intent intent, int requestCode, int toastError, Activity cntx) {
        try {
            cntx.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Toast.makeText(cntx, toastError, Toast.LENGTH_SHORT).show();
        }
    }
}
