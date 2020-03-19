package com.blindassistant.smartphone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blindassistant.smartphone.R;

public class Connected_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);
       // String adress = getIntent().getStringExtra("adress");

        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), BluetoothService.class);
       // intent.putExtra("adress",adress);
        startService(intent);
    }
}
