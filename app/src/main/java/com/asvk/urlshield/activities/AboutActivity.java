package com.asvk.urlshield.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asvk.urlshield.BuildConfig;
import com.asvk.urlshield.R;
import com.asvk.urlshield.utilities.AndroidSettings;
import com.asvk.urlshield.utilities.methods.AndroidUtils;
import com.asvk.urlshield.utilities.methods.Inflater;
import com.asvk.urlshield.utilities.methods.PackageUtils;

import java.util.List;

public class AboutActivity extends Activity {

    // ------------------- links -------------------

    private static final List<Pair<Integer, String>> LINKS = List.of(
            Pair.create(R.string.link_source, "https://github.com/amitskr/URL-Shield"),
            Pair.create(R.string.link_privacy, "https://github.com/amitskr/URL-Shield/blob/master/docs/PRIVACY%20POLICY.md")
    );

    // ------------------- listeners -------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSettings.setTheme(this, false);
        AndroidSettings.setLocale(this);
        setContentView(R.layout.activity_about);
        setTitle(R.string.a_about);
        AndroidUtils.configureUp(this);

        if (!BuildConfig.DEBUG) {
            // on release, append version to the action bar title
            setTitle(getTitle() + " (V" + BuildConfig.VERSION_NAME + ")");
        } else {
            // on no-alpha, append type
            setTitle(getTitle() + " (" + BuildConfig.BUILD_TYPE + ")");
        }

        // fill contributors and translators
        this.<TextView>findViewById(R.id.txt_about).setText(
                getString(R.string.txt_about,
                        getString(R.string.contributors),
                        getString(R.string.all_translators)
                )
        );

        // create links
        ViewGroup v_links = findViewById(R.id.links);
        for (var link : LINKS) {
            var v_link = Inflater.<TextView>inflate(R.layout.about_link, v_links);
            v_link.setText(link.first);
            AndroidUtils.setAsClickable(v_link);
            v_link.setTag(link.second.replace("{package}", getPackageName()));
            // click to open, long-click to share
            v_link.setOnClickListener(v -> open(((String) v.getTag())));
            v_link.setOnLongClickListener(v -> {
                share(((String) v.getTag()));
                return true;
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // press the 'back' button in the action bar to go back
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ------------------- actions -------------------

    /**
     * Open an url in the browser
     */
    private void open(String url) {
        PackageUtils.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), R.string.toast_noBrowser, this);
    }

    /**
     * Share an url as plain text
     */
    private void share(String url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.addFlags(Intent. FLAG_ACTIVITY_NEW_DOCUMENT);

        // Add data to the intent, the receiving app will decide what to do with it.
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, url);

        PackageUtils.startActivity(Intent.createChooser(share, getString(R.string.share)), R.string.toast_noApp, this);
    }
}