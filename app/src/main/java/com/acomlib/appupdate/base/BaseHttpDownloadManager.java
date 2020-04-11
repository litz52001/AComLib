package com.acomlib.appupdate.base;


import com.acomlib.appupdate.listener.OnDownloadListener;

/**
 * 描述:     TODO 下载管理者
 */

public abstract class BaseHttpDownloadManager {

    /**
     * 下载apk
     *
     * @param apkUrl   apk下载地址
     * @param apkName  apk名字
     * @param listener 回调
     */
    public abstract void download(String apkUrl, String apkName, OnDownloadListener listener);

    /**
     * 取消下载apk
     */
    public abstract void cancel();
}
