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

package com.asvk.urlshield.fragments;

import android.content.Intent;
import android.util.Log;

import com.asvk.urlshield.utilities.methods.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * An injector to allow registering for activityResult.
 * Alternative to using the deprecate android fragments (and without using the huge compatibility library)
 */
public class ActivityResultInjector {
    private static final int RESERVED = 123; // just in case, registered listeners will have requestCode >= this
    private final List<Listener> listeners = new ArrayList<>();

    /* ------------------- client use ------------------- */

    public interface Listener {
        /**
         * Called when the event fires for a particular registrar
         */
        void onActivityResult(int resultCode, Intent data);

    }

    /**
     * Call this to register an onActivityResult listener. Returns the requestCode you must use in the startActivityForResult call
     */
    public int register(Listener listener) {
        listeners.add(listener);
        return RESERVED + listeners.size() - 1;
    }

    /* ------------------- activity use ------------------- */

    /**
     * An activity must use this as:
     * <pre>
     *     @Override
     *     public void onActivityResult(int requestCode, int resultCode, Intent data) {
     *         if (!activityResultInjector.onActivityResult(requestCode, resultCode, data))
     *             super.onActivityResult(requestCode, resultCode, data);
     *     }
     * </pre>
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        var index = requestCode - RESERVED;
        if (index < 0) {
            // external
            Log.d("ACTIVITY_RESULT", "External request code (" + requestCode + "), consider using ActivityResultInjector");
            return false;
        }
        if (index >= listeners.size()) {
            // external?
            AndroidUtils.assertError("Invalid request code (" + requestCode + "), consider using ActivityResultInjector or a requestCode less than " + RESERVED);
            return false;
        }
        listeners.get(index).onActivityResult(resultCode, data);
        return true;
    }
}
