package com.example.karol.musicapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadApiHelper {
    private String fileName;
    private String Url;

    public DownloadApiHelper(String fileName,String Url)
    {
        this.fileName=fileName;
        this.Url=Url;
        init();
    }

    private void init()
    {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://youtubetoany.com").
                build();

        DownloadApi retrofitDownload = retrofit.create(DownloadApi.class);

        Call<ResponseBody> call = retrofitDownload.downloadFileWithDynamicUrlSync(this.Url);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if(writeResponseBodyToDisk(response.body().byteStream())==true)
                        {
                            return null;
                        }
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean writeResponseBodyToDisk(InputStream body) {
        try {
            String filePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/"+this.fileName+".mp3";
            File audioFile = new File(filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;
                inputStream = body;
                boolean fileExist=audioFile.exists();
                if(fileExist==false) {
                    audioFile.createNewFile();
                }
                outputStream = new FileOutputStream(audioFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("MainActivity",new Long(fileSizeDownloaded).toString());
                }
                outputStream.flush();
                Log.d("MainActivity","END Saving");
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
