package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.csmm.gestorescolar.R;

public class ComunicacionesViewHolder extends RecyclerView.ViewHolder {

    TextView mSender;
    TextView mEmailTitle;
    TextView mEmailDetails;
    TextView mEmailTime;
    ImageView mFavorite;
    RelativeLayout mLayout;

    public ComunicacionesViewHolder(View itemView) {
        super(itemView);

        mSender = itemView.findViewById(R.id.tvEmailSender);
        mEmailTitle = itemView.findViewById(R.id.tvEmailTitle);
        mEmailDetails = itemView.findViewById(R.id.tvEmailDetails);
        mEmailTime = itemView.findViewById(R.id.tvEmailTime);
        mFavorite = itemView.findViewById(R.id.ivFavorite);
        mLayout = itemView.findViewById(R.id.layout);
    }
}
