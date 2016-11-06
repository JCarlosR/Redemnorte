package com.youtube.sorcjc.redemnorte.io.response;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("mensaje")
    private String message;

    @SerializedName("error")
    private boolean error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
