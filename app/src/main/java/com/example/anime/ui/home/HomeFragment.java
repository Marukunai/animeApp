package com.example.anime.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.R;
import com.example.anime.adapters.AnimeAdapter;
import com.example.anime.api.AnimeApiService;
import com.example.anime.api.ApiClient;
import com.example.anime.model.Anime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter adapter;
    private EditText searchBar;
    private List<Anime> listaAnimes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchBar = view.findViewById(R.id.searchBar);

        listaAnimes = new ArrayList<>();
        adapter = new AnimeAdapter(getContext(), listaAnimes, anime -> {
            int userId = obtenerUserId();

            anime.setFavorito(!anime.isFavorito());
            adapter.notifyDataSetChanged();

            if (anime.isFavorito()) {
                guardarFavoritoEnApi(userId, anime.getId());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        cargarAnimesDesdeApi();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrarLista(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarAnimesDesdeApi(); // Refresca lista cuando el fragmento vuelve a ser visible
    }

    private void cargarAnimesDesdeApi() {
        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        int userId = obtenerUserId();

        Call<List<Anime>> call = apiService.getAnimesPorUsuario(userId);
        call.enqueue(new Callback<List<Anime>>() {
            @Override
            public void onResponse(Call<List<Anime>> call, Response<List<Anime>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.actualizarLista(response.body());
                } else {
                    Log.e("API", "Respuesta no válida");
                }
            }

            @Override
            public void onFailure(Call<List<Anime>> call, Throwable t) {
                Log.e("API", "Error cargando animes: " + t.getMessage());
            }
        });
    }

    private void guardarFavoritoEnApi(int userId, int animeId) {
        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Map<String, Integer> body = new HashMap<>();
        body.put("userId", userId);
        body.put("animeId", animeId);

        Call<Void> call = apiService.insertarFavorito(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.e("API", "Fallo insertando favorito");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "Error insertando favorito: " + t.getMessage());
            }
        });
    }

    private int obtenerUserId() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        return prefs.getInt("userId", -1); // devuelve -1 si no está
    }
}