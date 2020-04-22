package com.test.testlib.net.module1.api.http;

import com.test.testlib.BuildConfig;
import com.test.testlib.net.module1.api.http.conver.MyConverterFactory;
import com.test.testlib.net.module1.api.http.conver.NullOnEmptyConverterFactory;
import com.test.testlib.net.module1.api.http.interceptor.InterceptorUtil;
import com.test.testlib.net.module1.api.http.interceptor.LogCusInterceptor;
import com.test.testlib.net.module1.api.utils.Constant;
import com.test.testlib.net.module1.api.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Description: Retrofit请求封装
 */
public class RetrofitClient {

    private static RetrofitClient mInstance;
    private Retrofit mRetrofit;

    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 20;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 20;
    public static final int ERITE_TIME_OUT = 20;

    private RetrofitClient() {
        init();
    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitClient.class) {
                if (mInstance == null) mInstance = new RetrofitClient();
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象与参数
     */
    private void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if(BuildConfig.DEBUG){
            builder.addInterceptor(InterceptorUtil.LogInterceptor());//添加日志拦截器
        }

        File cacheFile = new File(Constant.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.addInterceptor(new LogCusInterceptor());//日志打印和添加头部信息
        builder.cache(cache);

        builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)   //链接超时
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)//读取超时
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)//设置写入超时时间
                .retryOnConnectionFailure(true);//失败自动重连


        //初始化Retrofit并添加配置
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())//请求结果转换为基本类型
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MyConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                //这里是自定义的GsonConverterFactory
                .build();
    }

    public <T> T getApi(Class<T> clz) {
        return mRetrofit.create(clz);
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!SystemUtil.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (SystemUtil.isNetworkConnected()) {
                int maxAge = 0;
                // 有网络时, 不缓存, 最大保存时长为0
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    };

}
