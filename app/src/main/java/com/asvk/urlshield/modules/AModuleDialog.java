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

package com.asvk.urlshield.modules;

import android.app.Activity;

import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.fragments.Fragment;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.methods.JavaUtils;

import java.util.Map;

/**
 * Base class for a module's dialog fragment.
 */
public abstract class AModuleDialog implements Fragment {

    // ------------------- private data -------------------

    private final MainDialog dialog;

    // ------------------- initialization -------------------

    public AModuleDialog(MainDialog dialog) {
        this.dialog = dialog;
    }

    // ------------------- abstract functions -------------------

    /**
     * Prepare a new url. This will always be called for each new module.
     */
    public void onPrepareUrl(UrlData urlData) {
    }

    /**
     * Analyze and optionally modify an url. This may not be called for new modules.
     * To modify the url call the setNewUrl callback. It will return true iff you can stop processing changes (false if you need to continue).
     * > if(setNewUrl.apply(new UrlData(""))) return;
     */
    public void onModifyUrl(UrlData urlData, JavaUtils.Function<UrlData, Boolean> setNewUrl) {
    }

    /**
     * Update UI and all needed for this final url. This will only be called for the final shown url.
     */
    public void onDisplayUrl(UrlData urlData) {
    }

    /**
     * Last call for any update a module may need (like the drawer module needing to know how many modules are visible).
     */
    public void onFinishUrl(UrlData urlData) {
    }

    // ------------------- utilities -------------------

    /**
     * @return this activity context
     */
    public final Activity getActivity() {
        return dialog;
    }

    /**
     * @return the current url
     */
    protected final String getUrl() {
        return dialog.getUrl();
    }

    /**
     * Changes the current url. (no extra data)
     *
     * @param url new url
     */
    protected final void setUrl(String url) {
        setUrl(new UrlData(url));
    }

    /**
     * Changes the current url.
     *
     * @param urlData new url and data
     */
    protected final void setUrl(UrlData urlData) {
        urlData.trigger = this;
        dialog.onNewUrl(urlData);
    }

    /**
     * saves global data
     */
    public void putData(String key, String value) {
        dialog.globalData.put(key, value);
    }

    /**
     * gets global data
     */
    public String getData(String key) {
        return dialog.globalData.get(key);
    }

    /**
     * returns the global data map, for advanced uses
     */
    public Map<String, String> getGlobalData() {
        return dialog.globalData;
    }

    /**
     * Changes this module visibility
     */
    protected final void setVisibility(boolean visible) {
        dialog.setModuleVisibility(this, visible);
    }

}
