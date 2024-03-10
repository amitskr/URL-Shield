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

package com.asvk.urlshield.modules.companions;

import android.app.Activity;
import android.content.Context;

import com.asvk.urlshield.R;
import com.asvk.urlshield.utilities.generics.JsonCatalog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Catalog of hosts configuration. The whole hosts files are not included as ready-to-use copy like ClearURLs, because they are a lot bigger.
 * Instead, the configuration specifies the urls where to download them, and a parsed&optimized version is saved instead.
 * Manual update is required, they are too big and they should be updated in a background process
 */
public class HostsCatalog extends JsonCatalog {

    public HostsCatalog(Activity cntx) {
        super(cntx, "hosts", R.string.mHosts_editor);
    }

    @Override
    public JSONObject buildBuiltIn(Context cntx) throws JSONException {
        return new JSONObject()
                .put(cntx.getString(R.string.mHosts_malware), new JSONObject()
                        .put("color", "#80E91E63")
                        .put("file", "https://raw.githubusercontent.com/StevenBlack/hosts/master/hosts")
                )
                .put(cntx.getString(R.string.mHosts_fakenews), new JSONObject()
                        .put("color", "#803C1E1E")
                        .put("file", "https://raw.githubusercontent.com/StevenBlack/hosts/master/alternates/fakenews/hosts")
                        .put("replace", false)
                )
                .put(cntx.getString(R.string.mHosts_gambling), new JSONObject()
                        .put("color", "#80483360")
                        .put("file", "https://raw.githubusercontent.com/StevenBlack/hosts/master/alternates/gambling/hosts")
                        .put("replace", false)
                )
                .put(cntx.getString(R.string.mHosts_adult), new JSONObject()
                        .put("color", "#80603351")
                        .put("file", "https://raw.githubusercontent.com/StevenBlack/hosts/master/alternates/porn/hosts")
                        .put("replace", false)
                )
                .put(cntx.getString(R.string.vivekajee), new JSONObject()
                        .put("color", "#FCDABA")
                        .put("hosts", new JSONArray()
                                .put("triangularapps.blogspot.com")
                                .put("asvk.github.io"))
                        .put("enabled", "false")
                )
                ;
    }
}
