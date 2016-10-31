package com.youtube.sorcjc.redemnorte.model;

public class Detail {
    // General
    private String qr;
    private String patrimonial;
    private String description;
    private String color;
    private String brand;
    private String model;
    private String series;

    // Dimensions
    private String dimLong;
    private String dimWidth;
    private String dimHigh;

    // Status
    private String preservation;
    private boolean operative;
    private String observation;

    public Detail(String qr, String patrimonial, String description) {
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
}
