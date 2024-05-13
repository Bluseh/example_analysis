package com.example.alertdialog.pojo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Transhistory implements Serializable {
    @Expose
    private Integer id;
    @Expose private String eid;
    @Expose private Double lat;
    @Expose private Double lon;
    @Expose private int type;

    public Transhistory() {}

    public Transhistory(String eid, Double lat, Double lon,int type) {
        this.eid = eid;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
    }

    public Transhistory(Integer id, String eid, Double lat, Double lon) {
        this.id = id;
        this.eid = eid;
        this.lat = lat;
        this.lon = lon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transhistory{" +
                "id=" + id +
                ", eid='" + eid + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", type=" + type +
                '}';
    }

    public static final class TYPE {
        public static final int START = 1;  // 出发地
        public static final int END = 2;  // 结束地
        public static final int TRANSFER = 3; // 中转站
    }
}

