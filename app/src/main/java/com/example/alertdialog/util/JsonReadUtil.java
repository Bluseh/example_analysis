package com.example.alertdialog.util;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * created by zzy
 * ���ڶ�ȡjson�ļ�
 */
public class JsonReadUtil {
    public static String readJson(String fileName, Activity context){
        AssetManager assetManager = context.getApplicationContext().getAssets();
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb =  new StringBuffer();
        try{
            inputStream = assetManager.open(fileName);
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);

            sb.append(br.readLine());
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
