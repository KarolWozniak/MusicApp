package com.example.karol.musicapp.network;

import android.util.Log;


import com.example.karol.musicapp.activities.MainActivity;

import com.example.karol.musicapp.json.downloadURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Karol on 2018-03-17.
 */

public class DataApiHelper {
    public final DataApi api;
    private downloadURL videoUrl;
    private MainActivity mainActivity;
    private String url;

    public DataApiHelper (String url, final MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        this.api = RetrofitClient.getClient().create(DataApi.class);
        this.url = url;
        startDownloading();
    }

    public void startDownloading() {
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

    public downloadURL getVideo() {
        return this.videoUrl;
    }

}
