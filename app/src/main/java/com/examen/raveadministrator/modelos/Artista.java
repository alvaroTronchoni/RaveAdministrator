package com.examen.raveadministrator.modelos;

import java.io.Serializable;

public class Artista implements Serializable {

    private String idArtista;
    private String nombreArtista;
    private String generoArtista;
    private String urlPerfil;
    private String urlBack;
    private String userInstagram;
    private String popularity;
    String followers;
    private int idPerfil;
    private int idBack;

    public Artista(String idArtista, String nombreArtista, String generoArtista, String urlPerfil, String urlBack, String userInstagram, String popularity) {
        this.idArtista = idArtista;
        this.nombreArtista = nombreArtista;
        this.generoArtista = generoArtista;
        this.urlPerfil = urlPerfil;
        this.urlBack = urlBack;
        this.userInstagram = userInstagram;
        this.popularity = popularity;
    }

    public Artista(String nombreArtista, String generoArtista, int idPerfil, int idBack) {
        this.nombreArtista = nombreArtista;
        this.generoArtista = generoArtista;
        this.idPerfil = idPerfil;
        this.idBack = idBack;
    }

    public String getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(String idArtista) {
        this.idArtista = idArtista;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public void setNombreArtista(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public String getGeneroArtista() {
        return generoArtista;
    }

    public void setGeneroArtista(String generoArtista) {
        this.generoArtista = generoArtista;
    }

    public String getUrlPerfil() {
        return urlPerfil;
    }

    public void setUrlPerfil(String urlPerfil) {
        this.urlPerfil = urlPerfil;
    }

    public String getUrlBack() {
        return urlBack;
    }

    public void setUrlBack(String urlBack) {
        this.urlBack = urlBack;
    }

    public String getUserInstagram() {
        return userInstagram;
    }

    public void setUserInstagram(String userInstagram) {
        this.userInstagram = userInstagram;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public int getIdBack() {
        return idBack;
    }

    public void setIdBack(int idBack) {
        this.idBack = idBack;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
}
