package com.example.alertdialog.Activity;

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

public class RegisterTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "RegisterTask";

    private String name;
    private String telCode;
    private String address;
    private String password;
    private String state;
    private String regionCode;

    public RegisterTask(String name, String telCode, String address, String password,String regionCode) {
        this.name = name;
        this.telCode = telCode;
        this.address = address;
        this.password = password;
        this.regionCode = regionCode;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // 构建请求参数
            JSONObject requestData = new JSONObject();
            requestData.put("name", name);
            requestData.put("telCode", telCode);
            requestData.put("address", address);
            requestData.put("password", password);
            requestData.put("regionCode", regionCode);

            // 发起 POST 请求
            URL url = new URL("http://10.10.11.27:8080/REST/App/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 发送请求数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestData.toString().getBytes());
            System.out.println(requestData.toString());
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

                Log.d(TAG,response);
                if ("create_success".equals(state)) {
                    // 注册成功
                    Log.d(TAG, "注册成功");
                    RegisterActivity.isRegistered=true;

                    //跳转到登陆界面
                } else if ("create_fail".equals(state)) {
                    // 账号已存在
                    Log.d(TAG, "账号已存在");
                } else {
                    Log.d(TAG, "未知状态：" + state);
                }
            }
        else {
            Log.e(TAG, "Response is null");
        }
    }
}

