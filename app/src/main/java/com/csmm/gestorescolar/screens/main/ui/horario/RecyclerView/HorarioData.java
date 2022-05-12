package com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView;

public class HorarioData {

    private String horaInicio;
    private String horaFinal;
    private String asignatura;
    private String profesor;

    public HorarioData(String horaInicio, String horaFinal, String asignatura, String profesor) {
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.asignatura = asignatura;
        this.profesor = profesor;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public String getProfesor() {
        return profesor;
    }
}