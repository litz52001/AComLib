package com.acommonlibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.acommonlibrary.BaseApp;
import com.acommonlibrary.util.ToastUtils;
import com.acommonlibrary.R;

/**
 *  信息提示框
 *  统一管理
 */
public class TipUtils {
    static ToastUtils toast = null;

    public static void showToast(String msg) {
        ToastUtils.showShortToastSafe(msg);
    }

    public static void showToast(int resId) {
        ToastUtils.showShortToastSafe(resId);
    }


    public static void showDialog(Context mContext, String context, boolean isSingleBtn,
                                  ACommomDialog.OnCloseListener onClick) {
        showDialog (mContext, "提示", context, isSingleBtn, onClick);
    }

    public static void showDialog(Context mContext, String context,
                                  ACommomDialog.OnCloseListener onClick) {
        showDialog (mContext, "提示", context, false, onClick);
    }

    public static void showDialog(Context mContext, String title, String context,
                                  ACommomDialog.OnCloseListener onClick) {
        showDialog (mContext, title, context, false, onClick);
    }

    static ACommomDialog dialog = null;

    public static void showDialog(Context mContext, String title, String context,
                                  boolean isSingle, final ACommomDialog.OnCloseListener onClick) {
        dialog = new ACommomDialog (mContext,
                R.style.basedialog,
                context,
                new ACommomDialog.OnCloseListener () {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (onClick != null)
                            onClick.onClick (dialog, confirm);

                        dialog.dismiss ();
                    }
                }).setTitle (title)
                .setSingleButton (isSingle);
        dialog.show ();
    }

    public static void dismissDialog() {
        if (dialog != null) dialog.dismiss ();
    }


}
