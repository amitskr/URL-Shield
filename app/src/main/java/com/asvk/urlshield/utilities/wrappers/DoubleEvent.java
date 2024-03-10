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

package com.asvk.urlshield.utilities.wrappers;

/**
 * A small utility to detect repeated events in a short time
 */
public class DoubleEvent {

    private long lastTime;

    private final long delay;

    /**
     * Two events separated by [delayMillis] will be considered equal
     */
    public DoubleEvent(long delayMillis) {
        this.delay = delayMillis;
        reset();
    }

    /**
     * returns true if an event now happens less than delay from before
     * Doesn't trigger an event
     */
    public boolean check() {
        return System.currentTimeMillis() - lastTime < delay;
    }

    /**
     * Triggers an event
     */
    public void trigger() {
        lastTime = System.currentTimeMillis();
    }

    /**
     * returns true if an event now happens less than delay from before
     * Triggers an event afterward
     */
    public boolean checkAndTrigger() {
        var check = check();
        trigger();
        return check;
    }

    /**
     * Resets the event trigger, next check will always return false
     */
    public void reset() {
        lastTime = -1;
    }

}
