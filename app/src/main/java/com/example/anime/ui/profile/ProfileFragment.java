package com.example.anime.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anime.R;

public class ProfileFragment extends Fragment {

    private Button btnFavoritos, btnCerrarSesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_perfil, container, false);

        btnFavoritos = view.findViewById(R.id.btnFavoritos);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        btnFavoritos.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FavoritosActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> {
            getActivity().finish(); // o lógica de logout
        });

        return view;
    }
}
