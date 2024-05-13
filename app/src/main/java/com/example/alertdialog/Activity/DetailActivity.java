package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// DetailActivity.java
public class DetailActivity extends AppCompatActivity {
    private ImageView barcodeImageView;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView detailImageView;
    private ImageView locationImageView;
    private int mode;
    private String ExpressId = "";
    private Context context = DetailActivity.this;
    private String comment;
    private String expressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NoTitle);
        setContentView(R.layout.activity_detail);
        backImageView = findViewById(R.id.back_menu);
        titleTextView = findViewById(R.id.webview_title);
        detailImageView = findViewById(R.id.main_detail);
        locationImageView = findViewById(R.id.main_location);

        TextView tv0 = findViewById(R.id.textViewExpressId);
        TextView tv1 = findViewById(R.id.textViewRecipient);
        TextView tv2 = findViewById(R.id.textViewSender);
        TextView tv3 = findViewById(R.id.textViewContent);
        TextView tv4 = findViewById(R.id.textViewAddress);
        TextView tv5 = findViewById(R.id.textViewPhoneNumber);

        setListener();
        TextView tv6 = findViewById(R.id.textViewComment);
        tv6.setEnabled(false);
        tv6.setVisibility(View.INVISIBLE);

        Button evaluate = findViewById(R.id.evaluate);
        Button sign = findViewById(R.id.sign);
        barcodeImageView = findViewById(R.id.barcodeImageView);
        sign.setEnabled(false);
        sign.setVisibility(View.INVISIBLE);
        evaluate.setEnabled(false);
        evaluate.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);

        try {
            ExpressId = (String) intent.getStringExtra("expressId");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (mode == 1) {
            titleTextView.setText("寄件信息");
            String name = intent.getStringExtra("receiver");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            String expressId = intent.getStringExtra("expressId");
            if (expressId != null && name != null && type != -1 && addr != null && tel != null) {
                tv0.setText("快递单号：" + expressId);
                tv1.setText("收件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("收件人地址：" + addr);
                tv5.setText("收件人电话：" + tel);
            } else {
                Toast.makeText(context, "获取信息失败！", Toast.LENGTH_SHORT).show();
            }

        } else if (mode == 2) {
            titleTextView.setText("收件信息");
            String name = intent.getStringExtra("sender");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            String expressId = intent.getStringExtra("expressId");
            if (name != null && type != -1 && addr != null && tel != null && expressId != null) {
                tv0.setText("快递单号：" + expressId);
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
            } else {
                Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
            }

        } else if (mode == 3) {

            String name = intent.getStringExtra("sender");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            expressId = intent.getStringExtra("expressId");
            if (name != null && type != -1 && addr != null && tel != null && expressId != null) {
                tv0.setText("快递单号：" + expressId);
                tv2.setText("寄件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("寄件人地址：" + addr);
                tv5.setText("寄件人电话：" + tel);
            } else {
                Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
            }
            evaluate.setEnabled(true);
            evaluate.setVisibility(View.VISIBLE);
            evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("请输入评价");
                    // 创建一个 EditText 用于用户输入评价文本
                    final EditText input = new EditText(context);
                    builder.setView(input);

                    // 点击确认按钮后触发的事件
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 获取用户输入的评价文本
                            comment = input.getText().toString();
                            new DetailActivity.NetworkTask().execute();
                            System.out.println("评价内容：" + comment);
                        }
                    });
                    // 创建并显示对话框
                    builder.create().show();
                }
            });

        }else if (mode == 4) {

            String name = intent.getStringExtra("sender");
            int type = intent.getIntExtra("type", -1);
            String addr = intent.getStringExtra("addr");
            String tel = intent.getStringExtra("tel");
            expressId = intent.getStringExtra("expressId");
            String comment = intent.getStringExtra("comment");
            if (name != null && type != -1 && addr != null && tel != null && comment != null && expressId != null) {
                tv0.setText("快递单号：" + expressId);
                tv2.setText("寄件人：" + name);
                tv3.setText("快递内容：" + String.valueOf(type));
                tv4.setText("寄件人地址：" + addr);
                tv5.setText("寄件人电话：" + tel);
                tv6.setEnabled(true);
                tv6.setVisibility(View.VISIBLE);
                tv6.setText("评价：" + comment);
            } else {
                Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
            }

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

    private void setListener() {
        detailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, Current_DetailActivity.class);
                intent.putExtra("expressId", ExpressId);
                intent.putExtra("mode", mode);
                startActivity(intent);
            }
        });
        locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ExpressMapActivity.class);
                intent.putExtra("expressId", ExpressId);
                intent.putExtra("mode", mode);
                startActivity(intent);

            }
        });
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // 在后台执行网络请求
            try {
                String encodedString = URLEncoder.encode(comment, "UTF-8");
                OkHttpClient client = new OkHttpClient();
                String expressUrl = "http://" + ip + ":8080/REST/Domain/Express/commentExpress?param=" + encodedString + "&expressId=" + expressId;
                System.out.println(expressUrl);
                String decodedString = URLDecoder.decode(encodedString, "UTF-8");
                System.out.println(decodedString);
                final Request request = new Request.Builder().url(expressUrl).build();
                Call call = client.newCall(request);
                Response response = call.execute();
                String content = response.header("state");
                System.out.println(content);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 网络请求完成后的处理逻辑
            Toast.makeText(context, "评价成功!", Toast.LENGTH_SHORT).show();
        }
    }

}