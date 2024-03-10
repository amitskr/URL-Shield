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

import android.content.Context;

import com.asvk.urlshield.BuildConfig;
import com.asvk.urlshield.activities.TutorialActivity;
import com.asvk.urlshield.utilities.generics.GenericPref;

/**
 * Manages the app version, to notify of updates
 */
public class VersionManager {

    private final GenericPref.Str lastVersion;

    public static GenericPref.Str LASTVERSION_PREF(Context cntx) {
        return new GenericPref.Str("changelog_lastVersion", null, cntx);
    }

    /**
     * Check if the version must be updated
     */
    public static void check(Context cntx) {
        // just call the constructor, it does the check
        new VersionManager(cntx);
    }

    public VersionManager(Context cntx) {
        lastVersion = LASTVERSION_PREF(cntx);
        if (lastVersion.get() == null) {
            // no previous setting, the app is a new install, mark as seen
            // ... or maybe it was updated from an old version (where the setting was not yet implemented, 2.12 or below)
            // we check by testing the tutorial flag (which should be set if the app was used)
            if (TutorialActivity.DONE(cntx).get()) lastVersion.set("<2.12");
            else markSeen();
        }
    }

    /**
     * returns true iff the app was updated since last time it was used
     */
    public boolean wasUpdated() {
        // just check inequality. If the app was downgraded, you probably also want to be notified.
        return !BuildConfig.VERSION_NAME.equals(lastVersion.get());
    }

    /**
     * Marks the current version as seen (wasUpdated will return false until a new update happens)
     */
    public void markSeen() {
        lastVersion.set(BuildConfig.VERSION_NAME);
    }
}
