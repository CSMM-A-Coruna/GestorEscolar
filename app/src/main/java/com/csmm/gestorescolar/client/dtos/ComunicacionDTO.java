package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class ComunicacionDTO {
    private int idComunicacion;
    private int idRemite;
    private int idDestino;
    private String tipoRemite;
    private String tipoDestino;
    private int idAlumnoAsociado;
    private String asunto;
    private String texto;
    private boolean importante;
    private String fecha;
    private String leida;
    private String eliminado;
    private String alumnoAsociado;
    private String nombreRemite;
    private String nombreDestino;

    public ComunicacionDTO(JSONObject jsonObject) {
        try {
            this.idComunicacion = jsonObject.getInt("id_comunicacion");
            this.idRemite = jsonObject.getInt("id_remite");
            this.idDestino = jsonObject.getInt("id_destino");
            this.tipoRemite = jsonObject.getString("tipo_remite");
            this.tipoDestino = jsonObject.getString("tipo_destino");
            this.idAlumnoAsociado = jsonObject.getInt("id_alumnoAsociado");
            this.asunto = jsonObject.getString("asunto");
            this.texto = jsonObject.getString("texto");
            if(jsonObject.getInt("importante") == 0) {
                this.importante = false;
            } else {
                this.importante = true;
            }
            this.fecha = formatearFecha(jsonObject.getString("fecha"));
            this.leida = jsonObject.getString("leida");
            this.eliminado = jsonObject.getString("eliminado");
            this.alumnoAsociado = jsonObject.getString("nombre_alumnoAsociado");
            this.nombreRemite = jsonObject.getString("nombreRemite");
            this.nombreDestino = jsonObject.getString("nombreDestino");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    private String formatearFecha(String fecha) {
        try {
            java.util.Date msgDate = Date.from(Instant.parse(fecha));
            java.util.Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, -1);
            String finalDate;
            if(msgDate.before(calendar.getTime())) {
                String day, month;
                SimpleDateFormat df = new SimpleDateFormat("dd");
                day = df.format(msgDate);
                df = new SimpleDateFormat("MM");
                month = df.format(msgDate);
                df = new SimpleDateFormat("yyyy");
                String year = df.format(msgDate);
                finalDate = day+"/"+month+"/"+year;
            } else {
                String hour, minutes;
                SimpleDateFormat df = new SimpleDateFormat("HH");
                hour = df.format(msgDate);
                df = new SimpleDateFormat("mm");
                minutes = df.format(msgDate);
                finalDate = hour+":"+minutes;
            }
            return finalDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public void setImportante(boolean importante) {
        this.importante = importante;
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

    public String getTipoRemite() {
        return tipoRemite;
    }

    public String getTipoDestino() {
        return tipoDestino;
    }

    public int getIdAlumnoAsociado() {
        return idAlumnoAsociado;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isImportante() {
        return importante;
    }

    public String getFecha() {
        return fecha;
    }

    public String getLeida() {
        return leida;
    }

    public String getEliminado() {
        return eliminado;
    }

    public String getAlumnoAsociado() {
        return alumnoAsociado;
    }

    public String getNombreRemite() {
        return nombreRemite;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }
}
