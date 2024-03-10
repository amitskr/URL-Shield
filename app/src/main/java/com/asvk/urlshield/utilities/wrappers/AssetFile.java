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

import com.asvk.urlshield.utilities.methods.StreamUtils;

import java.io.IOException;

/**
 * Represents a file from assets (read-only)
 */
public class AssetFile {
    private final String fileName;
    private final Context cntx;

    public AssetFile(String fileName, Context cntx) {
        this.fileName = fileName;
        this.cntx = cntx;
    }

    /**
     * Returns the content of the file, or null if can't be read)
     */
    public String get() {
        // get the updated file first
        try {
            return StreamUtils.inputStream2String(cntx.getAssets().open(fileName));
        } catch (IOException ignored) {
            return null;
        }
    }

}
