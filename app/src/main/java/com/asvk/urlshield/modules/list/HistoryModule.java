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
import android.view.View;
import android.widget.ImageButton;

import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.modules.DescriptionConfig;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.methods.AndroidUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This module keeps a list of previous urls, and allows to jump between them
 */
public class HistoryModule extends AModuleData {
    @Override
    public String getId() {
        return "history";
    }

    @Override
    public int getName() {
        return R.string.mHist_name;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new HistoryDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DescriptionConfig(R.string.mHist_desc);
    }
}

class HistoryDialog extends AModuleDialog {

    // views
    private ImageButton first;
    private ImageButton back;
    private ImageButton list;
    private ImageButton forward;
    private ImageButton last;

    private final List<String> history = new ArrayList<>(); // list of urls
    private int index = -1; // current url in history (-1 if none)

    // ------------------- internal -------------------

    public HistoryDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_history;
    }

    // ------------------- init -------------------

    @Override
    public void onInitialize(View views) {
        // get views
        first = views.findViewById(R.id.first);
        back = views.findViewById(R.id.back);
        list = views.findViewById(R.id.list);
        forward = views.findViewById(R.id.forward);
        last = views.findViewById(R.id.last);

        // set listeners
        first.setOnClickListener(v -> {
            removeDuplicates(true);// partial cleanup
            setIndex(0);
        });
        back.setOnClickListener(v -> {
            removeDuplicates(true);// partial cleanup
            setIndex(index - 1);
        });
        list.setOnClickListener(v -> showList());
        forward.setOnClickListener(v -> {
            removeDuplicates(true);// partial cleanup
            setIndex(index + 1);
        });
        last.setOnClickListener(v -> {
            removeDuplicates(true);// partial cleanup
            setIndex(history.size() - 1);
        });
    }

    @Override
    public void onPrepareUrl(UrlData urlData) {
        // clear newer entries
        if (index + 1 < history.size())
            history.subList(index + 1, history.size()).clear();

        if (urlData.minorUpdate) {
            // minor update, replace previous entry
            history.set(index, urlData.url);
        } else {
            // add new entry
            history.add(urlData.url);
            index++;
        }
    }

    @Override
    public void onDisplayUrl(UrlData urlData) {
        // update
        updateUI();
    }

    /* ------------------- internal ------------------- */

    /**
     * updated the UI with the internal data (buttons visibility)
     */
    private void updateUI() {
        AndroidUtils.setEnabled(first, index > 0); // at least something to go back
        AndroidUtils.setEnabled(back, index > 0); // at least something to go back
        list.setEnabled(!history.isEmpty()); // at least something
        AndroidUtils.setEnabled(forward, index < history.size() - 1); // at least something to go forward
        AndroidUtils.setEnabled(last, index < history.size() - 1); // at least something to go forward

        setVisibility(history.size() > 1); // show if there is another element (other than the current one)
    }

    private void showList() {
        // cleanup
        removeDuplicates(false);
        // prepare elements (inverted because they look nicer top=newer)
        List<String> items = new ArrayList<>(history);
        Collections.reverse(items);

        // show list
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(
                        items.toArray(new CharSequence[0]),
                        index == -1 ? -1 : items.size() - 1 - index,
                        (dialog, which) -> {
                            setIndex(history.size() - 1 - which);
                            dialog.dismiss();
                        })
                .show();
        updateUI();
    }


    /**
     * Removes duplicated entries, also empty ones
     */
    private void removeDuplicates(boolean continuous) {
        for (int i = 0; i < history.size(); ) {
            String entry = history.get(i);
            int replaceIndex = i;
            if (continuous) {
                // check if next element is the same
                if (i + 1 < history.size() && history.get(i + 1).equals(entry)) {
                    replaceIndex = i + 1;
                }
            } else {
                // check if there is another later same element
                replaceIndex = history.lastIndexOf(entry);
            }
            if (entry.isEmpty() && index != i) {
                // remove empty, unless it is current index
                replaceIndex = index;
            }

            if (replaceIndex != i) {
                // remove this and replace
                if (index == i) index = replaceIndex;
                history.remove(i);
                if (index > i) index--;
            } else {
                // keep and continue
                i++;
            }
        }
    }

    /**
     * Sets a new index. If invalid, it is unchanged.
     * Updates UI too
     *
     * @param newIndex new index to set
     */
    private void setIndex(int newIndex) {
        if (newIndex >= 0 && newIndex < history.size()) {
            index = newIndex;
            setUrl(
                    new UrlData(history.get(newIndex))
                            .disableUpdates()
                            .dontTriggerOwn()
            );
        }
        updateUI();
    }
}