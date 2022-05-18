package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

public class DocumentoDTO {

    private int idDocumento;
    private String documento;
    private String enlace;
    private String categoria;
    private String fecha;
    private String tipoDocumento;

    public DocumentoDTO(JSONObject json) {
        try {
            this.idDocumento = json.getInt("id_documento");
            this.documento = json.getString("documento");
            this.enlace =  json.getString("enlace");
            this.categoria = json.getString("categoria");
            String[] splited = json.getString("fecha").split("T");
            this.fecha = splited[0];
            this.tipoDocumento = calcularTipoDocumento(json.getString("enlace"));
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    private String calcularTipoDocumento(String enlace) {
        try {
            String[] splited = enlace.split("/");
            String documento = splited[splited.length-1];
            String[] splited2 = documento.split("\\.");
            return splited2[splited2.length-1];
        } catch (Exception e) {
            e.printStackTrace();
            return "default";
        }
    }
}
