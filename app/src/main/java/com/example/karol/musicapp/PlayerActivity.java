package com.example.karol.musicapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

    private List<File>songs;
    private int curr_songNumber;
    private MediaPlayer mediaPlayer;
    private boolean playingSong;
    private CatLoadingView catProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        this.songs=new LinkedList<>();
        this.curr_songNumber=0;
        this.playingSong=false;
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        getSongs();
    }

    public void getSongs() {
        File filePath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        for(File file:filePath.listFiles())
        {
            this.songs.add(file);
        }
    }

    public void checkMp3(String name)
    {
        if(name.contains(".mp3"))
            fabButton.setVisibility(View.INVISIBLE);
        else
            fabButton.setVisibility(View.VISIBLE);
    }
    public void playSong(){
        try {
            if (!this.playingSong) {
                this.mediaPlayer.setDataSource(this.songs.get(this.curr_songNumber).getPath());
                String fileName = this.songs.get(this.curr_songNumber).getName();
                this.textView.setText(fileName);
                checkMp3(fileName);
                this.mediaPlayer.prepare();
            }
            this.mediaPlayer.start();
            this.playingSong = true;
        }catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void convertFiletoMp3(final File file)
    {
        startAnimation();
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                Log.d("MainActivity","Saving ends successfully");
                file.delete();
                stopAnimation();
            }
            @Override
            public void onFailure(Exception error) {
                Log.d("MainActivity","Something went wrong with saving! :C");
            }
        };
        AndroidAudioConverter.with(PlayerActivity.this)
                .setFile(file)
                .setFormat(AudioFormat.MP3)
                .setCallback(callback)
                .convert();
    }

    public void startAnimation()
    {
        catProgress=new CatLoadingView();
        catProgress.show(getSupportFragmentManager(),"");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        catProgress.setText("Converting ...");
        catProgress.setCancelable(false);
    }

    public void stopAnimation()
    {
        this.catProgress.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        refreshList();
    }

    public void refreshList()
    {
        this.songs.clear();
        getSongs();
    }

    @OnClick(R.id.next_button)
    public void nextSong(View view){
        this.mediaPlayer.reset();
        this.playingSong=false;
        if(this.curr_songNumber+1==this.songs.size())
        {
            this.curr_songNumber=0;
        }
        else {
            this.curr_songNumber++;
        }
        playSong();
    }

    @OnClick(R.id.prev_button)
    public void prevSong(View view){
        this.mediaPlayer.reset();
        this.playingSong=false;
        if(this.curr_songNumber==0){
            this.curr_songNumber=this.songs.size()-1;
            return;
        }
        this.curr_songNumber--;
        playSong();
    }

    @OnClick(R.id.play_button)
    public void playButton(View view){
        if(this.mediaPlayer.isPlaying())
        {
            this.playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play));
            this.mediaPlayer.pause();
        }
        else
        {
            this.playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause));
            playSong();
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        convertFiletoMp3(songs.get(curr_songNumber));
    }
}
