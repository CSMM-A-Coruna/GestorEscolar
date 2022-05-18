package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class DocumentoDTO {

    private int idDocumento;
    private String documento;
    private String enlace;
    private String categoria;
    private String fecha;

    public DocumentoDTO(JSONObject json) {
        try {
            idDocumento = json.getInt("id_documento");
            documento = json.getString("documento");
            enlace =  json.getString("enlace");
            categoria = json.getString("categoria");
            fecha = json.getString("fecha");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEnlace() {
        return enlace;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getFecha() {
        return fecha;
    }
}
