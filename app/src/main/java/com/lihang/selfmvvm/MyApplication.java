package com.lihang.selfmvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lihang.selfmvvm.bean.User;
import com.lihang.selfmvvm.launchstater.TaskDispatcher;
import com.lihang.selfmvvm.launchstater.mytasks.SmartRefreshLayoutTask;
import com.lihang.selfmvvm.launchstater.mytasks.X5WebTask;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.crash.CrashHandler;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.smtt.sdk.QbSdk;

import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_TOKEN;

public class MyApplication extends Application {

    private static MyApplication context;
    private static User loginUser;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.blue_gray, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //捕获崩溃日志，位置在外部存储的LianSou
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        context = this;
        TaskDispatcher.init(this);
        TaskDispatcher dispatcher = TaskDispatcher.createInstance();
        dispatcher.addTask(new SmartRefreshLayoutTask())
                .addTask(new X5WebTask())
                .start();

        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e("snow", "========onCoreInitFinished===");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.e("snow", "x5初始化结果====" + b);
            }
        });

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

    public static String getToken() {
        return (String) PreferenceUtil.get(USER_LOGIN_TOKEN, "");
    }

}
