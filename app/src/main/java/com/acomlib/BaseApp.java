package com.acomlib;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public abstract class BaseApp extends Application {
    private static Context mContext;
    private RefWatcher mRefWatcher;
    public static Typeface iconfont;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName();//ProcessUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, setBuglyAPPID(), setBuglyStatus(), strategy);

        if(setLeakCanaryStatus()){
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            mRefWatcher = LeakCanary.install(this);
        }

    }

    public static Context getAppContext() {
        return mContext;
    }

    public static Typeface getIconfont(Context context) {
        if (iconfont != null) {
            return iconfont;
        } else {
            iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        }
        return iconfont;
    }

    /**
     * 设置BuglyAPPID
     */
    public abstract String setBuglyAPPID();

    /**
     * 设置Bugly调试状态 发布需要设置为false
     */
    public abstract boolean setBuglyStatus();

    /**
     * 是否监控内存泄漏
     * @return
     */
    public abstract boolean setLeakCanaryStatus();
    /**
     *  Fragment he activity 的 onDestroy ()使用
     *
     * RefWatcher refWatcher = ExampleApplication.getRefWatcher(getActivity());
     * refWatcher.watch(this);
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        BaseApp application = (BaseApp) context.getApplicationContext();
        return application.mRefWatcher;
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
