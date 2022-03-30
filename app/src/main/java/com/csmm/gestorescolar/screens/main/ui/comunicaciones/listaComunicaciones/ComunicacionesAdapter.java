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
import com.csmm.gestorescolar.databinding.ComunicacionesDetalleBinding;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.ComunicacionDetalle;

import java.util.List;

public class ComunicacionesAdapter extends RecyclerView.Adapter<ComunicacionesViewHolder> {

    private List<ComunicacionesData> mEmailData;
    private Context mContext;

    public ComunicacionesAdapter(Context mContext, List<ComunicacionesData> mEmailData) {
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
        holder.mSender.setText(mEmailData.get(position).getmSender());
        holder.mEmailTitle.setText(mEmailData.get(position).getmTitle());
        holder.mEmailDetails.setText(mEmailData.get(position).getmDetails());
        holder.mEmailTime.setText(mEmailData.get(position).getmTime());
        if(mEmailData.get(position).getmImportante()) {
            holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext, com.google.android.material.R.color.abc_btn_colored_borderless_text_material));
        } else {
            holder.mFavorite.clearColorFilter();
        }
        if(!mEmailData.get(position).getmLeido()) {
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
                            com.google.android.material.R.color.abc_btn_colored_borderless_text_material));
                }
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, ComunicacionDetalle.class);
                mIntent.putExtra("sender", holder.mSender.getText().toString());
                mIntent.putExtra("title", holder.mEmailTitle.getText().toString());
                mIntent.putExtra("details", holder.mEmailDetails.getText().toString());
                mIntent.putExtra("time", holder.mEmailTime.getText().toString());
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

    public void updateData(List<ComunicacionesData> data) {
        this.mEmailData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mEmailData.size();
    }
}

