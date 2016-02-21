package com.examen.raveadministrator.modelos;

import java.io.Serializable;

public class Festival implements Serializable {

    private String idFestival;
    private String nombreFestival;
    private String ubicacionFestival;
    private String logoUrl;
    private String backUrl;
    private String latitudFestival;
    private String longitudFestival;
    private String descripcionFestival;
    private String webUrlFestival;
    private String instagramUserFestival;
    private int logoId;
    private int backId;

    public Festival(String idFestival, String nombreFestival, String ubicacionFestival, String logoUrl, String backUrl) {
        this.idFestival = idFestival;
        this.nombreFestival = nombreFestival;
        this.ubicacionFestival = ubicacionFestival;
        this.logoUrl = logoUrl;
        this.backUrl = backUrl;
    }

    public Festival(String idFestival, String nombreFestival, String ubicacionFestival, int logoId, int backId) {
        this.idFestival = idFestival;
        this.nombreFestival = nombreFestival;
        this.ubicacionFestival = ubicacionFestival;
        this.logoId = logoId;
        this.backId = backId;
    }

    public String getIdFestival() {
        return idFestival;
    }

    public void setIdFestival(String idFestival) {
        this.idFestival = idFestival;
    }

    public String getNombreFestival() {
        return nombreFestival;
    }

    public void setNombreFestival(String nombreFestival) {
        this.nombreFestival = nombreFestival;
    }

    public String getUbicacionFestival() {
        return ubicacionFestival;
    }

    public void setUbicacionFestival(String ubicacionFestival) {
        this.ubicacionFestival = ubicacionFestival;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getLatitudFestival() {
        return latitudFestival;
    }

    public void setLatitudFestival(String latitudFestival) {
        this.latitudFestival = latitudFestival;
    }

    public String getLongitudFestival() {
        return longitudFestival;
    }

    public void setLongitudFestival(String longitudFestival) {
        this.longitudFestival = longitudFestival;
    }

    public String getDescripcionFestival() {
        return descripcionFestival;
    }

    public void setDescripcionFestival(String descripcionFestival) {
        this.descripcionFestival = descripcionFestival;
    }

    public String getWebUrlFestival() {
        return webUrlFestival;
    }

    public void setWebUrlFestival(String webUrlFestival) {
        this.webUrlFestival = webUrlFestival;
    }

    public String getInstagramUserFestival() {
        return instagramUserFestival;
    }

    public void setInstagramUserFestival(String instagramUserFestival) {
        this.instagramUserFestival = instagramUserFestival;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public int getBackId() {
        return backId;
    }

    public void setBackId(int backId) {
        this.backId = backId;
    }
}
