package com.test.testlib.net.module1.api.http.interceptor;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * *@description 拦截器工具类
 */

public class InterceptorUtil {

    public static String TAG="----";
    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor(){
        return new HttpLoggingInterceptor (new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                Log.w(TAG, "log: "+message );//原打印方式
                if(!TextUtils.isEmpty(message)){
                    if(isJson(message)){
                        Logger.json(message);
                    }else if(!message.contains("Content") && !message.contains("Transfer-Encoding")
                            && !message.contains("END")&& !message.contains("Date")&& !message.contains("Set-Cookie")){
                        Logger.d(message);
                    }
                }
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    public static Interceptor HeaderInterceptor(){
        return   new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request mRequest=chain.request();
                //在这里你可以做一些想做的事,比如token失效时,重新获取token
                //或者添加header等等,PS我会在下一篇文章总写拦截token方法
                return chain.proceed(mRequest);
            }
        };
    }


    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


}
