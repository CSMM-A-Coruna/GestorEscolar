package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostSendComunicacionResponseHandler;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle.utils.AdjuntoUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ComunicacionDetalleNueva extends AppCompatActivity {


    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    private Chip chipPara, chipAlumno;
    private Button btnSeleccionarDestino, btnSeleccionarAlumno;
    private Chip chipAdjunto;
    private EditText asuntoEditText, textoEditText;
    MaterialToolbar topBar;

    private String path;
    private String fileName;

    final String[] filtradoAlumnoString = {""};
    final int[] filtradoAlumnoInt = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comunicacion_detalle_nueva);

        chipPara = findViewById(R.id.chipComunicacionDestino);
        btnSeleccionarDestino = findViewById(R.id.btnSeleccionarDestino);
        chipAlumno = findViewById(R.id.chipAlumnoAsociado);
        btnSeleccionarAlumno = findViewById(R.id.btnSeleccionarAlumno);
        chipAdjunto = findViewById(R.id.chipAdjunto);
        asuntoEditText = findViewById(R.id.asuntoEditText);
        textoEditText = findViewById(R.id.textoEditText);
        topBar = findViewById(R.id.topAppBar);

        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.adjuntar) {
                    askPermissionAndBrowserFile();
                } else if (item.getItemId() == R.id.enviar) {
                    sendCom();
                } else if (item.getItemId() == R.id.descartar) {
                    showDialog();
                }
                return false;
            }
        });

        chipAdjunto.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = null;
                fileName = null;
                chipAdjunto.setVisibility(View.INVISIBLE);
            }
        });

        // Selector de alumnos
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<String>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            if(alumnos.length() == 1) {
                JSONObject json = alumnos.getJSONObject(0);
                setDefaultAlumno(json.getString("nombre"));
            } else {
                for(int i = 0; i < alumnos.length(); i++) {
                    JSONObject json = alumnos.getJSONObject(i);
                    listAlumnos.add(json.getString("nombre"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] arrayAlumnosSoloNombre = new String[listAlumnos.size()];
        for(int i=0; i<listAlumnos.size(); i++) {
            String nombre = listAlumnos.get(i);
            String[] splited = nombre.split("\\s+");
            arrayAlumnosSoloNombre[i] = splited[0];
        }

        btnSeleccionarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(ComunicacionDetalleNueva.this)
                        .setTitle("Alumnos")
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                filtradoAlumnoInt[0] = 0;
                                filtradoAlumnoString[0] = arrayAlumnosSoloNombre[0];
                            }
                        })
                        .setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                filtradoAlumnoString[0] = arrayAlumnosSoloNombre[filtradoAlumnoInt[0]];
                                chipAlumno.setText("Alumno: " + filtradoAlumnoString[0]);
                                btnSeleccionarAlumno.setVisibility(View.INVISIBLE);
                                chipAlumno.setCloseIconVisible(true);
                            }
                        })
                        .setSingleChoiceItems(arrayAlumnosSoloNombre, filtradoAlumnoInt[0], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                filtradoAlumnoInt[0] = i;
                            }
                        }).create().show();
            }
        });

        chipAlumno.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSeleccionarAlumno.setVisibility(View.VISIBLE);
                chipAlumno.setText("Alumno:");
                chipAlumno.setCloseIconVisible(false);
                filtradoAlumnoString[0] = "";
                filtradoAlumnoInt[0] = 0;
            }
        });
    }

    private void setDefaultAlumno(String alumno) {
        chipAlumno.setText("Alumno: " + alumno);
        btnSeleccionarAlumno.setVisibility(View.INVISIBLE);
    }

    private void sendCom() {
        //!TODO Cálculo tipo_destino, idRemite, idDestino, idAlumnoAsociado,
        RestClient.getInstance(this).postSendComunicacion(asuntoEditText.getText().toString(), textoEditText.getText().toString(), 1, 1, 1, 1, new PostSendComunicacionResponseHandler() {
            @Override
            public void requestDidComplete(String idNuevaComunicacion) {
                if (getPath() != null) {
                    getFileName();
                    new UploadFile().execute(idNuevaComunicacion);
                }
                Snackbar.make(chipAdjunto, "Comunicación enviada con éxito", Snackbar.LENGTH_SHORT).show();
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
            Map<String, String> params = new HashMap<String, String>();
            String url = RestClient.REST_API_BASE_URL + "/resources/upload?id_comunicacion="+urls[0];
            try {
                String result = RestClient.getInstance(getApplicationContext()).uploadAdjunto(url, params, path, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("¡Cuidado!")
                .setMessage("¿Estás seguro de que quieres descartar esta comunicación?")
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