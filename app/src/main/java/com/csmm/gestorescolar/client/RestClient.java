package com.csmm.gestorescolar.client;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.CompareDataResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesBorradasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesEnviadasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesRecibidasResponseHandler;
import com.csmm.gestorescolar.client.handlers.DefaultErrorHandler;
import com.csmm.gestorescolar.client.handlers.PostEstadoComunicacionHandler;
import com.csmm.gestorescolar.client.handlers.PostLoginResponseHandler;
import com.csmm.gestorescolar.client.handlers.PostSendComunicacionResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {
    private RequestQueue queue;
    private Context context;


    private final String REST_API_BASE_URL = "http://192.168.11.40:3000/v1";

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

    public void compareData(CompareDataResponseHandler handler) {
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
                REST_API_BASE_URL + "/auth/update",
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
                REST_API_BASE_URL + "/comms/received?user_id=" + savedId,
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
                REST_API_BASE_URL + "/comms/sent?user_id=" + savedId,
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
                REST_API_BASE_URL + "/comms/deleted?user_id=" + savedId,
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
                Request.Method.POST,
                REST_API_BASE_URL + "/comms/update?id_com=" + id + "&id_destino=" + idDestino + "&state=" + estado,
                null,
                response -> {
                        handler.sessionRequestDidComplete(true);
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
