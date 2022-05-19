package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostSendComunicacionResponseHandler;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle.utils.AdjuntoUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ComunicacionDetalleResponder extends AppCompatActivity {

    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private String path;
    private String fileName;

    private Chip destinoChip;
    private Chip chipAdjunto;
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
        Chip remiteChip = findViewById(R.id.chipComunicacionRemite);
        asuntoEditText = findViewById(R.id.asuntoEditText);
        textoEditText = findViewById(R.id.textoEditText);
        topBar = findViewById(R.id.topAppBar);
        chipAdjunto = findViewById(R.id.chipAdjunto);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            idRemite = mBundle.getInt("id_remite");
            tipoDestino = mBundle.getString("tipo_destino");
            idDestino = mBundle.getInt("id_destino");
            idAlumnoAsociado = mBundle.getInt("id_alumnoAsociado");


            destinoChip.setText(String.format("Para: %s", mBundle.getString("destino")));
            remiteChip.setText(String.format("De: %s", mBundle.getString("remite")));
            asuntoEditText.setText(String.format("Re: %s", mBundle.getString("asunto")));
            if (mBundle.getString("fecha").contains("/")) {
                textoEditText.setText(String.format("El %s, %s escribió: \n%s", mBundle.getString("fecha"), mBundle.getString("destino"), mBundle.getString("texto")));
            } else {
                textoEditText.setText(String.format("Hoy a las %s, %s escribió: \n%s", mBundle.getString("fecha"), mBundle.getString("destino"), mBundle.getString("texto")));
            }

            topBar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.adjuntar) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        askPermissionAndBrowserFile();
                    }
                } else if (item.getItemId() == R.id.enviar) {
                    sendCom();
                } else if (item.getItemId() == R.id.descartar) {
                    showDialog();
                }
                return false;
            });

            chipAdjunto.setOnCloseIconClickListener(view -> {
                path = null;
                fileName = null;
                chipAdjunto.setVisibility(View.INVISIBLE);
            });
        }
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("¡Cuidado!")
                .setMessage("¿Estás seguro de que quieres descartar esta respuesta?")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {

                })
                .setPositiveButton("Descartar", (dialogInterface, i) -> finish()).show();
    }

    private void sendCom() {
        int destino;
        if (tipoDestino.equals("profesores")) {
            destino = 3;
        } else if (tipoDestino.equals("familias")) {
            destino = 2;
        } else {
            destino = 1;
        }
        RestClient.getInstance(this).postSendComunicacion(asuntoEditText.getText().toString(), textoEditText.getText().toString(), idRemite, destino, idDestino, idAlumnoAsociado, new PostSendComunicacionResponseHandler() {
            @Override
            public void requestDidComplete(String idNuevaComunicacion) {
                if (getPath() != null) {
                    getFileName();
                    new UploadFile().execute(idNuevaComunicacion);
                }
                Snackbar.make(destinoChip, "Comunicación enviada con éxito", Snackbar.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(this::finishActivity, 1000);
            }

            private void finishActivity() {
                finish();
            }

            @Override
            public void requestDidFail(int statusCode) {
                System.out.println(statusCode);
            }
        });
    }


    private class UploadFile extends AsyncTask<String, String, Void> {
        protected Void doInBackground(String...urls) {
            //setup params
            Map<String, String> params = new HashMap<>();
            String url = RestClient.REST_API_BASE_URL + "/resources/adjunto/upload?id_comunicacion="+urls[0];
            try {
                RestClient.getInstance(getApplicationContext()).uploadAdjunto(url, params, path, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void askPermissionAndBrowserFile() {

        // If you have access to the external storage, do whatever you need
        if (Environment.isExternalStorageManager()){
            doBrowseFile();
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void doBrowseFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Escoge el archivo");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();
                    String filePath = null;
                    try {
                        filePath = AdjuntoUtils.getPath(getApplicationContext(), fileUri);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                    this.path = filePath;
                    getFileName();
                    chipAdjunto.setText(fileName);
                    chipAdjunto.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath() {
        return this.path;
    }

    public void getFileName() {
        StringTokenizer string = new StringTokenizer(getPath(), "/");
        while(string.hasMoreTokens()) {
            this.fileName = string.nextToken();
        }
        this.fileName = this.fileName.replace(" ", "-");
    }
}