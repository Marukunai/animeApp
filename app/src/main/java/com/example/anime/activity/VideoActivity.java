package com.example.anime.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.anime.R;

import java.util.Locale;

public class VideoActivity extends Activity {

    private ConstraintLayout rootLayout;
    private FrameLayout playerFrame;
    private FrameLayout fullscreenContainer;
    private VideoView videoView;
    private WebView webView;
    private ProgressBar loadingSpinner;
    private LinearLayout topBar;
    private LinearLayout bottomControls;
    private LinearLayout infoSection;
    private ImageButton btnBack, btnPlayPause, btnFullscreen;
    private Button btnAbrirNavegador;
    private TextView tvPlayerTitle, tvTime, tvAnimeTitleInfo, tvEpisodeInfo;
    private SeekBar seekBar;

    private boolean isFullscreen = false;
    private boolean isNativePlayback = false;

    private View webCustomView;
    private WebChromeClient.CustomViewCallback webCustomViewCallback;

    private final Handler handler = new Handler();
    private final Runnable hideControlsRunnable = () -> bottomControls.setVisibility(View.GONE);
    private final Runnable showFallbackRunnable = () -> btnAbrirNavegador.setVisibility(View.VISIBLE);
    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (videoView != null && videoView.isPlaying()) {
                seekBar.setProgress(videoView.getCurrentPosition());
                tvTime.setText(formatTime(videoView.getCurrentPosition()) + " / " + formatTime(videoView.getDuration()));
            }
            handler.postDelayed(this, 500);
        }
    };

    @SuppressLint({"ClickableViewAccessibility", "SourceLockedOrientationActivity", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rootLayout = findViewById(R.id.rootLayout);
        playerFrame = findViewById(R.id.playerFrame);
        fullscreenContainer = findViewById(R.id.fullscreenContainer);
        videoView = findViewById(R.id.videoView);
        webView = findViewById(R.id.webView);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        topBar = findViewById(R.id.topBar);
        bottomControls = findViewById(R.id.bottomControls);
        infoSection = findViewById(R.id.infoSection);
        btnBack = findViewById(R.id.btnBack);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnFullscreen = findViewById(R.id.btnFullscreen);
        btnAbrirNavegador = findViewById(R.id.btnAbrirNavegador);
        tvPlayerTitle = findViewById(R.id.tvPlayerTitle);
        tvTime = findViewById(R.id.tvTime);
        tvAnimeTitleInfo = findViewById(R.id.tvAnimeTitleInfo);
        tvEpisodeInfo = findViewById(R.id.tvEpisodeInfo);
        seekBar = findViewById(R.id.seekBar);

        String videoUrl = getIntent().getStringExtra("videoUrl");
        String animeTitulo = getIntent().getStringExtra("animeTitulo");
        String episodio = getIntent().getStringExtra("episodio");

        if (videoUrl == null || videoUrl.isEmpty()) {
            Toast.makeText(this, "Episodio no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String tituloMostrado = animeTitulo != null ? animeTitulo : "Anime";
        String episodioMostrado = episodio != null ? episodio : "?";

        tvAnimeTitleInfo.setText(tituloMostrado);
        tvEpisodeInfo.setText("Episodio " + episodioMostrado);
        tvPlayerTitle.setText(tituloMostrado + " · Episodio " + episodioMostrado);

        reproducirVideo(videoUrl);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnFullscreen.setOnClickListener(v -> toggleFullscreen());

        btnPlayPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);
            } else {
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
            }
            resetHideControlsTimer();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tvTime.setText(formatTime(progress) + " / " + formatTime(videoView.getDuration()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(hideControlsRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());
                resetHideControlsTimer();
            }
        });

        // Mostrar controles temporalmente al tocar el video
        playerFrame.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && isNativePlayback) {
                bottomControls.setVisibility(View.VISIBLE);
                resetHideControlsTimer();
            }
            return false;
        });
    }

    private void reproducirVideo(String videoUrl) {
        loadingSpinner.setVisibility(View.VISIBLE);

        if (videoUrl.contains("mega.nz")) {
            // Enlace tipo "embed": necesita vivir dentro de un WebView, no como pestaña suelta
            isNativePlayback = false;
            bottomControls.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

            // Permite inspeccionar este WebView desde chrome://inspect en el PC mientras corre el emulador
            WebView.setWebContentsDebuggingEnabled(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    loadingSpinner.setVisibility(View.GONE);
                    btnAbrirNavegador.setVisibility(View.GONE);
                    handler.removeCallbacks(showFallbackRunnable);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    if (request.isForMainFrame()) {
                        loadingSpinner.setVisibility(View.GONE);
                        btnAbrirNavegador.setVisibility(View.VISIBLE);
                        Log.e("WebViewLoad", "Error cargando " + request.getUrl()
                                + " -> code=" + error.getErrorCode() + " desc=" + error.getDescription());
                        Toast.makeText(VideoActivity.this, "Error cargando el vídeo: " + error.getDescription(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    if (request.isForMainFrame()) {
                        Log.e("WebViewLoad", "HTTP " + errorResponse.getStatusCode() + " cargando " + request.getUrl());
                    }
                }
            });

            btnAbrirNavegador.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(intent);
            });
            handler.postDelayed(showFallbackRunnable, 6000);

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onShowCustomView(View view, CustomViewCallback callback) {
                    // El propio reproductor de Mega pidió pantalla completa (API HTML5 de vídeo)
                    if (webCustomView != null) {
                        callback.onCustomViewHidden();
                        return;
                    }
                    webCustomView = view;
                    webCustomViewCallback = callback;
                    fullscreenContainer.addView(view);
                    fullscreenContainer.setVisibility(View.VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    hideSystemUI();
                }

                @Override
                public void onHideCustomView() {
                    fullscreenContainer.setVisibility(View.GONE);
                    fullscreenContainer.removeView(webCustomView);
                    webCustomView = null;
                    if (webCustomViewCallback != null) {
                        webCustomViewCallback.onCustomViewHidden();
                        webCustomViewCallback = null;
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    showSystemUI();
                }
            });

            webView.loadUrl(videoUrl);

        } else if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) {
            loadingSpinner.setVisibility(View.GONE);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);
            finish();

        } else {
            // Vídeo directo (mp4 y similares): reproductor nativo con controles propios
            isNativePlayback = true;
            webView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            bottomControls.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);

            videoView.setOnPreparedListener(mp -> {
                loadingSpinner.setVisibility(View.GONE);
                seekBar.setMax(videoView.getDuration());
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
                handler.post(progressRunnable);
                resetHideControlsTimer();
            });

            videoView.setOnCompletionListener(mp -> btnPlayPause.setImageResource(R.drawable.ic_play));
        }
    }

    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerFrame.getLayoutParams();
        if (isFullscreen) {
            params.height = 0;
            params.dimensionRatio = null;
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            infoSection.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            hideSystemUI();
            btnFullscreen.setImageResource(R.drawable.ic_fullscreen_exit);
        } else {
            params.height = 0;
            params.dimensionRatio = "16:9";
            params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
            infoSection.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            showSystemUI();
            btnFullscreen.setImageResource(R.drawable.ic_fullscreen);
        }
        playerFrame.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        if (webCustomView != null) {
            webView.getWebChromeClient().onHideCustomView();
        } else if (isFullscreen) {
            toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    private void resetHideControlsTimer() {
        handler.removeCallbacks(hideControlsRunnable);
        handler.postDelayed(hideControlsRunnable, 3000);
    }

    private String formatTime(int millis) {
        int totalSeconds = millis / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}