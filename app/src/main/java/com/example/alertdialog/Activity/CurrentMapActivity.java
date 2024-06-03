package com.example.alertdialog.Activity;

import static com.example.alertdialog.Activity.MainActivity.ip;
import static com.xuexiang.xui.XUI.getContext;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.alertdialog.R;
import com.example.alertdialog.Service.BackService;
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.pojo.Express;
import com.example.alertdialog.pojo.Transhistory;
import com.example.alertdialog.util.JsonUtils;
import com.example.alertdialog.util.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CurrentMapActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Intent intent;
    PreferencesUtil preferencesUtil;
    public String CustomerTel;
    public String ExpressId;
    private List<Transhistory> transhistoryList = new ArrayList<>();
    private List<LatLng> points = new ArrayList<>();
    private TextView textView;
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    public double Latitude, Longitude, lastLatitude = 0, lastLongitude = 0;
    private SensorManager mSensorManager;
    private float mCurrentDirection;
    private float mCurrentAccracy;
    public boolean isFirst=true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.example.alertdialog.NEW_MESSAGE".equals(action)) {
                String message = intent.getStringExtra("message");
                // 处理接收到的消息
                Log.d("CurrentMapActivity", "Received message: " + message);
                System.out.println("yyq\nmessage:"+message);
                try {
                    mBaiduMap.clear();
                    // 解析字符串，提取对象属性值
                    int id = Integer.parseInt(message.substring(message.indexOf("id=") + 3, message.indexOf(",")));
                    String eid = message.substring(message.indexOf("eid='") + 5, message.indexOf("'", message.indexOf("eid='") + 5));
                    double lat = Double.parseDouble(message.substring(message.indexOf("lat=") + 4, message.indexOf(",", message.indexOf("lat=") + 4)));
                    double lon = Double.parseDouble(message.substring(message.indexOf("lon=") + 4, message.indexOf(",", message.indexOf("lon=") + 4)));
                    // 创建对象
                    Transhistory transhistory = new Transhistory(id, eid, lat, lon);
                    System.out.println("\nyyq_transhistory"+transhistory.toString());
//                    transhistoryList.add(transhistory);
//                    for (int i = 0; i < transhistoryList.size(); i++) {
                        points.add(new LatLng(lat, lon));
//                    }
                    if (points.size()>=2){
                        //设置折线的属性
                        OverlayOptions mOverlayOptions = new PolylineOptions()
                                .width(10)
                                .color(0xAAFF0000)
                                .points(points);
                        //在地图上绘制折线
                        mBaiduMap.addOverlay(mOverlayOptions);
                    }
                    StringBuffer sb = new StringBuffer(256);
                    sb.append("latitude:  " + lat + "\n");
                    sb.append("longitude: " + lon);
                    textView.setText("快递员位置：\n"+sb.toString() + "\n" + points.size());
//                    transhistory = transhistoryList.get(transhistoryList.size() - 1);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_mark);
                    OverlayOptions option = new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .icon(bitmap);
                    mBaiduMap.addOverlay(option);
                    if (isFirst){
                        //将地图视图的中心移动到历史轨迹的最后一个点，并进行缩放。
                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(new LatLng(transhistory.getLat(), transhistory.getLon()), 22);
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(new LatLng(39.779675,113.541423 ), 17);
                        mBaiduMap.animateMapStatus(update);
                        isFirst=false;
                    }



                } catch (Exception e) {
                    Log.e("CurrentMapActivity", "Error parsing message: " + message, e);
                }


                // 在这里你可以更新地图或做其他操作


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NoTitle);
        setContentView(R.layout.activity_current_map);

        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.example.alertdialog.NEW_MESSAGE");
        registerReceiver(receiver, filter);
        preferencesUtil = PreferencesUtil.getInstance(getApplicationContext());
        CustomerTel = preferencesUtil.getString("telCode");
        Intent intent1 = getIntent();
        ExpressId = intent1.getStringExtra("expressId");
        initMap();
        intent = new Intent(CurrentMapActivity.this, BackService.class);
        intent.putExtra("telCode", CustomerTel);
        intent.putExtra("expressId", ExpressId);
        startService(intent);
    }

    private void initMap() {


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);

//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(new LatLng(39.779675,113.541423 ), 17);
        textView = findViewById(R.id.txtPosition);
//
//
//        try {
//            mLocationClient = new LocationClient(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mOption = new LocationClientOption();
//
//        /* 设置选项 */
//
//        mOption.setOpenGps(true);
//        mOption.setCoorType("bd09ll");
//        mOption.setScanSpan(1000);
//        mOption.setOpenGnss(true);
//        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        //mOption.setLocationNotify(true);
//
//
//        /* 本地取址Client 端设置 Option选项 */
//        mLocationClient.setLocOption(mOption);
//        /* 设置监听器，监听服务器发送过来的地址信息 */
//        mLocationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                if (bdLocation == null)
//                    return;
//                StringBuffer sb = new StringBuffer(256);
//                mCurrentAccracy = bdLocation.getRadius();
//                /* 获取经纬度 */
//                Latitude = bdLocation.getLatitude();
//                Longitude = bdLocation.getLongitude();
//                System.out.println("yyq\n当前扫描经纬：" + Latitude + "  " + Longitude);
//                sb.append("latitude:  " + Latitude + "\n");
//                sb.append("longitude: " + Longitude);
//                if (points.size() == 0) {
//                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), 22);
//                    mBaiduMap.animateMapStatus(update);
//                }
////                if ((abs(Latitude - lastLatitude) > 0.0001 && abs(Longitude - lastLongitude) < 0.0001)||(points.size()==0)){
////                    if (Latitude-lastLatitude!=0||Longitude - lastLongitude!=0){
//                points.add(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
//                System.out.println("yyq\n添加坐标：" + Latitude + "  " + Longitude);
//                Toast.makeText(getApplicationContext(), "\n" + points.size(), Toast.LENGTH_SHORT).show();
//                lastLatitude = Latitude;
//                lastLongitude = Longitude;
//                MyLocationData myLocationData = new MyLocationData.Builder()
//                        .accuracy(mCurrentAccracy)
//                        // 此处设置开发者获取到的方向信息，顺时针0-360
//                        .direction(mCurrentDirection)
//                        .latitude(Latitude)
//                        .longitude(Longitude).build();
//                // 设置定位图层数据
//                mBaiduMap.setMyLocationData(myLocationData);
////                    }
////
////                }
//                if (points.size() >= 2) {
//                    OverlayOptions mOverlayOptions = new PolylineOptions()
//                            .width(10)
//                            .color(0xAAFF0000)
//                            .points(points);
//                    //在地图上绘制折线
//                    mBaiduMap.addOverlay(mOverlayOptions);
//                    Transhistory transhistory = new Transhistory(ExpressId, bdLocation.getLatitude(), bdLocation.getLongitude());
////                    System.out.println("\nyyq_transhistory"+transhistory.toString());
//                    transhistoryList.add(transhistory);
//                    //new SaveworkTask(transhistoryList).doInBackground();
//
//                }
//
//                textView.setText(sb.toString() + "\n" + points.size());
//
//
//                //mLocationClient.stop();
//            }
//        });
//        if (mLocationClient == null)
//            return;
//        mLocationClient.start();
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        //        // 获取传感器管理服务
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
////        // 为系统的方向传感器注册监听器
//        mSensorManager.registerListener(new MySensorEventListener(), mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//                SensorManager.SENSOR_DELAY_UI);
//

    }
    public class MySensorEventListener implements SensorEventListener {
        private double lastX;

        /**
         * 传感器方向信息回调
         */
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double x = sensorEvent.values[SensorManager.DATA_X];
            if (abs(x - lastX) > 1.0) {
                mCurrentDirection = (float) x;
                // 构造定位图层数据
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(mCurrentAccracy)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mCurrentDirection)
                        .latitude(Latitude)
                        .longitude(Longitude).build();
                // 设置定位图层数据
                mBaiduMap.setMyLocationData(myLocationData);
            }
            lastX = x;
//            MyTranshistoryAdapter myTranshistoryAdapter = new MyTranshistoryAdapter();
//            myTranshistoryAdapter.notifyDataSetChanged();
            //Toast.makeText(getApplicationContext(), "GPS发生变化,lat:" + Latitude + "lon:" + Longitude, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;

        // 注销广播接收器
        //unregisterReceiver(receiver);
    }

    public class SaveworkTask extends AsyncTask<Void, Void, Void> {
        List<Transhistory> transhistoryList;

        public SaveworkTask(List<Transhistory> transhistoryList) {
            super();
            this.transhistoryList = transhistoryList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // 在后台执行网络请求
            try {
                OkHttpClient client = new OkHttpClient();
                String Url = "http://" + ip + ":8080/REST/Domain/Transhistory/saveTranshistoryList";
                String json = JsonUtils.toJson(transhistoryList, true);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
                System.out.println(Url);
                final Request request = new Request.Builder().url(Url).post(requestBody).build();
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
            System.out.println("123456");
        }
    }

}

