package com.example.anime.api;

import com.example.anime.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioApiService {
    @GET("usuarios/{id}")
    Call<Usuario> getUsuarioPorId(@Path("id") int id);

    @PUT("usuarios/{id}")
    Call<Void> actualizarPerfil(@Path("id") int id, @Body Usuario usuario);

}
