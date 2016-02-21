package com.examen.raveadministrator.modelos;

public class Comentario {

    private String nombreAutor;
    private String perfilUsuario;
    private String fecha;
    private String texto;

    public Comentario(String nombreAutor, String perfilUsuario, String fecha, String texto) {
        this.nombreAutor = nombreAutor;
        this.perfilUsuario = perfilUsuario;
        this.fecha = fecha;
        this.texto = texto;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(String perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
