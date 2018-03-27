package com.example.karol.musicapp;

import Json.Video;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Karol on 2018-03-17.
 */

public interface DataApi {
    @GET
    Call<Video> getVideo (@Url String url);
}
