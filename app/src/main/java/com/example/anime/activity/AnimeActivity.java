package com.example.anime.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.anime.R;

public class AnimeActivity extends AppCompatActivity {

    private TextView tvTitulo, tvNombreJapones, tvGenero, tvAnio, tvPG, tvSinopsis;
    private LinearLayout listaEpisodios;
    private ImageView imgAnime;
    private Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        // Vincular vistas
        tvTitulo = findViewById(R.id.tvAnimeTitulo);
        tvNombreJapones = findViewById(R.id.tvNombreJapones);
        tvGenero = findViewById(R.id.tvGenero);
        tvAnio = findViewById(R.id.tvAnio);
        tvPG = findViewById(R.id.tvPG);
        tvSinopsis = findViewById(R.id.tvSinopsis);
        listaEpisodios = findViewById(R.id.listaEpisodios);
        imgAnime = findViewById(R.id.imgAnime);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        // Recoger datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String nombreJapones = getIntent().getStringExtra("nombreJapones");
        String genero = getIntent().getStringExtra("genero");
        String anio = getIntent().getStringExtra("anio");
        String pg = getIntent().getStringExtra("pg");
        String sinopsis = getIntent().getStringExtra("sinopsis");
        String imagenUrl = getIntent().getStringExtra("imagenUrl"); // si usas Glide
        String[] episodios = getIntent().getStringArrayExtra("episodios");

        // Mostrar datos
        tvTitulo.setText(titulo);
        tvNombreJapones.setText(nombreJapones);
        tvGenero.setText("Género: " + genero);
        tvAnio.setText("Año: " + anio);
        tvPG.setText("PG: " + pg);
        tvSinopsis.setText(sinopsis);

        // Cargar imagen (usando Glide)
        Glide.with(this).load(imagenUrl).into(imgAnime);

        // Añadir episodios dinámicamente
        if (episodios != null) {
            for (String ep : episodios) {
                TextView tvEp = new TextView(this);
                tvEp.setText(ep);
                tvEp.setTextColor(getResources().getColor(android.R.color.black));
                tvEp.setBackgroundColor(getResources().getColor(android.R.color.white));
                tvEp.setPadding(24, 16, 24, 16);
                tvEp.setTextSize(14);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 16);
                tvEp.setLayoutParams(lp);
                listaEpisodios.addView(tvEp);
            }
        }

        // Switch de modo oscuro (solo funcional si lo enlazas a tus preferencias globales)
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Aquí puedes activar/desactivar modo oscuro según tu lógica de la app
        });
    }
}