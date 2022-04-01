package com.csmm.gestorescolar.screens.main.ui.comunicaciones;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostEstadoComunicacionHandler;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class ComunicacionDetalle extends AppCompatActivity {

    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    MaterialToolbar topBar;
    Button volverButton;
    Drawable favIcon;
    boolean favIconMarked;
    int idComunicacion;
    String leida;
    String eliminado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicaciones_detalle);

        mSender = findViewById(R.id.tvEmailSender);
        mEmailTitle = findViewById(R.id.tvEmailTitle);
        mEmailDetails = findViewById(R.id.tvEmailDetails);
        mEmailTime = findViewById(R.id.tvEmailTime);
        topBar = findViewById(R.id.topAppBar);
        volverButton = findViewById(R.id.volverButton);
        favIcon = topBar.getMenu().findItem(R.id.iconTopFavorite).getIcon();
        favIconMarked = false;


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mSender.setText(mBundle.getString("remite"));
            mEmailTitle.setText(mBundle.getString("asunto"));
            mEmailDetails.setText(mBundle.getString("texto"));
            mEmailTime.setText(mBundle.getString("fecha"));
            idComunicacion = mBundle.getInt("id_com");
            leida = mBundle.getString("leida");
            eliminado = mBundle.getString("eliminado");
            if(mBundle.getBoolean("importante")) {
                toggleFavIcon();
            }
        }

        if(leida.equals("null")) {
            updateServer("leida");
        }

        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                 if(item.getItemId() == R.id.iconTopFavorite) {
                     toggleFavIcon();
                } else if(item.getItemId() == R.id.iconResponder) {
                     //Snackbar.make(mEmailTime, "Por implementar", Snackbar.LENGTH_SHORT).show();
                 } else if(item.getItemId() == R.id.eliminar) {
                     //Snackbar.make(mEmailTime, "Por implementar", Snackbar.LENGTH_SHORT).show();
                 }
                return false;
            }
        });

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        RestClient.getInstance(getApplicationContext()).postEstadoComunicacion(idComunicacion, estado, new PostEstadoComunicacionHandler() {
            @Override
            public void sessionRequestDidComplete(boolean update) {

            }

            @Override
            public void requestDidFail(int statusCode) {
            }
        });
    }
}
