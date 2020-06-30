package com.lihang.selfmvvm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.launchstater.TaskDispatcher;
import com.lihang.selfmvvm.launchstater.mytasks.SmartRefreshLayoutTask;
import com.lihang.selfmvvm.launchstater.mytasks.X5WebTask;
import com.lihang.selfmvvm.utils.PreferenceUtil;

public class MyApplication extends Application {

    private static MyApplication context;
    private static User loginUser;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //捕获崩溃日志，位置在外部存储的LianSou
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        context = this;
        TaskDispatcher.init(this);
        TaskDispatcher dispatcher = TaskDispatcher.createInstance();
        dispatcher.addTask(new SmartRefreshLayoutTask())
                .addTask(new X5WebTask())
                .start();

    }

    public static User getLoginUser() {
        if (loginUser == null) {
            loginUser = PreferenceUtil.getByClass("user", User.class);
        }
        return loginUser;
    }

    public static void updateUser(User user) {
        PreferenceUtil.putByClass("user", user);
        loginUser = user;
    }

    public static void logOut() {
        loginUser = null;
        PreferenceUtil.clearByClass(User.class);
    }


    public static Context getContext() {
        return context;
    }
}
