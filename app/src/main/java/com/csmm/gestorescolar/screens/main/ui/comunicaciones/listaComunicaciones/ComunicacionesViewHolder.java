package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.csmm.gestorescolar.R;

public class ComunicacionesViewHolder extends RecyclerView.ViewHolder {

    TextView mRemite;
    TextView mAsunto;
    TextView mCuerpo;
    TextView mHora;
    ImageView mImportante;
    RelativeLayout mLayout;

    public ComunicacionesViewHolder(View itemView) {
        super(itemView);

        mRemite = itemView.findViewById(R.id.remite);
        mAsunto = itemView.findViewById(R.id.asunto);
        mCuerpo = itemView.findViewById(R.id.cuerpo);
        mHora = itemView.findViewById(R.id.hora);
        mImportante = itemView.findViewById(R.id.importante);
        mLayout = itemView.findViewById(R.id.layout);
    }
}
