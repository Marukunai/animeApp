package com.example.anime.model;

public class Video {
    private int id;
    private int idAnime;
    private String episode;
    private String url;
    private String image;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAnime() { return idAnime; }
    public void setIdAnime(int idAnime) { this.idAnime = idAnime; }

    public String getEpisode() { return episode; }
    public void setEpisode(String episode) { this.episode = episode; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}