package com.examen.raveadministrator.modelos;

public class Evento {

    private String nombreEvento;
    private String fecha_evento;
    private String ubicacion_evento;
    private String ticket_status;
    private String ticket_url;
    private String lat;
    private String lng;

    public Evento(String nombreEvento, String fecha_evento, String ubicacion_evento, String ticket_status, String ticket_url, String lat, String lng) {
        this.nombreEvento = nombreEvento;
        this.fecha_evento = fecha_evento;
        this.ubicacion_evento = ubicacion_evento;
        this.ticket_status = ticket_status;
        this.ticket_url = ticket_url;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getFecha_evento() {
        return fecha_evento;
    }

    public void setFecha_evento(String fecha_evento) {
        this.fecha_evento = fecha_evento;
    }

    public String getUbicacion_evento() {
        return ubicacion_evento;
    }

    public void setUbicacion_evento(String ubicacion_evento) {
        this.ubicacion_evento = ubicacion_evento;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getTicket_url() {
        return ticket_url;
    }

    public void setTicket_url(String ticket_url) {
        this.ticket_url = ticket_url;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
