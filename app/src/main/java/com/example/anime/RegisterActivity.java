package com.example.anime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Switch switchThemeRegister;
    private View rootView;
    private boolean isDarkMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        switchThemeRegister = findViewById(R.id.switchThemeRegister);
        rootView = findViewById(android.R.id.content);
        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        TextView tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        // Cargar preferencias del usuario
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = preferences.getBoolean("darkMode", true);
        applyTheme();

        // Cambiar tema con el switch
        switchThemeRegister.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            applyTheme();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("darkMode", isDarkMode);
            editor.apply();
        });

        // Ir a la pantalla de login
        tvIniciarSesion.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad de registro
        });

        // Acciones al pulsar "Registrarse"
        btnRegistrarse.setOnClickListener(view -> {
            String nombre = ((EditText) findViewById(R.id.etNombre)).getText().toString();
            String email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
            String contrasena = ((EditText) findViewById(R.id.etContrasenaRegister)).getText().toString();
            String telefono = ((EditText) findViewById(R.id.etTelefono)).getText().toString();

            if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || telefono.isEmpty()) {
                return;
            }

            // Aquí puedes guardar los datos en una base de datos o usarlos como necesites.
        });
    }

    private void applyTheme() {
        if (isDarkMode) {
            rootView.setBackgroundColor(Color.BLACK);
        } else {
            rootView.setBackgroundColor(Color.WHITE);
        }
    }
}
