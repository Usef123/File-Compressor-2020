package com.naveed.AnimatedZipUnZip.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.List;

public class InternalStorageMain extends AppCompatActivity {

    String str_check_value, str_date, str_msg;
    private int[] img;
    private String[] name;
    private BoomMenuButton bmb;
    InterstitialAd interstitialAd;


    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    TextOutsideCircleButton.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internalstoragemain);
        bmb = findViewById(R.id.App_bmb);
        InitializeFields();
        CreateBoomMenu();
        reqNewInterstitial();
        refreshAd();

        materialDesignFAM = findViewById(R.id.floatingactionmenu_internalStorage);
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1_internalStorage);
        floatingActionButton2 = findViewById(R.id.material_design_floating_action_menu_item2_internalStorage);
        floatingActionButton3 = findViewById(R.id.material_design_floating_action_menu_item3_internalStorage);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.count++;
                if (interstitialAd.isLoaded() && MainActivity.count% 2 == 0) {

                    interstitialAd.show();

                } else {
                    reqNewInterstitial();
                    startActivity(new Intent(InternalStorageMain.this , Compressed_Files_Activity.class));
                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        reqNewInterstitial();
                        startActivity(new Intent(InternalStorageMain.this , Compressed_Files_Activity.class));
                    }
                });
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                MainActivity.count++;
                if (interstitialAd.isLoaded() && MainActivity.count % 2 == 0) {
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
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked

                MainActivity.count++;
                if (interstitialAd.isLoaded() && MainActivity.count % 2 == 0) {

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
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InternalStorageMain.this, MainActivity.class));
        finish();

    }

    private void InitializeFields() {
        img = new int[]{
                R.drawable.ppticon,
                R.drawable.docicon,
                R.drawable.txticon,
                R.drawable.jpgicon,
                R.drawable.mp4icon,
                R.drawable.mp3icon,
                R.drawable.apkicon,
                R.drawable.xlxsicon,
                R.drawable.pdficon
        };
        name = new String[]{
                "PPT Files",
                "Word Files",
                "Text Files",
                "All Images",
                "All Videos",
                "MP3 Files",
                "APK Files",
                "Excel Files",
                "PDF Files"
        };
    }

    private void CreateBoomMenu() {
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(img[i])
                    .normalText(name[i])
                    .normalColorRes(R.color.white)
                    .highlightedColorRes(R.color.colorPrimary)
                    .pieceColorRes(R.color.colorPrimary)
                    //  .pieceColor(Color.BLUE)
                    .shadowEffect(true)
                    .shadowOffsetX(20)
                    .imagePadding(new Rect(10,10,10,10))
                    .shadowOffsetY(0)
                    .maxLines(2)
                    .textGravity(Gravity.CENTER)
                    .textTopMargin(5)
                    .textSize(14)
                    // .textHeight(50)
                    .typeface(Typeface.DEFAULT_BOLD)
                    .rippleEffect(true).listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            switch (index) {
                                case 0:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.pptfiles));
                                    break;
                                case 1:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.docsfiles));
                                    break;
                                case 2:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.txt));
                                    break;
                                case 3:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.image));
                                    break;
                                case 4:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.video));
                                    break;
                                case 5:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.audio));
                                    break;
                                case 6:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.apk));
                                    break;
                                case 7:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.xlxs));
                                    break;
                                case 8:
                                    MainActivity.count++;
                                    movetoShowAllCategoriesfilesActivity(getResources().getString(R.string.pdffiles));
                                    break;
                            }
                        }
                    });

            bmb.addBuilder(builder);
        }
    }

    public void movetoShowAllCategoriesfilesActivity(final String name) {
        if (interstitialAd.isLoaded() && MainActivity.count% 3 == 0) {

            interstitialAd.show();

        } else {
            reqNewInterstitial();
            Intent intent8 = new Intent(InternalStorageMain.this, ShowAllCategoriesFiles.class);
            intent8.putExtra("category", name);
            intent8.putExtra("loc", "");
            startActivity(intent8);
        }

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();

                Intent intent8 = new Intent(InternalStorageMain.this, ShowAllCategoriesFiles.class);
                intent8.putExtra("category", name);
                intent8.putExtra("loc", "");
                startActivity(intent8);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.count++;
        reqNewInterstitial();
    }
    public void reqNewInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial_ID));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    private void refreshAd() {
        //refresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.native_id));

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                FrameLayout frameLayout =
                        findViewById(R.id.framelayout2);
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
                Toast.makeText(InternalStorageMain.this, "Failed to load native ad: "
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

}
