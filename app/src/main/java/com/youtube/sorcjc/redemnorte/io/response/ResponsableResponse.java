package com.youtube.sorcjc.redemnorte.io.response;

import com.youtube.sorcjc.redemnorte.model.ResponsibleUser;

import java.util.ArrayList;

public class ResponsableResponse {

    private ArrayList<ResponsibleUser> responsables;
    private boolean error;

    public ArrayList<ResponsibleUser> getResponsables() {
        return responsables;
    }

    public void setResponsables(ArrayList<ResponsibleUser> responsables) {
        this.responsables = responsables;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
