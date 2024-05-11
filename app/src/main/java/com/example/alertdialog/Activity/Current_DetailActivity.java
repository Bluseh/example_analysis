package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_CREATED;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_DELIVERING;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_PICKED;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_SIGNED;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_SORTED;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_TRANSPORTING;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_UNSORTED;
import static com.example.alertdialog.pojo.Express.STATUS.STATUS_WAIT_DELIVER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alertdialog.R;
import com.example.alertdialog.adapters.TraceAdapter;
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.pojo.Express;
import com.example.alertdialog.pojo.ExpressTrack;
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

public class Current_DetailActivity extends AppCompatActivity {
    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView picImageView;
    private TextView statusTextView;
    private TextView companyTextView;
    private TextView expressidTextView;
    private RecyclerView traceRv; //物流追踪列表
    private TraceAdapter mAdapter;
    private String customerId = LoginActivity.customerId;
    PreferencesUtil preferencesUtil;
    String expressId = "";
    private Express express;
    List<ExpressTrack> expresstrackList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NoTitle);
        setContentView(R.layout.activity_current_detail);

        backImageView = findViewById(R.id.back_menu);
        titleTextView = findViewById(R.id.webview_title);
        picImageView = findViewById(R.id.main_pic_iv);
        statusTextView = findViewById(R.id.tv_express_status);
        companyTextView = findViewById(R.id.tv_express_company);
        expressidTextView = findViewById(R.id.tv_express_id);
        Intent intent = getIntent();
        expressId = intent.getStringExtra("expressId");
        System.out.println("\nexpressId753951:\n"+expressId);
        initRecyclerView();
//        if (expressId=="") {
//            //XToast.error(this, "未获得快递单号").show();
//        } else {
//
//        }


    }
    private void initRecyclerView() {
        try {
            OkHttpClient client = new OkHttpClient();
            String customerUrl = "http://"+ip+":8080/REST/Domain/Express/getExpress/"+expressId;
            System.out.println("\nyyq123\nexpressId:\n"+expressId);
            final Request request = new Request.Builder().url(customerUrl).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();
            System.out.println("\nbody:"+content);
            Gson gson = new Gson();
            Type expressType = new TypeToken<Express>() {}.getType();
            express = gson.fromJson(content, expressType);
            //System.out.println("\nyyq\n\n"+gson.fromJson(content, expressType));
            //express = gson.fromJson(content, customerListType);
            //System.out.println("\nyyq\nexpress:\n"+express.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ivBack = findViewById(R.id.iv_back);
//        tvExpressId = findViewById(R.id.tv_express_id);
//        tvExpressStatus = findViewById(R.id.tv_express_status);
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ExpressDetailActivity.this.finish();
//            }
//        });
        expressidTextView.setText("快件单号:" +express.getId());
        String status = "快件状态:";
        switch (express.getStatus()) {
            case STATUS_CREATED:
                status += "已下单";
                break;
            case STATUS_PICKED:
                status += "已揽收";
                break;
            case STATUS_SORTED:
            case STATUS_UNSORTED:
            case STATUS_TRANSPORTING:
                status += "运输中";
                break;
            case STATUS_WAIT_DELIVER:
                status += "待派送";
                break;
            case STATUS_DELIVERING:
                status += "派送中";
                break;
            case STATUS_SIGNED:
                status += "已签收";
                break;
            default:
                status += "未知状态";
                break;
        }
        statusTextView.setText(status);
        traceRv = (RecyclerView) findViewById(R.id.traceRv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        traceRv.setLayoutManager(layoutManager);
        mAdapter = new TraceAdapter(this);
        traceRv.setAdapter(mAdapter);
        try {
            OkHttpClient client = new OkHttpClient();
            String customerUrl = "http://"+ip+":8080/REST/Domain/Express/getExpressTrackList/"+expressId;
            System.out.println("\nyyq\nexpressId:\n"+expressId);
            final Request request = new Request.Builder().url(customerUrl).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();

            System.out.println("\nyyq\nExpressTrackList:"+content);
            Gson gson = new Gson();
            Type expressTrackListType = new TypeToken<List<ExpressTrack>>() {}.getType();
            expresstrackList = gson.fromJson(content, expressTrackListType);
            //System.out.println("\nyyq\n\n"+gson.fromJson(content, expressTrackListType));
            //express = gson.fromJson(content, customerListType);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        ExpressTrackListLoader loader = new ExpressTrackListLoader(mAdapter, this);
//        loader.getExpressTrackList(mCurrentExpress.getId());
        mAdapter.setData(expresstrackList);
        mAdapter.notifyDataSetChanged();
    }
}