package com.example.alertdialog.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class GetJsonDataUtils {
    public String getJson() throws IOException {
        String path = "/sdcard/data.json";
        File file = new File(path.trim());
        FileReader fileReader = new FileReader(file);
        Reader reader = new InputStreamReader(new FileInputStream(file), "Utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        return sb.toString();
    }

}


