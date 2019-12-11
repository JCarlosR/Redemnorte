package com.youtube.sorcjc.redemnorte.io.response;

import com.youtube.sorcjc.redemnorte.model.Item;

import java.util.ArrayList;

public class BienesResponse {

    private ArrayList<Item> bienes;
    private boolean error;

    public ArrayList<Item> getBienes() {
        return bienes;
    }

    public void setBienes(ArrayList<Item> bienes) {
        this.bienes = bienes;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
