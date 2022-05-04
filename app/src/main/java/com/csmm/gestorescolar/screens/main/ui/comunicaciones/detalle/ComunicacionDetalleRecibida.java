package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostEstadoComunicacionHandler;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;

public class ComunicacionDetalleRecibida extends AppCompatActivity {

    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    MaterialToolbar topBar;
    Button volverButton;
    Drawable favIcon;
    boolean favIconMarked;
    int idComunicacion;
    int idDestino;
    String leida;
    String eliminado;
    String[] adjuntos;
    Chip chipAdjunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicaciones_detalle_recibida);

        mSender = findViewById(R.id.tvEmailSender);
        mEmailTitle = findViewById(R.id.tvEmailTitle);
        mEmailDetails = findViewById(R.id.tvEmailDetails);
        mEmailTime = findViewById(R.id.tvEmailTime);
        topBar = findViewById(R.id.topAppBar);
        volverButton = findViewById(R.id.volverButton);
        favIcon = topBar.getMenu().findItem(R.id.iconTopFavorite).getIcon();
        chipAdjunto = findViewById(R.id.chipAdjunto);
        favIconMarked = false;


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mEmailTitle.setText(mBundle.getString("asunto"));
            mEmailDetails.setText(mBundle.getString("texto"));
            mEmailTime.setText(mBundle.getString("fecha"));
            if(mBundle.getString("estado").equals("recibida")) {
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
                if(mBundle.getBoolean("importante")) {
                    favIcon.setTint(getResources().getColor(R.color.importante));
                    favIconMarked = true;
                }
                chipAdjunto.setOnClickListener(view -> {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    String savedtoken= sharedPref.getString("token",null);
                    Uri uri = Uri.parse(RestClient.REST_API_BASE_URL + "/resources/download").buildUpon()
                            .appendQueryParameter("file_name", adjuntos[0])
                            .appendQueryParameter("id_comunicacion", String.valueOf(idComunicacion))
                            .appendQueryParameter("auth", savedtoken)
                            .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
                topBar.setOnMenuItemClickListener(item -> {
                    if(item.getItemId() == R.id.iconTopFavorite) {
                        toggleFavIcon();
                    } else if(item.getItemId() == R.id.iconResponder) {
                        Intent intent = new Intent(getApplicationContext(), ComunicacionDetalleResponder.class);
                        intent.putExtra("id_destino", mBundle.getInt("id_remite"));
                        intent.putExtra("tipo_destino", mBundle.getString("tipo_remite"));
                        intent.putExtra("destino", mBundle.getString("remite"));
                        intent.putExtra("id_remite", idDestino);
                        intent.putExtra("remite", mBundle.getString("destino"));
                        intent.putExtra("asunto", mBundle.getString("asunto"));
                        intent.putExtra("texto", mBundle.getString("texto"));
                        intent.putExtra("fecha", mBundle.getString("fecha"));
                        intent.putExtra("id_alumnoAsociado", mBundle.getInt("id_alumnoAsociado"));
                        startActivity(intent);
                    } else if(item.getItemId() == R.id.eliminar) {
                        // Fix del issue #15
                        updateServer("no_importante");
                        updateServer("eliminado");
                        finish();
                    }
                    return false;
                });
                if(leida.equals("null")) {
                    updateServer("leida");
                }
            } else {
                topBar.setVisibility(View.GONE);
            }
        }

        volverButton.setOnClickListener(view -> finish());
    }

    private void toggleFavIcon() {
        String estado;
        if(favIconMarked) {
            favIcon.setTint(getResources().getColor(R.color.gris));
            estado = "no_importante";
        } else {
            favIcon.setTint(getResources().getColor(R.color.importante));
            estado = "importante";
        }
        updateServer(estado);
        favIconMarked = !favIconMarked;
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
