package com.example.anime.api;

import com.example.anime.model.Anime;
import com.example.anime.model.Usuario;
import com.example.anime.model.Video;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Definición Retrofit de los endpoints de animeDB usados por la app.
 *
 * OJO — insertarFavorito() manda un @Body JSON con userId/animeId, pero el
 * controller del backend (FavoritoController) espera esos mismos datos como
 * query params (@RequestParam); revisar antes de dar por hecho que un
 * favorito se guarda correctamente.
 */
public interface AnimeApiService {
    /** Catálogo de animes, opcionalmente filtrado por usuario. */
    @GET("animes")
    Call<List<Anime>> getAnimesPorUsuario(@Query("userId") int userId);

    /** Añade un anime a favoritos. Ver nota de desajuste con el backend arriba. */
    @POST("/favoritos/add")
    Call<Void> insertarFavorito(@Body Map<String, Integer> data);

    /** Favoritos de un usuario. */
    @GET("favoritos/{userId}")
    Call<List<Anime>> getUserFavorites(@Path("userId") int userId);

    /** Quita un anime de favoritos. */
    @DELETE("favoritos/remove/{userId}/{animeId}")
    Call<Void> removeFavorite(@Path("userId") int userId, @Path("animeId") int animeId);

    /** Login por email + password (POST /usuarios/login en el backend). */
    @FormUrlEncoded
    @POST("usuarios/login")
    Call<Usuario> login(
            @Field("email") String email,
            @Field("password") String password
    );

    /** Registro de usuario (POST /usuarios en el backend). */
    @POST("usuarios")
    Call<Usuario> createUsuario(@Body Usuario usuario);

    /** Datos de un usuario por id (perfil). */
    @GET("/usuarios/{id}")
    Call<Usuario> obtenerUsuarioPorId(@Path("id") int id);

    /** Episodios de un anime. */
    @GET("videos/anime/{animeId}")
    Call<List<Video>> getVideosPorAnime(@Path("animeId") int animeId);
}
