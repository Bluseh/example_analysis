package com.example.alertdialog.util;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;

import com.example.alertdialog.Activity.MainActivity;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.Timer;
import java.util.TimerTask;

public final class ClickUtils {
    private static final long DEFAULT_INTERVAL_MILLIS = 1000L;
    private static long sLastClickTime;
    private static int sLastClickViewId;
    private static final int COUNTS = 5;
    private static long[] sHits = new long[5];
    private static final long DEFAULT_DURATION = 1000L;
    private static boolean sIsExit = false;

    private ClickUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isFastDoubleClick(View v) {
        return isFastDoubleClick(v, 1000L);
    }

    public static boolean isFastDoubleClick(View v, long intervalMillis) {
        long time = System.currentTimeMillis();
        int viewId = v.getId();
        long timeD = time - sLastClickTime;
        if (0L < timeD && timeD < intervalMillis && viewId == sLastClickViewId) {
            return true;
        } else {
            sLastClickTime = time;
            sLastClickViewId = viewId;
            return false;
        }
    }

    public static void doClick(OnContinuousClickListener listener) {
        doClick(1000L, listener);
    }

    public static void doClick(long duration, OnContinuousClickListener listener) {
        System.arraycopy(sHits, 1, sHits, 0, sHits.length - 1);
        sHits[sHits.length - 1] = SystemClock.uptimeMillis();
        if (sHits[0] >= SystemClock.uptimeMillis() - duration) {
            sHits = new long[5];
            if (listener != null) {
                listener.onContinuousClick();
            }
        }

    }

    public static void exitBy2Click(Context ctx) {
        exitBy2Click(ctx,2000L, (OnClick2ExitListener)null);
    }

    public static void exitBy2Click(Context ctx,long intervalMillis, OnClick2ExitListener listener) {
        if (!sIsExit) {
            sIsExit = true;
            if (listener != null) {
                listener.onRetry();
            } else {
                XToast.info(ctx,"再按一次退出程序").show();
            }

            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    ClickUtils.sIsExit = false;
                }
            }, intervalMillis);
        } else if (listener != null) {
            listener.onExit();
        } else {
            ((MainActivity)ctx).finish();
        }

    }

    public interface OnClick2ExitListener {
        void onRetry();

        void onExit();
    }

    public interface OnContinuousClickListener {
        void onContinuousClick();
    }
}

