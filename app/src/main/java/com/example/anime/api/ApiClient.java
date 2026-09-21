package com.example.anime.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton de Retrofit para hablar con la API de animeDB.
 *
 * BASE_URL apunta a 10.0.2.2 (localhost del PC anfitrión visto desde el
 * emulador de Android Studio). Si pruebas en un dispositivo físico, cámbiala
 * por la IP local de tu PC en la red (ej. 192.168.1.X).
 */
public class ApiClient {
    // 10.0.2.2 = localhost de tu PC visto desde el emulador Android.
    // Si pruebas en un dispositivo físico, cambia esto por la IP local de tu PC (ej. 192.168.1.X).
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
