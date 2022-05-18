package com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioViewHolder> {

    private List<HorarioData> mData;
    private final Context mContext;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public HorarioAdapter(Context mContext, List<HorarioData> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horario_recyclerviewitem,
                parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorarioViewHolder holder, int position) {
        // Detalles visuales
        if(holder.getAdapterPosition() == 0) {
            holder.lineaArriba.setVisibility(View.INVISIBLE);
        }
        if(mData.get(holder.getAdapterPosition()).getProfesor().equals("")) {
            holder.tvAsignatura.setPadding(0, 40, 0, 0);
        } else {
            holder.tvAsignatura.setPadding(0, 0, 0, 0);
        }

        // Seteamos los textos
        holder.tvHoraInicial.setText(mData.get(holder.getAdapterPosition()).getHoraInicio());
        holder.tvHoraFinal.setText(mData.get(holder.getAdapterPosition()).getHoraFinal());
        holder.tvAsignatura.setText(mData.get(holder.getAdapterPosition()).getAsignatura());
        holder.tvProfesor.setText(mData.get(holder.getAdapterPosition()).getProfesor());
        setAnimation(holder.itemView, holder.getAdapterPosition());
    }

    public void updateData(List<HorarioData> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}

