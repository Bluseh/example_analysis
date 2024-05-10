package com.example.alertdialog.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.alertdialog.pojo.Address;
import com.example.alertdialog.adapters.AddressAdapter;
import com.example.alertdialog.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button addButton;
    private Button refreshButton;

    private List<Address> addressesList;

    private List<String> addresses = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> telCodes = new ArrayList<>();
    private List<String> regionCodes = new ArrayList<>();

    private AddressAdapter adapter;
    private String customerId = LoginActivity.customerId;

    private List<Address> splashedAddressList = SplashActivity.splashedAddressesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Intent intent = getIntent();
        Integer mode = intent.getIntExtra("mode", 0);

        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.addButton);
        refreshButton = findViewById(R.id.refreshButton);

        addressesList = splashedAddressList;
        System.out.println("已加载的地址簿:");
        System.out.println(addressesList);

        for (Address address : addressesList) {
            // System.out.println(address.getAddress());
            addresses.add(address.getAddress());
            names.add(address.getName());
            telCodes.add(address.getTel_code());
            regionCodes.add(address.getRegionCode());
        }

        adapter = new AddressAdapter(names, telCodes, addresses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {

            //TODO: 删除只区分地址，不区分名字和电话号
            @Override
            public void onItemClick(int position) {
                // 获取SharedPreferences实例
                SharedPreferences sharedPreferences = getSharedPreferences("selectedAddress", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (mode == 1) {
                    editor.putString("senderName", names.get(position));
                    editor.putString("senderTel", telCodes.get(position));
                    editor.putString("senderAddress", addresses.get(position));
                    editor.putString("senderRegionCode", regionCodes.get(position));
                } else if (mode == 2) {
                    editor.putString("receiverName", names.get(position));
                    editor.putString("receiverTel", telCodes.get(position));
                    editor.putString("receiverAddress", addresses.get(position));
                    editor.putString("receiverRegionCode", regionCodes.get(position));
                } else {
                    Toast.makeText(AddressActivity.this,"异常",Toast.LENGTH_SHORT);
                }
                editor.apply();
                finish();
            }

            @Override
            public void onItemLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this);
                builder.setTitle("提醒")
                        .setMessage("你确定要删除该地址吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String oldAddress = addresses.get(position);
                                try {
                                    addresses.remove(position);
                                    names.remove(position);
                                    telCodes.remove(position);
                                    regionCodes.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    String encodedString = URLEncoder.encode(oldAddress, "UTF-8");
                                    try {
                                        OkHttpClient client = new OkHttpClient();
                                        String expressUrl = "http://10.131.31.23:8080/REST/Misc/AddressList/deleteAddressByCustomerId?param=" + encodedString + "&id=" + customerId;
                                        System.out.println(expressUrl);
                                        String decodedString = URLDecoder.decode(encodedString, "UTF-8");
                                        System.out.println(decodedString);
                                        final Request request = new Request.Builder().url(expressUrl).build();
                                        Call call = client.newCall(request);
                                        Response response = call.execute();
                                        String content = response.header("state");
                                        System.out.println(content);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
                Toast.makeText(AddressActivity.this, "页面已刷新！", Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddressAddActivity.class);
                startActivity(intent);
            }
        });
    }
}

