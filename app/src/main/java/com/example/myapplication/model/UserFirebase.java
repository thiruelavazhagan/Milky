package com.example.myapplication.model;

public class UserFirebase {

    private String name;
    private String phone;
    private String address;
    private String start;
    private String milk;
    private String milkcq;
    private String perday;
    private String id;

    public UserFirebase(){

    }


    public UserFirebase(String id, String name, String phone, String address, String start, String milk, String milkcq, String perday) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.start = start;
        this.milk = milk;
        this.milkcq = milkcq;
        this.perday = perday;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public String getMilkcq() {
        return milkcq;
    }

    public void setMilkcq(String milkcq) {
        this.milkcq = milkcq;
    }

    public String getPerday() {
        return perday;
    }

    public void setPerday(String perday) {
        this.perday = perday;
    }
}
