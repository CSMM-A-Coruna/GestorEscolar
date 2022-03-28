package com.csmm.gestorescolar.screens.main.ui.documentacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.FragmentDocumentacionBinding;

public class DocumentacionFragment extends Fragment {

    private FragmentDocumentacionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DocumentacionViewModel documentacionViewModel =
                new ViewModelProvider(this).get(DocumentacionViewModel.class);

        binding = FragmentDocumentacionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDocumentacion;
        documentacionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}