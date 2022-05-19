package com.csmm.gestorescolar.screens.main.ui.documentacion.recyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;

public class DocumentacionViewHolder extends RecyclerView.ViewHolder {

    TextView mNombre, mCategoria, mFecha;
    ImageView tipoDocumento, protegido;
    ConstraintLayout mLayout;

    public DocumentacionViewHolder(View itemView) {
        super(itemView);

        mNombre = itemView.findViewById(R.id.tvNombre);
        mCategoria = itemView.findViewById(R.id.tvCategoria);
        mFecha = itemView.findViewById(R.id.tvFecha);
        tipoDocumento = itemView.findViewById(R.id.tipoDocumento);
        protegido = itemView.findViewById(R.id.protegidoIcon);
        mLayout = itemView.findViewById(R.id.clMain);
    }
}
