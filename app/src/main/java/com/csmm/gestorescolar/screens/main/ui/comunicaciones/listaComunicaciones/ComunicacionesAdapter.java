package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.databinding.ComunicacionesDetalleBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.ComunicacionDetalle;

import java.util.List;

public class ComunicacionesAdapter extends RecyclerView.Adapter<ComunicacionesViewHolder> {

    private List<ComunicacionDTO> mEmailData;
    private Context mContext;

    public ComunicacionesAdapter(Context mContext, List<ComunicacionDTO> mEmailData) {
        this.mEmailData = mEmailData;
        this.mContext = mContext;
    }

    @Override
    public ComunicacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comunicaciones_recyclerviewitem,
                parent, false);
        return new ComunicacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ComunicacionesViewHolder holder, int position) {
        holder.mSender.setText(mEmailData.get(position).getNombreRemite());
        holder.mEmailTitle.setText(mEmailData.get(position).getAsunto());
        holder.mEmailDetails.setText(mEmailData.get(position).getTexto());
        holder.mEmailTime.setText(mEmailData.get(position).getFecha());
        if(mEmailData.get(position).isImportante()) {
            holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.importante));
        } else {
            holder.mFavorite.clearColorFilter();
        }
        if(mEmailData.get(position).getLeida().equals("null")) {
            holder.itemView.setBackgroundColor(0xffffffff);
            holder.mSender.setTypeface(null, Typeface.BOLD);
            holder.mEmailTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.itemView.setBackgroundColor(0x88EAEAEA);
            holder.mSender.setTypeface(null, Typeface.NORMAL);
            holder.mEmailTitle.setTypeface(null, Typeface.NORMAL);
        }
        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mFavorite.getColorFilter() != null) {
                    holder.mFavorite.clearColorFilter();
                } else {
                    holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                            R.color.importante));
                }
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, ComunicacionDetalle.class);
                mIntent.putExtra("remite", holder.mSender.getText().toString());
                mIntent.putExtra("asunto", holder.mEmailTitle.getText().toString());
                mIntent.putExtra("texto", holder.mEmailDetails.getText().toString());
                mIntent.putExtra("fecha", holder.mEmailTime.getText().toString());
                if(holder.mFavorite.getColorFilter() != null) {
                    mIntent.putExtra("importante", true);
                } else {
                    mIntent.putExtra("importante", false);
                }
                mContext.startActivity(mIntent);
                ((Activity)  mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

    public void updateData(List<ComunicacionDTO> data) {
        this.mEmailData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mEmailData.size();
    }
}

