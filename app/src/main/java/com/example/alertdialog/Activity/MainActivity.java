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
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.util.PreferencesUtil;

public class MainActivity extends AppCompatActivity {

    private String customerId = LoginActivity.customerId;
    private Customer customer;
    PreferencesUtil preferencesUtil;
    public static final String ip = "10.10.11.226";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);


        // 找到 "包裹下单" 的LinearLayout
        LinearLayout actionAddressList = findViewById(R.id.action_addressList);
        LinearLayout actionOrderLayout = findViewById(R.id.action_order);
        LinearLayout actionSenderList = findViewById(R.id.action_senderList);
        LinearLayout actionReceiverList = findViewById(R.id.action_receiverList);
        LinearLayout actionSignedList = findViewById(R.id.action_signedList);
        LinearLayout actionMarkedList = findViewById(R.id.action_markedList);

        actionAddressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

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
        actionSignedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignedActivity.class);
                startActivity(intent);
            }
        });
        actionMarkedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MarkedActivity.class);
                startActivity(intent);
            }
        });
    }


}
