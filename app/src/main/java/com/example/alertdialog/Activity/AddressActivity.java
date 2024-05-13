package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertdialog.pojo.Address;
import com.example.alertdialog.adapters.AddressAdapter;
import com.example.alertdialog.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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

    private List<Address> addressesList = null;

    private List<String> addresses = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> telCodes = new ArrayList<>();
    private List<String> regionCodes = new ArrayList<>();

    private AddressAdapter adapter;
    private String customerId = LoginActivity.customerId;



    private String encodedString;
    private Integer mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.addButton);
        refreshButton = findViewById(R.id.refreshButton);

        new AddressListTask().execute();

    }


    private class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // 在后台执行网络请求
            try {
                OkHttpClient client = new OkHttpClient();
                String expressUrl = "http://" + ip + ":8080/REST/Misc/AddressList/deleteAddressByCustomerId?param=" + encodedString + "&id=" + customerId;
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 网络请求完成后的处理逻辑
            Toast.makeText(AddressActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddressListTask extends AsyncTask<Void, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(Void... voids) {
            List<Address> addresses = new ArrayList<>();
            try {
                OkHttpClient client = new OkHttpClient();
                String expressUrl = "http://" + ip + ":8080/REST/Misc/AddressList/getAddressListByCustomerId/" + customerId;
                Request request = new Request.Builder().url(expressUrl).build();
                System.out.println("\nyya\n753951" + expressUrl);
                Response response = client.newCall(request).execute();
                String content = response.body().string();
                Gson gson = new Gson();
                Type addressListType = new TypeToken<List<Address>>() {
                }.getType();
                addresses = gson.fromJson(content, addressListType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> result) {
            addressesList = result;
            if (addressesList != null) {
                System.out.println("asdas\n123\n" + addressesList.toString());
                for (Address address : addressesList) {
                    addresses.add(address.getAddress());
                    names.add(address.getName());
                    telCodes.add(address.getTel_code());
                    regionCodes.add(address.getRegionCode());
                }
                adapter = new AddressAdapter(names, telCodes, addresses);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddressActivity.this));

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
                            editor.apply();
                            finish();
                        } else if (mode == 2) {
                            editor.putString("receiverName", names.get(position));
                            editor.putString("receiverTel", telCodes.get(position));
                            editor.putString("receiverAddress", addresses.get(position));
                            editor.putString("receiverRegionCode", regionCodes.get(position));
                            editor.apply();
                            finish();
                        } else {
                            Toast.makeText(AddressActivity.this, "异常", Toast.LENGTH_SHORT);
                        }

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
                                        addresses.remove(position);
                                        names.remove(position);
                                        telCodes.remove(position);
                                        regionCodes.remove(position);
                                        addressesList.remove(position);
                                        adapter.notifyItemRemoved(position);

                                        try {
                                            encodedString = URLEncoder.encode(oldAddress, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            throw new RuntimeException(e);
                                        }

                                        Toast.makeText(AddressActivity.this, "发送请求", Toast.LENGTH_SHORT).show();
                                        new AddressActivity.NetworkTask().execute();

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
    }
}




