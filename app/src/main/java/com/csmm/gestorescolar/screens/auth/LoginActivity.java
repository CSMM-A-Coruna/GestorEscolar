package com.csmm.gestorescolar.screens.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.PreferencesDTO;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.GetPreferencesResponseHandler;
import com.csmm.gestorescolar.client.handlers.PostLoginResponseHandler;
import com.csmm.gestorescolar.screens.main.MainActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity {

    private Button iniciarSesionButton;
    private TextInputEditText usuario;
    private TextInputEditText password;
    private LinearProgressIndicator progressBar;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        progressBar = findViewById(R.id.linearProgress);
        progressBar.setVisibility(View.GONE);
        usuario = findViewById(R.id.editText_Usuario);
        password = findViewById(R.id.editText_Password);
        iniciarSesionButton = findViewById(R.id.iniciarSesionButton);
        iniciarSesionButton.setOnClickListener(view -> {
            iniciarSesion();
        });
    }

    // Ocultar keyboard si se toca la pantalla
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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
                    if(dto.getToken().isEmpty()) {
                        Snackbar.make(iniciarSesionButton, "Ha habido un error con tu inicio de sesión inténtalo de nuevo más tarde...", Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else if(dto.getTipoUsuario() != 2) {
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
                        editor.putString("alumnosAsociados", dto.getAlumnosAsociados().toString());
                        editor.apply();
                        // Coger la configuración guardada en el servidor del usuario y aplicarla
                        getUserPreferencesFromServer();
                        // Log Firebase
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.METHOD, "login");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
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

    private void getUserPreferencesFromServer() {
        RestClient.getInstance(getApplicationContext()).getPreferences(new GetPreferencesResponseHandler() {
            @Override
            public void requestDidComplete(PreferencesDTO response) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("autentificacion_dos_fases", response.isAutentificacion_dos_fases());
                editor.putBoolean("proteccion_restablecimiento", response.isProteccion_restablecimiento());
                editor.putBoolean("not_comunicaciones_push", response.isNot_comunicaciones_push());
                editor.putBoolean("not_calificaciones_push", response.isNot_calificaciones_push());
                editor.putBoolean("not_entrevistas_push", response.isNot_entrevistas_push());
                editor.putBoolean("not_extraescolares_push", response.isNot_extraescolares_push());
                editor.putBoolean("not_enfermeria_push", response.isNot_enfermeria_push());
                editor.putBoolean("not_comunicaciones_email", response.isNot_comunicaciones_email());
                editor.putBoolean("not_calificaciones_email", response.isNot_calificaciones_email());
                editor.putBoolean("not_entrevistas_email", response.isNot_entrevistas_email());
                editor.putBoolean("not_extraescolares_email", response.isNot_extraescolares_email());
                editor.putBoolean("not_enfermeria_email", response.isNot_enfermeria_email());
                editor.apply();
            }

            @Override
            public void requestDidFail(int statusCode) {

            }
        });
    }
}