package com.csmm.gestorescolar.screens.main.ui.llavero.recyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;

public class LlaveroViewHolder extends RecyclerView.ViewHolder {

    TextView mAplicacion;
    ImageView protegido;
    ConstraintLayout mLayout;

    public LlaveroViewHolder(View itemView) {
        super(itemView);

        mAplicacion = itemView.findViewById(R.id.tvAplicacion);
        protegido = itemView.findViewById(R.id.protegidoIcon);
        mLayout = itemView.findViewById(R.id.clMain);
    }
}
