package com.example.anime.ui.favoritos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.R;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder> {

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorito, parent, false);
        return new FavoritoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position) {
        // Datos de ejemplo (reemplazar con tus datos reales)
        holder.txtNombre.setText("Nombre del Anime " + (position + 1));
        holder.txtCategoria.setText("Categoría");
    }

    @Override
    public int getItemCount() {
        return 3; // número de ítems ficticios
    }

    static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnime;
        TextView txtNombre;
        TextView txtCategoria;
        TextView iconCorazon;

        FavoritoViewHolder(View itemView) {
            super(itemView);
            imgAnime = itemView.findViewById(R.id.imgAnime);
            txtNombre = itemView.findViewById(R.id.txtNombreAnime);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            iconCorazon = itemView.findViewById(R.id.txtCorazon);
        }
    }
}