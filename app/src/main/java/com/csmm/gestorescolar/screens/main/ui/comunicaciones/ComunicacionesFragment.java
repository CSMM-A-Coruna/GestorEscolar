package com.csmm.gestorescolar.screens.main.ui.comunicaciones;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesBorradasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesEnviadasResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetComunicacionesRecibidasResponseHandler;
import com.csmm.gestorescolar.databinding.ComunicacionesFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.ComunicacionesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComunicacionesFragment extends Fragment {

    private ComunicacionesFragmentBinding binding;
    private List<ComunicacionDTO> toggleList = new ArrayList<>();
    private List<ComunicacionDTO> allList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipLayout;
    private ComunicacionesAdapter mAdapter;
    private Button filtrarAlumnos;
    private ImageButton filtrar;
    private Chip chipFiltrarAlumno;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView navButton;
    private String currentNav;
    final String[] filtradoAlumno = {"Todos"};
    final String[] filtradoPropiedad = {"Todos"};
    final int[] alumnoChecked = {0};
    final int[] checkedItem = {0};


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("comunicaciones", Context.MODE_PRIVATE);
        currentNav = "recibidos";

        binding = ComunicacionesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Asignamos el swipe to refresh, botones y demás
        swipLayout = root.findViewById(R.id.swipe_layout);
        filtrarAlumnos = root.findViewById(R.id.btnFiltrarAlumnos);
        filtrar = root.findViewById(R.id.btnFiltros);
        chipFiltrarAlumno = root.findViewById(R.id.chipFiltradoAlumnos);
        navButton = root.findViewById(R.id.bottom_navigation);


        // Selector de alumnos
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<String>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            listAlumnos.add("Todos");
            System.out.println(alumnos);
            for(int i = 0; i < alumnos.length(); i++) {
                JSONObject json = alumnos.getJSONObject(i);
                listAlumnos.add(json.getString("nombre"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        filtrarAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(root.getContext())
                        .setTitle("Alumnos")
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alumnoChecked[0] = 0;
                                filtradoAlumno[0] = arrayAlumnos[0];
                                filterData();
                            }
                        })
                        .setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                filtradoAlumno[0] = arrayAlumnos[alumnoChecked[0]];
                                filterData();
                            }
                        })
                        .setSingleChoiceItems(arrayAlumnosSoloNombre, alumnoChecked[0], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alumnoChecked[0] = i;
                            }
                        }).create().show();
            }
        });

        String[] filtrosArray = {"Todos", "No leídos", "Leídos", "Importantes"};
        filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(root.getContext())
                        .setTitle("Filtrar")
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkedItem[0] = 0;
                                filtradoPropiedad[0] = filtrosArray[0];
                                filterData();
                            }
                        })
                        .setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(checkedItem[0]==1) {
                                    filtradoPropiedad[0] = filtrosArray[1];
                                } else if(checkedItem[0]==2){
                                    filtradoPropiedad[0] = filtrosArray[2];
                                } else if(checkedItem[0]==3) {
                                    filtradoPropiedad[0] = filtrosArray[3];
                                } else {
                                    filtradoPropiedad[0] = filtrosArray[0];
                                }
                                filterData();
                            }
                        })
                        .setSingleChoiceItems(filtrosArray, checkedItem[0], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkedItem[0] = i;
                            }
                        }).create().show();
            }
        });

        // Asignamos el RecyclerView de la lista de comunicaciones
        mRecyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ComunicacionesAdapter(getContext(), allList);
        mRecyclerView.setAdapter(mAdapter);

        navButton.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.enviados) {
                currentNav = "enviados";
                updateToEnviadas(!filtradoAlumno[0].equals("Todos"));
            } else if(item.getItemId() == R.id.recibidos) {
                currentNav = "recibidos";
                updateToRecibidos(!filtradoAlumno[0].equals("Todos"));
            } else if(item.getItemId() == R.id.papelera) {
                currentNav = "papelera";
                updateToPapelera(!filtradoAlumno[0].equals("Todos"));
            }
            return true;
        });
        return root;
    }

    private void filterData() {
        if(filtradoPropiedad[0].equals("Todos") && filtradoAlumno[0].equals("Todos")) {
            updateData(allList);
            chipFiltrarAlumno.setVisibility(View.GONE);
        } else if(!filtradoAlumno[0].equals("Todos")) {
            chipFiltrarAlumno.setVisibility(View.VISIBLE);
            chipFiltrarAlumno.setText(filtradoAlumno[0]);
            List<ComunicacionDTO> lista = new ArrayList<>();
            if(filtradoPropiedad[0].equals("No leídos")) {
                chipFiltrarAlumno.setText(filtradoAlumno[0] + " - " + filtradoPropiedad[0]);
                allList.forEach(data -> {
                    if(data.getLeida().equals("null")) {
                        lista.add(data);
                    }
                });
            } else if(filtradoPropiedad[0].equals("Leídos")) {
                chipFiltrarAlumno.setText(filtradoAlumno[0] + " - " + filtradoPropiedad[0]);
                allList.forEach(data -> {
                    if(!data.getLeida().equals("null")) {
                        lista.add(data);
                    }
                });
            } else if(filtradoPropiedad[0].equals("Importantes")) {
                chipFiltrarAlumno.setText(filtradoAlumno[0] + " - " + filtradoPropiedad[0]);
                allList.forEach(data -> {
                    if(data.isImportante()) {
                        lista.add(data);
                    }
                });
            } else {
                lista.addAll(allList);
            }
            filtroAlumno(lista, filtradoAlumno[0]);
        } else {
            toggleList.clear();
            chipFiltrarAlumno.setVisibility(View.VISIBLE);
            chipFiltrarAlumno.setText(filtradoPropiedad[0]);
            if(filtradoPropiedad[0].equals("No leídos")) {
                allList.forEach(data -> {
                    if(data.getLeida().equals("null")) {
                        toggleList.add(data);
                    }
                });
            } else if(filtradoPropiedad[0].equals("Leídos")) {
                allList.forEach(data -> {
                    if(!data.getLeida().equals("null")) {
                        toggleList.add(data);
                    }
                });
            } else if(filtradoPropiedad[0].equals("Importantes")) {
                allList.forEach(data -> {
                    if(data.isImportante()) {
                        toggleList.add(data);
                    }
                });
            }
            updateData(toggleList);
        }
    }

    private void filtroAlumno(List<ComunicacionDTO> list, String alumno) {
        toggleList.clear();
        list.forEach(data -> {
            if(data.getNombreAlumnoAsociado().equals(alumno)) {
                toggleList.add(data);
            }
        });
        updateData(toggleList);
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
                if(currentNav.equals("enviados")) {
                    updateToEnviadas(!filtradoAlumno[0].equals("Todos"));
                } else if(currentNav.equals("recibidos")) {
                    updateToRecibidos(!filtradoAlumno[0].equals("Todos"));
                } else if(currentNav.equals("papelera")) {
                    updateToPapelera(!filtradoAlumno[0].equals("Todos"));
                }
                Thread.sleep(600);
            } catch (Exception e) {
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

    private void updateData(List<ComunicacionDTO> list) {
        mAdapter.updateData(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        new CargarNuevosEmails().execute();
    }

    private void updateToRecibidos(boolean filter) {
        allList.clear();
        toggleList.clear();
        RestClient.getInstance(getContext()).getComunicacionesRecibidas(new GetComunicacionesRecibidasResponseHandler() {
            @Override
            public void sessionRequestDidComplete(List<ComunicacionDTO> response) {
                allList.addAll(response);
                updateData(allList);
            }

            @Override
            public void requestDidFail(int statusCode) {
                if(statusCode!=404) {
                    try {
                        JSONArray json = new JSONArray(sharedPreferences.getString("recibidas", null));
                        List<ComunicacionDTO> listaComunicaciones = new ArrayList<>();
                        for(int i = 0; i < json.length(); i++) {
                            try {
                                JSONObject iterationElement = json.getJSONObject(i);
                                ComunicacionDTO com = new ComunicacionDTO(iterationElement);
                                listaComunicaciones.add(com);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        allList.addAll(listaComunicaciones);
                        Snackbar.make(mRecyclerView, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(mRecyclerView, "No has recibido ninguna comunicación", Snackbar.LENGTH_SHORT).show();
                }
                updateData(allList);
            }
        });
        if(!filter) {
            filtradoAlumno[0] = "Todos";
            filtradoPropiedad[0] = "Todos";
            checkedItem[0] = 0;
            alumnoChecked[0] = 0;
        } else {
            filtradoPropiedad[0] = "Todos";
            checkedItem[0] = 0;
            filterData();
        }
        filtrar.setVisibility(View.VISIBLE);
    }

    private void updateToEnviadas(boolean filter) {
        allList.clear();
        toggleList.clear();
        RestClient.getInstance(getContext()).getComunicacionesEnviadas(new GetComunicacionesEnviadasResponseHandler() {
            @Override
            public void sessionRequestDidComplete(List<ComunicacionDTO> response) {
                allList.addAll(response);
                updateData(allList);
            }

            @Override
            public void requestDidFail(int statusCode) {
                if(statusCode!=404 && sharedPreferences.getString("enviadas", null) != null) {
                    try {
                        JSONArray json = new JSONArray(sharedPreferences.getString("enviadas", null));
                        List<ComunicacionDTO> listaComunicaciones = new ArrayList<>();
                        for (int i = 0; i < json.length(); i++) {
                            try {
                                JSONObject iterationElement = json.getJSONObject(i);
                                ComunicacionDTO com = new ComunicacionDTO(iterationElement);
                                listaComunicaciones.add(com);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        allList.addAll(listaComunicaciones);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(mRecyclerView, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                } else if(statusCode==404) {
                    Snackbar.make(mRecyclerView, "No has recibido ninguna comunicación", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mRecyclerView, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                }
                updateData(allList);
            }
        });
        if(!filter) {
            filtradoAlumno[0] = "Todos";
            filtradoPropiedad[0] = "Todos";
            checkedItem[0] = 0;
            alumnoChecked[0] = 0;
        } else {
            filtradoPropiedad[0] = "Todos";
            checkedItem[0] = 0;
            filterData();
        }
        filtrar.setVisibility(View.INVISIBLE);
    }

    private void updateToPapelera(boolean filter) {
        allList.clear();
        toggleList.clear();
        RestClient.getInstance(getContext()).getComunicacionesBorradas(new GetComunicacionesBorradasResponseHandler() {
            @Override
            public void sessionRequestDidComplete(List<ComunicacionDTO> response) {
                allList.addAll(response);
                updateData(allList);
            }

            @Override
            public void requestDidFail(int statusCode) {
                if(statusCode!=404) {
                    try {
                        JSONArray json = new JSONArray(sharedPreferences.getString("borradas", null));
                        List<ComunicacionDTO> listaComunicaciones = new ArrayList<>();
                        for(int i = 0; i < json.length(); i++) {
                            try {
                                JSONObject iterationElement = json.getJSONObject(i);
                                ComunicacionDTO com = new ComunicacionDTO(iterationElement);
                                listaComunicaciones.add(com);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        allList.addAll(listaComunicaciones);
                        Snackbar.make(mRecyclerView, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(mRecyclerView, "No has recibido ninguna comunicación", Snackbar.LENGTH_SHORT).show();
                }
                updateData(allList);
            }
        });
        if(!filter) {
            filtradoAlumno[0] = "Todos";
            filtradoPropiedad[0] = "Todos";
            checkedItem[0] = 0;
            alumnoChecked[0] = 0;
        } else {
            filtradoPropiedad[0] = "Todos";
            checkedItem[0] = 0;
            filterData();
        }
        filtrar.setVisibility(View.INVISIBLE);
    }
}