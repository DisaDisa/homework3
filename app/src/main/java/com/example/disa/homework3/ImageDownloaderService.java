package com.example.disa.homework3;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Disa on 30.11.2016.
 */

public class ImageDownloaderService extends Service {
    boolean started = false;
    private String imagePath = "https://cs7053.vk.me/c540106/v540106826/38ac3/Mi1UgwmG1IY.jpg";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(started) return START_STICKY;
        started = true;


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Log.e("My Log", "Start downloading");
                File f = new File(getFilesDir(), MainActivity.IMAGENAME);
                if(!f.exists() || BitmapFactory.decodeFile(f.getAbsolutePath()) == null) {
                    HttpsURLConnection connection = null;
                    InputStream in = null;
                    FileOutputStream out = null;
                    try {
                        connection = (HttpsURLConnection) new URL(imagePath).openConnection();
                        in = new BufferedInputStream(connection.getInputStream());
                        out = new FileOutputStream(f);
                        byte[] buf = new byte[4096];
                        int size;
                        while((size = in.read(buf)) > 0) {
                            out.write(buf, 0, size);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (in != null) in.close();
                            if (connection != null) connection.disconnect();
                            if (out != null) out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                sendBroadcast(new Intent(MainActivity.ImageDownloaded));
                started = false;
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
