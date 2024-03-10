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

package com.asvk.urlshield.utilities.generics;

import android.app.Activity;
import android.content.Context;

import com.asvk.urlshield.dialogs.JsonEditor;
import com.asvk.urlshield.utilities.wrappers.InternalFile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a generic catalog
 */
public abstract class JsonCatalog {

    private final Activity cntx;
    private final InternalFile custom;
    private final int editorDescription;

    public JsonCatalog(Activity cntx, String fileName, int editorDescription) {
        this.cntx = cntx;
        this.editorDescription = editorDescription;
        custom = new InternalFile(fileName, cntx);
    }

    /**
     * Returns the current catalog
     */
    public JSONObject getCatalog() {
        // get the updated file first
        try {
            String content = custom.get();
            if (content != null) return new JSONObject(content);
        } catch (JSONException ignored) {
        }

        // no updated file or can't read, use built-in one
        return getBuiltIn();
    }

    /**
     * Gets the builtin catalog
     */
    public JSONObject getBuiltIn() {
        try {
            return buildBuiltIn(cntx);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    /**
     * Builds the builtIn catalog
     */
    abstract public JSONObject buildBuiltIn(Context cntx) throws JSONException;

    /**
     * Saves a json as new catalog
     */
    public boolean save(JSONObject content) {

        // same as builtin (maybe a reset?), delete custom
        if (content.toString().equals(getBuiltIn().toString())) {
            custom.delete();
            return true;
        }

        // store
        return custom.set(content.toString());
    }

    /**
     * Shows a dialog to manually edit the catalog
     */
    public void showEditor() {
        JsonEditor.show(getCatalog(), getBuiltIn(), editorDescription, cntx, this::save);
    }

}