package com.divpix.easyjoke;

import android.app.Application;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;

public class JokeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Iconics.registerFont(new GoogleMaterial());
    }
}
