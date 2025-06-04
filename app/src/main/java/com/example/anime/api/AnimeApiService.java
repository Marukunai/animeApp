package com.example.anime.api;

import com.example.anime.model.Anime;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AnimeApiService {
    @GET("animes")
    Call<List<Anime>> getAnimesPorUsuario(@Query("userId") int userId);

    @POST("/favoritos/add")
    Call<Void> insertarFavorito(@Body Map<String, Integer> data);
}