package com.com4fun.sdk_demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.ms.monetize.ads.Ad;
import com.ms.monetize.ads.AdError;
import com.ms.monetize.ads.AdListener;
import com.ms.monetize.ads.AdRequest;
import com.ms.monetize.ads.BannerAdView;
import com.ms.monetize.ads.InterstitialAd;
import com.ms.monetize.ads.VideoAd;
import com.ms.sdk.pf.PFKey;
import com.ms.sdk.pf.PFSDK;
import com.ms.sdk.pf.api.CallBack;
import com.ms.sdk.pf.api.PFAPI;
import com.ms.sdk.pf.bean.PayResult;
import com.ms.sdk.pf.bean.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView tvHint;
    LinearLayout llScrollContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHint = findViewById(R.id.tv_hint);
        llScrollContent = findViewById(R.id.ll_scroll_content);
    }

    private PFAPI pfapi;

    public void init(View view) {
        Map<String, String> params = new HashMap<>();
        params.put(PFKey.APP_ID, "200100");
        showProgress("Initing ...");
        PFSDK.init(getApplication(), params, new CallBack<PFAPI>() {
            @Override
            public void onSuccess(PFAPI pfapi) {
                toast("api init success");
                MainActivity.this.pfapi = pfapi;
                dismissProgress();
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("api init fail:" + msg);
                dismissProgress();
            }
        });
    }

    public void login(View view) {
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }
        showProgress("Login ...");
        tvHint.setText("");
        pfapi.login(this, null, new CallBack<User>() {
            @Override
            public void onSuccess(User user) {
                toast("login success");
                log("Demo", "login :" + Json.toJson(user));
                dismissProgress();
                showUser(user);
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("login fail:" + msg);
                dismissProgress();
            }
        });

    }

    public void logout(View view) {
        tvHint.setText("");
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }

        pfapi.logout(this, null, new CallBack<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                toast("logout success");
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("logout fail:" + msg);
            }
        });
    }

    public void pay(View view) {
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(PFKey.AMOUNT, "1");
        User user = pfapi.getCurrentUser();
        params.put(PFKey.UID, user == null ? null : user.getUId());
        showProgress("Paying ...");
        pfapi.pay(this, params, new CallBack<PayResult>() {
            @Override
            public void onSuccess(PayResult payResult) {
                toast("pay success");
                showPayResult(payResult);
                dismissProgress();
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("pay fail:" + msg);
                dismissProgress();
            }
        });
    }

    void showUser(User user) {
        StringBuilder sb = new StringBuilder();
        tvHint.setText("");
        if (user != null) {
            sb.append("id: ").append(user.getUId());
            sb.append("\nemail: ").append(user.getEmail());
            sb.append("\navatar: ").append(user.getAvatar());
            sb.append("\nfrom: ").append(user.getFrom());
            sb.append("\nusername: ").append(user.getUsername());
        } else {
            sb.append("Login fail");
        }
        tvHint.setText(sb.toString());
    }

    private void showPayResult(PayResult payResult) {
        StringBuilder sb = new StringBuilder();
        tvHint.setText("");
        sb.append("PayResult:" + payResult);
        if (payResult != null) {
            sb.append("\nuid: ").append(payResult.getUid());
            sb.append("\nsdk_order_id: ").append(payResult.getSdkOrderId());
            sb.append("\namount: ").append(payResult.getAmount());
            sb.append("\nstate: ").append(payResult.getState());
            sb.append("\npayload: ").append(payResult.getPayload());

        }

        tvHint.setText(sb.toString());
    }

    private void log(String tag, String text){
        tvHint.setText(tag + ":" + text);
    }

    BannerAdView bannerAdView;

    public void loadBanner(View view) {
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }
        bannerAdView = new BannerAdView(view.getContext());
        bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdError(Ad ad, AdError adError) {
                log(TAG, adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                log(TAG, "onAdLoaded");
                llScrollContent.addView(bannerAdView);
            }

            @Override
            public void onAdShowed(Ad ad) {
                log(TAG, "onAdShowed");
            }

            @Override
            public void onAdClosed(Ad ad) {
                log(TAG, "onAdClosed");
            }

            @Override
            public void onAdClicked(Ad ad) {
                log(TAG, "onAdClicked");
            }
        });
        bannerAdView.loadAd(AdRequest.newBuilder().pid("banner@justlucky").build());

    }

    private InterstitialAd interstitialAd;
    public void loadInterstitial(View view) {
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }
        interstitialAd = new InterstitialAd(view.getContext());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdError(Ad ad, AdError adError) {
                log(TAG, adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                log(TAG, "onAdLoaded");
                interstitialAd.show();
            }

            @Override
            public void onAdShowed(Ad ad) {
                log(TAG, "onAdShowed");
            }

            @Override
            public void onAdClosed(Ad ad) {
                log(TAG, "onAdClosed");
            }

            @Override
            public void onAdClicked(Ad ad) {
                log(TAG, "onAdClicked");
            }
        });
        interstitialAd.loadAd(AdRequest.newBuilder().pid("interstitial@justlucky").build());
    }

    public void loadNative(View view) {
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }

    }

    private VideoAd videoAd;
    public void loadVideo(View view){
        if (pfapi == null) {
            toast("shoud init first");
            return;
        }
        videoAd = new VideoAd(view.getContext(), "juslucky@rewardVideo", "12345");
        videoAd.init();
        videoAd.loadAd(new AdListener() {
            @Override
            public void onAdError(Ad ad, AdError adError) {
                log(TAG, "onAdError：" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                log(TAG, "onAdLoaded");
                videoAd.show();
            }

            @Override
            public void onAdShowed(Ad ad) {
                log(TAG, "onAdShowed");
            }

            @Override
            public void onAdClosed(Ad ad) {
                log(TAG, "onAdClosed");
            }

            @Override
            public void onAdClicked(Ad ad) {
                log(TAG, "onAdClicked");
            }
        });
    }
}
