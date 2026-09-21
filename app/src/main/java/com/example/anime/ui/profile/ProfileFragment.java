package com.example.anime.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anime.activity.FavoritosActivity;
import com.example.anime.R;
import com.example.anime.activity.LoginActivity;

/**
 * Versión en Fragment del perfil: reutiliza el layout de ProfileActivity
 * (activity_profile) pero solo implementa los botones de Favoritos y
 * Cerrar sesión — no carga los datos del usuario como sí hace ProfileActivity.
 */
public class ProfileFragment extends Fragment {

    private Button btnFavoritos, btnCerrarSesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        btnFavoritos = view.findViewById(R.id.btnFavoritos);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        btnFavoritos.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FavoritosActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferences prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });


        return view;
    }
}
