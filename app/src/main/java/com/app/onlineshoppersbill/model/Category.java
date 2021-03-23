package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

public class Category {


    @SerializedName("product_category_id")
    private String productCategoryId;


    @SerializedName("product_category_name")
    private String productCategoryName;

    @SerializedName("product_category_code")
    private String productCategoryCode;

    @SerializedName("product_category_description")
    private String productCategoryDescription;


    @SerializedName("value")
    private String value;


    public String getProductCategoryId() {
        return productCategoryId;
    }
    public String getProductCategoryName() {
        return productCategoryName;
    }
    public String getProductCategoryDescription() {
        return productCategoryDescription;
    }
    public String getProductCategoryCode() {
        return productCategoryCode;
    }


    public String getValue() {
        return value;
    }


}