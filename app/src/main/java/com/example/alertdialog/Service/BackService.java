package com.example.alertdialog.Service;

import static com.example.alertdialog.Activity.MainActivity.ip;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.alertdialog.Activity.MainActivity;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class BackService extends Service {

    public Application application;

    public Context context;
    public String CustomerTel;
    public String ExpressId;
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 15 * 1000;//每隔15秒进行一次对长连接的心跳检测
    private String WEBSOCKET_HOST_AND_PORT;
    private WebSocket mWebSocket;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取传递的参数
        if (intent != null) {
            CustomerTel = intent.getStringExtra("telCode");
            ExpressId = intent.getStringExtra("expressId");
            WEBSOCKET_HOST_AND_PORT = "ws://" + ip + ":8080/REST/playBack/"+CustomerTel;//可替换为自己的主机名和端口号
            System.out.println("yyq\nyyq\n"+WEBSOCKET_HOST_AND_PORT);
            System.out.println("yyq\nyyq\n"+ExpressId);
            // 使用传递的参数
        }
        // 执行其他后台任务
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
        new InitSocketThread().start();
        //application= BaseApplication.getApplication();//这个是application，需要在功能清单里面的--android:name=".main.app.TzApplication"
        //context=BaseApplication.getApplication();
        Log.e("TAG","onCreate------------*************-------------");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebSocket != null){
            mWebSocket.close(1000,null);
        }
    }




    public class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 初始化socket
        private void initSocket() throws UnknownHostException, IOException {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
            Request request = new Request.Builder().url(WEBSOCKET_HOST_AND_PORT).build();
            client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {//开启长连接成功的回调
                    super.onOpen(webSocket, response);
                    mWebSocket = webSocket;
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {//接收消息的回调
                    super.onMessage(webSocket, text);
                    //收到服务器端传过来的消息text
                    Log.e("TAG", "收到来自后台的信息-------------" + text);
                    System.out.println("yyq\nyyq\ntext:\n"+text);
                    // 发送广播
                    Intent intent = new Intent("com.example.alertdialog.NEW_MESSAGE");
                    intent.putExtra("message", text);
                    sendBroadcast(intent);
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    super.onMessage(webSocket, bytes);
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    super.onClosing(webSocket, code, reason);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    super.onClosed(webSocket, code, reason);
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {//长连接连接失败的回调
                    super.onFailure(webSocket, t, response);
                }
            });
            client.dispatcher().executorService().shutdown();
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        }

        private long sendTime = 0L;
        // 发送心跳包
        private Handler mHandler = new Handler();
        private Runnable heartBeatRunnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                    boolean isSuccess = mWebSocket.send("");//发送一个空消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                    if (!isSuccess) {//长连接已断开
                        mHandler.removeCallbacks(heartBeatRunnable);
                        mWebSocket.cancel();//取消掉以前的长连接
                        new InitSocketThread().start();//创建一个新的连接
                    } else {//长连接处于连接状态
                        //长连接处于连接状态---
                        Log.e("TAG", "发送心跳包-------------长连接处于连接状态");
                    }

                    sendTime = System.currentTimeMillis();
                }
                mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
            }
        };


    }

}

