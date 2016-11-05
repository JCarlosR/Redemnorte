package com.youtube.sorcjc.redemnorte.io.response;

import com.youtube.sorcjc.redemnorte.model.Bien;

import java.util.ArrayList;

public class BienResponse {

    private Bien bien;
    private boolean error;

    public Bien getBien() {
        return bien;
    }

    public void setBien(Bien bien) {
        this.bien = bien;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
