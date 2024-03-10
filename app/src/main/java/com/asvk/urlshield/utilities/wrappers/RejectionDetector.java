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

package com.asvk.urlshield.utilities.wrappers;

import android.content.Context;

import com.asvk.urlshield.utilities.generics.GenericPref;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Saves the package and time of the last opened url.
 * If it is requested again in a short amount of time, that package is considered to 'reject' the url and is hidden.
 */
public class RejectionDetector {

    private static final int TIMEFRAME = 5000;
    private final GenericPref.LstStr rejectLast; // [openedTimeMillis, package, url]

    public RejectionDetector(Context cntx) {
        rejectLast = new GenericPref.LstStr("reject_last", "\n", 3, Collections.emptyList(), cntx);
    }

    /**
     * Marks a url as opened to a package (at this moment)
     */
    public void markAsOpen(String url, String packageName) {
        rejectLast.set(List.of(Long.toString(System.currentTimeMillis()), packageName, url));
    }

    /**
     * returns the last package that opened the url if it happened in a short amount of time, null otherwise
     */
    public String getPrevious(String url) {
        try {
            var data = rejectLast.get();

            // return the saved package if the time is less than the timeframe and the url is the same
            return !data.isEmpty()
                    && System.currentTimeMillis() - Long.parseLong(data.get(0)) < TIMEFRAME
                    && Objects.equals(data.get(2), url)
                    ? data.get(1)
                    : null;
        } catch (Exception ignore) {
            // just ignore errors while retrieving the data
            return null;
        }
    }
}
