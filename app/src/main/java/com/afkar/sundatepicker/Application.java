package com.afkar.sundatepicker;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Alireza Afkar on 2/11/16 AD.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
