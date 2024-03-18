<h1>Document admob sdk</h1>
<h3><br>Adding the library to your project: Add the following in your root build.gradle at the end of repositories:</br></h3>
<pre>
  dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
}
</pre>
<h3>Implement library in your app level build.gradle:</h3>
<pre>
 dependencies {
    implementation ("com.github.vtd-admob-sdk:AdmobLib:1.0.0")
    implementation ("com.google.android.gms:play-services-ads:23.0.0")
    //multidex
    implementation ("androidx.multidex:multidex:2.0.1")
  }

defaultConfig {
multiDexEnabled true
}
</pre>
<h3><li>Add app id in Manifest:</br></h3>
<pre>
     < meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-3940256099942544~3347511713" />
</pre>
<h3><br>Init aplication</br></h3>
<pre> < application
   android:name=".MyApplication"
   .
   .
   .../></pre>
<pre>
  public class MyApplication extends AdsApplication {
    public static String PRODUCT_ID_MONTH = "android.test.purchased";
    @Override
    public void onCreate() {
        super.onCreate();
        AppOpenManager.getInstance().disableAppResumeWithActivity(Splash.class);

        //init Appsflyer
        //AppsflyerEvent.getInstance().init(this, "1233", true);
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

    // set id app open resume
    @Override
    protected String initAppOpenResume() {
        return "ca-app-pub-3940256099942544/9257395921";
    }

    // yêu cầu dùng Adjust thì set = true không dùng thì set = false
    @Override
    protected boolean isSetUpAdjust() {
        return true;
    }
}

</pre>

<h3><br>init Ump (Use in Splash Activity)</br></h3>
<pre>
// chú ý muốn hiện dialog ump thì cần fake ip sang châu âu, và logo của app phải có kích thước 512x512
AdsConsentManager adsConsentManager = new AdsConsentManager(this);
        adsConsentManager.requestUMP(!AdsConsentManager.getConsentResult(Splash.this),result -> {
                // accept ump
                if (result){
                    //init sdk
                    Admob.getInstance().initAdmod(Splash.this);
                    //funtion use to show ads splash
                    initShowAdsSplash();
                    // trường hợp project tích hợp với remote config thì cần init remote config sau đó mới initShowAdsSplash()
                }else {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }
            
        });
  private void initShowAdsSplash(){
         adsSplash = AdsSplash.init(true, true, "30_70");
        List<String> idsOpen = new ArrayList<String>();
        idsOpen.add("ca-app-pub-3940256099942544/9257395921");
        List<String> idsInter = new ArrayList<String>();
        idsInter.add("ca-app-pub-3940256099942544/1033173712");
        adsSplash.showAdsSplash(Splash.this, idsOpen, idsInter, adCallback, interCallback);
    }

   @Override
    protected void onResume() {
     if (adsSplash != null) {
            adsSplash.onCheckShowSplashWhenFail(this, adCallback, mInterCallback);
        }
    }

</pre>
<h3>- BannerAds</h3>
<div class="content">
  <h4>View xml</h4>
<pre>< include
        android:id="@+id/include"
        layout="@layout/layout_banner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:layout_constraintBottom_toBottomOf="parent" /> 

 </pre>
<h4>Load in ativity</h4>
<pre>
    //load banner không reload lại khi chuyển màn
    Admob.getInstance().loadBanner(this,"bannerID");
    //load banner có reload khi chuyển màn
      BannerBuilder bannerBuilder = new BannerBuilder(this, this)
                    .initId(List<String> ids);
            bannerManager = new BannerManager(bannerBuilder);
    // gọi hàm này khi có sự kiện chuyển màn (startActivity)
    bannerManager.setReloadAds();

</pre>
<h4>Load in fragment</h4>
<pre>

Admob.getInstance().loadBannerFragment( mActivity, "bannerID",  rootView)

</pre>
</div>

<h3>- InterstitialAds</h3>
  <h4>Create and load interstitialAds</h4>
<pre>
  private InterstitialAd mInterstitialAd;

 Admob.getInstance().loadInterAdsFloor(context, ids, object : InterCallback() {
                override fun onAdLoadSuccess(interstitialAd: InterstitialAd?) {
                    super.onAdLoadSuccess(interstitialAd)
                    mInterstitial = interstitialAd
                }
            })
</pre>
<h4>Show interstitialAds</h4>
<pre>
   Admob.getInstance().setOpenActivityAfterShowInterAds(false)
            Admob.getInstance().showInterAds(context, mInterstitial, object : InterCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    mInterstitial = null;
                    Admob.getInstance().setOpenActivityAfterShowInterAds(true)
                    action()
                }

                override fun onAdClosedByUser() {
                    super.onAdClosedByUser()
                    Admob.getInstance().setOpenActivityAfterShowInterAds(true)
                    action()
                }
            })
</pre>
</div>

<h3>- RewardAds</h3>
<div class="content">
  <h4>Init RewardAds</h4>
<pre>  Admob.getInstance().initRewardAds(this,reward_id);</pre>
<h3>Show RewardAds</h3>
<pre>
    var isReward = false
 Admob.getInstance().showRewardAds(activity, object : RewardCallback {
                override fun onEarnedReward(rewardItem: RewardItem?) {
                    isReward = true;
                }

                override fun onAdClosed() {
                    if (isReward) {
                        action()
                    } else {
                        loadAdsReward(activity, id, remote)
                    }
                }
                override fun onAdFailedToShow(p0: Int) {
                    Log.e("ccccccc", "onAdFailedToShow: ")
                }

                override fun onAdImpression() {
                    Log.e("ccccccc", "onAdImpression: ")
                }

            })
</pre>


<h3>- NativeAds</h3>
<div class="content">
  <h4>View xml</h4>
<pre>

    <FrameLayout
        android:id="@+id/native_ads"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" >
        <include layout="@layout/ads_native_shimer" />

</pre>
<h3>Create and show nativeAds</h3>
<pre>

     private FrameLayout native_ads;
     
     native_ads = findViewById(R.id.native_ads);


     =========== OPTION 1 ===========
      Admob.getInstance().loadNativeAd(this, "native_id", new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                NativeAdView adView = ( NativeAdView) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_native, null);
                native_ads.addView(adView);
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
            }
        });
           @Override
                public void onAdFailedToLoad() {
                    fr_ads1.removeAllViews();
                }


     =========== OPTION 2 ==========
      Admob.getInstance().loadNativeAd(this, "id native", native_ads,R.layout.ads_native);
    Admob.getInstance().loadNativeAdFloor(this, List<String>, frameLayout,R.layout.ads_native);



      // trường hơp loaad native có reload 
          NativeBuilder builder = new NativeBuilder(this, binding.frAds,
                        R.layout.ads_shimmer_view_layout, R.layout.ads_native_list_pre_layout);
            builder.setListIdAd(List<String> ids);
              NativeManager nativeManager = new NativeManager(this, this, builder);
    // gọi hàm này khi có sự kiện chuyển màn (startActivity)
    nativeManager.setReloadAds();

</pre>

</div>


<h3>Load collapse banner</h3>
  <h4>View xml</h4>
<pre>< include
        android:id="@+id/include"
        layout="@layout/layout_banner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:layout_constraintBottom_toBottomOf="parent" /> 

 </pre>
<pre>
//load collapse banner khi không cần reload
 Admob.getInstance().loadCollapsibleBannerFloor(activity, this.getListIDCollapseBannerAll(), "bottom");

 //load collapse banner khi có reload
 Admob.getInstance().loadCollapsibleBannerFloorWithReload(this,listID,getLifecycle());
</pre>
<h3>Hide all ads</h3>
<pre>
//ẩn app open resume
 AppOpenManager.getInstance().disableAppResumeWithActivity(class);

// true - show all ads
 // false - hide all ads
 Admob.getInstance().setShowAllAds(true);
 
</pre>

</pre>
