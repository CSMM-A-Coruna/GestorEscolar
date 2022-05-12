package com.csmm.gestorescolar.screens.main.ui.horario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.databinding.HorarioFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.CustomLinearLayoutManager;
import com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView.HorarioAdapter;
import com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView.HorarioData;
import com.csmm.gestorescolar.screens.main.ui.horario.TimetableView.Schedule;
import com.csmm.gestorescolar.screens.main.ui.horario.TimetableView.Time;
import com.csmm.gestorescolar.screens.main.ui.horario.TimetableView.TimetableView;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HorarioFragment extends Fragment {

    private HorarioFragmentBinding binding;
    private TimetableView timetable;
    private RecyclerView mRecyclerView;
    private MaterialButtonToggleGroup toggleDias;
    private HorarioAdapter mAdapter;
    private final List<HorarioData> horario = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = HorarioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        toggleDias = root.findViewById(R.id.toggleButton);

        toggleDias.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.lunes:
                        updateRecyclerView("Lunes");
                        break;
                    case R.id.martes:
                        updateRecyclerView("Martes");
                        break;
                    case R.id.miercoles:
                        updateRecyclerView("Miercoles");
                        break;
                    case R.id.jueves:
                        updateRecyclerView("Jueves");
                        break;
                    case R.id.viernes:
                        updateRecyclerView("Viernes");
                        break;
                }
            }
        });

        // Asignamos el RecyclerView del horario
        mRecyclerView = root.findViewById(R.id.recyclerView);
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), 0));
        mRecyclerView.setLayoutManager(customLinearLayoutManager);
        mAdapter = new HorarioAdapter(getContext(), horario);
        mRecyclerView.setAdapter(mAdapter);
        setUpHorarioRecyclerView();

        timetable = root.findViewById(R.id.timetable);
        setUpEvents();
        timetable.setHeaderHighlight(1);
        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(Schedule schedule) {
                System.out.println(schedule.getClassTitle());
            }
        });


        return root;
    }

    private void setUpHorarioRecyclerView() {
        HorarioData horarioData = new HorarioData("8:30", "9:20", "Biología", "María José García");
        horario.add(horarioData);
        HorarioData horarioData1 = new HorarioData("9:20", "10:10", "Física", "Pedro Lounzas");
        horario.add(horarioData1);
        HorarioData horarioData2 = new HorarioData("10:10", "11:00", "Física", "Pedro Lounzas");
        horario.add(horarioData2);
        HorarioData horarioData3 = new HorarioData("11:00", "11:30", "Recreo", "");
        horario.add(horarioData3);
        HorarioData horarioData4 = new HorarioData("11:30", "12:20", "E.Física", "Juan Lounzas");
        horario.add(horarioData4);
        HorarioData horarioData5 = new HorarioData("12:20", "13:10", "Matemáticas", "María Dolores García");
        horario.add(horarioData5);
        HorarioData horarioData6 = new HorarioData("13:10", "14:00", "Inglés", "Montse Vázquez");
        horario.add(horarioData6);
        mAdapter.notifyDataSetChanged();
    }

    private void updateRecyclerView(String dia) {
        Collections.shuffle(horario);
        mAdapter.notifyDataSetChanged();
    }

    private void setUpEvents() {
        ArrayList<Schedule> schedules = new ArrayList<>();
        for(int i=0; i<5; i++) {
            Schedule schedule = new Schedule();
            schedule.setDay(i);
            schedule.setClassTitle("Física"); // sets subject
            schedule.setColor("#9BCAE1");
            //schedule.setClassPlace("IT-601"); // sets place
            schedule.setProfessorName("Juan Rodríguez"); // sets professor
            schedule.setStartTime(new Time(8,30)); // sets the beginning of class time (hour,minute)
            schedule.setEndTime(new Time(9,20)); // sets the end of class time (hour,minute)
            schedules.add(schedule);

            Schedule schedule1 = new Schedule();
            schedule1.setDay(i);
            schedule1.setClassTitle("Mate");
            schedule1.setColor("#E1C99B");
            //schedule1.setProfessorName("Laura García");
            schedule1.setStartTime(new Time(9, 20));
            schedule1.setEndTime(new Time(10, 10));
            schedules.add(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setDay(i);
            schedule2.setClassTitle("Biología");
            schedule2.setColor("#A1E19B");
            //schedule1.setProfessorName("Laura García");
            schedule2.setStartTime(new Time(10, 10));
            schedule2.setEndTime(new Time(11, 0));
            schedules.add(schedule2);

            Schedule schedule3 = new Schedule();
            schedule3.setDay(i);
            schedule3.setClassTitle("Recreo");
            schedule3.setColor("#E7E7E7");
            //schedule1.setProfessorName("Laura García");
            schedule3.setStartTime(new Time(11, 0));
            schedule3.setEndTime(new Time(11, 30));
            schedules.add(schedule3);

            Schedule schedule4 = new Schedule();
            schedule4.setDay(i);
            schedule4.setClassTitle("E.Física");
            schedule4.setColor("#D6B5E7");
            //schedule1.setProfessorName("Laura García");
            schedule4.setStartTime(new Time(11, 30));
            schedule4.setEndTime(new Time(12, 20));
            schedules.add(schedule4);

            Schedule schedule5 = new Schedule();
            schedule5.setDay(i);
            schedule5.setClassTitle("E.Física");
            schedule5.setColor("#D6B5E7");
            //schedule1.setProfessorName("Laura García");
            schedule5.setStartTime(new Time(12, 20));
            schedule5.setEndTime(new Time(13, 10));
            schedules.add(schedule5);

            Schedule schedule6 = new Schedule();
            schedule6.setDay(i);
            schedule6.setClassTitle("Filosofía");
            schedule6.setColor("#E6B1B1");
            //schedule1.setProfessorName("Laura García");
            schedule6.setStartTime(new Time(13, 10));
            schedule6.setEndTime(new Time(14, 0));
            schedules.add(schedule6);

            Schedule schedule7 = new Schedule();
            schedule7.setDay(i);
            schedule7.setClassTitle("Comida");
            schedule7.setColor("#E7E7E7");
            schedule7.setStartTime(new Time(14, 0));
            schedule7.setEndTime(new Time(16, 0));
            schedules.add(schedule7);
        }
        timetable.addSchedules(schedules);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}