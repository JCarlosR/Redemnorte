package com.youtube.sorcjc.redemnorte.io.response;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.redemnorte.model.BienConsolidado;

public class ByPatrimonialResponse {

    @SerializedName("bien")
    private BienConsolidado bienConsolidado;

    @SerializedName("mensaje")
    private String message;

    @SerializedName("error")
    private boolean error;

    public BienConsolidado getBienConsolidado() {
        return bienConsolidado;
    }

    public void setBienConsolidado(BienConsolidado bienConsolidado) {
        this.bienConsolidado = bienConsolidado;
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
