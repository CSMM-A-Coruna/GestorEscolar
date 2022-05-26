package com.csmm.gestorescolar.screens.main.ui.llavero.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.dtos.LlaveroDTO;

import java.util.List;

public class LlaveroAdapter  extends RecyclerView.Adapter<LlaveroViewHolder> {

    private List<LlaveroDTO> mData;
    private final Context mContext;

    public LlaveroAdapter(Context mContext, List<LlaveroDTO> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public LlaveroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.llavero_recyclerview_item,
                parent, false);
        return new LlaveroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LlaveroViewHolder holder, int position) {
        holder.mAplicacion.setText(mData.get(holder.getAdapterPosition()).getAplicacion());
        if(!mData.get(holder.getAdapterPosition()).isModificable()) {
            holder.protegido.setVisibility(View.VISIBLE);
        } else {
            holder.protegido.setVisibility(View.GONE);
        }

    }

    public void updateDate(List<LlaveroDTO> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
