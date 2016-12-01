package com.example.disa.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGENAME = "Tatars.jpg";
    public static final String ImageDownloaded = "com.example.disa.homework3.ImageDownloadFinished";
    private final String NotDownloaded = "Не загружено";
    private File imageFile;
    private ImageView image;
    private TextView error_text;
    private BroadcastReceiver imageLoader;
    private BroadcastReceiver launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("MyLog", "On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        error_text = (TextView) findViewById(R.id.text_error);

        launcher = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("My log", "Service started");
                context.startService(new Intent(context, ImageDownloaderService.class));
            }
        };

        imageLoader = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("My Log", "try show image 1");
                showImage();
            }
        };

        registerReceiver(launcher, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(imageLoader, new IntentFilter(ImageDownloaded));
        showImage();
        Log.e("My log", "end On create");
    }

    boolean openFile() {
        imageFile = new File(getFilesDir(), IMAGENAME);
        Log.e("My log", "image exists is " + Boolean.toString(imageFile.exists()));
        return imageFile.exists();
    }

    void showImage() {
        Log.e("My log", "tried to show Image");
        if(openFile()) {
            image.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            image.setVisibility(View.VISIBLE);
            error_text.setVisibility(View.INVISIBLE);
        } else {
            error_text.setText(NotDownloaded);
            image.setVisibility(View.INVISIBLE);
            error_text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(launcher);
        unregisterReceiver(imageLoader);
    }
}
