package com.example.alertdialog.adapters;

import com.example.alertdialog.pojo.Transhistory;

import java.util.List;

public class CurrentMapAdapter implements IDataAdapter<List<Transhistory>>{
    private List<Transhistory> transhistories;

    @Override
    public List<Transhistory> getData() {
        return transhistories;
    }

    @Override
    public void setData(List<Transhistory> data) {
        this.transhistories = data;
    }

    @Override
    public void notifyDataSetChanged() {

    }
}
