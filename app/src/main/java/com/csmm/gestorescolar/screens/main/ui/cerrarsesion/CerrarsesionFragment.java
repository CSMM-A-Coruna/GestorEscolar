package com.csmm.gestorescolar.screens.main.ui.cerrarsesion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.PostFCMTokenResponseHandler;
import com.csmm.gestorescolar.databinding.CerrarsesionFragmentBinding;
import com.csmm.gestorescolar.screens.auth.LoginActivity;

public class CerrarsesionFragment extends Fragment {

    private CerrarsesionFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = CerrarsesionFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RestClient.getInstance(root.getContext()).postNewFCMToken("-", new PostFCMTokenResponseHandler() {
            @Override
            public void requestDidComplete() {
            }

            @Override
            public void requestDidFail(int statusCode) {
            }
        });

        SharedPreferences preferences;
        preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences = requireActivity().getSharedPreferences("comunicaciones", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences = requireActivity().getSharedPreferences("horario", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        preferences.edit().clear().apply();
        Intent intent = new Intent(root.getContext(), LoginActivity.class);
        startActivity(intent);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}