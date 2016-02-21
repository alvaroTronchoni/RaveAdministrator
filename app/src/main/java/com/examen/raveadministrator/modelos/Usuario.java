package com.examen.raveadministrator.modelos;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String id;
    private String nombre;
    private String correo;
    private String claveApi;
    private String perfilUsuario;

    public Usuario(String id, String nombre, String correo, String perfilUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.perfilUsuario = perfilUsuario;
    }

    public Usuario(String id, String perfilUsuario) {
        this.id = id;
        this.perfilUsuario = perfilUsuario;
    }

    /*public Usuario(String nombre, String correo, String claveApi) {
        this.nombre = nombre;
        this.correo = correo;
        this.claveApi = claveApi;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClaveApi() {
        return claveApi;
    }

    public void setClaveApi(String claveApi) {
        this.claveApi = claveApi;
    }

    public String getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(String perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }
}
