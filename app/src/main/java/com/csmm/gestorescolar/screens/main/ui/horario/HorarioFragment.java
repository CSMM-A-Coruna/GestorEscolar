package com.csmm.gestorescolar.screens.main.ui.horario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HorarioFragment extends Fragment {

    private HorarioFragmentBinding binding;
    private MaterialButton btnFiltrarPorAlumno;
    private RecyclerView mRecyclerView;
    private MaterialButtonToggleGroup toggleDias;
    private HorarioAdapter mAdapter;
    private final List<HorarioData> horario = new ArrayList<>();
    private final List<HorarioData> horarioToggle = new ArrayList<>();
    PopupMenu popupMenuFiltroAlumno;

    private String alumnoFiltrado;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = HorarioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        toggleDias = root.findViewById(R.id.toggleButton);

        toggleDias.addOnButtonCheckedListener((group, checkedId, isChecked) -> filtrarHorarioPorDia());

        btnFiltrarPorAlumno = root.findViewById(R.id.btnFiltrarAlumnos);
        // Animaciones de los botones
        btnFiltrarPorAlumno.setAlpha(0f);
        btnFiltrarPorAlumno.animate().alpha(1f).setDuration(1000);

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
            @SuppressLint("NonConstantResourceId")
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
                        filtrarHorarioPorDia();
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


        root.setOnTouchListener((v, event) -> {
            gesture.onTouchEvent(event);
            return true;
        });

        mRecyclerView.setOnTouchListener((view, event) -> {
            gesture.onTouchEvent(event);
            return true;
        });

        // Selector de alumnos
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            if(alumnos.length() > 1) {
                for(int i = 0; i < alumnos.length(); i++) {
                    JSONObject json = alumnos.getJSONObject(i);
                    listAlumnos.add(json.getString("nombre"));
                }
                btnFiltrarPorAlumno.setVisibility(View.VISIBLE);
                String[] arrayAlumnos = new String[listAlumnos.size()];
                for(int j =0;j<listAlumnos.size();j++){
                    arrayAlumnos[j] = listAlumnos.get(j);
                }
                String[] arrayAlumnosSoloNombre = new String[listAlumnos.size()];
                for(int i=0; i<listAlumnos.size(); i++) {
                    String nombre = listAlumnos.get(i);
                    String[] splited = nombre.split("\\s+");
                    arrayAlumnosSoloNombre[i] = splited[0];
                }

                alumnoFiltrado = arrayAlumnos[0];
                btnFiltrarPorAlumno.setText(arrayAlumnosSoloNombre[0]);

                btnFiltrarPorAlumno.setOnClickListener(view -> {
                    // Animación
                    btnFiltrarPorAlumno.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_flecha_arriba));
                    // PopUp
                    popupMenuFiltroAlumno = new PopupMenu(getContext(), view);
                    popupMenuFiltroAlumno.setOnMenuItemClickListener(item -> {
                        for(int i=0; i<arrayAlumnos.length; i++) {
                            if(arrayAlumnosSoloNombre[i].contentEquals(item.getTitle())) {
                                alumnoFiltrado = arrayAlumnos[i];
                                btnFiltrarPorAlumno.setText(arrayAlumnosSoloNombre[i]);
                            }
                        }
                        getHorarioFromServer(alumnoFiltrado);
                        return true;
                    });

                    for (String s : arrayAlumnosSoloNombre) {
                        popupMenuFiltroAlumno.getMenu().add(s);
                    }
                    popupMenuFiltroAlumno.show();

                    // Animación al cerrar el menu
                    popupMenuFiltroAlumno.setOnDismissListener(popupMenu1 -> btnFiltrarPorAlumno.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_flecha_abajo)));
                });
            } else {
                alumnoFiltrado = alumnos.getJSONObject(0).getString("nombre");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getHorarioFromServer(alumnoFiltrado);

        return root;
    }

    private void getHorarioFromServer(String alumno) {
        int idAlumno = -1;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            for (int i = 0; i < alumnos.length(); i++) {
                JSONObject json = alumnos.getJSONObject(i);
                if(alumno.equals(json.getString("nombre")))
                    idAlumno = json.getInt("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int finalIdAlumno = idAlumno;
        RestClient.getInstance(requireContext()).getHorario(idAlumno, new GetHorarioResponseHandler() {
            @Override
            public void requestDidComplete(HorarioDTO response) {
                updateHorario(response.dataToHorarioData());
            }

            @Override
            public void requestDidFail(int statusCode) {
                if(statusCode!=404) {
                    try {
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("horario", Context.MODE_PRIVATE);
                        JSONObject json = new JSONObject(sharedPreferences.getString(String.valueOf(finalIdAlumno), null));
                        HorarioDTO horarioDTO = new HorarioDTO(json);
                        Snackbar.make(mRecyclerView, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                        updateHorario(horarioDTO.dataToHorarioData());
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(mRecyclerView, "No hemos podido recuperar tu horario...", Snackbar.LENGTH_SHORT).show();
                    updateHorario(null);
                }
            }
        });
    }
    private void updateHorario(List<HorarioData> data) {
        horario.clear();
        if(data!=null)
            horario.addAll(data);
        filtrarHorarioPorDia();
    }

    @SuppressLint("NonConstantResourceId")
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