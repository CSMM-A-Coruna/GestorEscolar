package com.csmm.gestorescolar.screens.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.LoginResponseHandler;
import com.csmm.gestorescolar.screens.main.MainActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private Button iniciarSesionButton;
    private TextInputEditText usuario;
    private TextInputEditText password;
    private LinearProgressIndicator progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        progressBar = findViewById(R.id.linearProgress);
        progressBar.setVisibility(View.GONE);
        usuario = findViewById(R.id.editText_Usuario);
        password = findViewById(R.id.editText_Password);
        iniciarSesionButton = findViewById(R.id.iniciarSesionButton);
        iniciarSesionButton.setOnClickListener(view -> {
            iniciarSesion();
        });
    }

    private void iniciarSesion() {
        String sUsuario = usuario.getText().toString();
        String sPassword = password.getText().toString();
        if(!sUsuario.matches("") && !sPassword.matches("")) {
            progressBar.setVisibility(View.VISIBLE);
            RestClient.getInstance(getApplicationContext()).postLogin(sUsuario, sPassword, new LoginResponseHandler() {
                @Override
                public void sessionRequestDidComplete(UsuarioDTO dto) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void requestDidFail(int statusCode) {
                    if(statusCode==401) {
                        Snackbar.make(iniciarSesionButton, "Contraseña incorrecta", Snackbar.LENGTH_SHORT).setAction("REINTENTAR", view -> {
                            iniciarSesion();
                        }).show();
                    } else if (statusCode==404){
                        Snackbar.make(iniciarSesionButton, "Usuario incorrecto", Snackbar.LENGTH_SHORT).setAction("REINTENTAR", view -> {
                            iniciarSesion();
                        }).show();
                    } else {
                        Snackbar.make(iniciarSesionButton, "Algo ha salido mal...", Snackbar.LENGTH_SHORT).setAction("REINTENTAR", view -> {
                            iniciarSesion();
                        }).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Snackbar.make(iniciarSesionButton, "Rellena todos los campos", Snackbar.LENGTH_SHORT).show();
        }
    }
}