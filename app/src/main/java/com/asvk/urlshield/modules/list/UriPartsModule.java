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

import android.net.Uri;
import android.net.UrlQuerySanitizer;
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
import com.asvk.urlshield.utilities.methods.JavaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This module shows all parts of the url decoded
 */
public class UriPartsModule extends AModuleData {

    @Override
    public String getId() {
        return "uriparts";
    }

    @Override
    public int getName() {
        return R.string.mParts_name;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new UriPartsDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DescriptionConfig(R.string.mParts_desc);
    }
}

class UriPartsDialog extends AModuleDialog {

    private LinearLayout box;
    private final List<String> expandedGroups = new ArrayList<>();

    public UriPartsDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_parts;
    }

    @Override
    public void onInitialize(View views) {
        box = views.findViewById(R.id.box);
    }

    @Override
    public void onDisplayUrl(UrlData urlData) {
        // clear
        box.removeAllViews();

        // parse
        var uri = Uri.parse(urlData.url);
        var urlQuerySanitizer = new UrlQuerySanitizer();
        // the default parseUrl doesn't remove the fragment
        if (uri.getQuery() != null) {
            urlQuerySanitizer.setAllowUnregisteredParamaters(true);
            urlQuerySanitizer.parseQuery(uri.getQuery());
        }

        // domain elements
        if (uri.getAuthority() != null || uri.getScheme() != null) {
            var domain = addGroup("Domain", -1, null);
            addPart("scheme", uri.getScheme(), domain, null);
            addPart("user info", uri.getUserInfo(), domain, null);
            addPart("host", uri.getHost(), domain, null);
            addPart("port", uri.getPort() != -1 ? String.valueOf(uri.getPort()) : null, domain, null);
        }

        // paths
        var pathSegments = uri.getPathSegments();
        if (pathSegments.size() > 0) {
            var paths = addGroup("Paths", pathSegments.size(), uri.buildUpon().path(null));
            for (var i = 0; i < pathSegments.size(); i++) {
                var pathSegment = pathSegments.get(i);

                var builder = uri.buildUpon();
                builder.path(null);
                for (int newI = 0; newI < pathSegments.size(); newI++) {
                    if (newI != i) builder.appendPath(pathSegments.get(newI));
                }
                addPart("/", pathSegment, paths, builder);
            }
        }

        // query parameters
        var parameters = urlQuerySanitizer.getParameterList();
        if (parameters.size() > 0) {
            var queries = addGroup("Parameters", parameters.size(), uri.buildUpon().query(null));
            for (var i = 0; i < parameters.size(); i++) {
                // generate same url but without this parameter
                var builder = uri.buildUpon();
                builder.query(null);
                for (var j = 0; j < parameters.size(); j++) {
                    if (i != j) builder.appendQueryParameter(parameters.get(j).mParameter, parameters.get(j).mValue);
                }
                // append the parameter
                addPart(parameters.get(i).mParameter, parameters.get(i).mValue, queries, builder);
            }
        }

        // fragment
        if (uri.getFragment() != null) {
            var fragment = addGroup("Fragment", -1, uri.buildUpon().fragment(null));
            addPart("#", uri.getFragment(), fragment, null);
        }

        setVisibility(box.getChildCount() > 0);
    }

    /**
     * Adds a collapsible group
     */
    private LinearLayout addGroup(String name, int size, Uri.Builder onDelete) {
        var title = Inflater.inflate(R.layout.uri_part, box);
        title.findViewById(R.id.key).setVisibility(View.GONE);

        var name_view = title.<TextView>findViewById(R.id.value);
        name_view.setText(name + (size <= -1 ? "" : " (" + size + ")"));
        AndroidUtils.setAsClickable(name_view);

        var delete_view = title.<Button>findViewById(R.id.delete);
        if (onDelete == null) delete_view.setVisibility(View.GONE);
        else delete_view.setOnClickListener(v -> setUrl(onDelete.build().toString()));

        var group = Inflater.<LinearLayout>inflate(R.layout.dialog_parts, box);
        group.setVisibility(expandedGroups.contains(name) ? View.VISIBLE : View.GONE);
        AndroidUtils.toggleableListener(
                title,
                v -> JavaUtils.toggleContains(expandedGroups, name),
                v -> {
                    group.setVisibility(expandedGroups.contains(name) ? View.VISIBLE : View.GONE);
                    AndroidUtils.setStartDrawables(name_view,
                            expandedGroups.contains(name) ? R.drawable.arrow_down : R.drawable.arrow_right
                    );
                }
        );

        return group;
    }

    /**
     * Adds a part
     */
    private void addPart(String name, String value, LinearLayout container, Uri.Builder onDelete) {
        if (value == null) return;
        // create row
        var part = Inflater.inflate(R.layout.uri_part, container);

        // configure key
        var key_view = part.<TextView>findViewById(R.id.key);
        key_view.setText(name.isEmpty() ? getActivity().getString(R.string.mParts_empty) : name);
        if (!name.isEmpty()) key_view.setOnLongClickListener(longTapToCopy);

        // configure value
        var value_view = part.<TextView>findViewById(R.id.value);
        value_view.setText(value);
        AndroidUtils.setAsClickable(value_view);
        value_view.setOnClickListener(v -> setUrl(value));
        value_view.setOnLongClickListener(longTapToCopy);

        // configure delete
        var delete_view = part.<Button>findViewById(R.id.delete);
        if (onDelete != null) {
            try {
                var newUrl = onDelete.build().toString();
                delete_view.setOnClickListener(v -> setUrl(newUrl));
            } catch (UnsupportedOperationException ignored) {
                delete_view.setVisibility(View.GONE);
            }
        } else {
            delete_view.setVisibility(View.GONE);
        }
    }

    /**
     * OnLongClickListener to copy a part (textview text) to the clipboard
     */
    private final View.OnLongClickListener longTapToCopy = v -> {
        AndroidUtils.copyToClipboard(getActivity(), R.string.mParts_copy, ((TextView) v).getText().toString());
        return true;
    };

}
