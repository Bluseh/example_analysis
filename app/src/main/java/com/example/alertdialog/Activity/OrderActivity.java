package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.Address;
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.pojo.Express;
import com.example.myapplication.model.RegionData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1;
    private ProgressBar progressBar;
    private Express express;

    private EditText weightEditText;
    private EditText feeEditText;
    private EditText idEditText;
    private EditText createTimeEditText;
    private Spinner typeSpinner;
    //    private TextView senderTextView;
    private TextView senderTextViewProfile;
    private LinearLayout sender;
    private TextView senderAddressTextView;
    private LinearLayout receiver;
    private TextView receiverTextViewProfile;
    //    private TextView receiverTextView;
    private TextView receiverAddressTextView;


    private Button submitBtn;
    //    private Button addressesBtn;
    private ImageView barcodeImageView;
    private Button backButton;
//    private Button refreshButton;


    private String senderName;
    private String senderTel;
    private String senderAddress;
    private String senderRegionCode;

    private String receiverName;
    private String receiverTel;
    private String receiverAddress;
    private String receiverRegionCode;
    private String createTime;


    private List<Customer> customers = new ArrayList<>();
    private List<Address> addressList = new ArrayList<>();

    private Map<String, Integer> customer = new HashMap<>();
    private Map<String, Integer> type = new HashMap<>();
//    private Map<String, String> Address_RegionCode = new HashMap<>();
//    private Map<String, String> District_RegionCode = new HashMap<>();
//    private Map<String, RegionData> Province_ProvinceRegionData = new HashMap<>();
//    private Map<String, RegionData> City_CityRegionData = new HashMap<>();

    private String baseUrl = "http://" + ip + ":8080/REST/";
    OkHttpClient client = new OkHttpClient();

//    private List<RegionData> provinceList;
//    private List<RegionData> cityList;
//    private List<RegionData> districtList;

    private String customerId = LoginActivity.customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: 下单点两次
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initializeViews();
        loadCustomerData();
//        loadAddressData();


        submitOrder();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initializeViews() {
        // Initialize EditText, Spinner, and Button
        // 初始化EditText、Spinner和Button
        type.put("服装", 1);
        type.put("食品", 2);
        type.put("电子产品", 3);
        type.put("贵重物品", 4);
        type.put("书籍", 5);
        type.put("信件", 6);
        type.put("化学用品", 7);
        type.put("其他", 8);
        Set<String> keySet = type.keySet();
        ArrayList<String> types = new ArrayList<>(keySet);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Bind views
//        refreshButton = findViewById(R.id.refreshButton);
        weightEditText = findViewById(R.id.weightEditText);
        feeEditText = findViewById(R.id.feeEditText);
        feeEditText.setEnabled(false);
        idEditText = findViewById(R.id.idEditText);
        idEditText.setEnabled(false);
        createTimeEditText = findViewById(R.id.createTimeEditText);
        createTimeEditText.setEnabled(false);
        typeSpinner = findViewById(R.id.typeSpinner);
        typeSpinner.setAdapter(arrayAdapter);
        progressBar = findViewById(R.id.progressBar);
        sender = findViewById(R.id.sender);
        receiver = findViewById(R.id.receiver);
//        senderTextView = findViewById(R.id.senderTextView);
        senderTextViewProfile = findViewById(R.id.senderTextViewProfile);
//        senderTextView.setEnabled(false);
        senderAddressTextView = findViewById(R.id.senderTextView_address);
//        senderAddressTextView.setEnabled(false);
//        receiverTextView = findViewById(R.id.receiverTextView);
        receiverTextViewProfile = findViewById(R.id.receiverTextViewProfile);
//        receiverTextView.setEnabled(false);
        receiverAddressTextView = findViewById(R.id.receiverTextView_address);
//        receiverAddressTextView.setEnabled(false);
        submitBtn = findViewById(R.id.submitBtn);
        backButton = findViewById(R.id.back_button);
        barcodeImageView = findViewById(R.id.barcodeImageView);


        SharedPreferences sharedPreferences = getSharedPreferences("selectedAddress", Context.MODE_PRIVATE);

        // TODO: 根据存储显示上次选择的寄件地址和收件地址，但不会区分用户
        senderName = sharedPreferences.getString("senderName", "");
        senderTel = sharedPreferences.getString("senderTel", "");
        senderAddress = sharedPreferences.getString("senderAddress", "");
        senderRegionCode = sharedPreferences.getString("senderRegionCode", "");
        System.out.println("11111111" + senderName + senderTel + senderAddress);
        receiverName = sharedPreferences.getString("receiverName", "");
        receiverTel = sharedPreferences.getString("receiverTel", "");
        receiverAddress = sharedPreferences.getString("receiverAddress", "");
        receiverRegionCode = sharedPreferences.getString("receiverRegionCode", "");

        senderTextViewProfile.setText(senderName + " " + senderTel);
        senderAddressTextView.setText(senderAddress);
        receiverTextViewProfile.setText(receiverName + " " + receiverTel);
        receiverAddressTextView.setText(receiverAddress);
        // Set up adapters
        // Set up listeners
        //TODO:线程异步
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectSenderAndReceiverAndRefresh();
            }
        });

    }

    private void loadCustomerData() {
        // 创建一个请求对象，指定要访问的URL
        Request request = new Request.Builder()
                .url(baseUrl + "Misc/Customer/getCustomerNameList") // 示例URL
                .build();

        // 使用OkHttpClient发起请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 处理成功响应
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    Type customerListType = new TypeToken<List<Customer>>() {
                    }.getType();
                    customers = gson.fromJson(responseData, customerListType);
                    customer = new HashMap<>();
                    for (int i = 0; i < customers.size(); i++) {
                        customer.put(customers.get(i).getTelCode(), customers.get(i).getId());
//                        customer_address.put(customers.get(i).getName(), customers.get(i).getAddress());
                    }


                    Log.d("HTTP Response", responseData);

                } else {
                    // 处理失败响应
                    Log.e("HTTP Response", "Unexpected code " + response);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // 处理请求失败
                e.printStackTrace();
            }
        });

    }

    private void submitOrder() {
        // Prepare Express object with user input
        // Send POST request to the server to save the order
        // Update UI based on server response
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("111111111按钮被点");
                progressBar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(weightEditText.getText().toString())) {
                    Toast.makeText(OrderActivity.this, "重量不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(feeEditText.getText().toString())) {
//                    Toast.makeText(OrderActivity.this, "费用不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (senderTextViewProfile.getText() == null || senderAddressTextView.getText() == null) {
                    Toast.makeText(OrderActivity.this, "寄件人地址为空,请设置寄件地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (receiverTextViewProfile.getText() == null || receiverAddressTextView.getText() == null) {
                    Toast.makeText(OrderActivity.this, "收件人地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("通过校验");
                Float weight = Float.parseFloat(weightEditText.getText().toString());
//                Float fee = Float.parseFloat(feeEditText.getText().toString());
                Float fee = null;
                Integer type_id = type.get((String) typeSpinner.getSelectedItem());
                Integer sender_id = customer.get(senderTel);
                String senderAddress = senderAddressTextView.getText().toString();
                Integer receiver_id = customer.get(receiverTel);
                String receiverAddress = receiverAddressTextView.getText().toString();
                if (senderRegionCode.charAt(0) == receiverRegionCode.charAt(0)) {
                    if (weight <= 1.0) {
                        fee = (float) 13.0;
                    } else fee = (float) (13.0 + (weight - 1.0) * 2.0);
                } else {
                    if (weight <= 1.0) {
                        fee = (float) 18.0;
                    } else fee = (float) (18.0 + (weight - 1.0) * 5.0);
                }


                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println(timestamp);
                createTime = timestamp.toString().substring(0, 19);


                express = new Express();
                express.setWeight(weight);
                express.setFee(fee);
                express.setType(type_id);
                express.setSender(sender_id);
                express.setReceiver(receiver_id);
                express.setCreateTime(createTime);
                express.setSenderTel(senderTel);
                express.setReceiverTel(receiverTel);
                express.setReceiverAddress(receiverAddress);
                express.setSenderAddress(senderAddress);
                express.setReceiverRegionCode(receiverRegionCode);
                express.setSenderRegionCode(senderRegionCode);
                express.setStatus(0);
                express.setSenderName(senderName);
                express.setReceiverName(receiverName);


                System.out.println("express为:" + express);
                performGetExpressInBackground();
            }
        });

        // 添加返回按钮点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前活动
            }
        });

//        addressesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OrderActivity.this, AddressAddActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    private void performGetExpressInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int progress = 0; progress < 100; progress += 20) {
                        Thread.sleep(500);
                        updateProgressBar(progress);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateProgressBar(int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
            }
        });
    }

    private void updateUI() {
        OkHttpClient client1 = new OkHttpClient();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(express);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request1 = new Request.Builder().url(baseUrl + "Domain/Express/saveExpress").post(requestBody).build();
        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("HTTP Response", "Unexpected code ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    Express express = gson.fromJson(jsonResponse, Express.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            idEditText.setText(express.getId());
                            generateBarcode(express.getId());
                            createTimeEditText.setText(createTime);
                            feeEditText.setText(express.getFee().toString());
                            Toast.makeText(OrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });
        progressBar.setVisibility(View.GONE);
    }

    private void selectSenderAndReceiverAndRefresh() {

        sender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, AddressActivity.class);
                intent.putExtra("mode", 1);
                startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
            }
        });
        receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, AddressActivity.class);
                intent.putExtra("mode", 2);
                startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
            }
        });
//            refreshButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                }
//            });
    }


    private void generateBarcode(String data) {
        // Generate barcode image based on data
        // Display barcode image
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.barcode_image);
        Code128Writer code128Writer = new Code128Writer();
        try {
            BitMatrix bitMatrix = code128Writer.encode(data, BarcodeFormat.CODE_128, 800, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height + 50, Bitmap.Config.ARGB_8888); // 增加高度以容纳文字
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // 将条形码的值绘制到图像下面
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(24); // 设置文字大小
            canvas.drawText(data, 10, height + 40, paint); // 绘制文字（条形码的值）

            barcodeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            barcodeImageView.setImageBitmap(defaultBitmap); // 生成条形码失败时显示默认图片
            return;
        }

//        barcodeImageView.setImageBitmap(defaultBitmap); // 生成成功时显示默认图片
    }


    //获取regionCode与地区对应
//    private void updateCitySpinner(Context context, Spinner citySpinner) {
//        // Update city spinner based on selected province
//        List<String> cityNameList = new ArrayList<>();
//        for (int i = 0; i < cityList.size(); i++) {
//            cityNameList.add(cityList.get(i).getName());
//            City_CityRegionData.put(cityList.get(i).getName(), cityList.get(i));
//        }
//        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cityNameList);
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        citySpinner.setAdapter(cityAdapter);
//    }
//
//    private void updateDistrictSpinner(Context context, Spinner districtSpinner) {
//        // Update district spinner based on selected city
//        List<String> districtNameList = new ArrayList<>();
//        for (int i = 0; i < districtList.size(); i++) {
//            districtNameList.add(districtList.get(i).getName());
//            District_RegionCode.put(districtList.get(i).getName(), districtList.get(i).getId());
//        }
//        System.out.println(District_RegionCode);
//        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, districtNameList);
//        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        districtSpinner.setAdapter(districtAdapter);
//    }
//
//    private void loadJsonDataToSpinner() {
//        // Load region data from JSON file
//        // Populate province spinner
//        String json = loadJson();
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<RegionData>>() {
//        }.getType();
//        provinceList = gson.fromJson(json, listType);
//    }

    public String loadJson() {
        // Load JSON data from assets
        // Return JSON string
        AssetManager assetManager = getApplicationContext().getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = assetManager.open("region.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            return json;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("1111111111");
        if (data.getStringExtra("mode").equals("1")) {
            senderName = data.getStringExtra("senderName");
            senderTel = data.getStringExtra("senderTel");
            senderAddress = data.getStringExtra("senderAddress");
            senderRegionCode = data.getStringExtra("senderRegionCode");
            senderTextViewProfile.setText(senderName + " " + senderTel);
        }
        if (data.getStringExtra("mode").equals("2")) {
            receiverName = data.getStringExtra("receiverName");
            receiverTel = data.getStringExtra("receiverTel");
            receiverAddress = data.getStringExtra("receiverAddress");
            receiverRegionCode = data.getStringExtra("receiverRegionCode");
            receiverTextViewProfile.setText(receiverName + " " + receiverTel);
        }
    }

}
