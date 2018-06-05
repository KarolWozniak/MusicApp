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
import java.util.ArrayList;
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
        this.songs=new ArrayList<>();
        this.curr_songNumber=0;
        this.playingSong=false;
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnCompletionListener(mediaPlayer -> playNextSong());
        getSongs();
    }

    public void playNextSong() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        curr_songNumber = curr_songNumber+1;
        playingSong = false;
        changePlayButton();
        playSong();
    }


    public void getSongs() {
        File filePath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        for(File file:filePath.listFiles())
        {
            songs.add(file);
        }
    }

    public void checkMp3(String name) {
        if(name.contains(".mp3"))
            fabButton.setVisibility(View.INVISIBLE);
        else
            fabButton.setVisibility(View.VISIBLE);
    }

    public void playSong(){
        try {
            if (!playingSong) {
                mediaPlayer.setDataSource(songs.get(curr_songNumber).getPath());
                String fileName = songs.get(curr_songNumber).getName();
                textView.setText(fileName);
                checkMp3(fileName);
                mediaPlayer.prepare();
            }
            mediaPlayer.start();
            playingSong = true;
        }catch (IOException e) {
            //e.printStackTrace();
        }
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
        songs.clear();
        getSongs();
    }

    public void changePlayButton()
    {
        if(mediaPlayer.isPlaying())
        {
            playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play));
            mediaPlayer.pause();
        }
        else
        {
            playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause));
            playSong();
        }
    }

    @OnClick(R.id.next_button)
    public void nextSong(View view){
        mediaPlayer.reset();
        playingSong=false;
        if(curr_songNumber+1==this.songs.size())
        {
            curr_songNumber = 0;
        }
        else {
            curr_songNumber++;
        }
        changePlayButton();
        playSong();
    }

    @OnClick(R.id.prev_button)
    public void prevSong(View view){
        mediaPlayer.reset();
        playingSong=false;
        if(curr_songNumber==0){
            curr_songNumber = songs.size()-1;
            return;
        }
        curr_songNumber--;
        changePlayButton();
        playSong();
    }

    @OnClick(R.id.play_button)
    public void playButton(View view){
        changePlayButton();
    }

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        convertFiletoMp3(songs.get(curr_songNumber));
    }

}
