package com.example.karol.musicapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.roger.catloadinglibrary.CatLoadingView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

public class PlayerActivity extends AppCompatActivity {

    @BindView(R.id.prev_button)ImageButton prevButton;
    @BindView(R.id.play_button)ImageButton playButton;
    @BindView(R.id.next_button)ImageButton nextButton;
    @BindView(R.id.song_name)TextView textView;
    @BindView(R.id.fab)FloatingActionButton fabButton;
    @BindView(R.id.my_toolbar)android.support.v7.widget.Toolbar myToolbar;
    @BindView(R.id.list_button)ImageButton listButton;

    private String songName;
    private CatLoadingView catProgress;
    private MusicPlayerService musicService;
    private Intent playIntent;
    private ServiceConnection musicConnection=new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerService.MyBinder binder=(MusicPlayerService.MyBinder)iBinder;
            musicService=binder.getService();
            getExtras();
            Log.d("PlayerActivity","MusicService is connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("PlayerActivity","MusicService is disconnected!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicPlayerService.class);
            startService(playIntent);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService=null;
        super.onDestroy();
    }

    public void getExtras()
    {
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            String path = b.getString("SONG_PATH");
            musicService.setSong(path);
            changePlayButton();
        }
    }

    public void checkMp3() {
        if(songName.contains(".mp3"))
            fabButton.setVisibility(View.INVISIBLE);
        else
            fabButton.setVisibility(View.VISIBLE);
    }

    private void convertFiletoMp3(final File file) {
        startAnimation();
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                Log.d("MainActivity","Saving ends successfully");
                stopAnimation();
                file.delete();
                refreshList();
            }
            @Override
            public void onFailure(Exception error) {
                Log.d("MainActivity","Something went wrong with converting!");
            }
        };
        AndroidAudioConverter.with(PlayerActivity.this)
                .setFile(file)
                .setFormat(AudioFormat.MP3)
                .setCallback(callback)
                .convert();
    }

    public void startAnimation() {
        catProgress=new CatLoadingView();
        catProgress.show(getSupportFragmentManager(),"");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        catProgress.setText("Converting ...");
        catProgress.setCancelable(false);
    }

    public void stopAnimation() {
        catProgress.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void refreshList() {
        //songs.clear();
        //getSongs();
    }

    public void changePlayButton()
    {
        if(musicService.isPlaying())
        {
            playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play));
            musicService.pauseSong();
        }
        else
        {
            playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause));
            songName=musicService.playSong();
            textView.setText(songName);
            checkMp3();
        }
    }

    @OnClick(R.id.next_button)
    public void nextSong(View view){
        musicService.nextSong();
        changePlayButton();
    }

    @OnClick(R.id.prev_button)
    public void prevSong(View view){
        musicService.prevSong();
        changePlayButton();
    }

    @OnClick(R.id.list_button)
    public void gotoList(View view) {
        Intent intent = new Intent(this, SongsListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.play_button)
    public void playButton(View view){
        changePlayButton();
    }

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        convertFiletoMp3(musicService.getFile());
    }

}
