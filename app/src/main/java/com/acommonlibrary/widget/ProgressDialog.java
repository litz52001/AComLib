package com.acommonlibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.acommonlibrary.R;

public class ProgressDialog extends Dialog {

    private static ProgressDialog pro;

    private ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);

        // 将对话框的大小按屏幕大小的百分比设置
//        WindowManager windowManager = ((Activity)mContext).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        lp.width = (int)(display.getWidth() * 0.8); //设置宽度
//        this.getWindow().setAttributes(lp);

    }


//    @Override
//    public void onBackPressed() {
////        super.onBackPressed ();
//    }

    public static void showPro(Context context){
        if(pro == null)
            pro = new ProgressDialog (context);
        pro.show ();
    }

    public void dismissPro(){
        if(pro != null)
            pro.dismiss ();
    }

}
