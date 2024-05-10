package com.example.alertdialog.Activity;

// MainActivity2.java

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.adapters.CustomAdapter;
import com.example.alertdialog.pojo.Customer;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.Express;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// MainActivity2.java


public class ReceiveActivity extends AppCompatActivity {

    List<Customer> customerList = null;
    private String customerId = LoginActivity.customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            OkHttpClient client = new OkHttpClient();
            String customerUrl = "http://10.166.1.155:8080/REST/Misc/Customer/getCustomerNameList";
            final Request request = new Request.Builder().url(customerUrl).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();
            Gson gson = new Gson();
            Type customerListType = new TypeToken<List<Customer>>() {}.getType();
            customerList = gson.fromJson(content, customerListType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Customer customer : customerList) {
            System.out.println(customer.toString());
        }

        List<Express> haveExpressList = null;

        try {
            OkHttpClient client = new OkHttpClient();
            String expressUrl = "http://10.166.1.155:8080/REST/Domain/Express/getExpressesByReceiver/" + customerId;
            final Request request = new Request.Builder().url(expressUrl).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();
            Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Type expressListType = new TypeToken<List<Express>>() {
            }.getType();
            haveExpressList = builderTime.fromJson(content, expressListType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Express> packageList = new ArrayList<>(haveExpressList);

        CustomAdapter adapter = new CustomAdapter(this, R.layout.list_item_layout, packageList, customerList, 2);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReceiveActivity.this, "请使用返回键返回！", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Express selectedPackage = packageList.get(position);
                int send = selectedPackage.getSender();
                int type = selectedPackage.getType();
                String name = null;
                String addr = null;
                String tel = null;
                Customer desiredCustomer = null;
                for(Customer customer : customerList) {
                    if(customer.getId() == send) {
                        desiredCustomer = customer;
                        break;
                    }
                }
                addr = selectedPackage.getSenderAddress();
                tel = desiredCustomer.getTelCode();
                name = desiredCustomer.getName();

                Intent intent = new Intent(ReceiveActivity.this, DetailActivity.class);
                intent.putExtra("mode", 2);
                intent.putExtra("sender", name);
                intent.putExtra("type", type);
                intent.putExtra("addr", addr);
                intent.putExtra("tel", tel);
                startActivity(intent);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });


    }
}

