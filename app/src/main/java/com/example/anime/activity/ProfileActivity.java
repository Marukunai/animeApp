package com.example.anime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anime.R;

public class ProfileActivity extends AppCompatActivity {

    private Button btnEditarPerfil, btnFavoritos, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtNombreUsuario.setText("Joselu Martínez");
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnEditarPerfil.setOnClickListener(v -> {
            // Lógica para editar perfil
        });

        btnFavoritos.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, FavoritosActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> {
            // Lógica para cerrar sesión
            finish();
        });
    }
}