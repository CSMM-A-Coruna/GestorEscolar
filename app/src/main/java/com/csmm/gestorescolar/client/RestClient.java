package com.csmm.gestorescolar.client;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.client.dtos.DestinoDTO;
import com.csmm.gestorescolar.client.dtos.DocumentoDTO;
import com.csmm.gestorescolar.client.dtos.HorarioDTO;
import com.csmm.gestorescolar.client.dtos.LlaveroDTO;
import com.csmm.gestorescolar.client.dtos.PreferencesDTO;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.CheckPasswordResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetLlaveroByIdAlumnoResponseHandler;
import com.csmm.gestorescolar.client.handlers.ReloadTokenResponseHandler;
import com.csmm.gestorescolar.client.handlers.DefaultErrorHandler;
import com.csmm.gestorescolar.client.handlers.GetAllDocumentosResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesBorradasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesEnviadasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesRecibidasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetDestinosResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetHorarioResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetPreferencesResponseHandler;
import com.csmm.gestorescolar.client.handlers.PostEstadoComunicacionHandler;
import com.csmm.gestorescolar.client.handlers.PostFCMTokenResponseHandler;
import com.csmm.gestorescolar.client.handlers.PostLoginResponseHandler;
import com.csmm.gestorescolar.client.handlers.PostSendComunicacionResponseHandler;
import com.csmm.gestorescolar.client.handlers.UpdatePreferenceResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RestClient {

    //public static final String REST_API_BASE_URL = "https://csmm-api.herokuapp.com/v1";
    public static final String REST_API_BASE_URL = "http://192.168.28.34:3000/v1";
    private RequestQueue queue;
    private Context context;


    private static RestClient instance = null;

    private RestClient(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static RestClient getInstance(Context context) {
        if(instance == null) {
            instance = new RestClient(context);
        }
        return instance;
    }


    public void postLogin(String usuario, String password, PostLoginResponseHandler handler) {
        JSONObject cuerpoPeticion = new JSONObject();
        try {
            cuerpoPeticion.put("usuario", usuario);
            cuerpoPeticion.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/auth/login",
                cuerpoPeticion,
                response -> {
                    UsuarioDTO usuarioDTO = new UsuarioDTO(response);
                    handler.sessionRequestDidComplete(usuarioDTO);
                }, new DefaultErrorHandler(handler)
        );
        queue.add(request);
    }

    public void reloadToken(ReloadTokenResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        JSONObject cuerpoPeticion = new JSONObject();
        try {
            cuerpoPeticion.put("token", savedtoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/auth/reload",
                cuerpoPeticion,
                response -> {
                    UsuarioDTO usuarioDTO = new UsuarioDTO(response);
                    handler.sessionRequestDidComplete(usuarioDTO);
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getComunicacionesRecibidas(GetComunicacionesRecibidasResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int savedId = sharedPref.getInt("id", 0);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/comms/received/"+ savedId,
                null,
                response -> {
                    List<ComunicacionDTO> listaComunicaciones = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject iterationElement = response.getJSONObject(i);
                            ComunicacionDTO com = new ComunicacionDTO(iterationElement);
                            listaComunicaciones.add(com);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.requestDidFail(-1);
                        }
                    }
                    // Never forget to call the handler method!
                    handler.sessionRequestDidComplete(listaComunicaciones);
                    SharedPreferences comunicacionesCache = context.getSharedPreferences("comunicaciones", Context.MODE_PRIVATE);
                    String lista = response.toString();
                    if(!lista.equals(comunicacionesCache.getString("recibidas", null))) {
                        SharedPreferences.Editor editor = comunicacionesCache.edit();
                        editor.putString("recibidas", lista);
                        editor.apply();
                    }
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getComunicacionesEnviadas(GetComunicacionesEnviadasResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int savedId = sharedPref.getInt("id", 0);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/comms/sent/" + savedId,
                null,
                response -> {
                    List<ComunicacionDTO> listaComunicaciones = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject iterationElement = response.getJSONObject(i);
                            ComunicacionDTO com = new ComunicacionDTO(iterationElement);
                            listaComunicaciones.add(com);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.requestDidFail(-1);
                        }
                    }
                    // Never forget to call the handler method!
                    handler.sessionRequestDidComplete(listaComunicaciones);
                    SharedPreferences comunicacionesCache = context.getSharedPreferences("comunicaciones", Context.MODE_PRIVATE);
                    String lista = response.toString();
                    if(!lista.equals(comunicacionesCache.getString("enviadas", null))) {
                        SharedPreferences.Editor editor = comunicacionesCache.edit();
                        editor.putString("enviadas", lista);
                        editor.apply();
                    }
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getComunicacionesBorradas(GetComunicacionesBorradasResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int savedId = sharedPref.getInt("id", 0);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/comms/deleted/" + savedId,
                null,
                response -> {
                    List<ComunicacionDTO> listaComunicaciones = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject iterationElement = response.getJSONObject(i);
                            ComunicacionDTO com = new ComunicacionDTO(iterationElement);
                            listaComunicaciones.add(com);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.requestDidFail(-1);
                        }
                    }
                    // Never forget to call the handler method!
                    handler.sessionRequestDidComplete(listaComunicaciones);
                    SharedPreferences comunicacionesCache = context.getSharedPreferences("comunicaciones", Context.MODE_PRIVATE);
                    String lista = response.toString();
                    if(!lista.equals(comunicacionesCache.getString("borradas", null))) {
                        SharedPreferences.Editor editor = comunicacionesCache.edit();
                        editor.putString("borradas", lista);
                        editor.apply();
                    }
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }


    public void postEstadoComunicacion(int id, String estado, int idDestino, PostEstadoComunicacionHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                REST_API_BASE_URL + "/comms/update/" + id + "?id_destino=" + idDestino + "&state=" + estado,
                null,
                response -> handler.sessionRequestDidComplete(true), new DefaultErrorHandler(handler)
                ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                    // Añadimos la cabecera deseada
                    if (savedtoken!=null) {
                        headers.put("Authorization", "Bearer " + savedtoken);
                    }
                    return headers;
                }
            };
        queue.add(request);
    }

    public void postSendComunicacion(String asunto, String texto, int idRemite, int tipoDestino, int idDestino, int idAlumnoAsociado, PostSendComunicacionResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);

        JSONObject body = new JSONObject();
        try {
            body.put("asunto", asunto);
            body.put("texto", texto);
            body.put("id_remite", idRemite);
            body.put("tipo_destino", tipoDestino);
            body.put("id_destino", idDestino);
            body.put("id_alumnoAsociado", idAlumnoAsociado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/comms/send",
                body,
                response -> {
                    try {
                        handler.requestDidComplete(response.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public String uploadAdjunto(String urlTo, Map<String, String> params,  String filePath, String fileName) throws Exception {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);

        String twoHyphens = "--";
        String boundary = "*****" + System.currentTimeMillis() + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;

        String[] q = filePath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("Authorization", "Bearer " + savedtoken);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + "*/*" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void getDestinos(int idAlumno, GetDestinosResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/comms/senders/" + idAlumno,
                null,
                response -> {
                    List<DestinoDTO> listaDestinos = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject iterationElement = response.getJSONObject(i);
                            DestinoDTO destino = new DestinoDTO(iterationElement);
                            listaDestinos.add(destino);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.requestDidFail(-1);
                        }
                    }
                    handler.requestDidComplete(listaDestinos);
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void postNewFCMToken(String fcmToken, PostFCMTokenResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int userId = sharedPref.getInt("id", 0);
        JSONObject body = new JSONObject();
        try {
            body.put("id_usuario", userId);
            body.put("fcm_token", fcmToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                REST_API_BASE_URL + "/auth/firebase_token",
                body,
                response -> {}, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void updatePreference(String preference, boolean value, UpdatePreferenceResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int userId = sharedPref.getInt("id", 0);
        JSONObject body = new JSONObject();
        try {
            body.put("tipo_preferencia", preference);
            body.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                REST_API_BASE_URL + "/preferences/" + userId,
                body,
                response -> {}, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getPreferences(GetPreferencesResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int userId = sharedPref.getInt("id", 0);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/preferences/" + userId,
                null,
                response -> {
                    PreferencesDTO preferencesDTO = new PreferencesDTO(response);
                    handler.requestDidComplete(preferencesDTO);
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void checkPassword(String password, CheckPasswordResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int userId = sharedPref.getInt("id", 0);
        JSONObject body = new JSONObject();
        try {
            body.put("id_usuario", userId);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/auth/check_pass",
                body,
                response -> handler.requestDidComplete(), new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void changePassword(String password, CheckPasswordResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int userId = sharedPref.getInt("id", 0);
        JSONObject body = new JSONObject();
        try {
            body.put("id_usuario", userId);
            body.put("new_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                REST_API_BASE_URL + "/auth/change_password",
                body,
                response -> handler.requestDidComplete(), new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getHorario(int idAlumno, GetHorarioResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        String grupo = "";
        try {
            JSONArray alumnos = new JSONArray(sharedPref.getString("alumnosAsociados", null));
            for (int i = 0; i < alumnos.length(); i++) {
                JSONObject json = alumnos.getJSONObject(i);
                if(json.getInt("id") == idAlumno) {
                    grupo = json.getString("grupo");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Como el grupo contiene espacios, necesitamos tirar de URL encode
        String grupoEnc = "";
        try {
            grupoEnc = URLEncoder.encode(grupo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/horario?grupo="+grupoEnc,
                null,
                response -> {
                    HorarioDTO dto = new HorarioDTO(response);
                    SharedPreferences horarioCache = context.getSharedPreferences("horario", Context.MODE_PRIVATE);
                    String lista = response.toString();
                    if(!lista.equals(horarioCache.getString(String.valueOf(idAlumno), ""))) {
                        SharedPreferences.Editor editor = horarioCache.edit();
                        editor.putString(String.valueOf(idAlumno), lista);
                        editor.apply();
                    }
                    handler.requestDidComplete(dto);
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getAllDocumentos(int idAlumno, String grupo, GetAllDocumentosResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/resources/documentos?grupo="+grupo+"&id_alumno="+idAlumno,
                null,
                response -> {
                    try {
                        List<DocumentoDTO> lista = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++) {
                            JSONObject iterationElement = response.getJSONObject(i);
                            DocumentoDTO doc = new DocumentoDTO(iterationElement);
                            lista.add(doc);
                        }
                        handler.requestDidComplete(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.requestDidFail(-1);
                    }
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void getLlaveroByIdAlumno(int idAlumno, GetLlaveroByIdAlumnoResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/llavero/"+idAlumno,
                null,
                response -> {
                    try {
                        List<LlaveroDTO> lista = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++) {
                            JSONObject iterationElement = response.getJSONObject(i);
                            LlaveroDTO doc = new LlaveroDTO(iterationElement);
                            lista.add(doc);
                        }
                        handler.requestDidComplete(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.requestDidFail(-1);
                    }
                }, new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("Authorization", "Bearer " + savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }
}
