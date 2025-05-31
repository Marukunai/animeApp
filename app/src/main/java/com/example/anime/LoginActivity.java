package com.example.anime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {
    private Switch switchTheme;
    private View rootView;
    private boolean isDarkMode = true;

    private EditText editTextEmail, editTextPassword;
    private Button btnIniciarSesion;
    private TextView tvRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        switchTheme = findViewById(R.id.switchTheme);
        rootView = findViewById(android.R.id.content);
        editTextEmail = findViewById(R.id.etUsuario);
        editTextPassword = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvRegistrar = findViewById(R.id.tvRegistrar);

        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = preferences.getBoolean("darkMode", true);
        applyTheme();

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            applyTheme();
            preferences.edit().putBoolean("darkMode", isDarkMode).apply();
        });

        tvRegistrar.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnIniciarSesion.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                new LoginTask().execute(email, password);
            }
        });
    }

    private void applyTheme() {
        rootView.setBackgroundColor(isDarkMode ? Color.BLACK : Color.WHITE);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String email = params[0];
                String password = params[1];

                URL url = new URL("http://10.0.2.2:8080/usuarios/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", email);
                jsonParam.put("password", password);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    String response = scanner.useDelimiter("\\A").next();
                    scanner.close();

                    JSONObject json = new JSONObject(response);
                    return json.getBoolean("success");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
                prefs.edit().putBoolean("isLoggedIn", true).apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
