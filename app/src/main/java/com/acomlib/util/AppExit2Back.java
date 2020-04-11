package com.acomlib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.acomlib.widget.TipUtils;
import com.acomlib.R;

/**
 * 退出App程序应用
 */
public class AppExit2Back {

    private static long firstTime=0;
    public static void exitApp(Context context) {
        long secondTime=System.currentTimeMillis();
        if(secondTime-firstTime>2000){
            firstTime=secondTime;
            TipUtils.showToast(R.string.sys_exit_tip);

        } else {
            AppActivityMgr.getScreenManager().removeAllActivity();
            //创建ACTION_MAIN
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Context content = ((Activity) context);
            //启动ACTION_MAIN
            content.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}