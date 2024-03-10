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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.asvk.urlshield.R;
import com.asvk.urlshield.fragments.ActivityResultInjector;
import com.asvk.urlshield.fragments.BrowserButtonsFragment;
import com.asvk.urlshield.utilities.AndroidSettings;
import com.asvk.urlshield.utilities.generics.GenericPref;
import com.asvk.urlshield.utilities.methods.PackageUtils;
import com.asvk.urlshield.utilities.wrappers.DoubleEvent;

import java.util.Locale;

public class TutorialActivity extends Activity {

    private final DoubleEvent doubleClick = new DoubleEvent(500); // to avoid exiting when pressing next/prev very fast

    private Button prevButton;
    private Button nextButton;
    private GenericPref.Bool tutorialDone;
    private ViewFlipper flipper;
    private TextView pageIndexText;

    public static GenericPref.Bool DONE(Context cntx) {
        return new GenericPref.Bool("tutorial_done", false, cntx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSettings.setTheme(this, false);
        AndroidSettings.setLocale(this);
        setContentView(R.layout.activity_tutorial);
        setTitle(R.string.tutorial);

        tutorialDone = DONE(this);

        flipper = findViewById(R.id.flipper);
        prevButton = findViewById(R.id.bBack);
        nextButton = findViewById(R.id.bNext);
        pageIndexText = findViewById(R.id.pageIndex);

        configureBrowserButtons();
        updateButtons();

    }

    /* ------------------- browser fragment ------------------- */

    private final ActivityResultInjector activityResultInjector = new ActivityResultInjector();
    private final BrowserButtonsFragment browserButtons = new BrowserButtonsFragment(this, activityResultInjector);

    private void configureBrowserButtons() {
        browserButtons.onInitialize(findViewById(browserButtons.getLayoutId()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!activityResultInjector.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    /* ------------------- buttons ------------------- */

    @Override
    public void onBackPressed() {
        prev(null);
    }

    public void prev(View view) {
        if (flipper.getDisplayedChild() == 0) {
            // first page, exit
            if (view != null && doubleClick.checkAndTrigger()) return; // unless clicked too fast
            exit();
        } else {
            // show prev
            flipper.showPrevious();
            doubleClick.trigger();
            updateButtons();
        }
    }

    public void next(View view) {
        if (flipper.getDisplayedChild() == flipper.getChildCount() - 1) {
            // last page, exit
            if (doubleClick.checkAndTrigger()) return; // unless clicked too fast
            exit();
        } else {
            // show next
            flipper.showNext();
            doubleClick.trigger();
            updateButtons();
        }
    }

    public void openModulesActivity(View view) {
        PackageUtils.startActivity(new Intent(this, ModulesActivity.class), R.string.toast_noApp, this);
    }

    /* ------------------- actions ------------------- */

    /**
     * Updates the buttons and index texts
     */
    private void updateButtons() {
        var current = flipper.getDisplayedChild();
        var max = flipper.getChildCount();

        prevButton.setText(current == 0 ? R.string.btn_tutorialSkip : R.string.back);
        nextButton.setText(current != max - 1 ? R.string.next : R.string.btn_tutorialEnd);

        pageIndexText.setText(String.format(Locale.getDefault(), "%d/%d", current + 1, max));
    }

    /**
     * Marks the tutorial as completed and exits
     */
    private void exit() {
        tutorialDone.set(true);
        this.finish();
    }

}
