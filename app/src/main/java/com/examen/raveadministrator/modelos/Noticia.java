package com.examen.raveadministrator.modelos;

import java.io.Serializable;

public class Noticia implements Serializable {

    private String url;
    private String imageUrl;
    private String name;
    private String summary;

    public Noticia(String url, String imageUrl, String name, String summary) {
        this.url = url;
        this.imageUrl = imageUrl;
        this.name = name;
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage_url() {
        return imageUrl;
    }

    public void setImage_url(String date_found) {
        this.imageUrl = date_found;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
