package com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView;

public class HorarioData {

    private String horaInicio;
    private String horaFinal;
    private String asignatura;
    private String profesor;
    private int dia;

    public HorarioData(String horaInicio, String horaFinal, String asignatura, String profesor, int dia) {
        String[] splited = horaInicio.split(":");
        this.horaInicio = splited[0] + ":" + splited[1];
        splited = horaFinal.split(":");
        this.horaFinal = splited[0] + ":" + splited[1];
        this.asignatura = asignatura;
        this.profesor = profesor;
        this.dia = dia;
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

    public int getDia() {
        return dia;
    }
}