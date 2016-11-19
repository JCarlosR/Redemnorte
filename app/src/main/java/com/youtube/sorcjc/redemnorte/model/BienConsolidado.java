package com.youtube.sorcjc.redemnorte.model;

import com.google.gson.annotations.SerializedName;

public class BienConsolidado {

    // General
    @SerializedName("denominacion")
    private String description;
    @SerializedName("marca")
    private String brand;
    @SerializedName("modelo")
    private String model;
    @SerializedName("serie")
    private String series;

    // Status
    @SerializedName("estado")
    private String estado;

    @SerializedName("empleado")
    private String empleado;
    @SerializedName("situacion")
    private String situacion;

    private String ubicacion;
    private String local;

    public BienConsolidado(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String preservation) {
        this.estado = preservation;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getEmpleado() {
        return empleado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }
}
