package com.example.anime.ui.favoritos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.model.Anime;
import java.util.ArrayList;
import java.util.List;

import com.example.anime.R;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder> {

    public interface OnQuitarFavoritoListener {
        void onQuitarFavorito(Anime anime);
    }

    private List<Anime> listaFavoritos = new ArrayList<>();
    private OnQuitarFavoritoListener listener;

    public FavoritosAdapter(OnQuitarFavoritoListener listener) {
        this.listener = listener;
    }

    public void actualizarLista(List<Anime> nuevaLista) {
        listaFavoritos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorito, parent, false);
        return new FavoritoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position) {
        Anime anime = listaFavoritos.get(position);
        holder.txtNombre.setText(anime.getName());
        holder.txtCategoria.setText(anime.getGenre());
        holder.iconCorazon.setOnClickListener(v -> {
            listener.onQuitarFavorito(anime);
        });
    }

    @Override
    public int getItemCount() {
        return listaFavoritos.size();
    }

    static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnime;
        TextView txtNombre, txtCategoria, iconCorazon;

        FavoritoViewHolder(View itemView) {
            super(itemView);
            imgAnime = itemView.findViewById(R.id.imgAnime);
            txtNombre = itemView.findViewById(R.id.txtNombreAnime);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            iconCorazon = itemView.findViewById(R.id.txtCorazon);
        }
    }
}