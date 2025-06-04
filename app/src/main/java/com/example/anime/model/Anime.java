package com.example.anime.model;

public class Anime {
    private int id;
    private String name;
    private String description;
    private String type;
    private int year;
    private String image;
    private String originalName;
    private String rating;
    private String demography;
    private String genre;
    private String image2;
    private String image3;
    private boolean active;

    // Campo adicional para Android
    private boolean favorito;

    public Anime(int id, String name, String description, String type, int year,
                 String image, String originalName, String rating, String demography,
                 String genre, String image2, String image3, boolean active, boolean favorito) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.year = year;
        this.image = image;
        this.originalName = originalName;
        this.rating = rating;
        this.demography = demography;
        this.genre = genre;
        this.image2 = image2;
        this.image3 = image3;
        this.active = active;
        this.favorito = favorito;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public int getYear() { return year; }
    public String getImage() { return image; }
    public String getOriginalName() { return originalName; }
    public String getRating() { return rating; }
    public String getDemography() { return demography; }
    public String getGenre() { return genre; }
    public String getImage2() { return image2; }
    public String getImage3() { return image3; }
    public boolean isActive() { return active; }
    public boolean isFavorito() { return favorito; }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}