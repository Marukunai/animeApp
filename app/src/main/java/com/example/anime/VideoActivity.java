package com.example.anime;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoActivity extends Activity {

    private VideoView videoView;
    private static final String API_URL = "http://TU_IP_O_DOMINIO:PUERTO/episodios/"; // <-- CAMBIA ESTO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        hideSystemUI();

        int episodioId = getIntent().getIntExtra("episodio_id", -1);
        if (episodioId == -1) {
            Toast.makeText(this, "Episodio no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarVideoDesdeAPI(episodioId);
    }

    private void cargarVideoDesdeAPI(int episodioId) {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL + episodioId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                JSONObject episodio = new JSONObject(jsonBuilder.toString());
                String videoUrl = episodio.getString("url");

                runOnUiThread(() -> reproducirVideo(videoUrl));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error cargando el video", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }

    private void reproducirVideo(String videoUrl) {
        if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be") || videoUrl.contains("mega.nz")) {
            // Abrir en navegador o app externa
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);
            finish(); // cerrar la actividad actual
        } else {
            // Reproducir en el VideoView si es directo (.mp4, .webm, etc)
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setOnPreparedListener(mp -> videoView.start());
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }
}