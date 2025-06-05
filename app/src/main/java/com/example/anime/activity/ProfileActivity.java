package com.example.anime.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anime.R;
import com.example.anime.api.AnimeApiService;
import com.example.anime.api.ApiClient;
import com.example.anime.model.Usuario;

import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtNombreUsuario, txtEmail;
    private Button btnEditarPerfil, btnFavoritos, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        AtomicReference<SharedPreferences> prefs = new AtomicReference<>(getSharedPreferences("settings", MODE_PRIVATE));
        int userId = prefs.get().getInt("userId", -1);

        if (userId != -1) {
            cargarDatosUsuario(userId);
        }

        btnEditarPerfil.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditarPerfilActivity.class));
        });

        btnFavoritos.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritosActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> {
            prefs.set(getSharedPreferences("settings", MODE_PRIVATE));
            SharedPreferences.Editor editor = prefs.get().edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void cargarDatosUsuario(int userId) {
        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Call<Usuario> call = apiService.obtenerUsuarioPorId(userId);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    txtNombreUsuario.setText(usuario.getName());
                    if (txtEmail != null) txtEmail.setText(usuario.getEmail());
                } else {
                    Toast.makeText(ProfileActivity.this, "Error obteniendo usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}