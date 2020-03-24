package com.test.testlib;

import android.content.Context;

import com.acommonlibrary.BaseApp;



public class MyApp extends BaseApp {
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
//        Logger.addLogAdapter(new AndroidLogAdapter());

    }


    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public String setBuglyAPPID() {
        return "31098f9866";
    }

    @Override
    public boolean setBuglyStatus() {
        return true;
    }




}
