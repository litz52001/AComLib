package com.acommonlibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acommonlibrary.R;


public class ACommomDialog extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private LinearLayout cancelLL;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private boolean isSingle=false;

    public ACommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ACommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public ACommomDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected ACommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public ACommomDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public ACommomDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public ACommomDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    //只显示确认按钮
    public ACommomDialog setSingleButton(boolean isSingle){
        this.isSingle = isSingle;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);

        // 将对话框的大小按屏幕大小的百分比设置
        WindowManager windowManager = ((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.8); //设置宽度
        this.getWindow().setAttributes(lp);

        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        cancelLL = (LinearLayout) findViewById(R.id.cancelLL);

        if(TextUtils.isEmpty (content))
            content = "您确定删除此信息？";

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

        if(isSingle){
            this.cancelLL.setVisibility (View.GONE);
            this.setCancelable(false);
        }else{
            this.cancelLL.setVisibility (View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            if (listener != null) {
                listener.onClick(this, false);
            }
            this.dismiss();
        } else if (id == R.id.submit) {
            if (listener != null) {
                listener.onClick(this, true);
            }
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }


}
