package com.payten.msu.cse;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 * <p>
 * {- visa | mastercard | american-express | diners-club | discover | jcb | troy | dinacard}
 */
public enum CardBrand {
    VISA("visa"),
    MASTERCARD("mastercard"),
    AMERICAN_EXPRESS("american-express"),
    DINERS_CLUB("diners-club"),
    DISCOVER("discover"),
    JCB("jcb"),
    TROY("troy"),
    DINACARD("dinacard"),
    UNION_PAY("union-pay"),
    UNKNOWN("unknown");

    private final String brand;

    CardBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
}
