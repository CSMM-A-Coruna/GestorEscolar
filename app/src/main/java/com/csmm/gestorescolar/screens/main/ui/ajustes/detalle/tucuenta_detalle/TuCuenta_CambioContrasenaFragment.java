package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle.tucuenta_detalle;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.CheckPasswordResponseHandler;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TuCuenta_CambioContrasenaFragment extends Fragment {

    private TextInputEditText contrasenaActual, contrasenaNueva, contrasenaNuevaRepeticion;
    private TextInputLayout contrasenaActualLayout, contrasenaNuevaLayout, contrasenaNuevaRepeticionLayout;
    private Button olvidasteContrasenaBtn, cambiarContrasenaBtn;

    long delay = 1000; // 1 segundo después de que el usuario deje de escribir
    long last_text_edit = 0;
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ajustes_tucuenta_cambiocontrasena_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        olvidasteContrasenaBtn = view.findViewById(R.id.olvideContraseñaButton);

        contrasenaActual = view.findViewById(R.id.editText_PasswordActual);
        contrasenaNueva = view.findViewById(R.id.editText_PasswordNueva);
        contrasenaNuevaRepeticion = view.findViewById(R.id.editText_PasswordNuevaRepite);

        contrasenaActualLayout = view.findViewById(R.id.passwordTextFieldActual);
        contrasenaNuevaLayout = view.findViewById(R.id.passwordTextFieldNueva);
        contrasenaNuevaRepeticionLayout = view.findViewById(R.id.passwordTextFieldNuevaRepite);

        cambiarContrasenaBtn = view.findViewById(R.id.cambiarContrasenaButton);

        cambiarContrasenaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contrasenaActualLayout.getError()==null) {
                    if(contrasenaActual.getText().toString().isEmpty()) {
                        Snackbar.make(cambiarContrasenaBtn, "Escribe tu contraseña actual", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (contrasenaNueva.getText().toString().equals(contrasenaNuevaRepeticion.getText().toString())) {
                            if(contrasenaNueva.getText().toString().isEmpty() || contrasenaNuevaRepeticion.getText().toString().isEmpty()) {
                                Snackbar.make(cambiarContrasenaBtn, "Rellena todos los campos", Snackbar.LENGTH_SHORT).show();
                            } else {
                                if(contrasenaActual.getText().toString().equals(contrasenaNueva.getText().toString())) {
                                    Snackbar.make(cambiarContrasenaBtn, "La nueva contraseña es la misma que la actual", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    RestClient.getInstance(requireContext()).changePassword(contrasenaNueva.getText().toString(), new CheckPasswordResponseHandler() {
                                        @Override
                                        public void requestDidComplete() {
                                            Snackbar.make(cambiarContrasenaBtn, "Contraseña cambiada con éxito", Snackbar.LENGTH_SHORT).show();
                                            cambiarContrasenaBtn.setClickable(false);
                                            handler.postDelayed(finishAsync, 1500);
                                        }

                                        @Override
                                        public void requestDidFail(int statusCode) {
                                            Snackbar.make(cambiarContrasenaBtn, "Algo ha salido mal...", Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } else {
                            Snackbar.make(cambiarContrasenaBtn, "Las contraseñas no coinciden...", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Snackbar.make(cambiarContrasenaBtn, "Tu contraseña actual es incorrecta", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        contrasenaActual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Quitamos el callback para solo llamarlo una vez
                handler.removeCallbacks(checkPasswordSync);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Evitamos llamar al evento cuando esté vacío
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(checkPasswordSync, delay);
                }
            }
        });

        contrasenaNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Quitamos el callback para solo llamarlo una vez
                handler.removeCallbacks(checkNewPasswordsMatch);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Evitamos llamar al evento cuando esté vacío
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(checkNewPasswordsMatch, delay);
                }
            }
        });

        contrasenaNuevaRepeticion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Quitamos el callback para solo llamarlo una vez
                handler.removeCallbacks(checkNewPasswordsMatch);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Evitamos llamar al evento cuando esté vacío
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(checkNewPasswordsMatch, delay);
                }
            }
        });
    }

    private Runnable checkNewPasswordsMatch = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                if(contrasenaNueva.getText()!=null && contrasenaNuevaRepeticion.getText()!=null) {
                    if(contrasenaNueva.getText().toString().equals(contrasenaNuevaRepeticion.getText().toString())) {
                        contrasenaNuevaRepeticionLayout.setErrorEnabled(false);
                        contrasenaNuevaLayout.setErrorEnabled(false);

                        contrasenaNuevaLayout.setBoxStrokeColor(getResources().getColor(R.color.verde));
                        contrasenaNuevaLayout.setEndIconDrawable(R.drawable.ic_check);
                        contrasenaNuevaLayout.setEndIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.verde)));
                        contrasenaNuevaLayout.setEndIconVisible(true);

                        contrasenaNuevaRepeticionLayout.setBoxStrokeColor(getResources().getColor(R.color.verde));
                        contrasenaNuevaRepeticionLayout.setEndIconDrawable(R.drawable.ic_check);
                        contrasenaNuevaRepeticionLayout.setEndIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.verde)));
                        contrasenaNuevaRepeticionLayout.setEndIconVisible(true);
                    } else {
                        contrasenaNuevaRepeticionLayout.setErrorEnabled(true);
                        contrasenaNuevaRepeticionLayout.setError("Las contraseñas no coinciden");
                        contrasenaNuevaLayout.setErrorEnabled(true);
                        contrasenaNuevaLayout.setError(" ");
                    }
                }
            }
        }
    };

    private Runnable checkPasswordSync = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                RestClient.getInstance(getContext()).checkPassword(contrasenaActual.getText().toString(), new CheckPasswordResponseHandler() {
                    @Override
                    public void requestDidComplete() {
                        contrasenaActualLayout.setErrorEnabled(false);
                        contrasenaActualLayout.setBoxStrokeColor(getResources().getColor(R.color.verde));
                        contrasenaActualLayout.setEndIconDrawable(R.drawable.ic_check);
                        contrasenaActualLayout.setEndIconVisible(true);
                        contrasenaActualLayout.setEndIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.verde)));
                    }

                    @Override
                    public void requestDidFail(int statusCode) {
                        if(statusCode==401) {
                            contrasenaActualLayout.setErrorEnabled(true);
                            contrasenaActualLayout.setError("Contraseña incorrecta");
                        }
                    }
                });
            }
        }
    };

    private Runnable finishAsync = new Runnable() {
        @Override
        public void run() {
            requireActivity().onBackPressed();
        }
    };
}