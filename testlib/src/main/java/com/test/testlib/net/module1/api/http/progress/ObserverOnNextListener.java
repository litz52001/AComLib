package com.test.testlib.net.module1.api.http.progress;


import com.test.testlib.net.module1.api.http.exception.ExceptionHandle;

public interface ObserverOnNextListener<T> {
    void onNext(T t);
    void onError(ExceptionHandle.ResponeThrowable e);
}

