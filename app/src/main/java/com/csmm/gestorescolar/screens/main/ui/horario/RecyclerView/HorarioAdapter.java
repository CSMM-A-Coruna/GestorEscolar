package com.csmm.gestorescolar.screens.main.ui.horario.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioViewHolder> {

    private List<HorarioData> mData;
    private final Context mContext;

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
        if(holder.getAdapterPosition() == 0) {
            holder.lineaArriba.setVisibility(View.INVISIBLE);
        }
        if(mData.get(holder.getAdapterPosition()).getProfesor().equals("")) {
            holder.tvAsignatura.setPadding(0, 40, 0, 0);
        }
        holder.tvHoraInicial.setText(mData.get(holder.getAdapterPosition()).getHoraInicio());
        holder.tvHoraFinal.setText(mData.get(holder.getAdapterPosition()).getHoraFinal());
        holder.tvAsignatura.setText(mData.get(holder.getAdapterPosition()).getAsignatura());
        holder.tvProfesor.setText(mData.get(holder.getAdapterPosition()).getProfesor());
    }

    public void updateData(List<HorarioData> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}

