package com.csmm.gestorescolar.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.screens.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    Intent thisIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        thisIntent = getIntent();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startLoginActivity();
            }
        }, 2400);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}