package com.example.alertdialog.adapters;

public interface IDataAdapter<T> {
    public T getData();

    /*
    T 是一个类型参数，表示可以接受任意类型的数据
    */
    public void setData(T data);
    public void notifyDataSetChanged();
}
