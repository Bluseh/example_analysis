package com.example.alertdialog.pojo;

public class MyData {
    private String name;
    private String telCode;
    private String address;

    public MyData(String name, String telCode, String address) {
        this.name = name;
        this.telCode = telCode;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getTelCode() {
        return telCode;
    }

    public String getAddress() {
        return address;
    }
}

