package com.youtube.sorcjc.redemnorte.io.response;

import com.youtube.sorcjc.redemnorte.model.Responsable;

import java.util.ArrayList;

public class ResponsableResponse {

    private ArrayList<Responsable> responsables;
    private boolean error;

    public ArrayList<Responsable> getResponsables() {
        return responsables;
    }

    public void setResponsables(ArrayList<Responsable> responsables) {
        this.responsables = responsables;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
