package com.example.karol.musicapp;

import android.util.Log;


import Json.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

/**
 * Created by Karol on 2018-03-17.
 */

public class DataApiHelper {
    public final DataApi api;
    private Video video;
    private MainActivity mainActivity;
    public DataApiHelper (String url, final MainActivity mainActivity)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://youtubetoany.com/@api/json/mp3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.mainActivity=mainActivity;
        api = retrofit.create(DataApi.class);
        Call<Video> callObject = api.getVideo(url);
        callObject.enqueue(new Callback<Video>()
        {

            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful())
                {
                    video = response.body();
                    mainActivity.show();
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e(" ", t.getLocalizedMessage());
            }

        });
    }
    public Video getVideo()
    {
        return this.video;
    }

}
