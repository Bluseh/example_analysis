package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.LoginActivity.customerId;
import static com.example.alertdialog.Activity.MainActivity.ip;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.pojo.ExpressTrack;
import com.example.alertdialog.util.JsonUtils;
import com.example.alertdialog.util.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "LoginTask";


    private String telCode;

    private String password;
    private String state;
    private Activity context;
    private PreferencesUtil preferencesUtil;
    private Response response = null;
    private String content = "";


    public LoginTask(String telCode, String password) {
        this.telCode = telCode;
        this.password = password;
    }

    public LoginTask(Activity context, String telCode, String password) {
        this.context = context;
        this.telCode = telCode;
        this.password = password;
        this.preferencesUtil = new PreferencesUtil(context); // 初始化PreferencesUtil对象
    }

    @Override
    protected Void doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        String expressUrl = "http://" + ip + ":8080/REST/App/login?telCode=" + telCode + "&passWord=" + password;
        System.out.println(expressUrl);
        final Request request = new Request.Builder().url(expressUrl).build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
            content = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        state = response.header("state");
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        if (response != null) {
            Log.d(TAG, response.toString());
            System.out.println(state);
            System.out.println("yyqy\nyq\nresponse_body:\n" + content + "content");
            if ("verify_success".equals(state)) {
                try {
                    JSONObject jsonResponse = new JSONObject(content);
                    customerId = jsonResponse.optString("id"); // 更新 customerId 的值
                    Toast.makeText(context, "customerId已更新", Toast.LENGTH_SHORT).show();
                    System.out.println("\nyyq\n\nyyqn" + jsonResponse.optString("id"));
                    Customer customer = new Customer();
                    customer.setId(Integer.parseInt(jsonResponse.optString("id")));
                    customer.setName(jsonResponse.optString("name"));
                    customer.setAddress(jsonResponse.optString("address"));
                    customer.setTelCode(jsonResponse.optString("telCode"));
                    customer.setRegionCode(jsonResponse.optString("regionCode"));
                    customer.setPassword(jsonResponse.optString("password"));
                    preferencesUtil.putString("id", customer.getId().toString());
                    preferencesUtil.putString("name", customer.getName());
                    preferencesUtil.putString("address", customer.getAddress());
                    preferencesUtil.putString("telCode", customer.getTelCode());
                    preferencesUtil.putString("regionCode", customer.getRegionCode());
                    preferencesUtil.putString("password", customer.getPassword());
                    Log.d(TAG, "登录成功，customer : " + customer.toString());
                    Intent intent = new Intent(context, MainActivity.class);
                    context.finish();
                    context.startActivity(intent);
                } catch (JSONException e) {
                    Log.e(TAG, "解析 JSON 出错：" + e.getMessage());
                }
            } else if ("verify_fail".equals(state)) {

                // 账号已存在
                Log.d(TAG, "用户名或密码错误");
                XToast.error(context, "用户名或密码错误").show();
            } else {
                Log.d(TAG, "未知状态：" + state);
            }
        } else {
            Log.e(TAG, "Response is null");
        }
    }

}

