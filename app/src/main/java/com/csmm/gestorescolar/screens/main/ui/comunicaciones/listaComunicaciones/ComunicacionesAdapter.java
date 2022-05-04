package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

    private List<ComunicacionDTO> mData;
    private final Context mContext;

    public ComunicacionesAdapter(Context mContext, List<ComunicacionDTO> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ComunicacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comunicaciones_recyclerviewitem,
                parent, false);
        return new ComunicacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ComunicacionesViewHolder holder, int position) {

        // Setteamos los textos de `Asunto`, `Texto` y `Hora`, que son siempre iguales independientemente del tipo de comunicación
        holder.mAsunto.setText(mData.get(holder.getAdapterPosition()).getAsunto());
        holder.mCuerpo.setText(mData.get(holder.getAdapterPosition()).getTexto());
        holder.mHora.setText(mData.get(holder.getAdapterPosition()).getFecha());

        // Cambiamos el remite según sea enviada o no. (Si es enviada se mostrará el destino `Para: Sara Docampo`)
        if(mData.get(holder.getAdapterPosition()).getEstado().equals("enviada")) {
            String nombre = mData.get(holder.getAdapterPosition()).getNombreDestino();
            String[] splited = nombre.split("\\s+");
            String nombreSplit = splited[0] + " " +splited[1];
            holder.mRemite.setText("Para: " + nombreSplit);
        } else {
            String nombre = mData.get(holder.getAdapterPosition()).getNombreRemite();
            String[] splited = nombre.split("\\s+");
            String nombreSplit = splited[0] + " " + splited[1];
            holder.mRemite.setText(nombreSplit);
        }
        // Si es una comunicación recibida, damos la posibilidad de marcar como importante. Si no es recibida, mostramos todas las comunicaciones de color gris.
        if(mData.get(holder.getAdapterPosition()).getEstado().equals("recibida")) {
            holder.mImportante.setVisibility(View.VISIBLE);
            // Mostramos si está leída o no, jugando con los colores
            if(mData.get(holder.getAdapterPosition()).getLeida().equals("null")) {
                holder.itemView.setBackgroundColor(0xffffffff);
                holder.mRemite.setTypeface(null, Typeface.BOLD);
                holder.mAsunto.setTypeface(null, Typeface.BOLD);
            } else {
                holder.itemView.setBackgroundColor(0x88EAEAEA);
                holder.mRemite.setTypeface(null, Typeface.NORMAL);
                holder.mAsunto.setTypeface(null, Typeface.NORMAL);
            }
            // Mostramos si está marcada como importante o no, jugando con el color filter.
            if(mData.get(holder.getAdapterPosition()).isImportante()) {
                holder.mImportante.setColorFilter(ContextCompat.getColor(mContext, R.color.importante));
            } else {
                holder.mImportante.clearColorFilter();
            }

            // Listener de si es se clicka en el icono importante, actualizando el servidor
            holder.mImportante.setOnClickListener(view -> {
                String estado;
                if (holder.mImportante.getColorFilter() != null) {
                    holder.mImportante.clearColorFilter();
                    mData.get(holder.getAdapterPosition()).setImportante(false);
                    estado = "no_importante";
                } else {
                    holder.mImportante.setColorFilter(ContextCompat.getColor(mContext,
                            R.color.importante));
                    mData.get(holder.getAdapterPosition()).setImportante(true);
                    estado = "importante";
                }
                RestClient.getInstance(mContext).postEstadoComunicacion(mData.get(holder.getAdapterPosition()).getIdComunicacion(), estado, mData.get(holder.getAdapterPosition()).getIdDestino(), new PostEstadoComunicacionHandler() {
                    @Override
                    public void sessionRequestDidComplete(boolean update) { }
                    @Override
                    public void requestDidFail(int statusCode) { }
                });

            });
        } else {
            holder.itemView.setBackgroundColor(0x88EAEAEA);
            holder.mRemite.setTypeface(null, Typeface.NORMAL);
            holder.mAsunto.setTypeface(null, Typeface.NORMAL);
            holder.mImportante.setVisibility(View.GONE);
        }

        // Listener de cada celda
        holder.mLayout.setOnClickListener(view -> {
            Intent mIntent;
            if(mData.get(holder.getAdapterPosition()).getEstado().equals("recibida")) {
                mIntent = new Intent(mContext, ComunicacionDetalleRecibida.class);
            } else if (mData.get(holder.getAdapterPosition()).getEstado().equals("enviada")) {
                mIntent = new Intent(mContext, ComunicacionDetalleEnviada.class);
            } else {
                mIntent = new Intent(mContext, ComunicacionDetallePapelera.class);
            }
            setIntentExtrasAndStartActivity(mIntent, holder);
        });

        //!TODO Implementar selector para borrados en masa...
        holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
    }

    public void updateData(List<ComunicacionDTO> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Iniciamos el intent creado por el listener según el tipo de comunicación
    private void setIntentExtrasAndStartActivity(Intent mIntent, ComunicacionesViewHolder holder) {
        mIntent.putExtra("id_com", mData.get(holder.getAdapterPosition()).getIdComunicacion());
        mIntent.putExtra("remite", holder.mRemite.getText().toString());
        mIntent.putExtra("id_remite", mData.get(holder.getAdapterPosition()).getIdRemite());
        mIntent.putExtra("tipo_remite", mData.get(holder.getAdapterPosition()).getTipoRemite());
        mIntent.putExtra("destino", mData.get(holder.getAdapterPosition()).getNombreDestino());
        mIntent.putExtra("id_destino", mData.get(holder.getAdapterPosition()).getIdDestino());
        mIntent.putExtra("id_alumnoAsociado", mData.get(holder.getAdapterPosition()).getIdAlumnoAsociado());
        mIntent.putExtra("asunto", holder.mAsunto.getText().toString());
        mIntent.putExtra("texto", holder.mCuerpo.getText().toString());
        mIntent.putExtra("fecha", holder.mHora.getText().toString());
        mIntent.putExtra("leida", mData.get(holder.getAdapterPosition()).getLeida());
        mIntent.putExtra("eliminado", mData.get(holder.getAdapterPosition()).getEliminado());
        mIntent.putExtra("estado", mData.get(holder.getAdapterPosition()).getEstado());
        mIntent.putExtra("adjuntos", mData.get(holder.getAdapterPosition()).getAdjuntos());
        mIntent.putExtra("importante", holder.mImportante.getColorFilter() != null);
        mContext.startActivity(mIntent);
        ((Activity)  mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

