package com.example.karol.musicapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karol.musicapp.network.DataApiHelper;
import com.example.karol.musicapp.network.DownloadApiHelper;
import com.roger.catloadinglibrary.CatLoadingView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.editText) EditText url;
    @BindView(R.id.text)TextView text;
    @BindView(R.id.button)Button button;
    @BindView(R.id.my_recycler_view)RecyclerView audioList;

    private DataApiHelper data;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CatLoadingView catProgress;
    private boolean isRunning;
    private boolean progressVisible;
    private boolean progressStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getIntentData();
        checkNotification();
        this.audioList.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.audioList.setLayoutManager(mLayoutManager);
        this.isRunning=true;
        this.progressVisible=false;
        this.progressStop=false;
    }

    public void getIntentData() {
        Intent intent=getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if(Intent.ACTION_SEND.equals(action) && type != null)
        {
            if ("text/plain".equals(type)) {
                String urlNew = intent.getStringExtra(Intent.EXTRA_TEXT);
                url.setText(urlNew);
            }
        }
    }

    public void check(View view){
        Parser parser = new Parser(this.url.getText().toString());
        text.setText(parser.getRight_link());
        this.data = new DataApiHelper(parser.getRight_link(),this);
    }

    public void show() {
        listAdapter = new DataAdapter(data.getVideo(),this);
        audioList.setAdapter(listAdapter);
        text.setText(data.getVideo().getTitle());
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    public void downloadUrl(String url) {
        progressVisible=true;
        catProgress=new CatLoadingView();
        catProgress.show(getSupportFragmentManager(),"");
        boolean a=isStoragePermissionGranted();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        catProgress.setText("Downloading");
        catProgress.setCancelable(false);
        DownloadApiHelper downloadApi=new DownloadApiHelper(data.getVideo().getTitle(),url,this);
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning=false;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        isRunning=true;
        if(progressStop) {
            stopProgress();
        }
    }

    public void stopProgress() {
        if(this.isRunning && this.progressVisible) {
            Toast.makeText(MainActivity.this, "Download ends successfully", Toast.LENGTH_SHORT).show();
            catProgress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressVisible=false;
        }
        else
        {
            progressStop=true;
        }
    }

    public void checkNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel("MusicApp", name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void goPlay(View view) {
        Intent intent=new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }
}