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

package com.asvk.urlshield.modules.list;

import android.app.AlertDialog;
import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.generics.GenericPref;
import com.asvk.urlshield.utilities.methods.JavaUtils;

import java.util.Date;

/**
 * A module that logs all urls that passes through it
 */
public class LogModule extends AModuleData {

    public static GenericPref.Str LOG_DATA(Context cntx) {
        return new GenericPref.Str("log_data", "", cntx);
    }

    public static GenericPref.Int LOG_LIMIT(Context cntx) {
        return new GenericPref.Int("log_limit", 0, cntx); // 0 means unlimited
    }

    @Override
    public String getId() {
        return "log";
    }

    @Override
    public int getName() {
        return R.string.mLog_name;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new LogDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new LogConfig(cntx);
    }
}

class LogDialog extends AModuleDialog {

    private final GenericPref.Str log;
    private final GenericPref.Int limit;

    public LogDialog(MainDialog dialog) {
        super(dialog);
        log = LogModule.LOG_DATA(dialog);
        limit = LogModule.LOG_LIMIT(dialog);
    }

    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public void onInitialize(View views) {
        // new instance, log date
        addLine("--- " + new Date().toLocaleString() + " ---");
    }

    @Override
    public void onPrepareUrl(UrlData urlData) {
        // new url, log it
        addLine("> " + urlData.url);
    }

    private void addLine(String line) {
        var text = log.get();
        if (limit.get() > 0) text = JavaUtils.limitLines(text, '\n', limit.get() - 1);
        if (!text.isEmpty()) text += "\n";
        text += line.replace("\n", "%0A");
        log.set(text);
    }
}

class LogConfig extends AModuleConfig {

    private final GenericPref.Str log;

    public LogConfig(ModulesActivity activity) {
        super(activity);
        log = LogModule.LOG_DATA(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.config_log;
    }

    @Override
    public void onInitialize(View views) {
        views.findViewById(R.id.view).setOnClickListener(v -> showLog(false));
        views.findViewById(R.id.edit).setOnClickListener(v -> showLog(true));

        LogModule.LOG_LIMIT(getActivity()).attachToEditText(views.findViewById(R.id.limit), 0);
    }

    /**
     * Display the log, editable or clickable
     */
    public void showLog(boolean editable) {
        // init textview with content
        // on editable: an editText
        // on non-editable: a textview with links
        TextView textView = editable ? new EditText(getActivity()) : new TextView(getActivity());
        textView.setText(
                !log.get().isEmpty() ? log.get()
                        : editable ? ""
                        : getActivity().getString(R.string.mLog_empty)
        );
        if (!editable) Linkify.addLinks(textView, Linkify.WEB_URLS);

        // wrap into a padded scrollview for nice scrolling
        int pad = getActivity().getResources().getDimensionPixelSize(R.dimen.smallPadding);
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(textView);
        scrollView.setPadding(pad, pad, pad, pad);
        scrollView.post(() -> scrollView.scrollTo(0, textView.getHeight())); // start at bottom (new)

        // common dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.mLog_name)
                .setView(scrollView)
                .setNegativeButton(R.string.close, null);

        if (editable) {
            // editable: add save and clear buttons
            builder = builder
                    .setPositiveButton(R.string.save, (dialog, which) ->
                            log.set(textView.getText().toString())
                    )
                    .setNeutralButton(R.string.clear, null); // set below
        }

        // show
        AlertDialog dialog = builder.show();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // prepare more dialog
        // these are configured here to disable automatic auto-closing when they are pressed
        if (editable) {
            // editable: configure clear button
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                // clear content
                textView.setText("");
            });
        }

    }
}