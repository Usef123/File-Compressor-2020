package com.naveed.AnimatedZipUnZip.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.naveed.AnimatedZipUnZip.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;
import com.naveed.AnimatedZipUnZip.Adapters.Adapter_DeviceStorage;
import com.naveed.AnimatedZipUnZip.Adapters.Adapter_ShowAllFiles;
import com.naveed.AnimatedZipUnZip.Models.FilesModelClass;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.wang.avi.AVLoadingIndicatorView;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import ir.mahdi.mzip.zip.ZipArchive;

public class ShowAllCategoriesFiles extends AppCompatActivity implements Adapter_ShowAllFiles.Onfolderlistner ,
        Adapter_DeviceStorage.Onfolderlistner, Adapter_ShowAllFiles.Onfolderlistner1 {


    RecyclerView recyclerView;
    Adapter_ShowAllFiles adapter;
    Adapter_DeviceStorage ISadapter;
    String name;
    static SmartDialogBuilder dialogBuilder1;

    MenuItem archive;

    public static final File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "ZipUnZip");

    ArrayList<FilesModelClass> FilesModelClassObjectList;
    ArrayList<String> LocationArrayList_SelectedItems;
    ArrayList<FilesModelClass> SelectedItemArrayList;

    AVLoadingIndicatorView progressBar;
    String backPathOfstorage;
    File root;
    View bottomsheetLayout;
    View bottomsheetmakezipfiles;

    Button cancelbtn, extractbtn;
    Button cancelbtnzip, addfiletozipbtn;
    //FolderListAdapter adapter;
    String category;
    InterstitialAd interstitialAd;

    public static int counter = 0;
    int colorDailogbtn = 0;
    String location;
    AVLoadingIndicatorView avi;
    Color color;

    public void reqNewInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial_ID));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }
    AdView adView1;

    private void BannerAd() {
        adView1 = findViewById(R.id.adViewlight);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_categories_files);
        recyclerView = findViewById(R.id.allfiles_RecyclerView);
        progressBar = findViewById(R.id.allfilesProgressbar);
        bottomsheetLayout = findViewById(R.id.bottomsheetlayout);
        bottomsheetmakezipfiles = findViewById(R.id.dialogmakezipfile);
        cancelbtn = findViewById(R.id.cancelExtracting);
        extractbtn = findViewById(R.id.startextracting);
        cancelbtnzip = findViewById(R.id.cancelzipfilebtn);
        addfiletozipbtn = findViewById(R.id.makezipfilebtn);
        avi = findViewById(R.id.avidisplayallfiles);
        avi.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        avi.hide();
        reqNewInterstitial();
        BannerAd();
        LocationArrayList_SelectedItems= new ArrayList<>();
        SelectedItemArrayList = new ArrayList<>();

        Intent i = getIntent();
        category = i.getStringExtra("category");
        location = i.getStringExtra("loc");
        if (location.equals("")) {
            bottomsheetLayout.setVisibility(View.GONE);
        } else {
            bottomsheetLayout.setVisibility(View.VISIBLE);
        }
        FilesModelClassObjectList = new ArrayList<>();
        FilesModelClassObjectList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.displayallfilestoolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switch (category) {
            case "PPT Files":
                FilesModelClassObjectList = new ArrayList<>();
                new PPTAsyncTask().execute();
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_pptbar));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonppt));
                progressBar.setIndicatorColor(getResources().getColor(R.color.PPT));
                colorDailogbtn = getResources().getColor(R.color.PPT);

                break;
            case "Word Files":
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_wordbar));
                FilesModelClassObjectList = new ArrayList<>();
                progressBar.setIndicatorColor(getResources().getColor(R.color.DOCX));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonword));
                new WordAsyncTask().execute();
                colorDailogbtn = getResources().getColor(R.color.DOCX);
                break;
            case "PDF Files":
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_pdf));
                FilesModelClassObjectList = new ArrayList<>();
                progressBar.setIndicatorColor(getResources().getColor(R.color.PDF));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonpdf));
                colorDailogbtn = getResources().getColor(R.color.PDF);
                new PDfAsyncTask().execute();
                break;
            case "Excel Files":
                FilesModelClassObjectList = new ArrayList<>();
                new XLXSAsyncTask().execute();
                progressBar.setIndicatorColor(getResources().getColor(R.color.EXCEL));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonexcel));
                colorDailogbtn = getResources().getColor(R.color.EXCEL);
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_excelbar));
                break;
            case "Audio Files":
                AddingAllAudiostoRecyclerView();
                progressBar.setIndicatorColor(getResources().getColor(R.color.AUDIO));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonaudio));
                colorDailogbtn = getResources().getColor(R.color.AUDIO);
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_audiobar));
                break;
            case "Video Files":
                AddingVideosToRecyclerView();
                progressBar.setIndicatorColor(getResources().getColor(R.color.MP4));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonvideo));
                colorDailogbtn = getResources().getColor(R.color.MP4);
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_videobar));
                break;
            case "Image Files":
                AddingImagesToRecyclerView();
                progressBar.setIndicatorColor(getResources().getColor(R.color.IMAGE));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonimages));
                colorDailogbtn = getResources().getColor(R.color.IMAGE);
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_imagesbar));
                break;
            case "APK Files":
                FilesModelClassObjectList = new ArrayList<>();
                progressBar.setIndicatorColor(getResources().getColor(R.color.APK));
                addfiletozipbtn.setBackground(getResources().getDrawable(R.drawable.bgbuttonapk));
                colorDailogbtn = getResources().getColor(R.color.APK);
                new APKAsyncTask().execute();
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_apkbar));
                break;
            case "Text Files":
                FilesModelClassObjectList = new ArrayList<>();
                new TEXTAsyncTask().execute();
                colorDailogbtn = getResources().getColor(R.color.TEXT);
                addfiletozipbtn.setBackground(getDrawable(R.drawable.bgbuttontext));
                progressBar.setIndicatorColor(getResources().getColor(R.color.TEXT));
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_txtbar));
                break;
            case "Internal Storage":
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#874D7D")));
                AccessingInternalStorageFiles();
                break;

        }

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheetLayout.setVisibility(View.GONE);
            }
        });

        extractbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ExtractZipAsynctask().execute();
                ISadapter.notifyDataSetChanged();
                bottomsheetLayout.setVisibility(View.GONE);

            }
        });

        cancelbtnzip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheetmakezipfiles.setVisibility(View.GONE);
            }
        });
        addfiletozipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FlatDialog flatDialog = new FlatDialog(ShowAllCategoriesFiles.this);
                flatDialog.setTitle("Please Enter File Name")
                        .setTitleColor(getResources().getColor(R.color.grayas))
                        .setFirstTextFieldHint("e.g ZipFileImage")
                        .setFirstTextFieldHintColor(getResources().getColor(R.color.grayas))
                        .setFirstTextFieldBorderColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setFirstButtonText("Ok")
                        .setFirstTextFieldTextColor(getResources().getColor(R.color.grayas))
                        .setSecondButtonText("Cancel")
                        .setBackgroundColor(getResources().getColor(R.color.white))
                        .setFirstButtonColor(colorDailogbtn)
                        .setSecondButtonColor(colorDailogbtn)
                        .withFirstButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Please Wait Do Not Close App", Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                        .show();



                                flatDialog.dismiss();
                                progressBar.hide();
                                name = flatDialog.getFirstTextField();
                                avi.hide();
                                new CreateZipAsynTask().execute();
                            }
                        }).withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flatDialog.dismiss();
                        progressBar.hide();
                        recyclerView.setAlpha(1);
                        avi.hide();
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        counter++;
        reqNewInterstitial();


    }
    void UnselectItems() {
        for (int i = 0 ; i< SelectedItemArrayList.size() ; i++)
        {
            SelectedItemArrayList.get(i).setSelected(false);
        }
    }

    @Override
    public void onClickListnerfolder(int position) {
        File file = new File(FilesModelClassObjectList.get(position).getLocation());
        backPathOfstorage = file.getAbsolutePath();
        if (file.isDirectory()) {
            WalkThroughInternalStorage(file);
            ISadapter.notifyDataSetChanged();
        }
    }

    private class CreateZipAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setClickable(true);
            recyclerView.setAlpha((float) 0.2);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if (LocationArrayList_SelectedItems.size() > 0) {
                        try {
                            zipper(LocationArrayList_SelectedItems, name);

                            new SmartDialogBuilder(ShowAllCategoriesFiles.this)
                                    .setTitle("Successfully Zipped")
                                    .setSubTitle("Please Check Folder With App name In Internal Storage \n\n" +
                                            Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name))
                                    .setCancalable(false)
                                    .setNegativeButtonHide(true) //hide cancel button
                                    .setPositiveButton("OK", new SmartDialogClickListener() {
                                        @Override
                                        public void onClick(SmartDialog smartDialog) {
                                            smartDialog.dismiss();
                                            progressBar.hide();
                                            recyclerView.setAlpha(1);
                                            UnselectItems();
                                            adapter.notifyDataSetChanged();
                                        }
                                    }).build().show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                    {
                        new SmartDialogBuilder(ShowAllCategoriesFiles.this)
                                .setTitle("Zip Failed")
                                .setSubTitle("Please Select File ")
                                .setCancalable(false)
                                .setNegativeButtonHide(true) //hide cancel button
                                .setPositiveButton("OK", new SmartDialogClickListener() {
                                    @Override
                                    public void onClick(SmartDialog smartDialog) {
                                        smartDialog.dismiss();
                                    }
                                }).build().show();
                    }

                }
            }, 2000);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingSelectedItemList();
            return null;
        }
    }

    private class ExtractZipAsynctask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            avi.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    avi.hide();
                    dialogBuilder1 = new SmartDialogBuilder(ShowAllCategoriesFiles.this);

                            dialogBuilder1.setTitle("Successfully UnZipped")
                            .setCancalable(true)
                            .setNegativeButtonHide(true) //hide cancel button
                            .setPositiveButton("OK", new SmartDialogClickListener() {
                                @Override
                                public void onClick(SmartDialog smartDialog) {

                                    smartDialog.dismiss();
                                    Intent i = new Intent(ShowAllCategoriesFiles.this , InternalStorageMain.class);
                                    i.putExtra("s_n","Internal Storage");
                                    startActivity(i);
                                }
                            }).build().show();
                    recyclerView.setAlpha(1);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ShowAllCategoriesFiles.this, "Successfull Unziped", Toast.LENGTH_LONG).show();
                }
            }, 2000);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            ZipArchive zipArchive = new ZipArchive();
            zipArchive.unzip(location, backPathOfstorage, "");
            return null;
        }
    }

    private void AccessingInternalStorageFiles() {

        FilesModelClassObjectList = new ArrayList<>();
        root = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        backPathOfstorage = root.getAbsolutePath();
        WalkThroughInternalStorage(root);
        progressBar.hide();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        ISadapter = new Adapter_DeviceStorage(FilesModelClassObjectList, this, this);
        recyclerView.setAdapter(ISadapter);
    }

    public void WalkThroughInternalStorage(File f) {
        File[] listFile = f.listFiles();


        if (listFile != null) {
            FilesModelClassObjectList.clear();
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    FilesModelClass objects = new FilesModelClass();

                    objects.setName(listFile[i].getName());
                    objects.setLocation(listFile[i].getAbsolutePath());
                    objects.setType(getfileExtension(listFile[i]));
                    objects.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.storage));
                    FilesModelClassObjectList.add(objects);

                } else if (listFile[i].getName().endsWith(".pdf")) {
                    //Do what ever u want
                    FilesModelClass object = new FilesModelClass();
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.pdffile));

                    object.setType(".pdf");
                    FilesModelClassObjectList.add(object);

                } else if (listFile[i].getName().endsWith(".docx")) {

                    FilesModelClass object = new FilesModelClass();
                    object.setName(listFile[i].getName().toString());
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.docfile));
                    object.setLocation(listFile[i].getAbsolutePath());
                    object.setType(".docx");
                    FilesModelClassObjectList.add(object);
                } else if (listFile[i].getName().endsWith(".txt")) {

                    FilesModelClass object = new FilesModelClass();
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.txtfile));
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    object.setType(".txt");
                    FilesModelClassObjectList.add(object);

                } else if (listFile[i].getName().endsWith(".mp4") ||
                        listFile[i].getName().endsWith(".mkv")) {

                    FilesModelClass object = new FilesModelClass();
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.mp4file));
                    object.setType("mp4");
                    FilesModelClassObjectList.add(object);
                } else if (listFile[i].getName().endsWith(".mp3")) {
                    FilesModelClass object = new FilesModelClass();
                    object.setName(listFile[i].getName().toString());
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.mp3icon));
                    object.setLocation(listFile[i].getAbsolutePath());
                    object.setType(".mp3");
                    FilesModelClassObjectList.add(object);
                } else if (listFile[i].getName().endsWith(".apk")) {
                    FilesModelClass object = new FilesModelClass();
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.apkfile));
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    object.setType(".apk");
                    //object.setSize(String.valueOf(listFile[i].length()/1024));
                    FilesModelClassObjectList.add(object);
                } else if (listFile[i].getName().endsWith(".jpg") || listFile[i].getName().endsWith(".jpeg")
                        || listFile[i].getName().endsWith(".png")) {

                    FilesModelClass object = new FilesModelClass();
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    //  object.setSize(String.valueOf(listFile[i].length()/1024));
                    object.setType("image");
                    FilesModelClassObjectList.add(object);
                } else if (listFile[i].getName().endsWith(".pptx") || listFile[i].getName().endsWith(".ppt")) {
                    FilesModelClass object = new FilesModelClass();
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.pptfile));
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    // object.setSize(String.valueOf(listFile[i].length()/1024));
                    object.setType(".pptx");
                    FilesModelClassObjectList.add(object);
                } else if (listFile[i].getName().endsWith(".rar") || listFile[i].getName().endsWith(".zip")) {
                    FilesModelClass object = new FilesModelClass();
                    object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.zipfile));
                    object.setName(listFile[i].getName().toString());
                    object.setLocation(listFile[i].getAbsolutePath());
                    // object.setSize(String.valueOf(listFile[i].length()/1024));
                    object.setType(".pptx");
                    FilesModelClassObjectList.add(object);
                }


            }
        }
    }

    private String getfileExtension(File file) {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getFileExtensionFromUrl(contentResolver.getType(Uri.fromFile(file)));
        return extension;
    }

    public void GettingSelectedItemList() {
        for (int i = 0 ; i < FilesModelClassObjectList.size() ; i++)
        {
            if (FilesModelClassObjectList.get(i).isSelected())
            {
                LocationArrayList_SelectedItems.add(FilesModelClassObjectList.get(i).getLocation());
                SelectedItemArrayList.add(FilesModelClassObjectList.get(i));
            }
        }
    }

    @Override
    public void onClickListner1(int position) {
        File file = new File(FilesModelClassObjectList.get(position).getLocation());
        backPathOfstorage = file.getAbsolutePath();
        if (file.isDirectory()) {
            WalkThroughInternalStorage(file);
            adapter.notifyDataSetChanged();
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    private class ImagesAsynctask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            progressBar.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    progressBar.hide();

                }
            }, 500);
            super.onPostExecute(s);


        }

        @Override
        protected String doInBackground(String... strings) {

            GettingImagesFromStorage();
            return null;
        }
    }

    public void GettingImagesFromStorage() {

        ContentResolver contentResolver = Objects.requireNonNull(getContentResolver());

        Uri songsUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


        Cursor songCursor = contentResolver.query(songsUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int name = songCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            int location = songCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int sizes = songCursor.getColumnIndex(MediaStore.Images.Media.SIZE);


            do {
                String currentTitle = songCursor.getString(name);
                String Location = songCursor.getString(location);
                String size = songCursor.getString(sizes);
                FilesModelClass object = new FilesModelClass();
                object.setLocation(Location);
                object.setName(currentTitle);
                FilesModelClassObjectList.add(object);
            } while (songCursor.moveToNext());
        }
    }

    private void AddingImagesToRecyclerView() {
        FilesModelClassObjectList = new ArrayList<>();
        new ImagesAsynctask().execute();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "image");
        recyclerView.setAdapter(adapter);
    }

    ////////////////////////////////////////////////////////////////////////
    private class videoAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            progressBar.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms

                    progressBar.hide();
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllVideosFromStorage();
            return null;
        }
    }

    public void GettingAllVideosFromStorage() {
        ContentResolver contentResolver = getContentResolver();

        Uri songsUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songsUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songtitle = songCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int songlocation = songCursor.getColumnIndex(MediaStore.Video.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songtitle);
                String currentLocation = songCursor.getString(songlocation);
                FilesModelClass object = new FilesModelClass();
                object.setName(currentTitle);
                object.setLocation(currentLocation);
                FilesModelClassObjectList.add(object);
            } while (songCursor.moveToNext());
        }
    }

    private void AddingVideosToRecyclerView() {


        FilesModelClassObjectList = new ArrayList<>();
        new videoAsyncTask().execute();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "image");
        recyclerView.setAdapter(adapter);
    }

    //////////////////////////////////////////////////////////////////
    private class audioAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllAudiosFromStorage();
            return null;
        }
    }

    public void GettingAllAudiosFromStorage() {
        ContentResolver contentResolver = getContentResolver();

        Uri songsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songsUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songtitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int songlocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songtitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songlocation);
                String currentid = songCursor.getString(0);

                FilesModelClass object = new FilesModelClass();
                object.setLocation(currentLocation);
                object.setName(currentTitle);
                object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.mp3file));
                FilesModelClassObjectList.add(object);


            } while (songCursor.moveToNext());
        }


    }

    private void AddingAllAudiostoRecyclerView() {
        FilesModelClassObjectList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        new audioAsyncTask().execute();

    }

    ///////////////////////////////////////////////////////////////
    private void AddingAllPDFtoRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        recyclerView.setAdapter(adapter);
    }

    public void GettingAllPdfFromStorage(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    GettingAllPdfFromStorage(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want
                        FilesModelClass object = new FilesModelClass();
                        object.setName(listFile[i].getName().toString());
                        object.setLocation(listFile[i].getAbsolutePath());
                        object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.pdffile));
                        // object.setSize(String.valueOf(listFile[i].length() / 1024));
                        FilesModelClassObjectList.add(object);


                    }
                }
            }
        }
    }

    private class PDfAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddingAllPDFtoRecyclerView();
                    progressBar.setVisibility(View.GONE);
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllPdfFromStorage(Environment.getExternalStorageDirectory());
            return null;
        }
    }

    ////////////////////////////////////////////
    public void GettingAllWordFromStorage(File dir) {
        String pdfPattern = ".docx";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    GettingAllWordFromStorage(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want
                        FilesModelClass object = new FilesModelClass();
                        object.setName(listFile[i].getName().toString());
                        object.setLocation(listFile[i].getAbsolutePath());
                        object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.docfile));
                        // object.setSize(String.valueOf(listFile[i].length() / 1024));
                        FilesModelClassObjectList.add(object);

                    }
                }
            }
        }
    }

    private void AddingAllWordTORecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        recyclerView.setAdapter(adapter);
    }

    private class WordAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddingAllWordTORecyclerView();
                    progressBar.setVisibility(View.GONE);
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllWordFromStorage(Environment.getExternalStorageDirectory());
            return null;
        }
    }

    /////////////////////////////////////////////////////////////////////
    public void GettingAllPPTTORecyclerView(File dir) {
        String pdfPattern = ".pptx";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    GettingAllPPTTORecyclerView(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want
                        FilesModelClass object = new FilesModelClass();
                        object.setName(listFile[i].getName().toString());
                        object.setLocation(listFile[i].getAbsolutePath());
                        object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.pptfile));
                        // object.setSize(String.valueOf(listFile[i].length() / 1024));
                        FilesModelClassObjectList.add(object);

                    }
                }
            }
        }
    }

    private void AddingAllPPTTORecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        recyclerView.setAdapter(adapter);

    }

    private class PPTAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddingAllPPTTORecyclerView();
                    progressBar.setVisibility(View.GONE);
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllPPTTORecyclerView(Environment.getExternalStorageDirectory());
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    public void GettingAllTextTORecyclerView(File dir) {
        String pdfPattern = ".txt";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    GettingAllTextTORecyclerView(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want
                        FilesModelClass object = new FilesModelClass();
                        object.setName(listFile[i].getName().toString());
                        object.setLocation(listFile[i].getAbsolutePath());
                        object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.txtfile));
                        // object.setSize(String.valueOf(listFile[i].length() / 1024));
                        FilesModelClassObjectList.add(object);

                    }
                }
            }
        }
    }

    private void AddingAllTextToRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        recyclerView.setAdapter(adapter);
    }

    private class TEXTAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddingAllTextToRecyclerview();
                    progressBar.setVisibility(View.GONE);
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            GettingAllTextTORecyclerView(Environment.getExternalStorageDirectory());
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////
    public void GettingAllAPKfromStorage(File dir) {
        String pdfPattern = ".apk";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    GettingAllAPKfromStorage(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want
                        FilesModelClass object = new FilesModelClass();
                        object.setName(listFile[i].getName().toString());
                        object.setLocation(listFile[i].getAbsolutePath());
                        object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.apkfile));
                        // object.setSize(String.valueOf(listFile[i].length() / 1024));
                        FilesModelClassObjectList.add(object);

                    }
                }
            }
        }
    }

    private void AddingAllAPKtoRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        recyclerView.setAdapter(adapter);
    }

    private class APKAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AddingAllAPKtoRecyclerview();
                    progressBar.setVisibility(View.GONE);
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllAPKfromStorage(Environment.getExternalStorageDirectory());
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////
    public void GettingAllExcelFilesFromStorage(File dir) {
        String pdfPattern = ".xlxs";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    GettingAllExcelFilesFromStorage(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want
                        FilesModelClass object = new FilesModelClass();
                        object.setName(listFile[i].getName().toString());
                        object.setLocation(listFile[i].getAbsolutePath());
                        object.setItemIcon(ContextCompat.getDrawable(ShowAllCategoriesFiles.this, R.drawable.xlxsfile));
                        // object.setSize(String.valueOf(listFile[i].length() / 1024));
                        FilesModelClassObjectList.add(object);

                    }
                }
            }
        }
    }

    private void getAllXLXS() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ShowAllFiles(FilesModelClassObjectList, this, this, "");
        recyclerView.setAdapter(adapter);
    }

    private class XLXSAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    getAllXLXS();
                }
            }, 500);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            GettingAllExcelFilesFromStorage(Environment.getExternalStorageDirectory());
            return null;
        }
    }

    /////////////////////////////////////////////////////////////////////
    @Override
    public void onClickListner(int position) {

        Toast.makeText(this, FilesModelClassObjectList.get(position).getName(), Toast.LENGTH_SHORT).show();


    }


    public String zipper(ArrayList<String> allFiles, String zipFileName) throws IOException {

        String timeStampOfZipFile = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        mediaStorageDir.mkdirs();
        String zippath = mediaStorageDir.getAbsolutePath() + "/" + zipFileName + timeStampOfZipFile + ".zip";
        try {
            if (new File(zippath).exists()) {
                new File(zippath).delete();
            }
            //new File(zipFileName).delete(); // Delete if exists
            ZipFile zipFile = new ZipFile(zippath);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if (allFiles.size() > 0) {
                for (String fileName : allFiles) {
                    File file = new File(fileName);
                    zipFile.addFile(file, zipParameters);
                }
            }


        } catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }
        return zippath;
    }


    @Override
    public void onBackPressed() {
        if (category.equals("Internal Storage")) {

            if (backPathOfstorage.equals(root.getAbsolutePath())) {
                super.onBackPressed();
            } else {

                backPathOfstorage = backPathOfstorage.substring(0, backPathOfstorage.lastIndexOf("/"));
                File f = new File(backPathOfstorage);
                getSupportActionBar().setTitle(f.getName());
                WalkThroughInternalStorage(f);
                ISadapter.notifyDataSetChanged();
            }
        } else {
            super.onBackPressed();
            reqNewInterstitial();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (category.equals("Internal Storage")) {

        }
        else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.toolbar_menu, menu);
            archive = menu.findItem(R.id.search_option);

            archive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    bottomsheetmakezipfiles.setVisibility(View.VISIBLE);
                    //Toast.makeText(DisplayAllFilesActivity.this,, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        return true;
    }
}

