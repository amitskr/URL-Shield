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

package com.asvk.urlshield.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asvk.urlshield.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @see this#show
 */
public class JsonEditor {

    public interface Listener {
        /**
         * Current data that the user wants to save.
         * Return true if it is ok and the dialog should be dismissed, false otherwise.
         */
        boolean onSave(JSONObject content);
    }

    /**
     * Displays a generic editor for json content.
     */
    public static void show(JSONObject content, JSONObject reset, int description, Activity cntx, Listener onSave) {

        // prepare dialog content
        View views = cntx.getLayoutInflater().inflate(R.layout.json_editor, null);
        views.<TextView>findViewById(R.id.description).setText(description);

        // init rules
        EditText data = views.findViewById(R.id.data);
        data.setText(noFailToString(content));

        // formatter
        views.findViewById(R.id.format).setOnClickListener(v -> {
            try {
                data.setText(new JSONObject(data.getText().toString()).toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(cntx, R.string.toast_invalid, Toast.LENGTH_SHORT).show();
            }
        });

        // prepare dialog
        AlertDialog dialog = new AlertDialog.Builder(cntx)
                .setView(views)
                .setPositiveButton(R.string.save, null) // set below
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.reset, null) // set below
                .setCancelable(false)
                .show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // prepare more dialog
        // these are configured here to disable automatic auto-closing when they are pressed
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                if (onSave.onSave(new JSONObject(data.getText().toString()))) {
                    dialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(cntx, R.string.toast_invalid, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            // clear catalog and reload internal
            data.setText(noFailToString(reset));
        });
    }

    /**
     * Formats a json into a valid string that doesn't throws a JSON exception
     * (as a function because I needed a final string)
     */
    private static String noFailToString(JSONObject content) {
        try {
            return content.toString(2);
        } catch (JSONException e) {
            // panic, don't format then
            return content.toString();
        }
    }

}
