package com.example.rishugoel.basics;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyReceiver myReceiver;
    Button searchButton;
    EditText searchEditText;
    ImageView searchimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }
    protected void onStart() {
        // TODO Auto-generated method stub

        //Register BroadcastReceiver
        //to receive event from our service
        this.findAllViewsById();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        //Start our own service

        searchButton.setOnClickListener(this);
        super.onStart();
    }
    public void onClick(View v) {
            Intent serviceIntent = new Intent(this, MyService.class);
            String query = searchEditText.getText().toString();
            serviceIntent.putExtra("Query", query);
            startService(serviceIntent);
    }



    public void findAllViewsById() {
        searchEditText = (EditText) findViewById(R.id.editText);
        searchimageView = (ImageView) findViewById(R.id.imageView);
        searchButton = (Button) findViewById(R.id.button);
    }


    public void startService(View view) {

        startService(new Intent(getBaseContext(), MyService.class));
    }
    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            Bundle ex=arg1.getExtras();
            Bitmap datapassed = (Bitmap) ex.getParcelable("DATAPASSED" );
            searchimageView.setImageBitmap(datapassed);
            Toast.makeText(MainActivity.this,"Triggered by Service!\n"+ "Data passed: " + datapassed,Toast.LENGTH_LONG).show();

        }

    }
    protected void onStop(){
        unregisterReceiver(myReceiver);
        super.onStop();
    };
    // Method to stop the service
    public void stopService(View view) {

        unregisterReceiver(myReceiver);
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}
