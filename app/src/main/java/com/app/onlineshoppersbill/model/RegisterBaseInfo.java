package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterBaseInfo {
    @SerializedName("currency_list")
    private List<String> currencyList;

    @SerializedName("timezone_list")
    private List<String> timezoneList;

    @SerializedName("value")
    private String value;

    public List<String> getCurrencyList() {
        return currencyList;
    }
    public List<String> getTimezoneList() {
        return timezoneList;
    }
    public String getValue() {
        return value;
    }
}