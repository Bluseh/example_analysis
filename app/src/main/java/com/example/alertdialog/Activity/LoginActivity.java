package com.example.alertdialog.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone, etPassword;
    private Button btnLogin, btnRegister;
    public static String customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

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
                LoginTask loginTask = new LoginTask(telCode, password);
                loginTask.execute();
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

