package com.example.anime;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.anime.ui.favoritos.FavoritosFragment;

public class FavoritosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        // Cargar el fragmento
        if (savedInstanceState == null) {
            FavoritosFragment fragment = new FavoritosFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_favoritos, fragment);
            transaction.commit();
        }
    }
}