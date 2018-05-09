package com.example.karol.musicapp;

import android.util.Log;


import Json.downloadURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Karol on 2018-03-17.
 */

public class DataApiHelper {
    public final DataApi api;
    //private Video video;
    private downloadURL videoUrl;
    private MainActivity mainActivity;
    public DataApiHelper (String url, final MainActivity mainActivity)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://207.154.200.78:1997/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.mainActivity=mainActivity;
        api = retrofit.create(DataApi.class);
        Call<downloadURL> callObject = api.getVideo(url);
        callObject.enqueue(new Callback<downloadURL>()
        {

            @Override
            public void onResponse(Call<downloadURL> call, Response<downloadURL> response) {
                if (response.isSuccessful())
                {
                    videoUrl = response.body();
                    mainActivity.show();
                }
            }

            @Override
            public void onFailure(Call<downloadURL> call, Throwable t) {
                Log.e(" ", t.getLocalizedMessage());
            }

        });
    }
    public downloadURL getVideo()
    {
        return this.videoUrl;
    }

}
