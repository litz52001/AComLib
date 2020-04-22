package com.test.testlib.net.module1.api.http;


import com.test.testlib.net.module1.ArticleBean;
import com.test.testlib.net.module1.api.http.conver.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Description: api
 */
public interface ApiService {

    String BASE_URL = "https://www.wanandroid.com";
    /**
     * 获取首页文章列表
     * @param pageIndex pageIndex
     * @return Observable
     */
    @GET("/article/list/{pageIndex}/json")
    Observable<ArticleBean> getArticleList(@Path("pageIndex") int pageIndex);

    @GET("/article/list/{pageIndex}/json")
    Observable<BaseResponse<ArticleBean>> getArticleList1(@Path("pageIndex") int pageIndex);

}
