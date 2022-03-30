package com.csmm.gestorescolar.screens.main.ui.comunicaciones;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.databinding.ComunicacionesFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.ComunicacionesAdapter;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.ComunicacionesData;
import java.util.ArrayList;
import java.util.List;

public class ComunicacionesFragment extends Fragment {

    private ComunicacionesFragmentBinding binding;
    private final List<ComunicacionesData> toggleList = new ArrayList<>();
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipLayout;
    List<ComunicacionesData> mEmailData = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //ComunicacionesViewModel comunicacionesViewModel = new ViewModelProvider(this).get(ComunicacionesViewModel.class);
        //comunicacionesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding = ComunicacionesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Asignamos el swipe to refresh
        swipLayout = root.findViewById(R.id.swipe_layout);

        // Asignamos el spinner
        Spinner spinner = root.findViewById(R.id.spinnerAlumnos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.spinner_alumnos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Asignamos el RecyclerView de la lista de comunicaciones
        mRecyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        // Creamos los datos hardcodeados (de momento)
        setHardcodedData();
        // Asignamos el adaptador
        ComunicacionesAdapter mMailAdapter = new ComunicacionesAdapter(root.getContext(), mEmailData);
        mRecyclerView.setAdapter(mMailAdapter);

        // Creamos un onListener para el spinner, y seleccionamos los datos de la lista que nos inteteresa
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                toggleList.clear();
                if (item != null) {
                    if(!item.toString().equals("Todos")){
                        mEmailData.forEach(data -> {
                            if(data.getmAlumnoAsociado().equals(item.toString())) {
                                toggleList.add(data);
                            }
                        });
                    } else {
                        toggleList.addAll(mEmailData);
                    }
                    mMailAdapter.updateData(toggleList);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipLayout.setOnRefreshListener(this::cargarDatos);
    }

    private void cargarDatos() {
        new CargarNuevosEmails().execute();
    }

    private class CargarNuevosEmails extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipLayout.setRefreshing(false);
        }
    }

    private void setHardcodedData() {
        ComunicacionesData mEmail = new ComunicacionesData("Juan", "Juan Rodríguez",  "Reunión",
                "Texto de prueba....",
                "10:42am", false, false);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Juan Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", false, true);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Juan Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, true);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Juan Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, false);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Juan Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, false);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Juan Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, false);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Iria Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, false);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Iria Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, false);
        mEmailData.add(mEmail);
        mEmail = new ComunicacionesData("Sandra", "Iria Rodríguez", "Calificaciones 2ºTrimestre",
                "Adjunto las calificaciones del segundo trimestre de Historia",
                "16:04pm", true, false);
        mEmailData.add(mEmail);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}