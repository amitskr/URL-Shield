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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Why the default {@link android.view.LayoutInflater#inflate(int, ViewGroup)} returns the root supplied will always be a mystery, this fixes it.
 */
public interface Inflater {
    /**
     * like {@link android.view.LayoutInflater#inflate(int, ViewGroup)}, but returns the inflated view (not the root view)
     * Note: root must not be null (otherwise just use the original)
     */
    static <T extends View> T inflate(int resource, ViewGroup root) {
        final View view = LayoutInflater.from(root.getContext()).inflate(resource, root, false);
        root.addView(view);
        //noinspection unchecked
        return ((T) view);
    }
}
