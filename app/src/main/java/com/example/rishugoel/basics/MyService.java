package com.example.rishugoel.basics;

/**
 * Created by rishugoel on 04/08/16.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;

import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
;


public class MyService extends Service {
    final static String MY_ACTION = "MY_ACTION";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Let it continue running until it is stopped.
        String userID = intent.getExtras().getString("Query");
//        InputStream in = null;
//        int resCode = -1;

        String dataUrl = userID;
        downloadImage(dataUrl);
        return 1;
    }

    private void downloadImage(String urlStr) {
        final String url = urlStr;

        new Thread() {
            public void run() {
                InputStream in = null;
                Bitmap A;

                try {
                    in = openHttpConnection(url);
                    A = BitmapFactory.decodeStream(in);
                    Bundle b = new Bundle();
                    b.putParcelable("bitmap", A);
                    in.close();
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);

                    intent.putExtra("DATAPASSED",A);

                    sendBroadcast(intent);
                }

                catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }.start();
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}