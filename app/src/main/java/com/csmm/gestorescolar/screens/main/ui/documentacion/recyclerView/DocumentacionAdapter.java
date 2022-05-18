package com.csmm.gestorescolar.screens.main.ui.documentacion.recyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.dtos.DocumentoDTO;

import java.util.List;

public class DocumentacionAdapter extends RecyclerView.Adapter<DocumentacionViewHolder> {

    private List<DocumentoDTO> mData;
    private final Context mContext;

    public DocumentacionAdapter(Context mContext, List<DocumentoDTO> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DocumentacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.documentacion_recyclerview_item,
                parent, false);
        return new DocumentacionViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final DocumentacionViewHolder holder, int position) {
        holder.mNombre.setText(mData.get(holder.getAdapterPosition()).getDocumento());
        holder.mCategoria.setText(mData.get(holder.getAdapterPosition()).getCategoria());
        holder.mFecha.setText(mData.get(holder.getAdapterPosition()).getFecha());

        switch (mData.get(holder.getAdapterPosition()).getTipoDocumento()) {
            case "pdf":
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.pdf));
                break;
            case "docx":
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.word));
                break;
            case "pptx":
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.ppt));
                break;
            case "xlsx":
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.excel));
                break;
            case "png": case "jpg": case "jpeg":
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.image));
                break;
            case "zip": case "rar": case "7z":
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.zip));
                break;
            default:
                holder.tipoDocumento.setImageDrawable(mContext.getDrawable(R.drawable.documento));
        }

        /*holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                Uri uri = Uri.parse(RestClient.REST_API_BASE_URL + "/resources/download").buildUpon()
                        .appendQueryParameter("file_name", adjuntos[0])
                        .appendQueryParameter("id_comunicacion", String.valueOf(idComunicacion))
                        .appendQueryParameter("auth", savedtoken)
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });*/
    }

    public void updateDate(List<DocumentoDTO> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
