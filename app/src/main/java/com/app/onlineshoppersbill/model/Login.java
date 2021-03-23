package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

public class Login {


    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    @SerializedName("cell")
    private String cell;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("user_type")
    private String userType;

    @SerializedName("password")
    private String password;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    @SerializedName("shop_name")
    private String shopName;

    @SerializedName("shop_address")
    private String shopAddress;

    @SerializedName("shop_email")
    private String shopEmail;


    @SerializedName("shop_contact")
    private String shopContact;

    @SerializedName("tax")
    private String tax;
    @SerializedName("currency_symbol")
    private String currencySymbol;
    @SerializedName("shop_status")
    private String shopStatus;

    @SerializedName("public_key")
    private String public_key;

    @SerializedName("encrypt_key")
    private String encrypt_key;

    @SerializedName("currency_name")
    private String currency_name;

    @SerializedName("subscription_id")
    private String subscription_id;

    @SerializedName("token")
    private String token;





    public String getStaffId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCell() {
        return cell;
    }

    public String getPassword() {
        return password;
    }


    public String getValue() {
        return value;
    }


    public String getStoreID() {
        return storeId;
    }




    public String getUserType() {
        return userType;
    }

    public String getMassage() {
        return massage;
    }


    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }


    public String getShopEmail() {
        return shopEmail;
    }

    public String getShopContact() {
        return shopContact;
    }


    public String getShopStatus() {
        return shopStatus;
    }


    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getCurrencyName() {
        return currency_name;
    }
    public String getPublicKey() {
        return public_key;
    }
    public String getEncryptKey() {
        return encrypt_key;
    }

    public String getTax() {
        return tax;
    }
    public String getSubscriptionId() {
        return subscription_id;
    }

    public String getToken() { return token; }




}