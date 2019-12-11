package com.youtube.sorcjc.redemnorte.io.response;

import com.youtube.sorcjc.redemnorte.model.Item;

public class BienResponse {

    private Item item;
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
}
