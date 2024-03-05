package com.asvk.urlshield.modules.companions;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.asvk.urlshield.R;
import com.asvk.urlshield.utilities.generics.GenericPref;
import com.asvk.urlshield.utilities.methods.AndroidUtils;

/**
 * Manages the incognito feature
 */
public class Incognito {

    /**
     * preference
     */
    public static GenericPref.Enumeration<OnOffConfig> PREF(Context cntx) {
        return new GenericPref.Enumeration<>("open_incognito", OnOffConfig.AUTO, OnOffConfig.class, cntx);
    }

    private final GenericPref.Enumeration<OnOffConfig> pref;
    private boolean state = false;

    public Incognito(Context cntx) {
        this.pref = PREF(cntx);
    }

    /**
     * Initialization from a given intent and a button to toggle
     */
    public void initFrom(Intent intent, ImageButton button) {
        // init state
        boolean visible = switch (pref.get()) {
            default -> {
                // for Firefox
                state = intent.getBooleanExtra("private_browsing_mode", false);
                yield true;
            }
            case HIDDEN -> {
                // for Firefox
                state = intent.getBooleanExtra("private_browsing_mode", false);
                yield false;
            }
            case DEFAULT_ON -> {
                state = true;
                yield true;
            }
            case DEFAULT_OFF -> {
                state = false;
                yield true;
            }
            case ALWAYS_ON -> {
                state = true;
                yield false;
            }
            case ALWAYS_OFF -> {
                state = false;
                yield false;
            }
        };

        // init button
        if (visible) {
            // show and configure
            button.setVisibility(View.VISIBLE);
            AndroidUtils.longTapForDescription(button);
            AndroidUtils.toggleableListener(button,
                    imageButton -> state = !state,
                    v -> v.setImageResource(state ? R.drawable.incognito : R.drawable.no_incognito)
            );
        } else {
            // hide
            button.setVisibility(View.GONE);
        }
    }

    /**
     * applies the setting to a given intent
     */
    public void apply(Intent intent) {
        // for Firefox
        intent.putExtra("private_browsing_mode", state);
    }
}
