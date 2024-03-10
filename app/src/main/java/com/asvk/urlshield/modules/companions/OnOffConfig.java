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

package com.asvk.urlshield.modules.companions;

import com.asvk.urlshield.R;
import com.asvk.urlshield.utilities.Enums;

/**
 * Enum for a toggle with on/off/auto x hide/show states
 */
public enum OnOffConfig implements Enums.IdEnum, Enums.StringEnum {
    // TODO: think of better labels. "Show/Hide button + enabled/disabled/default state"?
    AUTO(0, R.string.auto),
    HIDDEN(5, R.string.hidden),
    DEFAULT_ON(1, R.string.defaultOn),
    DEFAULT_OFF(2, R.string.defaultOff),
    ALWAYS_ON(3, R.string.alwaysOn),
    ALWAYS_OFF(4, R.string.alwaysOff),
    ;

    // -----

    private final int id;
    private final int stringResource;

    OnOffConfig(int id, int stringResource) {
        this.id = id;
        this.stringResource = stringResource;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getStringResource() {
        return stringResource;
    }
}
