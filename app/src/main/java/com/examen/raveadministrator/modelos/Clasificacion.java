package com.examen.raveadministrator.modelos;

import java.io.Serializable;

public class Clasificacion implements Serializable {

    private String nombreClasificacion;
    private int idLogo;
    private int idBack;

    public Clasificacion(String nombreClasificacion, int idLogo, int idBack) {
        this.nombreClasificacion = nombreClasificacion;
        this.idLogo = idLogo;
        this.idBack = idBack;
    }

    public String getNombreClasificacion() {
        return nombreClasificacion;
    }

    public void setNombreClasificacion(String nombreClasificacion) {
        this.nombreClasificacion = nombreClasificacion;
    }

    public int getIdLogo() {
        return idLogo;
    }

    public void setIdLogo(int idLogo) {
        this.idLogo = idLogo;
    }

    public int getIdBack() {
        return idBack;
    }

    public void setIdBack(int idBack) {
        this.idBack = idBack;
    }
}

