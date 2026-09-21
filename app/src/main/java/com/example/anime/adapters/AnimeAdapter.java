package com.example.anime.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.anime.R;
import com.example.anime.activity.AnimeActivity;
import com.example.anime.model.Anime;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter de RecyclerView para listas de {@link Anime} (usado en Favoritos),
 * con carga de portada vía Glide, filtrado local por nombre/género, botón de
 * corazón para favorito ({@link OnAnimeClickListener}) y navegación a
 * {@link AnimeActivity} al tocar un item.
 */
public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private Context context;
    private List<Anime> animeList;
    private List<Anime> animeListFull;
    private OnAnimeClickListener listener;

    /** Sustituye la lista mostrada (no la lista completa usada para filtrar). */
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
        View view = LayoutInflater.from(context).inflate(R.layout.sample_anime, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.nombre.setText(anime.getName());
        holder.categoria.setText(anime.getGenre());

        Glide.with(context)
                .load(anime.getImage())
                .placeholder(R.drawable.narutoshippuden)
                .error(android.R.drawable.ic_menu_report_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideLoad", "Fallo cargando " + anime.getImage(), e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imagen);

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

        // Al tocar la tarjeta se abre el detalle, pasando todos los datos por Intent
        // (AnimeActivity no vuelve a pedirlos a la API).
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnimeActivity.class);
            intent.putExtra("animeId", anime.getId());
            intent.putExtra("titulo", anime.getName());
            intent.putExtra("nombreJapones", anime.getOriginalName());
            intent.putExtra("genero", anime.getGenre());
            intent.putExtra("anio", String.valueOf(anime.getYear()));
            intent.putExtra("pg", anime.getRating());
            intent.putExtra("sinopsis", anime.getDescription());
            intent.putExtra("imagenUrl", anime.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    /** Filtra sobre la lista completa por nombre o género (búsqueda local, no llama a la API). */
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
            categoria = itemView.findViewById(R.id.txtCategoriaAnime);
            btnCorazon = itemView.findViewById(R.id.btnFavorito);
        }
    }
}
