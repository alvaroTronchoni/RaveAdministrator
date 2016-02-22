package com.examen.raveadministrator.modelos;

public class ArtistaSimilar {

    private String nombre;
    private String match;
    private String url;
    private String imageUrl;
    private String popularity;

    public ArtistaSimilar(String nombre, String match, String url, String imageUrl, String popularity) {
        this.nombre = nombre;
        this.match = match;
        this.url = url;
        this.imageUrl = imageUrl;
        this.popularity = popularity;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
}
