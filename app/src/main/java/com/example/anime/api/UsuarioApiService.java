package com.example.anime.api;

import com.example.anime.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** Endpoints de perfil de usuario usados por EditarPerfilActivity. */
public interface UsuarioApiService {
    /** Datos actuales del usuario, para precargar el formulario de edición. */
    @GET("usuarios/{id}")
    Call<Usuario> getUsuarioPorId(@Path("id") int id);

    /** Guarda los cambios de nombre/teléfono del perfil. */
    @PUT("usuarios/{id}")
    Call<Void> actualizarPerfil(@Path("id") int id, @Body Usuario usuario);

}
