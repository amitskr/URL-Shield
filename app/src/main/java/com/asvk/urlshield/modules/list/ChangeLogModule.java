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

import android.view.View;
import android.widget.TextView;

import com.asvk.urlshield.BuildConfig;
import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.modules.DescriptionConfig;
import com.asvk.urlshield.modules.companions.VersionManager;
import com.asvk.urlshield.utilities.methods.AndroidUtils;

/**
 * This module will show a message if the app was updated.
 */
public class ChangeLogModule extends AModuleData {

    @Override
    public String getId() {
        return "changelog";
    }

    @Override
    public int getName() {
        return R.string.mChg_name;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new ChangeLogModuleDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DescriptionConfig(R.string.mChg_desc);
    }
}

class ChangeLogModuleDialog extends AModuleDialog {

    public ChangeLogModuleDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_changelog;
    }

    @Override
    public void onInitialize(View views) {
        var versionManager = new VersionManager(getActivity());

        // set visibility
        var updated = versionManager.wasUpdated();
        setVisibility(updated);

        // updated text
        var txt_updated = views.<TextView>findViewById(R.id.updated);
        if (updated) {
            AndroidUtils.setRoundedColor(R.color.good, txt_updated);
        } else {
            txt_updated.setVisibility(View.GONE);
        }

        // version
        views.<TextView>findViewById(R.id.current).setText(getActivity().getString(R.string.mChg_current, BuildConfig.VERSION_NAME));

        // click dismiss to hide
        var dismiss = views.findViewById(R.id.dismiss);
        if (updated) {
            dismiss.setOnClickListener(v -> {
                versionManager.markSeen();

                txt_updated.setVisibility(View.GONE);
                dismiss.setVisibility(View.GONE);
                setVisibility(false);
            });
        } else {
            dismiss.setVisibility(View.GONE);
        }

        // click view to open the changes (the url)
        views.findViewById(R.id.viewChanges).setOnClickListener(v -> {
            // TODO: somehow redirect to the current locale
            // or, even better, load the changes and show inline (ask the user to get them)
            setUrl("https://github.com/amitskr/URL-Shield/tree/master/app/src/main/play/release-notes");

            // auto-dismiss
            dismiss.performClick();
        });
    }
}