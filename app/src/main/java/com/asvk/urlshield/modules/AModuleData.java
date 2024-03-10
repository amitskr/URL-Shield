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

import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;

public abstract class AModuleData {

    /**
     * @return the unique identifier of this module
     */
    public abstract String getId();

    /**
     * @return the user visible name of this module
     */
    public abstract int getName();

    /**
     * @return whether this module should be enabled by default
     */
    public boolean isEnabledByDefault() {
        return true;
    }

    /**
     * Returns the dialog fragment of this module
     *
     * @param cntx for the fragment
     * @return the initialized module dialog class
     */
    public abstract AModuleDialog getDialog(MainDialog cntx);

    /**
     * Returns the configuration fragment of this module
     *
     * @param cntx for the fragment
     * @return the initialized module configuration class
     */
    public abstract AModuleConfig getConfig(ModulesActivity cntx);
}
