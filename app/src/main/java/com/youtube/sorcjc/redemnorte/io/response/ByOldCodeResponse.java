package com.youtube.sorcjc.redemnorte.io.response;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.redemnorte.model.Bien;
import com.youtube.sorcjc.redemnorte.model.BienConsolidado;

public class ByOldCodeResponse {

    @SerializedName("bien")
    private Bien bien;

    @SerializedName("mensaje")
    private String message;

    @SerializedName("error")
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
