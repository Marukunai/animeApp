package com.example.anime.activity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anime.R;

public class HomeActivity extends AppCompatActivity {

    private Switch switchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        switchTheme = findViewById(R.id.switchTheme);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Modo claro (a implementar)" : "Modo oscuro", Toast.LENGTH_SHORT).show();
            // Aquí puedes añadir lógica real de cambio de tema
        });
    }
}
