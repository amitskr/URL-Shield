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

import android.app.Activity;

import com.asvk.urlshield.utilities.methods.JavaUtils;

/**
 * A wrapper around {@link android.app.ProgressDialog} with more useful functions
 */
public class ProgressDialog extends android.app.ProgressDialog {

    /**
     * Usage:
     * <pre>
     *     ProgressDialog.run(cntx, R.string.text, progress -> {
     *         // do things
     *     });
     * </pre>
     */
    public static void run(Activity context, int title, JavaUtils.Consumer<ProgressDialog> consumer) {
        new ProgressDialog(context, title, consumer);
    }

    private final Activity cntx;

    /**
     * Constructs and shows the dialog
     */
    private ProgressDialog(Activity context, int title, JavaUtils.Consumer<ProgressDialog> consumer) {
        super(context);
        cntx = context;
        setTitle(title); // can't be changed later
        setMessage(""); // otherwise the message view can't be changed later
        setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL); // with spinner, indeterminate mode can't be changed
        setCancelable(false); // disable cancelable by default
        setCanceledOnTouchOutside(false);

        // show & start
        show();
        new Thread(() -> consumer.accept(this)).start();
    }

    /**
     * progress++
     */
    public void increaseProgress() {
        setProgress(getProgress() + 1);
    }

    /**
     * sets max value and resets to 0
     */
    @Override
    public void setMax(int max) {
        super.setMax(max);
        setProgress(0);
    }

    /**
     * Changes the message from any thread
     */
    @Override
    public void setMessage(CharSequence message) {
        cntx.runOnUiThread(() -> super.setMessage(message));
    }
}
