package com.nishantboro.splititeasy;


public class BillType {
    private String item;
    private String cost;
    private String currency;

    BillType(String item, String cost, String currency) {
        this.item = item;
        this.cost = cost;
        this.currency = currency;
    }

    public String getItem() {
        return item;
    }

    public String getCost() {
        return cost;
    }

    public String getCurrency() {
        return currency;
    }
}
