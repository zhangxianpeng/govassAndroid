package com.lihang.selfmvvm.utils;

import android.content.Context;

import com.lihang.selfmvvm.MyApplication;

public class Utils {
    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Context getApp() {
        return MyApplication.getContext();
    }
}
