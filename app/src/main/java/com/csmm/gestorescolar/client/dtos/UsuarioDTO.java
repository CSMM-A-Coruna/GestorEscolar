package com.csmm.gestorescolar.client.dtos;

import com.csmm.gestorescolar.client.helpers.JWTUtils;

import org.json.JSONObject;

public class UsuarioDTO {

    private int id;
    private String usuario;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String nacimiento;
    private String dni;
    private String oa;
    private int accesos;
    private int tipoUsuario;
    private String token;
    private JSONObject tokenDecoded;

    public UsuarioDTO(JSONObject token) {
        try {
            this.token = token.getString("token");
            this.tokenDecoded = JWTUtils.decoded(this.token);
            this.id = tokenDecoded.getInt("id");
            this.usuario = tokenDecoded.getString("usuario");
            this.nombre = tokenDecoded.getString("nombre");
            this.apellido1 = tokenDecoded.getString("apellido1");
            this.apellido2 = tokenDecoded.getString("apellido2");
            this.nacimiento = tokenDecoded.getString("nacimiento");
            this.dni = tokenDecoded.getString("nacimiento");
            this.oa = tokenDecoded.getString("oa");
            this.accesos = tokenDecoded.getInt("accesos");
            switch (tokenDecoded.getString("tipoUsuario")) {
                case "alumnos":
                    this.tipoUsuario = 1;
                    break;
                case "familias":
                    this.tipoUsuario = 2;
                    break;
                case "profesores":
                    this.tipoUsuario = 3;
                    break;
                case "administradores":
                    this.tipoUsuario = 4;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public String getOa() {
        return oa;
    }

    public int getAccesos() {
        return accesos;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public JSONObject getTokenDecoded() {
        return tokenDecoded;
    }
}
