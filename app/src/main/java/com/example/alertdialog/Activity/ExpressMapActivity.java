package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;
import static com.xuexiang.xui.XUI.getContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.alertdialog.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.alertdialog.pojo.Express;
import com.example.alertdialog.pojo.Transhistory;
import com.example.alertdialog.util.JsonUtils;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExpressMapActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //    private ImageView backImageView;
//    private TextView TitleTextView;
    private String expressId;
    private Express mCurrentExpress = new Express();
    private List<Transhistory> transhistoryList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NoTitle);
        setContentView(R.layout.express_map_activity);
        intdata();
        initMap();
//        backImageView = findViewById(R.id.back_menu);
//        TitleTextView = findViewById(R.id.webview_title);

    }

    private void intdata() {
        Intent intent = getIntent();
        expressId = intent.getStringExtra("expressId");
        try {
            OkHttpClient client = new OkHttpClient();
            String customerUrl = "http://" + ip + ":8080/REST/Domain/Express/getExpress/" + expressId;
            System.out.println("\nyyq123\nexpressId:\n" + expressId);
            final Request request = new Request.Builder().url(customerUrl).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();
            System.out.println("\nExpress_body:" + content);
            //这个很关键，不然会因为Express有些属性没有而崩掉
            mCurrentExpress = JsonUtils.fromJson(content, new TypeToken<Express>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            OkHttpClient client = new OkHttpClient();
            String transhistory_Url = "http://" + ip + ":8080/REST/Domain/Transhistory/getTranshistoryListByEid/" + expressId;
            System.out.println("\nyyq123\nexpressId:\n" + expressId);
            final Request request = new Request.Builder().url(transhistory_Url).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            String content = response.body().string();
            System.out.println("\nTranshistoryList_body:" + content);
            //这个很关键，不然会因为Express有些属性没有而崩掉
            transhistoryList = JsonUtils.fromJson(content, new TypeToken<List<Transhistory>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initMap() {


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        List<LatLng> points = new ArrayList<>();
        if (transhistoryList == null) {
            /*
            应该是快件刚下单
             */
            XToast.error(getContext(), "快件历史获取失败！").show();
            return;
        } else if (transhistoryList.size() == 1) {
            Transhistory transhistory = transhistoryList.get(0);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_start);
            OverlayOptions option = new MarkerOptions()
                    .position(new LatLng(transhistory.getLat(), transhistory.getLon()))
                    .icon(bitmap);
            mBaiduMap.addOverlay(option);
            points.add(new LatLng(transhistory.getLat(), transhistory.getLon()));
            System.out.println("\nyyq\nyyq\n+point:\n" + points.toString());
        } else {
            for (Transhistory transhistory : transhistoryList) {
                System.out.println("\nyyq\nyyq\nTrans_history:\n" + transhistory.toString());
                if (transhistory.getType() == Transhistory.TYPE.START) {
                    System.out.println("\nyyq\nyyq\nSTART_Trans_history:\n" + transhistory.toString());
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_start);
                    OverlayOptions option = new MarkerOptions()
                            .position(new LatLng(transhistory.getLat(), transhistory.getLon()))
                            .icon(bitmap);
                    mBaiduMap.addOverlay(option);

                } else if (transhistory.getType() == Transhistory.TYPE.END) {
                    System.out.println("\nyyq\nyyq\nEND_Trans_history:\n" + transhistory.toString());
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_end);
                    OverlayOptions option = new MarkerOptions()
                            .position(new LatLng(transhistory.getLat(), transhistory.getLon()))
                            .icon(bitmap);
                    mBaiduMap.addOverlay(option);
                } else if (transhistory.getType() == Transhistory.TYPE.TRANSFER) {
                    System.out.println("\nyyq\nyyq\nTRANSFER_history:\n" + transhistory.toString());
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_mark);
                    OverlayOptions option = new MarkerOptions()
                            .position(new LatLng(transhistory.getLat(), transhistory.getLon()))
                            .icon(bitmap);
                    mBaiduMap.addOverlay(option);
                }
                points.add(new LatLng(transhistory.getLat(), transhistory.getLon()));
                System.out.println("\nyyq\nyyq\n+point:\n" + points.toString());
            }
            //设置折线的属性
            OverlayOptions mOverlayOptions = new PolylineOptions()
                    .width(10)
                    .color(0xAAFF0000)
                    .points(points);
            //在地图上绘制折线
            mBaiduMap.addOverlay(mOverlayOptions);
        }
        //将地图视图的中心移动到历史轨迹的最后一个点，并进行缩放。
        Transhistory transhistory = transhistoryList.get(transhistoryList.size() - 1);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(new LatLng(transhistory.getLat(), transhistory.getLon()), 17);
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(new LatLng(39.779675,113.541423 ), 17);
        mBaiduMap.animateMapStatus(update);
    }


}


