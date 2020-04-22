package com.test.testlib.net.module1.api.http;

import android.content.Context;


import com.acomlib.util.MLog;
import com.acomlib.util.NetworkUtils;
import com.acomlib.widget.TipUtils;
import com.test.testlib.net.module1.api.http.exception.ExceptionHandle;
import com.test.testlib.net.module1.api.http.progress.ObserverOnNextListener;
import com.test.testlib.net.module1.api.http.progress.ProgressCancelListener;
import com.test.testlib.net.module1.api.http.progress.ProgressDialogHandler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {
    private static final String TAG = "ProgressObserver";
    private ObserverOnNextListener listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable d;


    public ProgressObserver(Context context, ObserverOnNextListener listener) {
        this(context,true,listener);
    }

    public ProgressObserver(Context context, boolean isShowProgress, ObserverOnNextListener listener) {
        this.listener = listener;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, isShowProgress);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
//        MLog.e("onStart","MySubscriber.onStart()");
        //接下来可以检查网络连接等操作
        if (!NetworkUtils.isConnected()) {
            TipUtils.showToast("当前网络不可用，请检查网络情况");
            // 一定要主动调用下面这一句,取消本次Subscriber订阅
            if (!d.isDisposed()) {
                d.dispose();
            }
            onComplete();
            return;
        }
        showProgressDialog();
    }

    @Override
    public void onNext(T t) {
        listener.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        MLog.e (e.toString ());
        if(e instanceof Exception){
            //访问获得对应的Exception
            listener.onError(ExceptionHandle.handleException(e));
        }else {
            //将Throwable 和 未知错误的status code返回
            listener.onError(new ExceptionHandle.ResponeThrowable(e,ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onComplete() {
        MLog.d(TAG, "onComplete: ");
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅，同时当前网络请求取消
        if (!d.isDisposed()) {
            d.dispose();
        }
    }

}

