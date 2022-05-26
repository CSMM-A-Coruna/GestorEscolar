package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class LlaveroDTO {

    private int id;
    private int idAlumno;
    private String aplicacion;
    private String usuario;
    private String email;
    private String contraseña;
    private boolean modificable;

    public LlaveroDTO(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.idAlumno = json.getInt("id_alumno");
            this.aplicacion = json.getString("aplicacion");
            this.usuario = json.getString("usuario");
            this.email = json.getString("email");
            this.contraseña = json.getString("contraseña");
            this.modificable = json.getInt("modificable") == 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getEmail() {
        return email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean isModificable() {
        return modificable;
    }
}
