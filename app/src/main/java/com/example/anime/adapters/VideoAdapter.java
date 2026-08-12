package com.example.anime.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.R;
import com.example.anime.activity.VideoActivity;
import com.example.anime.model.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<Video> lista;
    private String animeTitulo;

    public VideoAdapter(Context context, List<Video> lista, String animeTitulo) {
        this.context = context;
        this.lista = lista;
        this.animeTitulo = animeTitulo;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = lista.get(position);
        holder.tvNumero.setText("Episodio " + video.getEpisode());
        holder.tvDescripcion.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("videoUrl", video.getUrl());
            intent.putExtra("animeTitulo", animeTitulo);
            intent.putExtra("episodio", video.getEpisode());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvDescripcion;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}