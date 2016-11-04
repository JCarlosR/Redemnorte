package com.youtube.sorcjc.redemnorte.model;

import com.google.gson.annotations.SerializedName;

public class Bien {

    // Parent (header)
    @SerializedName("hoja_id")
    private String hoja_id;

    // Codes
    @SerializedName("QR_code")
    private String qr;
    @SerializedName("patrimonial_code")
    private String patrimonial;

    // Meta-data
    @SerializedName("old_code")
    private String old_code;
    @SerializedName("old_year")
    private String old_year;

    // General
    @SerializedName("denominacion")
    private String description;
    @SerializedName("marca")
    private String brand;
    @SerializedName("modelo")
    private String model;
    @SerializedName("serie")
    private String series;
    @SerializedName("color")
    private String color;

    // Dimensions
    @SerializedName("largo")
    private String dimLong;
    @SerializedName("ancho")
    private String dimWidth;
    @SerializedName("alto")
    private String dimHigh;

    // Status
    @SerializedName("condicion")
    private String preservation;
    @SerializedName("operativo")
    private boolean operative;
    @SerializedName("observacion")
    private String observation;

    public Bien(String qr, String patrimonial, String description) {
        this.qr = qr;
        this.patrimonial = patrimonial;
        this.description = description;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getDimLong() {
        return dimLong;
    }

    public void setDimLong(String dimLong) {
        this.dimLong = dimLong;
    }

    public String getDimWidth() {
        return dimWidth;
    }

    public void setDimWidth(String dimWidth) {
        this.dimWidth = dimWidth;
    }

    public String getDimHigh() {
        return dimHigh;
    }

    public void setDimHigh(String dimHigh) {
        this.dimHigh = dimHigh;
    }

    public String getPreservation() {
        return preservation;
    }

    public void setPreservation(String preservation) {
        this.preservation = preservation;
    }

    public boolean isOperative() {
        return operative;
    }

    public void setOperative(boolean operative) {
        this.operative = operative;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getPatrimonial() {
        return patrimonial;
    }

    public void setPatrimonial(String patrimonial) {
        this.patrimonial = patrimonial;
    }

    public String getHoja_id() {
        return hoja_id;
    }

    public void setHoja_id(String hoja_id) {
        this.hoja_id = hoja_id;
    }

    public String getOld_year() {
        return old_year;
    }

    public void setOld_year(String old_year) {
        this.old_year = old_year;
    }

    public String getOld_code() {
        return old_code;
    }

    public void setOld_code(String old_code) {
        this.old_code = old_code;
    }
}
