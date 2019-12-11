package com.youtube.sorcjc.redemnorte.io.response;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.redemnorte.model.Sheet;

public class HojaResponse {

    @SerializedName("sheet")
    private Sheet sheet;

    @SerializedName("error")
    private boolean error;

    @SerializedName("mensaje")
    private String message;

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
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
