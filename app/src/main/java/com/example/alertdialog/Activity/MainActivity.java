package com.example.alertdialog.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.StateSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertdialog.R;
import com.example.alertdialog.adapters.DrawerAdapter;
import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.util.ClickUtils;
import com.example.alertdialog.util.DrawerItem;
import com.example.alertdialog.util.PreferencesUtil;
import com.example.alertdialog.util.SimpleItem;
import com.example.alertdialog.util.SpaceItem;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener,View.OnTouchListener,ClickUtils.OnClick2ExitListener{

    private String customerId = LoginActivity.customerId;
    private Customer customer;
    PreferencesUtil preferencesUtil;
    public static final String ip = "10.10.11.226";
    //退出登录
    private static final int POS_LOGOUT = 5;
    private SlidingRootNav mSlidingRootNav;
    private LinearLayout mLLMenu;
    private LinearLayout navHeader;
    private TextView tvAvatar;
    private DrawerAdapter mAdapter;
    private Drawable[] mMenuIcons;
    private String[] mMenuTitles;
    GestureDetector mGestureDetector;
    private LinearLayout holePages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

//        holePages.setOnTouchListener(this);
//        holePages.setLongClickable(true);//必需设置这为true 否则也监听不到手势
        // 找到 "包裹下单" 的LinearLayout
        LinearLayout actionAddressList = findViewById(R.id.action_addressList);
        LinearLayout actionOrderLayout = findViewById(R.id.action_order);
        LinearLayout actionSenderList = findViewById(R.id.action_senderList);
        LinearLayout actionReceiverList = findViewById(R.id.action_receiverList);
        LinearLayout actionSignedList = findViewById(R.id.action_signedList);
        LinearLayout actionMarkedList = findViewById(R.id.action_markedList);
        //数据
        initData();
        initSlidingMenu(savedInstanceState);
        holePages.setOnTouchListener(this);
        holePages.setLongClickable(true);
        mLLMenu.setOnTouchListener(this);
        mLLMenu.setLongClickable(true);
        mGestureDetector = new GestureDetector(this, myGestureListener);

        actionAddressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        // 给 "包裹下单" 添加点击事件
        actionOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        actionSenderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });

        actionReceiverList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
                startActivity(intent);

            }
        });
        actionSignedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignedActivity.class);
                startActivity(intent);
            }
        });
        actionMarkedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MarkedActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        holePages = findViewById(R.id.main_activity);
        //menu标题
        mMenuTitles = ResUtils.getStringArray(this, R.array.menu_titles);
        //menu图标
        mMenuIcons = ResUtils.getDrawableArray(this, R.array.menu_icons);
        preferencesUtil= PreferencesUtil.getInstance(getApplicationContext());
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withGravity(ResUtils.isRtl(this) ? SlideGravity.RIGHT : SlideGravity.LEFT)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        mLLMenu = mSlidingRootNav.getLayout().findViewById(R.id.ll_menu);
        navHeader = findViewById(R.id.nav_header);
        tvAvatar = (TextView) findViewById(R.id.tv_avatar);
        tvAvatar.setText(preferencesUtil.getString("name"));
        /*
        隐藏整个带用户的划出显示的菜单栏
         */
        ViewUtils.setVisibility(mLLMenu, false);
        /*
        初始化侧滑菜单的“我的包裹”“我的位置”我的消息“”一行空“退出登录”
         */
        mAdapter = new DrawerAdapter(Arrays.asList(
                //MYPACKAGES
                createItemFor(0),
                //MYLOCATION
                createItemFor(1),
                //MYMESSAGES
                createItemFor(2),
                //ABOUT
                createItemFor(3),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        mAdapter.setListener(this);
        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);
        /*
        将当前选中的菜单项索引设置为-1，表示没有选中任何菜单项。
         */
        mAdapter.setSelected(-1);
        /*
        设置侧边栏菜单解锁，允许用户滑动手势打开或关闭菜单。
         */
        mSlidingRootNav.setMenuLocked(false);

        /*
        为侧边栏布局添加拖动状态监听器，以便在拖动开始和结束时进行相应的操作。
         */
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {

            /*
            当用户开始拖动侧边栏时调用的方法。在这里，将侧边栏菜单布局（mLLMenu）设置为可见（true）。
             */
            @Override
            public void onDragStart() {
                ViewUtils.setVisibility(mLLMenu, true);
            }

            /*
            当用户停止拖动侧边栏时调用的方法。参数isMenuOpened表示侧边栏是否打开。
            如果侧边栏打开，则将菜单布局设置为可见；否则，保持不可见状态。
             */
            @Override
            public void onDragEnd(boolean isMenuOpened) {
                if(!isMenuOpened){
                    ViewUtils.setVisibility(mLLMenu, false);
                }


            }
        });


    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(mMenuIcons[position], mMenuTitles[position])
                .withIconTint(Color.BLACK)  // 设置图标的颜色为纯黑色
                .withTextTint(Color.BLACK)  // 设置文本的颜色为纯黑色
                .withSelectedIconTint(Color.BLACK)  // 设置选中图标的颜色为纯黑色
                .withSelectedTextTint(Color.BLACK);  // 设置选中文本的颜色为纯黑色
    }

    public boolean isMenuOpen() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuOpened();
        }
        return false;
    }
    public void closeMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.closeMenu();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMenuOpen()) {
                closeMenu();
            } else {
                ClickUtils.exitBy2Click(this, 2000, this);
            }
        }
        return true;
    }




    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case 0:
//                if (mTabLayout != null) {
//                    //选择“我的包裹”自动跳到转运任务
//                    TabLayout.Tab tab = mTabLayout.getTabAt(3);
//                    if (tab != null) {
//                        tab.select();
//                    }
//                }
                XToast.info(this, "我的包裹功能即将上线").show();
                break;
            case 1:
//                Intent myLocationIntent = new Intent(this, MyLocationActivity.class);
//                startActivity(myLocationIntent);
                XToast.info(this, "我的位置功能即将上线").show();
                break;
            case 2:
                XToast.info(this, "我的消息功能即将上线").show();
                break;
            case 3:
                XToast.info(this, "关于功能即将上线").show();
                break;
            case POS_LOGOUT:
                AlertDialog exitDialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferencesUtil.putString("id","");
                                preferencesUtil.putString("name","");
                                preferencesUtil.putString("address","");
                                preferencesUtil.putString("telCode","");
                                preferencesUtil.putString("regionCode","");
                                preferencesUtil.putString("password","");
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                finish();
                                MainActivity.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 在取消按钮点击事件中处理逻辑
                                dialog.dismiss(); // 关闭对话框
                            }
                        })
                        .show();
                break;
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }
    /*
    如果检测到向右滑动（即 x 坐标的增加值大于 10），则通过日志记录向右手势
    并且侧边栏菜单处于关闭状态，，并打开侧边栏菜单（带用户的菜单）。
     */
    GestureDetector.SimpleOnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener(){
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX()-e1.getX();
            float y = e2.getY()-e1.getY();
            System.out.println("\n"+x);
            if(x > 10){
                Log.i("MainActivity","向右手势");
                if(mSlidingRootNav.isMenuClosed()){
                    mSlidingRootNav.openMenu();
                }
            }
            if(x<0){
                Log.i("MainActivity","向左手势");
                ViewUtils.setVisibility(mLLMenu, false);
                //mSlidingRootNav.closeMenu();
            }
            return false;
        };
    };

    @Override
    public void onRetry() {
        XToast.info(this,"再按一次退出程序").show();
    }

    @Override
    public void onExit() {
        moveTaskToBack(true);
    }

}

