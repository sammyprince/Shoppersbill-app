package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

public class BusinessLocation {


    @SerializedName("business_location_id")
    private String businessLocationId;


    @SerializedName("business_location_name")
    private String businessLocationName;


    @SerializedName("value")
    private String value;


    public String getLocationId() {
        return businessLocationId;
    }



    public String getLocationName() {
        return businessLocationName;
    }


    public String getValue() {
        return value;
    }

}