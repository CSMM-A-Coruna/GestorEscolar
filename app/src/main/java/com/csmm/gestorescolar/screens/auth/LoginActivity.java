package com.csmm.gestorescolar.screens.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.PostLoginResponseHandler;
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
            RestClient.getInstance(getApplicationContext()).postLogin(sUsuario, sPassword, new PostLoginResponseHandler() {
                @Override
                public void sessionRequestDidComplete(UsuarioDTO dto) {
                    //Guardo esos valores en un objeto SharedPreferences usando su Editor
                    if(dto.getTipoUsuario() != 2) {
                        Snackbar.make(iniciarSesionButton, "Estás intentado iniciar sesión con un usuario que no es considerado \"familia\"", Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
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
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
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