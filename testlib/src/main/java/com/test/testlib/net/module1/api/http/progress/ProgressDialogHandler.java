package com.test.testlib.net.module1.api.http.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class ProgressDialogHandler extends Handler {
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;

    private Context context;
    private boolean isShow;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener
            mProgressCancelListener, boolean isShow) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.isShow = isShow;
    }

    private void initProgressDialog() {
        if(!isShow) return;

        if (pd == null) {
            pd = new ProgressDialog(context);
            pd.setMessage("正在加载中...");

            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);//只有手机返回按键可使得对话框消失
            pd.setOnKeyListener(onKeyListener);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mProgressCancelListener.onCancelProgress();
                }
            });

            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                mProgressCancelListener.onCancelProgress();
            }
            return false;
        }
    };


    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}

