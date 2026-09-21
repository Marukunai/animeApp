package com.example.anime.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime.R;
import com.example.anime.adapters.AnimeAdapter;
import com.example.anime.api.AnimeApiService;
import com.example.anime.api.ApiClient;
import com.example.anime.model.Anime;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Lista de animes favoritos del usuario logueado. El userId se lee de
 * SharedPreferences "user_session"/"user_id" — nótese que el resto de la
 * app usa "settings"/"userId" (ver LoginActivity/ProfileActivity), así que
 * esta pantalla probablemente nunca encuentra el userId real; iría bien
 * unificar la clave usada en toda la app.
 */
public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView recyclerFavoritos;
    private AnimeAdapter adapter;  // Asegúrate de tener este adaptador implementado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        recyclerFavoritos = findViewById(R.id.recyclerFavoritos);
        recyclerFavoritos.setLayoutManager(new LinearLayoutManager(this));

        // Obtener userId desde SharedPreferences (si lo usas para guardar la sesión)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            cargarFavoritos(userId);
        } else {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show();
        }
    }

    /** GET /favoritos/{userId} y lo pinta en el RecyclerView vía AnimeAdapter. */
    private void cargarFavoritos(int userId) {
        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Call<List<Anime>> call = apiService.getUserFavorites(userId);

        call.enqueue(new Callback<List<Anime>>() {
            @Override
            public void onResponse(Call<List<Anime>> call, Response<List<Anime>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new AnimeAdapter(FavoritosActivity.this, response.body(), anime -> {
                    });
                    recyclerFavoritos.setAdapter(adapter);
                } else {
                    Toast.makeText(FavoritosActivity.this, "No se encontraron favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Anime>> call, Throwable t) {
                Toast.makeText(FavoritosActivity.this, "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
