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

package com.asvk.urlshield.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.quicksettings.TileService;
import android.view.Window;
import android.widget.Toast;

import com.asvk.urlshield.R;
import com.asvk.urlshield.utilities.AndroidSettings;
import com.asvk.urlshield.utilities.methods.AndroidUtils;
import com.asvk.urlshield.utilities.methods.PackageUtils;

import java.util.Collections;
import java.util.Set;

/**
 * This activity opens (on this app) a link detected on the clipboard text. If multiple asks.
 */
public class ShortcutsActivity extends Activity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            // old android method
            setResult(RESULT_OK, new Intent()
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, getClass())
                            .setAction(Intent.ACTION_VIEW)
                    )
                    .putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.shortcut_checkClipboard))
                    .putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.mipmap.clipboard_launcher))
            );
            finish();
            return;
        }

        // set theme without action bar
        AndroidSettings.setTheme(this, true);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        waitForFocus(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // reused activity, cancel previous dialog
        if (dialog != null) dialog.dismiss();
        waitForFocus(0);
    }

    /**
     * Waits for the app to has focus (up to 5 seconds) then runs.
     * The clipboard isn't available until the app is fully visible and with focus
     */
    private void waitForFocus(int retry) {
        if (!hasWindowFocus() && retry < 50) getWindow().getDecorView().postDelayed(() -> waitForFocus(retry + 1), 100);
        else run();
    }

    /**
     * To run when the clipboard is available
     */
    private void run() {
        var links = getLinksFromClipboard();
        switch (links.size()) {
            case 0:
                // no links, notify
                Toast.makeText(this, "No links detected", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 1:
                // 1 link, open
                open(links.iterator().next());
                finish();
                break;
            default:
                // multiple links, choose
                var links_array = links.toArray(new String[0]);
                dialog = new AlertDialog.Builder(this)
                        .setItems(links_array, (dialog, which) -> {
                            open(links_array[which]);
                            dialog.cancel();
                        })
                        .setOnCancelListener(o -> this.finish())
                        .show();
        }
    }

    /**
     * Opens a link on our app
     */
    private void open(String link) {
        PackageUtils.startActivity(
                new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, link)
                        .setType("text/plain")
                        .setPackage(getPackageName()),
                R.string.toast_noApp,
                this
        );
    }

    /**
     * Returns all links detected on the clipboard
     */
    private Set<String> getLinksFromClipboard() {

        var clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) return Collections.emptySet();

        var primaryClip = clipboard.getPrimaryClip();
        if (primaryClip == null || primaryClip.getItemCount() < 1) return Collections.emptySet();

        return AndroidUtils.getLinksFromText(primaryClip.getItemAt(0).coerceToText(this));
    }

    /**
     * The tile, just a shortcut to the activity above
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static class ShortcutsTile extends TileService {

        @Override
        public void onClick() {
            super.onClick();
            // just call the activity to handle it
            startActivityAndCollapse(new Intent(this, ShortcutsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
