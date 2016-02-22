package com.examen.raveadministrator.modelos;

public class TopTrack {

    private String imageUrl;
    private String duration_ms;
    private String name;
    private String uri;
    private String artists;
    private String popularity;

    public TopTrack(String imageUrl, String duration_ms, String name, String uri, String artists, String popularity) {
        this.imageUrl = imageUrl;
        this.duration_ms = duration_ms;
        this.name = name;
        this.uri = uri;
        this.artists = artists;
        this.popularity = popularity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(String duration_ms) {
        this.duration_ms = duration_ms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
}
