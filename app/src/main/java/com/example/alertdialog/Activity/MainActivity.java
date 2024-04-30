package com.example.alertdialog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.R;

public class MainActivity extends AppCompatActivity {

    private String customerId = LoginActivity.customerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);


        // 找到 "包裹下单" 的LinearLayout
        LinearLayout actionOrderLayout = findViewById(R.id.action_order);
        LinearLayout actionSenderList = findViewById(R.id.action_senderList);
        LinearLayout actionReceiverList = findViewById(R.id.action_receiverList);

        // 给 "包裹下单" 添加点击事件
        actionOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        actionSenderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });

        actionReceiverList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
                startActivity(intent);

            }
        });
    }
}
