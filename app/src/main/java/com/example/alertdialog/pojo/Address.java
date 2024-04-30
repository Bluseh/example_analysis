package com.example.alertdialog.pojo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Address implements Serializable {

    public Address() {
    }

    @Expose
    private Integer id;
    @Expose
    private Integer customerId;

    @Expose
    private String address;

    @Expose
    private String regionCode;

    @Expose
    private String name;

    @Expose
    private String tel_code;

    public String getTel_code() {
        return tel_code;
    }

    public void setTel_code(String tel_code) {
        this.tel_code = tel_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", address='" + address + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", name='" + name + '\'' +
                ", tel_code='" + tel_code + '\'' +
                '}';
    }
}
