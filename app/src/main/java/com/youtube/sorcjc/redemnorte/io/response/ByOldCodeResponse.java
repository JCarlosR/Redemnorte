package com.youtube.sorcjc.redemnorte.io.response;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.redemnorte.model.Item;

public class ByOldCodeResponse {

    @SerializedName("item")
    private Item item;

    @SerializedName("mensaje")
    private String message;

    @SerializedName("error")
    private boolean error;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
