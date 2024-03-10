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

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.modules.companions.Hosts;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.methods.AndroidUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This module checks for patterns characters in the url
 */
public class HostsModule extends AModuleData {

    @Override
    public String getId() {
        return "hosts";
    }

    @Override
    public int getName() {
        return R.string.mHosts_name;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new HostsDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new HostsConfig(cntx);
    }
}

class HostsConfig extends AModuleConfig {
    private final Hosts hosts;

    public HostsConfig(ModulesActivity cntx) {
        super(cntx);
        hosts = new Hosts(cntx);
    }

    @Override
    public int getLayoutId() {
        return R.layout.config_hosts;
    }

    @Override
    public void onInitialize(View views) {
        // click to build
        views.findViewById(R.id.rebuild).setOnClickListener(v ->
                hosts.build(false, () ->
                        Toast.makeText(getActivity(), getActivity().getString(R.string.mHosts_built, hosts.size()), Toast.LENGTH_LONG).show()
                )
        );
        // click to edit
        views.findViewById(R.id.edit).setOnClickListener(v ->
                hosts.showEditor()
        );
    }

}

class HostsDialog extends AModuleDialog {

    private final Hosts hosts;

    private TextView text;

    public HostsDialog(MainDialog dialog) {
        super(dialog);
        hosts = new Hosts(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_hosts;
    }

    @Override
    public void onInitialize(View views) {
        text = views.findViewById(R.id.text);
        text.setOnClickListener(v -> {
            if (hosts.isUninitialized()) hosts.build(true, () -> onNewUrl(getUrl()));
        });
    }

    @Override
    public void onDisplayUrl(UrlData urlData) {
        if (hosts.isUninitialized()) {
            // check built
            text.setText(R.string.mHosts_uninitialized);
            AndroidUtils.setRoundedColor(R.color.warning, text);
            setVisibility(true);
            return;
        }
        onNewUrl(urlData.url);
    }

    private void onNewUrl(String url) {

        // init
        String host;
        try {
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            text.setText(R.string.mHosts_parseError);
            AndroidUtils.setRoundedColor(R.color.warning, text);
            setVisibility(true);
            return;
        }

        var label = hosts.contains(host);
        if (label != null) {
            text.setText(label.first);
            try {
                AndroidUtils.setRawRoundedColor(Color.parseColor(label.second), text);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                AndroidUtils.setRoundedColor(R.color.bad, text);
            }
            setVisibility(true);
        } else {
            text.setText(R.string.mHosts_noLabel);
            AndroidUtils.clearRoundedColor(text);
            setVisibility(false);
        }
    }

}
