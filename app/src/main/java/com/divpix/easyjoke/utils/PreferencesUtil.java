package com.divpix.easyjoke.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
    Activity context;
    SharedPreferences sharedPref;

    public PreferencesUtil(Activity context) {
        this.context = context;
        this.sharedPref = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void setSpeech(boolean b) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("speech_action", true);
        editor.apply();
    }

    public boolean getSpeech() {
        return sharedPref.getBoolean("speech_action", false);
    }
}
