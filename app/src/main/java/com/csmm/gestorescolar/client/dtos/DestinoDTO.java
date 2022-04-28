package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class DestinoDTO {

    private int id;
    private String nombre;
    private String tipoUsuario;
    private int tipoDestino;

    public DestinoDTO(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.nombre = json.getString("nombre");
            this.tipoUsuario = json.getString("tipo_usuario");
            this.tipoDestino = calcularTipoDestino();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //!TODO Modificar los parámetros de enfermería, informática y adminstración. (Pendiente de API y BBDD también)
    private int calcularTipoDestino() {
        switch (tipoUsuario){
            case "tutor":
                return 3;
            case "enfermería":
                return 0;
            case "informática":
                return -1;
            case "administración":
                return -2;
            default:
                return '3';
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public int getTipoDestino() {
        return tipoDestino;
    }
}
