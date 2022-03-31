package com.csmm.gestorescolar.screens.main.ui.comunicaciones;

import android.content.Context;
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
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.client.handlers.CommsRecibidasResponseHandler;
import com.csmm.gestorescolar.databinding.ComunicacionesFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.ComunicacionesAdapter;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.ComunicacionesData;
import java.util.ArrayList;
import java.util.List;

public class ComunicacionesFragment extends Fragment {

    private ComunicacionesFragmentBinding binding;
    private final List<ComunicacionDTO> toggleList = new ArrayList<>();
    List<ComunicacionDTO> mEmailData = new ArrayList<>();
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipLayout;
    ComunicacionesAdapter mMailAdapter;
    Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //ComunicacionesViewModel comunicacionesViewModel = new ViewModelProvider(this).get(ComunicacionesViewModel.class);
        //comunicacionesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding = ComunicacionesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Asignamos el swipe to refresh
        swipLayout = root.findViewById(R.id.swipe_layout);

        // Asignamos el spinner
        spinner = root.findViewById(R.id.spinnerAlumnos);
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
        // Asignamos el adaptador
        RestClient.getInstance(root.getContext()).getComunicacionesRecibidas(new CommsRecibidasResponseHandler() {
            @Override
            public void sessionRequestDidComplete(List<ComunicacionDTO> response) {
                mEmailData = response;
                mMailAdapter = new ComunicacionesAdapter(root.getContext(), mEmailData);
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
                                    if(data.getAlumnoAsociado().equals(item.toString())) {
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
            }

            @Override
            public void requestDidFail(int statusCode) {
                System.out.println(statusCode);
            }
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

    private class CargarNuevosEmails extends AsyncTask<List<ComunicacionDTO>, List<ComunicacionDTO>, List<ComunicacionDTO>> {
        List<ComunicacionDTO> newComs = new ArrayList<>();
        final Object async = new Object();
        @Override
        protected List<ComunicacionDTO> doInBackground(List<ComunicacionDTO>... params) {
            try {
                RestClient.getInstance(getContext()).getComunicacionesRecibidas(new CommsRecibidasResponseHandler() {
                    @Override
                    public void sessionRequestDidComplete(List<ComunicacionDTO> response) {
                        newComs = response;
                        CargarNuevosEmails.this.async.notify();
                    }

                    @Override
                    public void requestDidFail(int statusCode) {
                        CargarNuevosEmails.this.async.notify();
                        newComs = null;
                    }
                });
                synchronized (CargarNuevosEmails.this.async) {
                    try {
                        CargarNuevosEmails.this.async.wait();
                        return newComs;
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ComunicacionDTO> aVoid) {
            super.onPostExecute(aVoid);
            updateData(aVoid);
            swipLayout.setRefreshing(false);
        }
    }

    /*private void setHardcodedData() {
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
    }*/

    private void updateData(List<ComunicacionDTO> list) {
        mMailAdapter.updateData(list);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}