package com.example.karol.musicapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends AppCompatActivity {
    @BindView(R.id.prev_button)ImageButton prevButton;
    @BindView(R.id.play_button)ImageButton playButton;
    @BindView(R.id.next_button)ImageButton nextButton;
    @BindView(R.id.song_name)TextView textView;

    private List<File>songs;
    private int curr_songNumber;
    private MediaPlayer mediaPlayer;
    private boolean playingSong;
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

    public void playSong(){
        try {
            if (!this.playingSong) {
                this.mediaPlayer.setDataSource(this.songs.get(this.curr_songNumber).getPath());
                this.textView.setText(this.songs.get(this.curr_songNumber).getName());
                this.mediaPlayer.prepare();
            }
            this.mediaPlayer.start();
            this.playingSong = true;
        }catch (IOException e) {
            //e.printStackTrace();
        }
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

}
