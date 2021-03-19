package com.app.onlinesmartpos.model;

import com.google.gson.annotations.SerializedName;

public class Subscribe {
    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }
}