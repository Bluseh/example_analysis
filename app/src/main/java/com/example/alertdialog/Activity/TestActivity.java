package com.example.alertdialog.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertdialog.R;
import com.example.alertdialog.adapters.MyAdapter;
import com.example.alertdialog.pojo.MyData;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private String customerId = "15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MyData> dataList = new ArrayList<>();
        dataList.add(new MyData("John", "12345", "123 Main St"));
        dataList.add(new MyData("Alice", "67890", "456 Elm St"));
        dataList.add(new MyData("Bob", "75757", "河南省三门峡市渑池县金堂财富大街"));

        MyAdapter adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }
}

