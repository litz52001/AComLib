package com.acomlib.util;

import android.os.Environment;
import android.util.Log;

import com.acomlib.BaseApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 模仿logger，创建自己的日志操作类
 * 前提是必须继承BaseApp
 */
public class MLog {
    public static final String TAG = MLog.class.getSimpleName();

    private static String dir = null;
    //日志写入文件
    private static boolean log2File = false;
    /**  控制变量，是否显示log日志 */
    public static boolean isPrintLog = true;

    public static String defaultMsg = "";
    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;
    public static final int P = 6;//System.out.println(msg);



    /**
     *  初始化控制变量
     * @param isShowLog
     */
    public static void init(boolean isShowLog) {
        MLog.isPrintLog = isShowLog;
    }

    /**
     * 初始化控制变量和默认日志
     * @param isShowLog 默认 true
     * @param log2File  默认 false
     */
    public static void init(boolean isShowLog, boolean log2File) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = BaseApp.getAppContext().getExternalCacheDir().getPath() + File.separator + "log" + File.separator;
        } else {
            dir = BaseApp.getAppContext().getCacheDir().getPath() + File.separator + "log" + File.separator;
        }

        MLog.isPrintLog = isShowLog;
        MLog.log2File = log2File;
    }

    public static String getLogCacheDir(){
        return dir;
    }

    public static void v() {
        llog(V, null, defaultMsg);
    }

    public static void v(Object obj) {
        llog(V, null, obj);
    }

    public static void v(String tag, Object obj) {
        llog(V, tag, obj);
    }

    public static void d() {
        llog(D, null, defaultMsg);
    }

    public static void d(Object obj) {
        llog(D, null, obj);
    }

    public static void d(String tag, Object obj) {
        llog(D, tag, obj);
    }

    public static void i() {
        llog(I, null, defaultMsg);
    }

    public static void i(Object obj) {
        llog(I, null, obj);
    }

    public static void i(String tag, String obj) {
        llog(I, tag, obj);
    }

    public static void w() {
        llog(W, null, defaultMsg);
    }

    public static void w(Object obj) {
        llog(W, null, obj);
    }

    public static void w(String tag, Object obj) {
        llog(W, tag, obj);
    }

    public static void e() {
        llog(E, null, defaultMsg);
    }

    public static void e(Object obj) {
        llog(E, null, obj);
    }

    public static void e(String tag, Object obj) {
        llog(E, tag, obj);
    }

    public static void sysPrint(Object obj) {
        llog(P, null, obj);
    }

    public static void sysPrint(String tag, Object obj) {
        llog(P, tag, obj);
    }



    /**
     * 执行打印方法
     * @param type
     * @param tagStr
     * @param obj
     */
    private static void llog(int type, String tagStr, Object obj) {
        String msg;
        if (!isPrintLog) {
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        if(stackTrace.length <= 4)
            index = stackTrace.length-1;

        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        if (obj == null) {
            msg = "Log with null Object";
        } else {
            msg = obj.toString();
        }
        if (msg != null) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();

        switch (type) {
            case V:
                Log.v(tag, logStr);
                break;
            case D:
                Log.d(tag, logStr);
                break;
            case I:
                Log.i(tag, logStr);
                break;
            case W:
                Log.w(tag, logStr);
                break;
            case E:
                Log.e(tag, logStr);
                break;
            case P:
                System.out.println(tag+" : "+logStr);
                break;
        }
        if(log2File){
            log2File('e',tag,logStr);
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param type    日志类型
     * @param tag     标签
     * @param content 内容
     **/
    private synchronized static void log2File(final char type, final String tag, final String content) {
        if (content == null) return;
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        final String fullPath = dir + date + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        final String dateLogContent = time + ":" + type + ":" + tag + ":" + content + '\n';
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(dateLogContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(bw);
                }
            }
        }).start();
    }

    /**
     * 输出json格式数据
     * 支持 String 、List 、Map 、Object
     * @param
     */
    public static void json(Object obj) {
        String msg = "";
        if (obj == null) {
            msg = "Log with null Object";
        }else if(obj instanceof String){
            if(JsonUtils.isJson(obj.toString())){
                msg = obj.toString();
            }else{
                msg = "parameters is not json String : "+obj.toString();
            }
        }else if (obj.getClass().isArray()){
            msg = JsonUtils.list2json((List<?>) obj);
        }else if(obj instanceof Map){
            msg = JsonUtils.map2json((Map) obj);
        }else{
            msg = JsonUtils.toJson(obj);
        }
        String jsonStr = JsonFormatTool.formatJson(msg);
        llog(E, null, jsonStr);
    }

}

