package com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.DestinoDTO;
import com.csmm.gestorescolar.client.handlers.GetDestinosResponseHandler;
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
import java.util.List;
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

    final String[] seleccionAlumnoString = {""};
    final int[] seleccionAlumnoInt = {0};
    final String[] seleccionDestinoString = {""};
    final int[] seleccionDestinosInt = {0};

    String[] arrayDestinos;
    List<DestinoDTO> destinos = new ArrayList<>();

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

        btnSeleccionarDestino.setVisibility(View.INVISIBLE);

        topBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.adjuntar) {
                askPermissionAndBrowserFile();
            } else if (item.getItemId() == R.id.enviar) {
                sendCom();
            } else if (item.getItemId() == R.id.descartar) {
                showDialog();
            }
            return false;
        });

        // Selector de alumnos
        String[] arrayAlumnos = getAlumnosSharedPreferences();
        btnSeleccionarAlumno.setOnClickListener(view -> new MaterialAlertDialogBuilder(ComunicacionDetalleNueva.this)
                .setTitle("Alumnos")
                .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                    seleccionAlumnoInt[0] = 0;
                    seleccionAlumnoString[0] = arrayAlumnos[0];
                })
                .setPositiveButton("Seleccionar", (dialogInterface, i) -> {
                    seleccionAlumnoString[0] = arrayAlumnos[seleccionAlumnoInt[0]];
                    chipAlumno.setText(String.format("Alumno: %s", seleccionAlumnoString[0]));
                    getDestinos(calcularIdAlumno(seleccionAlumnoString[0]));
                    btnSeleccionarAlumno.setVisibility(View.INVISIBLE);
                    chipAlumno.setCloseIconVisible(true);
                    btnSeleccionarDestino.setVisibility(View.VISIBLE);
                })
                .setSingleChoiceItems(arrayAlumnos, seleccionAlumnoInt[0], (dialogInterface, i) -> seleccionAlumnoInt[0] = i).create().show());

        // Selector de destino
        btnSeleccionarDestino.setOnClickListener(view -> new MaterialAlertDialogBuilder(ComunicacionDetalleNueva.this)
                .setTitle("Destinos disponibles")
                .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                    seleccionDestinosInt[0] = 0;
                    seleccionDestinoString[0] = arrayDestinos[0];
                })
                .setPositiveButton("Seleccionar", (dialogInterface, i) -> {
                    seleccionDestinoString[0] = arrayDestinos[seleccionDestinosInt[0]];
                    chipPara.setText(String.format("Para: %s", seleccionDestinoString[0]));
                    btnSeleccionarDestino.setVisibility(View.INVISIBLE);
                    chipPara.setCloseIconVisible(true);
                })
                .setSingleChoiceItems(arrayDestinos, seleccionDestinosInt[0], (dialogInterface, i) -> seleccionDestinosInt[0] = i).create().show());

        chipPara.setOnCloseIconClickListener(view -> {
            btnSeleccionarDestino.setVisibility(View.VISIBLE);
            chipPara.setText("Para:");
            chipPara.setCloseIconVisible(false);
            seleccionDestinosInt[0] = 0;
            seleccionDestinoString[0] = "";
        });

        chipAlumno.setOnCloseIconClickListener(view -> {
            btnSeleccionarAlumno.setVisibility(View.VISIBLE);
            chipAlumno.setText("Alumno:");
            chipAlumno.setCloseIconVisible(false);
            seleccionAlumnoString[0] = "";
            seleccionAlumnoInt[0] = 0;

            // Reinicamos botón y chip de destinos
            btnSeleccionarDestino.setVisibility(View.INVISIBLE);
            chipPara.setText("Para:");
            chipPara.setCloseIconVisible(false);
        });

        chipAdjunto.setOnCloseIconClickListener(view -> {
            path = null;
            fileName = null;
            chipAdjunto.setVisibility(View.INVISIBLE);
        });
    }

    private void sendCom() {
        int idDestino = 0, tipoDestino = 0;
        for (String arrayDestino : arrayDestinos) {
            if (arrayDestino.equals(destinos.get(0).getNombre() + " - " + destinos.get(0).getTipoUsuario())) {
                idDestino = destinos.get(0).getId();
                tipoDestino = destinos.get(0).getTipoDestino();
                break;
            }
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        //!TODO Cálculo tipo_destino, idRemite, idDestino, idAlumnoAsociado,
        RestClient.getInstance(this).postSendComunicacion(asuntoEditText.getText().toString(), textoEditText.getText().toString(),  sharedPreferences.getInt("id", 0), tipoDestino, idDestino, calcularIdAlumno(seleccionAlumnoString[0]), new PostSendComunicacionResponseHandler() {
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


    private String[] getAlumnosSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            /*if(alumnos.length() == 1) {
                JSONObject json = alumnos.getJSONObject(0);
                setDefaultAlumno(json.getString("nombre"));
            } else {*/
                for (int i = 0; i < alumnos.length(); i++) {
                    JSONObject json = alumnos.getJSONObject(i);
                    listAlumnos.add(json.getString("nombre"));
                }
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] arrayAlumnos = new String[listAlumnos.size()];
        for(int i=0; i<listAlumnos.size(); i++) {
            String nombre = listAlumnos.get(i);
            String[] splited = nombre.split("\\s+");
            arrayAlumnos[i] = splited[0];
        }
        return arrayAlumnos;
    }

    private int calcularIdAlumno(String nombre) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            for (int i = 0; i < alumnos.length(); i++) {
                JSONObject json = alumnos.getJSONObject(i);
                String nombreEntero = json.getString("nombre");
                String[] splited = nombreEntero.split("\\s+");
                if(nombre.equals(splited[0])) {
                    return json.getInt("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
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

    private void showDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("¡Cuidado!")
                .setMessage("¿Estás seguro de que quieres descartar esta comunicación?")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {

                })
                .setPositiveButton("Descartar", (dialogInterface, i) -> finish()).show();
    }

    private void getDestinos(int id) {
        RestClient.getInstance(getApplicationContext()).getDestinos(id, new GetDestinosResponseHandler() {
            @Override
            public void requestDidComplete(List<DestinoDTO> response) {
                destinos = response;
                arrayDestinos = new String[response.size()];
                for(int i=0; i < arrayDestinos.length; i++) {
                    arrayDestinos[i] = response.get(i).getNombre() + " - " + response.get(0).getTipoUsuario();
                }
            }

            @Override
            public void requestDidFail(int statusCode) {
                Snackbar.make(chipAdjunto, "No se han podido calcular los destinos disponibles", Snackbar.LENGTH_SHORT).show();
                Handler handler = new Handler();
                btnSeleccionarDestino.setVisibility(View.INVISIBLE);
                handler.postDelayed(this::finishActivity, 2000);
            }

            private void finishActivity() {
                finish();
            }
        });
    }

    private void askPermissionAndBrowserFile() {

        // If you have access to the external storage, do whatever you need
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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