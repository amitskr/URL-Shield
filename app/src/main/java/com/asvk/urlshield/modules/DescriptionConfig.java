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

package com.asvk.urlshield.modules;

import android.view.View;
import android.widget.TextView;

import com.asvk.urlshield.R;

/**
 * A simple Module configuration where just a description is needed
 * This module can always be enabled
 */
public class DescriptionConfig extends AModuleConfig {

    private final int description;

    public DescriptionConfig(int description) {
        this.description = description;
    }

    @Override
    public int getLayoutId() {
        return R.layout.config_description;
    }

    @Override
    public void onInitialize(View views) {
        ((TextView) views).setText(description);
    }
}
