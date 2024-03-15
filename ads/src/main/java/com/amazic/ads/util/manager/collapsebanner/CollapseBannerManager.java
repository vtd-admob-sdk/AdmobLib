package com.amazic.ads.util.manager.collapsebanner;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.amazic.ads.callback.BannerCallBack;
import com.amazic.ads.util.Admob;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class CollapseBannerManager implements LifecycleEventObserver {
    protected AdView adView;
    protected ViewGroup adContainerView;
    protected static Lifecycle mLifecycle;
    private boolean mReload = true;
    private Activity mActivity;
    private List<String> mListId;
    private String mGravity;
    private BannerCallBack mBannerCallBack;
    private static CollapseBannerManager instance;

    public void initLifecycle(Lifecycle lifecycle){
        mLifecycle = lifecycle;
        mLifecycle.addObserver(this);
    }

    public void setReload(boolean reload) {
        this.mReload = reload;
    }

    public void loadCollapseBannerHasReload(Activity activity, List<String> listId, String gravity, BannerCallBack bannerCallBack) {
        mActivity = activity;
        mListId = listId;
        mGravity = gravity;
        mBannerCallBack = bannerCallBack;
        adView = Admob.getInstance().loadCollapsibleBannerFloorWithReload(activity, listId, gravity, bannerCallBack);
    }

    public void loadCollapseBannerHasReload(Activity activity, List<String> listId, String gravity) {
        mActivity = activity;
        mListId = listId;
        mGravity = gravity;
        adView = Admob.getInstance().loadCollapsibleBannerFloorWithReload(activity, listId, gravity);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        Log.e("LifeCycleEvent", "onStateChanged: ");
        switch (event) {
            case ON_CREATE: {
                Log.e("LifeCycleEvent", "native on create");
                break;
            }
            case ON_START: {
                Log.e("LifeCycleEvent", "native on start");
                if (mReload) {
                    if (mBannerCallBack != null) {
                        adView = Admob.getInstance().loadCollapsibleBannerFloorWithReload(mActivity, mListId, mGravity, mBannerCallBack);
                    } else {
                        adView = Admob.getInstance().loadCollapsibleBannerFloorWithReload(mActivity, mListId, mGravity);
                    }
                }
                break;
            }
            case ON_RESUME: {
                Log.e("LifeCycleEvent", "native on Resume");
                break;
            }
            case ON_PAUSE: {
                Log.e("LifeCycleEvent", "native on pause");
                break;
            }
            case ON_STOP: {
                Log.e("LifeCycleEvent", "native on stop");
                if (adView != null) {
                    adView.removeAllViews();
                    adView.destroy();
                }
                break;

            }
            case ON_DESTROY: {
                Log.e("LifeCycleEvent", "native on destroy");
//                if (adView != null) {
//                    adView.destroy();
//                }
//                if (mLifecycle != null) {
//                    mLifecycle.removeObserver(this);
//                }


                if (mLifecycle != null) {
                    mLifecycle.removeObserver(this);
                }
                break;
            }
            case ON_ANY: {
                Log.e("LifeCycleEvent", "native on any");
                break;

            }
        }
    }
}
