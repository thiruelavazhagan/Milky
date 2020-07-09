package com.example.myapplication.model;

public class MonthlyPayment {

    private String id,name,status,monthamount,paiddate,totamount;
    public MonthlyPayment(){

    }
    public MonthlyPayment(String id, String name, String status, String monthamount, String paiddate, String totamount) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.monthamount = monthamount;
        this.paiddate = paiddate;
        this.totamount = totamount;
    }

    public MonthlyPayment(String id, String name, String status, String monthamount) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.monthamount = monthamount;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMonthamount() {
        return monthamount;
    }

    public void setMonthamount(String monthamount) {
        this.monthamount = monthamount;
    }

    public String getPaiddate() {
        return paiddate;
    }

    public void setPaiddate(String paiddate) {
        this.paiddate = paiddate;
    }

    public String getTotamount() {
        return totamount;
    }

    public void setTotamount(String totamount) {
        this.totamount = totamount;
    }


}
