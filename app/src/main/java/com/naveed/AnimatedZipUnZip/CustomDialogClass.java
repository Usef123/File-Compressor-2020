package com.naveed.AnimatedZipUnZip;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.naveed.AnimatedZipUnZip.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CustomDialogClass extends Dialog implements
        View.OnClickListener {



    public Activity activity;
    public Dialog d;
    public Button yes, no;
    SmileRating smileRating;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }
    AdView adView1;

    private void BannerAd() {
        adView1 = findViewById(R.id.adViewcustomdialog);
        AdRequest adrequest = new AdRequest.Builder()
                .build();
        adView1.loadAd(adrequest);
        adView1.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                adView1.setVisibility(View.GONE);
            }

        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_box);
        BannerAd();
        yes = findViewById(R.id.btn_maybelater);
        no = (Button) findViewById(R.id.btn_never);
        smileRating = findViewById(R.id.ratingView);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Okay");
                        Uri uri = Uri.parse("market://details?id="+ activity.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            activity.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="+activity.getApplicationContext().getPackageName())));
                        }
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Okay");
                        Uri uri1 = Uri.parse("market://details?id="+ activity.getPackageName());
                        Intent goToMarket1 = new Intent(Intent.ACTION_VIEW, uri1);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            activity.startActivity(goToMarket1);
                        } catch (ActivityNotFoundException e) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="+activity.getApplicationContext().getPackageName())));
                        }
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_maybelater:
                dismiss();
                break;
            case R.id.btn_never:
                activity.finish();
                break;
            default:
                break;
        }
        dismiss();
    }
}