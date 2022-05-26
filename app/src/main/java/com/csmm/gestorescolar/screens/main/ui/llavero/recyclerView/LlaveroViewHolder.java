package com.csmm.gestorescolar.screens.main.ui.llavero.recyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class LlaveroViewHolder extends RecyclerView.ViewHolder {

    TextView mAplicacion, tvUsuario, tvEmail, tvContrasena;
    Button modificarBtn;
    ImageView protegido;
    ExpandableLayout expandableLayout;
    ConstraintLayout mLayout;

    public LlaveroViewHolder(View itemView) {
        super(itemView);

        mAplicacion = itemView.findViewById(R.id.tvAplicacion);
        protegido = itemView.findViewById(R.id.protegidoIcon);
        tvUsuario = itemView.findViewById(R.id.tvUsuario);
        tvEmail = itemView.findViewById(R.id.tvEmail);
        tvContrasena = itemView.findViewById(R.id.tvContrasena);
        modificarBtn = itemView.findViewById(R.id.modificarButton);
        expandableLayout = itemView.findViewById(R.id.expandable_layout);
        mLayout = itemView.findViewById(R.id.clMain);
    }
}
