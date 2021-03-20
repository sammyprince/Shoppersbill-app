package com.app.onlineshoppersbill.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Package implements Serializable {

    @SerializedName("package_id")
    private String packageId;

    @SerializedName("package_name")
    private String packageName;

    @SerializedName("package_description")
    private String packageDescription;

    @SerializedName("package_location_count")
    private String packageLocationCount;

    @SerializedName("package_user_count")
    private String packageUserCount;

    @SerializedName("package_product_count")
    private String packageProductCount;

    @SerializedName("package_booking")
    private String packageBooking;

    @SerializedName("package_kitchen")
    private String packageKitchen;

    @SerializedName("package_order_screen")
    private String packageOrderScreen;

    @SerializedName("package_tables")
    private String packageTables;

    @SerializedName("package_invoice_count")
    private String packageInvoiceCount;

    @SerializedName("package_interval")
    private String packageInterval;

    @SerializedName("package_interval_count")
    private String packageIntervalCount;

    @SerializedName("package_trial_days")
    private String packageTrialDays;

    @SerializedName("package_price")
    private String packagePrice;

    @SerializedName("value")
    private String value;

    public String getPackageId() {
        return packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public String getPackageLocationCount() {
        return packageLocationCount;
    }

    public String getPackageUserCount() {
        return packageUserCount;
    }

    public String getPackageProductCount() {
        return packageProductCount;
    }

    public String getPackageBooking() {
        return packageBooking;
    }

    public String getPackageKitchen() {
        return packageKitchen;
    }

    public String getPackageOrderScreen() {
        return packageOrderScreen;
    }

    public String getPackageTables() {
        return packageTables;
    }

    public String getPackageInvoiceCount() {
        return packageInvoiceCount;
    }

    public String getPackageInterval() {
        return packageInterval;
    }

    public String getPackageIntervalCount() {
        return packageIntervalCount;
    }

    public String getPackageTrialDays() {
        return packageTrialDays;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public String getValue() {
        return value;
    }
}