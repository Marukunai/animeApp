package com.example.anime.ui.favoritos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.R;
import com.example.anime.api.AnimeApiService;
import com.example.anime.api.ApiClient;
import com.example.anime.model.Anime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Variante en Fragment (con RecyclerView propio, sin ViewModel) de la
 * pantalla de favoritos — a diferencia de activity.FavoritosActivity, esta
 * sí lee el userId de la clave "settings"/"userId" que usa el resto de la
 * app, y refresca la lista cada vez que el fragment vuelve a primer plano.
 */
public class FavoritosFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritosAdapter adapter;
    private List<Anime> listaActual = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        recyclerView = view.findViewById(R.id.recyclerFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FavoritosAdapter(anime -> quitarFavorito(anime));
        recyclerView.setAdapter(adapter);

        cargarFavoritosDesdeApi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarFavoritosDesdeApi(); // Refresca lista cuando el fragmento vuelve a ser visible
    }

    /** GET /favoritos/{userId}. */
    private void cargarFavoritosDesdeApi() {
        int userId = obtenerUserId();

        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Call<List<Anime>> call = apiService.getUserFavorites(userId);

        call.enqueue(new Callback<List<Anime>>() {
            @Override
            public void onResponse(Call<List<Anime>> call, Response<List<Anime>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaActual = response.body();
                    adapter.actualizarLista(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Anime>> call, Throwable t) {
                Log.e("Favoritos", "Error al obtener favoritos: " + t.getMessage());
            }
        });
    }

    private int obtenerUserId() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }

    /** DELETE /favoritos/remove/{userId}/{animeId} y actualiza la lista local si va bien. */
    private void quitarFavorito(Anime anime) {
        int userId = obtenerUserId();

        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Call<Void> call = apiService.removeFavorite(userId, anime.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaActual.remove(anime);
                    adapter.actualizarLista(listaActual);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Favoritos", "Error al eliminar favorito: " + t.getMessage());
            }
        });
    }
}
