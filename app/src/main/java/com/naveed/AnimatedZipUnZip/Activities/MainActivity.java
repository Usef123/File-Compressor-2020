package com.naveed.AnimatedZipUnZip.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.naveed.AnimatedZipUnZip.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.naveed.AnimatedZipUnZip.CustomDialogClass;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int [] img;
    private String[] name;
    private BoomMenuButton bmb;

    public static int count = 1;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bmb = findViewById(R.id.main_bmb);
        InitializeFields();
        CreateBoomMenu();
        reqNewInterstitial();
        refreshAd();
        verifyPermissions();


        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getDrawable(R.drawable.ic_launcher_foreground));

        materialDesignFAM =   findViewById(R.id.floatingactionmenu);
        floatingActionButton1 =  findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 =  findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 =  findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded() && count% 2 == 0) {

                    interstitialAd.show();

                } else {
                    reqNewInterstitial();
                    startActivity(new Intent(MainActivity.this , Compressed_Files_Activity.class));
                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        reqNewInterstitial();
                        startActivity(new Intent(MainActivity.this , Compressed_Files_Activity.class));
                    }
                });
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                if (interstitialAd.isLoaded() && count % 2 == 0) {
                    interstitialAd.show();
                } else {
                    reqNewInterstitial();
                    Uri uri = Uri.parse("market://details?id="+getApplicationContext().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
                    }

                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        reqNewInterstitial();
                        Uri uri = Uri.parse("market://details?id="+getApplicationContext().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
                        }
                    }
                });
                count++;
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked


                if (interstitialAd.isLoaded() && count % 2 == 0) {

                    interstitialAd.show();

                } else {
                    try {
                        reqNewInterstitial();
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.music.player.audio.booster.audio.player" + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        e.toString();
                    }

                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        try {
                            reqNewInterstitial();
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.music.player.audio.booster.audio.player" + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            e.toString();
                        }

                    }
                });
                count++;



            }
        });
    }
    public void reqNewInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial_ID));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void InitializeFields() {
        img = new int[]{
                R.drawable.storageicon,
                R.drawable.sdcard,
        };
        name = new String[]{
                "Device Storage",
                "SD Card Storage",
        };
    }



    private void CreateBoomMenu() {
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(img[i])
                    .normalText(name[i])
                    .normalColorRes(R.color.white)
                    .highlightedColorRes(R.color.colorPrimary)
                    .pieceColorRes(R.color.colorPrimary)
                    //  .pieceColor(Color.BLUE)
                    .shadowEffect(true)
                    .shadowOffsetX(20)
                    .shadowOffsetY(0)
                    .maxLines(2)
                    .imagePadding(new Rect(15,15,15,15))
                    .textGravity(Gravity.CENTER)
                    .textTopMargin(5)
                    .textSize(14)
                    // .textHeight(50)
                    .typeface(Typeface.DEFAULT_BOLD)
                    .rippleEffect(true)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            switch (index) {
                                case 0:

                                    if (interstitialAd.isLoaded()&& count% 2 == 0) {
                                        interstitialAd.show();
                                    } else {
                                        reqNewInterstitial();

                                        Intent subject_intent = new Intent(MainActivity.this, InternalStorageMain.class);
                                        startActivity(subject_intent);
                                        finish();

                                    }
                                    interstitialAd.setAdListener(new AdListener() {
                                        @Override
                                        public void onAdClosed() {

                                            reqNewInterstitial();
                                            Intent subject_intent = new Intent(MainActivity.this, InternalStorageMain.class);
                                            startActivity(subject_intent);
                                            finish();

                                        }
                                    });
                                    count++;
                                    break;
                                case 1:

                                    if (interstitialAd.isLoaded()&&count%2 == 0) {
                                        interstitialAd.show();
                                    } else {
                                        reqNewInterstitial();
                                        if(externalMemoryAvailable(MainActivity.this))
                                        {
                                            count++;
                                            Intent intent = new Intent(MainActivity.this, InternalStorageMain.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            count++;
                                            new SmartDialogBuilder(MainActivity.this)
                                                    .setTitle("Storage Check")
                                                    .setSubTitle("No SD Card Available")
                                                    .setCancalable(true)
                                                    .setNegativeButtonHide(true) //hide cancel button
                                                    .setPositiveButton("OK", new SmartDialogClickListener() {
                                                        @Override
                                                        public void onClick(SmartDialog smartDialog) {
                                                            smartDialog.dismiss();
                                                        }
                                                    }).build().show();
                                        }

                                    }
                                    interstitialAd.setAdListener(new AdListener() {
                                        @Override
                                        public void onAdClosed() {

                                            reqNewInterstitial();
                                            if(externalMemoryAvailable(MainActivity.this))
                                            {
                                                count++;
                                                Intent intent = new Intent(MainActivity.this, InternalStorageMain.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                count++;
                                                new SmartDialogBuilder(MainActivity.this)
                                                        .setTitle("Storage Check")
                                                        .setSubTitle("No SD Card Available")
                                                        .setCancalable(true)
                                                        .setNegativeButtonHide(true) //hide cancel button
                                                        .setPositiveButton("OK", new SmartDialogClickListener() {
                                                            @Override
                                                            public void onClick(SmartDialog smartDialog) {
                                                                smartDialog.dismiss();
                                                            }
                                                        }).build().show();
                                            }

                                        }
                                    });
                                    count++;
                                    break;


                            }
                        }
                    });
            bmb.addBuilder(builder);
        }
    }
    public static boolean externalMemoryAvailable(Activity context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null)
            {
            return true;
            }
        else
            return false;

    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded() && count% 2 == 0) {

            interstitialAd.show();

        } else {
            reqNewInterstitial();
            CustomDialogClass cdd=new CustomDialogClass(MainActivity.this);
            cdd.show();
        }
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                CustomDialogClass cdd=new CustomDialogClass(MainActivity.this);
                cdd.show();
            }
        });
        count++;
    }
    @Override
    protected void onResume() {
        super.onResume();
        count++;
        reqNewInterstitial();
    }



    private void refreshAd() {
        //refresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.native_id));

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                FrameLayout frameLayout =
                        findViewById(R.id.framelayout);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.nativeads, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //      refresh.setEnabled(true);
                Toast.makeText(MainActivity.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        //   videoStatus.setText("");
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.
                // refresh.setEnabled(true);
                // videoStatus.setText("Video status: Video playback has ended.");
                super.onVideoEnd();
            }
        });

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        ImageView mainImageView = adView.findViewById(R.id.ad_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
//            videoStatus.setText(String.format(Locale.getDefault(),
//                    "Video status: Ad contains a %.2f:1 video asset.",
//                    vc.getAspectRatio()));
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            // At least one image is guaranteed.
            List<NativeAd.Image> images = nativeAd.getImages();
            mainImageView.setImageDrawable(images.get(0).getDrawable());

//            refresh.setEnabled(true);
//            videoStatus.setText("Video status: Ad does not contain a video asset.");
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }

    private void verifyPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                    finish();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                //act according to the request code used while requesting the permission(s).
            }
        }
    }


}

