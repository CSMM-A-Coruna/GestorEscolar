package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class ComunicacionDTO {
    private int idComunicacion;
    private int idRemite;
    private int idDestino;
    private String alumnoAsociado;
    private String tipoRemite;
    private String tipoDestino;
    private String asunto;
    private String texto;
    private String fecha;
    private String nombreRemite;
    private String nombreDestino;

    public ComunicacionDTO(JSONObject jsonObject) {
        try {
            this.idComunicacion = jsonObject.getInt("idComunicacion");
            this.idRemite = jsonObject.getInt("idRemite");
            this.idDestino = jsonObject.getInt("idDestino");
            this.alumnoAsociado = jsonObject.getString("alumnoAsociado");
            this.tipoRemite = jsonObject.getString("tipoRemite");
            this.tipoDestino = jsonObject.getString("tipoDestino");
            this.asunto = jsonObject.getString("asunto");
            this.texto = jsonObject.getString("texto");
            this.fecha = jsonObject.getString("fecha");
            this.nombreRemite = jsonObject.getString("nombreRemite");
            this.nombreDestino = jsonObject.getString("nombreDestino");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public int getIdComunicacion() {
        return idComunicacion;
    }

    public int getIdRemite() {
        return idRemite;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public String getAlumnoAsociado() {
        return alumnoAsociado;
    }

    public String getTipoRemite() {
        return tipoRemite;
    }

    public String getTipoDestino() {
        return tipoDestino;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getTexto() {
        return texto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNombreRemite() {
        return nombreRemite;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }
}
