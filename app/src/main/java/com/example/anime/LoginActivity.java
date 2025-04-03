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

public class LoginActivity extends AppCompatActivity {
    private Switch switchTheme;
    private View rootView;
    private boolean isDarkMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        switchTheme = findViewById(R.id.switchTheme);
        rootView = findViewById(android.R.id.content);
        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        TextView tvRegistrar = findViewById(R.id.tvRegistrar);

        // Cargar preferencias del usuario
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = preferences.getBoolean("darkMode", true);
        applyTheme();

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            applyTheme();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("darkMode", isDarkMode);
            editor.apply();
        });

        // Ir a la pantalla de registro
        tvRegistrar.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
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
