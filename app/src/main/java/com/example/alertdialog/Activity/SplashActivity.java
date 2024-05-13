package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.LoginActivity.customerId;
import static com.example.alertdialog.Activity.MainActivity.ip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.Address;
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.util.PreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends Activity {

    private static int SPLASH_DISPLAY_LENGHT = 1500;
    public static List<Address> splashedAddressesList;
    private ImageView imageView;
    PreferencesUtil preferencesUtil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesUtil = PreferencesUtil.getInstance(getApplicationContext());
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.iv_image);
        Context context = this.getApplicationContext();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                String tel = preferencesUtil.getString("telCode");
                String pw = preferencesUtil.getString("password");
                //if (tel.equals("")&&pw.equals("")) {
                    //Toast.makeText(context, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(SplashActivity.this, LoginActivity.class);
                //} else {
                    //intent = new Intent(SplashActivity.this, MainActivity.class);
                //}
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }


    public static class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // 在后台执行网络请求
            try {
                OkHttpClient client = new OkHttpClient();
                String expressUrl = "http://"+ip+":8080/REST/Misc/AddressList/getAddressListByCustomerId/" + customerId;
                System.out.println("\nyyq\nawdsa\n12345678"+expressUrl);
                final Request request = new Request.Builder().url(expressUrl).build();
                Call call = client.newCall(request);
                Response response = call.execute();
                String content = response.body().string();
                System.out.println(content);
                Gson gson = new Gson();
                Type addressListType = new TypeToken<List<Address>>() {
                }.getType();
                splashedAddressesList = gson.fromJson(content, addressListType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 网络请求完成后的处理逻辑
            System.out.println(splashedAddressesList);
        }
    }

}
