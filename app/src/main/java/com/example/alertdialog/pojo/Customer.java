package com.example.alertdialog.pojo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Customer implements Serializable {

	public Customer() {
	}
	
	@Expose private Integer id;
	@Expose private String name;
	@Expose private String address;
	@Expose private String telCode;
	@Expose private String regionCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelCode() {
		return telCode;
	}

	public void setTelCode(String telCode) {
		this.telCode = telCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}


	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", telCode='" + telCode + '\'' +
				", regionCode='" + regionCode + '\'' +
				'}';
	}
}
