package com.example.myapplication.model;

public class MilkDetail {

    private String name;
    private String amount;
    private String qty;

    public MilkDetail(String name, String amount, String qty) {
        this.name = name;
        this.amount = amount;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }




}
