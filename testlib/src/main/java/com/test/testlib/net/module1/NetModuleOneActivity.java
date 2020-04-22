package com.test.testlib.net.module1;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acomlib.util.MLog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.testlib.R;
import com.test.testlib.net.module1.api.http.ApiService;
import com.test.testlib.net.module1.api.http.ApiServiceImpl;
import com.test.testlib.net.module1.api.http.ProgressObserver;
import com.test.testlib.net.module1.api.http.exception.ExceptionHandle;
import com.test.testlib.net.module1.api.http.progress.ObserverOnNextListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class NetModuleOneActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
//    private CompositeDisposable mCompositeDisposable;//多个请求一起取消

    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RetrofitClient.getInstance().init();
        setContentView(R.layout.activity_net1);
        initView();
        mAdapter = new ArticleAdapter(null);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDetailBean bean = mAdapter.getData().get(position);
                Uri uri = Uri.parse(bean.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int margin = getResources().getDimensionPixelSize(R.dimen.dp_10);
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = margin;
                    outRect.bottom = margin;
                } else {
                    outRect.top = 0;
                    outRect.bottom = margin;
                }
                outRect.left = margin;
                outRect.right = margin;
            }
        });
        onRefresh();
    }

    private void initView() {
        mRefreshLayout = findViewById(R.id.srl);
        mRecyclerView = findViewById(R.id.rv_list);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);
//        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        requestData();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreRequested() {
        requestData();
    }

    private void requestData() {
//        Disposable disposable =
                ApiServiceImpl.getApi(ApiService.class)
                .getArticleList(pageIndex)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver<ArticleBean>(NetModuleOneActivity.this, new ObserverOnNextListener<ArticleBean>() {
                    @Override
                    public void onNext(ArticleBean articleBean) {
                        List<ArticleDetailBean> list = articleBean.getDatas();
                        if (pageIndex == 0) {
                            mAdapter.setNewData(list);
                        } else if (articleBean.getCurPage() >= articleBean.getPageCount()) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.addData(list);
                            mAdapter.loadMoreComplete();
                        }
                        pageIndex = articleBean.getCurPage();
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        MLog.e(e.getCode() + e.getMessage());
                    }

                }));

//        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
//        mCompositeDisposable.dispose();
        super.onDestroy();
    }

}
