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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asvk.urlshield.R;
import com.asvk.urlshield.activities.ModulesActivity;
import com.asvk.urlshield.dialogs.MainDialog;
import com.asvk.urlshield.modules.AModuleConfig;
import com.asvk.urlshield.modules.AModuleData;
import com.asvk.urlshield.modules.AModuleDialog;
import com.asvk.urlshield.modules.DescriptionConfig;
import com.asvk.urlshield.url.UrlData;
import com.asvk.urlshield.utilities.methods.AndroidUtils;
import com.asvk.urlshield.utilities.methods.Inflater;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This module removes queries "?foo=bar" from an url
 * Originally made by PabloOQ
 */
public class RemoveQueriesModule extends AModuleData {

    @Override
    public String getId() {
        return "removeQueries";
    }

    @Override
    public int getName() {
        return R.string.mRemove_name;
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new RemoveQueriesDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DescriptionConfig(R.string.mRemove_desc);
    }
}

class RemoveQueriesDialog extends AModuleDialog {

    private TextView info;
    private Button remove;
    private LinearLayout box;

    public RemoveQueriesDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_removequeries;
    }

    @Override
    public void onInitialize(View views) {
        info = views.findViewById(R.id.text);
        remove = views.findViewById(R.id.button);
        remove.setText(R.string.mRemove_all);
        box = views.findViewById(R.id.box);

        // expand queries
        info.setOnClickListener(v -> {
            box.setVisibility(box.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            updateMoreIndicator();
        });

        // remove all queries
        remove.setOnClickListener(v -> setUrl(new UrlParts(getUrl()).getUrlWithoutQueries()));
    }

    @Override
    public void onDisplayUrl(UrlData urlData) {
        // initialize
        box.removeAllViews();

        // parse
        UrlParts parts = new UrlParts(urlData.url);

        if (parts.queriesSize() == 0) {
            // no queries present, nothing to notify
            info.setText(R.string.mRemove_noQueries);
            remove.setEnabled(false); // disable the remove button
            setVisibility(false);
        } else {
            // queries present, notify
            info.setText(parts.queriesSize() == 1
                    ? getActivity().getString(R.string.mRemove_found1) // 1 query
                    : getActivity().getString(R.string.mRemove_found, parts.queriesSize()) // 2+ queries
            );
            AndroidUtils.setAsClickable(info);
            remove.setEnabled(true); // enable the remove all button
            setVisibility(true);

            // for each query, create a button
            for (int i = 0; i < parts.queriesSize(); i++) {
                var button_text = Inflater.inflate(R.layout.button_text, box);

                // button that removes the query
                var queryName = parts.getQueryName(i);
                var button = button_text.<Button>findViewById(R.id.button);
                button.setText(queryName.isEmpty()
                        // if no name
                        ? getActivity().getString(R.string.mRemove_empty)
                        // with name
                        : getActivity().getString(R.string.mRemove_one, queryName)
                );
                var finalI = i;
                button.setOnClickListener(v -> setUrl(parts.getUrlWithoutQuery(finalI)));

                // text that displays the query value and sets it
                var queryValue = parts.getQueryValue(i);
                var text = button_text.<TextView>findViewById(R.id.text);
                text.setText(queryValue);
                AndroidUtils.setAsClickable(text);
                text.setOnClickListener(v -> setUrl(queryValue));
            }
        }

        // update
        updateMoreIndicator();
    }

    /**
     * Sets the 'more' indicator.
     */
    private void updateMoreIndicator() {
        AndroidUtils.setStartDrawables(info,
                box.getChildCount() == 0 ? 0
                        : box.getVisibility() == View.VISIBLE ? R.drawable.arrow_down
                        : R.drawable.arrow_right);
    }

    /**
     * Manages the splitting, removing and merging of queries
     */
    private static class UrlParts {
        private final String preQuery; // "http://google.com"
        private final List<String> queries = new ArrayList<>(); // ["ref=foo","bar"]
        private final String postQuery; // "#start"

        /**
         * Prepares a url and extracts its queries
         */
        public UrlParts(String url) {
            // an uri is defined as [scheme:][//authority][path][?query][#fragment]
            // we need to find a '?' followed by anything except a '#'
            // this allows us to work with any string, even with non-standard or malformed uris
            int iStart = url.indexOf("?"); // position of '?' (-1 if not present)
            int iEnd = url.indexOf("#", iStart + 1);
            iEnd = iEnd == -1 ? url.length() : iEnd; // position of '#' (end of string if not present)

            // add part until '?' or until postQuery if not present
            preQuery = url.substring(0, iStart != -1 ? iStart : iEnd);
            // add queries if any
            if (iStart != -1) {
                queries.addAll(splitFix(url.substring(iStart + 1, iEnd), "&"));
            }
            // add part after queries (empty if not present)
            postQuery = url.substring(iEnd);
        }

        /**
         * returns the number of queries present
         */
        public int queriesSize() {
            return queries.size();
        }

        /**
         * Returns the name of a query (by index)
         */
        public String getQueryName(int index) {
            return queries.get(index).split("=")[0];
        }

        /**
         * Returns the decoded value of a query (by index)
         */
        public String getQueryValue(int index) {
            String[] split = queries.get(index).split("=");
            if (split.length == 1) return "";
            try {
                return URLDecoder.decode(split[1]);
            } catch (Exception e) {
                // can't decode, return it directly
                return split[1];
            }
        }

        /**
         * Returns the full url
         */
        public String getUrl() {
            return getUrlWithoutQuery(-1);
        }

        /**
         * Returns the url without one query (by index)
         */
        public String getUrlWithoutQuery(int index) {
            var sb = new StringBuilder();

            // concatenate queries
            for (int i = 0; i < queries.size(); ++i)
                // excluding the required one
                if (i != index)
                    // first after '?', the rest after '&'
                    sb.append(sb.length() == 0 ? "?" : "&").append(queries.get(i));

            // finish building
            sb.insert(0, preQuery);
            sb.append(postQuery);
            return sb.toString();
        }

        /**
         * Returns the url without queries
         */
        public String getUrlWithoutQueries() {
            return preQuery + postQuery;
        }
    }

    /**
     * {@link String#split(String)} won't return the last element if it's empty.
     * This function does. And it returns a list instead of an array.
     * Everything else is the same.
     * Note: regex must not match '#', it does change this function
     */
    private static List<String> splitFix(String string, String regex) {
        // split with an extra char
        String[] parts = (string + "#").split(regex);
        // remove the extra char
        parts[parts.length - 1] = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 1);
        return Arrays.asList(parts);
    }
}
