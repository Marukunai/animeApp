package com.example.anime.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anime.R;
import com.example.anime.api.AnimeApiService;
import com.example.anime.api.ApiClient;
import com.example.anime.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Pantalla de registro de un nuevo usuario contra la API. */
public class RegisterActivity extends AppCompatActivity {

    private Switch switchThemeRegister;
    private View rootView;
    private boolean isDarkMode = true;

    private EditText etNombre, etEmail, etContrasena, etTelefono;
    private Button btnRegistrarse;
    private TextView tvIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Referencias a views
        switchThemeRegister = findViewById(R.id.switchThemeRegister);
        rootView = findViewById(android.R.id.content);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasenaRegister);
        etTelefono = findViewById(R.id.etTelefono);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = preferences.getBoolean("darkMode", true);
        applyTheme();

        // Cambio de tema
        switchThemeRegister.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            preferences.edit().putBoolean("darkMode", isDarkMode).apply();
            applyTheme();
        });

        // Redirige al login
        tvIniciarSesion.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Acción al pulsar "Registrarse"
        btnRegistrarse.setOnClickListener(view -> {
            String nombre = etNombre.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etContrasena.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setName(nombre);
                nuevoUsuario.setEmail(email);
                nuevoUsuario.setPassword(password);
                nuevoUsuario.setPhone(telefono);

                registrarUsuario(nuevoUsuario);
            }
        });
    }

    private void applyTheme() {
        rootView.setBackgroundColor(isDarkMode ? Color.BLACK : Color.WHITE);
    }

    /** Llama a AnimeApiService.createUsuario(); si va bien, vuelve a la pantalla de login. */
    private void registrarUsuario(Usuario usuario) {
        AnimeApiService apiService = ApiClient.getClient().create(AnimeApiService.class);
        Call<Usuario> call = apiService.createUsuario(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
