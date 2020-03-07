package com.naveed.AnimatedZipUnZip.Activities;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.naveed.AnimatedZipUnZip.R;
import com.google.android.gms.ads.InterstitialAd;
import com.naveed.AnimatedZipUnZip.Adapters.Adapter_ZipFiles;
import com.naveed.AnimatedZipUnZip.Models.FilesModelClass;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.wang.avi.AVLoadingIndicatorView;

import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import ir.mahdi.mzip.zip.ZipArchive;

public class Compressed_Files_Activity extends AppCompatActivity implements Adapter_ZipFiles.interfaceCLicklistner {

    RecyclerView recyclerView;
    ArrayList<FilesModelClass> FilesModelClassObjectList;
    Adapter_ZipFiles adapter;
    AVLoadingIndicatorView progressBar;
    View layoutdialog;
    boolean checkdoInbackground = false;
    File zipFile , targetDirectory;
    String zipfilelocation , targetdirectorylocation;

    AVLoadingIndicatorView avi;
    GetAllZipFilesAsynctask loadZipFiles;
    int pos = 0;
    InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressed__files_);

        Toolbar toolbar = findViewById(R.id.coompressedfilesTOolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Compressed Files");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_actionbar));


        recyclerView = findViewById(R.id.zipfiles_RecyclerView);
        progressBar = findViewById(R.id.progressbarrzipfiles);
        avi = findViewById(R.id.avi);
        avi.setIndicatorColor(getResources().getColor(R.color.zip));
        progressBar.setIndicatorColor(getResources().getColor(R.color.zip));
        avi.hide();

        loadZipFiles = new GetAllZipFilesAsynctask();
        loadZipFiles.execute();
    }

    @Override
    public void onStop() {
        if(loadZipFiles != null && loadZipFiles.getStatus() == AsyncTask.Status.RUNNING) {
            loadZipFiles.cancel(true);
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(loadZipFiles != null && loadZipFiles.getStatus() == AsyncTask.Status.RUNNING) {
            loadZipFiles.cancel(true);
        }
        super.onBackPressed();
    }


    public class GetAllZipFilesAsynctask extends AsyncTask<String,String,String> {


        @Override
        protected void onPreExecute() {
            FilesModelClassObjectList = new ArrayList<>();
            progressBar.setVisibility(View.VISIBLE);
            checkdoInbackground = true;
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    progressBar.setVisibility(View.GONE );
                    getAllCompressedFiles();
                }
            }, 1000);
            super.onPostExecute(s);
        }@Override
        protected String doInBackground(String... strings) {
            if(checkdoInbackground) {
                walkdirRAR(Environment.getExternalStorageDirectory());
            }
            return null;
        }
    }



    private class UnZipAsynctask extends AsyncTask<String,String,String> {

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
                    new SmartDialogBuilder(Compressed_Files_Activity.this)
                            .setTitle("Successfully UnZipped")
                            .setSubTitle("Please Check Folder With App name In Internal Storage \n\n" + FilesModelClassObjectList.get(pos).getLocation())
                            .setCancalable(true)
                            //set sub title font
                            .setNegativeButtonHide(true) //hide cancel button
                            .setPositiveButton("OK", new SmartDialogClickListener() {
                                @Override
                                public void onClick(SmartDialog smartDialog) {

                                    smartDialog.dismiss();
                                }
                            }).build().show();
                    recyclerView.setAlpha(1);
                    Toast.makeText(Compressed_Files_Activity.this,"Successfull Unziped" ,Toast.LENGTH_LONG).show();
                    getAllCompressedFiles();
                }
            }, 2000);
            super.onPostExecute(s);
        }@Override
        protected String doInBackground(String... strings) {

            ZipArchive zipArchive = new ZipArchive();
            zipArchive.unzip(zipfilelocation , targetdirectorylocation ,"");
            return null;
        }
    }
    private void getAllCompressedFiles() {
        LinearLayoutManager manager = new LinearLayoutManager(Compressed_Files_Activity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter_ZipFiles(FilesModelClassObjectList , Compressed_Files_Activity.this , this);
        recyclerView.setAdapter(adapter);
    }
    public void walkdirRAR(File dir) {
        String zippatteren = ".zip";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdirRAR(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(zippatteren))
                    {
                        if(checkPasswordProtectection(listFile[i].getAbsolutePath())) {
                            FilesModelClass object = new FilesModelClass();
                            object.setName(listFile[i].getName());
                            object.setLocation(listFile[i].getAbsolutePath());
                            object.setItemIcon(ContextCompat.getDrawable(Objects.requireNonNull(Compressed_Files_Activity.this), R.drawable.zipfile));
                            // object.setSize(String.valueOf(listFile[i].length() / 1024));
                            FilesModelClassObjectList.add(object);
                        }
                    }
                }
            }
        }
    }

    private boolean checkPasswordProtectection(String absolutePath) {

        try {
            ZipFile zipFile = new ZipFile(absolutePath);

            if (zipFile.isEncrypted()) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }


    @Override
    public void onClickListner(final int position) {


        recyclerView.setAlpha((float) 0.1);

        zipFile = new File(FilesModelClassObjectList.get(position).getLocation());
        targetDirectory = zipFile.getParentFile();

        zipfilelocation = FilesModelClassObjectList.get(position).getLocation();
        targetdirectorylocation = zipFile.getParent();
        final FlatDialog flatDialog = new FlatDialog(Compressed_Files_Activity.this);
        flatDialog.setTitle("Please Select")
                .setTitleColor(getResources().getColor(R.color.grayas))
                .setFirstButtonText("Extract At File location")
                .setSecondButtonText("Select Location to Extract")
                .setThirdButtonText("Cancel")
                .setBackgroundColor(getResources().getColor(R.color.white))
                .setFirstButtonColor(getResources().getColor(R.color.zip))
                .setSecondButtonColor(getResources().getColor(R.color.zip))
                .setThirdButtonColor(getResources().getColor(R.color.zip))
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new UnZipAsynctask().execute();
                        pos = position;
                        flatDialog.dismiss();


                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "";
                        Intent intent = new Intent(Compressed_Files_Activity.this, ShowAllCategoriesFiles.class);
                        intent.putExtra("category", "Internal Storage");
                        intent.putExtra("loc" , zipfilelocation);
                        startActivity(intent);
                        flatDialog.dismiss();
                        recyclerView.setAlpha(1);
                    }
                }).withThirdButtonListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flatDialog.dismiss();
                recyclerView.setAlpha(1);
            }
        }).show();


    }

}
