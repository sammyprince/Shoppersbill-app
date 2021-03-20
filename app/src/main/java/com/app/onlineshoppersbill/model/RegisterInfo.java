package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterInfo {
    @SerializedName("business_name")
    private String business_name;

    @SerializedName("email")
    private String email;

    @SerializedName("country")
    private String country;

    @SerializedName("currency")
    private String currency;

    @SerializedName("time_zone")
    private String time_zone;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("password")
    private String password;

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }
}