package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.alertdialog.R;
import com.example.alertdialog.adapters.AddressAdapter;
import com.example.alertdialog.beans.ProviceBean;
import com.example.alertdialog.util.AddressConverterUtils;
import com.example.alertdialog.util.GetJsonDataUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class AddressAddActivity extends AppCompatActivity {

    // SharedPreferences 文件名
    private static final String SHARED_PREFS_FILE = "my_shared_prefs";
    // 键值对中的键
    private static final String KEY_STRING1 = "saved_string1";
    private static final String KEY_STRING2 = "saved_string2";

    private Button addButton;
    private Button selectButton;
    private EditText inputAddressEt;
    private EditText inputNameEt;
    private EditText inputTelEt;
    private TextView choosedAreaTv;

    //省、市、区-列表
    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();
    private String customerId = LoginActivity.customerId;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时调用自定义函数
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        addButton = findViewById(R.id.add_button);
        selectButton = findViewById(R.id.selectButton);
        inputAddressEt = findViewById(R.id.inputAddressEt);
        inputNameEt = findViewById(R.id.inputNameEt);
        inputTelEt = findViewById(R.id.inputTelEt);
        choosedAreaTv = findViewById(R.id.choosedAreaTv);

        // 初始化三级选择
        initJsonData();

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaPicker();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newAddress = inputAddressEt.getText().toString();
                String newName = inputNameEt.getText().toString();
                String newTel = inputTelEt.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                String savedString1 = sharedPreferences.getString(KEY_STRING1, "");
                String savedString2 = sharedPreferences.getString(KEY_STRING2, "");
                if (savedString1.equals("")) {
                    Toast.makeText(AddressAddActivity.this, "还未选择当前地区！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regioncode = AddressConverterUtils.convertToAreaCode(savedString2);

                // 拼接两个字符串
                String finalString = savedString1 + newAddress;
                try {

                    String encodedString = URLEncoder.encode(finalString, "UTF-8");
                    try {
                        OkHttpClient client = new OkHttpClient();
                        String expressUrl = "http://"+ip+":8080/REST/Misc/AddressList/saveAddressByCustomerId?param=" + encodedString + "&id=" + customerId + "&regioncode=" + regioncode + "&name=" + newName + "&tel=" + newTel;
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
                Toast.makeText(AddressAddActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void areaPicker() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = options1Items.get(options1) +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(KEY_STRING1);
                editor.remove(KEY_STRING2);
                editor.putString(KEY_STRING1, str);
                editor.putString(KEY_STRING2, options3Items.get(options1).get(options2).get(options3));
                editor.apply();
                Toast.makeText(AddressAddActivity.this, str, Toast.LENGTH_SHORT).show();
                choosedAreaTv.setText("当前已选择：" + str);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    private void initJsonData() {

        String str = null;
        try {
            str = new GetJsonDataUtils().getJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<ProviceBean> list = new Gson().fromJson(str, new TypeToken<List<ProviceBean>>() {
        }.getType());
        for (ProviceBean bean : list) {
            options1Items.add(bean.getName());
            List<String> city = new ArrayList<>();
            List<List<String>> area = new ArrayList<>();
            for (ProviceBean.CityBean cityBean : bean.getCity()) {
                city.add(cityBean.getName());
                area.add(cityBean.getArea());
            }
            options2Items.add(city);
            options3Items.add(area);
        }
    }
}
