package com.amazicadslibrary;

import androidx.annotation.NonNull;

import com.amazic.ads.event.AppsflyerEvent;
import com.amazic.ads.util.Admob;
import com.amazic.ads.util.AdsApplication;
import com.amazic.ads.util.AppOpenManager;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends AdsApplication {
    public static String PRODUCT_ID_MONTH = "android.test.purchased";
    @Override
    public void onCreate() {
        super.onCreate();
        AppOpenManager.getInstance().disableAppResumeWithActivity(Splash.class);
        AppsflyerEvent.getInstance().init(this, "1233", true);
        List<String> listINAPId = new ArrayList<>();
        listINAPId.add(PRODUCT_ID_MONTH);
        List<String> listSubsId = new ArrayList<>();
        listSubsId.add(PRODUCT_ID_MONTH);
    }

    @NonNull
    @Override
    public String getAppTokenAdjust() {
        return "null";
    }

    @NonNull
    @Override
    public String getFacebookID() {
        return "null";
    }

    @Override
    public Boolean buildDebug() {
        return true;
    }

    @Override
    protected String initAppOpenResume() {
        return "ca-app-pub-3940256099942544/9257395921";
    }

    @Override
    protected boolean isSetUpAdjust() {
        return true;
    }
}
