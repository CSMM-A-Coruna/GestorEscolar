package com.csmm.gestorescolar.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.CompareDataResponseHandler;
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
        handler.postDelayed(this::startApp, 1500);
    }

    private void startApp() {
        SharedPreferences sharedPref = getSharedPreferences("user", MODE_PRIVATE);
        if(sharedPref.getString("token", null) != null) {
            RestClient.getInstance(getApplicationContext()).compareData(new CompareDataResponseHandler() {
                @Override
                public void sessionRequestDidComplete(UsuarioDTO dto) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", dto.getToken());
                    editor.putInt("id", dto.getId());
                    editor.putString("usuario", dto.getUsuario());
                    editor.putString("nombre", dto.getNombre());
                    editor.putString("apellido1", dto.getApellido1());
                    editor.putString("apellido2", dto.getApellido2());
                    editor.putString("nacimiento", dto.getNacimiento());
                    editor.putString("dni", dto.getDni());
                    editor.putString("oa", dto.getOa());
                    editor.putInt("accesos", dto.getAccesos());
                    editor.putInt("tipoUsuario", dto.getTipoUsuario());
                    editor.putString("alumnosAsociados", dto.getAlumnosAsociados().toString());
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void requestDidFail(int statusCode) {
                    if(statusCode != 401) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}