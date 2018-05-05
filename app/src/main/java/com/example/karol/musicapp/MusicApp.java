package com.example.karol.musicapp;

import android.app.Application;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;

public class MusicApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(Exception error) {
            }
        });
    }
}
