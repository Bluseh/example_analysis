package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.LoginActivity.customerId;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "LoginTask";


    private String telCode;

    private String password;
    private String state;


    public LoginTask(String telCode, String password) {
        this.telCode = telCode;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // 构建请求参数
            JSONObject requestData = new JSONObject();
            requestData.put("telCode", telCode);
            requestData.put("password", password);

            // 发起 POST 请求
            URL url = new URL("http://10.131.31.23:8080/REST/App/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 发送请求数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestData.toString().getBytes());
            outputStream.flush();

            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                state = connection.getHeaderField("state");
                System.out.println(state);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response.toString());
                JSONObject jsonResponse = new JSONObject(response.toString());
                // customerId= jsonResponse.optString("id");
//                this.logindId = jsonResponse.optInt("id");
//                System.out.println("2222222222222");
//                System.out.println(logindId);
                return response.toString();
            } else {
                Log.e(TAG, "HTTP error code: " + responseCode);
                return null;
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            Log.d(TAG, response);
            if ("verify_success".equals(state)) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    customerId = jsonResponse.optString("id"); // 更新 customerId 的值
                    Log.d(TAG, "登录成功，customerId = " + customerId);
                } catch (JSONException e) {
                    Log.e(TAG, "解析 JSON 出错：" + e.getMessage());
                }
            } else if ("verify_fail".equals(state)) {
                // 账号已存在
                Log.d(TAG, "用户名或密码错误");
            } else {
                Log.d(TAG, "未知状态：" + state);
            }
        } else {
            Log.e(TAG, "Response is null");
        }
    }
}

