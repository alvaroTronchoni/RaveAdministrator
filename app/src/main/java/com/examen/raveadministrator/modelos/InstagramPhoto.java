package com.examen.raveadministrator.modelos;

public class InstagramPhoto {

    private String imageUrl;
    private String url;

    public InstagramPhoto(String imageUrl, String url) {
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
