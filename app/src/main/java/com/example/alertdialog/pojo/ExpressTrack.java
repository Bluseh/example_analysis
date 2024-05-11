package com.example.alertdialog.pojo;

public class ExpressTrack {
    private int type; //类型，0：当前位置（最新的一条物流信息），1：历史记录
    private String acceptTime; //接收时间
    private String acceptStation; //接收站点和描述

    public ExpressTrack() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ExpressTrack(int type, String acceptTime, String acceptStation) {
        this.type = type;
        this.acceptTime = acceptTime;
        this.acceptStation = acceptStation;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return acceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation;
    }
    public static final class STATUS{
        public static final int TYPE_CURRENT = 0; //当前
        public static final int TYPE_PAST = 1; //历史记录
    }
}
