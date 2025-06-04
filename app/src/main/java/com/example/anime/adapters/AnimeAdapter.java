package com.example.anime.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.R;
import com.example.anime.model.Anime;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private Context context;
    private List<Anime> animeList;
    private List<Anime> animeListFull;
    private OnAnimeClickListener listener;

    @SuppressLint("NotifyDataSetChanged")
    public void actualizarLista(List<Anime> nuevaLista) {
        animeList.clear();
        animeList.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    public interface OnAnimeClickListener {
        void onFavoritoClick(Anime anime);
    }

    public AnimeAdapter(Context context, List<Anime> animeList, OnAnimeClickListener listener) {
        this.context = context;
        this.animeList = animeList;
        this.animeListFull = new ArrayList<>(animeList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorito, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.nombre.setText(anime.getName());
        holder.categoria.setText(anime.getGenre());

        if (anime.isFavorito()) {
            holder.btnCorazon.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.btnCorazon.setImageResource(R.drawable.ic_heart_outline);
        }

        holder.btnCorazon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoritoClick(anime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public void filtrarLista(String texto) {
        List<Anime> filtrada = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) {
            filtrada = new ArrayList<>(animeListFull);
        } else {
            String filtro = texto.toLowerCase();
            for (Anime anime : animeListFull) {
                if (anime.getName().toLowerCase().contains(filtro) ||
                        anime.getGenre().toLowerCase().contains(filtro)) {
                    filtrada.add(anime);
                }
            }
        }
        animeList = filtrada;
        notifyDataSetChanged();
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre, categoria;
        ImageButton btnCorazon;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgAnime);
            nombre = itemView.findViewById(R.id.txtNombreAnime);
            categoria = itemView.findViewById(R.id.txtCategoria);
            btnCorazon = itemView.findViewById(R.id.btnFavorito);
        }
    }
}
