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
import com.csmm.gestorescolar.client.handlers.CommsRecibidasResponseHandler;
import com.csmm.gestorescolar.client.handlers.DefaultErrorHandler;
import com.csmm.gestorescolar.client.handlers.LoginResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {
    private RequestQueue queue;
    private Context context;


    private final String REST_API_BASE_URL = "http://192.168.28.31:3000/v1";

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


    public void postLogin(String usuario, String password, LoginResponseHandler handler) {
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
                    //Guardo esos valores en un objeto SharedPreferences usando su Editor
                    SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", usuarioDTO.getToken());
                    editor.putInt("id", usuarioDTO.getId());
                    editor.putString("usuario", usuarioDTO.getUsuario());
                    editor.putString("nombre", usuarioDTO.getNombre());
                    editor.putString("apellido1", usuarioDTO.getApellido1());
                    editor.putString("apellido2", usuarioDTO.getApellido2());
                    editor.putString("nacimiento", usuarioDTO.getNacimiento());
                    editor.putString("dni", usuarioDTO.getDni());
                    editor.putString("oa", usuarioDTO.getOa());
                    editor.putInt("accesos", usuarioDTO.getAccesos());
                    editor.putInt("tipoUsuario", usuarioDTO.getTipoUsuario());
                    editor.commit();
                }, new DefaultErrorHandler(handler)
        );
        queue.add(request);
    }

    public void getComunicacionesRecibidas(CommsRecibidasResponseHandler handler) {
        SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String savedtoken= sharedPref.getString("token",null);
        int savedId = sharedPref.getInt("id", 0);
        int tipoDestino = sharedPref.getInt("tipoUsuario", 0);
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
