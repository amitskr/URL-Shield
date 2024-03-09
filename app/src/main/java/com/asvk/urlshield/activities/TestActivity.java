package com.asvk.urlshield.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.asvk.urlshield.BuildConfig;
import com.asvk.urlshield.R;
import com.asvk.urlshield.utilities.AndroidSettings;
import com.asvk.urlshield.utilities.methods.AndroidUtils;
import com.asvk.urlshield.utilities.methods.PackageUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TestActivity extends Activity {
    EditText url;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSettings.setTheme(this, false);
        AndroidSettings.setLocale(this);
        setContentView(R.layout.activity_test);
        AndroidUtils.configureUp(this);

        if (!BuildConfig.DEBUG) {
            // on release, append version to the action bar title
            setTitle(getTitle() + " (V" + BuildConfig.VERSION_NAME + ")");
        } else {
            // on no-alpha, append type
            setTitle(getTitle() + " (" + BuildConfig.BUILD_TYPE + ")");
        }

        url=findViewById(R.id.uri);

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

    public void openSample(View view) {
        PackageUtils.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.sample_url))
        ).setPackage(getPackageName()), R.string.toast_noApp, this);
    }

    public void checkURL(View view) {
        uri=url.getText().toString().trim();
        if (uri.startsWith("https://") || uri.startsWith("http://")) {
            PackageUtils.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(uri)
            ).setPackage(getPackageName()), R.string.toast_noApp, this);
        } else {
            url.setError("Please Enter Correct URL");
        }
    }
}