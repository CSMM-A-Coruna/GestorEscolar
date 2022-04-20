package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.client.handlers.PostEstadoComunicacionHandler;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle.ComunicacionDetalleEnviada;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle.ComunicacionDetallePapelera;
import com.csmm.gestorescolar.screens.main.ui.comunicaciones.detalle.ComunicacionDetalleRecibida;

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
        holder.mEmailTitle.setText(mEmailData.get(holder.getAdapterPosition()).getAsunto());
        holder.mEmailDetails.setText(mEmailData.get(holder.getAdapterPosition()).getTexto());
        holder.mEmailTime.setText(mEmailData.get(holder.getAdapterPosition()).getFecha());

        if(mEmailData.get(holder.getAdapterPosition()).getEstado().equals("enviada")) {
            String nombre = mEmailData.get(holder.getAdapterPosition()).getNombreDestino();
            String[] splited = nombre.split("\\s+");
            String nombreSplit = splited[0] + " " +splited[1];
            holder.mSender.setText("Para: " + nombreSplit);
        } else {
            String nombre = mEmailData.get(holder.getAdapterPosition()).getNombreRemite();
            String[] splited = nombre.split("\\s+");
            String nombreSplit = splited[0] + " " + splited[1];
            holder.mSender.setText(nombreSplit);
        }
        if(mEmailData.get(holder.getAdapterPosition()).getEstado().equals("recibida")) {
            holder.mFavorite.setVisibility(View.VISIBLE);
            if(mEmailData.get(holder.getAdapterPosition()).getLeida().equals("null")) {
                holder.itemView.setBackgroundColor(0xffffffff);
                holder.mSender.setTypeface(null, Typeface.BOLD);
                holder.mEmailTitle.setTypeface(null, Typeface.BOLD);
            } else {
                holder.itemView.setBackgroundColor(0x88EAEAEA);
                holder.mSender.setTypeface(null, Typeface.NORMAL);
                holder.mEmailTitle.setTypeface(null, Typeface.NORMAL);
            }
            if(mEmailData.get(holder.getAdapterPosition()).isImportante()) {
                holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.importante));
            } else {
                holder.mFavorite.clearColorFilter();
            }
            holder.mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String estado;
                    if (holder.mFavorite.getColorFilter() != null) {
                        holder.mFavorite.clearColorFilter();
                        mEmailData.get(holder.getAdapterPosition()).setImportante(false);
                        estado = "no_importante";
                    } else {
                        holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                                R.color.importante));
                        mEmailData.get(holder.getAdapterPosition()).setImportante(true);
                        estado = "importante";
                    }
                    RestClient.getInstance(mContext).postEstadoComunicacion(mEmailData.get(holder.getAdapterPosition()).getIdComunicacion(), estado, mEmailData.get(holder.getAdapterPosition()).getIdDestino(), new PostEstadoComunicacionHandler() {
                        @Override
                        public void sessionRequestDidComplete(boolean update) { }
                        @Override
                        public void requestDidFail(int statusCode) { }
                    });

                }
            });
        } else {
            holder.itemView.setBackgroundColor(0x88EAEAEA);
            holder.mSender.setTypeface(null, Typeface.NORMAL);
            holder.mEmailTitle.setTypeface(null, Typeface.NORMAL);
            holder.mFavorite.setVisibility(View.GONE);
        }

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent;
                if(mEmailData.get(holder.getAdapterPosition()).getEstado().equals("recibida")) {
                    mIntent = new Intent(mContext, ComunicacionDetalleRecibida.class);
                } else if (mEmailData.get(holder.getAdapterPosition()).getEstado().equals("enviada")) {
                    mIntent = new Intent(mContext, ComunicacionDetalleEnviada.class);
                } else {
                    mIntent = new Intent(mContext, ComunicacionDetallePapelera.class);
                }
                mIntent.putExtra("id_com", mEmailData.get(holder.getAdapterPosition()).getIdComunicacion());
                mIntent.putExtra("remite", holder.mSender.getText().toString());
                mIntent.putExtra("id_remite", mEmailData.get(holder.getAdapterPosition()).getIdRemite());
                mIntent.putExtra("tipo_remite", mEmailData.get(holder.getAdapterPosition()).getTipoRemite());
                mIntent.putExtra("destino", mEmailData.get(holder.getAdapterPosition()).getNombreDestino());
                mIntent.putExtra("id_destino", mEmailData.get(holder.getAdapterPosition()).getIdDestino());
                mIntent.putExtra("id_alumnoAsociado", mEmailData.get(holder.getAdapterPosition()).getIdAlumnoAsociado());
                mIntent.putExtra("asunto", holder.mEmailTitle.getText().toString());
                mIntent.putExtra("texto", holder.mEmailDetails.getText().toString());
                mIntent.putExtra("fecha", holder.mEmailTime.getText().toString());
                mIntent.putExtra("leida", mEmailData.get(holder.getAdapterPosition()).getLeida());
                mIntent.putExtra("eliminado", mEmailData.get(holder.getAdapterPosition()).getEliminado());
                mIntent.putExtra("estado", mEmailData.get(holder.getAdapterPosition()).getEstado());
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

