package com.example.infinitycenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static String TAGM = "mytag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, Patient.class));
            finish();
        }
    }
}
