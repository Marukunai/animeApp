package com.example.anime.activity;

import static androidx.core.graphics.drawable.DrawableCompat.applyTheme;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anime.R;
import com.example.anime.adapters.VideoAdapter;
import com.example.anime.api.AnimeApiService;
import com.example.anime.api.ApiClient;
import com.example.anime.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimeActivity extends AppCompatActivity {

    private TextView tvTitulo, tvNombreJapones, tvGenero, tvAnio, tvPG, tvSinopsis;
    private ImageView imgAnime;
    private RecyclerView recyclerEpisodios;
    private VideoAdapter videoAdapter;
    private ProgressBar progressBar;
    private RelativeLayout toggleDarkLight;
    private View toggleThumb;
    private boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        // Vincular vistas
        tvTitulo = findViewById(R.id.tvAnimeTitulo);
        tvGenero = findViewById(R.id.tvGenero);
        tvAnio = findViewById(R.id.tvAnio);
        tvPG = findViewById(R.id.tvPG);
        tvSinopsis = findViewById(R.id.tvSinopsis);
        imgAnime = findViewById(R.id.imgAnime);
        recyclerEpisodios = findViewById(R.id.listaEpisodios);
        progressBar = findViewById(R.id.progressBar);

        recyclerEpisodios.setLayoutManager(new LinearLayoutManager(this));

        // Recoger datos del Intent
        int animeId = getIntent().getIntExtra("animeId", -1);
        String titulo = getIntent().getStringExtra("titulo");
        String nombreJapones = getIntent().getStringExtra("nombreJapones");
        String genero = getIntent().getStringExtra("genero");
        String anio = getIntent().getStringExtra("anio");
        String pg = getIntent().getStringExtra("pg");
        String sinopsis = getIntent().getStringExtra("sinopsis");
        String imagenUrl = getIntent().getStringExtra("imagenUrl");

        // Mostrar datos
        tvTitulo.setText(titulo);
        tvNombreJapones.setText(nombreJapones);
        tvGenero.setText("Género: " + genero);
        tvAnio.setText("Año: " + anio);
        tvPG.setText("PG: " + pg);
        tvSinopsis.setText(sinopsis);
        Glide.with(this).load(imagenUrl).into(imgAnime);

        // Cargar episodios desde la API
        if (animeId != -1) {
            cargarEpisodiosDesdeApi(animeId);
        }

        toggleDarkLight = findViewById(R.id.toggleDarkLight);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = preferences.getBoolean("darkMode", true);
        applyTheme();
        toggleDarkLight.setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            preferences.edit().putBoolean("darkMode", isDarkMode).apply();
            applyTheme();
        });

    }

    private void applyTheme() {
        View root = findViewById(android.R.id.content);
        if (isDarkMode) {
            root.setBackgroundColor(Color.parseColor("#121212"));
            toggleThumb.setBackgroundColor(Color.parseColor("#121212"));
            ((RelativeLayout.LayoutParams) toggleThumb.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            root.setBackgroundColor(Color.WHITE);
            toggleThumb.setBackgroundColor(Color.WHITE);
            ((RelativeLayout.LayoutParams) toggleThumb.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_END);
        }
        toggleThumb.requestLayout();
    }


    private void cargarEpisodiosDesdeApi(int animeId) {
        progressBar.setVisibility(View.VISIBLE);  // Mostrar mientras carga

        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Call<List<Video>> call = apiService.getVideosPorAnime(animeId);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                progressBar.setVisibility(View.GONE);  // Ocultar al completar
                if (response.isSuccessful() && response.body() != null) {
                    videoAdapter = new VideoAdapter(AnimeActivity.this, response.body());
                    recyclerEpisodios.setAdapter(videoAdapter);
                } else {
                    Toast.makeText(AnimeActivity.this, "No se encontraron episodios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);  // Ocultar en error
                Toast.makeText(AnimeActivity.this, "Error al cargar episodios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}