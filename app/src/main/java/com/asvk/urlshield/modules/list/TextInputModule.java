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

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.modules.DescriptionConfig;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.wrappers.DefaultTextWatcher;
import com.asvk.urlshield.utilities.wrappers.DoubleEvent;

/**
 * This module shows the current url and allows manual editing
 */
public class TextInputModule extends AModuleData {

    @Override
    public String getId() {
        return "text";
    }

    @Override
    public int getName() {
        return R.string.mInput_name;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new TextInputDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DescriptionConfig(R.string.mInput_desc);
    }
}

class TextInputDialog extends AModuleDialog {

    private final DoubleEvent doubleEdit = new DoubleEvent(1000); // if two updates happens in less than this milliseconds, they are considered as the same
    private boolean skipUpdate = false;

    private EditText edtxt_url;

    public TextInputDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_text;
    }

    @Override
    public void onInitialize(View views) {
        edtxt_url = views.findViewById(R.id.url);
        edtxt_url.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (skipUpdate) return;

                // new url by the user
                var newUrlData = new UrlData(s.toString())
                        .dontTriggerOwn()
                        .disableUpdates();

                // mark as minor if too quick
                if (doubleEdit.checkAndTrigger()) newUrlData.asMinorUpdate();

                // set
                setUrl(newUrlData);
            }
        });
    }

    @Override
    public void onDisplayUrl(UrlData urlData) {
        // setText fires the afterTextChanged listener, so we need to skip it
        skipUpdate = true;
        edtxt_url.setText(urlData.url);
        skipUpdate = false;
        doubleEdit.reset(); // next user update, even if immediately after, will be considered new
    }
}
