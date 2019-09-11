package com.creativeshare.roses.share;


import android.app.Application;
import android.content.Context;


import com.creativeshare.roses.language.Language;
import com.creativeshare.roses.preferences.Preferences;


public class Local extends Application {
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(this, "SERIF", "fonts/din-next-lt-w23-regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(this, "DEFAULT", "fonts/din-next-lt-w23-regular.ttf");
        TypefaceUtil.overrideFont(this, "MONOSPACE", "fonts/din-next-lt-w23-regular.ttf");
        TypefaceUtil.overrideFont(this, "SANS_SERIF", "fonts/din-next-lt-w23-regular.ttf");
    }
}

