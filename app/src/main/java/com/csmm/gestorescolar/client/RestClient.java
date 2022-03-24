package com.csmm.gestorescolar.client;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;
import com.csmm.gestorescolar.client.handlers.DefaultErrorHandler;
import com.csmm.gestorescolar.client.handlers.LoginResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {
    private RequestQueue queue;
    private Context context;


    private final String REST_API_BASE_URL = "http://192.168.28.33:3000/v1";

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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UsuarioDTO usuarioDTO = new UsuarioDTO(response);
                        handler.sessionRequestDidComplete(usuarioDTO);
                        //Guardo esos valores en un objeto SharedPreferences usando su Editor
                        SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
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
                        editor.putString("tipoUsuario", usuarioDTO.getTipoUsuario());
                        editor.commit();
                    }
            }, new DefaultErrorHandler(handler)
        );
        queue.add(request);
    }
}
