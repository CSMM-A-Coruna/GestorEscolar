package com.csmm.gestorescolar.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.screens.auth.LoginActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Iniciamos la actividad Login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}