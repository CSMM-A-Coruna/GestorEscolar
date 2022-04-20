package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csmm.gestorescolar.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicacion_detalle_enviada);

        mSender = findViewById(R.id.tvEmailSender);
        mEmailTitle = findViewById(R.id.tvEmailTitle);
        mEmailDetails = findViewById(R.id.tvEmailDetails);
        mEmailTime = findViewById(R.id.tvEmailTime);
        volverButton = findViewById(R.id.volverButton);

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
        }

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });    }
}