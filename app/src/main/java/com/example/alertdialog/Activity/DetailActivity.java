package com.example.alertdialog.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.Express;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// DetailActivity.java
public class DetailActivity extends AppCompatActivity {
    private ImageView barcodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv1 = findViewById(R.id.textViewRecipient);
        TextView tv2 = findViewById(R.id.textViewSender);
        TextView tv3 = findViewById(R.id.textViewContent);
        TextView tv4 = findViewById(R.id.textViewAddress);
        TextView tv5 = findViewById(R.id.textViewPhoneNumber);
        Button evaluate = findViewById(R.id.evaluate);
        Button sign = findViewById(R.id.sign);
        barcodeImageView = findViewById(R.id.barcodeImageView);
        sign.setEnabled(false);
        sign.setVisibility(View.INVISIBLE);
        evaluate.setEnabled(false);
        evaluate.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", -1);
        if (mode == 1) {
            String name = intent.getStringExtra("receiver");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            if (name != null && type != -1 && addr != null && tel!= null) {
                tv1.setText("收件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("收件人地址：" + addr);
                tv5.setText("收件人电话：" + tel);
            }
        } else if (mode == 2) {

            //新增开始11111111111111111111111
            String name = intent.getStringExtra("sender");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            String expressId = intent.getStringExtra("expressId");
            if (name != null && type != -1 && addr != null && tel!= null) {
                tv2.setText("寄件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("寄件人地址：" + addr);
                tv5.setText("寄件人电话：" + tel);
                sign.setEnabled(true);
                sign.setVisibility(View.VISIBLE);
                sign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailActivity.this, "签收成功", Toast.LENGTH_SHORT).show();
                        generateBarcode(expressId);
                    }
                });
            }

        } else if (mode == 3) {
            evaluate.setEnabled(true);
            evaluate.setVisibility(View.VISIBLE);
            String name = intent.getStringExtra("sender");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            if (name != null && type != -1 && addr != null && tel!= null) {
                tv2.setText("寄件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("寄件人地址：" + addr);
                tv5.setText("寄件人电话：" + tel);
            }
//新增结束111111111111111111111111111111111111111111111
        } else {
            //错误处理
        }
    }
    private void generateBarcode(String data) {
        // Generate barcode image based on data
        // Display barcode image
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.barcode_image);
        Code128Writer code128Writer = new Code128Writer();
        try {
            BitMatrix bitMatrix = code128Writer.encode(data, BarcodeFormat.CODE_128, 800, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height + 50, Bitmap.Config.ARGB_8888); // 增加高度以容纳文字
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // 将条形码的值绘制到图像下面
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(24); // 设置文字大小
            canvas.drawText(data, 10, height + 40, paint); // 绘制文字（条形码的值）

            barcodeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            barcodeImageView.setImageBitmap(defaultBitmap); // 生成条形码失败时显示默认图片
            return;
        }

//        barcodeImageView.setImageBitmap(defaultBitmap); // 生成成功时显示默认图片
    }
}