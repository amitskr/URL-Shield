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
import android.graphics.Color;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.modules.companions.VirusTotalUtility;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.generics.GenericPref;
import com.asvk.urlshield.utilities.methods.AndroidUtils;
import com.asvk.urlshield.utilities.methods.UrlUtils;
import com.asvk.urlshield.utilities.wrappers.DefaultTextWatcher;

/**
 * This module uses the VirusTotal api (https://developers.virustotal.com/reference) for url reports
 */
public class VirusTotalModule extends AModuleData {

    static GenericPref.Str API_PREF(Context cntx) {
        return new GenericPref.Str("api_key", "", cntx);
    }

    @Override
    public String getId() {
        return "virustotal";
    }

    @Override
    public int getName() {
        return R.string.mVT_name;
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new VirusTotalDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new VirusTotalConfig(cntx);
    }
}

class VirusTotalConfig extends AModuleConfig {

    final GenericPref.Str api_key;

    public VirusTotalConfig(ModulesActivity cntx) {
        super(cntx);
        api_key = VirusTotalModule.API_PREF(cntx);
    }

    @Override
    public boolean canBeEnabled() {
        final String key = api_key.get();
        return key != null && !key.isEmpty();
    }

    @Override
    public int getLayoutId() {
        return R.layout.config_virustotal;
    }

    @Override
    public void onInitialize(View views) {
        var edit_key = views.<TextView>findViewById(R.id.api_key);
        edit_key.setText(api_key.get());
        edit_key.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                api_key.set(s.toString());
                if (!canBeEnabled()) disable();
            }
        });
    }
}

class VirusTotalDialog extends AModuleDialog {

    private static final int RETRY_TIMEOUT = 5000;
    private Button btn_scan;
    private TextView txt_result;

    private boolean scanning = false;
    private VirusTotalUtility.InternalReponse result = null;

    private final GenericPref.Str api_key;

    public VirusTotalDialog(MainDialog dialog) {
        super(dialog);
        api_key = VirusTotalModule.API_PREF(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.button_text;
    }

    @Override
    public void onInitialize(View views) {
        btn_scan = views.findViewById(R.id.button);
        btn_scan.setText(R.string.mVT_scan);
        btn_scan.setOnClickListener(v -> scanOrCancel());

        txt_result = views.findViewById(R.id.text);
        txt_result.setOnClickListener(v -> showInfo(false));
        txt_result.setOnLongClickListener(v -> {
            showInfo(true);
            return true;
        });
    }

    @Override
    public void onDisplayUrl(UrlData urlData) {
        // TODO: cancel on onPrepare
        scanning = false;
        result = null;
        updateUI();
    }

    /**
     * Performs a scan of the current url, in background.
     * Or cancels current scan
     */
    private void scanOrCancel() {
        if (scanning) {
            // already scanning? cancel
            scanning = false;
        } else {
            // start scan
            scanning = true;
            new Thread(this::_scanUrl).start();
        }
        updateUI();
    }

    /**
     * Manages the scanning in the background
     */
    private void _scanUrl() {
        VirusTotalUtility.InternalReponse response;
        while (scanning) {
            // asks for the report
            response = VirusTotalUtility.scanUrl(getUrl(), api_key.get(), getActivity());

            // check valid report
            if (response.detectionsTotal > 0 || response.error != null) {
                result = response;
                scanning = false;
                getActivity().runOnUiThread(this::updateUI);
                return;
            }

            // retry if still no report
            try {
                Thread.sleep(RETRY_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Updates the ui
     */
    private void updateUI() {
        if (scanning) {
            // scanning in progress, show cancel
            btn_scan.setText(R.string.mVT_cancel);
            setResult(getActivity().getString(R.string.mVT_scanning), 0);
            btn_scan.setEnabled(true);
        } else {
            // not a scanning in progress
            btn_scan.setText(R.string.mVT_scan);
            if (result == null) {
                // no result available, new url
                setResult("", 0);
                btn_scan.setEnabled(true);
            } else {
                // result available
                if (result.error != null) {
                    // an error ocurred
                    setResult(result.error, Color.TRANSPARENT);
                    btn_scan.setEnabled(true);
                } else {
                    // valid result
                    btn_scan.setEnabled(false);
                    if (result.detectionsPositive > 2) {
                        // more that two bad detection, bad url
                        setResult(getActivity().getString(R.string.mVT_badUrl, result.detectionsPositive, result.detectionsTotal, result.date), R.color.bad);
                    } else if (result.detectionsPositive > 0) {
                        // 1 or 2 bad detections, warning
                        setResult(getActivity().getString(R.string.mVT_warningUrl, result.detectionsPositive, result.detectionsTotal, result.date), R.color.warning);
                    } else {
                        // no detections, good
                        setResult(getActivity().getString(R.string.mVT_goodUrl, result.detectionsTotal, result.date), R.color.good);
                    }
                }
            }
        }

    }

    /**
     * Utility to update the ui
     *
     * @param message with this message
     * @param color   and this background color
     */
    private void setResult(String message, int color) {
        txt_result.setText(message);
        if (color != 0) {
            AndroidUtils.setRoundedColor(color, txt_result);
        } else {
            AndroidUtils.clearRoundedColor(txt_result);
        }
    }

    /**
     * Shows the report results
     *
     * @param details if true, the virustotal page is opened, if false just a basic dialog with the json
     */
    private void showInfo(boolean details) {
        if (result == null || result.error != null) return;

        if (details) {
            UrlUtils.openUrlRemoveThis(result.scanUrl, getActivity());
        } else {
            // TODO: beautify this
            new AlertDialog.Builder(getActivity())
                    .setMessage(result.info)
                    .show();
        }
    }
}
