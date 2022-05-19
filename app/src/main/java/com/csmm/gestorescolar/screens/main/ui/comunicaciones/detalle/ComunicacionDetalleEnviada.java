package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.google.android.material.chip.Chip;

public class ComunicacionDetalleEnviada extends AppCompatActivity {


    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    Button volverButton;
    int idComunicacion;
    int idDestino;
    String leida;
    String eliminado;
    String[] adjuntos;
    Chip chipAdjunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicacion_detalle_enviada);

        mSender = findViewById(R.id.tvEmailSender);
        mEmailTitle = findViewById(R.id.tvEmailTitle);
        mEmailDetails = findViewById(R.id.tvEmailDetails);
        mEmailTime = findViewById(R.id.tvEmailTime);
        volverButton = findViewById(R.id.volverButton);
        chipAdjunto = findViewById(R.id.chipAdjunto);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mEmailTitle.setText(mBundle.getString("asunto"));
            mEmailDetails.setText(mBundle.getString("texto"));
            mEmailTime.setText(mBundle.getString("fecha"));
            mSender.setText(mBundle.getString("remite"));
            idComunicacion = mBundle.getInt("id_com");
            idDestino = mBundle.getInt("id_destino");
            leida = mBundle.getString("leida");
            eliminado = mBundle.getString("eliminado");
            adjuntos = mBundle.getStringArray("adjuntos");
            if(adjuntos.length != 0) {
                chipAdjunto.setText(adjuntos[0]);
                chipAdjunto.setVisibility(View.VISIBLE);
            }
        }

        chipAdjunto.setOnClickListener(view -> {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String savedtoken= sharedPref.getString("token",null);
            //String token = idComunicacion + adjuntos[0];
            //StringBuilder strb = new StringBuilder(token);
            //token = strb.reverse().toString();
            Uri uri = Uri.parse(RestClient.REST_API_BASE_URL + "/resources/adjunto/download").buildUpon()
                    .appendQueryParameter("file_name", adjuntos[0])
                    .appendQueryParameter("id_comunicacion", String.valueOf(idComunicacion))
                    .appendQueryParameter("auth", savedtoken)
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        volverButton.setOnClickListener(view -> finish());    }
}