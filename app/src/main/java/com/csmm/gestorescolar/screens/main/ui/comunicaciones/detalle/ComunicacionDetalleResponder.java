package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostSendComunicacionResponseHandler;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ComunicacionDetalleResponder extends AppCompatActivity {

    private Chip destinoChip;
    private Chip remiteChip;
    private EditText asuntoEditText, textoEditText;
    MaterialToolbar topBar;
    private int idRemite;
    private String tipoDestino;
    private int idDestino;
    private int idAlumnoAsociado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicacion_detalle_responder);

        destinoChip = findViewById(R.id.chipComunicacionDestino);
        remiteChip = findViewById(R.id.chipComunicacionRemite);
        asuntoEditText = findViewById(R.id.asuntoEditText);
        textoEditText = findViewById(R.id.textoEditText);
        topBar = findViewById(R.id.topAppBar);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            idRemite = mBundle.getInt("id_remite");
            tipoDestino = mBundle.getString("tipo_destino");
            idDestino = mBundle.getInt("id_destino");
            idAlumnoAsociado = mBundle.getInt("id_alumnoAsociado");


            destinoChip.setText("Para: " + mBundle.getString("destino"));
            remiteChip.setText("De: " + mBundle.getString("remite"));
            asuntoEditText.setText("Re: " + mBundle.getString("asunto"));
            if(mBundle.getString("fecha").contains("/")) {
                textoEditText.setText("El " + mBundle.getString("fecha") + ", " + mBundle.getString("destino") + " escribió: \n" + mBundle.getString("texto"));
            } else {
                textoEditText.setText("Hoy a las " + mBundle.getString("fecha") + ", " + mBundle.getString("destino") + " escribió: \n" + mBundle.getString("texto"));
            }

            topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.adjuntar) {
                        System.out.println("Por implementar");
                    } else if(item.getItemId() == R.id.enviar) {
                        sendCom();
                        finish();
                    } else if(item.getItemId() == R.id.descartar) {
                        showDialog();
                    }
                    return false;
                }
            });
        }
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("¡Cuidado!")
                .setMessage("¿Estás seguro de que quieres descartar esta respuesta?")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void sendCom() {
        int destino;
        if(tipoDestino.equals("profesores")) {
            destino = 3;
        } else if(tipoDestino.equals("familias")) {
            destino = 2;
        } else {
            destino = 1;
        }
        RestClient.getInstance(this).postSendComunicacion(asuntoEditText.getText().toString(), textoEditText.getText().toString(), idRemite, destino, idDestino, idAlumnoAsociado, new PostSendComunicacionResponseHandler() {
            @Override
            public void requestDidComplete() {
            }

            @Override
            public void requestDidFail(int statusCode) {
            }
        });
    }
}