package com.csmm.gestorescolar.screens.main.ui.horario;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HorarioFragment extends Fragment {

    private HorarioFragmentBinding binding;
    private RecyclerView mRecyclerView;
    private MaterialButtonToggleGroup toggleDias;
    private HorarioAdapter mAdapter;
    private final List<HorarioData> horario = new ArrayList<>();
    private final int[] diasId = {R.id.lunes, R.id.martes, R.id.miercoles, R.id.jueves, R.id.viernes};
    private final String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
    // 0 = Lunes, 1 = Martes...etc
    private int diaChecked = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = HorarioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        toggleDias = root.findViewById(R.id.toggleButton);

        toggleDias.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                System.out.println(diaChecked);
                switch (checkedId) {
                    case R.id.lunes:
                        diaChecked = 0;
                        updateRecyclerView();
                        break;
                    case R.id.martes:
                        diaChecked = 1;
                        updateRecyclerView();
                        break;
                    case R.id.miercoles:
                        diaChecked = 2;
                        updateRecyclerView();
                        break;
                    case R.id.jueves:
                        diaChecked = 3;
                        updateRecyclerView();
                        break;
                    case R.id.viernes:
                        diaChecked = 4;
                        updateRecyclerView();
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

        // Implementación de detector de gestos (para que al desplazar se desplace en los botones también)
        final GestureDetector gesture = new GestureDetector(requireActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            //!TODO OJO, está función estaba mucho mas compacta con otro sistema, pero no lo di hecho funcionar. Queda pendiente de refactor para establecer un código más limpio.
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                final int SWIPE_MIN_DISTANCE = 120;
                final int SWIPE_MAX_OFF_PATH = 250;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                        return false;
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        // Derecha a izquierda
                        switch (toggleDias.getCheckedButtonId()) {
                            case R.id.viernes:
                                System.out.println("No rotamos, ultimo botón");
                                break;
                            case R.id.jueves:
                                toggleDias.check(R.id.viernes);
                                break;
                            case R.id.miercoles:
                                toggleDias.check(R.id.jueves);
                                break;
                            case R.id.martes:
                                toggleDias.check(R.id.miercoles);
                                break;
                            case R.id.lunes:
                                toggleDias.check(R.id.martes);
                                break;
                        }
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        // Izquierda a derecha
                        switch (toggleDias.getCheckedButtonId()) {
                            case R.id.viernes:
                                toggleDias.check(R.id.jueves);
                                break;
                            case R.id.jueves:
                                toggleDias.check(R.id.miercoles);
                                break;
                            case R.id.miercoles:
                                toggleDias.check(R.id.martes);
                                break;
                            case R.id.martes:
                                toggleDias.check(R.id.lunes);
                                break;
                            case R.id.lunes:
                                System.out.println("No rotamos, primer botón");
                                break;
                        }
                    }
                } catch (Exception e) {
                    // nothing
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });


        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gesture.onTouchEvent(event);
                return true;
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                gesture.onTouchEvent(event);
                return true;
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

    private void updateRecyclerView() {
        System.out.println("Día: " + dias[diaChecked]);
        Collections.shuffle(horario);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}