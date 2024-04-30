package com.example.myapplication.model;

import java.util.List;

import java.util.List;

public class RegionData {
    private String id;
    private String name;
    private List<RegionData> list;

    public RegionData() {
        // 默认构造方法
    }

    public RegionData(String id, String name, List<RegionData> list) {
        this.id = id;
        this.name = name;
        this.list = list;
    }

    // Getter 和 Setter 方法
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

    public List<RegionData> getList() {
        return list;
    }

    public void setList(List<RegionData> list) {
        this.list = list;
    }
}

