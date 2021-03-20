package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

public class Suppliers {


    @SerializedName("supplier_id")
    private String suppliersId;

    @SerializedName("supplier_name")
    private String suppliersName;
    @SerializedName("supplier_email")
    private String suppliersEmail;

    @SerializedName("supplier_cell")
    private String suppliersCell;

    @SerializedName("supplier_address")
    private String suppliersAddress;

    @SerializedName("supplier_contact_person")
    private String suppliersContactPerson;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getSuppliersId() {
        return suppliersId;
    }

    public String getSuppliersName() {
        return suppliersName;
    }

    public String getSuppliersEmail() {
        return suppliersEmail;
    }

    public String getSuppliersCell() {
        return suppliersCell;
    }

    public String getSuppliersAddress() {
        return suppliersAddress;
    }


    public String getSuppliersContactPerson() {
        return suppliersContactPerson;
    }


    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}