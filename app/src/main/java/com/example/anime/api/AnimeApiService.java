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

public interface AnimeApiService {
    @GET("animes")
    Call<List<Anime>> getAnimesPorUsuario(@Query("userId") int userId);

    @POST("/favoritos/add")
    Call<Void> insertarFavorito(@Body Map<String, Integer> data);

    @GET("favoritos/{userId}")
    Call<List<Anime>> getUserFavorites(@Path("userId") int userId);

    @DELETE("favoritos/remove/{userId}/{animeId}")
    Call<Void> removeFavorite(@Path("userId") int userId, @Path("animeId") int animeId);

    @FormUrlEncoded
    @POST("/login")
    Call<Usuario> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("/register")
    Call<Usuario> createUsuario(@Body Usuario usuario);

    @GET("/usuarios/{id}")
    Call<Usuario> obtenerUsuarioPorId(@Path("id") int id);

    @GET("videos/anime/{animeId}")
    Call<List<Video>> getVideosPorAnime(@Path("animeId") int animeId);
}