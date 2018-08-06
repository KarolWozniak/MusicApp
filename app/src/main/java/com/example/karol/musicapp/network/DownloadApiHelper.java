package com.example.karol.musicapp.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.karol.musicapp.activities.MainActivity;
import com.example.karol.musicapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadApiHelper {

    private static final int NOTIFICATION_ID = 2150;
    private static final String CHANNEL_NAME = "MusicApp";

    private String fileName;
    private String Url;
    private static MainActivity mainActivity;

    public DownloadApiHelper(String fileName, String Url, MainActivity mainActivity) {
        this.fileName = fileName;
        this.Url = Url;
        this.mainActivity = mainActivity;
        init();
    }

    private void init() {
        DownloadApi retrofitDownload = RetrofitClient.getClient().create(DownloadApi.class);

        Call<ResponseBody> call = retrofitDownload.downloadFileWithDynamicUrlSync(this.Url);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if (writeResponseBodyToDisk(response.body().byteStream())) {
                            showNotification();
                            stopAnim();
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

    private void stopAnim() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = () -> mainActivity.stopProgress();
        mainHandler.post(myRunnable);
    }

    private boolean writeResponseBodyToDisk(InputStream body) {
        try {
            File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File audioFile = new File(filePath, this.fileName + ".webm");
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;
                inputStream = body;
                boolean fileExist = audioFile.exists();
                if (fileExist == false) {
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
                    Log.d("MainActivity", Long.toString(fileSizeDownloaded));
                }
                outputStream.flush();
                Log.d("MainActivity", "END Saving");
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

    private void showNotification() {
        Intent intent = new Intent(mainActivity.getApplicationContext(), DownloadApiHelper.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mainActivity.getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(mainActivity.getApplicationContext(), CHANNEL_NAME)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("MusicApp")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentText("Downloading " + this.fileName + " ends successfully")
                .build();
        NotificationManager notificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
