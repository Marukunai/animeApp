package com.example.anime.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anime.R;
import com.example.anime.api.ApiClient;
import com.example.anime.api.UsuarioApiService;
import com.example.anime.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono;
    private Button btnGuardar;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        cargarDatosPerfil();

        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatosPerfil() {
        UsuarioApiService api = ApiClient.getClient().create(UsuarioApiService.class);
        Call<Usuario> call = api.getUsuarioPorId(userId);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario user = response.body();
                    etNombre.setText(user.getName());
                    etTelefono.setText(user.getPhone());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Error al cargar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        String nuevoNombre = etNombre.getText().toString().trim();
        String nuevoTelefono = etTelefono.getText().toString().trim();

        Usuario actualizado = new Usuario();
        actualizado.setId(userId);
        actualizado.setName(nuevoNombre);
        actualizado.setPhone(nuevoTelefono);

        UsuarioApiService api = ApiClient.getClient().create(UsuarioApiService.class);
        Call<Void> call = api.actualizarPerfil(userId, actualizado);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarPerfilActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
