package com.example.myapplication.model;

public class PaymentHistory {
    private String id,name,amount,permonth,date,status,month;

    public PaymentHistory(){

    }
    public PaymentHistory(String id, String name, String amount, String permonth, String date, String status, String month) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.permonth = permonth;
        this.date = date;
        this.status = status;
        this.month = month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPermonth() {
        return permonth;
    }

    public void setPermonth(String permonth) {
        this.permonth = permonth;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }





}
