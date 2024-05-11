package com.example.alertdialog.Activity;

import static com.xuexiang.xui.XUI.getContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.Address;
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.util.PreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone, etPassword;
    private Button btnLogin, btnRegister;
    public static String customerId = "";
    PreferencesUtil preferencesUtil;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        preferencesUtil = PreferencesUtil.getInstance(getApplicationContext());

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入的电话和密码
                String telCode = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (telCode.equals("")){
                    XToast.error(getContext(),"请输入手机号！").show();
                } else if (password.equals("")) {
                    XToast.error(getContext(),"请输入密码！").show();
                }else {
                    LoginTask loginTask = new LoginTask(LoginActivity.this,telCode, password);
                    loginTask.execute();
                    Toast.makeText(LoginActivity.this, "发送请求", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            startActivity(intent);
                        }
                    }, 1500);
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        preLogin();
    }
    private void preLogin(){
        customer=new Customer();
        String tel = preferencesUtil.getString("telCode");
        String pw = preferencesUtil.getString("password");
        System.out.println("tel\n\npw\n"+tel+pw);

    }


}

