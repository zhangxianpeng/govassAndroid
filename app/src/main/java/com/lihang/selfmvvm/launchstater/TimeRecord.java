package com.lihang.selfmvvm.launchstater;

import android.util.Log;

/**
 * 这里是为了测试启动速度优化
 * 用来记录耗时时间
 */
public class TimeRecord {
    private static long time;

    public static void startRecord() {
        time = System.currentTimeMillis();
    }

    public static void endRecord(String msg) {
        long stayTime = System.currentTimeMillis() - time;
        Log.e("启动速度优化", msg + "====" + stayTime);
    }
}
