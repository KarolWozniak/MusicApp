package com.example.karol.musicapp.network;

import com.example.karol.musicapp.json.downloadURL;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Karol on 2018-03-17.
 */

public interface DataApi {

    @GET
    Call<downloadURL> getVideo (@Url String url);
}
