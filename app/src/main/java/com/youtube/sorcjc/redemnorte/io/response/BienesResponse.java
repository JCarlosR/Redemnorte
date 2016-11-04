package com.youtube.sorcjc.redemnorte.io.response;

import com.youtube.sorcjc.redemnorte.model.Bien;

import java.util.ArrayList;

public class BienesResponse {

    private ArrayList<Bien> bienes;
    private boolean error;

    public ArrayList<Bien> getBienes() {
        return bienes;
    }

    public void setBienes(ArrayList<Bien> bienes) {
        this.bienes = bienes;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
