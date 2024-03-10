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

import android.view.View;

/**
 * Android fragments are deprecated?
 * I need to use that ugly huge compatibility library?
 * No way, I'll do my own.
 */
public interface Fragment {

    /**
     * @return the layout resource of this module
     */
    int getLayoutId();

    /**
     * Initializes this module from the given views (generated from {@link #getLayoutId()})
     *
     * @param views the inflated views
     */
    void onInitialize(View views);
}
