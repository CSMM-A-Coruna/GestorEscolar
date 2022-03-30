package com.csmm.gestorescolar.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.screens.auth.LoginActivity;
import com.csmm.gestorescolar.screens.main.MainActivity;

public class LauncherActivity extends AppCompatActivity {

    Intent thisIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        thisIntent = getIntent();

        Handler handler = new Handler();
        handler.postDelayed(this::startLoginActivity, 2400);
    }

    private void startLoginActivity() {
        SharedPreferences sharedPref = getSharedPreferences("user", MODE_PRIVATE);
        if(sharedPref.getString("token", null) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}