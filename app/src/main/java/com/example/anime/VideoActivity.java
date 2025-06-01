package com.example.anime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoActivity extends Activity {

    private VideoView videoView;
    private LinearLayout bottomBar;
    private ImageButton fullscreenButton;
    private boolean isFullscreen = false;

    private View extraInfoLayout;

    private static final String API_URL = "http://TU_IP_O_DOMINIO:PUERTO/episodios/";

    private final Handler handler = new Handler();
    private final Runnable hideControlsRunnable = () -> bottomBar.setVisibility(View.GONE);

    @SuppressLint({"ClickableViewAccessibility", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        bottomBar = findViewById(R.id.bottomBar);
        fullscreenButton = findViewById(R.id.fullscreenButton);
        extraInfoLayout = findViewById(R.id.infoContainer);

        int episodioId = getIntent().getIntExtra("episodio_id", -1);
        if (episodioId == -1) {
            Toast.makeText(this, "Episodio no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarVideoDesdeAPI(episodioId);

        // Manejo de pantalla completa
        fullscreenButton.setOnClickListener(v -> {
            isFullscreen = !isFullscreen;
            if (isFullscreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                hideSystemUI();
                extraInfoLayout.setVisibility(View.GONE);
                bottomBar.setVisibility(View.GONE);
            }
            else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                showSystemUI();
                extraInfoLayout.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
            }

            // Animación (presupone que ya tienes tus animaciones definidas)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Mostrar controles temporalmente al tocar el video en fullscreen
        videoView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isFullscreen) {
                    bottomBar.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(hideControlsRunnable);
                    handler.postDelayed(hideControlsRunnable, 3000);
                }
                videoView.performClick(); // ✅ aquí la llamada correcta
            }
            return true;
        });

        videoView.setOnClickListener(v -> {
            if (isFullscreen) {
                bottomBar.setVisibility(View.VISIBLE);
                handler.removeCallbacks(hideControlsRunnable);
                handler.postDelayed(hideControlsRunnable, 3000);
            }
        });

    }

    private void cargarVideoDesdeAPI(int episodioId) {
        new Thread(() -> {
            try {
                JSONObject episodio = getURL(episodioId);
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

    @NonNull
    private static JSONObject getURL(int episodioId) throws IOException, JSONException {
        URL url = new URL(API_URL + episodioId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        return new JSONObject(jsonBuilder.toString());
    }

    private void reproducirVideo(String videoUrl) {
        if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be") || videoUrl.contains("mega.nz")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);
            finish();
        } else {
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
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}