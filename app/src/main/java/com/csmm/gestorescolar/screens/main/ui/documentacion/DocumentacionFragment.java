package com.csmm.gestorescolar.screens.main.ui.documentacion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.DocumentoDTO;
import com.csmm.gestorescolar.client.handlers.GetAllDocumentosResponseHandler;
import com.csmm.gestorescolar.databinding.DocumentacionFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.documentacion.recyclerView.DocumentacionAdapter;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DocumentacionFragment extends Fragment {

    private DocumentacionFragmentBinding binding;
    private RecyclerView mRecyclerView;
    private DocumentacionAdapter mAdapter;
    private List<DocumentoDTO> serverList = new ArrayList<>();
    private List<DocumentoDTO> toggleList = new ArrayList<>();
    private MaterialButton btnFiltrarPorAlumno;
    private ImageButton btnFiltros;
    private PopupMenu popupMenuFiltroAlumno, popupMenuFiltro;
    private SearchView searchView;


    private String alumnoFiltrado, propiedadFiltrada;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DocumentacionFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = root.findViewById(R.id.searchView);
        btnFiltrarPorAlumno = root.findViewById(R.id.btnFiltrarAlumnos);
        btnFiltros = root.findViewById(R.id.btnFiltros);

        // Animaciones de los botones
        btnFiltrarPorAlumno.setAlpha(0f);
        btnFiltrarPorAlumno.animate().alpha(1f).setDuration(1000);
        btnFiltros.setAlpha(0f);
        btnFiltros.animate().alpha(1f).setDuration(1000);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DocumentacionAdapter(getContext(), serverList);
        mRecyclerView.setAdapter(mAdapter);

        // Botón filtros
        btnFiltros.setOnClickListener(view -> {
            // Animación
            btnFiltros.animate().setDuration(300).rotationBy(90f).start();
            // PopUp
            popupMenuFiltro = new PopupMenu(getContext(), view);
            popupMenuFiltro.setOnMenuItemClickListener(item -> {
                propiedadFiltrada = String.valueOf(item.getTitle());
                filtrarPor(propiedadFiltrada);
                return true;
            });
            String[] filtrosDisponibles = {"Todos", "Categoría", "Más reciente", "Más antiguo"};
            for (String s : filtrosDisponibles) {
                popupMenuFiltro.getMenu().add(s);
            }
            popupMenuFiltro.show();

            // Animación al cerrar el menu
            popupMenuFiltro.setOnDismissListener(popupMenu12 -> btnFiltros.animate().setDuration(300).rotationBy(-90f).start());
        });

        // Selector de alumnos
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            listAlumnos.add("Generales");
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
                    getDocumentosFromServer(alumnoFiltrado);
                    return true;
                });

                for (String s : arrayAlumnosSoloNombre) {
                    popupMenuFiltroAlumno.getMenu().add(s);
                }

                popupMenuFiltroAlumno.show();
                // Animación al cerrar el menu
                popupMenuFiltroAlumno.setOnDismissListener(popupMenu1 -> btnFiltrarPorAlumno.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_flecha_abajo)));
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            updateDocumentos(serverList);
            return false;
        });

        getDocumentosFromServer(alumnoFiltrado);

        return root;
    }

    private void filtrarPor(String propiedad) {
        switch (propiedad) {
            case "Todos":
                toggleList.clear();
                toggleList.addAll(serverList);
                updateDocumentos(toggleList);
                break;
            case "Categoría":
                toggleList.clear();
                toggleList.addAll(serverList);
                Collections.sort(toggleList,
                        (o1, o2) -> o1.getCategoria().compareTo(o2.getCategoria()));
                break;
            case  "Más reciente":
                toggleList.clear();
                toggleList.addAll(serverList);
                break;
                case "Más antiguo":
                    toggleList.clear();
                    toggleList.addAll(serverList);
                    Collections.reverse(toggleList);
                    break;
        }
        updateDocumentos(toggleList);
    }

    // Filtro del SearchView
    public void search(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        toggleList.clear();
        if (charText.length() == 0) {
            toggleList.addAll(serverList);
        } else {
            for (DocumentoDTO wp : serverList) {
                if (wp.getDocumento().toLowerCase(Locale.getDefault()).contains(charText)) {
                    toggleList.add(wp);
                }
            }
        }
        updateDocumentos(toggleList);
        System.out.println(toggleList.toArray().length);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getDocumentosFromServer(String alumno) {
        int idAlumno = -1;
        String grupo = "", grupoEnc = "";
        if(!alumno.equals("Generales")) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            try {
                JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
                for (int i = 0; i < alumnos.length(); i++) {
                    JSONObject json = alumnos.getJSONObject(i);
                    if(alumno.equals(json.getString("nombre")))
                        idAlumno = json.getInt("id");
                }
                for (int i = 0; i < alumnos.length(); i++) {
                    JSONObject json = alumnos.getJSONObject(i);
                    if(json.getInt("id") == idAlumno) {
                        grupo = json.getString("grupo");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Como el grupo contiene espacios, necesitamos utilizar URL encode
            try {
                grupoEnc = URLEncoder.encode(grupo, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            grupoEnc = "0";
        }
        RestClient.getInstance(requireContext()).getAllDocumentos(idAlumno, grupoEnc, new GetAllDocumentosResponseHandler() {
            @Override
            public void requestDidComplete(List<DocumentoDTO> response) {
                serverList.clear();
                serverList.addAll(response);
                updateDocumentos(response);
            }

            @Override
            public void requestDidFail(int statusCode) {
                System.out.println(statusCode);
            }
        });
    }

    private void updateDocumentos(List<DocumentoDTO> data) {
        mAdapter.updateDate(data);
    }
}