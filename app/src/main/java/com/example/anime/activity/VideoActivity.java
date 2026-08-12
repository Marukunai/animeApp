package com.example.anime.activity;

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
import android.widget.Toast;
import android.widget.VideoView;

import com.example.anime.R;

public class VideoActivity extends Activity {

    private VideoView videoView;
    private LinearLayout bottomBar;
    private ImageButton fullscreenButton;
    private boolean isFullscreen = false;

    private View extraInfoLayout;

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

        String videoUrl = getIntent().getStringExtra("videoUrl");
        if (videoUrl == null || videoUrl.isEmpty()) {
            Toast.makeText(this, "Episodio no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reproducirVideo(videoUrl);

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
                videoView.performClick();
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