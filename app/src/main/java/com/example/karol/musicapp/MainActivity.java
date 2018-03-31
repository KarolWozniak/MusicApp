package com.example.karol.musicapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roger.catloadinglibrary.CatLoadingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText) EditText url;
    @BindView(R.id.text)TextView text;
    @BindView(R.id.button)Button button;
    @BindView(R.id.my_recycler_view)RecyclerView audioList;

    private DataApiHelper data;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CatLoadingView catProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        audioList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        audioList.setLayoutManager(mLayoutManager);
    }

    public void check(View view){
        Parser parser = new Parser(this.url.getText().toString());
        text.setText(parser.getRight_link());
        this.data=new DataApiHelper(parser.getRight_link(),this);
    }

    public void show()
    {
        listAdapter = new DataAdapter(data.getVideo(),this);
        audioList.setAdapter(listAdapter);
        text.setText(data.getVideo().getVidTitle());
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
//watch?v=elwTgpHlty0
    public void downloadUrl(String url)
    {
        this.catProgress=new CatLoadingView();
        this.catProgress.show(getSupportFragmentManager(),"");
        boolean a=isStoragePermissionGranted();
        DownloadApiHelper downloadApi=new DownloadApiHelper(data.getVideo().getVidTitle(),url,this);
        Toast.makeText(MainActivity.this,"Download ends successfully", Toast.LENGTH_SHORT).show();
    }

    public void stopProgress()
    {
        this.catProgress.dismiss();
    }

}