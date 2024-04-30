package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.LoginActivity.customerId;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.example.alertdialog.R;


public class SplashActivity extends Activity {

    private static int SPLASH_DISPLAY_LENGHT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        Context context = this.getApplicationContext();
        Intent intent = getIntent();
        Integer mode = intent.getIntExtra("mode", 0);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = null;

                if (customerId.isEmpty() && mode == 0) {
                    Toast.makeText(context, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else if (mode == 1) {
                    intent = new Intent(SplashActivity.this, AddressActivity.class);
                    intent.putExtra("mode", 1);
                } else if (mode == 2) {
                    intent = new Intent(SplashActivity.this, AddressActivity.class);
                    intent.putExtra("mode", 2);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
