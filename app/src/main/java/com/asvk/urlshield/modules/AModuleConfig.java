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

import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.fragments.Fragment;

/**
 * Base class for a module configuration fragment
 */
public abstract class AModuleConfig implements Fragment {

    // ------------------- private data -------------------

    private final ModulesActivity activity;

    // ------------------- initialization -------------------

    public AModuleConfig() {
        this.activity = null;
    }

    public AModuleConfig(ModulesActivity activity) {
        this.activity = activity;
    }

    // ------------------- abstract functions -------------------

    /**
     * @return true iff this module can be enabled (possibly from current settings).
     * True by default
     */
    public boolean canBeEnabled() {
        return true;
    }

    // ------------------- utilities -------------------

    /**
     * Disables this module
     */
    public final void disable() {
        if (activity != null) activity.disableModule(this);
    }

    /**
     * Returns the config activity. Will be null when initialized with empty constructor
     */
    public final Activity getActivity() {
        return activity;
    }

}
