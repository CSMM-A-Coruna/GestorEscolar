package com.csmm.gestorescolar.screens.main.ui.horario;

import android.annotation.SuppressLint;
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
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.HorarioDTO;
import com.csmm.gestorescolar.client.handlers.GetHorarioResponseHandler;
import com.csmm.gestorescolar.databinding.HorarioFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.CustomLinearLayoutManager;
import com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView.HorarioAdapter;
import com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView.HorarioData;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class HorarioFragment extends Fragment {

    private HorarioFragmentBinding binding;
    private RecyclerView mRecyclerView;
    private MaterialButtonToggleGroup toggleDias;
    private HorarioAdapter mAdapter;
    private List<HorarioData> horario = new ArrayList<>();
    private List<HorarioData> horarioToggle = new ArrayList<>();
    private int diaChecked;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = HorarioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        toggleDias = root.findViewById(R.id.toggleButton);

        toggleDias.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.lunes:
                        diaChecked = 0;
                        break;
                    case R.id.martes:
                        diaChecked = 1;
                        break;
                    case R.id.miercoles:
                        diaChecked = 2;
                        break;
                    case R.id.jueves:
                        diaChecked = 3;
                        break;
                    case R.id.viernes:
                        diaChecked = 4;
                        break;
                }
                filtrarHorarioPorDia();
            }
        });

        // Asignamos el RecyclerView del horario
        mRecyclerView = root.findViewById(R.id.recyclerView);
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), 0));
        mRecyclerView.setLayoutManager(customLinearLayoutManager);
        mAdapter = new HorarioAdapter(getContext(), horario);
        mRecyclerView.setAdapter(mAdapter);

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
                                // Nothing
                                break;
                            case R.id.jueves:
                                toggleDias.check(R.id.viernes);
                                diaChecked = 4;
                                break;
                            case R.id.miercoles:
                                toggleDias.check(R.id.jueves);
                                diaChecked = 3;
                                break;
                            case R.id.martes:
                                toggleDias.check(R.id.miercoles);
                                diaChecked = 2;
                                break;
                            case R.id.lunes:
                                toggleDias.check(R.id.martes);
                                diaChecked = 1;
                                break;
                        }
                        filtrarHorarioPorDia();
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        // Izquierda a derecha
                        switch (toggleDias.getCheckedButtonId()) {
                            case R.id.viernes:
                                toggleDias.check(R.id.jueves);
                                diaChecked = 3;
                                break;
                            case R.id.jueves:
                                toggleDias.check(R.id.miercoles);
                                diaChecked = 2;
                                break;
                            case R.id.miercoles:
                                toggleDias.check(R.id.martes);
                                diaChecked = 1;
                                break;
                            case R.id.martes:
                                toggleDias.check(R.id.lunes);
                                diaChecked = 1;
                                break;
                            case R.id.lunes:
                                // Nothing
                                break;
                        }
                        filtrarHorarioPorDia();
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

        RestClient.getInstance(requireContext()).getHorario(1, new GetHorarioResponseHandler() {
            @Override
            public void requestDidComplete(HorarioDTO response) {
                updateHorario(response.dataToHorarioData());
            }

            @Override
            public void requestDidFail(int statusCode) {

            }
        });

        return root;
    }

    private void updateHorario(List<HorarioData> data) {
        horario.clear();
        horario.addAll(data);
        filtrarHorarioPorDia();
    }

    private void filtrarHorarioPorDia() {
        horarioToggle.clear();
        int dia = -1;
        switch (toggleDias.getCheckedButtonId()) {
            case R.id.viernes:
                dia = 4;
                break;
            case R.id.jueves:
                dia = 3;
                break;
            case R.id.miercoles:
                dia = 2;
                break;
            case R.id.martes:
                dia = 1;
                break;
            case R.id.lunes:
                dia = 0;
                break;
        }
        for(int i=0; i<horario.toArray().length; i++) {
            if(horario.get(i).getDia() == dia) {
                horarioToggle.add(horario.get(i));
            }
        }
        mAdapter.updateData(horarioToggle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}