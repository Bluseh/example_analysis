package com.example.alertdialog.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.MyData;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static List<MyData> mDataList;

    public MyAdapter(List<MyData> dataList) {
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_address_rec, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyData data = mDataList.get(position);
        holder.textViewNameAndTelCode.setText(data.getName() + " " + data.getTelCode());
        holder.textViewAddress.setText(data.getAddress());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNameAndTelCode;
        TextView textViewAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameAndTelCode = itemView.findViewById(R.id.textViewNameAndTelCode);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);

            // 添加点击事件监听器
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // 获取当前点击的列表项的位置
            int position = getAdapterPosition();

            // 确保位置有效
            if (position != RecyclerView.NO_POSITION) {
                // 获取当前点击的列表项对应的数据
                MyData data = mDataList.get(position);
                // 输出姓名、电话号和地址
                Log.d("ClickedItem", "Name: " + data.getName());
                Log.d("ClickedItem", "TelCode: " + data.getTelCode());
                Log.d("ClickedItem", "Address: " + data.getAddress());
            }
        }
    }

}

