package com.example.anime.model;

public class Video {
    private int id;
    private int animeId;
    private String titulo;
    private String descripcion;
    private int numero;
    private String url;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAnimeId() { return animeId; }
    public void setAnimeId(int animeId) { this.animeId = animeId; }

    public String getTitulo() { return "Episodio " + titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
