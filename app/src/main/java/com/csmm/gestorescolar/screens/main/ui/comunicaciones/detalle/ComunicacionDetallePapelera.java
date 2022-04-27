package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostEstadoComunicacionHandler;
import com.google.android.material.chip.Chip;

public class  ComunicacionDetallePapelera extends AppCompatActivity {

    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    Button volverButton;
    Button restaurarButton;
    int idComunicacion;
    int idDestino;
    String leida;
    String eliminado;
    String adjuntos[];
    Chip chipAdjunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicacion_detalle_papelera);

        mSender = findViewById(R.id.tvEmailSender);
        mEmailTitle = findViewById(R.id.tvEmailTitle);
        mEmailDetails = findViewById(R.id.tvEmailDetails);
        mEmailTime = findViewById(R.id.tvEmailTime);
        volverButton = findViewById(R.id.volverButton);
        restaurarButton = findViewById(R.id.restaurarButton);
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

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        restaurarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateServer("restaurar");
                finish();
            }
        });
    }

    private void updateServer(String estado) {
        RestClient.getInstance(getApplicationContext()).postEstadoComunicacion(idComunicacion, estado, idDestino, new PostEstadoComunicacionHandler() {
            @Override
            public void sessionRequestDidComplete(boolean update) {

            }

            @Override
            public void requestDidFail(int statusCode) {
            }
        });
    }
}
