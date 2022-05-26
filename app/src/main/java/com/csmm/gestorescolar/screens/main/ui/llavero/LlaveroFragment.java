package com.csmm.gestorescolar.screens.main.ui.llavero;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.LlaveroDTO;
import com.csmm.gestorescolar.client.handlers.CheckPasswordResponseHandler;
import com.csmm.gestorescolar.client.handlers.GetLlaveroByIdAlumnoResponseHandler;
import com.csmm.gestorescolar.databinding.LlaveroFragmentBinding;
import com.csmm.gestorescolar.screens.main.ui.llavero.recyclerView.LlaveroAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LlaveroFragment extends Fragment {

    private LlaveroFragmentBinding binding;
    private RecyclerView mRecyclerView;
    private LlaveroAdapter mAdapter;
    private List<LlaveroDTO> serverList = new ArrayList<>();
    private List<LlaveroDTO> toggleList = new ArrayList<>();
    private MaterialButton btnFiltrarPorAlumno;
    private PopupMenu popupMenuFiltroAlumno;
    private FloatingActionButton nuevoRegistroLlavero;
    private TextView noEncontrado;


    private String alumnoFiltrado = "";
    private Context mContext;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = LlaveroFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mContext = root.getContext();
        btnFiltrarPorAlumno = root.findViewById(R.id.btnFiltrarAlumnos);
        nuevoRegistroLlavero = root.findViewById(R.id.nuevoRegistro);
        noEncontrado = root.findViewById(R.id.noEncontrado);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new LlaveroAdapter(getContext(), serverList);
        mRecyclerView.setAdapter(mAdapter);

        autentificar();

        return root;
    }

    private void autentificar() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Confirmar tu identidad.");
        alertDialog.setMessage("Introduce tu contraseña: ");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Confirmar",
                (dialog, which) -> {
                    String password = input.getText().toString();
                    RestClient.getInstance(getContext()).checkPassword(password, new CheckPasswordResponseHandler() {
                        @Override
                        public void requestDidComplete() {
                            iniciarFlujo();
                        }

                        @Override
                        public void requestDidFail(int statusCode) {
                            if(statusCode==401) {
                                Snackbar.make(btnFiltrarPorAlumno, "Contraseña incorrecta.", Snackbar.LENGTH_SHORT).show();
                                btnFiltrarPorAlumno.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.GONE);
                                noEncontrado.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                });

        alertDialog.setNegativeButton("Cancelar",
                (dialog, which) -> {
                    dialog.cancel();
                    btnFiltrarPorAlumno.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    noEncontrado.setVisibility(View.VISIBLE);
                });

        alertDialog.show();

    }

    private void iniciarFlujo() {
        // Animaciones de los botones
        btnFiltrarPorAlumno.setAlpha(0f);
        btnFiltrarPorAlumno.animate().alpha(1f).setDuration(1000);
        // Animación fab
        nuevoRegistroLlavero.setScaleX(0);
        nuevoRegistroLlavero.setScaleY(0);
        final Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                android.R.interpolator.fast_out_slow_in);
        nuevoRegistroLlavero.animate()
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

        nuevoRegistroLlavero.setOnClickListener(view -> {
            //!TODO
        });

        inicializarFiltradoAlumnos();

        getLlaveroFromServer(alumnoFiltrado);
    }

    private void inicializarFiltradoAlumnos() {
        // Selector de alumnos
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ArrayList<String> listAlumnos = new ArrayList<>();
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            if(alumnos.length() <= 1) {
                alumnoFiltrado = alumnos.getJSONObject(0).getString("nombre");
                btnFiltrarPorAlumno.setVisibility(View.GONE);
                return;
            }
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
                btnFiltrarPorAlumno.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_flecha_arriba));
                // PopUp
                popupMenuFiltroAlumno = new PopupMenu(getContext(), view);
                popupMenuFiltroAlumno.setOnMenuItemClickListener(item -> {
                    for(int i=0; i<arrayAlumnos.length; i++) {
                        if(arrayAlumnosSoloNombre[i].contentEquals(item.getTitle())) {
                            alumnoFiltrado = arrayAlumnos[i];
                            btnFiltrarPorAlumno.setText(arrayAlumnosSoloNombre[i]);
                        }
                    }
                    getLlaveroFromServer(alumnoFiltrado);
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
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateLlavero(List<LlaveroDTO> data) {
        mAdapter.updateDate(data);
    }

    private void getLlaveroFromServer(String alumno) {
        int idAlumno = -1;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        try {
            JSONArray alumnos = new JSONArray(sharedPreferences.getString("alumnosAsociados", null));
            for (int i = 0; i < alumnos.length(); i++) {
                JSONObject json = alumnos.getJSONObject(i);
                if (alumno.equals(json.getString("nombre")))
                    idAlumno = json.getInt("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestClient.getInstance(requireContext()).getLlaveroByIdAlumno(idAlumno, new GetLlaveroByIdAlumnoResponseHandler() {
            @Override
            public void requestDidComplete(List<LlaveroDTO> response) {
                serverList.clear();
                serverList.addAll(response);
                updateLlavero(response);
            }

            @Override
            public void requestDidFail(int statusCode) {
                System.out.println(statusCode);
            }
        });
    }
}