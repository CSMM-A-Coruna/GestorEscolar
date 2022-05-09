package com.csmm.gestorescolar.client.dtos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreferencesDTO {

    //!TODO Implementar una lógica menos repetitiva con listas
    private boolean autentificacion_dos_fases;
    private boolean proteccion_restablecimiento;
    private boolean not_comunicaciones_push;
    private boolean not_calificaciones_push;
    private boolean not_entrevistas_push;
    private boolean not_extraescolares_push;
    private boolean not_enfermeria_push;
    private boolean not_comunicaciones_email;
    private boolean not_calificaciones_email;
    private boolean not_entrevistas_email;
    private boolean not_extraescolares_email;
    private boolean not_enfermeria_email;

    public PreferencesDTO(JSONObject json) {
        try {
            autentificacion_dos_fases = setValue(json.getInt("autentificacion_dos_fases"));
            proteccion_restablecimiento = setValue(json.getInt("proteccion_restablecimiento"));
            not_comunicaciones_push = setValue(json.getInt("not_comunicaciones_push"));
            not_calificaciones_push = setValue(json.getInt("not_calificaciones_push"));
            not_entrevistas_push = setValue(json.getInt("not_entrevistas_push"));
            not_extraescolares_push = setValue(json.getInt("not_extraescolares_push"));
            not_enfermeria_push = setValue(json.getInt("not_enfermeria_push"));
            not_comunicaciones_email = setValue(json.getInt("not_comunicaciones_email"));
            not_calificaciones_email = setValue(json.getInt("not_calificaciones_email"));
            not_entrevistas_email = setValue(json.getInt("not_entrevistas_email"));
            not_extraescolares_email = setValue(json.getInt("not_extraescolares_email"));
            not_enfermeria_email = setValue(json.getInt("not_enfermeria_email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean setValue(int i) {
        return i != 0;
    }

    public boolean isAutentificacion_dos_fases() {
        return autentificacion_dos_fases;
    }

    public boolean isProteccion_restablecimiento() {
        return proteccion_restablecimiento;
    }

    public boolean isNot_comunicaciones_push() {
        return not_comunicaciones_push;
    }

    public boolean isNot_calificaciones_push() {
        return not_calificaciones_push;
    }

    public boolean isNot_entrevistas_push() {
        return not_entrevistas_push;
    }

    public boolean isNot_extraescolares_push() {
        return not_extraescolares_push;
    }

    public boolean isNot_enfermeria_push() {
        return not_enfermeria_push;
    }

    public boolean isNot_comunicaciones_email() {
        return not_comunicaciones_email;
    }

    public boolean isNot_calificaciones_email() {
        return not_calificaciones_email;
    }

    public boolean isNot_entrevistas_email() {
        return not_entrevistas_email;
    }

    public boolean isNot_extraescolares_email() {
        return not_extraescolares_email;
    }

    public boolean isNot_enfermeria_email() {
        return not_enfermeria_email;
    }


}
