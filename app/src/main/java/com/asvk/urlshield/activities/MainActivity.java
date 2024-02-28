package com.asvk.urlshield.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asvk.urlshield.R;
import com.asvk.urlshield.fragments.ActivityResultInjector;
import com.asvk.urlshield.modules.companions.VersionManager;
import com.asvk.urlshield.utilities.AndroidSettings;
import com.asvk.urlshield.utilities.methods.AndroidUtils;
import com.asvk.urlshield.utilities.methods.PackageUtils;

/**
 * The activity to show when clicking the desktop shortcut (when 'opening' the app)
 */
public class MainActivity extends Activity {

    private final ActivityResultInjector activityResultInjector = new ActivityResultInjector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSettings.setTheme(this, false);
        AndroidSettings.setLocale(this);
        setContentView(R.layout.activity_main);

        // mark as seen if required
        VersionManager.check(this);

        // open tutorial if not done yet
        if (!TutorialActivity.DONE(this).get()) {
            PackageUtils.startActivity(new Intent(this, TutorialActivity.class), R.string.toast_noApp, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        AndroidUtils.fixMenuIconColor(menu.findItem(R.id.menu_checkClipboard), this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_checkClipboard) {
            // the open in clipboard shortcut
            PackageUtils.startActivity(new Intent(this, ShortcutsActivity.class), R.string.toast_noApp, this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!activityResultInjector.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    /* ------------------- button clicks ------------------- */

    public void openModulesActivity(View view) {
        PackageUtils.startActivity(new Intent(this, ModulesActivity.class), R.string.toast_noApp, this);
    }

    public void openSettings(View view) {
        PackageUtils.startActivityForResult(
                new Intent(this, SettingsActivity.class),
                AndroidSettings.registerForReloading(activityResultInjector, this),
                R.string.toast_noApp,
                this
        );
    }

    public void openAbout(View view) {
        PackageUtils.startActivity(new Intent(this, AboutActivity.class), R.string.toast_noApp, this);
    }

    public void openSample(View view) {
        PackageUtils.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.sample_url))
        ).setPackage(getPackageName()), R.string.toast_noApp, this);
    }

    public void aboutToast(View view) {
        Toast.makeText(this, getString(R.string.app_name) + " - " + getString(R.string.vivekajee), Toast.LENGTH_SHORT).show();
    }

}