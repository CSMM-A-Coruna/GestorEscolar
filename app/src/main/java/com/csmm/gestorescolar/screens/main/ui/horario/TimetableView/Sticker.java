package com.csmm.gestorescolar.screens.main.ui.horario.TimetableView;

import android.widget.TextView;

import java.io.Serializable;

public class Sticker implements Serializable {
    private TextView view;
    private Schedule schedule;
    private String color;

    public Sticker() {
    }

    public void addColor(String color) {
        this.color = color;
    }
    public void addTextView(TextView v){
        this.view = v;
    }

    public void addSchedule(Schedule schedule){
        this.schedule = schedule;
    }


    public String getColors() {
        return color;
    }

    public TextView getView() {
        return view;
    }

    public Schedule getSchedules() {
        return schedule;
    }
}
