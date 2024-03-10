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

package com.asvk.urlshield.utilities;

import java.util.HashMap;
import java.util.Map;

public interface Enums {

    interface StringEnum {
        /**
         * This must return the string resourced associated with this enum value
         */
        int getStringResource();
    }

    interface IdEnum {
        /**
         * The id of the saved preference. Must never change
         */
        int getId();
    }

    interface ImageEnum {
        int getImageResource();
    }

    /**
     * Get an enum from an id
     */
    static <TE extends IdEnum> TE toEnum(Class<TE> te, int id) {
        TE[] enumConstants = te.getEnumConstants();
        for (TE constant : enumConstants) {
            if (constant.getId() == id) {
                return constant;
            }
        }
        return null;
    }

    /**
     * Get a map of id and enum
     */
    static <TE extends IdEnum> Map<Integer, TE> toEnumMap(Class<TE> te) {
        Map<Integer, TE> res = new HashMap<>();
        TE[] enumConstants = te.getEnumConstants();
        for (TE constant : enumConstants) {
            res.put(constant.getId(), constant);
        }
        return res;
    }
}
