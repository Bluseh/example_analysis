package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.LoginActivity.customerId;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alertdialog.R;
import com.example.alertdialog.util.JsonReadUtil;
import com.example.alertdialog.util.JsonUtils;
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener;
import com.lljjcoder.bean.CustomCityData;
import com.lljjcoder.citywheel.CustomConfig;
import com.lljjcoder.style.citycustome.CustomCityPicker;

import com.google.gson.reflect.TypeToken;

import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    String pickedRegion;
    String pickedRegionid;
    private List<CustomCityData> allRegionList;
    private CustomCityPicker regionPicker = new CustomCityPicker(this);
    private CustomConfig regionConfig;

    private EditText editTextUsername, editTextPhone, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister, buttonSelect;
    private static int SPLASH_DISPLAY_LENGHT = 1500;
    public static boolean isRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        // 初始化控件
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSelect = findViewById(R.id.selectButton);
        buttonRegister = findViewById(R.id.buttonRegister);
        allRegionList = JsonUtils.fromJson(JsonReadUtil.readJson("allRegionAndTransNodes.json", this), new TypeToken<List<CustomCityData>>() {
        });
        regionConfig = new CustomConfig.Builder()
                .title("选择城市")
                .visibleItemsCount(5)
                .setCityWheelType(CustomConfig.WheelType.PRO_CITY_DIS)
                .setCustomItemLayout(android.R.layout.simple_list_item_1)
                .setCustomItemTextViewId(android.R.id.text1)
                .setCityData(allRegionList)//设置数据源
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .build();
        regionPicker.setCustomConfig(regionConfig);

        // 注册按钮点击事件
        // TODO: 注册提醒 以及 加盐？
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的信息
                String name = editTextUsername.getText().toString().trim();
                String telCode = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String address = pickedRegion;
                String regionCode = pickedRegionid.substring(0, 6);
                System.out.println(regionCode);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = null;

                        if (isRegistered) {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "存在相同手机号用户！", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, SPLASH_DISPLAY_LENGHT);
                // 检查输入是否合法
                if (name.isEmpty() || telCode.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 在此处执行注册逻辑，可以调用网络请求方法进行用户注册
                RegisterTask registerTask = new RegisterTask(name, telCode, address, password, regionCode);
                registerTask.execute();
                //Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                // 注册成功后可以跳转到其他页面，比如登录页面
                //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //startActivity(intent);

                // 结束当前注册页面
                //finish();
            }
        });

        //注册地址选择事件
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regionPicker.setOnCustomCityPickerItemClickListener(new OnCustomCityPickerItemClickListener() {
                    @Override
                    public void onSelected(CustomCityData province, CustomCityData city, CustomCityData district) {
                        if (province != null && city != null && district != null) {
                            pickedRegion = province.getName() + city.getName() + district.getName();
                            try {
                                pickedRegionid = district.getId();
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("结果出错！");
                        }
                    }
                });
                regionPicker.showCityPicker();
            }
        });

    }

}
