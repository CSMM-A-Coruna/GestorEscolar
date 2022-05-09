package com.csmm.gestorescolar.screens.main.ui.comunicaciones;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle.ComunicacionDetalleNueva;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.ComunicacionesAdapter;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones.CustomLinearLayoutManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComunicacionesFragment extends Fragment {

    private ComunicacionesFragmentBinding binding;
    private final List<ComunicacionDTO> toggleList = new ArrayList<>();
    private final List<ComunicacionDTO> allList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipLayout;
    private ComunicacionesAdapter mAdapter;
    private ImageButton btnFiltrarPorPropiedad;
    BottomNavigationView navButton;
    private Chip chipFiltro;
    private SharedPreferences sharedPreferences;
    private String currentNav;
    private boolean isScrolling;
    PopupMenu popupMenuFiltroPropiedad, popupMenuFiltroAlumno;

    private String alumnoFiltrado = "Todos";
    private String propiedadFiltrada = "Todos";


    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = requireContext().getSharedPreferences("comunicaciones", Context.MODE_PRIVATE);
        currentNav = "recibidos";

        binding = ComunicacionesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Asignamos el swipe, botones y demás
        swipLayout = root.findViewById(R.id.swipe_layout);
        MaterialButton btnFiltrarPorAlumno = root.findViewById(R.id.btnFiltrarAlumnos);
        navButton = root.findViewById(R.id.bottom_navigation);
        FloatingActionButton nuevaComButton = root.findViewById(R.id.nuevaComunicacionButton);
        btnFiltrarPorPropiedad = root.findViewById(R.id.btnFiltros);
        chipFiltro = root.findViewById(R.id.chipFiltradoAlumnos);


        // Animaciones de los botones
        btnFiltrarPorAlumno.setAlpha(0f);
        btnFiltrarPorAlumno.animate().alpha(1f).setDuration(1000);
        btnFiltrarPorPropiedad.setAlpha(0f);
        btnFiltrarPorPropiedad.animate().alpha(1f).setDuration(1000);

        // Animación fab
        nuevaComButton.setScaleX(0);
        nuevaComButton.setScaleY(0);
        final Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                android.R.interpolator.fast_out_slow_in);
        nuevaComButton.animate()
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(interpolador)
                .setDuration(600)
                .setStartDelay(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {}

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });

        nuevaComButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ComunicacionDetalleNueva.class);
            startActivity(intent);
        });

        // Selector de alumnos
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            listAlumnos.add("Todos");
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

        btnFiltrarPorAlumno.setOnClickListener(view -> {
            // Animación
            btnFiltrarPorAlumno.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_flecha_arriba));
            // PopUp
            popupMenuFiltroAlumno = new PopupMenu(getContext(), view);
            popupMenuFiltroAlumno.setOnMenuItemClickListener(item -> {
                for(int i=0; i<arrayAlumnos.length; i++) {
                    if(arrayAlumnosSoloNombre[i].contentEquals(item.getTitle())) {
                        alumnoFiltrado = arrayAlumnos[i];
                    }
                }
                filterData();
                return true;
            });

            for (String s : arrayAlumnosSoloNombre) {
                popupMenuFiltroAlumno.getMenu().add(s);
            }
            popupMenuFiltroAlumno.show();

            // Animación al cerrar el menu
            popupMenuFiltroAlumno.setOnDismissListener(popupMenu1 -> btnFiltrarPorAlumno.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_flecha_abajo)));
        });

        btnFiltrarPorPropiedad.setOnClickListener(view -> {
            // Animación
            btnFiltrarPorPropiedad.animate().setDuration(300).rotationBy(90f).start();
            // PopUp
            popupMenuFiltroPropiedad = new PopupMenu(getContext(), view);
            popupMenuFiltroPropiedad.setOnMenuItemClickListener(item -> {
                propiedadFiltrada = String.valueOf(item.getTitle());
                filterData();
                return true;
            });
            String[] filtrosDisponibles = {"Todos", "No leídos", "Leídos", "Importantes"};
            for (String s : filtrosDisponibles) {
                popupMenuFiltroPropiedad.getMenu().add(s);
            }
            popupMenuFiltroPropiedad.show();

            // Animación al cerrar el menu
            popupMenuFiltroPropiedad.setOnDismissListener(popupMenu12 -> btnFiltrarPorPropiedad.animate().setDuration(300).rotationBy(-90f).start());
        });

        // Asignamos el RecyclerView de la lista de comunicaciones
        mRecyclerView = root.findViewById(R.id.recyclerView);
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(customLinearLayoutManager);
        mAdapter = new ComunicacionesAdapter(getContext(), allList);
        mRecyclerView.setAdapter(mAdapter);

        navButton.setOnItemSelectedListener(item -> {
            if(!isScrolling) {
                if(item.getItemId() == R.id.enviados) {
                    currentNav = "enviados";
                    updateToEnviadas();
                } else if(item.getItemId() == R.id.recibidos) {
                    currentNav = "recibidos";
                    updateToRecibidos();
                } else if(item.getItemId() == R.id.papelera) {
                    currentNav = "papelera";
                    updateToPapelera();
                }
                return true;
            } else {
                return false;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isScrolling = false;
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isScrolling = true;
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // Listener de scroll hacia los lados, para cambiar de pantallas.
        /*container.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    float deltaX = x2 - x1;
                    if (deltaX < 0) {
                        switch (currentNav) {
                            case "recibidos":
                                currentNav = "enviados";
                                updateToEnviadas();
                                navButton.setSelectedItemId(R.id.enviados);
                                break;
                            case "enviados":
                                currentNav = "papelera";
                                updateToPapelera();
                                navButton.setSelectedItemId(R.id.papelera);
                                break;
                        }
                    }else if(deltaX >0){
                        switch (currentNav) {
                            case "papelera":
                                currentNav = "enviados";
                                updateToEnviadas();
                                navButton.setSelectedItemId(R.id.enviados);
                                break;
                            case "enviados":
                                currentNav = "recibidos";
                                updateToRecibidos();
                                navButton.setSelectedItemId(R.id.recibidos);
                                break;
                        }
                    }
                    break;
            }
            return false;
        });*/
        return root;
    }

    private void filterData() {
        if(propiedadFiltrada.equals("Todos") && alumnoFiltrado.equals("Todos")) {
            updateData(allList);
            chipFiltro.setVisibility(View.GONE);
        } else if(!alumnoFiltrado.equals("Todos")) {
            chipFiltro.setVisibility(View.VISIBLE);
            chipFiltro.setText(alumnoFiltrado);
            List<ComunicacionDTO> lista = new ArrayList<>();
            switch (propiedadFiltrada) {
                case "No leídos":
                    chipFiltro.setText(String.format("%s - %s", alumnoFiltrado, propiedadFiltrada));
                    allList.forEach(data -> {
                        if (data.getLeida().equals("null")) {
                            lista.add(data);
                        }
                    });
                    break;
                case "Leídos":
                    chipFiltro.setText(String.format("%s - %s", alumnoFiltrado, propiedadFiltrada));
                    allList.forEach(data -> {
                        if (!data.getLeida().equals("null")) {
                            lista.add(data);
                        }
                    });
                    break;
                case "Importantes":
                    chipFiltro.setText(String.format("%s - %s", alumnoFiltrado, propiedadFiltrada));
                    allList.forEach(data -> {
                        if (data.isImportante()) {
                            lista.add(data);
                        }
                    });
                    break;
                default:
                    lista.addAll(allList);
                    break;
            }
            filtroAlumno(lista, alumnoFiltrado);
        } else {
            toggleList.clear();
            chipFiltro.setVisibility(View.VISIBLE);
            chipFiltro.setText(propiedadFiltrada);
            switch (propiedadFiltrada) {
                case "No leídos":
                    allList.forEach(data -> {
                        if (data.getLeida().equals("null")) {
                            toggleList.add(data);
                        }
                    });
                    break;
                case "Leídos":
                    allList.forEach(data -> {
                        if (!data.getLeida().equals("null")) {
                            toggleList.add(data);
                        }
                    });
                    break;
                case "Importantes":
                    allList.forEach(data -> {
                        if (data.isImportante()) {
                            toggleList.add(data);
                        }
                    });
                    break;
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipLayout.setOnRefreshListener(this::cargarDatos);
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        // Prevenimos errores al recargar nuevas comunicaciones y que estén filtradas. PopUps nulos (Se ha refrescado antes de hacer llenar el menu)
        try {
            if(!propiedadFiltrada.equals("Todos") || !alumnoFiltrado.equals("Todos")) {
                reiniciarFiltro();
            }
            new CargarNuevasComunicaciones().execute();
        } catch (Exception e) {
            swipLayout.setRefreshing(false);
            e.printStackTrace();
        }
    }


    private class CargarNuevasComunicaciones extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                switch (currentNav) {
                    case "enviados":
                        updateToEnviadas();
                        break;
                    case "recibidos":
                        updateToRecibidos();
                        break;
                    case "papelera":
                        updateToPapelera();
                        break;
                }
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

    private void updateToRecibidos() {
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
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(mRecyclerView, "No has recibido ninguna comunicación", Snackbar.LENGTH_SHORT).show();
                }
                updateData(allList);
            }
        });
        btnFiltrarPorPropiedad.setVisibility(View.VISIBLE);
    }

    private void updateToEnviadas() {
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
                    Snackbar.make(mRecyclerView, "No has enviado ninguna comunicación", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mRecyclerView, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                }
                updateData(allList);
            }
        });
        btnFiltrarPorPropiedad.setVisibility(View.INVISIBLE);
    }

    private void updateToPapelera() {
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
                    Snackbar.make(mRecyclerView, "No tienes ninguna comunicación en la papelera", Snackbar.LENGTH_SHORT).show();
                }
                updateData(allList);
            }
        });
        btnFiltrarPorPropiedad.setVisibility(View.INVISIBLE);
    }

    private void reiniciarFiltro() {
        propiedadFiltrada = "Todos";
        popupMenuFiltroPropiedad.getMenu().findItem(0).setChecked(true);
        System.out.println(popupMenuFiltroPropiedad.getMenu().findItem(0).isEnabled());
        alumnoFiltrado = "Todos";
        popupMenuFiltroAlumno.getMenu().findItem(0).setChecked(true);
        System.out.println(popupMenuFiltroAlumno.getMenu().findItem(0).isEnabled());
        chipFiltro.setText("");
        chipFiltro.setVisibility(View.GONE);
    }
}