package com.example.alertdialog.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.R;

// DetailActivity.java
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv1 = findViewById(R.id.textViewRecipient);
        TextView tv2 = findViewById(R.id.textViewSender);
        TextView tv3 = findViewById(R.id.textViewContent);
        TextView tv4 = findViewById(R.id.textViewAddress);
        TextView tv5 = findViewById(R.id.textViewPhoneNumber);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", -1);
        if (mode == 1) {
            String name = intent.getStringExtra("receiver");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            if (name != null && type != -1 && addr != null && tel!= null) {
                tv1.setText("收件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("收件人地址：" + addr);
                tv5.setText("收件人电话：" + tel);
            }
        } else if (mode == 2) {
            String name = intent.getStringExtra("sender");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            if (name != null && type != -1 && addr != null && tel!= null) {
                tv2.setText("寄件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("寄件人地址：" + addr);
                tv5.setText("寄件人电话：" + tel);
            }

        } else {
            //错误处理
        }
    }
}


