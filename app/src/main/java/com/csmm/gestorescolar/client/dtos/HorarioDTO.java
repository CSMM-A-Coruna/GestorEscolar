package com.csmm.gestorescolar.client.dtos;

import com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView.HorarioData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HorarioDTO {

    private JSONArray lunes, martes, miercoles, jueves, viernes;

    public HorarioDTO(JSONObject json) {
        try {
            this.lunes = json.getJSONArray("lunes");
            this.martes = json.getJSONArray("martes");
            this.miercoles = json.getJSONArray("miercoles");
            this.jueves = json.getJSONArray("jueves");
            this.viernes = json.getJSONArray("viernes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getLunes() {
        return lunes;
    }

    public JSONArray getMartes() {
        return martes;
    }

    public JSONArray getMiercoles() {
        return miercoles;
    }

    public JSONArray getJueves() {
        return jueves;
    }

    public JSONArray getViernes() {
        return viernes;
    }

    public List<HorarioData> dataToHorarioData() {
        List<HorarioData> list = new ArrayList<>();
        try {
            for(int i=0; i<lunes.length(); i++) {
                HorarioData horarioData = new HorarioData(
                        lunes.getJSONObject(i).getString("inicio"),
                        lunes.getJSONObject(i).getString("fin"),
                        lunes.getJSONObject(i).getString("materia"),
                        lunes.getJSONObject(i).getString("profesor"),
                        0);
                list.add(horarioData);
            }
            for(int i=0; i<martes.length(); i++) {
                HorarioData horarioData = new HorarioData(
                        martes.getJSONObject(i).getString("inicio"),
                        martes.getJSONObject(i).getString("fin"),
                        martes.getJSONObject(i).getString("materia"),
                        martes.getJSONObject(i).getString("profesor"),
                        1);
                list.add(horarioData);
            }
            for(int i=0; i<miercoles.length(); i++) {
                HorarioData horarioData = new HorarioData(
                        miercoles.getJSONObject(i).getString("inicio"),
                        miercoles.getJSONObject(i).getString("fin"),
                        miercoles.getJSONObject(i).getString("materia"),
                        miercoles.getJSONObject(i).getString("profesor"),
                        2);
                list.add(horarioData);
            }
            for(int i=0; i<jueves.length(); i++) {
                HorarioData horarioData = new HorarioData(
                        jueves.getJSONObject(i).getString("inicio"),
                        jueves.getJSONObject(i).getString("fin"),
                        jueves.getJSONObject(i).getString("materia"),
                        jueves.getJSONObject(i).getString("profesor"),
                        3);
                list.add(horarioData);
            }
            for(int i=0; i<viernes.length(); i++) {
                HorarioData horarioData = new HorarioData(
                        viernes.getJSONObject(i).getString("inicio"),
                        viernes.getJSONObject(i).getString("fin"),
                        viernes.getJSONObject(i).getString("materia"),
                        viernes.getJSONObject(i).getString("profesor"),
                        4);
                list.add(horarioData);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            return null;
        }
        return list;
    }
}
